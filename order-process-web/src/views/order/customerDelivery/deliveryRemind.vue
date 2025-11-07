<template>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

        <el-form-item label="客户名称" prop="supplierName">
            <el-input v-model="queryParams.customerName" placeholder="请输入客户名称"></el-input>
        </el-form-item>

        <el-form-item label="客户类别" prop="supplierType">
          <el-select v-model="queryParams.customerType">
            <el-option v-for="dict in dict.type.customer_type" :key="dict.value" :value="dict.value" :label="dict.label"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="产品" prop="materielId">
          <el-select v-model="queryParams.materielId" filterable placeholder="请选择产品">
            <el-option v-for="item in materielList" :key="item.materielId" :value="item.materielId" :label="item.materielName"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="采购单号" prop="orderNo">
            <el-input v-model="queryParams.orderNo" placeholder="请输入采购单号"></el-input>
        </el-form-item>

        <el-form-item label="送货状态" prop="status">
          <el-select v-model="queryParams.status" multiple placeholder="请选择">
            <el-option label="全部送货" value="全部送货"></el-option>
            <el-option label="部分送货" value="部分送货"></el-option>
            <el-option label="未送货" value="未送货"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="送货地址" prop="address">
            <el-input v-model="queryParams.address" placeholder="请输入送货地址"></el-input>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handlePrint">打印</el-button>
          <!-- <el-button icon="el-icon-refresh" size="mini" @click="handleExport">导出</el-button> -->
          <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['order:deliveryRemind:export']">导出</el-button>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>

      <div class="bgBox">
      
        <!-- <el-row :gutter="10" class="mb8">
            <el-col :span="24" class="tatle">
            送货提醒表
            </el-col>
        </el-row>
        <el-row :gutter="10" class="mb8 taxtRow">
            <el-col :span="8" class="tableHeadTaxt">
                销售数量:<span class="tableHeadAmount">{{sumNum}}</span>
            </el-col>
            <el-col :span="7" class="tableHeadTime tableHeadTaxt">
            {{timeStart}}——{{ timeEnd }}
            </el-col>
            <el-col :span="8" class="tableHeadUnit tableHeadTaxt">
            单位：元
            </el-col>
        </el-row> -->
    
        <!-- :span-method="objectSpanMethod" -->
        <el-table border ref="brtTable" v-loading="loading" :data="deliveryRemindList"
           show-summary :summary-method="getSummaries" :span-method="objectSpanMethod" @row-click="rowClick" :row-class-name="tableRowClassName">

            <el-table-column label="送货提醒表" align="center">
            <template slot="header" slot-scope="scope">
              <div>
                <h1>送货提醒表</h1>
                <div class="statement-table-title">
                  <div class="textl">销售总额:¥<span> {{sumNum}}</span></div>
                  <div>
                    {{parseTime(timeStart,'{y}.{m}.{d}')}}-{{parseTime(timeEnd,'{y}.{m}.{d}')}}
                  </div>
                  <div class="textr">单位:元</div>
                </div>
              </div>
            </template>
            <!-- 循环字段 -->
            <template v-for="(item,index) in cacheCloumnList">
            <el-table-column v-if="item.visible" :label="item.label" :prop="item.prop" :width="item.width||''"
                :align="item.align||'center'">
                <template slot-scope="scope">
                <!-- 字典 -->
                <dict-tag v-if="item.type == 'dict'" :options="dict.type[item.dictType]" :value="scope.row[item.prop]" />
                <dict-tag v-else-if="item.type == 'objDict'" :options="dict.type[item.dictType]"
                    :value="getObjAttr(scope.row, item.prop)" />
                <!-- 日期 -->
                <span v-else-if="item.type == 'date'">{{ parseTime(scope.row[item.prop], item.dateFormat) }}</span>
                <!-- 图片 -->
                <image-preview v-else-if="item.type == 'img'" :src="scope.row[item.prop]" :width="50" :height="50" />
                <!-- 多层对象 -->
                <span v-else-if="item.type == 'obj'">{{ getObjAttr(scope.row, item.prop) }}</span>
                <!-- 其他 -->
                <template v-else-if="item.type == 'other'">
                  <el-button v-if="item.prop == 'handle'" type="text" @click="delivery(scope.row.orderId,scope.row.customerId)">{{scope.row[item.prop]}}</el-button>
                  <el-button v-if="item.prop == 'orderNo'" type="text" @click="toSalesOrder(scope.row)">{{scope.row[item.prop]}}</el-button>
                </template>
                <span v-else>{{scope.row[item.prop]}}</span>
                </template>
            </el-table-column>
            </template>
            </el-table-column>
        </el-table>
      </div>

      <el-dialog title="送货信息" :visible.sync="deliveryForm.open" width="1200px">
        <el-row><span class="lable">客户名称：</span><span class="customerName">{{deliveryForm.customerName}}</span></el-row>
        <el-row><span class="lable">送货地址：</span><span class="address">{{deliveryForm.address}}</span></el-row>
        <el-table :data="deliveryList">
          <el-table-column label="产品名称" property="materielName"></el-table-column>
          <el-table-column label="销售数量" property="detailsNum"></el-table-column>
          <el-table-column label="已送数量" property="fulfillNum"></el-table-column>
          <el-table-column label="未送数量" property="surplusNum"></el-table-column>
          <el-table-column label="本次送货" property="thisNum">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.thisNum" controls-position="right"></el-input-number>
            </template>
          </el-table-column>
        </el-table>

        <div slot="footer" class="dialog-footer">
          <el-button @click="deliveryForm.open = false">取 消</el-button>
          <el-button type="primary" @click="deliverySubmit">确 定</el-button>
        </div>
      </el-dialog>
  
      <customer-delivery :orderId ="currentRowOrderId" ref="customerDeliveryRef"></customer-delivery>

    </div>
  </template>
  
  <script>
    import {
      deliverySubmit,
      deliveryByOrder,
      deliveryRemind
    } from "@/api/order/customerDelivery";

    import {
      listMaterielAll
    } from "@/api/order/materiel";
  
    import customerDelivery from '@/views/order/customerDelivery/print';

    export default {
      name: "CustomerDelivery",
      dicts: ['customer_type'],
      components: {
        customerDelivery
      },
      data() {
        return {
          queryParams:{
            supplierName: null,
            supplierType: null,
            orderNo: null,
            status: [],
            address: null
          },
          materielList:[],
          deliveryRemindList:[],
          deliveryList:[],
          deliveryForm: {
            //弹出层是否显示
            open:false,
            //客户名
            customerName: "客户名称",
            //地址
            address: "地址三角龙骨i胡搜诶嘿工会叫哦离穗额合计过",
            //表格
            list:[],
          },

          //跨行ID记录
          mergeRowsOrderId:[],
          //选中的行
          currentRowOrderId: null,
          //表头销售数量
          sumNum: null,
          //表头时间显示
          timeStart: null,
          //表头时间显示
          timeEnd: null,
          //当前页的客户ID
          customerId: null,
          // 遮罩层
          loading: true,
          // 选中数组
          ids: [],
          // 非单个禁用
          single: true,
          // 非多个禁用
          multiple: true,
          // 显示搜索条件
          showSearch: true,
          // 总条数
          total: 0,
          // 弹出层标题
          title: "",
          // 是否显示弹出层
          open: false,
          // 查询参数
          queryParams: {
            pageNum: 1,
            pageSize: 10,
            customerId: null,
            orderId: null
          },
          // 表单参数
          form: {},
          // 表单校验
          rules: {},
          cacheCloumnList: [], //缓存字段列表
          //缓存名称
          cacheKey: "DeliveryRemind",
          columnList: [{
              label: "计划送货日期",
              prop: "deliveryTime",
              visible: true,
              align: "center",
              type:"date"
            },{
              label: "客户名称",
              prop: "customerName",
              visible: true,
              align: "center",
            },{
              label: "送货日期",
              prop: "updateTime",
              visible: true,
              align: "center",
              type:"date"
            },{
              label: "产品",
              prop: "materielName",
              visible: true,
              align: "center",
            },
            {
              label: "销售数量",
              prop: "detailsNum",
              visible: true,
              align: "center",
            },
            {
              label: "已送货数量",
              prop: "fulfillNum",
              visible: true,
              align: "center",
            },{
              label: "未送货数量",
              prop: "surplusNum",
              visible: true,
              align: "center",
            },{
              label: "备注",
              prop: "remark",
              visible: true,
              align: "center",
            },{
              label: "送货地址",
              prop: "address",
              visible: true,
              align: "center",
            },{
              label: "关联业务",
              prop: "orderNo",
              visible: true,
              align: "center",
              type: "other"
            },{
              label: "操作",
              prop: "handle",
              visible: true,
              align: "center",
              type: "other"
            },
          ]
        };
      },

      watch: {
        '$route': {
          handler: function(to,form){
            this.onLoad();
          },
          immediate: true
        }
      },

      created() {
        
      },
      methods: {

        onLoad(){
          this.getMaterielList();
          const customerId = this.$route.query.customerId;
          this.customerId =  (customerId==undefined||customerId==null||customerId=='')?null:customerId;
          this.getList();
          this.refreshCloumn(this);
        },

        rowClick(row,column,event){
          this.currentRowOrderId = row.orderId;
        },

        tableRowClassName({row, rowIndex}) {
          if (row.orderId === this.currentRowOrderId) {
            return 'row-selected';
          } else {
            return '';
          }
        },

        handlePrint(){
          if(!this.currentRowOrderId){
            this.$modal.msgError("请先选中一行！");
            return;
          }
          this.$refs.customerDeliveryRef.handleOpen(this.currentRowOrderId);
        },


        //查询所有产品
        getMaterielList(){
          listMaterielAll().then(res=>{
              this.materielList = res.data;
          })
        },

        deliverySubmit(){
          deliverySubmit(this.deliveryList).then(res=>{
            this.deliveryForm.open = false;
            this.$modal.msgSuccess("操作成功");
            this.getList();
          })
        },
        // 送货操作
        delivery(orderId,customerId){
          this.deliveryForm.customerName = "";
          this.deliveryForm.address = "";
          let data = {
            "orderId":orderId,
            "customerId": customerId
          }
          deliveryByOrder(data).then(res=>{
              this.deliveryList = res.data.list;
              this.deliveryForm.customerName = res.data.customerName
              this.deliveryForm.address = "公司地址-"+res.data.address+(res.data.remark == undefined || res.data.remark == null || res.data.remark == ''?'':"-"+res.data.remark)
              console.log(this.deliveryList);
          })
          this.deliveryForm.open = true;
        },

        // 跳转到销售单详情页
        toSalesOrder(row){
          this.$router.push({
          path: '/salesOrder/info/index/'+row.templateId,
          query: {
            orderId: row.orderId
          }
        });
        },

        /** 查询客户送货单列表 */
        getList() {
          // this.queryParams.customerId = this.customerId;
          this.loading = true;
          deliveryRemind(this.queryParams).then(response => {
            this.deliveryRemindList = response.data.list;
            this.sumNum = response.data.sumNum;
            this.timeStart = response.data.timeStart;
            this.timeEnd = response.data.timeEnd;
            this.total = response.total;
            this.loading = false;
          });
        },
        // 表单重置
        reset() {
          this.form = {
            deliveryId: null,
            customerId: null,
            orderId: null,
            orderNodeId: null,
            deliveryStatus: null,
            createTime: null,
            createBy: null,
            updateTime: null,
            updateBy: null
          };
          this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
          // this.queryParams.pageNum = 1;
          this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
          this.resetForm("queryForm");
          this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
          this.ids = selection.map(item => item.deliveryId)
          this.single = selection.length !== 1
          this.multiple = !selection.length
        },
        /** 导出按钮操作 */
        handleExport() {
          this.download('order/customerDelivery/exportDeliveryRemind', {
            ...this.queryParams
          }, `客户送货单——${new Date().getTime()}.xlsx`)
        },
        getSummaries(param) {
        const {
          columns,
          data
        } = param;
        const sums = [];
        const sumColumnLabels = ['销售数量','已送货数量','未送货数量']; //需要合计的列名称
        columns.forEach((column, index) => {
          console.log()
          if (index === 0) {
            sums[index] = '合计';
            return;
          } else if (sumColumnLabels.includes(column.label)) {
            const values = data.map(item => Number(item[column.property]));
            if (!values.every(value => isNaN(value))) {
              sums[index] = values.reduce((prev, curr) => {
                const value = Number(curr);
                if (!isNaN(value)) {
                  return prev + curr;
                } else {
                  return prev;
                }
              }, 0);
              sums[index] += '';
            } else {
              sums[index] = '';
            }
          }

        });
        return sums;
      },

            /**
       * 跨行
       */
       objectSpanMethod({
        row,
        column,
        rowIndex,
        columnIndex
      }) {
        // 需要合并的列
        const labelArr = ['计划送货日期','客户名称','关联业务', '操作'];
        // 判断当前列是否是需要合并的列
        if (labelArr.includes(column.label)) {

          // 判断该列是否合并过
          let key = row.orderId + "" + column.label;
          if (!this.mergeRowsOrderId.includes(key)) {
            this.mergeRowsOrderId.push(key);
            this.ids.push(rowIndex)
            const orderDetailsList = this.deliveryRemindList.filter(item => item.orderId == row.orderId);

            return {
              rowspan: orderDetailsList.length,
              colspan: 1
            };
          } else {
            if (!this.ids.includes(rowIndex)) {
              return {
                rowspan: 0,
                colspan: 0
              };
            } else {
              const orderDetailsList = this.deliveryRemindList.filter(item => item.orderId == row.orderId);
              return {
                rowspan: orderDetailsList.length,
                colspan: 1
              };
            }
          }
        }
        return {
          rowspan: 1,
          colspan: 1
        };

      },

      }
    };
  </script>
<style>
  .statement-table-title {
    display: flex;

    div {
      flex: 1;
    }

    span {
      font-size: 1.3em;
    }
  }

  .el-table .row-selected {
      background-color: #befdfd; /* 选中行的背景色 */
    }
</style>