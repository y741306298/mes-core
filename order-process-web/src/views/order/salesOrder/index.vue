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
      <el-form-item label="来源单号" prop="orderSn">
        <el-input v-model="queryParams.orderSn" placeholder="请输入来源单号" clearable @keyup.enter.native="handleQuery" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['order:salesOrder:add']">新 增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="isEdit" @click="handleUpdate"
          v-hasPermi="['order:salesOrder:edit']">编 辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
          批量操作
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="copy" v-hasPermi="['order:salesOrder:copy']">复制订单</el-dropdown-item>
            <el-dropdown-item command="download">下载模板</el-dropdown-item>
            <el-dropdown-item command="improt" v-hasPermi="['order:salesOrder:improt']">导入销售单</el-dropdown-item>
            <el-dropdown-item command="export" v-hasPermi="['order:salesOrder:export']">导出销售单</el-dropdown-item>
            <el-dropdown-item command="print" :disabled="single"
              v-hasPermi="['order:salesOrder:print']">打印</el-dropdown-item>
            <el-dropdown-item command="delete" :disabled="multiple"
              v-hasPermi="['order:salesOrder:remove']">删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="salesOrderList"
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
              <el-button v-if="item.prop == 'orderNo'" type="text" @click="toOrderDetails(scope.row)">{{
      scope.row.orderNo }}</el-button>
            </template>
            <span v-else>{{ getObjAttr(scope.row, item.prop) }}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 打印 -->
    <print ref="printRef"></print>

    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url"
        :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess"
        :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
            @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listSalesOrder,
  listSalesOrderAll,
  getSalesOrder,
  delSalesOrder,
  addSalesOrder,
  updateSalesOrder,
  copySalesOrder
} from "@/api/order/salesOrder";

import {
  getToken
} from "@/utils/auth";

import {
  listCustomerAll
} from "@/api/order/customer";

import {
  listAllUser
} from "@/api/system/user";

import print from "@/views/order/salesOrder/print"

export default {
  name: "SalesOrder",
  components: {
    print
  },
  dicts: ['sales_order_status', 'yes_no'],
  data() {
    return {
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {
          Authorization: "Bearer " + getToken()
        },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/order/salesOrder/importData"
      },
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      //编辑
      isEdit: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 销售单表格数据
      salesOrderList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        orderSn: null,
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
      cacheKey: "SalesOrder",
      columnList: [{
        label: "订单编号",
        prop: "orderNo",
        visible: true,
        align: "center",
        type: "other",
        width: 150
      },
      {
        label: "来源单号",
        prop: "orderSn",
        visible: true,
        align: "center",
        width: 180
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
        prop: "postpone",
        visible: true,
        align: "center",
        type: "dict",
        dictType: "yes_no"
      },
      {
        label: "订单状态",
        prop: "orderStatus",
        visible: true,
        align: "center",
        type: "dict",
        dictType: "sales_order_status"
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
      salesmanUserList: [],
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
      this.getSalesmanUserList();
    },
    /**
     * 复制订单
     */
    handleCopy() {
      const data = {
        orderId: this.ids[0]
      };
      copySalesOrder(data).then(res => {
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
      this.download('order/salesOrder/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
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
        case 'improt':
          this.handleImport();
          break;
      }
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "导入";
      this.upload.open = true;
    },

    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response
        .msg + "</div>", "导入结果", {
        dangerouslyUseHTMLString: true
      });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
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
        path: '/salesOrder/info/index/' + row.templateId,
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
      listSalesOrder(this.queryParams).then(response => {
        this.salesOrderList = response.rows;
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
      let statusList = selection.map(item => item.status);
      this.single = selection.length !== 1;
      this.isEdit = selection.length !== 1 || statusList.includes("5");
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({
        path: '/salesOrder/add/index',
      });
    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.$router.push({
        path: '/salesOrder/add/index',
        query: {
          orderId: this.ids[0]
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = this.ids;
      this.$modal.confirm('确认删除？').then(function () {
        return delSalesOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/salesOrder/export', {
        ...this.queryParams,
        ids: this.ids
      }, `salesOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
