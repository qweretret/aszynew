<template>
  <div>
    <el-row>
      <el-col :span="24">
        <span>{{ parameter[`name${col}`] }}</span>
      </el-col>
    </el-row>
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-plus"
      v-if="ifadd && display"
      @click="add(parameter, col,id)"
    ></el-button>
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-edit"
      v-if="ifedit && display"
      @click="edit(parameter, col,id)"
    ></el-button>
    <el-button
      type="danger"
      size="mini"
      icon="el-icon-delete"
      v-if="ifremove && display"
      @click="remove(parameter, col)"
    ></el-button>
    <el-button
      size="mini"
      icon="el-icon-arrow-up"
      v-if="ifup && display"
      @click="upOrDown(-1, parameter, col)"
    ></el-button>
    <el-button
      size="mini"
      icon="el-icon-arrow-down"
      v-if="ifdown && display"
      @click="upOrDown(1, parameter, col)"
    ></el-button>
    <edit ref="edit"></edit>
    <edit2 ref="edit2"></edit2>
  </div>
</template>

<script>
import edit from "./edit.vue";
import edit2 from "./edit2.vue";
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
    id: {
      type: String,
    },
    scope: {
      type: Object,
    },
    display: {
      type: Boolean,
    },
  },
  data() {
    return {
      ifadd: false,
      ifedit: false,
      ifremove: false,
      ifup: false,
      ifdown: false,
    };
  },
  components: {
    edit,
    edit2,
  },
  mounted() {
    this.showBtn();
  },
  updated() {
    this.showBtn();
  },
  methods: {
    add(parameter, col,id) {
      // alert("添加"+id);
      console.log(id);
      let editParameter = {
        parameter: parameter,
        col: col,
      };
      if (col == 7) {
       // alert(id);
        this.$refs.edit2.open(editParameter, "添加-" + this.coltitle,id);
      } else {
        this.$refs.edit.open(editParameter, "添加-" + this.coltitle);
      }
    },
    edit(parameter, col,id) {
      let editParameter = {
        parameter: parameter,
        col: col,
      };
      if (col == 7) {
        this.$refs.edit2.open(editParameter, "编辑-" + this.coltitle,id);
      } else {
        this.$refs.edit.open(editParameter, "编辑-" + this.coltitle);
      }
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
    },
    showBtn() {
      if (typeof this.parameter[`name${this.col}`] !== "undefined") {
        if (this.coltitle == "态度和职业素养") {
          this.ifadd = true;
          this.ifedit = true;
          this.ifremove = true;
          this.ifup = true;
          this.ifdown = true;
        } else if (this.col == 2) {
          this.ifadd = false;
          this.ifedit = true;
          this.ifremove = false;
          this.ifup = false;
          this.ifdown = false;
        } else if (this.col == 7) {
          this.ifadd = false;
          this.ifedit = true;
          this.ifremove = true;
          this.ifup = false;
          this.ifdown = false;
        } else if (this.col == 8) {
          this.ifadd = false;
          this.ifedit = false;
          this.ifremove = false;
          this.ifup = false;
          this.ifdown = false;
        } else {
          this.ifadd = true;
          this.ifedit = true;
          this.ifremove = true;
          this.ifup = true;
          this.ifdown = true;
        }
      } else if (typeof this.parameter[`name${this.col}`] == "undefined") {
        if (this.col == 8) {
          this.ifadd = false;
          this.ifedit = false;
          this.ifremove = false;
          this.ifup = false;
          this.ifdown = false;
        } else {
          this.ifadd = true;
          this.ifedit = false;
          this.ifremove = false;
          this.ifup = false;
          this.ifdown = false;
        }
      }
    },
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
