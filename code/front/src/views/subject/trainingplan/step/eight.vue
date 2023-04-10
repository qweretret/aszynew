<template>
  <!-- 实施保障 -->
  <div>
    <div  style="font-weight:bolder" > 师资队伍</div>
    <el-input
      v-if="isShowEdit" 
      type="textarea"
      placeholder="请输入内容"
      v-model="param.step8[0].szdw"
      maxlength="15000"
      show-word-limit
      :rows="12"
      style="width:700px;margin-top:16px"
    >
    </el-input>
    <div  v-else  v-html="param.step8[0].szdw" style="width:700px;margin-top:16px;margin-left: 30px;"></div>

    <div style="margin-top:16px;font-weight:bolder">教学设施</div>
    <el-input
      v-if="isShowEdit" 
      type="textarea"
      placeholder="请输入内容"
      v-model="param.step8[0].jxss"
      maxlength="15000"
      show-word-limit
      :rows="12"
      style="width:700px;margin-top:16px"
    >
    </el-input>
    <div  v-else  v-html="param.step8[0].jxss" style="width:700px;margin-top:16px;margin-left: 30px;"></div>
    <div style="margin-top:16px;font-weight:bolder">教学资源</div>
    <el-input
      v-if="isShowEdit" 
      type="textarea"
      placeholder="请输入内容"
      v-model="param.step8[0].jxzy"
      maxlength="15000"
      show-word-limit
      :rows="12"
      style="width:700px;margin-top:16px"
    >
    </el-input>
    <div  v-else  v-html="param.step8[0].jxzy" style="width:700px;margin-top:16px;margin-left: 30px;"></div>
    <div style="margin-top:16px;font-weight:bolder">教学方法</div>
    <el-input
      v-if="isShowEdit" 
      type="textarea"
      placeholder="请输入内容"
      v-model="param.step8[0].jxff"
      maxlength="15000"
      show-word-limit
      :rows="12"
      style="width:700px;margin-top:16px;"
    >
    </el-input>
    <div  v-else  v-html="param.step8[0].jxff" style="width:700px;margin-top:16px;margin-left: 30px;"></div>
    <div style="margin-top:16px;font-weight:bolder">学习评价</div>
    <el-input
      v-if="isShowEdit" 
      type="textarea"
      placeholder="请输入内容"
      v-model="param.step8[0].xxpj"
      maxlength="15000"
      show-word-limit
      :rows="12"
      style="width:700px;margin-top:16px"
    >
    </el-input>
    <div  v-else  v-html="param.step8[0].xxpj" style="width:700px;margin-top:16px;margin-left: 30px;"></div>
    <div style="margin-top:16px;font-weight:bolder">质量管理</div>
    <el-input
      v-if="isShowEdit" 
      type="textarea"
      placeholder="请输入内容"
      v-model="param.step8[0].zlgl"
      maxlength="15000"
      show-word-limit
      :rows="12"
      style="width:700px;margin-top:16px"
    >
    </el-input>    <div  v-else  v-html="param.step8[0].zlgl" style="width:700px;margin-top:16px;margin-left: 30px;"></div><br>
    <el-button v-if="isShowEdit"  style="margin: 12px 0 0;"     type="warning" @click="toSave()">保存</el-button>
  </div>
</template>

<script>
export default {
  props: {
    param:   Object,
    isShowEdit:  Boolean
  },
  data() {
    return {};
  },
  methods:{
    toSave() {
      this.$emit('child');
      console.log("step8保存");
    }
  },
  mounted(){}
};
</script>

<style></style>
