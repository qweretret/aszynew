<template>
  <div class="tab-body">
    <el-row class="autoFlex" :gutter="10">
      <el-col :span="4">
        <el-card class="leftCard">
          <div slot="header" style="text-align: center" class="clearfix">
            <span>专业列表</span>
          </div>
          <el-link
            :underline="false"
            :type="item.id==searchParams.subId?'primary':''"
            v-for="item in subjectList"
            :key="item.id"
            class="text item"
            @click="subClick(item.id)"
            >{{ item.name }}</el-link
          >
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card class="rightCard">
          <div class="search">
            <el-form ref="form" :model="searchParams" label-width="80px">
              <el-row>
                <el-col :span="5">
                  <el-form-item label="课程代码">
                    <el-input v-model="searchParams.code"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item label="名称">
                    <el-input v-model="searchParams.name"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item label="类别">
                    <el-input v-model="searchParams.type"></el-input>
                  </el-form-item>
                </el-col>
                <el-collapse-transition>
                  <div v-if="isShowMoreSearch">
                    <!--隐藏区域-->
                  </div>
                </el-collapse-transition>
                <div class="search-btn">
                  <el-button
                    type="primary"
                    icon="el-icon-search"
                    @click="search"
                    >搜索</el-button
                  >
                  <el-button @click="clearSearchVal">清空</el-button>
                </div>
              </el-row>
            </el-form>
          </div>
          <div class="operator">
            <el-button type="primary" icon="el-icon-plus" @click="toSave()"
               v-has-permission-code-and="'10210460'"
              >添加新课程</el-button
            >
            
          </div>
          <div class="dataTable autoFlex" v-if="listData">
            <el-table
              class="table"
              :data="listData"
              stripe
              border
              height="auto"
              @selection-change="selectionChange"
              v-loading="listLoading"
            >
              <el-table-column prop="name" label="课程名称"></el-table-column>
              <el-table-column prop="code" label="课程代码"></el-table-column>
              <el-table-column  prop="credit"  width="65px" label="总学分"></el-table-column>
              <el-table-column  prop="period" width="65px" label="总课时"></el-table-column>
              <el-table-column label="课程类别">
                <template slot-scope="scope">
                  <el-tag type="success" size="mini">{{
                    scope.row.dbColumn_level1
                  }}</el-tag>
                  <el-tag type="warning" size="mini">{{
                    scope.row.dbColumn_level2
                  }}</el-tag> 
                </template>
              </el-table-column>
              <el-table-column
                prop="dbColumn_subjectid"
                label="所属专业"
              ></el-table-column>
              <el-table-column label="操作" fixed="right" width="400px">
                <template slot-scope="scope">
                  <div class="dataTable-operator">
                    <el-button
                      size="mini"
                      type="primary"
                      @click="toDetail(scope.$index, scope.row)"
                      >详情</el-button
                    >
                    <el-button
                      size="mini"
                      type="warning"
                      v-has-permission-code-and="'10210460'"
                      @click="toUpdate(scope.$index, scope.row)"
                      >编辑</el-button
                    >
                    <el-button
                      size="mini"
                      type="danger"
                      v-has-permission-code-and="'10210460'"
                      @click="remove(scope.$index, scope.row)"
                      >删除</el-button
                    >
                    <el-button
                      size="mini"
                      type="primary"
                      v-has-permission-code-and="'10210470'"
                      @click="toMore(  scope.row)"
                      >扩展信息</el-button
                    >
                    <el-button
                      size="mini"
                      type="primary"
                      v-has-permission-code-and="'10210470'"
                      @click="toPlan(scope.row)"
                      >课程安排</el-button
                    >
                  </div>
                </template>
              </el-table-column>
            </el-table>
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
        </el-card>
      </el-col>
    </el-row>
    <edit ref="edit"></edit>
    <p-edit ref="pEdit"></p-edit>
    <coursetimeplan ref="coursetimeplan"></coursetimeplan>
  </div>
</template>
<script>
import edit from "./edit.vue";
import pEdit from "@/views/subject/courseplan/edit.vue";
import api from "@/api/subject/course/course.js";
import coursetimeplan from "@/views/subject/coursetimeplan/coursetimeplan.vue";
// import subApi from '@/api/subject/subject/subject.js';
export default {
  name: "course",
  components: {
    edit,
    pEdit,
    coursetimeplan
  },
  data() {
    return {
      searchParams: {},
      isShowMoreSearch: false,
      listLoading: false,
      listData: [],
      selected: [],
      subjectList: [],
      page: new this.$constant.pageObj(),
    };
  },
  methods: {
    toPlan(row) {
      //console.log(row)
      //let param = {
      //  cid:row.id,
      //  sid: row.subjectid,
      //}
      this.$refs.coursetimeplan.open(row);
    },
    subClick(id) {
      this.searchParams={subId:id}
      this.list()
    },
    //获取数据
    list() {
      this.listLoading = true;
      let params = this.$utils.merger(this.searchParams, this.page);
      //右边根据左边选中的专业加载课程
      api.list2(params, (response) => {
        this.listData = response.data;
        this.listLoading = false;
      });
    },
    //专业列表数据
    subList() {
      api.allList({}, (response) => {
        this.subjectList = response.data;
        console.log( this.subjectList);
        //右边默认加载第一个专业的课程
        if(this.subjectList && this.subjectList[0]){
          this.searchParams.subId = this.subjectList[0].id
        }
        //打开默认显示第一个
        this.subClick(this.searchParams.subId)
        this.listLoading = false;
      });
    },
    //去添加
    toSave() {
      this.$refs.edit.open({subId: this.searchParams.subId}, "添加");
    },
    //去编辑
    toUpdate(index, row) {
      let editParameter = {
        id: row.id,
      };
      this.$refs.edit.open(editParameter, "编辑");
    },
    //去详情
    toDetail(index, row) {
      let detailParameter = {
        id: row.id,
      };
      this.$refs.edit.open(detailParameter, "详情", true);
    },
    toMore( row ) {
      let moreParameter = {
        csid: row.id,
        sid: row.subjectid,
      };
      this.$refs.pEdit.open(moreParameter, row.name+" - 课程扩展信息",false);
    },
    //删除
    remove(index, row) {
      let parameter = {
        ids: row.id,
      };
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        api.remove(
          parameter,
          (response) => {
            this.$utils.msg.success(response.msg);
            this.list();
          },
          (response) => {
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
        ids: this.selected,
      };
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        api.remove(
          {
            ids: this.selected,
          },
          (response) => {
            this.$utils.msg.success(response.msg);
            this.list();
          },
          (response) => {
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
        this.selected.push(item.id);
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
     // this.searchParams = {};
       delete this.searchParams.code
       delete this.searchParams.name
       delete this.searchParams.type

      this.list();
    },
  },
  mounted() {
   // this.list();
    this.subList();
 
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
// .head {
//   text-align: center;
// }

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

// .clearfix:before,
// .clearfix:after {
//   display: table;
//   content: "";
// }
// .clearfix:after {
//   clear: both;
// }

// .box-card {
//   width: 480px;
// }
</style>

