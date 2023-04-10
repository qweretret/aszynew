<template>
  <!-- 培养目标与培养规格 -->
  <div>
    <el-tabs
      v-model="activeName"
      :tab-position="tabPosition"
      @tab-click="handleClick"
    >
      <el-tab-pane label="培训目标">
        <div class="paneTitle">人才培养目标</div>
        <el-row>
          <el-col :span="18">
            <el-input type="textarea"  :readonly="!isShowEdit"  :rows="12" placeholder="请输入内容" v-model="param.step5[0].rcpymb" style="margin-top:8px">
          </el-input>
          </el-col>
        </el-row>
        <div>
          <el-button   v-if="isShowEdit"   style="margin-top: 12px" type="warning" @click="toSave"
            >保存</el-button
          >
          <el-button
            style="margin-top: 12px"
            @click="next"
            v-if="activeName >= 0 && activeName < 3 ? actButton : actButton"
            >下一步</el-button
          >
        </div>
      </el-tab-pane>

      <el-tab-pane label="培训规格">
        <div>毕业生能力要求(参考专业教学标准和毕业生能力模型)</div>
        <div class="paneTitle">素质要求</div>
        <el-row>
          <el-col :span="18">
            <el-input type="textarea"  :readonly="!isShowEdit"  :rows="12" placeholder="请输入内容" v-model="param.step5[0].szyq" style="margin-top:8px">
          </el-input>
          </el-col>
        </el-row>
        <div class="paneTitle">知识要求</div>
        <el-row>
          <el-col :span="18">
            <el-input type="textarea"  :readonly="!isShowEdit"  :rows="12" placeholder="请输入内容" v-model="param.step5[0].zsyq" style="margin-top:8px">
          </el-input>
          </el-col>
        </el-row>
        <div class="paneTitle">能力要求</div>
        <el-row>
          <el-col :span="18">
            <el-input type="textarea"  :readonly="!isShowEdit"  :rows="12" placeholder="请输入内容" v-model="param.step5[0].nlyq" style="margin-top:8px">
          </el-input>
          </el-col>
        </el-row>
        <div>
          <el-button   v-if="isShowEdit"   style="margin-top: 12px" type="warning" @click="toSave"
            >保存</el-button
          >
          <el-button
            style="margin-top: 12px"
            @click="next"
            v-if="activeName >= 0 && activeName < 3 ? actButton : actButton"
            >下一步</el-button
          >
        </div>
      </el-tab-pane>

      <el-tab-pane label="毕业生能力要求指标点">
        <el-row>
          <el-col :span="12">
            <el-card class="box-card myCard">
              <div style="margin-bottom: 12px">毕业生能力参考模型</div>
              <five-model></five-model>
            </el-card>
          </el-col>
          <el-card class="box-card myCard">
            <div style="margin-bottom: 12px">毕业生能力要求指标点</div>
            <five-indicators
              :params="param"
              :next="next"
              :isShowEdit="isShowEdit"
              @child="toSave()"
            ></five-indicators>
          </el-card>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="课程体系与毕业生能力指标点关联矩阵">
        <template slot-scope="scope">
          <el-button
            type="primary"
            style="margin: 12px 0"
            v-if="isShowEdit"  
            @click="course(scope.$index, scope.row)"
            >添加课程</el-button
          >
          <el-row>
            <el-col :span="12">
              <five-associated  :params="param" :year="year" :subname="subname" :rootnext="rootnext"  :isShowEdit="isShowEdit"
              @child="toSave()"></five-associated>
            </el-col>
          </el-row>
        </template>
      </el-tab-pane>
    </el-tabs>
    <edit ref="edit" :params="param"></edit>
  </div>
</template>

<script>
import wangEdit from "@/components/wangEdit/myWangEdit.vue";
import fiveModel from "./five/fiveModel.vue";
import fiveIndicators from "./five/fiveIndicators.vue";
import fiveAssociated from "./five/fiveAssociated.vue";
import edit from "./five/edit.vue";
export default {
  components: {
    fiveModel,
    fiveIndicators,
    fiveAssociated,
    wangEdit,
    edit,
  },
  props: {
    param:  Object, 
    rootnext: Function,
    subname:  String,
    year:  String,
    isShowEdit: Boolean
  },
  data() {
    return {
      wangEditValue: "",
      mavonEditorValue: "",
      activeName: "0",
      actButton: true,
      tabPosition: "top",
      indexData: [{ ability: "", index: "" }],
    };
  },
  methods: {
    // childSave(){
    //   this.$emit("child");
    // },
    toSave() {
      this.$emit("child");
      console.log("step5保存");
    },
    //下一步
    handleClick(tab) {
      this.activeName = tab.index;
      if (tab.index < 3) {
        this.actButton = true;
      } else {
        this.actButton = false;
      }
    },
    next() {  
      if (this.activeName >= 0 && this.activeName < 2) {
        this.activeName = String(Number(this.activeName) + 1);
        this.actButton = true;
      } else {
        this.activeName = String(Number(this.activeName) + 1);
        this.actButton = false;
      }
        
    },

    //添加
    clickPlus(index, row) {
      this.$prompt("内容", "毕业生能力要求", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      }).then(({ value }) => {
          // this.indexData.push({ ability: value });
          this.$message({
            type: "success",
            message: "添加成功",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消添加",
          });
        });
    },

    //编辑
    clickEdit(index, row) {
      this.$prompt("内容", "更新毕业生能力要求指标点", {
        confirmButtonText: "更新",
        cancelButtonText: "取消",
      })
        .then(({ value }) => {
          this.indexData[index].ability = value;
          this.$message({
            type: "success",
            message: "更新成功",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消更新",
          });
        });
    },

    //删除
    clickDel(index) {
      this.indexData.splice(index, 1);
    },
    //添加课程(课程体系与毕业生能力指标点关联矩阵)
    course() {
      this.$refs.edit.open({}, "添加专业课程");
    },
  } 
};
</script>

<style lang="scss" scoped="scoped">
.paneTitle {
  margin: 15px 0px 10px;
}
.myCard {
  margin: 0px 10px;
}
</style>
