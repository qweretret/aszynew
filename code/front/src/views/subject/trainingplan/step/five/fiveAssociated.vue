<template>
  <div>
    <div>
      {{subname}}-{{year}}年 毕业生能力要求指标点
      <el-button     type="danger" v-if="isShowEdit" @click="toSync">同步</el-button>
      <el-button     type="warning" v-if="isShowEdit" @click="toSave">保存</el-button>
      <el-button @click="toNext">下一步</el-button>
    </div>
    <vxe-table border :scroll-y="{ enabled: isDisplay }" :span-method="rowspanMethod" v-loading="listLoading"
      :data="tableData">
      <vxe-column field="name1" title="课程性质">
        <template slot-scope="scope">
          {{ scope.row.name1 }}
        </template>
      </vxe-column>
      <vxe-column field="name2" title="课程名称">
        <template slot-scope="scope">
          <my-com :parameter="scope.row" :coltitle="'课程名称'" :scope="scope"></my-com>
        </template>
      </vxe-column>

      <vxe-column width="55px" v-for="(v, i) in titles" :key="i" :field="`name${3 + i}`" :title="v">
        <template slot-scope="scope">
          <el-checkbox v-model="scope.row.checked[i]"></el-checkbox>
        </template>
      </vxe-column>
    </vxe-table>
  </div>
</template>

<script>
import MyCom from "./assoc/myCom.vue";
import XEUtils, { remove } from "xe-utils";
export default {
  components: {
    MyCom,
  },
  data() {
    return {
      //课程体系与毕业生能力指标点关联矩阵
      zbdgl: [],

      treeData: "",
      tableData: [],

      listLoading: false,
      isDisplay: true,
      byszbd: [],
      titles: [],
      newData: {},
      newClass: {},
    };
  },
  props: {
    params: Object,
    rootnext: Function,
    year: String,
    subname: String,
    isShowEdit: Boolean
  },
  inject: ["getSelectId", "bigRootNext"], //注入  //使下一步继续跳转
  mounted() {

    this.$eventBus.$on("rmvAssocEvent", (optId) => {

      //拆解ID 
      let id_f = optId.substr(0, 2)
      let id_c = optId.substr(2)

      //通过 optID查找到treeData中的children中的id2.移除
      for (let i = 0; i < this.treeData.length; i++) {
        for (let j = 0; j < this.treeData[i].children.length; j++) {
          if (
            this.treeData[i].children[j] &&
            this.treeData[i].children[j].id == optId
          ) {
            //删除
            this.treeData[i].children.splice(j, 1);

            //重算treeData的id
            if (this.treeData[i].children.length == 0) {
              //删除父节点  后面的节点往前移动
              this.treeData.splice(i, 1);
              if (this.treeData.length == 0) {
                //被删的是最后一个 do nothing
              } if (this.treeData.length == 1) {
                //只剩1个
                for (let k = 0; k = this.treeData[0].children.length; k++) {
                  this.treeData[0].children[k].id = "01" + k
                  if (k < 10) {
                    this.treeData[0].children[k].id = "010" + k
                  } else {
                    this.treeData[0].children[k].id = "01" + k
                  }
                  this.treeData[0].children[k].parentId = "01"
                }
              } else {
                //后面往前挪
                for (let k = (id_f * 1); k < this.treeData.length; k++) {
                  for (let g = 0; g < this.treeData[k].children.length; g++) {

                    if (k < 10) {
                      if (g < 9) {
                        this.treeData[k].children[g].id ="0"+ k + "0" + (g + 1)
                      } else {
                        this.treeData[k].children[g].id ="0"+ k + (g + 1)
                      }
                    } else {
                      if (g < 9) {
                        this.treeData[k].children[g].id = k + "0" + (g + 1)
                      } else {
                        this.treeData[k].children[g].id = k + (g + 1)
                      }
                    }
                    this.treeData[k].children[g].parentId =  this.treeData[k].children[g].id.substr(0,2)
                  }
                }
              }
            } else if (this.treeData[i].children.length == 1) {
              //只剩一个
              this.treeData[i].children[0].id = id_f + "01"
            } else if (this.treeData[i].children.length == (id_c * 1 - 1)) {
              //被删的是最后一个 do nothing
            } else {
              //删除中间位置，treeData数据一分为二，后面的前移
              for (let k = (id_c * 1); k < this.treeData[i].children.length; k++) {
                if (k < 10) {
                  this.treeData[i].children[k].id = id_f + "0" + k
                } else {
                  this.treeData[i].children[k].id = id_f + k
                }
              }
            }
            //tableData的id更新
            this.toColTreeData(this.treeData);
            return;

          }
        }
      }
      this.toColTreeData(this.treeData);

    });

    this.$eventBus.$on("editAssocEvent", (param) => {

      for (let i = 0; i < this.treeData.length; i++) {
        for (let j = 0; j < this.treeData[i].children.length; j++) {
          if (
            this.treeData[i].children[j] &&
            this.treeData[i].children[j].id == param.id2
          ) {
            this.treeData[i].children[j].name = param.name2;
            this.toSave();
            return;
          }
        }
      }
    });

    this.byszbd = this.params.step5[0].byszbd;
    this.titles = this.params.step5[0].titles;

    this.list();

    //添加课程
    this.$eventBus.$on("saveSubject", (data) => {

      //查重
      for (let i = 0; i < this.treeData.length; i++) {
        for (let j = 0; j < this.treeData[i].children.length; j++) {
          if (
            this.treeData[i].children[j] &&
            this.treeData[i].children[j].name == data.kcmc
          ) {
            this.$message({
              type: "danger",
              message: "无法添加,[" + data.kcmc + "]已经存在",
            });
            return;
          }
        }
      }
  
      //第一个节点
      if (this.treeData.length == 0) {
        let inits = {
          children: [
            {
              checked: [false, false, false, false, false, false],
              id: "0101",
              keys: ["01", "0101"],
              name: data.kcmc,
              parentId: "01",
            },
          ],
          id: "01",
          keys: ["01"],
          name: data.kcxz,
          parentId: "0",
        };
        this.treeData.push(inits);
        this.list();
        this.$emit("child");
      } else {
        let list = this.treeData.map((item) => {
          return item.name;
        });

        this.treeData.forEach((item) => {
          if (list.indexOf(data.kcxz) != -1) {
            return;
          } else {
            this.newData = {
              children: [
                {
                  checked: [false, false, false, false, false, false],
                  id: item.keys[0] + `0${item.children.length + 1}`,
                  keys: [
                    item.keys[0],
                    item.keys[0] + `0${item.children.length + 1}`,
                  ],
                  name: data.kcmc,
                  parentId: item.keys[0],
                },
              ],
              id:
                this.treeData.length < 10
                  ? `0${this.treeData.length + 1}`
                  : this.treeData.length + 1,
              keys: [
                this.treeData.length < 10
                  ? `0${this.treeData.length + 1}`
                  : this.treeData.length + 1,
              ],
              name: data.kcxz,
              parentId: "0",
            };
          }
        });

        if (list.indexOf(data.kcxz) != -1) {
          this.newData = {};
        } else {
          this.newClass = {};
          this.treeData.push(this.newData);
        }

        this.params.step5[0].zbdgl = this.treeData;
        this.list();
        this.$emit("child");
      }
    });
  },
  methods: {
    toSync() {
      this.titles = [];

      this.byszbd.forEach((v) => {
        if (v.children) {
          v.children.forEach((itm) => {
            if (itm.name) {
              //"1-1.信息加密"
              this.titles.push(
                itm.id.substr(0, 2) * 1 + "-" + itm.id.substr(2) * 1
              );
            }
          });
        }
      });
      this.params.step5[0].titles = this.titles;

      this.toColTreeData(this.treeData, true);
    },
    async toNext() {
      this.params.step5[0].zbdgl = this.treeData;
      await this.$emit("child");
      // this.rootnext(); //props接回来的是undefined
      await this.bigRootNext();
    },
    toSave() {
      this.params.step5[0].zbdgl = this.treeData;
      this.$emit("child");
    },
    list() {
      this.treeData = this.params.step5[0].zbdgl;

      console.log(JSON.stringify(this.treeData))
      this.toColTreeData(this.treeData);

    },
    //vxetable文档自带，渲染table用
    toColTreeData(treeData, isSync) {
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
            //毕业生能力指标点关联矩阵
            if (isSync) {
              row.checked = [];
              //初始值
              this.titles.forEach((v) => row.checked.push(false));
              item.checked = row.checked;
            } else {
              //数据库中的回显
              row.checked = item.checked;
            }

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
      const fields = ["name1", "name2"];
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

</style>