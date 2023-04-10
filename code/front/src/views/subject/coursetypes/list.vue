<template>
	<div class="tab-body">
		<div class="search">
			<el-form ref="form" :model="searchParams" label-width="80px">
				<el-row>
					<el-col :span="6">
						<el-form-item label="类型名称">
							<el-input v-model="searchParams.name"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="类型code">
							<el-input v-model="searchParams.code"></el-input>
						</el-form-item>
					</el-col>
					<el-collapse-transition>
						<div v-if="isShowMoreSearch">
							<!--隐藏区域-->
						</div>
					</el-collapse-transition>
					<div class="search-btn">
						<el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
						<el-button @click="clearSearchVal">清空</el-button>
					</div>
				</el-row>
			</el-form>
		</div>
		<div class="operator">
			<el-button type="primary" v-has-permission-code-and="'10210210'" icon="el-icon-plus" @click="toSave()">
				添加一级课程类型</el-button>
		</div>
		<div class="dataTable autoFlex" v-if="listData">
			<el-table ref="table" class="table" :data="listData.records" stripe border height="auto" row-key="id"
				:tree-props="{children: 'children', hasChildren: 'hasChildren'}" :select-on-indeterminate="false"
				@select="select" @select-all="selectAll" @selection-change="selectionChange" v-loading="listLoading">

				<el-table-column type="selection" width="55" fixed="left"></el-table-column>

				<el-table-column prop="code" label="课程类型代码"></el-table-column>
				<el-table-column prop="name" label="课程类型名称"></el-table-column>
				<el-table-column prop="mdtm" label="修改时间"></el-table-column>
				<el-table-column prop="dbColumn_mder" label="修改人"></el-table-column>

				<el-table-column label="操作" fixed="right" width="300px">
					<template slot-scope="scope">
						<div class="dataTable-operator">
							<el-button v-if="scope.row.code<100" v-has-permission-code-and="'10210210'" size="mini"
								icon="el-icon-search" @click="toAddSon(scope.row)">添加子分类</el-button>
							<el-button size="mini" icon="el-icon-edit" v-has-permission-code-and="'10210220'"
								@click="toUpdate(scope.$index, scope.row)">编辑</el-button>
							<el-button size="mini" icon="el-icon-delete" v-has-permission-code-and="'10210230'"
								type="danger" @click="remove(scope.$index, scope.row)">删除</el-button>
						</div>
					</template>
				</el-table-column>
			</el-table>

			<div class="pageBar">
				<el-pagination @size-change="sizeChange" @current-change="currentChange" :total="listData.total"
					:page-size="page.size" :current-page="page.current" :layout="this.$constant.page.layout"
					:page-sizes="this.$constant.page.pageSizes"></el-pagination>
			</div>

		</div>
		<edit ref="edit"></edit>
	</div>
</template>
<script>
import edit from './edit.vue';
import api from '@/api/subject/coursetypes/coursetypes.js';
export default {
	name: 'coursetypes',
	components: {
		edit
	},
	data() {
		return {
			searchParams: {},
			isShowMoreSearch: false,
			listLoading: false,
			listData: [],
			selected: [],
			page: new this.$constant.pageObj(),
		};
	},
	methods: {
		//获取数据
		list() {
			this.listLoading = true;
			let params = this.$utils.merger(this.searchParams, this.page);
			api.list(params, response => {
				this.listData = response.data;
				this.listLoading = false;
			})
		},
		//去添加
		toSave() {
			this.$refs.edit.open({}, 2, '添加');
		}, toAddSon(row) {
			//添加子类
			this.$refs.edit.open(row, 1, '添加 【' + row.name + '】 的子分类');
		},
		//去编辑
		toUpdate(index, row) {
			let editParameter = {
				id: row.id
			};
			this.$refs.edit.open(editParameter, 0, '编辑');
		},
		//去详情
		toDetail(index, row) {
			let detailParameter = {
				id: row.id
			};
			this.$refs.edit.open(detailParameter, '详情', true);
		},
		//删除
		remove(index, row) {
			let parameter = {
				ids: row.id
			}
			this.$utils.confirm.warning('提示', '确定删除吗？', () => {
				api.remove(
					parameter,
					response => {
						this.$utils.msg.success(response.msg);
						this.list();
					},
					response => {
						this.$utils.msg.warning(response.msg);
					}
				);
			});
		},
		//批量删除
		batchRemove() {
			if (!this.selected || this.selected.length == 0) {
				this.$utils.msg.info('请选择至少一行');
				return;
			}
			let parameter = {
				ids: this.selected
			};
			this.$utils.confirm.warning('提示', '确定删除吗？', () => {
				api.remove({
					ids: this.selected
				},
					response => {
						this.$utils.msg.success(response.msg);
						this.list();
					},
					response => {
						this.$utils.msg.warning(response.msg);
					}
				);
			});
		}, setChildren(children, type) {
			// 编辑多个子层级
			children.map(j => {
				this.toggleSelection(j, type)
				if (j.children) {
					this.setChildren(j.childList, type)
				}
			})
		},
		// 选中父节点时，子节点一起选中取消
		select(selection, row) {
			if (selection.some(el => { return row.id === el.id })) {
				if (row.children) {
					this.setChildren(row.children, true)
				}
			} else {
				if (row.children) {
					this.setChildren(row.children, false)
				}
			}
		},
		toggleSelection(row, select) {
			if (row) {
				this.$nextTick(() => {
					this.$refs.table && this.$refs.table.toggleRowSelection(row, select)
				})
			}
		},
		// 选择全部
		selectAll(selection) {
			// tabledata第一层只要有在selection里面就是全选
			const isSelect = selection.some(el => {
				const tableDataIds = this.listData.records.map(j => j.id)
				return tableDataIds.includes(el.id)
			})
			// tableDate第一层只要有不在selection里面就是全不选
			const isCancel = !this.listData.records.every(el => {
				const selectIds = selection.map(j => j.id)
				return selectIds.includes(el.id)
			})
			console.log(isSelect, 'isSelect')
			if (isSelect) {
				selection.map(el => {
					if (el.children) {
						this.setChildren(el.children, true)
					}
				})
			}
			if (isCancel) {
				 this.listData.records.map(el => {
					if (el.children) {
						this.setChildren(el.children, false)
					}
				})
			}
			this.$emit('handleSelect', this.listData.records)
		},

		//改变选择项
		selectionChange(val) {
			//清空
			this.selected = [];
			//console.log(this.selected)
			for (let item of val) {
				this.selected.push(item.id);
				//if (item.children && item.children.length > 0) {
				//	item.children.forEach(v => {
				//		this.selected.push(v.id);
				//	});
				//}
			}
			console.log(this.selected)
			//this.$forceUpdate()    this.$set    没搞定
		},
		//改变每页显示数量
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
			this.list();
		},
		//清空搜索框
		clearSearchVal() {
			this.searchParams = {};
			this.list();
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

