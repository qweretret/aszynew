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
        <el-row v-if="!parameter.id">
          <el-col :span="24">
            <el-form-item label="选择专业">
              <el-cascader v-model="value" :options="options" @change="handleChange"></el-cascader>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4" v-if="formParameter.type">
            <el-form-item label="类别:">{{formParameter.type}}</el-form-item>
          </el-col>
          <el-col :span="8" v-if="formParameter.subtype1">
            <el-form-item label="大类:">{{formParameter.subtype1}}</el-form-item>
          </el-col>
          <el-col :span="6" v-if="formParameter.subtype2">
            <el-form-item label="二级类:">{{formParameter.subtype2}}</el-form-item>
          </el-col>
          <el-col :span="6" v-if="formParameter.name">
            <el-form-item label="专业名称:">{{formParameter.name}}</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="设立时间" prop="crdate">
              <el-input v-model="formParameter.crdate" autocomplete="off" type="date"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="学制" prop="year">
              <select-option-dictionary
                v-model="formParameter.year"
                dkey="sub_xz"
                :multiple="false"
                :stringMode="true"
              ></select-option-dictionary>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="生源类型" prop="stutype">
              <select-option-dictionary
                v-model="formParameter.stutype"
                dkey="sub_sy"
                :multiple="true"
                :stringMode="true"
              ></select-option-dictionary>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="ramark">
              <el-input v-model="formParameter.ramark" autocomplete="off"></el-input>
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
import api from "@/api/subject/subject/subject.js";
import apiType from "@/api/subject/subjecttypes/subjecttypes.js";
import selectOptionDictionary from "@/components/biz/selectOptionDictionary/selectOptionDictionary.vue";
export default {
  name: "edit",
  components: {
    popup,
    selectOptionDictionary
  },
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      value: [],
      parameter: {},
      formParameter: {},
      options: [],
      rules: {}
    };
  },
  methods: {
    handleChange(value) {
      //清空
      this.formParameter.type = "";
      this.formParameter.subtype1 = "";
      this.formParameter.subtype2 = "";
      this.formParameter.name = "";

      //回显
      // this.formParameter.type = value[0];
      // if (value.length > 1) this.formParameter.subtype1 = value[1];
      // if (value.length > 2) this.formParameter.subtype2 = value[2];
      // if (value.length > 3) this.formParameter.name = value[3];
      if (value.length === 4) {
        this.formParameter.subtype1 = value[1];
        this.formParameter.subtype2 = value[2];
      } else if (value.length === 3) {
        this.formParameter.subtype1 = value[1];
      }
      this.formParameter.type = value[0];
      this.formParameter.name = value[value.length - 1];
    },
    //获取数据
    list() {
      apiType.typeList({}, response => {
        this.options = this.getTreeData(response.data);
      });
    },
    //将数据转为elementui cascader所需格式
    getTreeData(data) {
      for (var i = 0; i < data.length; i++) {
        data[i].label = data[i].name;
        data[i].value = data[i].name;
        if (data[i].children.length < 1) {
          data[i].children = undefined; //避免children为空的时候，下一级显示为空白
        } else {
          this.getTreeData(data[i].children);
        }
      }
      return data;
    },
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
      this.$refs.popup.close();
    },
    //提交表单
    confirm() {
      // console.log(this.value);
      if (this.value.length === 4) {
        this.formParameter.subtype1 = this.value[1];
        this.formParameter.subtype2 = this.value[2];
      } else if (this.value.length === 3) {
        this.formParameter.subtype1 = this.value[1];
      }
      this.formParameter.type = this.value[0];
      this.formParameter.name = this.value[this.value.length - 1];
      this.$utils.checkForm(this.$refs.form, () => {
        this.confirmLoading = true;
        if (!this.parameter.id) {
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
      this.value = [];
      //清楚校验
      this.$refs.form.clearValidate();
      //加载专业类别
      this.list();

      if (this.parameter.id) {
        //编辑查询数据
        let parameter = {
          id: this.parameter.id
        };
        api.toUpdate(parameter, response => {
          this.formParameter = response.data;
          this.value.push(this.formParameter.type);
          this.value.push(this.formParameter.subtype1);
          this.value.push(this.formParameter.subtype2);
          this.value.push(this.formParameter.name);

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
rules: {}
      // rules: {
      // 			//姓名
      // 			name: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					max: 50,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // 			//类别
      // 			type: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					max: 50,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // 			//大类名称
      // 			subtype1: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					max: 50,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // 			//二级类名称
      // 			subtype2: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					max: 50,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // 			//设立时间
      // 			crdate: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					// max: ,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // 			//学制
      // 			year: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					max: 11,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // 			//生源类型
      // 			stutype: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					max: 20,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // 			//备注
      // 			ramark: [
      // 				{
      // 					validator: this.$validate.required,
      // 					trigger: 'blur'
      // 				},
      // 				{
      // 					validator: this.$validate.length,
      // 					max: 200,
      // 					trigger: 'blur'
      // 				}
      // 			],
      // }