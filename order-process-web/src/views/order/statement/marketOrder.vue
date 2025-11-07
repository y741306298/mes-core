<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="日期" prop="dateType">
        <el-select v-model="queryParams.dateType" filterable clearable>
          <el-option value="0" label="当日"></el-option>
          <el-option value="1" label="昨日"></el-option>
          <el-option value="2" label="当月"></el-option>
          <el-option value="3" label="本季度"></el-option>
          <el-option value="4" label="本年"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="">
        <el-date-picker v-model="queryParams.createTimes" value-format="yyyy-MM-dd" type="daterange" range-separator="至"
          start-placeholder="开始日期" end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="供应商名称" prop="supplierId">
        <el-select v-model="queryParams.supplierId" placeholder="请输入供应商名称" clearable filterable>
          <el-option v-for="(item, index) in supplierList" :value="item.supplierId"
            :label="item.supplierName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="供应商类型" prop="supplierTypeId">
        <el-select v-model="queryParams.supplierTypeId" placeholder="请输入供应商类型" clearable filterable>
          <el-option v-for="(item, index) in dict.type.supplier_type" :value="item.value"
            :label="item.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="产品名称" prop="materielId">
        <el-select v-model="queryParams.materielId" placeholder="请输入产品名称" clearable filterable>
          <el-option v-for="(item, index) in materielList" :value="item.materielId"
            :label="item.materielName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="业务员" prop="userId">
        <el-select v-model="queryParams.userId" clearable filterable class="drag-screenful-contnet">
          <el-option v-for="(item, index) in salesmanUserList" :value="item.userId" :label="item.userName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" @click="hanldeOpenPrint" size="mini">打 印</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['order:marketOrder:export']">导出</el-button>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="marketOrderList" :summary-method="getSummaries"
      :span-method="objectSpanMethod">
      <el-table-column label="销售流水表" align="center">
        <template slot="header" slot-scope="scope">
          <div>
            <h1>销售流水表</h1>
            <div class="statement-table-title">
              <div class="textl">销售总额:¥<span> {{ otherData.totalAmount }}</span></div>
              <div>
                {{ parseTime(queryParams.createTimeStart, '{y}.{m}.{d}') }}-{{ parseTime(queryParams.createTimeEnd, '{y}.{m}.{d}') }}
              </div>
              <div class="textr">单位:元</div>
            </div>
          </div>
        </template>
        <!-- <el-table-column label="销售日期" prop="marketOrderVo.orderTime"  align="center"/> -->
        <!-- 循环字段 -->
        <template v-for="(item, index) in cacheCloumnList">

          <el-table-column v-if="item.visible" :label="item.label" :prop="item.prop" :width="item.width || ''"
            :align="item.align || 'center'">
            <template slot-scope="scope">
              <!-- 字典 -->
              <dict-tag v-if="item.type == 'dict'" :options="dict.type[item.dictType]" :value="scope.row[item.prop]" />
              <dict-tag v-else-if="item.type == 'objDict'" :options="dict.type[item.dictType]"
                :value="getObjAttr(scope.row, item.prop)" />
              <!-- 日期 -->
              <span v-else-if="item.type == 'date'">{{ parseTime(scope.row[item.prop], item.dateFormat) }}</span>
              <span v-else-if="item.type == 'objDate'">{{ parseTime(getObjAttr(scope.row, item.prop), item.dateFormat)
                }}</span>
              <!-- 图片 -->
              <image-preview v-else-if="item.type == 'img'" :src="scope.row[item.prop]" :width="50" :height="50" />
              <!-- 多层对象 -->
              <span v-else-if="item.type == 'obj'">{{ getObjAttr(scope.row, item.prop) }}</span>
              <!-- 其他 -->
              <template v-else-if="item.type == 'other'">
                <span v-if="item.prop == 'materielName'">
                  {{ scope.row.materielVo ? scope.row.materielVo.materielName : scope.row.materielId }}
                </span>
                <el-button v-if="item.prop == 'orderNo'" type="text"
                  @click="toOrderDetails(scope.row)">{{ scope.row.marketOrderVo.orderNo }}</el-button>
              </template>
              <span v-else>{{ scope.row[item.prop] }}</span>
            </template>
          </el-table-column>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 打开弹框 -->
    <market-order-print :queryParams="queryParams" ref="marketOrderPrintRef"></market-order-print>

  </div>
</template>

<script>
import {
  statementList
} from "@/api/order/marketOrderDetails";

import {
  listSupplierAll
} from "@/api/order/supplier";

import {
  listAllUser
} from "@/api/system/user";

// import {
//   listSupplierTypeAll
// } from "@/api/order/supplierType";

import {
  listMaterielAll
} from "@/api/order/materiel";

import marketOrderPrint from "@/views/order/statement/printMarketOrder";

export default {
  name: "statementMarketOrder",
  components: {
    marketOrderPrint
  },
  dicts: ['supplier_type'],
  data() {
    return {
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
      // 销售单表格数据
      marketOrderList: [],
      otherData: {
        totalAmount: 0
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        supplierId: null,
        contact: null,
        contactTel: null,
        supplierAddressId: null,
        orderTime: null,
        deliveryTime: null,
        templateId: null,
        craftType: null,
        totalNum: null,
        totalAmount: null,
        currencyType: null,
        attachments: null,
        userId: null,
        dateType: null,
        createTimes: [],
        createTimeStart: null,
        createTimeEnd: null,
        supplierTypeId: null,
        materielId: null
      },
      cacheCloumnList: [], //缓存字段列表
      //缓存名称
      cacheKey: "StatementMarketOrder",
      columnList: [{
        label: "销售日期",
        prop: "marketOrderVo.orderTime",
        visible: true,
        align: "center",
        type: "objDate",
        dateFormat: "{y}-{m}-{d}"
      },
      {
        label: "供应商名称",
        prop: "supplierName",
        visible: true,
        align: "center",
      },
      {
        label: "产品",
        prop: "materielName",
        visible: true,
        align: "center",
        type: "other"
      },
      {
        label: "总数量",
        prop: "marketOrderVo.totalNum",
        visible: true,
        align: "center",
        type: "obj"
      },
      // {
      //   label: "售价",
      //   prop: "detailsPrice",
      //   visible: true,
      //   align: "center",
      // },
      {
        label: "总额",
        prop: "marketOrderVo.totalAmount",
        visible: true,
        align: "center",
        type: "obj"
      },
      {
        label: "业务员",
        prop: "user.userName",
        visible: true,
        align: "center",
        type: "obj"
      },
      // {
      //   label: "送货日期",
      //   prop: "deliveryTime",
      //   visible: true,
      //   align: "center",
      //   type: "date",
      //   dateFormat: "{y}-{m}-{d}"
      // },
      // {
      //   label: "送货数量",
      //   prop: "totalNum",
      //   visible: true,
      //   align: "center",
      // },
      // {
      //   label: "送货金额",
      //   prop: "totalNum",
      //   visible: true,
      //   align: "center",
      // },
      {
        label: "备注",
        prop: "detailsRemark",
        visible: true,
        align: "center",
      },
      {
        label: "关联业务",
        prop: "orderNo",
        visible: true,
        align: "center",
        type: "other",
        width: 150
      }
      ],
      //已合并的订单ID
      mergeRowsOrderId: [],
      // 供应商列表
      supplierList: [],
      // 产品列表
      materielList: [],
      // 供应商类型列表
      supplierTypeList: [],
      // 业务员列表
      salesmanUserList: [],

    };
  },
  created() {
    this.getList();
    this.refreshCloumn(this);
    this.getSalesmanUserList();
    this.getSupplierList();
    this.getMaterielList();
  },
  methods: {
    /**
     * 打开弹框
     */
    hanldeOpenPrint() {
      this.$refs.marketOrderPrintRef.handleOpen()
    },
    // 合计
    getSummaries(param) {
      const {
        columns,
        data
      } = param;
      const sums = [];
      const sumColumnLabels = ['总数量', '总额']; //需要合计的列名称
      columns.forEach((column, index) => {
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
     * 获取物料列表
     */
    getMaterielList() {
      listMaterielAll().then(res => {
        this.materielList = res.data;
      })
    },
    /**
     * 获取业务员列表
     */
    getSalesmanUserList() {
      listAllUser({
        roleId: 2
      }).then(res => {
        this.salesmanUserList = res.data
      })
    },
    /**
     * 获取供应商列表
     */
    getSupplierList() {
      listSupplierAll().then(res => {
        this.supplierList = res.data;
      })
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
      const labelArr = ['销售日期', '供应商名称', '总数量', '总额', '业务员', '关联业务'];
      // 判断当前列是否是需要合并的列
      if (labelArr.includes(column.label)) {

        // 判断该列是否合并过
        let key = row.orderId + "" + column.label;
        if (!this.mergeRowsOrderId.includes(key)) {
          this.mergeRowsOrderId.push(key);
          this.ids.push(rowIndex)
          const orderDetailsList = this.marketOrderList.filter(item => item.orderId == row.orderId);

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
            const orderDetailsList = this.marketOrderList.filter(item => item.orderId == row.orderId);
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
    /**
     * 跳转订单详情
     */
    toOrderDetails(row) {
      this.$router.push({
        path: '/marketOrder/info/index/' + row.marketOrderVo.templateId,
        query: {
          orderId: row.marketOrderVo.orderId
        }
      });
    },
    /** 查询销售单列表 */
    getList() {
      this.loading = true;
      if (this.queryParams.createTimes != null && this.queryParams.createTimes.length > 0) {
        this.queryParams.createTimeStart = this.queryParams.createTimes[0]
        this.queryParams.createTimeEnd = this.queryParams.createTimes[1]
      } else {
        this.queryParams.createTimeStart = null
        this.queryParams.createTimeEnd = null
      }
      statementList(this.queryParams).then(response => {
        this.marketOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.otherData = response.otherData;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.createTimes = []
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/marketOrderDetails/export', {
        ...this.queryParams
      }, `marketOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style lang="scss">
.statement-table-title {
  display: flex;

  div {
    flex: 1;
  }

  span {
    font-size: 1.3em;
  }
}
</style>