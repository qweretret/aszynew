<template>
  <!-- 职业面向 -->
  <div>
    <div style="width: 70%">
      <four-model></four-model>
    </div>
    <el-table :data="param.step4" border style="width: 80%; margin-top: 20px">
      <el-table-column
        prop="sCategories"
        label="所属专业大类（代码）"
        width="70px"
      >
      </el-table-column>
      <el-table-column prop="sClass" label="所属专业类（代码）" width="100px">
      </el-table-column>
      <el-table-column prop="dyhy" label="对应行业（代码）">
        <template slot-scope="scope">
          <el-input type="textarea"  :readonly="!isShowEdit"    :rows="16" placeholder="请输入内容" v-model="scope.row.dyhy" style="margin-top:8px">
          </el-input>
        </template>
      </el-table-column>
      <el-table-column prop="zyzhlb" label="主要职业类别（代码）">
        <template slot-scope="scope">
          <el-input type="textarea"  :readonly="!isShowEdit"  :rows="16" placeholder="请输入内容" v-model="scope.row.zyzhlb" style="margin-top:8px">
          </el-input>
        </template>
      </el-table-column>
      <el-table-column prop="zygw" label="主要岗位群或技术领域举例">
        <template slot-scope="scope">
          <el-input type="textarea"  :readonly="!isShowEdit"  :rows="16" placeholder="请输入内容" v-model="scope.row.zygw" style="margin-top:8px">
          </el-input>
        </template>
      </el-table-column>
      <el-table-column prop="zyzs" label="职业资格证书和职业技能等级证书举例">
        <template slot-scope="scope">
          <el-input type="textarea"  :readonly="!isShowEdit"  :rows="16" placeholder="请输入内容" v-model="scope.row.zyzs" style="margin-top:8px">
          </el-input>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="warning" v-if="isShowEdit"  style="margin-top: 20px" @click="toSave" >保存</el-button
    >
  </div>
</template>

<script>
import wangEdit from "@/components/wangEdit/myWangEdit.vue";
import api from "@/api/subject/trainingplan/trainingplan.js";
import fourModel from "./four/fourModel.vue";
export default {
  components: {
    fourModel,
    wangEdit,
  },
  props: {
    param:  Object,
    isShowEdit: Boolean
  },
  data() {
    return {};
  },
  methods: {
    toSave() {
      this.$emit('child');
      // 触发child事件
      // 父控件使用编程式  this.$eventBus.$on() 监听或使用 @child指令接收
      console.log("step4保存");
    },
  },
  mounted() {},
};
</script>

<style></style>
