<template>
  <div>
    <el-row>
      <el-col :span="24">
        <span>{{ parameter.name2}}</span>
      </el-col>
    </el-row>
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-edit"
      @click="edit(parameter)"
    ></el-button>
    <el-button
      type="danger"
      size="mini"
      icon="el-icon-delete"
      @click="remove(parameter)"
    ></el-button>
    <edit ref="edit"></edit>
  </div>
</template>

<script>
import edit from "./edit.vue";
import apiy from "@/api/subject/subjecttypes/subjecttypes.js";
import betterDebounce from "@/util/betterDebounce.js";
export default {
  props: {
    parameter: {
      type: Object,
    },
    coltitle: {
      type: String,
    },
    scope: {
      type: Object,
    }
  },
  data() {
    return {
      ifup: false,
      ifdown: false,
    };
  },
  components: {
    edit,
  },
  methods: {
    edit(parameter) {
      let editParameter = {
        parameter: parameter,
        col: 2,
      };
      this.$refs.edit.open(editParameter, "编辑-" + this.coltitle);
    },
    remove(parameter, col) {
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        let optId = parameter.id2;
        if(optId){
          console.log("optId = ",optId)
          // 消息监听传递修改操作
          this.$eventBus.$emit("rmvAssocEvent", optId);
        }
      });
    }

  },
  watch: {
    parameter(val) {
      this.parameter = val;
    },
  },
};
</script>

<style lang="scss" scoped="scoped">
.el-button {
  padding: 5px !important;
  margin: 1px !important;
}
</style>
