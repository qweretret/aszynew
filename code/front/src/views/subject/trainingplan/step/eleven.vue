<template>
  <!-- 查看 -->
  <div>
     <el-button    type="primary" @click="toExport(1)">导出完整方案</el-button>
     <el-button    type="primary" @click="toExport(2)">导出人才培养方案</el-button>
  </div>
</template>

<script>
import apiExprt from "@/api/exprt/exprt.js";
export default {
  props: {
    param:   Object 
  },
  inject:["getRcplanId"],

  methods:{
    toExport(type){
  
        if(type==1){
          apiExprt.tpv({id:this.getRcplanId(),cid:this.param.cid},"完整人才培养方案.zip","zip" )
        }else{
          apiExprt.getTrainplan({id:this.getRcplanId()},"人才培养方案.doc","word");
        }
       
    } 
  }
};
</script>

<style></style>
