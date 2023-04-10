<template>
  <div class="tab-body">
    <div v-if="!isCoreModel && !isTrainingplan && !state">
      <div class="operator">
        <el-button type="primary" icon="el-icon-plus" @click="toSave()" v-if="isShowEdit">添加年级</el-button>
        <el-button type="primary" v-if="isShowEdit" @click="toCopy()">复制</el-button>
        <el-button type="info" @click="returnSubList()">返回</el-button>
      </div>
      <div class="dataTable autoFlex" v-if="listData">
        <el-table class="table" :data="listData.records" stripe border v-loading="listLoading">
          <el-table-column prop="sname" label="专业"></el-table-column>
          <el-table-column prop="grade" label="年级"></el-table-column>
          <el-table-column v-if="isShowEdit" prop="grade" label="参考资料">

            <template slot-scope="scope">
              <el-button size="mini" type="success" @click="uploadAccessories1(scope.row)">上传附件</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="coremodel" label="专业核心能力模型">
            <template slot-scope="scope">
              <el-button size="mini" type="success" @click="toCoreModel(scope.row)">{{isShowEdit?'编写':'查看'}}</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="zcmodel" label="人才培养方案">
            <template slot-scope="scope">
              <el-button size="mini" type="success" @click="toTrainingplan(scope.row)">{{isShowEdit?'编写':'查看'}}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="otrmodel" label="课程标准编制">
            <template slot-scope="scope">
              <el-button size="mini" type="success" @click="toCoursestandards(scope.$index, scope.row)">
                {{isShowEdit?'编写':'查看'}}</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="state" label="状态">
            <template slot-scope="scope">
              {{
              scope.row.state == 0
              ? "填写中"
              : scope.row.state == 1
              ? "未通过"
              : "通过"
              }}
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="200px">
            <template slot-scope="scope">
              <div class="dataTable-operator">

                <el-button v-has-permission-code-and="'10000800000'" size="mini" type="danger"
                  @click="remove(scope.$index, scope.row)">删除</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <edit ref="edit"></edit>
    <core-table v-if="isCoreModel" ref="coreTable" @returnList="returnList" :messageId="subjectId"
      :messageName="subjectName" :messageYear="currentYear" :selectId="selectId" :isShowEdit="isShowEdit"></core-table>

    <training-plan v-if="isTrainingplan" @trainingPlanBack="trainingPlanBack" :subjectId="subjectId" :subname="subjectName"
      :currentYear="currentYear" :isShowEdit="isShowEdit"></training-plan>

    <coursestandards v-if="state" @coursestandardsReturn="coursestandardsReturn" :isShowEdit="isShowEdit">
    </coursestandards>

    <el-dialog :title="dlg1_title" :visible.sync="dialogUpldVisible1" width="45%">
      <div>
        文件名称:<br />
        <div>
          <el-autocomplete class="inline-input" v-model="atta.name" :fetch-suggestions="querySearch"
            placeholder="文件名称" @select="attaSelected"></el-autocomplete>
        </div><br />
        文件类型:<br />
        <el-radio  :disabled="dlg1_disabled" v-model="atta.type" label="1">外链</el-radio>
        <el-radio  :disabled="dlg1_disabled" v-model="atta.type" label="2">附件</el-radio><br /><br />
      <div v-show="atta.type==1">
        链接地址:<br />
        <el-input :disabled="dlg1_disabled" v-model="atta.weburl"  ></el-input>
      </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="colseUpldDlg1">取 消</el-button>
        <el-button type="primary" @click="upldDlg1">{{(atta.type=="1" ||  this.dlg1_disabled  )?"添 加":"去上传"}}  </el-button>
      </span>
    </el-dialog>

    <el-dialog title="请选择附件" :visible.sync="dialogUpldVisible" width="30%" :before-close="handleCloseUpldDlg">
      
      <excel-upload drag model="fileSystem" uri="/statics/zysz" :size="upldsize"  v-on:change="uploadSuccess">
      </excel-upload>

      <span slot="footer" class="dialog-footer">
        <el-button @click="colseUpldDlg">关 闭</el-button>
      </span>
    </el-dialog>

  </div>
</template>
<script>
import edit from "./edit.vue";

import coreTable from "./coremodel.vue";
import trainingPlan from ".././trainingplan/list.vue";
import coursestandards from "../coursestandards/list.vue";

import api from "@/api/subject/coremodel/coremodel.js";
import excelUpload from "@/components/biz/fileUpload/excelUpload.vue";
import apiDoc from '@/api/subject/docresource/docresource.js';

export default {
  name: "coremodel",
  components: {
    edit,
    coreTable,
    trainingPlan,
    coursestandards,
    excelUpload
  },
  data() {
    return {
      state: false,
      current: {},
      roles: [],
      searchParams: {},
      isShowMoreSearch: false,
      listLoading: false,
      listData: [],
      showUpload: false, //上传附件弹框
      subjectId: "",
      subjectName: "",
      currentYear: "",
      selectId: "",
      selected: [],
      fileList: [],
      isCoreModel: false,
      isTrainingplan: false,
      page: new this.$constant.pageObj(),

      dialogUpldVisible1: false,
      dialogUpldVisible: false,
      upldsize: "2048",
      atta: { name: "", type: "1" },
      attaTmp:{},
      dlg1_title:"请填写附件的详细名称",
      dlg1_disabled:false

    };
  },
  props: {
    messageId: String,
    messageName: String,
    isShowEdit: Boolean
  },
  provide() {
    //依赖
    return {
      getSelectId: () => {
        return this.selectId;
      },
      sid: this.messageId,
    };
  },
  created() {
    this.subjectId = this.messageId;
    this.subjectName = this.messageName;
  },
  watch: {
    dialogUpldVisible1(val) {
      if(!val){
        this.colseUpldDlg1()
      }
    }
  },
  methods: {

    //返回专业列表
    returnSubList() {
      this.$emit("returnSubList");
    },
    //去人才培养方案
    toTrainingplan(row) {
      this.currentYear = row.grade;
      this.selectId = row.id;
      this.isTrainingplan = true;
    },
    //子组件专业核心模型回调事件
    returnList() {
      this.isCoreModel = false;
    },
    //去专业核心能力模型界面
    toCoreModel(row) {
      this.isCoreModel = true;
      this.currentYear = row.grade;
      this.selectId = row.id;
    },
    //去课程标准编制界面
    toCoursestandards(index, row) {
      this.state = true;
      this.$nextTick(() => {
        this.$eventBus.$emit("sendSid", { sid: row.sid, cid: row.id });
      });

    },
    //子组件课程标准编制回调事件
    coursestandardsReturn() {
      this.state = false;
    },
    //获取数据
    list(sid) {
      this.listLoading = true;
      this.searchParams.sid = sid;
      let params = this.$utils.merger(this.searchParams, this.page);
      //params.sid = sid
      api.list(params, (response) => {
        this.listData = response.data;
        this.listData.records.forEach(v => {
          v.sname = this.subjectName;
        });
        this.listLoading = false;
      });
    },
    //去添加
    toSave() {
      let param = {
        sid: this.subjectId,
        state: 0,
      };
      this.$refs.edit.open(param, "添加");
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
            this.list(this.subjectId);
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
            this.list(this.subjectId);
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
      this.list(this.subjectId);
    },
    //改变现在的页码
    currentChange(val) {
      this.page.current = val;
      this.list(this.subjectId);
    },
    //搜索
    search() {
      this.list(this.subjectId);
    },
    //清空搜索框
    clearSearchVal() {
      this.searchParams = {}
    },
    //返回
    trainingPlanBack() {
      this.isTrainingplan = false
    },

    //上传附件   
    uploadAccessories1() { //弹出第一个dlg
      this.dialogUpldVisible1 = true
    }, colseUpldDlg1() {   //关闭第一个dlg

      this.dialogUpldVisible1 = false
      this.dlg1_disabled = false
      //暂存 to 上传页面     reset
      this.attaTmp = this.atta
      this.atta =  { name: "", type: "1" }
    
    }, upldDlg1() {
       
        //是外链的            ||  选择了现有的
      if( this.atta.type=="1" || this.dlg1_disabled   ){
        //校验
        if(this.atta.type=="1" && ( (!this.atta.name) || (!this.atta.weburl ) ||  this.atta.name.length<2 || this.atta.weburl.indexOf("http")!=0 )){
          this.$utils.msg.warning("外链数据为空,或地址非法地址");
          return
        }
         apiDoc.save({  sid:this.subjectId, type:1, vo:this.atta },res=>{
          this.dialogUpldVisible1 = false;
           this.$utils.msg.success("保存成功");
         },err=>{
           this.$utils.msg.warning("上传fail,请确认该文件是否已存在？");
         });

      }else{
        
        if( this.atta.name  ){  //去物理上传，弹第二个Dlg
          this.dialogUpldVisible1 = false;
          this.dialogUpldVisible = true;
        }
      }
 
    },  //选择附件
    querySearch(queryString, cb) {
      this.dlg1_title=="请填写附件的详细名称"
      this.dlg1_disabled =false
      if (queryString && queryString.length > 0) {
        apiDoc.intnName({ name: queryString }, res => {
          if (res.data) {
            let attas = res.data;
            let results = queryString ? attas.filter(this.attaFilter(queryString)) : attas;
            cb(results);
          }
        })
      }
    },
    attaFilter(queryString) {
      return (clist) => {
        return (clist.value.indexOf(queryString) === 0);
      };
    }, attaSelected(atta) { //选中现存附件
      
      this.atta.name = atta.value
      this.atta.type = atta.type
      this.atta.id = atta.id
      this.atta.weburl = atta.weburl

      //显示提示
      this.dlg1_title="已选择现有文件"
      this.dlg1_disabled =true
  
    }, uploadSuccess(fileName) {  //上传文件成功的回调
         
         this.atta =  this.attaTmp
         this.atta.weburl = fileName.substring( fileName.indexOf("/aszy")+5 )
         this.atta.type=2

         //将刚才传上去的文件写入数据库
         apiDoc.save({  sid:this.subjectId, type:2, vo:this.atta },res=>{
           this.dialogUpldVisible = false;
           this.$utils.msg.success("上传成功");
         },err=>{
           this.$utils.msg.warning("上传fail,请确认该文件是否已存在？");
         });

    },colseUpldDlg() {
      this.dialogUpldVisible = false;
      this.colseUpldDlg1()
    }, handleCloseUpldDlg() {
      // x 的close
      this.dialogUpldVisible = false;
      this.colseUpldDlg1()
    },dwonDoc(fpath,fname){
      apiDoc.dwonDoc(fpath,fname,"word");
    }
  },
  mounted() {
    this.list(this.subjectId);
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";

 /deep/.el-autocomplete{
  width: 100%;
}
</style>

