<template>
  <div>
    <el-row>
      <el-col :span="24">
        <span v-if="col < 9"> {{ parameter[`name${col}`] }} </span>
        <span v-else-if="col == 9">{{ catchLilunTime(parameter[`name${col}`]) }}</span>
        <span v-else-if="col == 10">{{ catchAllTime(parameter[`name${col}`]) }}</span>
        <span v-else-if="col == 11">{{ catchTrueTime(parameter[`name${col}`]) }}</span>
      </el-col>
    </el-row>
    <!-- <el-button
      type="primary"
      size="mini"
      icon="el-icon-plus"
      v-if="ifadd"
      @click="add(parameter, col)"
    ></el-button> -->
    <!-- <el-button
      type="primary"
      size="mini"
      icon="el-icon-edit"
      v-if="ifedit"
      @click="edit(parameter, col)"
    ></el-button>
    <el-button
      type="danger"
      size="mini"
      icon="el-icon-delete"
      v-if="ifremove"
      @click="remove(parameter, col)"
    ></el-button>
    <el-button
      size="mini"
      icon="el-icon-arrow-up"
      v-if="ifup"
      @click="upOrDown(-1,parameter, col)"
    ></el-button>
    <el-button
      size="mini"
      icon="el-icon-arrow-down"
      v-if="ifdown"
      @click="upOrDown(1,parameter, col)"
    ></el-button> -->
    <!-- <edit ref="edit"></edit> -->
  </div>
</template>

<script>
// import edit from "./edit.vue";
import apiy from "@/api/subject/subjecttypes/subjecttypes.js";
import betterDebounce from "@/util/betterDebounce.js";
export default {
  props: {
    parameter: {
      type: Object,
      // default() {
      //   return {};
      // },
    },
    col: {
      type: Number,
    },
    coltitle: {
      type: String,
    },
    scope: {
      type: Object,
    },
  },
  data() {
    return {
      ifadd: false,
      ifedit: false,
      ifremove: false,
      ifup: false,
      ifdown: false,
      // numForNum: 0,
      // allTimeList:[],
      // lilunTimeList:[],
      // shixunTimeList:[],
    };
  },
  components: {
    // edit,
  },
  mounted() {
    this.showBtn();
    // console.log(this.parameter)
  },
  updated() {
    this.showBtn();
  },
  methods: {
    // add(parameter, col) {

    //   let editParameter = {
    //     parameter: parameter,
    //     col: col,
    //   };
    //   this.$refs.edit.open(editParameter, "添加-"+this.coltitle);

    // },
    // edit(parameter, col) {
    //   let editParameter = {
    //     parameter: parameter,
    //     col: col,
    //   };
    //   this.$refs.edit.open(editParameter, "编辑-"+this.coltitle);
    // },
    // remove(parameter, col)  {
    //      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
    //         let optId = parameter[`id${col}`]
    //         this.$eventBus.$emit("rmvEvent", optId);
    //      });
    // },
    // upOrDown(isUp,parameter, col) {
    //   let page = {isUp:isUp,  optId:parameter[`id${col}`],col:col}
    //   this.$eventBus.$emit("updownEvent", page);
    // },
    showBtn() {
      if (typeof this.parameter[`name${this.col}`] !== "undefined") {
        if (this.col !== 2) {
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
        }
      } else if (typeof this.parameter[`name${this.col}`] == "undefined") {
        if(this.col == 10){ 
          // console.log(parameter[`name${col}`])
          
          // let wordCopy = this.parameter[`name${this.col}`]
          // this.lilunTime = wordCopy.slice(0, wordCopy.indexOf("("))
        }
        this.ifadd = true;
        this.ifedit = false;
        this.ifremove = false;
        this.ifup = false;
        this.ifdown = false;
      }
    },
    catchAllTime(word) {
      let reg = "(";
      let reg2 = /实操/;
      let reg3 = ")";
      let wordCopy = this.parameter[`name8`];
      if (reg2.test(wordCopy)) {
        return Number(wordCopy.slice(0, wordCopy.indexOf("(")))
      } else {
        return wordCopy;
      }
    },
    catchLilunTime(word) {
      let reg = "(";
      let reg2 = /实操/;
      let reg3 = ")";
      let wordCopy = this.parameter[`name8`];
      if (reg2.test(wordCopy)) {
        return (Number(wordCopy.slice(0, wordCopy.indexOf("(")))-Number(wordCopy.slice(wordCopy.indexOf("操")+1, wordCopy.indexOf(")"))))
      } else {
        return wordCopy;
      }
    },
    catchTrueTime(word) {
      let reg = "(";
      let reg2 = /实操/;
      let reg3 = ")";
      let wordCopy = this.parameter[`name8`];
      if (reg2.test(wordCopy)) {
        return Number(wordCopy.slice(wordCopy.indexOf("操")+1, wordCopy.indexOf(")")))
      } else {
        return wordCopy;
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
