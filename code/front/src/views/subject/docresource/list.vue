<template>
	<div class="tab-body">
		<div class="search">
			<el-form ref="form" :model="searchParams" label-width="80px">
				<el-row>
					<el-col :span="10">
						<el-form-item label="名称">
							<el-autocomplete class="inline-input" v-model="searchParams.name"
								:fetch-suggestions="queryName" placeholder="请输入内容" :trigger-on-focus="false"
								:popper-append-to-body="false" style="width:100%"></el-autocomplete>
						</el-form-item>
					</el-col>
					<div class="search-btn">
						<el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
						<el-button @click="clearSearchVal">清空</el-button>
					</div>
				</el-row>
			</el-form>
		</div>
		<div class="dataTable autoFlex" v-if="listData">
			<el-table class="table" :row-class-name="tableRowClassName" :data="listData.records" stripe border
				height="auto" v-loading="listLoading">
				<el-table-column label="" type="index" min-width="5%"></el-table-column>

				<el-table-column prop="name" label="名称" min-width="25%"></el-table-column>
				<el-table-column label="资料" show-overflow-tooltip min-width="55%">
					<template slot-scope="scope">
						<a  v-if="scope.row.type==1" style="color:blue" :href="scope.row.weburl" target="_blank" class="buttonText">{{
								scope.row.weburl
						}}</a>
						<el-button size="mini"  v-else @click="dwonDoc(scope.row.weburl,scope.row.name)">下载</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="crtm" label="创建时间" min-width="15%"></el-table-column>

			</el-table>
			<div class="pageBar">
				<el-pagination @size-change="sizeChange" @current-change="currentChange" :total="listData.total"
					:page-size="page.size" :current-page="page.current" :layout="this.$constant.page.layout"
					:page-sizes="this.$constant.page.pageSizes"></el-pagination>
			</div>
		</div>

	</div>
</template>
<script>

import api from '@/api/subject/docresource/docresource.js';
export default {
	name: 'docresource',
	components: {
	},
	data() {
		return {
			searchParams: {},
			listLoading: false,
			listData: [],
			// selected: [],
			page: new this.$constant.pageObj(),
		};
	},
	methods: {
		//获取数据
		list() {
			this.listLoading = true;
			this.searchParams.subid = ""
			let params = this.$utils.merger(this.searchParams, this.page);
			api.list(params, response => {
				this.listData = response.data;
				this.listLoading = false;
			})
		},

		queryName(queryString, cb) {
			api.intnName({ name: queryString }, res => {
				let names = res.data;
				let results = queryString ? names.filter(this.nameFilter(queryString)) : names;
				// 调用 callback 返回建议列表的数据
				cb(results);
			})
		},
		nameFilter(queryString) {
			return (restaurant) => {
				return (restaurant.value.indexOf(queryString) === 0);
			};
		},
		tableRowClassName({ row, rowIndex }) {
			if (rowIndex / 2 == 0) {
				return 'success-row';
			}
			if (rowIndex / 2 == 1) {
				return 'success-row';
			}

			return 'success-row';
		},
	 
		sizeChange(val) {
			this.page.size = val;
			this.list();
		},
		//改变现在的页码
		currentChange(val) {
			this.page.current = val;
			this.list();
		},

		//搜索
		search() {
			this.listLoading = true;
			let params = {
				name: this.searchParams.name,
			};
			this.list();
		},
		//清空搜索框
		clearSearchVal() {
			this.searchParams = {};
			this.list();
		},dwonDoc(fpath,fname){
         api.downDoc(fpath,fname +fpath.substring( fpath.lastIndexOf(".") )  ,"word");
        }
	},
	mounted() {
		this.list();
	}
};
</script>
<style>
.el-table .success-row {
	background: #f0f9eb;
}
</style>
<style lang="scss" scoped="scoped">
@import '~common/custom/css/common.scss';

/deep/ .el-autocomplete-suggestion {
	width: auto !important;
}
</style>

