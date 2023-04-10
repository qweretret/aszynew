<template>
  <div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <!-- model="fileSystem"
        uri="/statics/excels" -->
      <excel-upload
        drag
        model="fileSystem"
        uri="/statics/zysz"
        :size="upsize"
        @click="uploadSuccess"  >
        <!-- @change="" -->
        <!-- @change="" -->
    
      </excel-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="colse">取 消</el-button>
        <el-button type="primary" @click="colse">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>



export default {
  components: {
    excelUpload,
  },
  props: {
    disabled: {
      type: Boolean,
    },
  },
  data() {
    return {
      dialogVisible: false,
      upsize: "512",
    };
  },
  watch: {
    disabled(val) {
      this.dialogVisible = val;
    },
  },
  methods: {
    uploadSuccess(res) {
      console.log("res: ", res);
      if (res && res.length > 32) {
        this.$utils.msg.success("上传成功");
        this.dialogVisible = false;
        //http://127.0.0.1:8036/study3/statics/excels/f4429aac-f035-4816-b5ab-3c297e50abe7-学生信息.xls
        // let fnm = res.substr(res.lastIndexOf("/") + 1);

        // api.toImport({ fnm: fnm }, (res2) => {
        //   console.log("res2: ", res2);
        //   this.$utils.msg.success("上传success");
        // });
      } else {
        this.$utils.msg.warning("上传fail,亲联系管理员.");
      }
    },
    colse() {
      this.dialogVisible = false;
      console.log('this.dialogVisible: ', this.dialogVisible);
      this.$emit("disabled");
      this.dialogVisible = true;
      console.log('this.dialogVisible: ', this.dialogVisible);
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          this.$emit("disabled");
          done();
        })
        .catch((_) => {});
    },
  },
};
</script>

<style></style>
