<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">任务记录</div>
    </div>
    <el-table border ref="brtTable" v-loading="loading" :data="orderCollectionRecordList">
      <el-table-column label="收款金额" prop="collectionAmount" align="center" />
      <el-table-column label="剩余收款金额" prop="surplusAmount" align="center" />
      <el-table-column label="记录时间" prop="createTime" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="userId && (checkRole(['admin']) || userId == $store.state.user.id) && nodeStatus == '1'">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDelete(scope.row)"
            v-hasPermi="['order:orderCollectionRecord:remove']">撤销</el-button>
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
    listOrderCollectionRecord,
    listOrderCollectionRecordAll,
    getOrderCollectionRecord,
    delOrderCollectionRecord,
    addOrderCollectionRecord,
    updateOrderCollectionRecord
  } from "@/api/order/orderCollectionRecord";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderCollectionRecord",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 收款记录表格数据
        orderCollectionRecordList: [],
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
      orderId: function() {
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      checkRole,
      /** 查询收款记录列表 */
      getList() {
        this.loading = true;
        if (!this.orderId && !this.orderNodeId) {
          this.orderCollectionRecordList = [];
          this.total = 0;
          this.loading = false;
          return;
        }
        this.queryParams.orderId = this.orderId;
        this.queryParams.nodeId = this.nodeId;
        this.queryParams.orderNodeId = this.orderNodeId;
        listOrderCollectionRecord(this.queryParams).then(response => {
          this.orderCollectionRecordList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const recordIds = row.recordId;
        this.$modal.confirm('确认撤销？').then(function() {
          return delOrderCollectionRecord(recordIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("撤销成功");
        }).catch(() => {});
      },
    }
  };
</script>
