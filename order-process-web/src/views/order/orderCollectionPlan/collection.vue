<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">收款</div>
    </div>
    <el-table border ref="brtTable" :data="orderCollectionPlanList">
      <el-table-column label="订单金额" prop="planAmount" align="center" />
      <el-table-column label="已收款金额" prop="collectionAmount" align="center" />
      <el-table-column label="剩余收款金额" align="center">
        <template slot-scope="scope">
          {{scope.row.planAmount - scope.row.collectionAmount}}
        </template>
      </el-table-column>
      <el-table-column label="本次收款" align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.amount" placeholder="请输入本次收款金额" type="number"></el-input>
        </template>
      </el-table-column>
    </el-table>
    <div class="fr mt20" v-if="(checkRole(['admin']) || userId == $store.state.user.id || principal == $store.state.user.id) && ['1'].includes(nodeStatus)">
      <el-button size="mini" type="primary" plain @click="handleFill">一键填充</el-button>
      <!-- v-hasPermi="['order:orderCollectionRecord:add']" -->
      <el-button size="mini" type="primary" plain  @click="handleSubmit">确认添加</el-button>
    </div>
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

  import {
    addOrderCollectionRecord
  } from "@/api/order/orderCollectionRecord";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderCollectionPlanC",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus","principal"],
    data() {
      return {
        // 开票计划表格数据
        orderCollectionPlanList: [],
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
        } = this.orderCollectionPlanList[0];

        let data = {
          orderId: orderId,
          collectionAmount: amount,
          nodeId: this.nodeId,
          orderNodeId: this.orderNodeId
        };

        addOrderCollectionRecord(data).then(res => {
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
          collectionAmount
        } = this.orderCollectionPlanList[0];
        this.$set(this.orderCollectionPlanList[0], 'amount', (planAmount - collectionAmount))
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
