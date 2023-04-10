<template>
  <div class="tab-body">
    <div v-if="!isList">
      <h2>专业列表</h2>
      <div class="dataTable autoFlex" v-if="listData">
        <el-table
          class="table"
          :data="listData.records"
          stripe
          border
          @selection-change="selectionChange"
          v-loading="listLoading"
        >
          <el-table-column prop="idx" width="75px"  label="序号">
			  <template slot-scope="scope">
				  {{scope.$index+1}}
				  </template>
		  </el-table-column>
          <el-table-column prop="name" label="专业名称"></el-table-column>
          <el-table-column prop="code" label="专业代码"></el-table-column>
          <el-table-column prop="year" width="160px" label="学制(修业年限)"></el-table-column>
          <el-table-column prop="stutype" width="300px" label="生源类型"></el-table-column>
          <el-table-column label="操作" fixed="right" width="220px">
            <template slot-scope="scope">
              <div class="dataTable-operator">
              
                <el-button
                  v-if="isShowEdit(scope.row)"
				          type="primary"
                  size="mini"
                  @click="toManage(scope.row)"
                  >管理</el-button
                >
                <el-button
				          type="success"
                  v-else
                  size="mini"
                  @click="toManage(scope.row)"
                  >查看</el-button
                >

              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <list v-if="isList"  @returnSubList="returnSubList" :messageId="subjectInfo.id" :messageName="subjectInfo.name"  :isShowEdit="isSHow"  ></list>
  </div>
</template>
<script>
import list from "./list.vue";
import api from "@/api/subject/subject/subject.js";
export default {
  name: "subject",
  components: {
    list,
  },
  provide() {
    return {
      subjectInfo: this.subjectInfo,
    };
  },
  data() {
    return {
      searchParams: {},
      isShowMoreSearch: false,
      listLoading: false,
      listData: [],
      selected: [],
      subjectInfo:{},
      isList: false,
      page: new this.$constant.pageObj(),
      roleTag:'',
      dptCode:'',
      isSHow:false
    };
  },
  methods: {
    returnSubList() {
      this.isList = false;
    },
    toManage(row) {
      this.subjectInfo = row
      this.isSHow = this.isShowEdit(row)
      this.isList = true;
    },
    //获取数据
    list() {
      this.listLoading = true;
      let params = this.$utils.merger(this.searchParams, this.page);
      api.list(params, (response) => {
        this.listData = response.data;
        this.listLoading = false;
      });
    },
    //去添加
    toSave() {
      this.$refs.edit.open({}, "添加");
    },
    //去编辑
    toUpdate(index, row) {
      let editParameter = {
        id: row.id,
      };
      this.$refs.edit.open(editParameter, "编辑");
    },
    //去详情
    toDetail(index, row) {
      let detailParameter = {
        id: row.id,
      };
      this.$refs.edit.open(detailParameter, "详情", true);
    },
    //删除
    remove(index, row) {
      let parameter = {
        ids: row.id,
      };
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        api.remove(
          parameter,
          (response) => {
            this.$utils.msg.success(response.msg);
            this.list();
          },
          (response) => {
            this.$utils.msg.warning(response.msg);
          }
        );
      });
    },
    //批量删除
    batchRemove() {
      if (!this.selected || this.selected.length == 0) {
        this.$utils.msg.info("请选择至少一行");
        return;
      }
      let parameter = {
        ids: this.selected,
      };
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        api.remove(
          {
            ids: this.selected,
          },
          (response) => {
            this.$utils.msg.success(response.msg);
            this.list();
          },
          (response) => {
            this.$utils.msg.warning(response.msg);
          }
        );
      });
    },
    //改变选择项
    selectionChange(val) {
      //清空
      this.selected = [];
      for (let item of val) {
        this.selected.push(item.id);
      }
    },
    //改变每页显示数量
    sizeChange(val) {
      this.page.size = val;
      this.list();
    },
    //改变现在的页码
    currentChange(val) {
      this.page.current = val;
      this.list();
    },

    //搜索
    search() {
      this.list();
    },
    //清空搜索框
    clearSearchVal() {
      this.searchParams = {};
    },isShowEdit(row){
      if(this.roleTag=='adm'){
          return true
      }else if( this.roleTag=='jys' && row.dcode == this.dptCode ){
          return true
      }
       return false
     }
  },
  mounted() {
    this.list();
    this.roleTag =  this.$caches.roleCodes.get()[0].substr(0,3)  ;
    this.dptCode =  this.$caches.departmentCode.get()[0]  ;
    console.log( this.roleTag )
    console.log( this.dptCode )
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
</style>

