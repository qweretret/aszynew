<template>
  <div style="width: 90%; margin: 0 auto;">
    <div slot="body" style="margin-bottom: 20px;">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="课程概述" name="seconds">
          <div>
            课程概述
            <el-input :disabled="disabled" type="textarea" :rows="6" v-model="courseTwo.desc"></el-input>
          </div>
        </el-tab-pane>
        <el-tab-pane label="课程所对应职业标准及工作任务" name="firsts">
          <el-table :data="listData1" :span-method="objectSpanMethod" border style="width: 100%">
            <el-table-column prop="name1" label="课程标准"> </el-table-column>
            <el-table-column prop="name2" label="任务"> </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <!--div slot="footer" style="display: flex; flex-direction: row; justify-content: center;">
      <el-button v-if="!disabled && activeName=='seconds'" type="warning" :loading="confirmLoading" @click="save()">保存</el-button>
    </div-->
  </div>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api_q from "@/api/subject/coursetypes/coursetypes.js";
import coremodelApi from "@/api/subject/coremodel/coremodel.js";
export default {
 // props: { csdata: Object, courseid: String },
  name: "list",
  components: {
    popup,
  },
  data() {
    return {
    
      list1: [],
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      activeName: "seconds",

      courseTwo:{}  ,
      csdata:{}  ,
      courseid: "",

      rules: {},
      listData: [],
      listData1: [],
      listData2: [],
    };
  },
  methods: {
    //查看是否有children
    hasChildren(row, column) {
      if (row.children) {
        let aa = 0;
        row.children.forEach((item) => {
          if (aa == 0) {
            --column;
            aa++;
          }
          if (column == 0) {
            this.list1.push(item);
          }
          while (column > 0) {
            this.hasChildren(item, column);
            break;
          }
        });
      }
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        if (row.typeIndex) {
          return {
            rowspan: row.typeIndex,
            colspan: 1,
          };
        } else {
          return {
            rowspan: 0,
            colspan: 0,
          };
        }
      }
    },
    initView(csdata,courseid,  disabled ) {
      this.disabled=disabled
      this.csdata=csdata,
      this.courseid=courseid
      this.courseTwo =this.csdata.two

      this.listData2 = this.listData.filter((item) => {
        return item.id === this.courseid;
      });
      // this.$nextTick(() => {
      coremodelApi.list({}, (response) => {
        this.listData = response.data.records;
        // let arr = [];
        let arr = this.listData[0];
        let le = JSON.parse(arr.coremodel);
        //console.log(le);
        // let rw=[];
        for (let j = 0; j < le.length; j++) {
          let obj = {
            name: "",
            id: "",
            children: [],
          };
          obj.name = le[j].name;

          obj.id = le[j].id;
          le[j].children[0].children.forEach((item) => {
            let yyy = {
              id: item.id,
              name: item.name,
            };
            obj.children.push(yyy);
          });
         // console.log(obj);
          this.listData1.push(obj);
        //  console.log(this.listData1);
        }
       // console.log(this.listData1);
        let getDate = []; // 存储新表格数据
        let typeIndex = [0]; // 保存id,地区需要合并的值
        this.listData1.forEach((v, index) => {
          if (v.children && v.children.length) {
            for (let index = 0; index < v.children.length; index++) {
              if (index == v.children.length - 1) {
                typeIndex.push(v.children.length);
              }
              getDate.push({
                id: v.id,
                name1: v.name,
                name2: v.children[index].name,
              });
            }
  
          }
        });

        let t = 0;

        typeIndex.forEach((v, i, typeArr) => {
          if (typeArr[i + 1]) {
            getDate[t].typeIndex = typeArr[i + 1];
            t += typeArr[i + 1];
          }
        });
        this.listData1 = getDate;
      });
    },
    handleClick() {

     },    //清空数据
    clear() {
       this.courseTwo = {};
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
