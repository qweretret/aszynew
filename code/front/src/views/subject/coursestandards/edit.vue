<template>
  <popup ref="popup" :loading="popupLoading">
    <div slot="body">
      <el-steps :active="active" finish-status="success" align-center style="margin-bottom: 30px;">
        <el-step title="课程基本信息"></el-step>
        <el-step title="课程概述"></el-step>
        <el-step title="能力标准"></el-step>
        <el-step title="课程模块"></el-step>
        <el-step title="课程合格标准"></el-step>
        <el-step title="课程实施建议"></el-step>
      </el-steps>         

      <one v-show="active == 0" ref="one" @merge="merge" :csdata="parameter" :course="coursePlan" :sid="sid"></one>
      <two v-show="active == 1" ref="two" @merge="merge"></two>
      <three v-show="active == 2" ref="three" @merge="merge" :csdata="coursePlan"></three>
      <four v-show="active == 3" @merge="merge" :csdata="coursePlan" ref="four"></four>
      <five v-show="active == 4" ref="five" @merge="merge" :csdata="coursePlan"></five>
      <six v-show="active == 5" ref="six" @merge="merge" :csdata="coursePlan"></six>

    </div>

    <div slot="footer">
      <el-button v-if="!disabled" type="primary" :loading="confirmLoading" @click="merge()">保 存</el-button>
      <el-button style="margin-top: 12px" :disabled="active == 0" @click="pre">上一步</el-button>
      <el-button style="margin-top: 12px" :disabled="active == 5"  @click="next">下一步</el-button>
      <el-button @click="close">关 闭</el-button>
    </div>
  </popup>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api from "@/api/subject/coursestandards/coursestandards.js";

import one from "./step/One";
import two from "./step/Two";
import three from "./step/Three";
import four from "./step/Four";
import five from "./step/Five";
import six from "./step/Six";

export default {
  name: "edit",
  components: {
    popup,
    one,
    two,
    three,
    four,
    five,
    six,
  },
  props: { sid: String, cid: String },
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      parameter: {},
      coursePlanId:undefined,
      active: 0,
      subjectId: "",
      coursePlan: { one: {}, two: {}, three: {}, four: [], five: {}, six: {} },
      abtype: 0,

    };
  },
  methods: {
    pre() {
      if (this.active-- < 1) this.active = 0;
    },
    next() {
      if (this.active++ > 4) {
        this.active = 5;
      }

     
    },
    //关闭弹框
    close() {
      //清空表单
      this.coursePlan = { one: {}, two: {}, three: {}, four: [], five: {}, six: {} }
      this.$refs.one.clear()
      this.$refs.two.clear()
      this.$refs.three.clear()
      this.$refs.four.clear()
      this.$refs.five.clear()
      this.$refs.six.clear()
      this.disabled = false;
      this.coursePlanId = false
      this.active = 0;
      this.$refs.popup.close();
    }, merge() {   //合并  判断id？更新：添加

      if (this.coursePlanId) {
        //编辑
        let parameter = { id: this.coursePlanId };
        parameter.courest = this.coursePlan;
        api.update(parameter, res => {
          this.$utils.msg.success("编辑成功")
          this.$parent.list();
        }, err => this.$utils.msg.warning(err.msg));
      } else {
        //添加
        let parameter = {};
        parameter.coursename = this.coursePlan.one.name;
        parameter.abtype = this.abtype
        parameter.cid = this.cid;
        parameter.subjectid = this.sid;
        parameter.courest = this.coursePlan;

        api.save(parameter, res => {
          //获取新增加的数据库id
          this.parameter.id = res.msg
          this.$utils.msg.success("添加成功")
          this.$parent.list();
        }, err => this.$utils.msg.warning(err.msg));

      }
      console.log(JSON.stringify(this.coursePlan))
    },
    open(parameter, title, type, disabled, size) {
      let titleFlag = title.substr(0,2)

      if(titleFlag == '编辑'){
        this.abtype = parameter.row.type
        this.parameter = parameter.listData
        this.coursePlanId=  parameter.row.id
        this.disabled = disabled;
      }else  if(titleFlag == '新建'){
        this.parameter = parameter;
        // 1:新建专业核心能力   2:新建专业支撑能力   3：新建其他能力
        this.abtype = type
        this.disabled = disabled;

      }else{
         title == '查看详细'
        this.abtype = parameter.row.type
        this.parameter = parameter.listData
        this.coursePlanId=  parameter.row.id
        this.disabled = true;
      }

      this.$nextTick(() => {
            this.init();
            this.$refs.popup.open(title, "100%");
       });
    
    },
    init() {
      this.popupLoading = true;
      //清除等待
      this.confirmLoading = false;
      //清楚校验
      console.log(this.coursePlanId);
      if (this.coursePlanId) {
        //编辑查询数据
        let parameter = {
          id:  this.coursePlanId 
        };
        api.toUpdate(parameter, (response) => {

          this.coursePlan = JSON.parse(response.data.courest);

          //父子通过函数传值 ：可以更主动
          this.$refs.one.initView(this.coursePlan,this.parameter,this.sid, this.disabled )
 
          this.$refs.two.initView(this.coursePlan,this.parameter.cid, this.disabled )
          this.$refs.three.initView(this.coursePlan, this.disabled )
          this.$refs.four.initView(this.coursePlan, this.disabled )
          this.$refs.five.initView(this.coursePlan, this.disabled )
          this.$refs.six.initView(this.coursePlan,this.disabled )

          this.popupLoading = false;
        });
      } else {
  
        //添加  延迟到  this.$refs.one 加载完毕
        setTimeout(()=>{
        //  console.log(this.coursePlan,this.parameter);
           this.$refs.one.initView(this.coursePlan,this.parameter,this.sid)
           this.$refs.two.initView(this.coursePlan,this.parameter.cid)
           this.$refs.three.initView(this.coursePlan)
           this.$refs.four.initView(this.coursePlan)
           this.$refs.five.initView(this.coursePlan)
           this.$refs.six.initView(this.coursePlan)
           this.popupLoading = false;
         }, 200)
        
      }
    },
  } 
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
