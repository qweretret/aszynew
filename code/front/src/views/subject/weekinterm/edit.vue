<template>
  <popup ref="popup" :loading="popupLoading">
    <div slot="body">
      <el-form
        class="dataForm"
        ref="form"
        :disabled="disabled"
        :model="formParameter"
        :inline="true"
        label-width="80px"
        :rules="rules"
      >
        <el-row>
          <el-col :span="24">
            <el-form-item label="学年" prop="year">
              <el-input :disabled="sigbal===1" v-model="formParameter.year"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="第一学期" prop="one">
              <el-input v-model="formParameter.one"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="第二学期" prop="two">
              <el-input v-model="formParameter.two" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="暑假" prop="three">
              <el-input v-model="formParameter.three" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div slot="footer">
      <el-button v-if="!disabled" type="primary" :loading="confirmLoading" @click="confirm()">确 定</el-button>
      <el-button @click="close">取 消</el-button>
    </div>
  </popup>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api from "@/api/subject/weekinterm/weekinterm.js";
export default {
  name: "edit",
  components: {
    popup
  },
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      parameter: {},
	  sigbal:0,
      formParameter: {
		  year:"",
		  one:"",
		  two:"",
		  three:"",
	  },
      rules: {
        //学年
        year: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 11,
            trigger: "blur"
          }
        ],
        //学期
        term: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 50,
            trigger: "blur"
          }
        ],
        //周数
        weeks: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 11,
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    //打开弹框
    open(parameter, title,sigbal,size) {
      this.parameter = parameter;
	  this.sigbal = sigbal;
      this.$refs.popup.open(title, size);
      this.$nextTick(() => {
        this.init();
      });
    },
    //关闭弹框
    close() {
      this.disabled = false;
      this.$refs.popup.close();
    },
    //提交表单
    confirm() {
      this.$utils.checkForm(this.$refs.form, () => {
        this.confirmLoading = true;
        if (!this.parameter.year) {
          //添加
          let parameter = this.$utils.merger(this.formParameter);
          api.save(
            parameter,
            response => {
              this.$utils.msg.success(response.msg);
              this.confirmLoading = false;
              this.close();
              this.$parent.list();
            },
            response => {
              this.$utils.msg.warning(response.msg);
              this.confirmLoading = false;
            }
          );
        } else {
          //编辑
          let parameter = this.$utils.merger(this.formParameter);
          api.update(
            parameter,
            response => {
              this.$utils.msg.success(response.msg);
              this.confirmLoading = false;
              this.close();
              this.$parent.list();
            },
            response => {
              this.$utils.msg.warning(response.msg);
              this.confirmLoading = false;
            }
          );
        }
      });
    },
    init() {
      this.popupLoading = true;
      //清除等待
      this.confirmLoading = false;
      //清空内容
      this.formParameter = {};
      //清楚校验
      this.$refs.form.clearValidate();

      if (this.parameter.year) {
        //编辑查询数据
        let parameter = {
          year: this.parameter.year
        };
        api.toUpdate(parameter, response => {
			this.$set(this.formParameter,"year",response.data[0].year)
			this.$set(this.formParameter,"one",response.data[0].weeks)
			this.$set(this.formParameter,"two",response.data[1].weeks)
			this.$set(this.formParameter,"three",response.data[2].weeks)
          console.log(this.formParameter);
          this.popupLoading = false;
        });
      } else {
        //添加
        this.popupLoading = false;
      }
    }
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
