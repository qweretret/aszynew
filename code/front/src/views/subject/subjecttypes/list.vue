<template>
	<div class="tab-body">
		<el-row class="autoFlex" :gutter="10">
			<el-col :span="6">
				<el-card class="leftCard">
					<div slot="header" class="clearfix">
						<span>专业类别树</span>
						<el-button @click="expandTree" class="expandBtn" type="primary" icon="el-icon-s-fold"
							size="small" style="margin-left: 20px;"></el-button>
					</div>
					<tree v-loading="treeLoading" ref="subjecttypesTree" :items="subjecttypesList"
						:defaultExpandAll="treeExpand" :defaultParent="0" @nodeClick="treeClick"></tree>
				</el-card>
			</el-col>
			<el-col :span="18">
				<el-card class="rightCard">
					<div slot="header" class="clearfix"><span> {{currentNode.level==0?"类别 - "+currentNode.name:(currentNode.level==1?"大类名称 - "+currentNode.name:(currentNode.level==2?"二级类名称 - "+currentNode.name:(currentNode.level==3?"专业名称 - "+currentNode.name:"根类别列表")))}}
							<el-button type="info" v-show="currentNode.name" size="mini" icon="el-icon-back"
								@click="backToRoot">返回根类别</el-button>
						</span></div>
					<div class="operator">
						<el-button type="primary" v-show="notSubject" v-has-permission-code-and="'1021050000'" icon="el-icon-plus"
							@click="toSave()">添加</el-button>
					</div>
					<div class="dataTable autoFlex">
						<qlh-table  v-show="notSubject" class="table" :data="subjecttypesTable" stripe border height="auto">
							<qlh-table-column prop="name"
								:label='currentNode.level==0?"大类名称":(currentNode.level==1?"二级类名称":(currentNode.level==2?"专业列表":  "类别" ))'
								sortable :editable="false"></qlh-table-column>
							<qlh-table-column prop="idex" label="排序" sortable></qlh-table-column>
							<qlh-table-column label="操作" fixed="right" width="300" ignored :editable="false">
								<template slot-scope="scope">
									<div class="dataTable-operator">
										<el-button size="mini" icon="el-icon-search"
											@click="toDetail(scope.$index, scope.row)">详情
										</el-button>
										<el-button size="mini" v-has-permission-code-and="'1021050000'"
											icon="el-icon-edit" @click="toUpdate(scope.$index, scope.row)">编辑
										</el-button>
										<el-button size="mini" v-has-permission-code-and="'1021050005'" type="danger"
											icon="el-icon-delete" @click="remove(scope.$index, scope.row)">删除
										</el-button>
									</div>
								</template>
							</qlh-table-column>
						</qlh-table>
					</div>
				</el-card>
			</el-col>
		</el-row>
		<edit ref="edit"></edit>
	</div>
</template>
<script>
import edit from './edit.vue';
import tree from '@/components/tree/tree.vue';
import api from '@/api/subject/subjecttypes/subjecttypes.js';
import qlhTable from '@/components/qlhTable/qlhTable.vue';
import qlhTableColumn from '@/components/qlhTable/qlhTableColumn.vue';
export default {
	name: 'subjecttypes',
	components: {
		tree,
		edit,
		qlhTable,
		qlhTableColumn
	},
	data() {
		return {
			treeLoading: false,
			treeRef: 'subjecttypesTree',
			treeExpand: true,
			subjecttypesList: [],
			currentNode: {},
			subjecttypesTable: [],
			tableLoading: true,
			editParameter: {},
			notSubject:true

		};
	},
	methods: {
		//返回根类别
		backToRoot() {
			this.currentNode = {}
			this.notSubject = true
			this.freshTable();
			$('.el-tree-node').removeClass('is-current');
		},
		//获取数据
		list() {
			this.treeLoading = true;
			api.list({}, response => {
				this.subjecttypesList = response.data;
				// console.log(this.subjecttypesList);
				this.treeLoading = false;
				this.freshTable();
			})
		},
		expandTree() {
			this.treeExpand = !this.treeExpand;
			//控制el-tree展开收起
			for (var i = 0; i < this.$refs.subjecttypesTree.getTree().store._getAllNodes().length; i++) {
				this.$refs.subjecttypesTree.getTree().store._getAllNodes()[i].expanded = this.treeExpand;
			}
		},
		//点击类别
		treeClick(data) {
		
				if (this.currentNode == data) {
					this.currentNode = {};
					$('.el-tree-node').removeClass('is-current');
				} else {
					this.currentNode = data;
				}
				if (this.currentNode.level == 3) {
                   this.notSubject = false
                } else {
					this.notSubject = true
					this.freshTable();
			    }

		},
		freshTable() {
			this.tableLoading = true;
			this.subjecttypesTable = [];
			if (this.currentNode.id) {
				for (var d of this.subjecttypesList) {
					if (d.pid == this.currentNode.id) {
						this.subjecttypesTable.push(d);
					}
				}
			} else {
				for (var d of this.subjecttypesList) {
					//根节点
					if (d.pid == 0) {
						this.subjecttypesTable.push(d);
					}
				}
			}
			this.tableLoading = false;
		},
		//去添加
		toSave() {
			let parameter = {
				pid: this.currentNode.id != undefined ? this.currentNode.id : 0,
				level: this.currentNode.level != undefined ? this.currentNode.level + 1 : 0
			}


			this.$refs.edit.open(parameter, '添加');
		},
		//去编辑
		toUpdate(index, row) {
			let parameter = {
				id: row.id
			};
			this.$refs.edit.open(parameter, '编辑');
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
	},

	mounted() {
		this.list();
	}
};
</script>

<style lang="scss" scoped="scoped">
@import '~common/custom/css/common.scss';
</style>
