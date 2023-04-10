<template>
  <div>
    <el-form class="dataForm" ref="form" :disabled="disabled" :model="courseOne" :inline="true" label-width="120px"
      :rules="rules">
      <el-row>
        <el-col :span="10">
          <el-form-item label="课程名称" prop="name">

            <el-select v-model="courseIdx" placeholder="请选择" @change="recordsChg">
              <el-option v-for="(item, idx) in course.listData" :key="item.name" :label="item.name" :value="idx">
              </el-option>
            </el-select>

          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="教学单位" prop="company">

            <el-select v-model="courseOne.company" placeholder="请输入教学单位">
              <el-option v-for="item in jys" :key="item.name" :label="item.name" :value="item.name">
              </el-option>
            </el-select>

          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <el-col :span="7">
          <el-form-item label="课程代码" prop="code">
            <el-input :disabled="true" v-model="courseOne.code" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="课程类型" prop="type">
            <el-input :disabled="true" v-model="courseOne.type" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="专业代码" prop="subjectCode">
            <el-input :disabled="true" v-model="courseOne.subjectCode" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label=" 学 分 " prop="credit">
            <el-input :disabled="true" v-model="courseOne.credit" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label=" 学 时 " prop="period">
            <el-input :disabled="true" v-model="courseOne.period" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="理论课程总学时" prop="theoryperiod">
            <el-input :disabled="true" v-model="courseOne.theoryperiod" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="实践课程总学时" prop="actperiod">
            <el-input :disabled="true" v-model="courseOne.actperiod" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20">
          <el-form-item label="授课对象" prop="object">
            <el-input v-model="courseOne.object" autocomplete="off" placeholder="请输入授课对象"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="先修课程" prop="beforeCourse">

            <el-select v-model="courseOne.beforeCourse" placeholder="请输入先修课程" @change="recordsChg1">
              <el-option v-for="(item, idx) in records1" :key="item" :label="item" :value="idx">
              </el-option>
            </el-select>

          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="后续课程" prop="followCourse">

            <el-select v-model="courseOne.followCourse" placeholder="请输入后续课程[如果有先修，请先选【先修课程】再选【后续课程】]">
              <el-option v-for="item in records2" :key="item" :label="item" :value="item">
              </el-option>
            </el-select>

          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
// import course_api from "@/api/subject/course/course.js";
import api_q from "@/api/subject/coursetypes/coursetypes.js";
import courseplanApi from "@/api/subject/courseplan/courseplan.js";
import subjectApi from "@/api/subject/subject/subject.js";


export default {
  // props: { csdata:Object, course: Object,sid:String },
  name: "list",
  components: {
    popup
  },
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      rules: {},
      subject: {},
      records1: [],
      records2: [],
      subjectCode: "",
      jys: [],

      courseOne: {},
      csdata: {},
      course: {},
      sid: "",
      courseIdx: undefined
    };
  },
  methods: {

    recordsChg() {

      this.csdata.one = this.course.listData[this.courseIdx]

      //修剪掉不需要的数据  准备存到json
      delete this.csdata.one.id
      delete this.csdata.one.level1
      delete this.csdata.one.level2
      delete this.csdata.one.subjectid
      delete this.csdata.one.dbColumn_level1
      delete this.csdata.one.dbColumn_level2
      delete this.csdata.one.dbColumn_subjectid
      delete this.csdata.one.mder
      delete this.csdata.one.mdtm
      delete this.csdata.one.crer
      delete this.csdata.one.crtm

      courseplanApi.list({ csid: this.courseOne.id }, (response) => {
        let courseplanData = response.data.records[0];
        this.csdata.one.credit = courseplanData.credit
        this.csdata.one.period = courseplanData.period
        this.csdata.one.actperiod = courseplanData.actperiod

        this.csdata.one.subjectCode = this.subjectCode
        this.$set(this.csdata.one, "theoryperiod", courseplanData.theoryperiod)
        this.courseOne = this.csdata.one
      });

      //reset
      //克隆数组
      this.records1 = []
      this.records2 = []

      this.course.listData.forEach(itm => {
        this.records1.push(itm.name)
        this.records2.push(itm.name)
      })

      if (this.courseIdx != undefined) {
        this.records1.splice(this.courseIdx, 1)
        this.records2.splice(this.courseIdx, 1)
      }

      this.courseOne.beforeCourse = ""
      //有内容就清
      if (this.courseOne.followCourse) {
        this.courseOne.followCourse = ""
      }

    }, recordsChg1(idx) {
      this.courseOne.beforeCourse = this.records1[idx]
      this.records2.splice(idx, 1)
    },
    save() {
      this.$emit("merge")
    },
    //清空数据
    clear() {
      this.courseOne = {};
      this.courseIdx = undefined
    }, initView(csdata, course, sid, disabled) {
      this.disabled = disabled
      //对象传值传引用
      this.csdata = csdata

      this.course = course
      this.sid = sid
      this.courseOne = this.csdata.one

      this.popupLoading = true;
      //清除等待
      this.confirmLoading = false;
      subjectApi.toUpdate({ id: this.sid }, res => {
        console.log(res);
        this.subject = res.data.data
        this.jys = res.data.depts
        this.subjectCode = this.subject.code

        this.popupLoading = false;
      })

      this.popupLoading = false;
      this.confirmLoading = false;
    }
  }, mounted() {


    //console.log("One mounted");

  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";

.savebtn {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>