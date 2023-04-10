<template>
  <div>
    <div class="signature">参与人署名：{{userInfo.name}}</div>
    <vxe-table
      border
      :scroll-y="{ enabled: isDisplay }"
      :span-method="rowspanMethod"
      :data="tableData"
    >
      <vxe-column field="name1" title="毕业生能力要求">
        <template slot-scope="scope">
          <my-com
            :parameter="scope.row"
            :coltitle="'毕业生能力要求'"
            :scope="scope"
            :col="1"
          ></my-com>
        </template>
      </vxe-column>
      <vxe-column field="name2" title="毕业生能力要求指标点">
        <template slot-scope="scope">
          <my-com
            :parameter="scope.row"
            :col="2"
            :coltitle="'毕业生能力要求指标点'"
            :scope="scope"
          ></my-com>
        </template>
      </vxe-column>
    </vxe-table>
    <div>
      <el-button  v-if="isShowEdit"  style="margin-top: 12px" type="primary" @click="toSave"
        >保存</el-button
      >
      <el-button style="margin-top: 12px"   @click="toNext">下一步</el-button>
    </div>
  </div>
</template>

<script>
import MyCom from "./indica/myCom.vue";
import Vue from "vue";
import XEUtils, { remove } from "xe-utils";
import api from "@/api/subject/coremodel/coremodel.js";
Vue.prototype.$eventBus = new Vue();
export default {
  components: {
    MyCom,
  },
  data() {
    return {
      treeData: [],
      // newTreeData: "",
      tableData: [],
      isDisplay: true,
      modelInfo: {},
      userInfo:JSON.parse(sessionStorage.getItem('user')) 
    };
  },
  props: {
    params:   Object,
    next:  Function,
    isShowEdit: Boolean
  },
  created() {
    //加载数据
    this.list();
  },

  mounted() {
    //消息主题
    this.$eventBus.$on("updownEvent", (param) => {
      let col = param.col;
      let optId = param.optId;
      let idArr = [];
      for (let i = 0; i < col; i++) {
        idArr.push(optId.substr(i * 2, 2) * 1 - 1);
      }
      //当前的序号
      let idx = idArr[idArr.length - 1];
      let optArr = this.findNode(optId, idArr);

      //向上还是向下
      if (param.isUp > 0) {
        //向下
        if (idx < optArr.length - 1) {
          //有效性:不是最后一个
          //ES6解构换位
          [optArr[idx].id, optArr[idx + 1].id] = [
            optArr[idx + 1].id,
            optArr[idx].id,
          ];
          [optArr[idx].keys, optArr[idx + 1].keys] = [
            optArr[idx + 1].keys,
            optArr[idx].keys,
          ];

          [optArr[idx], optArr[idx + 1]] = [optArr[idx + 1], optArr[idx]];
          this.updtChldrenIds(optArr[idx]);
          this.updtChldrenIds(optArr[idx + 1]);
        }
      } else {
        //向上
        if (idx > 0) {
          //有效性:不是第一个
          [optArr[idx - 1].id, optArr[idx].id] = [
            optArr[idx].id,
            optArr[idx - 1].id,
          ];
          [optArr[idx - 1].keys, optArr[idx].keys] = [
            optArr[idx].keys,
            optArr[idx - 1].keys,
          ];

          [optArr[idx - 1], optArr[idx]] = [optArr[idx], optArr[idx - 1]];
          this.updtChldrenIds(optArr[idx - 1]);
          this.updtChldrenIds(optArr[idx]);
        }
      }

      this.toColTreeData(this.treeData);
    });

    this.$eventBus.$on("rmvEvent", (optId) => {
      let col = optId.length / 2;
      let idArr = [];
      for (let i = 0; i < col; i++) {
        idArr.push(optId.substr(i * 2, 2) * 1 - 1);
      }
      let optArr = this.findNode(optId, idArr);

      let idx = idArr[idArr.length - 1];

      optArr.splice(idx, 1);

      //下面的id-1
      this.updtlittleBrotherIds(optArr, idx, -1);

      this.toColTreeData(this.treeData);
    });

    // 消息主题  接收  editEvent事件
    this.$eventBus.$on("editEvent", (data) => {
      //处理id
      let idArr = [];
      let optId = data.parameter[`id${data.col}`];
      for (let i = 0; i < data.col; i++) {
        idArr.push(optId.substr(i * 2, 2) * 1 - 1);
      }
      let optArr = this.findNode(optId, idArr);
      //当前的序号
      let idx = idArr[idArr.length - 1];
      optArr[idx].name = data.parameter[`name${data.col}`];

      this.toColTreeData(this.treeData);
    });

    this.$eventBus.$on("addEvent", (data) => {

      //添加课程内容会把添加课程的data覆盖，要判空
      if ( data.parameter.cType ) {
        this.cType = data.parameter.cType;
      }
      //新增节点前面每个col的中数组的序号
      let idArr = []; //01   01   02  010103
      //当前被操作的id
      let optId = data.parameter[`id${data.col}`];
      //被操作的父id
      let pid;
      if (data.col == 1) {
        pid = "";
      } else {
        pid = data.parameter[`id${data.col - 1}`];
      }

      if (!optId) {
        //本级的第一个节点
        optId = pid + "01";
      }

      for (let i = 0; i < data.col; i++) {
        //每个原始的id  对应  col的中数组   的序号
        idArr.push(optId.substr(i * 2, 2) * 1 - 1);
      }

      if (data.col == 1) {
        this.addNode(this.treeData, idArr[0], data.parameter.name1, "0");
      } else if (data.col == 2) {
        this.addNode(
          this.treeData[idArr[0]].children,
          idArr[1],
          data.parameter.name2,
          pid
        );
      }   
      //重绘table
       this.toColTreeData(this.treeData);
    });
  },
  methods: {
    toNext(){
       this.toSave();
       this.next()
    },
    toSave() {

      this.params.step5[0].byszbd = this.treeData;
      // console.log(this.params,"新params");
      this.$emit("child");
      // console.log("step5'byszbd'保存");
    },
    findNode(optId, idArr) {
      if (optId.length == 2) {
        return this.treeData;
      } else if (optId.length == 4) {
        return this.treeData[idArr[0]].children;
      } 
    },
    updtChldrenIds(node) {
      //更新子孙节点的id和parentId
      let arr = node.children;

      if (arr.length > 0) {
        let lodeSon;
        let col;
        for (let i = 0; i < arr.length; i++) {
          lodeSon = arr[i];
          lodeSon.parentId = node.id;
          lodeSon.id = node.id + lodeSon.id.substr(node.id.length);
          //维护keys
          col = lodeSon.keys.length;
          lodeSon.keys = [];
          for (let j = 0; j < col; j++) {
            lodeSon.keys.push(lodeSon.id.substr(0, (j + 1) * 2));
          }
          this.updtChldrenIds(arr[i]);
        }
      }
    },
    updtlittleBrotherIds(arrs, idx, isAdd) {
      //更新弟弟们的id
      for (let i = idx; i < arrs.length; i++) {
        let node = arrs[i];
        let frst0 = node.id.substr(0, 1) == "0" ? "0" : "";
        let id = node.id * 1;
        node.id = frst0 + (id + isAdd);

        //更新keys
        let col = node.keys.length;
        node.keys = [];
        for (let i = 0; i < col; i++) {
          node.keys.push(node.id.substr(0, (i + 1) * 2));
        }

        this.updtChldrenIds(node);
      }
    },
    addNode(arrs, idx, val, pid) {
      //新增节点
      let newId = idx + 1;

      //后面的id+1,挪位置
      this.updtlittleBrotherIds(arrs, newId, 1);

      if (arrs.length > 0) {
        //首节点加在点击的位置，其他节点加在下一个位置
        newId++;
      }

      let strNewId = "0";
      if (pid != "0") {
        strNewId = idx < 9 ? pid + "0" + newId : pid + newId;
      } else {
        // 第一级
        strNewId = idx < 9 ? "0" + newId : "" + newId;
      }
       arrs.push({ id: strNewId, parentId: pid, name: val, children: [] });
      //根据id重新排序
      if (arrs.length > 1) {
        //只有一个节点的话不要排序
        arrs.sort((a, b) => a.id * 1 - b.id * 1);
      }
 
    },
    list() {
      this.treeData = this.params.step5[0].byszbd
      this.toColTreeData(this.treeData);
      // 渲染table
    },
    //vxetable文档自带，渲染table用
    toColTreeData(treeData) {
      if (treeData == null || typeof treeData == undefined || treeData == "") {
        treeData = [
          {
            id: "01",
            parentId: "0",
            name: "",
            children: [],
          },
        ];
      }
      const options = { children: "children" };
      let list = [];
      let keyMap = {};
      XEUtils.eachTree(
        treeData,
        (item, index, result, paths, parent) => {
          keyMap[item.id] = item;
          item.keys = parent ? parent.keys.concat([item.id]) : [item.id];
          //最后一级
          if (!item.children || !item.children.length) {
             //生成1-1.  格式的序号
            let row = { idx :item.id.substr(0,2)*1+ "-"+item.id.substr(2)*1 };
            item.keys.forEach((key, index) => {
              let level = index + 1;
              let obj = keyMap[key];
              row[`id${level}`] = obj.id;
              row[`name${level}`] = obj.name;
            });
            list.push(row);
          }
        },
        options
      );
      this.keyMap = keyMap;
      this.tableData = list;
      //console.log(this.tableData, "this.tableData");
    },
    // 通用行合并函数（将相同多列数据合并为一行）
    rowspanMethod({ row, _rowIndex, column, visibleData }) {
      const fields = [
        "name1",
        "name2",
        "name3",
        "name4",
        "name5",
        "name6",
        "name7",
      ];
      const cellValue = row[column.property];
      if (cellValue && fields.includes(column.property)) {
        const prevRow = visibleData[_rowIndex - 1];
        let nextRow = visibleData[_rowIndex + 1];
        if (prevRow && prevRow[column.property] === cellValue) {
          return { rowspan: 0, colspan: 0 };
        } else {
          let countRowspan = 1;
          while (nextRow && nextRow[column.property] === cellValue) {
            nextRow = visibleData[++countRowspan + _rowIndex];
          }
          if (countRowspan > 1) {
            return { rowspan: countRowspan, colspan: 1 };
          }
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped="scoped">
.top {
  display: flex;
  justify-content: space-between;
}
/deep/ .signature {
  // padding:100px 0 !important;
  margin: 10px 0;
}
</style>
<style></style>
