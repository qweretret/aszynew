<template>
  <div class="tab-body">
    <div class="operator">
      <el-button type="info" icon="el-icon-arrow-left" @click="Return()">返回</el-button>
    </div>
    <div>
      <el-table class="table" :data="listData" stripe border @selection-change="selectionChange">
        <el-table-column prop="name" label="课程模块"></el-table-column>
        <el-table-column prop="time" label="课时"></el-table-column>
        <el-table-column prop="studyContent" label="学习内容（范围）"></el-table-column>
        <el-table-column prop="studyProduce" label="学习产出（评价标准）"></el-table-column>
        <el-table-column prop="testMethod" label="测评方法（测评工具、测评场地）"></el-table-column>
        <el-table-column label="操作" fixed="right" width="120px">
          <template slot-scope="scope">
            <div class="dataTable-operator">
              <!-- <el-button
                size="mini"
                icon="el-icon-edit"
                @click="toUpdate(scope.$index, scope.row)"
                >编辑</el-button
              > -->
              <el-button size="mini" v-if="isShowEdit" icon="el-icon-delete" type="danger" @click="remove(scope.$index, scope.row)">删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
import api from "@/api/subject/coursestandards/coursestandards.js";

import courseApi from "@/api/subject/course/course.js";

export default {
  //   props:{courseid:String},
  name: "evaluate",
  components: {},
  props: {
    isShowEdit: Boolean
  },
  data() {
    return {
      searchParams: {

      },
      isShowMoreSearch: false,
      listLoading: false,
      listData: [],
      selected: [],
      coursestandards: [],
      id: "",
      page: new this.$constant.pageObj(),
    };
  },
  watch: {
    id: function () {
      this.list();
    }
  },
  methods: {
    Return() {
      this.$emit("returnEvaluate");
    },
    //获取数据
    list() {
      this.listLoading = true;

      this.searchParams.id = this.id;
      console.log(this.searchParams);
      let params2 = this.$utils.merger(this.searchParams, this.page);
      api.list(params2, (response) => {

        if (response.data.records[0].coursetype == "专业核心能力") {
          let s = JSON.parse(response.data.records[0].corest);
          this.listData = s.editFourData;
          this.listLoading = false;
          console.log(s, "专业核心能力")
        } else if (response.data.records[0].coursetype == "专业支撑能力") {
          let s = JSON.parse(response.data.records[0].zcst);
          this.listData = s.editFourData;
          this.listLoading = false;
          console.log(s, "专业支撑能力")
        } else if (response.data.records[0].coursetype == "其他能力课程") {
          let s = JSON.parse(response.data.records[0].othercourse);
          this.listData = s.editFourData;
          this.listLoading = false;
          console.log(s, "其他能力课程")
        }
        // let s=JSON.parse(response.data.records[0].othercourse);
        // console.log(s,"123")
        // this.listData=s.editFourData;
        // console.log(this.listData);
        // this.listLoading = false;
      });
      this.searchParams = {};
    },
    //新建其他能力
    otherAble() {
      let editParameter = {
        subjectId: this.subjectId,
        listData: this.listData,
        cid: this.courseid,
      };
      this.$refs.edit.open(editParameter, "新建其他能力");
    },
    //去添加

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
      this.searchParams = {};
    },
  },
  created() { },
  mounted() {
    this.$eventBus.$on("evaluate", (res) => {

      this.id = res;

    });
  },
  destroyed() {
    //组件销毁同时取消监听
    this.$eventBus.$off("evaluate");
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
</style>
