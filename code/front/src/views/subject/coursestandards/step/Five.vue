<template>
  <div style="width: 90%; margin: 0 auto;">
    <el-form  :disabled="disabled" >
      <el-form-item label="课程合格标准">
        <el-input   :rows="6"  type="textarea" v-model="courseFive.desc"></el-input>
      </el-form-item>
    </el-form>
    <!--div slot="footer" style="display: flex; flex-direction: row; justify-content: center;">
      <el-button v-if="!disabled" type="warning" :loading="confirmLoading" @click="save()">保存</el-button>
    </div-->
  </div>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api_q from "@/api/subject/coursetypes/coursetypes.js";
export default {
  //props: { csdata: Object },
  name: "list",
  components: {
    popup,
    api_q,
  },
  data() {
    return {
      confirmLoading: false,
      disabled: false,
      courseFive: {},
      csdata:{}
    };
  },
  methods: {
    save() {
      this.$emit("merge" );
    },
    //清空数据
    clear() {
      this.courseFive = {};
    },
     initView(csdata,  disabled ) {
      this.disabled=disabled
    this.csdata=csdata
    this.courseFive = this.csdata.five
    },
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
