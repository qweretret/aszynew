<template>
  <popup ref="popup" :loading="popupLoading">
    <div slot="body">
      <el-form
        class="dataForm"
        ref="form"
        :disabled="disabled"
        :model="parameter"
        :inline="true"
        label-width="100px"
      >
        <el-row>
          <el-col :span="24">
            <el-form-item label="专业课程" prop="name">
               <el-select v-model="parameter.name" value-key="id" @change="getCourseName" placeholder="请选择">
                  <el-option
                    v-for="(item,idx) in listData"
                    :key="idx"
                    :label="item.name"
                    :value="idx">
                  </el-option>
                </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="考核方式" prop="testmode">
              <select-option-dictionary
                v-model="parameter.testmode"
                dkey="sub-kaocha"
                :multiple="false"
                :stringMode="true"
              ></select-option-dictionary>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程性质" prop="csprop">
              <select-option-dictionary
                v-model="parameter.csprop"
                dkey="tran-xz"
                :multiple="false"
                :stringMode="true"
              ></select-option-dictionary>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="学分" prop="credit">
              <el-input
                v-model="parameter.credit"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="总学时" prop="period">
              <el-input
                v-model="parameter.period"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="理论课总学时" prop="theoryperiod">
              <el-input
                v-model="parameter.theoryperiod"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实践课总学时" prop="actperiod">
              <el-input
                v-model="parameter.actperiod"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程目标" prop="cstarget">
              <el-input
                type="textarea"
                placeholder="请输入内容"
                v-model="parameter.cstarget"
                maxlength="15000"
                show-word-limit
                :rows="12"
                style="margin-top: 16px; width: 650px"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="主要内容" prop="cscontent">
              <el-input
                type="textarea"
                placeholder="请输入内容"
                v-model="parameter.cscontent"
                maxlength="15000"
                show-word-limit
                :rows="12"
                style="margin-top: 16px; width: 650px"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="教学要求" prop="teachneed">
              <el-input
                type="textarea"
                placeholder="请输入内容"
                v-model="parameter.teachneed"
                maxlength="15000"
                show-word-limit
                :rows="12"
                style="margin-top: 16px; width: 650px"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input
                type="textarea"
                placeholder="请输入内容"
                v-model="parameter.remarks"
                maxlength="15000"
                show-word-limit
                :rows="12"
                style="margin-top: 16px; width: 650px"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div slot="footer">
      <el-button
        v-if="!disabled"
        type="warning"
        :loading="confirmLoading"
        @click="confirm()"
        >确 定</el-button
      >
      <el-button @click="close">取 消</el-button>
    </div>
  </popup>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api from "@/api/subject/course/course.js";
import tyapi from "@/api/subject/coursetypes/coursetypes.js";
import planApi from "@/api/subject/courseplan/courseplan.js";

import selectOptionDictionary from "@/components/biz/selectOptionDictionary/selectOptionDictionary.vue";
export default {
  name: "edit",
  components: {
    popup,
    selectOptionDictionary,
  },
  inject: ["subjectId"],
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      parameter: {},
      listData:[],
      index:0,
      title:"",
      code:"",
      cid:"",
      testtype:"",
      xzName:"",
      level1:"",
      level2:"",
      level1Name:"",
      level2Name:"",
      courseTypeList:[],
    };
  },
  methods: {
    //获取课程性质
    getXzName(){
      tyapi.list({}, response => {
        //请求课程类型表
        this.courseTypeList = response.data.records;
      });
      for (let i = 0; i < this.courseTypeList.length; i++) {
        if (this.level1 == this.courseTypeList[i].code) {
            this.level1Name = this.courseTypeList[i].name
            for (let j = 0; j < this.courseTypeList[i].children.length; j++) {
              if (this.level2 == this.courseTypeList[i].children[j].code) {
                  this.level2Name = this.courseTypeList[i].children[j].name
              }
            }
        }  
      }

      this.xzName = this.level1Name + "-" + this.level2Name
      // console.log(this.xzName,"xzName");
    },
    getCourseName(idx){
       this.level1 = ""
       this.level2 = ""
      //  console.log(idx);

       this.parameter = this.listData[idx]
       this.level1 = this.parameter.level1
       this.level2 = this.parameter.level2
       this.parameter.testtype = this.parameter.type

       planApi.list({csid: this.parameter.id},res=>{
          
           let cosplan =  res.data.records[0]     
          // this.parameter.period =  
          // console.log( cosplan)
          //强制绑定
           this.$set(this.parameter, "period", cosplan.period)
           this.$set(this.parameter, "actperiod", cosplan.actperiod)
           this.$set(this.parameter, "cscontent", cosplan.cscontent)
           this.$set(this.parameter, "csprop", cosplan.csprop)
           this.$set(this.parameter, "cstarget", cosplan.cstarget)
           this.$set(this.parameter, "theoryperiod", cosplan.theoryperiod)
           this.$set(this.parameter, "testmode", cosplan.testmode)
           this.$set(this.parameter, "teachneed", cosplan.teachneed)
           this.$set(this.parameter, "remarks", cosplan.remarks)
           this.$set(this.parameter, "credit", cosplan.credit)
           
      
       })

      //获取课程编码，课程id，课程类别1，2级和课程类型
     /* for (let i = 0; i < this.listData.length ; i++) {
          if(e == this.listData[i].id){
            this.parameter.name = this.listData[i].name
            this.parameter.code = this.listData[i].code
            this.parameter.cid = this.listData[i].id
            this.level1 = this.listData[i].level1
            this.level2 = this.listData[i].level2
            this.getXzName()
            this.parameter.xzName = this.xzName
            this.parameter.testtype = this.listData[i].type
            
          }
      }*/
      
    },
    getCourseData(){
      let params = {subId:this.subjectId};
      api.list(params, response => {
        this.listData = response.data;
      });
    },
    //打开弹框
    open(parameter,index, title, disabled, size) {
      // console.log(index,"11111111111111111");
      this.getCourseData()
      this.parameter = parameter;
      this.disabled = disabled;
      this.index = index
      this.title = title
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
      console.log(this.parameter)
      //添加
      if(this.title == "添加课程"){
        this.$parent.addStep6(this.parameter)
      } else if(this.title == "编辑"){
        //修改
        this.$parent.saveStep6(this.index,this.parameter)
      }
      this.close()
    },
    init() {
      this.popupLoading = true;
      //清除等待
      this.confirmLoading = false;
      //清楚校验
      this.$refs.form.clearValidate();

      this.popupLoading = false;
    },
  },

};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
