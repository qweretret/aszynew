<template>
  <div style="width: 90%; margin: 0 auto;">
    <el-form :disabled="disabled">
      <el-row>
        <el-col :span="12">
          <el-form-item label="师资队伍">
            <el-input type="textarea" :rows="6" v-model="courseSix.teacherTeam"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="教学设施">
            <el-input type="textarea" :rows="6" v-model="courseSix.facilities"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="教学资源">
            <el-input type="textarea" :rows="6" v-model="courseSix.resources"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="教学建议">
            <el-input type="textarea" :rows="6" v-model="courseSix.suggest"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
    <!--div slot="footer" style="display: flex; flex-direction: row; justify-content: center;">
      <el-button v-if="!disabled" type="warning" :loading="confirmLoading" @click="save()">保存</el-button>
    </div-->
  </div>
</template>


<script>
import popup from "@/components/popup/drawerPopup.vue";
// import course_api from "@/api/subject/course/course.js";
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
      disabled: false,
      confirmLoading: false,
      courseSix: {},
      csdata: {}
    };
  },
  methods: {
    save() {
      this.$emit("merge");
    },
    //清空数据
    clear() {
      this.courseSix = {};
    },
    initView(csdata,  disabled ) {
      this.disabled=disabled
      this.csdata = csdata
      this.courseSix = this.csdata.six
    }
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
