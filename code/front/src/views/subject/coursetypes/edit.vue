<template>
	<popup ref="popup" :loading="popupLoading">
		<div slot="body">
			<el-form class="dataForm" ref="form" :disabled="disabled" :model="formParameter" :inline="true" label-width="120px" :rules="rules">
					<el-row>
						<el-col :span="12">
							<el-form-item label="一级代码" prop="code">
								<el-input  :readonly="opt==1"  placeholder="长度2位数值"   v-model="formParameter.code" autocomplete="off"></el-input>
								</el-form-item>
						</el-col>
						</el-row><el-row>
						<el-col :span="12" v-if="opt==1">
							<el-form-item label="二级代码" prop="codeson"><el-input v-model="codeson" autocomplete="off"  placeholder="不含父code的两位数值" ></el-input></el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="24">
							<el-form-item label="分类名称" prop="name"><el-input v-model="formParameter.name" autocomplete="off"></el-input></el-form-item>
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
import popup from '@/components/popup/drawerPopup.vue';
import api from '@/api/subject/coursetypes/coursetypes.js';
export default {
	name: 'edit',
	components: {
			popup
		},
	data() {
		return {
			disabled: false,
			popupLoading: false,
			confirmLoading: false,
			parameter: {},
			formParameter: {},
			codeson:"",
			opt:1,
			rules: {
						//分类代码
						code: [
							{
								validator: this.$validate.required,
								trigger: 'blur'
							},
							{
								validator: this.$validate.length,
								max: 11,
								trigger: 'blur'
							}
						],
						//分类名称
						name: [
							{
								validator: this.$validate.required,
								trigger: 'blur'
							},
							{
								validator: this.$validate.length,
								max: 32,
								trigger: 'blur'
							}
						],
			}
		};
	},
	methods: {
			//打开弹框
			open(parameter,opt, title, disabled, size) {
				this.parameter = parameter;
 
                this.opt =opt

				if(opt == 0 ){  //修改
				   api.toUpdate(this.parameter,res=>{
					   this.formParameter = res.data
				  })
				}else  if(opt == 1 ){  //添加子分类
					this.formParameter={ code :this.parameter.code }
				}else  if(opt == 2 ){  //添加根分类
					this.formParameter={}
				}
				this.disabled = disabled;
				this.$refs.popup.open(title, size);

			
			},
			//关闭弹框
			close() {
				this.disabled = false;
				this.codeson ="";
				this.parameter={};
				this.$refs.popup.close();
			},
			//提交表单
			confirm() {
				this.$utils.checkForm(this.$refs.form, () => {
					this.confirmLoading = true;
					if ( this.opt > 0 ){
						//添加
						let parameter = this.$utils.merger(this.formParameter);

						if(this.opt == 1){
                           	//添加子分类
							if(this.codeson*1 <100){
								parameter.code = this.formParameter.code + this.codeson
							}else{
								parameter.code =  this.codeson
							}
							
							parameter.id=""
						}
						api.save(parameter,
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
						api.update(parameter,
							response => {
								this.$utils.msg.success(response.msg);
								this.confirmLoading = false;
								this.close();
								//强制刷新父页面
								this.$parent.list();
							},
							response => {
								this.$utils.msg.warning(response.msg);
								this.confirmLoading = false;
							}
						);
					}
				});
			}
			 
		}
	};
</script>

<style lang="scss" scoped="scoped">
	@import '~common/custom/css/common.scss';
	@import '~common/custom/css/popup/popup.scss';
</style>
