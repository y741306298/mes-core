<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户名称" prop="customerId">
        <el-select v-model="queryParams.customerId" filterable clearable>
          <el-option v-for="(item,index) in customerList" :value="item.customerId"
            :label="item.customerName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable>
          <el-option v-for="dict in dict.type.audit_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :disabled="multiple" @click="handleAudit"
          v-hasPermi="['order:orderExamine:audit']">审批</el-button>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="orderExamineList"
      @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column type="selection" width="55" align="center" />
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
              <el-button type="text" size="mini" @click="toOrderDetails(scope.row)">{{scope.row.orderNo}}</el-button>
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改订单审批对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核状态" prop="auditStatus">
              <el-select v-model="form.auditStatus" class="drag-screenful-contnet" placeholder="请选择审核状态">
                <el-option v-for="dict in dict.type.audit_status" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核备注" prop="auditRemark">
              <el-input v-model="form.auditRemark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listOrderExamine,
    listOrderExamineAll,
    getOrderExamine,
    delOrderExamine,
    addOrderExamine,
    updateOrderExamine,
    audit
  } from "@/api/order/orderExamine";

  import {
    listCustomerAll
  } from "@/api/order/customer";

  import {
    getSalesOrder
  } from "@/api/order/salesOrder";

  import {
    getMarketOrder
  } from "@/api/order/marketOrder";

  import {
    getPriceSheetOrder
  } from "@/api/order/priceSheetOrder";

  export default {
    name: "OrderExamine",
    dicts: ['audit_status'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 订单审批表格数据
        orderExamineList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          orderId: null,
          orderNo: null,
          orderType: null,
          customerId: null,
          orderNum: null,
          orderAmount: null,
          orderDate: null,
          orderRemark: null,
          auditStatus: null,
          auditUserId: null,
          auditRemark: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "OrderExamine",
        columnList: [{
            label: "编号",
            prop: "orderNo",
            visible: true,
            align: "center",
            type: "other"
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
            prop: "orderNum",
            visible: true,
            align: "center",
          },
          {
            label: "总价",
            prop: "orderAmount",
            visible: true,
            align: "center",
          },
          {
            label: "日期",
            prop: "orderDate",
            visible: true,
            align: "center",
            type: "date",
            dateFormat: "{y}-{m}-{d}"
          },
          {
            label: "订单备注",
            prop: "orderRemark",
            visible: true,
            align: "center",
          },
          {
            label: "审核状态",
            prop: "auditStatus",
            visible: true,
            align: "center",
            type: "dict",
            dictType: "audit_status"
          },
          {
            label: "审核备注",
            prop: "auditRemark",
            visible: true,
            align: "center",
          },
        ],
        // 客户列表
        customerList: []
      };
    },
    created() {
      this.getList();
      this.refreshCloumn(this);
      this.getCustomerList();
    },
    methods: {
      /**
       * 跳转页面
       */
      async toOrderDetails(row){
        let href = "";
        switch(row.orderType){
          case '0' : // 报价单
          await getPriceSheetOrder(row.orderId).then(res => {
            href = "/priceSheetOrder/info/index/"+res.data.nowOrderNodeVo.orderTemplateId;
          })
            break;

          case '1' : // 销售单
          await getSalesOrder(row.orderId).then(res => {
            href = "/salesOrder/info/index/"+res.data.nowOrderNodeVo.orderTemplateId;
          })
          break;

          case '2' : // 采购单
          await getMarketOrder(row.orderId).then(res => {
            href = "/marketOrder/info/index/"+res.data.nowOrderNodeVo.orderTemplateId;
          })
          break;

        }
        this.$router.push({
          path: href,
          query: {
            orderId: row.orderId
          }
        });
      },
      /**
       * 查询客户列表
       */
      getCustomerList() {
        listCustomerAll().then(res => {
          this.customerList = res.data;
        })
      },
      /** 查询订单审批列表 */
      getList() {
        this.loading = true;
        listOrderExamine(this.queryParams).then(response => {
          this.orderExamineList = response.rows;
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
          examineIds: this.ids.join(','),
          auditStatus: null,
          auditUserId: null,
          auditRemark: null
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
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.examineId)
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAudit() {
        this.reset();
        this.open = true;
        this.title = "订单审批";
      },
      /** 修改按钮操作 */
      handleDetails(row) {

      },
      /** 提交按钮 */
      submitForm() {
        audit(this.form).then(res => {
          this.$notify({
            title: '成功',
            message: '审批成功',
            type: 'success'
          });
          this.getList();
          this.open = false;
        })
      }
    }
  };
</script>
