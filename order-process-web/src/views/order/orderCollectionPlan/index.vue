<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">任务计划</div>
    </div>
    <el-table border ref="brtTable" :data="orderCollectionPlanList">
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
    listOrderCollectionPlan,
    listOrderCollectionPlanAll,
    getOrderCollectionPlan,
    delOrderCollectionPlan,
    addOrderCollectionPlan,
    updateOrderCollectionPlan
  } from "@/api/order/orderCollectionPlan";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderCollectionPlan",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus"],
    data() {
      return {
        // 收款计划表格数据
        orderCollectionPlanList: [],
        // 查询参数
        queryParams: {
          orderId: null,
          nodeId: null,
          orderNodeId: null
        },
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
        updateOrderCollectionPlan(this.orderCollectionPlanList[0]).then(res => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.getList();
        })
      },
      /** 查询收款计划列表 */
      getList() {
        if (!this.orderId && !this.orderNodeId) {
          this.orderCollectionPlanList = [];
          return;
        }
        this.queryParams.orderId = this.orderId;
        this.queryParams.nodeId = this.nodeId;
        this.queryParams.orderNodeId = this.orderNodeId;
        listOrderCollectionPlanAll(this.queryParams).then(response => {
          this.orderCollectionPlanList = response.data;
        });
      }
    }
  };
</script>
