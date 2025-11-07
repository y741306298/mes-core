<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">任务计划</div>
    </div>
    <el-table border ref="brtTable" :data="orderMaterielPlanList">
      <el-table-column label="产品名称" prop="materielId" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.materielVo == null ? scope.row.materielId : scope.row.materielVo.materielName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单数量" prop="orderNum" align="center" />
      <el-table-column label="计划数量" align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.planNum" placeholder="请输入计划数量" type="number"></el-input>
        </template>
      </el-table-column>
    </el-table>

    <div class="mt20 textr" v-if="userId && (checkRole(['admin']) || userId == $store.state.user.id) && nodeStatus == '1'">
      <el-button size="mini" type="primary" plain>取消</el-button>
      <el-button size="mini" type="primary" plain v-hasPermi="['order:orderMaterielPlan:edit']"
        @click="handleSubmit">保存</el-button>
    </div>

    <el-divider class="mt20"></el-divider>
  </div>
</template>

<script>
  import {
    listOrderMaterielPlan,
    listOrderMaterielPlanAll,
    getOrderMaterielPlan,
    delOrderMaterielPlan,
    addOrderMaterielPlan,
    updateOrderMaterielPlan
  } from "@/api/order/orderMaterielPlan";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderMaterielPlan",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus"],
    data() {
      return {
        // 物料数量计划表格数据
        orderMaterielPlanList: [],
        // 查询参数
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
        updateOrderMaterielPlan(this.orderMaterielPlanList).then(res => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.getList();
        })
      },
      /** 查询物料数量计划列表 */
      getList() {
        if (!this.orderId && !this.orderNodeId) {
          this.orderMaterielPlanList = [];
          return;
        }
        this.queryParams.orderId = this.orderId;
        this.queryParams.nodeId = this.nodeId;
        this.queryParams.orderNodeId = this.orderNodeId;
        listOrderMaterielPlanAll(this.queryParams).then(response => {
          this.orderMaterielPlanList = response.data;
        });
      },
    }
  };
</script>
