<template>
  <div>
    <el-dialog title="课程安排信息" :visible.sync="disabled" width="90%" :before-close="handleDialogClose">
      <div class="head">
        <!-- <div>课程名称：{{ coursePlanData.dbColumn_csid }}</div> -->
        <div>课程名称：{{ parameter.name }}</div>
        <br />
        <!-- <div>总课时:{{ coursePlanData.period }}</div> -->
        <div>总课时:{{ parameter.period }}</div>
        <!-- <div>总理论课时:{{ coursePlanData.theoryperiod }}</div> -->
        <div>总理论课时:{{ parameter.theoryperiod }}</div>
        <!-- <div>总实践课时:{{ coursePlanData.actperiod }}</div> -->
        <div>总实践课时:{{ parameter.actperiod }}</div>
      </div>
      <br />
      <br />
      <!-- :span-method="arraySpanMethod" -->
      <!-- v-for="(item1, idx) in timeData"
              :key="idx" -->
      <div class="table" v-for="item in weekData" :key="item.id">
        <el-table border style="width: 100%" :data="item.child">
          <el-table-column :label="item.year" align="center">
            <el-table-column :label="item.t1" align="center">
              <el-table-column label="理论课总学时" prop="t1">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.t1"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周数" prop="tw1">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.tw1"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周平均学时">
                <template slot-scope="scope">
                  <el-input :value="
                    scope.row.tw1 != 0 ? scope.row.t1 / scope.row.tw1 : 0
                  " :disabled="true"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课总学时" prop="p1">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.p1"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周数" prop="pw1"><template slot-scope="scope">
                  <el-input v-model="scope.row.pw1"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周平均学时"><template slot-scope="scope">
                  <el-input :value="scope.row.pw1 != 0 ? scope.row.p1 / scope.row.pw1 : 0" :disabled="true"></el-input>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column :label="item.t2" align="center">
              <el-table-column label="理论课总学时" prop="t2">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.t2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周数" prop="tw2">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.tw2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周平均学时">
                <template slot-scope="scope">
                  <el-input :value="scope.row.tw2 != 0 ? scope.row.t2 / scope.row.tw2 : 0" :disabled="true"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课总学时" prop="p2">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.p2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周数" prop="pw2"><template slot-scope="scope">
                  <el-input v-model="scope.row.pw2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周平均学时"><template slot-scope="scope">
                  <el-input :value="scope.row.pw2 != 0 ? scope.row.p2 / scope.row.pw2 : 0" :disabled="true"></el-input>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column :label="item.t3" align="center">
              <el-table-column label="理论课总学时" prop="t3">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.t3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周数" prop="tw3">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.tw3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周平均学时">
                <template slot-scope="scope">
                  <el-input :value="scope.row.tw3 != 0 ? scope.row.t3 / scope.row.tw3 : 0" :disabled="true"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课总学时" prop="p3">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.p3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周数" prop="pw3"><template slot-scope="scope">
                  <el-input v-model="scope.row.pw3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周平均学时">
                <template slot-scope="scope">
                  <el-input :value="scope.row.pw3 != 0 ? scope.row.p3 / scope.row.pw3 : 0" :disabled="true"></el-input>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table-column>
        </el-table>
      </div>
      <el-button type="defalut" @click="close">关闭</el-button>
      <el-button type="primary" @click="confirm">保存</el-button>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api/subject/coursetimeplan/coursetimeplan.js";
import weekApi from "@/api/subject/weekinterm/weekinterm.js";
import coursePlan from "@/api/subject/courseplan/courseplan.js";
import updataCourseTimePlanApi from "@/api/subject/trainingplan/trainingplan.js";
export default {
  name: "edit",
  inject: ["sid","refreshData"],
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      hasData: true, //课时安排是否有数据
      parameter: {},
      formParameter: {},
      weekData: [], //合并后的数据（总数据）
      nullData: [],
      coursePlanData: {},
      currentYear: "",//当前专业的年级
      // timeData: [
      //   "理论课总学时",
      //   "理论课周数",
      //   "理论课周平均学时",
      //   "实践课总学时",
      //   "实践课周数",
      //   "实践课周平均学时",
      // ],
      timePlanData: [], //课时安排数据
    };
  },
  methods: {
    //打开弹框
    open(parameter, currentYear) {
      this.parameter = parameter;
      this.disabled = true;
      this.$nextTick(() => {
        this.init();
      });
      this.currentYear = currentYear;
    },
    //关闭弹框
    close() {
      this.disabled = false;
      this.weekData.length = 0;
    },
    handleDialogClose() {
      this.disabled = false;
      this.weekData.length = 0;
    },
    //提交表单
    confirm() {
      // this.weekData.cid = this.parameter.cid
      let planParameter = [];
      this.weekData.forEach((item) => {
        for (let index = 0; index < 3; index++) {
          if (this.hasData) {
            let obj = {
              id: item.child[0][`id${index + 1}`],
              cid: this.parameter.cid,
              year: item.year.split("年")[0],
              term: index + 1,
              theorytime: item.child[0][`t${index + 1}`],
              theoryweek: item.child[0][`tw${index + 1}`],
              practicetime: item.child[0][`p${index + 1}`],
              praticeweek: item.child[0][`pw${index + 1}`],
              grade: this.currentYear,
            };
            planParameter.push(obj);
          } else {
            let obj = {
              cid: this.parameter.cid,
              year: item.year.split("年")[0],
              term: index + 1,
              theorytime: item.child[0][`t${index + 1}`],
              theoryweek: item.child[0][`tw${index + 1}`],
              practicetime: item.child[0][`p${index + 1}`],
              praticeweek: item.child[0][`pw${index + 1}`],
              grade: this.currentYear,
            };
            planParameter.push(obj);
          }
        }
      });
      // if (this.hasData) {
      //   // api.update(planParameter, (res) => {
      //   //   this.$utils.msg.success(res.msg);
      //   // });

      //   console.log(planParameter);

      // }
      // else {
      //   api.save(planParameter, (res) => {
      //     this.$utils.msg.success(res.msg);
      //   });
      updataCourseTimePlanApi.updataCourseTimePlan(planParameter, (res) => {
        this.$utils.msg.success(res.msg);
        // console.log(planParameter, '保存传达的参数');
        this.close();
        this.refreshData();
      })
      // }
    },
    init() {
      coursePlan.toUpdate({
        csid: this.parameter.id,
        sid: this.sid
      }, res => {
        this.coursePlanData = res.data
      })
      //学期周安排数据,将三个学期根据学年合并为一条数据
      weekApi.list({}, (res) => {
        let temp = res.data.records;
        // this.weekData = res.data.records
        let num = 0;
        let obj = {
          id: "",
          year: "",
          t1: "",
          t2: "",
          t3: "",
          child: [],
        };
        temp.forEach((item) => {
          if (num == 0) {
            obj = {
              id: item.id,
              year: item.year + "年",
              t1: "第一学期（" + item.weeks + ")",
              t2: "",
              t3: "",
              child: [],
            };
            num++;
          } else if (num == 1) {
            obj.t2 = "第二学期（" + item.weeks + ")";
            num++;
          } else {
            obj.t3 = "暑假（" + item.weeks + ")";
            this.weekData.push(obj);
            num = 0;
            obj = {
              id: "",
              year: "",
              t1: "",
              t2: "",
              t3: "",
              child: [],
            };
          }
        }
        );
        //课时安排数据，也是三条数据根据学年合并为一条，当数据为空时，使课时初始值为0
        api.list({ cid: this.parameter.id, grade: this.currentYear,sid:this.sid }, (res) => {
          this.timePlanData = [];
          if (res.data.length == 0) {
            this.hasData = false;
            for (let index = 0; index < this.weekData.length; index++) {
              let obj = {
                t1: 0,
                tw1: 0,
                p1: 0,
                pw1: 0,
                t2: 0,
                tw2: 0,
                p2: 0,
                pw2: 0,
                t3: 0,
                tw3: 0,
                p3: 0,
                pw3: 0,
              };
              this.timePlanData.push(obj);
            }
          } else {
            let temp = res.data.courseTMplan;
            let num = 0;
            let obj = {
              t1: 0,
              tw1: 0,
              p1: 0,
              pw1: 0,
              t2: 0,
              tw2: 0,
              p2: 0,
              pw2: 0,
              t3: 0,
              tw3: 0,
              p3: 0,
              pw3: 0,
            };
            temp.forEach((item) => {
              if (num == 0) {
                obj.id = item.id;
                obj.id1 = item.id;
                obj.t1 = item.theorytime;
                obj.tw1 = item.theoryweek;
                obj.p1 = item.practicetime;
                obj.pw1 = item.praticeweek;
                num++;
              } else if (num == 1) {
                obj.id2 = item.id;
                obj.t2 = item.theorytime;
                obj.tw2 = item.theoryweek;
                obj.p2 = item.practicetime;
                obj.pw2 = item.praticeweek;
                num++;
              } else {
                obj.id3 = item.id;
                obj.t3 = item.theorytime;
                obj.tw3 = item.theoryweek;
                obj.p3 = item.practicetime;
                obj.pw3 = item.praticeweek;
                this.timePlanData.push(obj);
                obj = {
                  t1: 0,
                  tw1: 0,
                  p1: 0,
                  pw1: 0,
                  t2: 0,
                  tw2: 0,
                  p2: 0,
                  pw2: 0,
                  t3: 0,
                  tw3: 0,
                  p3: 0,
                  pw3: 0,
                };
                num = 0;
              }
            });
          }
          
          //将课时安排的数据和学期周数据合并
          for (let index = 0; index < this.weekData.length; index++) {
            this.weekData[index].child.push(this.timePlanData[index]);
          }
        });
      });
    },
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
