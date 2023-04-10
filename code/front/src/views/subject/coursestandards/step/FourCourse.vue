<template>
  <popup ref="popup" :loading="popupLoading">
    <div slot="body">
      <el-form class="dataForm" ref="form" :disabled="disabled" :model="formParameter" :inline="true"
        label-width="120px" :rules="rules">
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程模块名称" prop="name">
              <el-input v-model="formParameter.name" autocomplete="off" placeholder="请输入课程模块"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程模块类型" prop="type">
              <el-radio-group v-model="formParameter.type">
                <el-radio label="必须通过的模块"></el-radio>
                <el-radio label="可以选修的模块"></el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="总学时" prop="allTime">
              <el-input v-model="formParameter.allTime" @blur="checkData(1)"  autocomplete="off" :placeholder="'总学时不大于'+this.period"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="实践学时" prop="practiceTime">
              <el-input v-model="formParameter.practiceTime" @blur="checkData(2)" autocomplete="off" :placeholder="'实践学时不大于'+this.actperiod"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="学习内容" prop="studyContent">
              <el-input type="textarea"  :rows="3" v-model="formParameter.studyContent" autocomplete="off"
               placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="学习产出" prop="studyProduce">
              <el-input type="textarea"  :rows="3" v-model="formParameter.studyProduce" autocomplete="off" placeholder="请输入学习产出"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="测评产地" prop="testPlace">
              <el-input v-model="formParameter.testPlace" autocomplete="off" placeholder="请输入测评场地"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="测评工具" prop="testTool">
              <el-input v-model="formParameter.testTool" autocomplete="off" placeholder="请输入测评工具"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="测评方法" prop="testMethod">
              <el-input v-model="formParameter.testMethod" autocomplete="off" placeholder="请输入测评方法"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div slot="footer">
      <el-button v-if="!disabled" type="primary" :loading="confirmLoading" @click="confirm()">确 定</el-button>
      <el-button @click="close">取 消</el-button>
    </div>
  </popup>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api from "@/api/subject/coremodel/coremodel.js";
export default {
  name: "edit",
  components: {
    popup,
  },
    // props: { actperiod:Number,period:Number },
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      parameter: {},
      formParameter: {},

      actperiod:0,
      period:0 ,

      rules: {
        //年级
        grade: [
          {
            validator: this.$validate.required,
            trigger: "blur",
          },
          {
            validator: this.$validate.length,
            max: 32,
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    //校验数据
    checkData(type){
      if( type ==1 && this.formParameter.allTime> this.period  ){
        this.$utils.msg.warning("总学时"+this.formParameter.allTime+"大于定义的"+this.period)
      }
      
      if( type ==2 && this.formParameter.practiceTime> this.actperiod  ){
        this.$utils.msg.warning("实践学时"+this.formParameter.practiceTime+"大于定义的"+this.actperiod)
      }
    },
    //打开弹框
 
    open(parameter,title,  disabled, size) {
      this.period=   parameter.period
      this.actperiod =  parameter.actperiod

      this.parameter = parameter;
      this.disabled = disabled;
      this.$refs.popup.open(title, size);
      this.$nextTick(() => {
        this.init();
      });
    },
    //关闭弹框
    close() {
      this.disabled = false;
      this.$refs.popup.close();
    },
    //提交表单
    confirm() {
      let time =
        "总课时" +
        this.formParameter.allTime +
        "(含实操" +
        this.formParameter.practiceTime +
        ")";
      let testMethod =
        "测评场地:" +
        this.formParameter.testPlace +
        "\n" +
        "测评工具:" +
        this.formParameter.testTool +
        "\n" +
        "测评方法" +
        this.formParameter.testMethod;

      this.formParameter.time = time;
      this.formParameter.testMethod = testMethod;
      this.$emit("editFourCourse", this.formParameter);

      this.close();
    },
    init() {
      this.popupLoading = true;
      //清除等待
      this.confirmLoading = false;
      //清空内容
      this.formParameter = {};
      //清楚校验
      this.$refs.form.clearValidate();

      console.log("init",this.actperiod  ,this.period)

      if (this.parameter.id) {
        //编辑查询数据
        let parameter = {
          id: this.parameter.id,
        };
        api.toUpdate(parameter, (response) => {
          this.formParameter = response.data;
          this.popupLoading = false;
        });
      } else {
        //添加
        this.popupLoading = false;
      }
    },
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
