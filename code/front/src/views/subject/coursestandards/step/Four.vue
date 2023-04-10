<template>
  <div style="width: 90%; margin: 0 auto;">
    <div class="operator">
      <el-button type="danger" v-if="!disabled" icon="el-icon-edit" @click="toSave()">新建</el-button>
    </div>
    <div style="margin-bottom: 20px;">
      <el-table class="table" :data="courseFour" stripe border @selection-change="selectionChange">
        <el-table-column prop="name" label="课程模块"></el-table-column>
        <el-table-column prop="time" label="课时"></el-table-column>
        <el-table-column prop="studyContent" label="学习内容（范围）"></el-table-column>
        <el-table-column prop="studyProduce" label="学习产出（评价标准）"></el-table-column>
        <el-table-column prop="testMethod" label="测评方法（测评工具、测评场地）"></el-table-column>
        <el-table-column label="操作" fixed="right" width="180px">
          <template slot-scope="scope">
            <div class="dataTable-operator">
              <el-button size="mini"  v-if="!disabled"  icon="el-icon-delete" type="danger" @click="remove(scope.$index)">删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <four-course ref="editFourCourse"   @editFourCourse="editFourCourse"></four-course>
 
  </div>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
// import course_api from "@/api/subject/course/course.js";
import api_q from "@/api/subject/coursetypes/coursetypes.js";
import fourCourse from "./FourCourse.vue";

export default {
  //props: { csdata: Object },
  name: "list",
  components: {
    popup,
    api_q,
    fourCourse
  },
  data() {
    return {
      disabled: false,
      confirmLoading: false,
      activeName: "second",
      parameter: {},
      listLoading: [],
      courseFour: [],
      csdata:{},

      actperiodSum:0,
      periodSum:0,
    };
  },
  methods: {
    // toUpdate(index,row){
    //    let data=this.listDataTwo[index];
    //    this.$refs.editFourCourse.open(data, "");
    // },
    remove(index) {
      this.courseFour.splice(index);
    },
    save() {
      this.$emit("merge");
    },
    editFourCourse(val) {
      this.courseFour.push(val);
      //子控件通过消息监听传值给父控件  更新剩余的课时量
      this.actperiodSum  += val.practiceTime*1
      this.periodSum  += val.allTime*1
    },
    initView(csdata,  disabled ) {
      this.disabled=disabled
      this.csdata=csdata

      // console.log(     this.csdata );
      // console.log( JSON.stringify( this.csdata )  );                           
      // this.actperiodSum =     this.csdata.one.actperiod
      // this.periodSum =     this.csdata.one.period
      // console.log(this.periodSum,this.actperiodSum);
 
      this.courseFour =  this.csdata.four
    },
    selectionChange() { },
    //清空数据
    clear() {
      this.actperiodSum=0
      this.periodSum=0
    },
    toSave() {
                           //父控件调用子控件方法 -传值
      this.$refs.editFourCourse.open({actperiod:  (this.csdata.one.actperiod-this.actperiodSum),period: (this.csdata.one.period-this.periodSum)}, "添加");
    }
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
