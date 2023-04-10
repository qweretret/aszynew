<template>
  <div>
    <el-row>
      <el-col :span="24">
        <span v-if="col==1">{{ parameter[`name${col}`] }}</span>
        <span v-if="col==2">{{ parameter.idx }}.{{ parameter[`name${col}`] }}</span>
      </el-col>
    </el-row>
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-plus"
      @click="add(parameter, col)"
    ></el-button>
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-edit"
      v-if="isShow()"
      @click="edit(parameter, col)"
    ></el-button>
    <el-button
      type="danger"
      size="mini"
      icon="el-icon-delete"
      v-if="isShow()"
      @click="remove(parameter, col)"
    ></el-button>
    <el-button
      size="mini"
      icon="el-icon-arrow-up"
      v-if="isShow()"
      @click="upOrDown(-1, parameter, col)"
    ></el-button>
    <el-button
      size="mini"
      icon="el-icon-arrow-down"
      v-if="isShow()"
      @click="upOrDown(1, parameter, col)"
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
    col: {
      type: Number,
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
    add(parameter, col) {
    
      let editParameter = {
        parameter: parameter,
        col: col,
      };
      this.$refs.edit.open(editParameter, "添加-" + this.coltitle);
    },
    edit(parameter, col) {
      let editParameter = {
        parameter: parameter,
        col: col,
      };
      this.$refs.edit.open(editParameter, "编辑-" + this.coltitle);
    },
    remove(parameter, col) {
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        let optId = parameter[`id${col}`];
        this.$eventBus.$emit("rmvEvent", optId);
      });
    },
    upOrDown(isUp, parameter, col) {
      let page = { isUp: isUp, optId: parameter[`id${col}`], col: col };
      this.$eventBus.$emit("updownEvent", page);
    } ,isShow(){
     
        if(this.col==2){
           return this.parameter.name2!=undefined
        }
        return true
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
