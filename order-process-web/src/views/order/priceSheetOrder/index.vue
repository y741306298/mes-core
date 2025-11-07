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
      <el-form-item label="客户名称" prop="customerId">
        <el-select v-model="queryParams.customerId" placeholder="请输入客户名称" clearable filterable>
          <el-option v-for="(item, index) in customerList" :value="item.customerId"
            :label="item.customerName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="业务员" prop="userId">
        <el-select v-model="queryParams.userId" clearable filterable class="drag-screenful-contnet">
          <el-option v-for="(item, index) in pricesheetmanUserList" :value="item.userId"
            :label="item.userName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['order:priceSheetOrder:add']">新 增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['order:priceSheetOrder:edit']">编 辑</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
          批量操作
          <el-dropdown-menu slot="dropdown">
            <!-- <el-dropdown-item command="copy" v-hasPermi="['order:priceSheetOrder:copy']">复制订单</el-dropdown-item>
            <el-dropdown-item command="download">下载模板</el-dropdown-item>
            <el-dropdown-item command="export" v-hasPermi="['order:priceSheetOrder:export']">导出销售单</el-dropdown-item> -->
            <el-dropdown-item command="print" :disabled="single"
              v-hasPermi="['order:priceSheetOrder:print']">打印</el-dropdown-item>
            <el-dropdown-item command="edit" :disabled="single"
              v-hasPermi="['order:priceSheetOrder:edit']">编辑</el-dropdown-item>
            <el-dropdown-item command="delete" :disabled="multiple"
              v-hasPermi="['order:priceSheetOrder:remove']">删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="priceSheetOrderList"
      @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column type="selection" width="55" align="center" />
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
            <!-- 图片 -->
            <image-preview v-else-if="item.type == 'img'" :src="scope.row[item.prop]" :width="50" :height="50" />
            <!-- 多层对象 -->
            <span v-else-if="item.type == 'obj'">{{ getObjAttr(scope.row, item.prop) }}</span>
            <!-- 其他 -->
            <template v-else-if="item.type == 'other'">
              <el-button v-if="item.prop == 'orderNo'" type="text"
                @click="toOrderDetails(scope.row)">{{ scope.row.orderNo }}</el-button>
            </template>
            <span v-else>{{ scope.row[item.prop] }}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 打印 -->
    <print ref="printRef"></print>

  </div>
</template>

<script>
import {
  listPriceSheetOrder,
  listPriceSheetOrderAll,
  getPriceSheetOrder,
  delPriceSheetOrder,
  addPriceSheetOrder,
  updatePriceSheetOrder,
  copyPriceSheetOrder
} from "@/api/order/priceSheetOrder";

import {
  listCustomerAll
} from "@/api/order/customer";

import {
  listAllUser
} from "@/api/system/user";

import print from "@/views/order/priceSheetOrder/print"

export default {
  name: "PriceSheetOrder",
  components: {
    print
  },
  // dicts: ['sales_order_status'],
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
      priceSheetOrderList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        customerId: null,
        contact: null,
        contactTel: null,
        customerAddressId: null,
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
        createTimeEnd: null
      },
      cacheCloumnList: [], //缓存字段列表
      //缓存名称
      cacheKey: "PriceSheetOrder",
      columnList: [{
        label: "订单编号",
        prop: "orderNo",
        visible: true,
        align: "center",
        type: "other",
        width: 150
      },
      {
        label: "客户名称",
        prop: "customerVo.customerName",
        visible: true,
        align: "center",
        type: "obj"
      },
      {
        label: "数量",
        prop: "totalNum",
        visible: true,
        align: "center",
      },
      {
        label: "总价",
        prop: "totalAmount",
        visible: true,
        align: "center",
      },
      {
        label: "下单日期",
        prop: "orderTime",
        visible: true,
        align: "center",
        type: "date",
        dateFormat: "{y}-{m}-{d}"
      },
      {
        label: "交货日期",
        prop: "deliveryTime",
        visible: true,
        align: "center",
        type: "date",
        dateFormat: "{y}-{m}-{d}"
      },
      {
        label: "当前节点",
        prop: "nowOrderNodeVo.flowNodeVo.nodeName",
        visible: true,
        align: "center",
        type: "obj"
      },
      {
        label: "执行人",
        prop: "user.nickName",
        visible: true,
        align: "center",
        type: "obj"
      },
      {
        label: "是否延期",
        prop: "",
        visible: true,
        align: "center",
      },
      {
        label: "订单状态",
        prop: "orderStatus",
        visible: true,
        align: "center",
        // type: "dict",
        // dictType: "sales_order_status"
      },
      {
        label: "备注",
        prop: "orderRemark",
        visible: true,
        align: "center",
      },
      ],
      // 客户列表
      customerList: [],
      // 业务员列表
      pricesheetmanUserList: [],
    };
  },

  watch: {
    '$route': {
      handler: function (to, form) {
        this.onLoad();
      },
      immediate: true
    }
  },

  created() {

  },
  methods: {

    onLoad() {
      this.getList();
      this.refreshCloumn(this);
      this.getCustomerList();
      this.getPriceSheetmanUserList();
    },

    /**
     * 复制订单
     */
    handleCopy() {
      const data = {
        orderId: this.ids[0]
      };
      copyPriceSheetOrder(data).then(res => {
        this.$notify({
          title: '成功',
          message: '复制成功',
          type: 'success'
        });
        this.getList();
      })
    },
    // 打印
    handlePrint() {
      this.$refs.printRef.handleOpen(this.ids[0]);
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('order/priceSheetOrder/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
    },
    /**
     * 批量操作按钮点击事件
     */
    handleDropdownClick(command) {
      switch (command) {
        case 'copy':
          this.handleCopy();
          break;
        case 'download':
          this.importTemplate();
          break;
        case 'export':
          this.handleExport();
          break;
        case 'delete':
          this.handleDelete();
          break;
        case 'print':
          this.handlePrint();
          break;
        case 'edit':
          this.handleEdit();
          break;
      }
    },
    handleEdit() {
      this.$router.push({
        path: '/priceSheetOrder/add/index',
        query: {
          orderId: this.ids[0]
        }
      });
    },
    /**
     * 获取业务员列表
     */
    getPriceSheetmanUserList() {
      listAllUser({
        roleId: 2
      }).then(res => {
        this.pricesheetmanUserList = res.data
      })
    },
    /**
     * 获取客户列表
     */
    getCustomerList() {
      listCustomerAll().then(res => {
        this.customerList = res.data;
      })
    },
    /**
     * 跳转订单详情
     */
    toOrderDetails(row) {
      this.$router.push({
        path: '/priceSheetOrder/info/index/' + row.templateId,
        query: {
          orderId: row.orderId
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
      listPriceSheetOrder(this.queryParams).then(response => {
        this.priceSheetOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({
        path: '/priceSheetOrder/add/index',
      });
    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.$router.push({
        path: '/priceSheetOrder/add/index',
        query: {
          orderId: this.ids[0]
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = this.ids;
      this.$modal.confirm('确认删除？').then(function () {
        return delPriceSheetOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/priceSheetOrder/export', {
        ...this.queryParams
      }, `priceSheetOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
