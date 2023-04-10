<template>
  <div class="tab-body">
    <div class="search">
      <el-form ref="form" :model="searchParams" label-width="80px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="学年">
              <el-input v-model="searchParams.name"></el-input>
            </el-form-item>
          </el-col>
          <div class="search-btn">
            <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
            <el-button @click="clearSearchVal">清空</el-button>
          </div>
        </el-row>
      </el-form>
    </div>
    <div class="operator" >
      <el-button type="primary" v-has-permission-code-and="'1021030'" icon="el-icon-plus" @click="toSave()">添加</el-button>
      <el-button type="danger"  v-has-permission-code-and="'1021031'"  icon="el-icon-delete" @click="batchRemove()">批量删除</el-button>
    </div>
    <div class="dataTable autoFlex" v-if="listData">
      <qlh-table
        class="table"
        ref="userTable"
        :data="listData.records"
        :span-method="objectSpanMethod"
        border
        height="auto"
        @selection-change="selectionChange"
        v-loading="listLoading"
      >
        <qlh-table-column type="selection" width="55" fixed="left"></qlh-table-column>
        <qlh-table-column prop="year" label="学年"></qlh-table-column>
     
        <qlh-table-column prop="term" label="学期">
          <template scope="scope"> 
               {{scope.row.term == 1?"第一学期":(scope.row.term == 2?"第二学期":"暑假")}}
            </template> 
            </qlh-table-column>
        <qlh-table-column prop="weeks" label="周数"></qlh-table-column>
        <qlh-table-column label="操作" fixed="right" width="200px">
          <template scope="scope">
            <div class="dataTable-operator">
              <!-- <el-button v-has-permission-code-and="''" size="mini" icon="el-icon-search" @click="toDetail(scope.$index, scope.row)">详情</el-button> -->
              <el-button 
                v-has-permission-code-and="'1021030'"
                size="mini"
                icon="el-icon-edit"
                @click="toUpdate(scope.row)"
              >修改</el-button>
              <el-button v-has-permission-code-and="'1021031'" size="mini" type="danger" @click="remove(scope.row)">删除</el-button>
            </div>
          </template>
        </qlh-table-column>
      </qlh-table>
      <div class="pageBar">
        <el-pagination
          @size-change="sizeChange"
          @current-change="currentChange"
          :total="listData.total"
          :page-size="page.size"
          :current-page="page.current"
          :layout="this.$constant.page.layout"
          :page-sizes="this.$constant.page.pageSizes"
        ></el-pagination>
      </div>
    </div>
    <edit ref="edit"></edit> 
  </div>
</template>
<script>
import edit from "./edit.vue";
import api from "@/api/subject/weekinterm/weekinterm.js";
import qlhTable from "@/components/qlhTable/qlhTable.vue";
import qlhTableColumn from "@/components/qlhTable/qlhTableColumn.vue";
export default {
  name: "weekinterm",
  components: {
    edit,
    qlhTable,
    qlhTableColumn
  },
  data() {
    return {
      searchParams: {},
      isShowMoreSearch: false,
      listLoading: false,
      listData: [],
      selected: [],
      page: new this.$constant.pageObj()
    };
  },
  methods: {
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0 || columnIndex === 1 || columnIndex === 4) {
        if (rowIndex % 3 === 0) {
          return {
            rowspan: 3,
            colspan: 1
          };
        } else {
          return {
            rowspan: 0,
            colspan: 0
          };
        }
      }
    },
    //获取数据
    list() {
      this.listLoading = true;
      let params = this.$utils.merger(this.searchParams, this.page);
      api.list(params, response => {
        this.listData = response.data;
        this.listData.records.forEach(item => {
          if (this.listData.records.term === 1) {
            this.listData.records.term = "第一学期";
          } else if (this.listData.records.term === 2) {
            this.listData.records.term = "第二学期";
          } else {
          }
          this.listData.records.term = "暑假";
        });
        this.listLoading = false;
      });
    },
    //去添加
    toSave() {
      this.$refs.edit.open({}, "添加");
    },
    //去编辑
    toUpdate(row) {
      let editParameter = {
        year: row.year
      };
      this.$refs.edit.open(editParameter, "编辑",1);
    },
    //去详情
    toDetail(index, row) {
      let detailParameter = {
        id: row.id
      };
      this.$refs.edit.open(detailParameter, "详情", true);
    },
    //删除
    remove(row) {
      let parameter = {
        years: row.year
      };
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        api.remove(
          parameter,
          response => {
            this.$utils.msg.success(response.msg);
            this.list();
          },
          response => {
            this.$utils.msg.warning(response.msg);
          }
        );
      });
    },
    //批量删除
    batchRemove() {
      if (!this.selected || this.selected.length == 0) {
        this.$utils.msg.info("请选择至少一行");
        return;
      }
      let parameter = {
        years: this.selected
      };
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        api.remove(
          {
        years: this.selected
      },
          response => {
            this.$utils.msg.success(response.msg);
            this.list();
          },
          response => {
            this.$utils.msg.warning(response.msg);
          }
        );
      });
    },
    //改变选择项
    selectionChange(val) {
      //清空
      this.selected = [];
      for (let item of val) {
        this.selected.push(item.year);
      }
    },
    //改变每页显示数量
    sizeChange(val) {
      this.page.size = val;
      this.list();
    },
    //改变现在的页码
    currentChange(val) {
      this.page.current = val;
      this.list();
    },

    //搜索
    search() {
      this.list();
    },
    //清空搜索框
    clearSearchVal() {
      this.searchParams = {};
      this.list()
    }
  },
  mounted() {
    this.list();
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
</style>

