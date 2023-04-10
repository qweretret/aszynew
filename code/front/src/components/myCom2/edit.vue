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
          <el-form ref="form" :model="form">
            <el-form-item>
              <el-select
                v-model="form.region"
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
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import apiCoursePlan from "@/api/subject/courseplan/courseplan.js";
import courseApi from "@/api/subject/course/course.js";
import selectOptionDictionary from "@/components/biz/selectOptionDictionary/selectOptionDictionary.vue";
export default {
  components: {
    selectOptionDictionary,
  },
  data() {
    return {
      dialogVisible: this.display,
      input: {},
      param: {},
      title: "",
      id: "",
      courseList: [],
      courseTypeList: [],
      value: "",
      flag: false,
      activeName: "first",
      form: {
        region: "",
      },
    };
  },
  methods: {
    // taps页切换按钮
    handleClick(tab, event) {},
    //打开弹框
    open(parameter, title) {
      this.courseTypeList = [];
      this.$nextTick(() => {
        this.param = parameter;
        this.input.subjectid = this.param.subjectid;
        courseApi.list2({ subId: this.input.subjectid }, (res) => {
          this.courseList = res.data;
        });
        this.title = title;
        this.dialogVisible = true;
        if (this.title.substr(0, 2) == "编辑") {
          this.form.region = this.param.parameter[`name${this.param.col}`];
        }
      });
    },
    //关闭弹框
    close() {
      this.param = {};
      this.input = {};
      this.courseTypeList = [];
      this.form.region = "";
      this.dialogVisible = false;
    },

    getPeriod(i) {
      //选择的对象
      this.id = this.courseList[i];
      this.form.region = this.id.name
      console.log(this.id);
    },
    //保存&修改
    save() {
      console.log(  " save()" );
      if (this.title.substr(0, 2) == "添加") {
        console.log(this.id)

        //trim()去除前后空格，当this.input添加课程的内容为空时
        if (this.activeName == "first") {

          let cName = this.id.name;
          let cType =  this.id.dbColumn_level1 + "-" + this.id.dbColumn_level2;

          // this.param.parameter.name1
          this.param.parameter[`name${this.param.col}`] = cName;
          this.param.parameter.cType = cType;
          let parameter = {
            csid: this.id,
            sid: this.param.subjectid,
          };
          
            this.param.parameter.name2 = this.id.cscontent;
            this.param.input2 =  this.id.period;
            this.param.input3 = this.id.period*1 - this.id.theoryperiod*1;

            console.log(this.param);
            // 更新col1
            this.$eventBus.$emit("addEvent2", this.param);
            this.close();

          //通过消息触发全局事件 交个tavle.vue处理
          if (this.flag) {
            this.param.input1 = input1;
            this.param.input2 = input2;
            this.param.input3 = input3;

            console.log(this.param);

            this.$eventBus.$emit("addEvent2", this.param);
            this.close();
          }
        } else if (this.input == "" || this.form.region == "") {
          this.$utils.msg.warning("请添加课程再保存");
          this.input = "";
          this.form.region = "";
        }
      }else if (this.title.substr(0, 2) == "编辑") {

        if (this.activeName == "first") {
          //this.form.region  id取name
          let cName = this.id.name;
          let cType =  this.id.dbColumn_level1 + "-" + this.id.dbColumn_level2;

          // this.param.parameter.name1 更新数据
          this.param.parameter[`name${this.param.col}`] = cName;
          this.param.parameter.cType = cType;
          let parameter = {
            csid: this.id.id,
            sid: this.param.subjectid,
          };
     
            this.param.parameter.name2 = this.id.cscontent;
            this.param.input2 =  this.id.period;
            this.param.input3 = this.id.period*1 - this.id.theoryperiod*1;

            console.log(this.param);
            // 更新
            this.$eventBus.$emit("editEvent2", this.param);
            
            this.close();
         // });

          
        }
        //消息驱动   触发editEvent，传param参数进去
      }
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
