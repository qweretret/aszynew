<template>
  <div>
     
  </div>
</template>

<script>

export default {
  methods:{
    updtChldrenIds(node){
       let arr2 = node.children;
       if(arr2.length>0){
            for(let i =0;i<arr2.length;i++){
                  arr2[i].parentId = node.id
                  arr2[i].id = node.id + arr2[i].id.substr( node.id.length)
                  this.updtChldrenIds(arr2[i])
            }
       } 
          
    }
  },
  data() {
    return {
      arrData: [],
    };
  }, 
  mounted() {
       this.arrData =  [{"id":"01","parentId":"0","name":"信息导论","children":[
                          {"id":"0101","parentId":"01","name":"信息加密","children":[
                             {"id":"010101","parentId":"0101","name":"吃瓜群众","children":[]},
                             {"id":"010102","parentId":"0101","name":"大师傅似的","children":[]},
                             {"id":"010103","parentId":"0101","name":"发射点发","children":[   
                                {"id":"01010301","parentId":"010103","name":"广泛的","children":[]},
                                {"id":"01010302","parentId":"010103","name":"华国锋","children":[]}]
                             },
                            {"id":"010104","parentId":"0101","name":"发射点发","children":[]}
                          ]}
                      ]},
                       {"id":"02","parentId":"0","name":"电器工程","title":"职业标准","children":[]}];
      console.log( JSON.stringify(this.arrData) )
      let node = this.arrData[0]
      let frst0 = node.id.substr(0,1)=="0"?"0":""
      let id =  node.id*1
      node.id = frst0+(id+1)
      this.updtChldrenIds(node)
      console.log( JSON.stringify(this.arrData) )
   }
}
</script>

<style></style>
