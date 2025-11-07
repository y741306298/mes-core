<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">任务记录</div>
    </div>
    <el-table border ref="brtTable" v-loading="loading" :data="orderMaterielRecordList">/>
      <el-table-column label="产品名称" prop="materielId" align="center" >
        <template slot-scope="scope">
          <span>{{scope.row.materielVo == null ? scope.row.materielId : scope.row.materielVo.materielName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成数" prop="materielNum" align="center" />
      <el-table-column label="记录时间" prop="createTime" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="userId && (checkRole(['admin']) || userId == $store.state.user.id) && nodeStatus == '1'">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDelete(scope.row)"
            v-hasPermi="['order:orderMaterielRecord:remove']">撤销</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-divider></el-divider>
  </div>
</template>

<script>
  import {
    listOrderMaterielRecord,
    listOrderMaterielRecordAll,
    getOrderMaterielRecord,
    delOrderMaterielRecord,
    addOrderMaterielRecord,
    updateOrderMaterielRecord
  } from "@/api/order/orderMaterielRecord";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderMaterielRecord",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 数量记录表格数据
        orderMaterielRecordList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
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
      /** 查询数量记录列表 */
      getList() {
        this.loading = true;
        if (!this.orderId && !this.orderNodeId) {
          this.orderMaterielRecordList = [];
          this.total = 0;
          this.loading = false;
          return;
        }
        this.queryParams.orderId = this.orderId;
        this.queryParams.nodeId = this.nodeId;
        this.queryParams.orderNodeId = this.orderNodeId;
        listOrderMaterielRecord(this.queryParams).then(response => {
          this.orderMaterielRecordList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const recordIds = row.recordId;
        this.$modal.confirm('确认撤销？').then(function() {
          return delOrderMaterielRecord(recordIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("撤销成功");
        }).catch(() => {});
      }
    }
  };
</script>
