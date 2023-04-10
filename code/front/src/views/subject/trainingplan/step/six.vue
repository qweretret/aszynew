<template>
  <!-- 课程设置及要求 -->
  <div>
    <el-button type="primary" v-if="isShowEdit" style="margin: 12px 0; float: right" @click="toSave()">添加课程</el-button>
    <el-table :data="param.step6" border style="width: 100%">
      <el-table-column prop="dbColumn_level2" label="课程性质"></el-table-column>
      <el-table-column prop="name" label="课程"></el-table-column>
      <el-table-column prop="cstarget" label="课程目标">
        <template slot-scope="scope">
          <div v-html="scope.row.cstarget"> </div>
        </template>
      </el-table-column>
      <el-table-column prop="cscontent" label="主要内容"></el-table-column>
      <el-table-column prop="teachneed" label="教学要求"></el-table-column>
      <el-table-column prop="remarks" label="备注"></el-table-column>
      <el-table-column label="操作" fixed="right" width="200px">
        <template slot-scope="scope">
          <div class="dataTable-operator">
            <el-button size="mini" v-if="isShowEdit" icon="el-icon-edit" @click="toUpdate(scope.$index, scope.row)">修改
            </el-button>
            <el-button size="mini" v-if="isShowEdit" icon="el-icon-delete" type="danger"
              @click="remove(scope.$index, scope.row)">删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <edit ref="edit"></edit>
  </div>
</template>

<script>
import edit from "./six/edit.vue";

export default {
  components: {
    edit,
  },
  props: {
    param: Object,
    isShowEdit: Boolean
  },
  data() {
    return {
      listData: [],
      tempList: {},
    };
  },

  methods: {

    toSave() {
      this.$refs.edit.open({}, 0, '添加课程');
    },
    toUpdate(index, row) {
      this.$refs.edit.open(row, index, '编辑');
    },
    saveStep6(index, list) {
      this.param.step6.cors[index] = list
      this.$emit("child");
      console.log("step6保存");
    },
    addStep6(obj) {
      // console.log(this.param)
      // console.log(obj)
      let state = true;
      if (this.param.step6 && this.param.step6.length > 0) {
        this.param.step6.forEach(item => {
          if (item.name === obj.name) {
            state = false;
            this.$utils.msg.warning("不能保存相同课程");
          }
        });
        if (state) {
          this.param.step6.push(obj);
          this.$emit("child");
          console.log("step6保存");
        }
      }else{
        if (state) {
          this.param.step6 = [obj]
          this.$emit("child");
          console.log("step6保存");
        }
      }

    },

    //删除
    remove(index, row) {
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        this.param.step6.cors.splice(index, 1);
        this.$emit("child");
        console.log("step6保存");
      });
    },
  },

};
</script>

<style lang="sass" scoped>

</style>
