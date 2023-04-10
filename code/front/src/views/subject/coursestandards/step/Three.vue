<template>
  <div style="width: 90%; margin: 0 auto;">
    <div slot="body" style="margin-bottom: 20px;">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="知识目标. 技能目标. 职业素养" name="tab1">
          <el-form :disabled="disabled">
            <el-form-item label="知识目标">
              <el-input type="textarea" :rows="4" v-model="courseThree.target"></el-input>
            </el-form-item>
            <el-form-item label="技能目标">
              <el-input type="textarea" :rows="4" v-model="courseThree.skill"></el-input>
            </el-form-item>
            <el-form-item label="职业素养">
              <el-input type="textarea" :rows="4" v-model="courseThree.accomplishment"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="课程能力点列表" name="tab2">
          <!-- TODO 不会-->
          <el-table border style="width: 100%; margin-top: 20px">
            <el-table-column prop="name" label="主要工作任务"></el-table-column>
            <el-table-column prop="amount1" label="工作内容"> </el-table-column>
            <el-table-column prop="amount2" label="	能力点"> </el-table-column>
            <el-table-column prop="amount3" label="	知识点"> </el-table-column>
            <el-input type="textarea" :rows="4" v-model="courseThree.skill"></el-input>
            <el-input type="textarea" :rows="4" v-model="courseThree.skill"></el-input>
            <el-input type="textarea" :rows="4" v-model="courseThree.skill"></el-input>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
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
 // props: { csdata: Object },
  name: "list",
  components: {
    popup,
    api_q,
  },
  data() {
    return {
      disabled: false,
      confirmLoading: false,
      activeName: "tab1",
      parameter: {},
      courseThree: {},
      csdata:{},
      tableData: [],
    };
  },
  methods: {
    initView(csdata,  disabled ) {
      this.disabled=disabled
      this.csdata=csdata
      this.courseThree =  this.csdata.three

      api_q.list({}, (response) => {
        this.tableData = response.data.records;
        this.listLoading = false;
      });
    },
    handleClick() { },
    //清空数据
    clear() {
      this.courseThree = {};
    },
    save() {
      this.$emit("merge");
    }, 
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
