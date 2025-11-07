<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">任务计划</div>
    </div>
    <el-table border ref="brtTable" :data="orderInvoicePlanList">
      <el-table-column label="订单编号" prop="orderNo" align="center" />
      <el-table-column label="订单金额" prop="orderAmount" align="center" />
      <el-table-column label="计划金额" align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.planAmount" placeholder="请输入计划金额" type="number"></el-input>
        </template>
      </el-table-column>
    </el-table>

    <div class="mt20 textr" v-if="userId && (checkRole(['admin']) || userId == $store.state.user.id) && nodeStatus == '1'">
      <el-button size="mini" type="primary" plain>取消</el-button>
      <el-button size="mini" type="primary" plain v-hasPermi="['order:orderInvoicePlan:edit']"
        @click="handleSubmit">保存</el-button>
    </div>

    <el-divider class="mt20"></el-divider>

  </div>
</template>

<script>
  import {
    listOrderInvoicePlan,
    listOrderInvoicePlanAll,
    getOrderInvoicePlan,
    delOrderInvoicePlan,
    addOrderInvoicePlan,
    updateOrderInvoicePlan
  } from "@/api/order/orderInvoicePlan";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderInvoicePlan",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus"],
    data() {
      return {
        // 开票计划表格数据
        orderInvoicePlanList: [],
        // 查询条件
        queryParams: {
          orderId: null,
          nodeId: null,
          orderNodeId: null
        }
      };
    },
    watch: {
      orderNodeId: function() {
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      checkRole,
      /**
       * 提交操作
       */
      handleSubmit() {
        updateOrderInvoicePlan(this.orderInvoicePlanList[0]).then(res => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.getList();
        })
      },
      /** 查询开票计划列表 */
      getList() {
        if (!this.orderId && !this.orderNodeId) {
          this.orderInvoicePlanList = [];
          return;
        }
        this.queryParams.orderId = this.orderId;
        this.queryParams.nodeId = this.nodeId;
        this.queryParams.orderNodeId = this.orderNodeId;
        listOrderInvoicePlanAll(this.queryParams).then(response => {
          this.orderInvoicePlanList = response.data;
        });
      }
    }
  };
</script>
