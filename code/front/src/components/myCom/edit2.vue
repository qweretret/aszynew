<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="已有课程" name="first">
          <el-form ref="form" :model="input">
            <el-row>
              <el-form-item>
                <el-select
                  v-model="input.input1"
                  style="width: 100%"
                  placeholder="请选择课程"
                  @change="getPeriod"
                >
                  <el-option
                    v-for="(item,idx) in courseList"
                    :key="idx"
                    :label="item.name"
                    :value="idx"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="本能力对应的总学时" prop="input2">
                <el-input v-model="input.input2" disabled></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="其中:实践总学时" prop="input3">
                <el-input v-model="input.input3" :disabled="true"></el-input>
              </el-form-item>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button
          v-show="!(activeName == 'second')"
          type="primary"
          @click="save"
          >保存</el-button
        >
        <el-button v-show="activeName == 'second'" type="primary" @click="save2"
          >保存课程</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import apiCourseplan from "@/api/subject/courseplan/courseplan.js";
import cTypeApi from "@/api/subject/coursetypes/coursetypes.js";
import courseApi from "@/api/subject/course/course.js";
import selectOptionDictionary from "@/components/biz/selectOptionDictionary/selectOptionDictionary.vue";
export default {
  data() {
    return {
      dialogVisible: this.display,
      input: {
        input1: "",
        input2: "",
        input3: "",
        subjectid: "",
      },
      form: {
        region: "",
      },
      param: {},
      title: "",
      courseList: [],
      courseTypeList: [],
      activeName: "first",
    };
  },
  components: {
    selectOptionDictionary,
  },
  methods: {
    handleClick(tab, event) {
      if (this.activeName == "second") {
        this.input.input1 = "";
      }
    },
    //打开弹框
    open(parameter, title, id) {

      this.subjectid = id;
      this.courseTypeList = [];
      this.$nextTick(() => {
        this.param = parameter;
        this.input.subjectid = this.subjectid;

        courseApi.list2({ subId: id }, (res) => {
        this.courseList = res.data;
        });

        this.title = title;
        this.dialogVisible = true;
        if (this.title.substr(0, 2) == "编辑") {
          this.input.input1 = this.param.parameter.name7;
          let val8Arr = this.param.parameter.name8.split("(实操");
          this.input.input2 = val8Arr[0];
          this.input.input3 = val8Arr[1].substr(0, val8Arr[1].length - 1);
        }
      });
    },
    //关闭弹框
    close() {
      this.param = {};
      this.input.input1 = "";
      this.input.input2 = "";
      this.input.input3 = "";
      this.dialogVisible = false;
 
    },
    //保存修改
    save2() {
      this.activeName = "first";
    },
    getPeriod() {
    
      let cors = this.courseList[this.input.input1] 
       
      this.input.input1 = cors.name
      this.input.input2 = cors.period;
      this.input.input3 = cors.period*1-cors.theoryperiod*1;

    },
    save() {

      if (this.title.substr(0, 2) == "添加") {
        if (this.input.input1.trim() != "") {
          this.param.parameter.name7 = this.input.input1;
          //通过消息触发全局事件 交个tavle.vue处理
          this.param.input2 = this.input.input2;
          this.param.input3 = this.input.input3;

          //this.form.region  id取name
          let cName = "";
          let cType = "";
            this.courseList.forEach((item) => {
              if (item.id == this.input.input1) {
                cName = item.name;
                cType = item.dbColumn_level1 + "-" + item.dbColumn_level2;
              }
            });

            // this.param.parameter.name1
            this.param.parameter[`name${this.param.col}`] = cName;
            this.param.parameter.cType = cType;

          this.$eventBus.$emit("addEvent", this.param);
          this.close();
        } else {
          this.$utils.msg.warning("请输入内容");
          this.input = "";
        }
      } else if (this.title.substr(0, 2) == "编辑") {
        this.param.parameter.name7 = this.input.input1;
        this.param.input2 = this.input.input2;
        this.param.input3 = this.input.input3;
        //this.form.region  id取name
        let cName = "";
        let cType = "";
        if (this.activeName == "first") {
          
          this.courseList.forEach((item) => {
            if (item.name == this.input.input1) {
              cName = item.name;
              cType = item.dbColumn_level1 + "-" + item.dbColumn_level2;
            }
          }); 

          this.param.parameter[`name${this.param.col}`] = cName;

          this.param.parameter.cType = cType;
        } /* else {
          //当选择的课程不为空时
          // this.param.parameter.name1
          this.param.parameter[`name${this.param.col}`] = this.input.input1;
          this.courseTypeList.forEach((item) => {
            if (item.code == this.input.value) {
              cType = item.name;
            }
          });
          this.param.parameter.cType = cType;
        } */
        //消息驱动        触发editEvent，传param参数进去
        this.$eventBus.$emit("editEvent", this.param);
        this.close();
      }
    },
  },
};
</script>

<style></style>
