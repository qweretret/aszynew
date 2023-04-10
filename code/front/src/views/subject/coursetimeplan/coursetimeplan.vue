<template>
  <div>
    <el-dialog :title="title" :visible.sync="disabled" width="90%" :before-close="handleDialogClose">
      <div class="head">
        <div>课程名称：{{parameter.name}},{{grade}}
          <!--el-date-picker
            v-model="grade"
            size="mini"
            type="year"
            @change="changeGrade"
            placeholder="选择届">
         </el-date-picker -->
        </div>

        <br />
        <div>总课时:{{coursePlanData.period}}</div>
        <div>总理论课时:{{coursePlanData.theoryperiod}}</div>
        <div>总实践课时:{{coursePlanData.actperiod}}</div>
      </div>
      <br />
      <br />
      <div class="table" v-for="item in yearDatas" :key="item.id">
        <el-table border style="width: 100%" :data="item.child">
          <el-table-column :label="item.year" align="center">
            <el-table-column :label="item.t1+' 周'" align="center">
              <el-table-column label="理论课总学时" @blur="checkData" prop="t1">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.t1"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周数" prop="tw1">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.tw1"></el-input>
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
                  <el-input @blur="checkData" v-model="scope.row.p1"></el-input>
                </template>
              </el-table-column>
              <el-table-column @blur="checkData" label="实践课周数" prop="pw1"><template slot-scope="scope">
                  <el-input v-model="scope.row.pw1"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周平均学时"><template slot-scope="scope">
                  <el-input :value="scope.row.pw1!=0?scope.row.p1 / scope.row.pw1:0" :disabled="true"></el-input>
                </template>
              </el-table-column>
            </el-table-column>

            <el-table-column :label="item.t2+' 周'" align="center">
              <el-table-column style="background-color: #336633 !important ;" label="理论课总学时222" prop="t2">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.t2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周数" prop="tw2">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.tw2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周平均学时">
                <template slot-scope="scope">
                  <el-input :value="scope.row.tw2!=0?scope.row.t2 / scope.row.tw2:0" :disabled="true"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课总学时" prop="p2">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.p2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周数" prop="pw2"><template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.pw2"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周平均学时"><template slot-scope="scope">
                  <el-input :value="scope.row.pw2!=0?scope.row.p2 / scope.row.pw2:0" :disabled="true"></el-input>
                </template>
              </el-table-column>
            </el-table-column>

            <el-table-column :label="item.t3+' 周'" align="center">
              <el-table-column label="理论课总学时333" prop="t3">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.t3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周数" prop="tw3">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.tw3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="理论课周平均学时">
                <template slot-scope="scope">
                  <el-input :value="scope.row.tw3!=0?scope.row.t3 / scope.row.tw3:0" :disabled="true"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课总学时" prop="p3">
                <template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.p3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周数" prop="pw3"><template slot-scope="scope">
                  <el-input @blur="checkData" v-model="scope.row.pw3"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="实践课周平均学时">
                <template slot-scope="scope">
                  <el-input :value="scope.row.pw3!=0?scope.row.p3 / scope.row.pw3:0" :disabled="true"></el-input>
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
import coursePlan from "@/api/subject/courseplan/courseplan.js"

export default {
  name: "edit",
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      hasData: true,  //课时安排是否有数据
      parameter: {},  //课程
      yearDatas: [],  //合并后的数据（总数据）

      title: '',
      grade: 2022,
      grades: [],
      coursePlanData: {},
      timePlanData: [], //课时安排数据
      tcnt: 0,  //理论总学时
      pcnt: 0  //实践总学时
    };
  },
  methods: {
    //验证数据完整性
    checkData() {
      //console.log(this.timePlanData);
      //重置理论总学时
      this.tcnt = 0
      //重置实践总学时
      this.pcnt = 0

      this.timePlanData.forEach((itm, i) => {

        for (let idx = 1; idx <= 3; idx++) {
          //伪数组    itm[`t`+1]  == itm.t1
          if (itm[`t` + idx]) {
            if (itm[`tw` + idx]) {
              if (itm[`t` + idx] % itm[`tw` + idx] > 0) {
                this.$utils.msg.warning("每周上课不能出现小数");
                //reset
                itm[`t` + idx] = 0
                itm[`tw` + idx] = 0
              }
            }

            //校验理论总学时
            this.tcnt += itm[`t` + idx] * 1
            console.log(this.tcnt, this.coursePlanData.theoryperiod, this.tcnt > this.coursePlanData.theoryperiod);
            if (this.tcnt > this.coursePlanData.theoryperiod) {
              this.$utils.msg.warning("理论总学时超出范围");
              //reset
              itm[`t` + idx] = 0
            }
          }

          if (itm[`p` + idx]) {
            if (itm[`pw` + idx]) {
              if (itm[`p` + idx] % itm[`pw` + idx] > 0) {
                this.$utils.msg.warning("每周上课不能出现小数");
                //reset
                itm[`p` + idx] = 0
                itm[`pw` + idx] = 0
              }
            }

            //校验实践总学时
            this.pcnt += itm[`p` + idx] * 1
            if (this.pcnt > this.coursePlanData.actperiod) {
              this.$utils.msg.warning("实践总学时超出范围");
              //reset
              itm[`p` + idx] = 0
            }
          }
        }
      })

    },
    changeGrade(val) {

      this.grade = val.getFullYear() + ""
      //学期周安排数据,将三个学期根据学年合并为一条数据
      this.yearDatas = []
      weekApi.list2({ year: this.grade }, (res) => {
        let temp = res.data.records;

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
            this.yearDatas.push(obj);
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
        });
        //课时安排数据，也是三条数据根据学年合并为一条，当数据为空时，使课时初始值为0
        api.list({ cid: this.parameter.id, grade: this.grade }, (res) => {
          this.timePlanData = [];
          if (res.data.length == 0) {
            this.hasData = false;
            for (let index = 0; index < this.yearDatas.length; index++) {
              //一个学年
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
            let temp = res.data;
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
          for (let index = 0; index < this.yearDatas.length; index++) {
            this.yearDatas[index].child.push(this.timePlanData[index]);
          }
        });
      });
    },
    //打开弹框
    open(course) {
      this.parameter = course;
      console.log("parameter", this.parameter)
      this.disabled = true;
      this.$nextTick(() => {
        this.init();
      });
    },
    //关闭弹框
    close() {
      this.disabled = false;
      this.yearDatas.length = 0;
    },
    handleDialogClose() {
      this.disabled = false;
      this.yearDatas.length = 0;
    },
    //提交表单
    confirm() {
      // 先进行总数校验
      if (this.tcnt != this.coursePlanData.theoryperiod) {
        this.$utils.msg.warning("理论总学时[" + this.tcnt + "]小于定义的" + this.coursePlanData.theoryperiod + "，请检查");
        return
      } else if (this.pcnt != this.coursePlanData.actperiod) {
        this.$utils.msg.warning("实践总学时[" + this.pcnt + "]小于定义的" + this.coursePlanData.actperiod + "，请检查");
        return
      }

      let planParameter = [];
      
      this.yearDatas.forEach((item) => {
        for (let index = 0; index < 3; index++) {
          if (this.hasData) {
            let obj = {
              id: item.child[0][`id${index + 1}`],
              cid: this.parameter.id,
              year: item.year.split("年")[0],
              grade: this.grade,
              term: index + 1,
              theorytime: item.child[0][`t${index + 1}`],
              theoryweek: item.child[0][`tw${index + 1}`],
              practicetime: item.child[0][`p${index + 1}`],
              praticeweek: item.child[0][`pw${index + 1}`],
            };
            planParameter.push(obj);
          } else {
            let obj = {
              cid: this.parameter.id,
              year: item.year.split("年")[0],
              grade: this.grade,
              term: index + 1,
              theorytime: item.child[0][`t${index + 1}`],
              theoryweek: item.child[0][`tw${index + 1}`],
              practicetime: item.child[0][`p${index + 1}`],
              praticeweek: item.child[0][`pw${index + 1}`],
            };
            planParameter.push(obj);
          }
        }
      });
      if (this.hasData) {
        api.update(planParameter, (res) => {
          this.$utils.msg.success(res.msg);
        });
      } else {
        api.save(planParameter, (res) => {
          this.$utils.msg.success(res.msg);
        });
      }
    },
    init() {
       api.list({ cid: this.parameter.id, grade: this.grade ,sid: this.parameter.subjectid}, (res) => {
      
          this.coursePlanData = res.data.courseplan

          let temp = res.data.weekTerms;
          if (temp && temp[0]) {
            this.grade = temp[0].year + ''
          }

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
              this.yearDatas.push(obj);
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
          });

          this.title = this.parameter.dbColumn_subjectid + ' - 课程安排信息'

          //////    课时安排数据，也是三条数据根据学年合并为一条，当数据为空时，使课时初始值为0
    
            this.timePlanData = [];
            if (res.data.courseTMplan.length == 0) {
              this.hasData = false;
              for (let index = 0; index < this.yearDatas.length; index++) {
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
            for (let index = 0; index < this.yearDatas.length; index++) {
              this.yearDatas[index].child.push(this.timePlanData[index]);
            }
       })
    },
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
 