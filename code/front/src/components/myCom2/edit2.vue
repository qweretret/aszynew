<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogVisible"
      width="50%"
    >
      内容:
      <el-input v-model="input1"></el-input>
      总学时:
      <el-input v-model.number="input2"></el-input>
      实践学时:
      <el-input v-model.number="input3"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
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
      input1: "",
      input2: "",
      input3: "",
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

      if (this.title.substr(0, 2) == "编辑") {
        this.input1 = this.param.parameter.name2;
        let val8Arr = this.param.parameter.name3.split("(实操");
        this.input2 = val8Arr[0];
        this.input3 = val8Arr[1].substr(0, val8Arr[1].length - 1);
      }
    },
    //关闭弹框
    close() {
      this.param = {};
      this.input1 = "";
      this.input2 = "";
      this.input3 = "";
      this.dialogVisible = false;
    },
    //保存修改
    save() {
      if (this.title.substr(0, 2) == "添加") {
        if (this.input1.trim() != ""&&this.input2!= ""&&this.input3!= "") {
          // this.param.parameter.name1
          this.param.parameter.name2 = this.input1;
          //通过消息触发全局事件 交个tavle.vue处理
          let reg = /[^\d\.]/g;
          if (
            new RegExp(reg).test(this.input2) ||
            new RegExp(reg).test(this.input3)
          ) {
            //非数字
            this.$message({ message: "请输入正确学时的格式", type: "warning" });
            return;
          } else {
            this.param.input2 = this.input2;
            this.param.input3 = this.input3;
            console.log(this.param);
            this.$eventBus.$emit("addEvent2", this.param);
            this.close();
          }
        } else {
          this.$utils.msg.warning("请输入内容");
          this.input = "";
        }
      } else if (this.title.substr(0, 2) == "编辑") {
        if (this.input1.trim() != ""&&this.input2!= ""&&this.input3!= "") {
        let reg = /[^\d\.]/g;
        if (
          new RegExp(reg).test(this.input2) ||
          new RegExp(reg).test(this.input3)
        ) {
          //非数字
          this.$message({ message: "请输入正确学时的格式", type: "warning" });
          return;
        }
        this.param.parameter.name2 = this.input1;
        this.param.input2 = this.input2;
        this.param.input3 = this.input3;
        //消息驱动        触发editEvent，传param参数进去
        console.log(this.param);
        this.$eventBus.$emit("editEvent2", this.param);
        this.close();
        }else{
           this.$utils.msg.warning("请输入内容");
        };
      }
    },
  },
};
</script>

<style></style>
