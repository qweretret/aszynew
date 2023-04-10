<template>
  <popup ref="popup" :loading="popupLoading">
    <div slot="body">
      <el-form class="dataForm" ref="form" :disabled="disabled" :model="formParameter" :inline="true"
        label-width="120px" :rules="rules">
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程名称" prop="name">
              <el-autocomplete class="inline-input" v-model="course" :fetch-suggestions="querySearch"
                placeholder="请选择课程" @select="courseSelected"></el-autocomplete>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程代码" prop="code">
                  <el-input placeholder="8位 = 3位系部编码+2位专业流水号+3位数值课程流水号" v-model="formParameter.code" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程类型" prop="type">
              <select-option-dictionary v-model="formParameter.type" dkey="sub-kctype" :multiple="false"
                :stringMode="true"></select-option-dictionary>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程类别1级" prop="level1">
              <el-select v-model="formParameter.level1" placeholder="请选择" @change="change1($event)">
                <el-option v-for="item in courseTypeList" :key="item.id" :label="item.name" :value="item.code">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="课程类别2级" prop="level2">
              <el-select v-model="formParameter.level2" placeholder="请选择"  >
                <el-option v-for="item in type2" :key="item.id" :label="item.name" :value="item.code"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="所属专业" prop="subjectid">
              <el-select v-model="formParameter.subjectid" placeholder="请选择">
                <el-option v-for="item in subjectTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
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
import api from "@/api/subject/course/course.js";
import tyapi from "@/api/subject/coursetypes/coursetypes.js";
import subtyapi from "@/api/subject/subject/subject.js";
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
      parameter: {},
      formParameter: {},
      courseTypeList: [],
      subjectTypes: [],
      type2: [],
  
      courseList: [],
      course: "",
      subjectid: "",

      rules: {

        //课程代码
        code: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 8,
            trigger: "blur"
          },{
            validator: this.$validate.length,
            min: 8,
            trigger: "blur"
          }

        ],
        //课程类型
        type: [
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
        //课程类别1级
        level1: [
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
        //课程类别2级
        level2: [
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
        //所属专业
        subjectid: [
          {
            validator: this.$validate.required,
            trigger: "blur"
          },
          {
            validator: this.$validate.length,
            max: 32,
            trigger: "blur"
          }
        ]
      }
    };
  },

  methods: {
    //选择课程
    courseSelected(course) {
      api.toUpdate({ id: course.id }, res => {
        this.formParameter = res.data
      })
    }, querySearch(queryString, cb) {
      var clist = this.courseList;
      var results = queryString ? clist.filter(this.createFilter(queryString)) : clist;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (clist) => {
        return (clist.name.indexOf(queryString) === 0);
      };
    },

    //二级select框跟随第一级change事件
    change1(val) {
      if (val) {
         
          this.type2 = [];
          this.$set(this.formParameter, "level2", "");
          this.courseTypeList.forEach(item => {
            if (item.code == val) {
              if (item.children) {
                this.type2 = item.children;
              }
            }
          });
      
      }

    },
    //打开弹框
    open(parameter, title, disabled, size) {

      if (parameter.id) {
        this.parameter = parameter;
        this.formParameter.subjectid = this.parameter.id
      } else {
        this.subjectid = parameter.subId;
        this.parameter = {}
      }

      this.disabled = disabled;
      this.$refs.popup.open(title, size);

      this.$nextTick(() => {
        this.init();
      });
    },
    subList() {
      subtyapi.list({}, response => {
        this.subjectTypes = response.data.records;
      });
    },

    //关闭弹框
    close() {
      this.disabled = false;
      this.course = ""
      this.code2 = false
      this.formParameter = {}
      this.parameter = {}
      this.$refs.popup.close();
    },
    //提交表单
    confirm() {

      this.$utils.checkForm(this.$refs.form, () => {
        this.confirmLoading = true;
        if (!this.parameter.id) {

          //添加
          let parameter = this.$utils.merger(this.formParameter, { name: this.course });
          //删除id，克隆一个
          delete parameter.id
          if(parameter.name.indexOf("[")>0){
            parameter.name = parameter.name.substr(0, parameter.name.indexOf("["))
          }
          api.save(
            parameter,
            response => {

              this.$utils.msg.success(response.msg);
              this.confirmLoading = false;
              this.close();
              this.$parent.list();
            },
            error => {

              this.$utils.msg.warning(error.msg);
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
    },typeList(){
        //两个select框的数据
        tyapi.list({}, response => {
            this.courseTypeList = response.data.records;
            this.courseTypeList.forEach(element => {
              //第2个select框的数据 Level2
              if (this.formParameter.level1 == element.code) {
                this.type2 = element.children;
              }
            });
          });
    },
    init() {
      this.popupLoading = true;
      //清除等待
      this.confirmLoading = false;

      //清空内容
      if (this.subjectid && this.subjectid.length > 1) {
        this.formParameter = { subjectid: this.subjectid };
      } else {
        this.formParameter = {}
      }

      //清楚校验
      this.$refs.form.clearValidate();

      // this.change1(10)
      this.subList();

      if (this.parameter.id) {
        //编辑查询数据
        let parameter = {
          id: this.parameter.id
        };
        api.toUpdate(parameter, response => {
          this.formParameter = response.data;
          this.code2 = false
          if (this.formParameter.level2) {
            let lv2 = this.formParameter.level2 % 100
            this.code2 = lv2 < 10 ? "0" + lv2 : lv2 + ""
          }
          this.course = this.formParameter.name
          this.typeList()

          // this.change1(this.formParameter.level1  )
          this.popupLoading = false;
        });
      } else {
        //添加
        this.popupLoading = false;
        api.list3({}, res => {
          this.courseList = res.data
          this.courseList.forEach(x => {
            x.value = x.name
          })
          this.typeList()
        });
      }
    }
  }
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
