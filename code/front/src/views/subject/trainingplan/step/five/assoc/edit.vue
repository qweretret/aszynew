<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-input v-model="input" placeholder="请输入内容"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="update">修改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import apiy from "@/api/subject/subjecttypes/subjecttypes.js";
import betterDebounce from "@/util/betterDebounce.js";
export default {
  data() {
    return {
      dialogVisible: this.display,
      input: "",
      param: {},
      title: "",
    };
  },
  methods: {
    //打开弹框
    open(parameter, title) {
      this.param = parameter;
      this.title = title;
      this.dialogVisible = true;
      console.log(this.param);
       if (this.title.substr(0,2) == "编辑") {
        /*  let x = {name:"tom"}
            x.name  x["name"]  伪数据*/
        //this.input = this.param.parameter["name"+this.param.col];
        this.input = this.param.parameter[`name${this.param.col}`];
      }
    },
    //关闭弹框
    close() {
      this.param = {};
      this.input = "";
      this.dialogVisible = false;
    },
    //保存修改
    update() {
            //回显
            this.param.parameter[`name${this.param.col}`]=this.input
            //消息驱动        触发editEvent，传param参数进去
            this.$eventBus.$emit("editAssocEvent",this.param.parameter);
            this.close();
      
    }
  },
};
</script>

<style></style>
