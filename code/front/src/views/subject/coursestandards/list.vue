<template>
  <div>
    <div class="tab-body" v-show="state === true">
      <div class="search">
        <el-form ref="form" :model="searchParams" label-width="80px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="课程名称">
                <el-input v-model="searchParams.coursename" placeholder="请输入班级名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="类型">
                <select-option-dictionary v-model="searchParams.coursetype" dkey="course-type">
                </select-option-dictionary>
              </el-form-item>
            </el-col>
            <div class="search-btn">
              <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
              <el-button @click="clearSearchVal">清空</el-button>
            </div>
          </el-row>
        </el-form>
      </div>
      <div class="operator">
        <el-button type="info" icon="el-icon-arrow-left" @click="Return()">返回</el-button>
        <el-button type="primary" v-if="isShowEdit" icon="el-icon-circle-plus-outline" @click="suggest(1)">新建专业核心能力
        </el-button>
        <el-button type="primary" v-if="isShowEdit" icon="el-icon-circle-plus-outline" @click="suggest(2)">新建专业支撑能力
        </el-button>
        <el-button type="primary" v-if="isShowEdit" icon="el-icon-circle-plus-outline" @click="suggest(3)">新建其他能力
        </el-button>
        <el-button type="danger" v-if="isShowEdit" icon="el-icon-delete" @click="batchRemove()">批量删除</el-button>
      </div>
      <div class="dataTable autoFlex" v-if="listData">
        <el-table class="table" :data="coursestandards" stripe border height="auto" @selection-change="selectionChange"
          v-loading="listLoading">
          <el-table-column type="selection" width="40" fixed="left"></el-table-column>
          <el-table-column label="序号" type="index" width="50"></el-table-column>
          <el-table-column prop="name" label="课程名称" width="160"></el-table-column>
          <el-table-column prop="type" label="课程类型" width="160">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.type == 1" type="success">专业核心能力</el-tag>
              <el-tag v-if="scope.row.type == 2">专业支撑能力</el-tag>
              <el-tag v-if="scope.row.type == 3" type="warning">其他能力课程</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="参考附件">
            <template slot-scope="scope">
              <el-button v-if="isShowEdit" size="mini" icon="el-icon-document-add"
                @click="toUpload(scope.$index, scope.row)">上传</el-button>
              <el-button size="mini" icon="el-icon-document" @click="toManage(1, scope.row)">查看</el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="600px">
            <template slot-scope="scope">
              <div class="dataTable-operator">
                <el-button size="mini" v-if="isShowEdit" icon="el-icon-edit-outline"
                  @click="toUpdate(scope.$index, scope.row)">修改
                </el-button>
                <el-button size="mini" v-if="isShowEdit" type="danger" icon="el-icon-delete"
                  @click="remove(scope.$index, scope.row)">删除
                </el-button>
                <el-button size="mini" type="info" icon="el-icon-reading" @click="toDetail(scope.row)">查看课程标准
                </el-button>
                <el-button size="mini" v-if="isShowEdit" icon="el-icon-upload2" @click="toExport(scope.row)">导出课程标准
                </el-button>
                <el-button size="mini" v-if="false" type="info" icon="el-icon-view">查看回退意见</el-button>
                <el-button size="mini" icon="el-icon-reading" @click="toManage(2, scope.row)">评价标准及题库</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>

      </div>

      <el-dialog title="请填写附件基本信息" :visible.sync="dialogVisible0" width="30%" :before-close="handleClose">
        <el-form class="fileInfo" label-position="left" label-width="80px" :model="pjbztkVo" :rules="rules" ref="form">
          <el-form-item label="附件名称" prop="nm">
            <el-input v-model="pjbztkVo.nm"></el-input>
          </el-form-item>
          <el-form-item label="" prop="type">
            <el-radio v-model="pjbztkVo.type" label="1">评价标准及题库</el-radio>
            <el-radio v-model="pjbztkVo.type" label="2">其他资料</el-radio>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible0 = false">取 消</el-button>
          <el-button @click="confirmInfo">下一步</el-button>
        </span>
      </el-dialog>

      <el-dialog title="上传附件" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
        <file-upload drag model="fileSystem" uri="/statics/zysz" :size="upsize" v-on:change="uploadSuccess">
        </file-upload>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
        </span>
      </el-dialog>

      <el-dialog :title="showTitle" :visible.sync="dialogVisible1" width="70%" :default-sort="{ prop: 'idx', order: 'ascending' }">

        <el-table :data="listPjbztkVo" style="width: 100%">
          <el-table-column prop="idx" label="排序" width="60px">
            <template slot-scope="scope">
              {{scope.$index+1}}
            </template>
          </el-table-column>
          <el-table-column prop="nm" label="附件名"></el-table-column>
          <el-table-column prop="type" label="类型" width="180px">
            <template slot-scope="scope">
              {{scope.row.type*1==1?"评价标准及题库":"其他附件"}}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300px">
            <template slot-scope="scope">
              <el-button size="mini" type="success" @click="downDoc(scope.row.url, scope.row.nm)">下载</el-button>
              <el-button size="mini"  v-if="showTitle.substring(0,4)=='全部附件'"  type="danger" @click="delDoc(scope.$index,scope.row)">删除</el-button>
              <el-button size="mini"  v-if="showTitle.substring(0,4)=='全部附件'"  @click="move(scope.$index,-1)">向上</el-button>
              <el-button size="mini"  v-if="showTitle.substring(0,4)=='全部附件'"  @click="move(scope.$index,1)">向下</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="display: flex;justify-content:flex-end;">
          <el-button size="mini" v-if="isMoved" type="warning" @click="saveDoc()">保存排序</el-button>
        </div>
       
      </el-dialog>

      <edit ref="edit" :sid="subjectId" :cid="cid"></edit>

    </div>
    <evaluate ref="evaluate" :isShowEdit="isShowEdit" v-show="state === false" @returnEvaluate="returnEvaluate">
    </evaluate>
  </div>
</template>
<script>

import edit from "./edit.vue";
import evaluate from "./evaluate.vue";

import selectOptionDictionary from "@/components/biz/selectOptionDictionary/selectOptionDictionary.vue";
import api from "@/api/subject/coursestandards/coursestandards.js";
import courseApi from "@/api/subject/course/course.js";
import fileUpload from "@/components/biz/fileUpload/fileUpload.vue";
import apiExprt from "@/api/exprt/exprt.js";
import apiDoc from '@/api/subject/docresource/docresource.js';

export default {
  name: "list",
  components: {
    edit,
    evaluate,
    selectOptionDictionary,
    fileUpload
  },
  props: {
    isShowEdit: Boolean
  },
  data() {
    return {
      dialogVisible0: false,//附件信息dal
      dialogVisible: false,//上传dal
      dialogVisible1: false,//查看、管理已上传附件dal
      upsize: 2048,

      searchParams: {
        subjectId: "",
      },
      isShowMoreSearch: false,
      listLoading: false,
      listData: [],
      coursestandards: [],
      state: true,
      selected: [],
      subjectId: "",
      cid: "",
      page: { size: 30, current: 1 },
      pjbztkVo: {//附件信息

      },
      curRowId: '',
      listPjbztkVo: [],//附件信息
      isMoved:false,
      showTitle:"",

      //附件信息规则
      rules: {
        type: [{
          validator: this.$validate.required,
          trigger: 'blur'
        },],
        nm: [{
          validator: this.$validate.required,
          trigger: 'blur'
        },
        {
          validator: this.$validate.length,
          min: 0,
          max: 32,
          trigger: 'blur'
        }]
      }

    };
  }, 
  methods: {
    move(idx,type){ //移动数据
      if( this.listPjbztkVo ){
        if( idx ==0 && type==-1 ){
           //无效
        } else if( idx == (this.listPjbztkVo.length-1) && type== 1 ){
           //无效
        }else{
           this.isMoved = true
           this.listPjbztkVo.splice( idx+type,0,this.listPjbztkVo.splice(idx,1)[0]  )
        }
      }
    },
    confirmInfo() {//确认附件信息
      this.$utils.checkForm(this.$refs.form, () => {
        this.dialogVisible = true;
        this.dialogVisible0 = false;
      });
    },saveDoc(){
      api.savePjbztk({
          id:   this.curRowId ,
          pjbztk:JSON.stringify(this.listPjbztkVo )
        }, resp => {
          this.$utils.msg.success("排序保存成功");
          this.isMoved = false
        })
    },
    delDoc(flag,row) {//根据唯一标识删除文件
      this.$utils.confirm.warning("提示", "确定删除吗？", () => {
        api.removePjbztk({
          id: this.curRowId,
          flag
        }, resp => {
          this.$utils.msg.success("删除成功");
          this.listPjbztkVo.splice(flag,1)
        })
      });
    },
    downDoc(url, name) {//下载文件
      url = '/statics/zysz' + url
      apiDoc.downDoc(url, name + url.substring(url.lastIndexOf(".")), "word");
    },
    //管理已上传附件
    toManage(type, row) {
      //当前的课程标准id
      this.curRowId = row.id
      //保存排序的按钮是否显示
      this.isMoved = false
      this.showTitle = (type==1?"全部附件":"评价标准及题库")+" - "+row.name
  
      //获取附件列表
      api.listPjbztkVo({ id:  row.id }, resp => {
      
        let list;
        if (resp.msg && resp.msg !== '') {

          list = JSON.parse(resp.msg);
          if (type == 2) { //评价题
            this.listPjbztkVo = list.filter(v=>v.type=="1")
          } else {
            this.listPjbztkVo = list
          }
        } else {
          this.listPjbztkVo = [];
        }
 
        this.dialogVisible1 = true;
      })
    },
    //上传文件
    toUpload(index, row) {
      this.curRowId = row.id;
      this.dialogVisible0 = true;
    },
    handleClose( ) {
      this.$confirm('确认关闭？')
        .then(_ => {
          if(!this.dialogVisible){
            this.pjbztkVo ={}
          }
        }).catch(_ => { });
    },
    uploadSuccess(res) {
      //写数据库
      if (res && res.length > 32) {
        //发请求，同步数据库
        let url = res.substring(res.indexOf("/aszy") + 5);
        url = url.substring(url.lastIndexOf("/") - 7);
        let params = this.$utils.merger({ url: url, id: this.curRowId }, this.pjbztkVo);

        api.updatePjbztk(params, resp => {
          this.$utils.msg.success("上传成功");
          this.dialogVisible = false
          this.pjbztkVo ={}
        });

      } else {
        this.$utils.msg.warning("上传失败,请联系管理员.")
      }
    },
    toExport(row) {

      let fname = "课程标准-" + row.name + ".doc"
      apiExprt.getCrsDoc({ id: row.id }, fname, "word")
    },
    Return() {
      this.$emit("coursestandardsReturn")
    },
    returnEvaluate() {
      this.state = true
    },

    //获取数据
    list() {
      this.listLoading = true;
      this.coursestandards = []
      this.searchParams.subId = this.subjectId;
      let params = this.$utils.merger(this.searchParams, this.page);

      courseApi.list(params, (response) => {
        this.listData = response.data;
        this.listLoading = false;
      });

      api.list(this.searchParams, (response) => {
        if (response.data && response.data.length > 0) {
          response.data.forEach(v => {
            let courest = JSON.parse(v.courest)
            courest.one.id = v.id
            courest.one.type = v.abtype
            this.coursestandards.push(courest.one)
          })
        }
        this.listLoading = false;

      });
      // this.searchParams = {};
    },
    //新建专业核心能力
    suggest(type) {
      let editParameter = {
        listData: this.listData
      };
      this.$refs.edit.open(editParameter, type == 1 ? "新建专业核心能力" : (type == 2 ? "新建专业支撑能力" : "新建其他能力"), type);
    },

    //去编辑
    toUpdate(index, row) {
      let editParameter = {
        listData: { listData: this.listData },
        row: row
      };
      if (row.type == 3) {
        this.$refs.edit.open(editParameter, "编辑【其他能力】", row.type);
      } else if (row.type == 2) {
        this.$refs.edit.open(editParameter, "编辑【专业支撑能力】", row.type)
      } else if (row.type == 1) {
        this.$refs.edit.open(editParameter, "编辑【专业核心能力】", row.type)
      }
    },
    //去详情
    toDetail(row) {
 
      let detailParameter = {
        listData: { listData: this.listData },
        row: row
      };
      this.$refs.edit.open(detailParameter, "详情【" + row.name + "】", 4, true);
    },
    //删除
    remove(index, row) {
      let parameter = {
        ids: row.id,
      };
      this.$utils.confirm.warning("提示", "确定删除 ' " + row.name + " ' 吗？", () => {
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

    //搜索
    search() {
      this.listLoading = true;
      let params = {
        coursename: this.searchParams.coursename,
        coursetype: this.searchParams.coursetype,
      };
      this.list();
    },
    //清空搜索框
    clearSearchVal() {
      this.searchParams = {};
      this.list();
    },
  },
  mounted() {
    this.$eventBus.$on("sendSid", (obj) => {
      this.subjectId = obj.sid
      this.cid = obj.cid
      this.list();
    });
  },
  destroyed() {
    //组件销毁同时取消监听
    this.$eventBus.$off("sendSid");
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
</style>

