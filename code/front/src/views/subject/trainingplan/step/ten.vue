<template>
  <!-- 附录 -->
  <div>
    <el-button type="primary" v-if="isShowEdit" @click="uploadAccessories1()" style="margin-top: 20px;float:right">添加附录
    </el-button>

    <el-table class="table" :data="tableData" stripe border v-loading="listLoading">
      <el-table-column label="" type="index" min-width="5%"></el-table-column>
      <el-table-column prop="name" label="名称" min-width="25%"></el-table-column>
      <el-table-column prop="weburl" label="资料" min-width="40%">
        <template slot-scope="scope">
          <a v-if="scope.row.type==1" style="color:blue" :href="scope.row.weburl" target="_blank" class="buttonText">{{
          scope.row.weburl
          }}</a>
          <el-button size="mini" v-else @click="dwonDoc(scope.row.weburl,scope.row.name)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="crtm" label="添加时间" min-width="10%"></el-table-column>
      <el-table-column prop="operate" label="操作" min-width="15%">
        <template slot-scope="scope">
          <el-button size="mini"  @click="delDoc(scope.row.id,scope.row.name)" >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dlg1_title" :visible.sync="dialogUpldVisible1" width="45%">
      <div>
        文件名称:<br />
        <div>
          <el-autocomplete class="inline-input" v-model="atta.name" :fetch-suggestions="querySearch" placeholder="文件名称"
            @select="attaSelected"></el-autocomplete>
        </div><br />
        文件类型:<br />
        <el-radio :disabled="dlg1_disabled" v-model="atta.type" label="1">外链</el-radio>
        <el-radio :disabled="dlg1_disabled" v-model="atta.type" label="2">附件</el-radio><br /><br />
        <div v-show="atta.type==1">
          链接地址:<br />
          <el-input :disabled="dlg1_disabled" v-model="atta.weburl"></el-input>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="colseUpldDlg1">取 消</el-button>
        <el-button type="primary" @click="upldDlg1">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="请选择附件" :visible.sync="dialogUpldVisible" width="30%" :before-close="handleCloseUpldDlg">
      <excel-upload drag model="fileSystem" uri="/statics/zysz" :size="upldsize" v-on:change="uploadSuccess">
      </excel-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="colseUpldDlg">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import excelUpload from "@/components/biz/fileUpload/excelUpload.vue";
import apiDoc from '@/api/subject/docresource/docresource.js';

export default {
  name: "trainplan",
  components: {
    excelUpload
  },
  props: {
    param: Object,
    isShowEdit: Boolean,
    subjectId: String
  },  watch: {
    dialogUpldVisible1(val) {
      if(!val){
        this.colseUpldDlg1()
      }
    }
  },
  data() {
    return {
      listLoading: false,
      tableData: [],
      dialogUpldVisible1: false,
      dialogUpldVisible: false,
      upldsize: "2048",
      atta: { name: "", type: "1" },
      attaTmp:{},
      dlg1_title: "请填写附件的详细名称",
      dlg1_disabled: false
    };
  }, methods: {
    dwonDoc(fpath, fname) {
      apiDoc.downDoc(fpath, fname + fpath.substring(fpath.lastIndexOf(".")), "word");
    },delDoc(docId,fname){
      this.$utils.confirm.warning('提示', '确认删除附件<<'+fname+'>>?', ()=>{
        apiDoc.remove( {id:docId,sid:this.subjectId}, res=>{
           this.list( )
           this.$utils.msg.success("删除成功");
          },err=>{
           this.$utils.msg.warning("删除Fail");
         });
      });
  
      
    },  uploadAccessories1() { //弹出第一个dlg
      this.dialogUpldVisible1 = true
    }, colseUpldDlg1() {

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
           this.list()
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
      this.dlg1_title == "请填写附件的详细名称"
      this.dlg1_disabled = false
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
    }, attaSelected(atta) {

      this.atta.name = atta.value
      this.atta.type = atta.type
      this.atta.id = atta.id
      this.atta.weburl = atta.weburl

      //显示提示
      this.dlg1_title = "已选择现有文件"
      this.dlg1_disabled = true

    }, uploadSuccess(fileName) {

      this.atta =  this.attaTmp
         this.atta.weburl = fileName.substring( fileName.indexOf("/aszy")+5 )
         this.atta.type=2

         //将刚才传上去的文件写入数据库
         apiDoc.save({  sid:this.subjectId, type:2, vo:this.atta },res=>{
           this.list()
           this.dialogUpldVisible = false;
           this.$utils.msg.success("上传成功");
         },err=>{
           this.$utils.msg.warning("上传fail,请确认该文件是否已存在？");
         });


    }, colseUpldDlg() {
      this.dialogUpldVisible = false;
      this.colseUpldDlg1()
    }, handleCloseUpldDlg() {
      // x 的close
      this.dialogUpldVisible = false;
      this.colseUpldDlg1()
    }, list() {

      this.listLoading = true;
      apiDoc.list2({ subid: this.subjectId }, (res) => {
        this.tableData = res.data;
        this.listLoading = false;
      });

    }
  }, mounted() {
    //延迟加载，等下this.subjectId
    setTimeout(this.list( ), 300)

  },
};
</script>

<style>

</style>
