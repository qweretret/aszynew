<template>
    <div class="cs">
      <vxe-table border :span-method="rowspanMethod" :data="tableData">
        <vxe-column field="name1" title="职业标准">
          <template slot-scope="scope">
            <my-com
              :display="true"
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
              :parameter="scope.row"
              :coltitle="'知识点'"
              :col="6"
              :scope="scope"
            ></my-com>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
</template>

<script>
import MyCom from "../../../../components/myCom/myCom.vue";
import Vue from "vue";
import XEUtils, { remove } from "xe-utils";
Vue.prototype.$eventBus = new Vue();
export default {
  components: {
    MyCom,
  },
  data() {
    return {
      treeData: "",
      tableData: "",
      subjectId: "",
    };
  },
  created() {
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
      //console.log(JSON.stringify(param));
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

      console.log(JSON.stringify(optArr));

      this.toColTreeData(this.treeData);
    });

    // 消息主题  接收  editEvent事件
    this.$eventBus.$on("editEvent", (data) => {
      console.log(data);
      //处理id
      let idArr = [];
      let optId = data.parameter[`id${data.col}`];
      for (let i = 0; i < data.col; i++) {
        idArr.push(optId.substr(i * 2, 2) * 1 - 1);
      }
      let optArr = this.findNode(optId, idArr);
      //当前的序号
      let idx = idArr[idArr.length - 1];

      // console.log("idx = "+idx);
      optArr[idx].name = data.parameter[`name${data.col}`];

      if ((data.col = 7)) {
        optArr[0].children[0].cntAll = data.input2;
        optArr[0].children[0].cntSx = data.input3;
        optArr[0].children[0].name = data.input2 + "(实操" + data.input3 + ")";
      }

      this.toColTreeData(this.treeData);
    });

    // this.$eventBus.$on("addEvent", (data) => {
    //   console.log(data);
    //   //新增节点前面每个col的中数组的序号
    //   let idArr = []; //01   01   02  010103
    //   //当前被操作的id
    //   let optId = data.parameter[`id${data.col}`];
    //   //被操作的父id
    //   let pid;
    //   if (data.col == 1) {
    //     pid = "";
    //   } else {
    //     pid = data.parameter[`id${data.col - 1}`];
    //   }

    //   if (!optId) {
    //     //本级的第一个节点
    //     optId = pid + "01";
    //   }

    //   for (let i = 0; i < data.col; i++) {
    //     //每个原始的id  对应  col的中数组   的序号
    //     idArr.push(optId.substr(i * 2, 2) * 1 - 1);
    //   }

    //   if (data.col == 1) {
    //     this.addNode(this.treeData, idArr[0], data.parameter.name1, "0");
    //   } else if (data.col == 2) {
    //     this.addNode(
    //       this.treeData[idArr[0]].children,
    //       idArr[1],
    //       data.parameter.name2,
    //       pid
    //     );
    //   } else if (data.col == 3) {
    //     this.addNode(
    //       this.treeData[idArr[0]].children[idArr[1]].children,
    //       idArr[2],
    //       data.parameter.name3,
    //       pid
    //     );
    //   } else if (data.col == 4) {
    //     this.addNode(
    //       this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
    //         .children,
    //       idArr[3],
    //       data.parameter.name4,
    //       pid
    //     );
    //   } else if (data.col == 5) {
    //     this.addNode(
    //       this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
    //         .children[idArr[3]].children,
    //       idArr[4],
    //       data.parameter.name5,
    //       pid
    //     );
    //   } else if (data.col == 6) {
    //     this.addNode(
    //       this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
    //         .children[idArr[3]].children[idArr[4]].children,
    //       idArr[5],
    //       data.parameter.name6,
    //       pid
    //     );
    //   } else if (data.col == 7) {
    //     this.addNode(
    //       this.treeData[idArr[0]].children[idArr[1]].children[idArr[2]]
    //         .children[idArr[3]].children[idArr[4]].children[idArr[5]].children,
    //       idArr[6],
    //       data.parameter.name7 +
    //         "-" +
    //         data.input2 +
    //         "(实操" +
    //         data.input3 +
    //         ")",
    //       pid
    //     );
    //   }
    //   //重绘table
    //   this.toColTreeData(this.treeData);
    // });
  },
  methods: {
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
      // console.log(strNewId + " = " + val);
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
          children: [
            {
              id: strNewId + "01",
              parentId: strNewId,
              name: varArr[1],
              all: cntAll,
              sx: cntSx,
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
      // 前端去后端取回数据
      this.treeData = [
        {
          id: "01",
          parentId: "0",
          name: "信息导论",
          children: [
            {
              id: "0101",
              parentId: "01",
              name: "信息加密",
              children: [
                {
                  id: "010101",
                  parentId: "0101",
                  name: "吃瓜群众",
                  children: [
                    {
                      id: "01010101",
                      parentId: "010101",
                      name: "吃瓜",
                      children: [
                        {
                          id: "0101010101",
                          parentId: "01010101",
                          name: "群众",
                          children: [
                            {
                              id: "010101010101",
                              parentId: "0101010101",
                              name: "瓜群",
                              children: [],
                            },
                          ],
                        },
                      ],
                    },
                  ],
                },
                {
                  id: "010102",
                  parentId: "0101",
                  name: "发射点发",
                  children: [],
                },
              ],
            },
          ],
        },
        {
          id: "02",
          parentId: "0",
          name: "电器工程",
          children: [
            {
              id: "0201",
              parentId: "02",
              name: "电器原理",
              children: [
                {
                  id: "020101",
                  parentId: "0201",
                  name: "外壳工艺",
                  children: [],
                },
                {
                  id: "020102",
                  parentId: "0201",
                  name: "电路板设计",
                  children: [],
                },
              ],
            },
          ],
        },
      ];
      // 渲染table
      this.toColTreeData(this.treeData);
    },
    //vxetable文档自带，渲染table用
    toColTreeData(treeData) {
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
/deep/ .signature {
  // padding:100px 0 !important;
  margin: 10px 0;
}
</style>
<style>
.cs {
  margin: 0px 10px;
}
</style>
