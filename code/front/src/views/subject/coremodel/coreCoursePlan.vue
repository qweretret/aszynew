<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogVisible"
      width="50%"
    >
        <!-- 其他能力 -->
        <div>
          <p
            style="
              border: 1px solid #e6e6fa;
              padding: 10px;
              text-align: center;
              font-weight: 1000;
            "
          >
            专业核心能力模型
          </p>
          <vxe-table
            border
            :scroll-y="{ enabled: false }"
            :span-method="rowspanMethod"
            :data="tableData"
            style="margin-bottom: 30px"
          >
            <!-- <vxe-column field="name1" title="职业标准">
            <template slot-scope="scope">
              <my-com
                :display="false"
                :parameter="scope.row"
                :coltitle="'职业标准'"
                :scope="scope"
                :col="1"
              ></my-com>
            </template>
          </vxe-column> -->
            <!-- <vxe-column field="name2" title="态度和职业素养">
            <template slot-scope="scope">
              <my-com
                :display="false"
                :parameter="scope.row"
                :col="2"
                :coltitle="'态度和职业素养'"
                :scope="scope"
              ></my-com>
            </template>
          </vxe-column> -->
            <vxe-column field="name3" title="课程分类">
              <template slot-scope="scope">
                <my-com
                  v-show="true"
                  :display="false"
                  :parameter="scope.row"
                  :col="3"
                  :coltitle="'课程分类'"
                  :scope="scope"
                ></my-com>
              </template>
            </vxe-column>
            <vxe-column field="name4" title="课程">
              <template slot-scope="scope">
                <my-com
                  v-show="scope.row.id3"
                  :display="false"
                  :parameter="scope.row"
                  :coltitle="'课程'"
                  :col="4"
                  :scope="scope"
                ></my-com>
              </template>
            </vxe-column>
            <vxe-column field="name5" title="总课程">
              <template slot-scope="scope">
                <my-com
                  v-show="scope.row.id4"
                  :display="false"
                  :parameter="scope.row"
                  :coltitle="'总课程'"
                  :col="5"
                  :scope="scope"
                ></my-com>
              </template>
            </vxe-column>
            <vxe-column field="name6" title="理论">
              <template slot-scope="scope">
                <my-com
                  v-show="scope.row.id5"
                  :display="false"
                  :parameter="scope.row"
                  :coltitle="'理论'"
                  :col="6"
                  :scope="scope"
                ></my-com>
              </template>
            </vxe-column>
            <vxe-column field="name7" title="实训">
              <template slot-scope="scope">
                <my-com
                  v-show="scope.row.id6"
                  :display="false"
                  :parameter="scope.row"
                  :coltitle="'实训'"
                  :col="7"
                  :scope="scope"
                ></my-com>
              </template>
            </vxe-column>
            <!-- <vxe-column field="name8" title="课时">
            <template slot-scope="scope">
              <my-com
                v-show="scope.row.id7"
                :display="false"
                :parameter="scope.row"
                :coltitle="'课时'"
                :col="8"
                :scope="scope"
              ></my-com>
            </template>
          </vxe-column> -->
          </vxe-table>
        </div>

        

       
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import XEUtils, { has } from "xe-utils";
import MyCom from "./myCom.vue";
export default {
  components: {
    MyCom,
  },
  created() {
    // const treeData = XEUtils.toArrayTree(this.getList());
    // this.toColTreeData(treeData);
  },
  data() {
    return {
      dialogVisible: this.display,
      input: "",
      param: {},
      listData:[],
      list:[],
      title: "查看课时安排",
      tableData: [],
    };
  },
  methods: {
    //查看是否有children
    hasChildren(row,column){
      if(row.children){
        let aa=0;
          row.children.forEach((item)=>{           
            if(aa==0){
              --column
              aa++;
            }
            if(column==0){
              this.list.push(item)
            }
            while(column>0){
              this.hasChildren(item,column)
              break;
            }
          })
        }
    },
    //处理treeDate
    getClassHour(treeData){
      this.list=[];
      treeData.forEach((item)=>{
          this.hasChildren(item,6);
        })
        treeData=this.list
          
      
      // this.param = parameter;
      // this.title = title;
      this.listData = [];
      this.cTypes = [];
      //整理数据，根据cType和每给行cType添加小计
      //先取出cType
      treeData.forEach((item) => {
        if (this.cTypes.length < 1) {
          this.cTypes.push(item.cType);
        } else {
          if (this.cTypes.indexOf(item.cType) == -1) {
            this.cTypes.push(item.cType);
          }
        }
      });
      //有几条数据
      let k = 0;
      //总课时
      let allTime = 0;
      let allSx = 0;
      //根据cType来排列数据
      for (let i = 0; i < this.cTypes.length; i++) {
        //在找出每个cType结束后插入小计计
        //每个类型cType小计需要的数据
        let cAlls = 0;
        let cSxs = 0;
        let cType = "";
        treeData.forEach((item) => {
          //要取的临时变量
          let keys = [];
          let cName = "";
          let cAll = 0;
          let cSx = 0;
          let id = "";
          if (item.cType == this.cTypes[i]) {
            k++;
            //临时数据赋值
            cName = item.name;
            cType = item.cType;
            cAll += item.all * 1;
            cSx += item.sx * 1;
            cAlls += cAll * 1;
            cSxs += cSx * 1;
            //变成树形结构
            //每行每一格的数据
            for (let j = 0; j < 5; j++) {
              //定义对象数据模型
              let data = {};
              id = id + "01";
              data.id = id;
              keys.push(id);
              data.children = [];
              //将一个数组克隆给新数组
              var key = [].concat(keys);
              //设置
              if (j == 0) {
                data.keys = key;
                data.name = cType;
                this.listData[k - 1] = data;
              } else if (j == 1) {
                data.keys = key;
                data.name = cName;
                this.listData[k - 1].children[0] = data;
              } else if (j == 2) {
                data.keys = key;
                data.name = cAll;
                this.listData[k - 1].children[0].children[0] = data;
              } else if (j == 3) {
                data.keys = key;
                data.name = cAll - cSx;
                this.listData[k - 1].children[0].children[0].children[0] = data;
              } else if (j == 4) {
                data.keys = key;
                data.name = cSx;
                this.listData[
                  k - 1
                ].children[0].children[0].children[0].children[0] = data;
              }
            }
          }
        });
        k++;
        // let xiaoji={};
        //小计
        let xiaoji={id:"01", keys:['01'], name:cType, children:[{id:"0101", keys:['01','0101'], name:'小计'
        , children:[{id:"010101", keys:['01','0101','010101'], name:cAlls
        , children:[{id:"01010101", keys:['01','0101','010101','01010101'], name:cAlls-cSxs
        , children:[{id:"0101010101", keys:['01','0101','010101','01010101','0101010101'], name:cSxs}]
        }]
        }]
        }]}
        this.listData.push(xiaoji);
        allTime += cAlls;
        allSx += cSxs;
      }
      //总计
      let all={id:"01", keys:['01'], name:'总课时', children:[{id:"0101", keys:['01','0101'], name:'总课时'
        , children:[{id:"010101", keys:['01','0101','010101'], name:allTime
        , children:[{id:"01010101", keys:['01','0101','010101','01010101'], name:allTime-allSx
        , children:[{id:"0101010101", keys:['01','0101','010101','01010101','0101010101'], name:allSx}]
        }]
        }]
        }]}
        this.listData.push(all);
    },
    //打开弹框
    open(treeData,tip) {
      this.getClassHour(treeData);
      this.toColTreeData(this.listData)

      this.dialogVisible = true;

      //  if (this.title.substr(0,2) == "编辑") {
      /*  let x = {name:"tom"}
            x.name  x["name"]  伪数据*/
      // this.input = this.param.parameter[`name${this.param.col}`];
      // }
    },
    //关闭弹框
    close() {
      this.tableData= [],
      this.listData=[];
      this.list=[];
      this.param = {};
      this.input = "";
      this.dialogVisible = false;
    },
    // /////////////////////////////////////////////vxe表格方法↓
    check1ChangeEvent(row, checked) {
      const { tableData } = this;
      let childList = tableData.filter((item) => item.name1 === row.name1);
      childList.forEach((item) => {
        item.check1 = checked;
      });
      childList = this.tableData.filter((item) => item.id1 === row.id1);
      childList.forEach((item) => {
        this.check2ChangeEvent(item, checked);
      });
    },
    check2ChangeEvent(row, checked) {
      const { tableData } = this;
      let childList = tableData.filter(
        (item) => item.id1 === row.id1 && item.name2 === row.name2
      );
      childList.forEach((item) => {
        item.check2 = checked;
      });
      childList = this.tableData.filter((item) => item.id2 === row.id2);
      childList.forEach((item) => {
        this.check3ChangeEvent(item, checked);
      });
    },
    check3ChangeEvent(row, checked) {
      const { tableData } = this;
      let childList = tableData.filter(
        (item) => item.id2 === row.id2 && item.name3 === row.name3
      );
      childList.forEach((item) => {
        item.check3 = checked;
      });
      childList = tableData.filter((item) => item.id3 === row.id3);
      childList.forEach((item) => {
        this.check4ChangeEvent(item, checked);
      });
    },
    check4ChangeEvent(row, checked) {
      const { tableData } = this;
      let childList = tableData.filter(
        (item) => item.id3 === row.id3 && item.name4 === row.name4
      );
      childList.forEach((item) => {
        item.check4 = checked;
      });
      childList = tableData.filter((item) => item.id3 === row.id3);
      const isChecked3 = childList.every((item) => item.check4);
      childList.forEach((item) => {
        item.check3 = isChecked3;
      });
      childList = tableData.filter((item) => item.id2 === row.id2);
      const isChecked2 = childList.every((item) => item.check3);
      childList.forEach((item) => {
        item.check2 = isChecked2;
      });
      childList = tableData.filter((item) => item.id1 === row.id1);
      const isChecked1 = childList.every((item) => item.check2);
      childList.forEach((item) => {
        item.check1 = isChecked1;
      });
    },
    getList() {
    //   const list = [
    //     { id: "10000", parentId: null, name: "账号管理" },
    //     { id: "11000", parentId: "10000", name: "用户管理" },
    //     { id: "11100", parentId: "11000", name: "查看" },
    //     { id: "11110", parentId: "11100", name: "用户列表" },
    //     { id: "11200", parentId: "11000", name: "编辑" },
    //     { id: "11210", parentId: "11200", name: "用户列表" },
    //     { id: "11220", parentId: "11200", name: "新增用户" },
    //     { id: "11300", parentId: "11000", name: "操作" },
    //     { id: "11310", parentId: "11300", name: "新增" },
    //     { id: "11320", parentId: "11300", name: "删除" },
    //     { id: "11330", parentId: "11300", name: "修改" },
    //     { id: "12000", parentId: "10000", name: "角色管理" },
    //     { id: "12100", parentId: "12000", name: "查看" },
    //     { id: "12110", parentId: "12100", name: "角色列表" },
    //     { id: "12200", parentId: "12000", name: "编辑" },
    //     { id: "122100", parentId: "12200", name: "角色列表" },
    //     { id: "12220", parentId: "12200", name: "新增角色" },
    //     { id: "12300", parentId: "12000", name: "操作" },
    //     { id: "12310", parentId: "12300", name: "新增" },
    //     { id: "12320", parentId: "12300", name: "删除" },
    //     { id: "12330", parentId: "12300", name: "修改" },
    //     { id: "20000", parentId: null, name: "个人中心" },
    //     { id: "21000", parentId: "20000", name: "个性化设置" },
    //     { id: "21100", parentId: "21000", name: "查看" },
    //     { id: "21110", parentId: "21100", name: "信息列表" },
    //     { id: "21200", parentId: "21000", name: "操作" },
    //     { id: "21210", parentId: "21200", name: "重置信息" },
    //     { id: "22000", parentId: "20000", name: "账户管理" },
    //     { id: "22100", parentId: "22000", name: "查看" },
    //     { id: "22110", parentId: "22100", name: "账户余额" },
    //     { id: "22120", parentId: "22100", name: "帐变记录" },
    //   ];
    //   return list;
    },
    // 将普通树结构转换为横向树列表
    toColTreeData(treeData) {
      const options = { children: "children" };
      const list = [];
      const keyMap = {};
      XEUtils.eachTree(
        treeData,
        (item, index, result, paths, parent) => {
          keyMap[item.id] = item;
          item.keys = parent ? parent.keys.concat([item.id]) : [item.id];
          if (!item.children || !item.children.length) {
            const row = {};
            item.keys.forEach((key, index) => {
              const level = index + 3;
              const obj = keyMap[key];
              row[`check${level}`] = false;
              row[`id${level}`] = obj.id;
              row[`name${level}`] = obj.name;
            });
            list.push(row);
          }
        },
        options
      );
      this.keyMap = keyMap;
      this.tableData = list;

    },
    // 通用行合并函数（将相同多列数据合并为一行）
    rowspanMethod({ row, _rowIndex, column, visibleData }) {
      const fields = ["name1", "name2", "name3"];
      const cellValue = row[column.property];
      if (cellValue && fields.includes(column.property)) {
        const prevRow = visibleData[_rowIndex - 1];
        let nextRow = visibleData[_rowIndex + 1];
        if (prevRow && prevRow[column.property] === cellValue) {
          return { rowspan: 0, colspan: 0 };
        } else {
          let countRowspan = 1;
          while (nextRow && nextRow[column.property] === cellValue) {
            nextRow = visibleData[++countRowspan + _rowIndex];
          }
          if (countRowspan > 1) {
            return { rowspan: countRowspan, colspan: 1 };
          }
        }
      }
    },
    // /////////////////////////////////////////////vxe表格方法↑
  },
};
</script>

<style></style>
