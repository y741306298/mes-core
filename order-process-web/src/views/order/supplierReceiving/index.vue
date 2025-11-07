<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单号" prop="orderId">
        <el-input v-model="queryParams.orderId" placeholder="请输入订单ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="收款状态" prop="collectionStatus">
        <el-select v-model="queryParams.collectionStatus" placeholder="请选择收款状态" clearable>
          <el-option v-for="dict in dict.type.collection_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="收货状态" prop="receivingStatus">
        <el-select v-model="queryParams.receivingStatus" placeholder="请选择收货状态" clearable>
          <el-option v-for="dict in dict.type.receiving_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间" prop="dateType">
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增销售单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-truck" size="mini" @click="handleReceiving">收货</el-button>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="supplierReceivingList"
      @selection-change="handleSelectionChange">
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
            </template>
            <span v-else>{{ scope.row[item.prop] }}</span>
          </template>
        </el-table-column>
      </template>

    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />


  </div>
</template>

<script>
import {
  getSupplierReceiving,
  delSupplierReceiving,
  addSupplierReceiving,
  updateSupplierReceiving
} from "@/api/order/supplierReceiving";

import {
  queryPurchaseRecord
} from "@/api/order/supplier";

export default {
  name: "SupplierReceiving",
  dicts: ['receiving_status', 'collection_status'],
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
      // 供应商收货单表格数据
      supplierReceivingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        supplierId: null,
        orderId: null,
        orderNodeId: null,
        collectionStatus: null,
        receivingStatus: null,
        dateType: null,
        createTimes: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      cacheCloumnList: [], //缓存字段列表
      //缓存名称
      cacheKey: "SupplierReceiving",
      columnList: [{
        label: "单号",
        prop: "orderNo",
        visible: true,
        align: "center",
      },
      {
        label: "日期",
        prop: "orderTime",
        visible: true,
        align: "center",
      },
      {
        label: "合同金额",
        prop: "totalAmount",
        visible: true,
        align: "center",
      },
      {
        label: "已收货",
        prop: "receivingNum",
        visible: true,
        align: "center",
      },
      {
        label: "未收款",
        prop: "unCollectionAmount",
        visible: true,
        align: "center",
      },
      {
        label: "已收款",
        prop: "collectionAmount",
        visible: true,
        align: "center",
      },
      {
        label: "收款状态",
        prop: "priceStatus",
        visible: true,
        align: "center",
        type: "dict",
        dictType: "collection_status"
      },
      {
        label: "收货状态",
        prop: "receivingStatus",
        visible: true,
        align: "center",
        type: "dict",
        dictType: "receiving_status"
      },
      ]
    };
  },
  created() {
    // this.getList();
    this.refreshCloumn(this);
  },
  methods: {
    setSupplierId(supplierId) {
      this.supplierId = supplierId
    },
    /** 查询供应商收货单列表 */
    getList() {
      let queryData = {
        supplierId: this.supplierId,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }
      this.loading = true;
      queryPurchaseRecord(queryData).then(response => {
        this.supplierReceivingList = response.rows;
        console.log(this.supplierReceivingList);
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        receivingId: null,
        supplierId: null,
        orderId: null,
        orderNodeId: null,
        receivingStatus: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      };
      this.resetForm("form");
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
      this.ids = selection.map(item => item.receivingId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({
        path: '/marketOrder/add/index',
        query: {
          supplierId: this.supplierId
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const receivingId = row.receivingId || this.ids
      getSupplierReceiving(receivingId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改供应商收货单";
      });
    },
    handleReceiving() {
      this.$router.push({
        path: '/supplierReceiving/receivingRemind/index',
        query: {
          supplierId: this.supplierId
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.receivingId != null) {
            updateSupplierReceiving(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSupplierReceiving(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const receivingIds = row.receivingId || this.ids;
      this.$modal.confirm('确认删除？').then(function () {
        return delSupplierReceiving(receivingIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/supplierReceiving/export', {
        ...this.queryParams
      }, `supplierReceiving_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
