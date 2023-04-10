<template>
  <div>
    <div class="search-btn">
      <el-button type="info" size="mini" icon="el-icon-arrow-left" @click="toReturn">返回专业设置列表</el-button>
    </div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <!-- 专业名称及代码 -->
      <el-tab-pane label="专业名称及代码" name="1">
        <el-card class="box-card">
          <div>专业名称：{{ rcplan.subname }}</div>
          <div>专业代码：{{ rcplan.code }}</div>
        </el-card>
      </el-tab-pane>

      <!-- 入学要求 -->
      <!-- two -->
      <el-tab-pane label="入学要求" name="2">
        <el-card class="box-card">
          <div>{{ rcplan.enterNd }}</div>
        </el-card>
      </el-tab-pane>

      <!-- 修习年限 -->
      <!-- three  -->
      <el-tab-pane label="修业年限" name="3">
        <el-card class="box-card">
          <div>修业年限：{{ rcplan.xynx }}</div>
        </el-card>
      </el-tab-pane>

      <!-- 职业面向 -->
      <el-tab-pane label="职业面向" name="4">
        <four :param="rcplan" v-if="listState == true" :isShowEdit="isShowEdit" @child="childSave()"></four>
      </el-tab-pane>

      <!-- 培养目标与培养规格 -->
      <el-tab-pane label="培养目标与培养规格" name="5">
        <five :param="rcplan" :year="currentYear" :subname="subname" :isShowEdit="isShowEdit" v-if="listState == true"
          @child="childSave()"></five>
      </el-tab-pane>

      <!-- 课程设置及要求 -->
      <el-tab-pane label="课程设置及要求" name="6">
        <six :param="rcplan" v-if="listState == true" :isShowEdit="isShowEdit" @child="childSave()"></six>
      </el-tab-pane>

      <!-- 教学进程总体安排 -->
      <el-tab-pane label="教学进程总体安排" name="7">
        <seven :param="rcplan" :currentYear="currentYear" :isShowEdit="isShowEdit" @child="childSave()"
          v-if="listState == true"></seven>
      </el-tab-pane>

      <!-- 实施保障 -->
      <el-tab-pane label="实施保障" name="8">
        <eight v-if="listState == true" :param="rcplan" :isShowEdit="isShowEdit" @child="childSave()"></eight>
      </el-tab-pane>

      <!-- 毕业要求 -->
      <el-tab-pane label="毕业要求" name="9">
        <nine v-if="listState == true" :param="rcplan" :isShowEdit="isShowEdit" @child="childSave()"></nine>
      </el-tab-pane>

      <!-- 附录 -->
      <el-tab-pane label="附录" name="10">
        <ten :param="rcplan" :subjectId="subjectId" :isShowEdit="isShowEdit" v-if="listState == true"></ten>
      </el-tab-pane>

      <!-- 查看 -->
      <el-tab-pane label="查看" v-if="listState == true" name="11">
        <eleven :param="rcplan" v-show="isShowEdit" v-if="listState == true"></eleven>
      </el-tab-pane>
    </el-tabs>

    <div class="nextBun">
      <el-button type="primary" @click="next" v-if="
        (activeName >= 0 && activeName < 5) ||
          (activeName > 6 && activeName < 11)
          ? actButton
          : actButton
      ">下一步</el-button>
    </div>
  </div>
</template>

<script>
import four from "./step/four.vue";
import five from "./step/five.vue";
import six from "./step/six.vue";
import seven from "./step/seven.vue";
import eight from "./step/eight.vue";
import nine from "./step/nine.vue";
import ten from "./step/ten.vue";
import eleven from "./step/eleven.vue";
import api from "@/api/subject/trainingplan/trainingplan.js";

 


export default {
  components: {
    four,
    five,
    six,
    seven,
    eight,
    nine,
    ten,
    eleven,
  },
  props: {
    currentYear: String,
    subname: String,
    subjectId:String,
    isShowEdit: Boolean
  },
  data() {
    return {
      rcplanId: "", //人才培养方案id(判断是否有数据)
      cid: "",
      activeName: "1",
      actButton: true,
      listState: false,
      rcplan: {}
    };
  },
  provide() {
    return {
      subjectId:this.subjectId,
      rcplan: this.rcplan,
      //获取id
      getRcplanId: () => {
        return this.rcplanId;
      },
      //查询列表
      getRcplan: () => {
        return this.rcplan;
      },
      //让 培养目标与培养规格 里的 课程体系与毕业生能力指标点关联矩阵 的“下一步”继续跳转
      bigRootNext: () => {
        this.activeName = String(Number(this.activeName) + 1);
        this.actButton = true;
      },
      // 让 教学进程总体安排 里的 修改后“点击保存”刷新该页面的数据
      refreshData: () => {
        this.getOne();
      },
    };
  },
  inject: ["getSelectId"],
  methods: {
    //人才培养方案列表
    getOne() {
 
      let param = {
        cid: this.getSelectId(),
      };
      api.oneRcplan(param, (res) => {
        //判断是否有数据
        if (res.data) {
          //是否有id
          //id传给子组件，判断是添加还是修改
          this.rcplanId = res.data.id;
          //更新列表数据
          this.rcplan = JSON.parse(res.data.rcplan);
          //子组件切换状态
          //给seven页面传 学年学时
          if (this.rcplan.step6.cors && this.rcplan.step6.cors.length > 0) {
            let params = { cids: [], grade: this.currentYear };
            this.rcplan.step6.cors.forEach(item => {
              params.cids.push(item.cid);
              console.log(" params.cids.push(item.cid)");
            });
            api.getCourseTimePlan(params, (res) => {
              this.rcplan.step6.cors.forEach((item, index) => {
                item.arr = res.data[index];
                console.log("item.arr = res.data[index]");
              })
            }, (err) => {
              console.log(err);
            });
          }
        }
        this.listState = true;
      });
    },
    childSave() {
      //收到save
      this.merge();
    },
    // 合并
    merge() {
      this.rcplan.year = this.currentYear;
      if (this.rcplanId) {
        console.log(this.rcplan);
        api.update({
          id: this.rcplanId,
          rcplan: JSON.stringify(this.rcplan)
        }, (res) => {
          this.$utils.msg.success("保存成功");
          // this.getOne();
        });
      } else {
        let params = {
          cid: this.getSelectId(),
          rcplan: JSON.stringify(this.rcplan)
        };
        api.save(params, (res) => {
          this.$utils.msg.success("保存成功");
          this.getOne();
        });
      }
    },
    toReturn() {
      this.$emit("trainingPlanBack");
      //console.log("返回頁面");
    },
    //下一步
    handleClick(tab, event) {
      this.activeName = tab.name;
      if (tab.name <= 4 || (tab.name >= 6 && tab.name < 11)) {
        this.actButton = true;
      } else {
        this.actButton = false;
      }
    },
    next() {
      if (
        (this.activeName >= 0 && this.activeName < 4) ||
        (this.activeName > 5 && this.activeName < 10)
      ) {
        this.activeName = String(Number(this.activeName) + 1);
        this.actButton = true;
      } else {
        this.activeName = String(Number(this.activeName) + 1);
        this.actButton = false;
      }
    },
  },
  destroyed() {
    //组件销毁同时取消监听
    this.$eventBus.$off("saveSubject");
  },
  beforeMount() {
    this.getOne();
  },
};
</script> 

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";

.search-btn {
  margin: 10px 0px;
}

.nextBun {
  margin: 12px 0;
}

.box-card {
  padding: 10px;

  div {
    margin: 10px;
    font-size: 20px;
  }
}
</style>
