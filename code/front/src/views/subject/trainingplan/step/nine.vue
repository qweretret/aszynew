<template>
  <!-- 毕业要求 -->
  <div>
    <el-input
      v-if="isShowEdit" 
      type="textarea"
      placeholder="请输入内容"
      v-model="param.step9[0].requirement"
      maxlength="15000"
      show-word-limit
      :rows="12"
      style="width: 700px"
    >
    </el-input>
    <div  v-else  v-html="param.step9[0].requirement" style="width:700px;margin-top:16px;margin-left: 30px;"></div>
    <br />
    <el-button v-if="isShowEdit"  style="margin: 12px 0 0;" type="warning" @click="toSave()">保存</el-button>
  </div>
</template>

<script>
export default {
  props: {
    param: Object,
    isShowEdit:  Boolean
  },
  data() {
    return {};
  },
  methods: {
    toSave() {
      this.$emit("child");
      console.log("step9保存");
    },
  },
  mounted() {},
};
</script>

<style></style>
