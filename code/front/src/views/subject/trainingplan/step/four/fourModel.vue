<template>
  <div>
    <vxe-table border :span-method="rowspanMethod" v-loading="listLoading" :data="tableData">
      <vxe-column field="name1" title="职业标准">
        <template slot-scope="scope">
          <my-com
            :parameter="scope.row"
            :coltitle="'职业标准'"
            :scope="scope"
            :col="1"
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
    </vxe-table>
  </div>
</template>

<script>
import MyCom from "../../../../../components/myCom/myCom.vue";
import api from "@/api/subject/coremodel/coremodel.js";
import XEUtils, { remove } from "xe-utils";
export default {
  components: {
    MyCom,
  },
  data() {
    return {
      treeData: "",
      tableData: [],
      listLoading: false,
    };
  },
  inject: ["getSelectId"], //注入  
  mounted() {
    this.list();
  },
  methods: {
    list() {
      this.listLoading = true;
      let param = {
        id: this.getSelectId(),
      };
      this.$nextTick(() => {
        api.toUpdate(param, (res) => {
          // this.modelInfo = res.data;
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
             this.listLoading = false;
          } else {
            this.treeData = JSON.parse(res.data.coremodel);
             this.listLoading = false;
          }
          this.toColTreeData(this.treeData);
        });
      });
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
</style>