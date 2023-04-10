<template>
  <div>
    <h1>{{ messageInfo.name }}-{{ messageInfo.year }}-专业支撑能力</h1>
    <div class="operator">
      <el-button type="primary" @click="toSupoportCoursePlan()"
       v-if="!isDisplay" >查看课时安排</el-button
      >
      <el-button type="primary"            v-has-permission-code-and="'10000800015'"       v-if="!isDisplay" @click="toCopy()">清除当前专家意见</el-button>
      <el-button type="warning" @click="save()">{{ isDisplay ? "保存" : "修改" }}</el-button>
      <el-button type="info"   @click="returnSupport()">返回</el-button>
    </div>
    <div  v-if="false"  class="signature">参与人署名：{{userInfo.name}}</div>

    <vxe-table
      border
      :scroll-y="{ enabled: false }"
      :span-method="rowspanMethod"
      :data="tableData"
    >
      <vxe-column field="name1" title="课程名称">
        <template slot-scope="scope">
          <my-com
            :display="isDisplay"
            :parameter="scope.row"
            :coltitle="'课程名称'"
            :col="1"
            :scope="scope"
            :messageId="messageInfo.id"
          ></my-com>
        </template>
      </vxe-column>
      <vxe-column field="name2" title="课程内容">
        <!-- <template slot-scope="scope">
          <my-com
            v-show="scope.row.id1"
            :display="false"
            :parameter="scope.row"
            :coltitle="'课程内容'"
            :col="2"
            :scope="scope"
          ></my-com>
        </template> -->
      </vxe-column>
      <vxe-column field="name3" title="学时（实践学时）">
        <template slot-scope="scope">
          <my-com
            v-show="scope.row.id1"
            :display="false"
            :parameter="scope.row"
            :coltitle="'学时（实践学时）'"
            :col="3"
            :scope="scope"
          ></my-com>
        </template>
      </vxe-column>
    </vxe-table>
    <supoport-course-plan ref="supoportCoursePlan"></supoport-course-plan>
  </div>
</template>

<script>
import MyCom from "@/components/myCom2/myCom.vue";
import supoportCoursePlan from "./supoportCoursePlan.vue";
import Vue from "vue";
import XEUtils, { remove } from "xe-utils";
// import apiy from "@/api/subject/subjecttypes/subjecttypes.js";
import api from "@/api/subject/coremodel/coremodel.js";

Vue.prototype.$eventBus = new Vue();

export default {
  components: {
    MyCom,
    supoportCoursePlan,
  },
  props: {
    messageInfo: Object,
  },
  data() {
    return {
      treeData: [],
      cType: "",
      tableData: [],
      isDisplay: false,
      userInfo:JSON.parse(sessionStorage.getItem('user')) 

    };
  },
  created() {
    // this.treeData = JSON.parse(this.messageInfo.zcmodel);
    //加载数据
    this.list();
  },
  watch: {
    messageInfo(val) {
      this.treeData = JSON.parse(val.model.zcmodel);
    },
  },
  mounted() {
    //消息主题
    this.$eventBus.$on("updownEvent2", (param) => {
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

    this.$eventBus.$on("rmvEvent2", (optId) => {
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
    this.$eventBus.$on("editEvent2", (data) => {

      //处理id
      let idArr = [];
      let optId = data.parameter[`id${data.col}`];
      for (let i = 0; i < data.col; i++) {
        idArr.push(optId.substr(i * 2, 2) * 1 - 1);
      }
      let optArr = this.findNode(optId, idArr);
      console.log(optArr)
      //当前的序号
      let idx = idArr[idArr.length - 1];
     //更新第一列  name
      optArr[idx].name = data.parameter.name1;
      //更新第2列  目标
      optArr[idx].children[0].name = data.parameter.name2;
      //更新第3列  课时
      optArr[idx].children[0].children[0].name = data.input2 + "(实操" + data.input3 + ")"

      this.toColTreeData(this.treeData);
    });

     this.$eventBus.$on("addEvent2", (data) => {
      
      console.log( data  )
      
      if (this.treeData.length != 0) {
        if (this.treeData[0].name == "") {
          this.$eventBus.$emit("rmvEvent2", this.treeData[0].id);
        }
      }
      //添加课程内容会把添加课程的data覆盖，要判空
      if (data.parameter.cType) {
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
        
        let newId =  this.addNode(this.treeData, idArr[0], data.parameter.name1, pid);
        
        //加第2列3列
         if (newId=='01') {
          this.addNode( this.treeData[idArr[0]].children,  1, data.parameter.name2+ "-"+data.input2 +   "(实操" + data.input3 +  ")",  newId );
         } else {
          this.addNode( this.treeData[idArr[0]+1].children,  1, data.parameter.name2+ "-"+data.input2 +   "(实操" + data.input3 +  ")", newId );
         }
      } 
      //重绘table
      this.toColTreeData(this.treeData);
    });
  },
  methods: {
    //去专业支撑能力课时安排
    toSupoportCoursePlan() {
      this.$refs.supoportCoursePlan.open(this.treeData, "查看课时安排");
    },
    save() {
      if (!this.isDisplay) {
        this.isDisplay = true;
      } else {
        this.isDisplay = false;
      let param = {
        id: this.messageInfo.gid,
        zcmodel: JSON.stringify(this.treeData),
      };
      api.update(param, (res) => {
        this.$utils.msg.success(res.msg);
        this.$parent.list();
      });
      };
    },
    returnSupport() {  this.$emit("returnSupport");   },
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
     

      if (strNewId.length == 4) {
        //第2个节点
        // val = "课程名称-32(6)"
        let varArr = val.split("-");
        let val8Arr = varArr[1].split("(实操");
        let cntAll = val8Arr[0];
        let cntSx = val8Arr[1].substr(0, val8Arr[1].length - 1);
        arrs.push({
          id: strNewId,
          parentId: pid,
          name: varArr[0],
          cType: this.cType,
          all: cntAll,
          sx: cntSx,
          children: [
            {
              id: strNewId + "01",
              parentId: strNewId,
              name: varArr[1],
              children: [],
            },
          ],
        });
      } else {
        arrs.push({
          id: strNewId,
          parentId: pid,
          name: val,
          cType: this.cType,
          children: [],
        });
      }

      //根据id重新排序
      if (arrs.length > 1) {
        //只有一个节点的话不要排序
        arrs.sort((a, b) => a.id * 1 - b.id * 1);
      }
      return strNewId;
    },
    list() {
      // 前端去后端取回数据
      this.$nextTick(() => {
        if (
          this.messageInfo.model.zcmodel == null ||
          typeof this.messageInfo.model.zcmodel == undefined ||
          this.messageInfo.model.zcmodel == ""
        ) {
          this.treeData = [
            {
              id: "01",
              parentId: "0",
              name: "",
              children: [],
            },
          ];
        } else {
          this.treeData = JSON.parse(this.messageInfo.model.zcmodel);
        }
        this.toColTreeData(this.treeData);
      });
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
          if (!item.children || !item.children.length) {
            let row = {};
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
    },
    // 通用行合并函数（将相同多列数据合并为一行）
    rowspanMethod({ row, _rowIndex, column, visibleData }) {
      const fields = ["name1"];
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

<style lang="scss" scoped>
.header {
  width: 100%;
  height: 50px;
}
/deep/ .signature {
  // padding:100px 0 !important;
  margin: 10px 0;
}
</style>
