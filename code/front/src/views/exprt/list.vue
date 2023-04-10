<template>
	<div class="tab-body">

	   <div class="search">
				<el-row>
					<el-col :span="24">
					     <excel-upload  drag  v-model="fileValue" model="fileSystem"  :size="2048" uri="/statics/zysztmp">
						 </excel-upload>
					</el-col>
                    <el-col :span="24"><el-tag type="success">{{fileValue}}</el-tag></el-col>		
				</el-row>
		</div>

        <div class="dataTable autoFlex" v-if="listData">
			<el-table class="table" :data="listData.records" stripe border height="auto" @selection-change="selectionChange" v-loading="listLoading">
						<el-table-column type="selection" width="55" fixed="left"></el-table-column>
						<el-table-column prop="name" label="专业名称xxx"></el-table-column>
						<el-table-column prop="grade" label="年级"></el-table-column>
				<el-table-column label="操作" fixed="right" width="600px">
					<template slot-scope="scope">
						<div class="dataTable-operator">
							<!-- <el-button   size="mini" icon="el-icon-search" @click="toCmd(scope.row)">导出核心能力</el-button>
						    <el-button   size="mini" icon="el-icon-edit" @click="toCmd('')">导出人才培养方案</el-button>
							<el-button   size="mini" icon="el-icon-edit" @click="toCmd('')">导出课程标准</el-button> -->
							<el-button   size="mini" icon="el-icon-edit" @click="toPlan('')">导出学期周数安排</el-button>
                        </div>
					</template>
				</el-table-column>
			</el-table>
			<div class="pageBar">
				<el-pagination 
				@size-change="sizeChange" 
				@current-change="currentChange" 
				:total="listData.total" 
				:page-size="page.size"
				:current-page="page.current" 
				:layout="this.$constant.page.layout" 
				:page-sizes="this.$constant.page.pageSizes"></el-pagination>
			</div>
		</div>
		<edit ref="edit"></edit>
	</div>
</template>
<script>
import edit from './edit.vue';
import api from '@/api/subject/subject/subject.js';
import apiExp from '@/api/exprt/exprt.js';
import excelUpload from '@/components/biz/fileUpload/dragFileUpload';

export default {
	name: 'subject',
	components: {
		edit,excelUpload
	},
	data() {
		return {
			listLoading: false,
			listData: [],
			page: new this.$constant.pageObj(),
			fileValue:""
		};
	},
	methods: {
		toPlan(){
			apiExp.getTimePlan({},res=>{

			})
		},
		//获取数据
		list() {
			this.listLoading = true;
			let params = this.$utils.merger(this.searchParams, this.page);
			api.list(params, response => {
				this.listData = response.data;
				console.log(response)
				this.listLoading = false;
			})
		},
		//核心能力模型
		toCmd(row) {

			apiExp.cmd({id:row.id}, res => {
			    console.log(res)
			})
			 
		},
		//人才培养方案
		toTvp() {
			apiExp.tvp({}, response => {
			 
			})
		},
		//课程标准
		toCtv() {
			apiExp.ctv({}, response => {
			 
			})
		}
	  
	},
	mounted() {
		this.list();
	}
};
</script>

<style lang="scss" scoped="scoped">
	@import '~common/custom/css/common.scss';
 
</style>

