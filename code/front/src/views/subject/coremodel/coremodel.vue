<template>
  <div>
    <div v-if="!isSupport && !isOther">
      <h1>{{ subjectInfo.name }}-{{ subjectInfo.year }}</h1>
      <div class="operator top">
        <div>
          <el-button type="primary" v-if="!isDisplay" @click="toSupport()"
            >专业支撑能力</el-button
          >
          <el-button type="primary" v-if="!isDisplay" @click="toOther()"
            >其它能力</el-button
          >
        </div>
        <div>
          <el-button type="primary" v-if="!isDisplay" @click="toAllCoursePlan()"
            >查看全部课时</el-button
          >
          <el-button type="primary" @click="toCoreCoursePlan()"
            >查看专业课时</el-button
          >
        
          <el-button type="danger"   v-has-permission-code-and="'10000800015'"  v-show="isShowEdit" v-if="!isDisplay" @click="toCopy()"
            >清除当前专家意见</el-button
          >
          <el-button type="warning"  v-show="isShowEdit"  @click="editList()">
            {{ isDisplay ? "保存" : "修改" }}
          </el-button>
          <el-button type="success"  v-show="isShowEdit"  v-if="!isDisplay" @click="toExport()"
            >导出</el-button
          >


          <el-button type="info" @click="returnList()">返回</el-button>
        </div>
      </div>
      <div v-if="false" class="signature">参与人署名：{{userInfo.name}}</div>
      <vxe-table
        border
        :scroll-y="{ enabled: isDisplay }"
        :span-method="rowspanMethod"
        :data="tableData"
      >
        <vxe-column field="name1" title="职业标准">
          <template slot-scope="scope">
            <my-com
              :display="isDisplay"
              :parameter="scope.row"
              :coltitle="'职业标准'"
              :scope="scope"
              :col="1"
            ></my-com>
          </template>
        </vxe-column>
        <vxe-column field="name2" title="态度和职业素养">
          <template slot-scope="scope">
            <my-com
              :display="isDisplay"
              :parameter="scope.row"
              :col="2"
              :coltitle="'态度和职业素养'"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
        <vxe-column field="name3" title="主要工作任务">
          <template slot-scope="scope">
            <my-com
              v-show="scope.row.id2"
              :display="isDisplay"
              :parameter="scope.row"
              :col="3"
              :coltitle="'主要工作任务'"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
        <vxe-column field="name4" title="具体工作内容">
          <template slot-scope="scope">
            <my-com
              v-show="scope.row.id3"
              :display="isDisplay"
              :parameter="scope.row"
              :coltitle="'具体工作内容'"
              :col="4"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
        <vxe-column field="name5" title="技能点">
          <template slot-scope="scope">
            <my-com
              v-show="scope.row.id4"
              :display="isDisplay"
              :parameter="scope.row"
              :coltitle="'技能点'"
              :col="5"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
        <vxe-column field="name6" title="知识点">
          <template slot-scope="scope">
            <my-com
              v-show="scope.row.id5"
              :display="isDisplay"
              :parameter="scope.row"
              :coltitle="'知识点'"
              :col="6"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
        <vxe-column field="name7" title="所属课程">
          <template slot-scope="scope">
            <my-com
              v-show="scope.row.id6"
              :display="isDisplay"
              :parameter="scope.row"
              :coltitle="'所属课程'"
              :col="7"
              :id="subjectInfo.id"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
        <vxe-column field="name8" title="课时">
          <template slot-scope="scope">
            <my-com
              v-show="scope.row.id6"
              :display="isDisplay"
              :parameter="scope.row"
              :id="messageInfo.id"
              :coltitle="'课时'"
              :col="8"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
    <supoport-ability
      v-if="isSupport"
      @returnSupport="returnSupport"
      :messageInfo="messageInfo"
    ></supoport-ability>

    <other-ability
      v-if="isOther"
      @returnOther="returnOther"
      :messageInfo="messageInfo"
    ></other-ability>

    <all-course-plan ref="allCoursePlan"></all-course-plan>
    <core-course-plan ref="coreCoursePlan"></core-course-plan>
  </div>
</template>

<script>
import MyCom from "../../../components/myCom/myCom.vue";

import Vue from "vue";
import coreCoursePlan from "./coreCoursePlan.vue";
import XEUtils, { remove } from "xe-utils";

import supoportAbility from "./supoportAbility.vue";
import otherAbility from "./otherAbility.vue";

import allCoursePlan from "./allCoursePlan.vue";

import api from "@/api/subject/coremodel/coremodel.js";
import apiExprt from "@/api/exprt/exprt.js";

Vue.prototype.$eventBus = new Vue();
export default {
  components: {
    MyCom,
    supoportAbility,
    otherAbility,
    allCoursePlan,
    coreCoursePlan,
  },
  props: {
    messageId: String,
    messageName: String,
    messageYear: String,
    selectId: String,
    isShowEdit: Boolean
  },
  data() {
    return {
      treeData: [],
      // newTreeData: "",
      tableData: [],
      isDisplay: false,
      isSupport: false,
      isOther: false,
      modelInfo: {},
      modelId: "",
      messageInfo: {
        id: "",
        gid: "",
        name: "",
        year: "",
        model: {},
      },
      subjectInfo: {
        id: "",
        name: "",
        year: "",
      },
			userInfo:JSON.parse(sessionStorage.getItem('user')) 
    };
  },

  created() {
    this.subjectInfo.id = this.messageId;
    this.subjectInfo.name = this.messageName;
    this.subjectInfo.year = this.messageYear;
    this.currentId = this.selectId;
    //加载数据
    this.list();
  },
  watch: {
    messageId(val) {
      this.subjectInfo.id = val;
    },
    messageName(val) {
      this.subjectInfo.name = val;
    },
    messageYear(val) {
      this.subjectInfo.year = val;
    },
    selectId(val) {
      this.currentId = val;
    },
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

      console.log("editEvent", data );

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

      if (data.col == 7) {
        // 修改了
        optArr[0].cType=data.parameter.cType;
        optArr[0].all=data.input2;
        optArr[0].sx=data.input3;
        optArr[0].children[0].cntAll = data.input2;
        optArr[0].children[0].cntSx = data.input3;
        optArr[0].children[0].name = data.input2 + "(实操" + data.input3 + ")";
      }

      this.toColTreeData(this.treeData);
    });

    this.$eventBus.$on(
      "addEvent",
      (data) => {
        console.log("saveEvent", data );

        if (this.treeData.length != 0) {
          if (this.treeData[0].name == "") {
            this.$eventBus.$emit("rmvEvent", this.treeData[0].id);
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
          this.addNode(this.treeData, idArr[0], data.parameter.name1, "0");
        } else if (data.col == 2) {
          this.addNode(
            this.treeData[idArr[0]].children,
            idArr[1],
            data.parameter.name2,
            pid
          );
        } else if (data.col == 3) {
          this.addNode(
            this.treeData[idArr[0]].children[idArr[1]].children,
            idArr[2],
            data.parameter.name3,
            pid
          );
        } else if (data.col == 4) {
          this.addNode(
            this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
              .children,
            idArr[3],
            data.parameter.name4,
            pid
          );
        } else if (data.col == 5) {
          this.addNode(
            this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
              .children[idArr[3]].children,
            idArr[4],
            data.parameter.name5,
            pid
          );
        } else if (data.col == 6) {
          this.addNode(
            this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
              .children[idArr[3]].children[idArr[4]].children,
            idArr[5],
            data.parameter.name6,
            pid
          );
        } else if (data.col == 7) {
          this.addNode(
            this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
              .children[idArr[3]].children[idArr[4]].children[idArr[5]]
              .children,
            idArr[6],
            data.parameter.name7 +
              "-" +
              data.input2 +
              "(实操" +
              data.input3 +
              ")",
            pid
          );
        }
        //重绘table
        this.toColTreeData(this.treeData);
      }

      // this.$eventBus.$on("addEvent3", (data) => {
      // }
    );
  },
  methods: {
    //导出
    toExport() {
      let fname = "专业核心能力.xls";
      let parameter = {
        id: this.modelInfo.id,
        fn: fname,
      };
      apiExprt.cmd(parameter, fname, "excel");
    },
    toCoreCoursePlan() {
      this.$refs.coreCoursePlan.open(this.treeData);
    },
    toAllCoursePlan() {
      let editParameter = {
        id: this.selectId,
      };
      this.$refs.allCoursePlan.open(editParameter);
    },
    toSupport() {
      this.messageInfo.id = this.subjectInfo.id;
      this.messageInfo.name = this.subjectInfo.name;
      this.messageInfo.year = this.subjectInfo.year;
      this.messageInfo.gid = this.currentId;
      this.messageInfo.model = this.modelInfo;
      this.isSupport = true;
      this.isOther = false;
    },
    toOther() {
      this.messageInfo.id = this.subjectInfo.id;
      this.messageInfo.name = this.subjectInfo.name;
      this.messageInfo.year = this.subjectInfo.year;
      this.messageInfo.gid = this.currentId;
      this.messageInfo.model = this.modelInfo;
      this.isSupport = false;
      this.isOther = true;
    },
    returnList() {
      if (this.isDisplay) {
        this.isDisplay = false;
      } else {
        this.$emit("returnList");
      }
    },
    returnSupport() {
      this.isSupport = false;
    },
    returnOther() {
      this.isOther = false;
    },
    editList() {

      if (!this.isDisplay) {
        this.isDisplay = true;
      } else {
        this.toColTreeData(this.treeData);
        let param = {
          id: this.currentId,
          coremodel: JSON.stringify(this.treeData),
        };
        api.update(param, (res) => {
          this.$utils.msg.success("保存成功");
        });
      }
      console.log(this.treeData);
      this.isDisplay = true;
    },
    findNode(optId, idArr) {
      if (optId.length == 2) {
        return this.treeData;
      } else if (optId.length == 4) {
        return this.treeData[idArr[0]].children;
      } else if (optId.length == 6) {
        return this.treeData[idArr[0]].children[idArr[1]].children;
      } else if (optId.length == 8) {
        return this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
          .children;
      } else if (optId.length == 10) {
        return this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
          .children[idArr[3]].children;
      } else if (optId.length == 12) {
        return this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
          .children[idArr[3]].children[idArr[4]].children;
      } else if (optId.length == 14) {
        return this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
          .children[idArr[3]].children[idArr[4]].children[idArr[5]].children;
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
      if (strNewId.length == 14) {
        //第7个节点
        // val = "课程名称-32(6)"
        let varArr = val.split("-");
        let val8Arr = varArr[1].split("(实操");
        let cntAll = val8Arr[0];
        let cntSx = val8Arr[1].substr(0, val8Arr[1].length - 1);
        arrs.push({
          id: strNewId,
          parentId: pid,
          name: varArr[0],
          all: cntAll,
          sx: cntSx,
          cType: this.cType,
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
        arrs.push({ id: strNewId, parentId: pid, name: val, children: [] });
      }

      //根据id重新排序
      if (arrs.length > 1) {
        //只有一个节点的话不要排序
        arrs.sort((a, b) => a.id * 1 - b.id * 1);
      }
    },
    list() {
      let param = {
        id: this.currentId,
      };
      this.$nextTick(() => {
        api.toUpdate(param, (res) => {
          this.modelInfo = res.data;
          if (
            res.data.coremodel == null ||
            typeof res.data.coremodel == undefined ||
            res.data.coremodel == ""
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
            this.treeData = JSON.parse(res.data.coremodel);
          }
          this.toColTreeData(this.treeData);
        });
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
