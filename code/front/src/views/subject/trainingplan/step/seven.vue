<template>
  <!-- 教学进程总体安排 -->
  <div>
    <el-table :data="courseInfo" style="width: 100%">
      <el-table-column prop="dbColumn_level2" label="课程性质" width="80">
      </el-table-column>
      <el-table-column prop="code" label="课程编码" width="80">
      </el-table-column>
      <el-table-column prop="name" label="课程名称" width="100">
      </el-table-column>
      <el-table-column prop="testtype" label="课程类别" width="80">
      </el-table-column>
      <el-table-column prop="testmode" label="考核方式" width="80">
      </el-table-column>
      <el-table-column prop="credit" label="课程学分" width="80">
      </el-table-column>
      <el-table-column label="课程学时">
        <el-table-column prop="period" label="总学时" width="60">
        </el-table-column>
        <el-table-column prop="theoryperiod" label="理论学时" width="60">
        </el-table-column>
        <el-table-column prop="actperiod" label="实践学时" width="60">
        </el-table-column>
      </el-table-column>
      <el-table-column :label="currentYear + '学年'">
        <el-table-column label="第一学期(18)">
          <el-table-column prop="courseTimeplan[0].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[0].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
        <el-table-column label="第二学期(18)">
          <el-table-column prop="courseTimeplan[1].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[1].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
        <el-table-column label="暑假（7）">
          <el-table-column prop="courseTimeplan[2].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[2].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
      </el-table-column>
      <el-table-column :label="(Number(currentYear) + 1) + '学年'">
        <el-table-column label="第一学期(20)">
          <el-table-column prop="courseTimeplan[3].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[3].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
        <el-table-column label="第二学期(20)">
          <el-table-column prop="courseTimeplan[4].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[4].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
        <el-table-column label="暑假（6）">
          <el-table-column prop="courseTimeplan[5].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[5].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
      </el-table-column>
      <el-table-column :label="(Number(currentYear) + 2) + '学年'">
        <el-table-column label="第一学期(1)">
          <el-table-column prop="courseTimeplan[6].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[6].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
        <el-table-column label="第二学期(1)">
          <el-table-column prop="courseTimeplan[7].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[7].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
        <el-table-column label="暑假（10）">
          <el-table-column prop="courseTimeplan[8].theorytime" label="理论课学时" width="40">
          </el-table-column>
          <el-table-column prop="courseTimeplan[8].practicetime" label="实践课学时" width="40">
          </el-table-column>
        </el-table-column>
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <div class="dataTable-operator">
            <el-button size="mini" v-if="isShowEdit" type="primary" icon="el-icon-edit"
              @click="toUpdate(scope.$index, scope.row)">修改
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top:20px">教学进程总体安排备注</div>
    <el-input type="textarea" v-if="isShowEdit" :rows="3" placeholder="请输入内容" v-model="param.step7"
      style="margin-top:16px">
    </el-input>
    <el-button v-if="isShowEdit" style="margin-top:16px" @click="toSave" type="warning">保存</el-button>
    <edit ref="edit"></edit>
  </div>
</template>

<script>
import edit from "./seven/edit.vue";
import api from "@/api/subject/coursetimeplan/coursetimeplan.js";

export default {
  components: {
    edit,
  },
  props: {
    param: Object,
    currentYear: String,
    isShowEdit: Boolean
  },
  mounted() {
    const cids = [];
    this.param.step6.forEach(element => {
      cids.push(element.id);
    });
    api.listByIds({ cids: cids }, resp => {
      this.param.step6.forEach(course => {
       let courseTimeplan =  resp.data.filter(element => {
          return element.cid === course.id;
        });
        this.courseInfo.push({...course,courseTimeplan});
      })
    })
  },
  data() {
    return {
      allData: [],
      courseInfo: [],
      //step6: {},
    };
  },
  methods: {
    toUpdate(index, row) {
      this.$refs.edit.open(row, this.currentYear, '教学进程总体安排', "disabled", "80%");
    },
    getPlan() {
      this.listLoading = true;
      let params = {
        current: 1,
        size: 15,
      };
      // api.list(params, (response) => {
      //   this.allData = response.data.records;
      //   this.listLoading = false;
      // });
    }, toSave() {
      this.$emit('child');
    },
  },
  beforeMount() {
    // console.log('seven.vue,param.step6', this.param.step6);
    // this.step6 = JSON.parse(JSON.stringify(this.param.step6));
  }
};
</script>

<style>

</style>
