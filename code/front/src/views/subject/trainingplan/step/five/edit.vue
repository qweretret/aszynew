<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-form
        class="dataForm"
        ref="form"
        :disabled="disabled"
        :model="parameter"
        :inline="true"
      >
        <el-row>
          <el-col :span="24">
            <span>专业课程</span>
            <el-form-item prop="name">
              <el-select
                v-model="parameter.name"
                value-key="id"
                @change="getCourseName"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in listData"
                  :key="item.id"
                  :label="item.name"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">关闭窗口</el-button>
        <el-button type="warning" @click="save">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api/subject/course/course.js";
export default {
  data() {
    return {
      dialogVisible: this.display,
      input: "",
      param: {},
      disabled: false,
      parameter: {},
      listData: [],
      title: "",
      // code: "",
      // cid: "",
      kcxz: "",
      kcmc: "",
    };
  },
  props: {
    params: {
      type: Object,
    },
  },
  inject: ["subjectId"],
  methods: {
    getCourseName(e) {
      // console.log(e, "e000");
      this.parameter.name = e.name;
      this.kcxz = e.dbColumn_level1 + "-" + e.dbColumn_level2;
      this.kcmc = e.name;
    },
    getCourseData() {
      let params = {subId:this.subjectId};
      api.list(params, (response) => {
        this.listData = response.data;
      });
    },
    //打开弹框
    open(parameter, title, disabled) {
      this.getCourseData();
      this.disabled = disabled;
      this.param = parameter;
      this.title = title;
      this.dialogVisible = true;
    },
    //关闭弹框
    close() {
      this.param = {};
      this.input = "";
      this.disabled = false;
      this.dialogVisible = false;
    },
    //保存修改
    save() {
       this.$eventBus.$emit("saveSubject", { kcmc: this.kcmc, kcxz: this.kcxz });
    },
  },
};
</script>

<style></style>
