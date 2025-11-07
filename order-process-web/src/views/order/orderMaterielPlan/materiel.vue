<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">发货</div>
    </div>
    <el-table border ref="brtTable" :data="orderMaterielPlanList">
      <el-table-column label="项目编号" prop="orderDetailsNo" align="center" />
      <el-table-column label="产品名称" prop="materielId" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.materielVo == null ? scope.row.materielId : scope.row.materielVo.materielName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" prop="materielType" align="center">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.materiel_type" :value="scope.row.materielType"></dict-tag>
        </template>
      </el-table-column>
      <el-table-column label="计划数量" prop="planNum" align="center" />
      <el-table-column label="已完成" prop="materielNum" align="center" />
      <el-table-column label="剩余数量" align="center">
        <template slot-scope="scope">
          {{scope.row.planNum - scope.row.materielNum}}
        </template>
      </el-table-column>
      <el-table-column label="当前进度" align="center">
        <template slot-scope="scope">
          <el-progress :text-inside="true" :stroke-width="15" :percentage="computeSchedule(scope.row)"></el-progress>
        </template>
      </el-table-column>
      <el-table-column label="本次添加" align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.num" placeholder="请输入本次数量" type="number"></el-input>
        </template>
      </el-table-column>
    </el-table>
    <div class="fr mt20" v-if="(checkRole(['admin']) || userId == $store.state.user.id || principal == $store.state.user.id) && ['1'].includes(nodeStatus)">
      <el-button size="mini" type="primary" plain @click="handleFill">一键填充</el-button>
      <!-- v-hasPermi="['order:orderCollectionRecord:add']" -->
      <el-button size="mini" type="primary" plain

        @click="handleSubmit">确认添加</el-button>
    </div>
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

  import {
    addOrderMaterielRecord
  } from "@/api/order/orderMaterielRecord";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
import { options } from "runjs";

  export default {
    name: "OrderMaterielPlanC",
    props: ["orderId", "nodeId", "orderNodeId","userId","nodeStatus","principal"],
    dicts: ["materiel_type"],
    data() {
      return {
        // 物料数量计划表格数据
        orderMaterielPlanList: [],
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
       * 计算进度
       */
      computeSchedule(row){
        return ((row.materielNum/row.planNum) * 100).toFixed(2);
      },
      /**
       * 确定添加
       */
      handleSubmit() {
        for (let i = 0; i < this.orderMaterielPlanList.length; i++) {
          this.$set(this.orderMaterielPlanList[i], 'materielNum', this.orderMaterielPlanList[i].num);
          this.$set(this.orderMaterielPlanList[i], 'nodeId', this.nodeId);
          this.$set(this.orderMaterielPlanList[i], 'orderNodeId', this.orderNodeId);
        }

        addOrderMaterielRecord(this.orderMaterielPlanList).then(res => {
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
        let _this = this;
        this.orderMaterielPlanList.forEach((item, index) => {
          _this.$set(item, 'num', (item.planNum - item.materielNum))
        })

      },
      /** 查询数量计划列表 */
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
      }
    }
  };
</script>
