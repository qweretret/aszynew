<template>
  <popup ref="popup" :loading="popupLoading">
    <div slot="body">
      <el-form
        class="dataForm"
        ref="form"
        :disabled="disabled"
        :model="formParameter"
        :inline="true"
        label-width="100px"
        :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程性质" prop="csprop">
              <select-option-dictionary
                v-model="formParameter.csprop"
                dkey="sub-kexz"
                :multiple="false"
                :stringMode="true"
              ></select-option-dictionary>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考核方式" prop="testmode">
              <select-option-dictionary
                v-model="formParameter.testmode"
                dkey="sub-kaocha"
                :multiple="false"
                :stringMode="true"
              ></select-option-dictionary>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学分" prop="credit">
              <el-input v-model="formParameter.credit" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总学时" prop="period">
              <el-input v-model="formParameter.period" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="理论课总学时" prop="theoryperiod">
              <el-input v-model="formParameter.theoryperiod" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实践课总学时" prop="actperiod">
              <el-input v-model="formParameter.actperiod" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程目标" prop="cstarget">
              <wang-edit v-model="formParameter.cstarget"></wang-edit>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="主要内容" prop="cscontent">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入内容"
                v-model="formParameter.cscontent">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="教学要求" prop="teachneed">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入内容"
                v-model="formParameter.teachneed">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入内容"
                v-model="formParameter.remarks">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div slot="footer">
      <el-button v-if="!disabled" type="primary"   :loading="confirmLoading" @click="confirm()">确 定</el-button>
      <el-button @click="close">取 消</el-button>
    </div>
  </popup>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api from "@/api/subject/courseplan/courseplan.js";
import selectOptionDictionary from "@/components/biz/selectOptionDictionary/selectOptionDictionary.vue";
import wangEdit from '@/components/wangEdit/wangEdit.vue';
export default {
  name: "edit",
  components: {
  popup,
	selectOptionDictionary,
	wangEdit
  },
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      parameter: {},
      formParameter: {},
	  rules: {
        csprop: [
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
        //考核方式
        testmode: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 32,
            trigger: "blur"
          }
        ],
        //学分
        credit: [
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
        //总学时
        period: [
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
        //理论课总学时
        theoryperiod: [
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
        //实践课总学时
        actperiod: [
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
        //课程目标
        cstarget: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 1024,
            trigger: "blur"
          }
        ],
        //主要内容
        cscontent: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 200,
            trigger: "blur"
          }
        ],
        //教学要求
        teachneed: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 200,
            trigger: "blur"
          }
        ],
        // 备注
        remarks: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 200,
            trigger: "blur"
          }
        ]
      }
    
    };
  },
  methods: {
    //打开弹框
    open(parameter, title, disabled, size) {
      this.parameter = parameter;
      this.disabled = disabled;
      this.$refs.popup.open(title, size);
      this.$nextTick(() => {
        this.init();
      });
    },
    //关闭弹框
    close() {
      this.disabled = false;
      this.formParameter ={}
      this.$refs.popup.close();
    },
    //提交表单
    confirm() {
      this.$utils.checkForm(this.$refs.form, () => {
		  console.log(this.parameter);
		  console.log(this.formParameter);
        this.confirmLoading = true;
        if (!this.formParameter.id) {
          //添加
          let parameter = this.$utils.merger(this.parameter,this.formParameter);
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

    //   if (this.parameter.id) {
        //编辑查询数据
        let parameter = {
          csid: this.parameter.csid,
          sid: this.parameter.sid
        };
        api.toUpdate(parameter, response => {
          this.formParameter = response.data.data;
          console.log(response.data)
          this.popupLoading = false;
        });
    }
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
