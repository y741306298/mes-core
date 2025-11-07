<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">开票</div>
    </div>
    <el-table border ref="brtTable" :data="orderInvoicePlanList">
      <el-table-column label="订单金额" prop="planAmount" align="center" />
      <el-table-column label="已开票金额" prop="invoiceAmount" align="center" />
      <el-table-column label="剩余开票金额" align="center">
        <template slot-scope="scope">
          {{scope.row.planAmount - scope.row.invoiceAmount}}
        </template>
      </el-table-column>
      <el-table-column label="本次开票" align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.amount" placeholder="请输入本次开票金额" type="number"></el-input>
        </template>
      </el-table-column>
    </el-table>
    <div class="fr mt20" v-if="(checkRole(['admin']) || userId == $store.state.user.id || principal == $store.state.user.id) && ['1'].includes(nodeStatus)">
      <el-button size="mini" type="primary" plain @click="handleFill">一键填充</el-button>
      <el-button size="mini" type="primary" plain @click="handleSubmit">确认添加</el-button>
    </div>
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

  import {
    addOrderInvoiceRecord,
  } from "@/api/order/orderInvoiceRecord";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderInvoicePlan",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus","principal"],
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
       * 确定添加
       */
      handleSubmit() {
        const {
          orderId,
          amount
        } = this.orderInvoicePlanList[0];

        let data = {
          orderId: orderId,
          invoiceAmount: amount,
          nodeId: this.nodeId,
          orderNodeId: this.orderNodeId
        };

        addOrderInvoiceRecord(data).then(res => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.getList();
          this.$emit("handleLater");
        })
      },
      /**
       * 一键填充
       */
      handleFill() {
        const {
          planAmount,
          invoiceAmount
        } = this.orderInvoicePlanList[0];
        this.$set(this.orderInvoicePlanList[0], 'amount', (planAmount - invoiceAmount))
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
