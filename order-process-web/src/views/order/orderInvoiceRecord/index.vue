<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">任务记录</div>
    </div>
    <el-table border ref="brtTable" v-loading="loading" :data="orderInvoiceRecordList">
      <el-table-column label="开票金额" prop="invoiceAmount" align="center" />
      <el-table-column label="剩余开票金额" prop="surplusAmount" align="center" />
      <el-table-column label="记录时间" prop="createTime" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="userId && (checkRole(['admin']) || userId == $store.state.user.id) && nodeStatus == '1'">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDelete(scope.row)"
            v-hasPermi="['order:orderInvoiceRecord:remove']">撤销</el-button>
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
    listOrderInvoiceRecord,
    listOrderInvoiceRecordAll,
    getOrderInvoiceRecord,
    delOrderInvoiceRecord,
    addOrderInvoiceRecord,
    updateOrderInvoiceRecord
  } from "@/api/order/orderInvoiceRecord";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderInvoiceRecord",
    props: ["orderId","nodeId","orderNodeId","userId","nodeStatus"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 开票记录表格数据
        orderInvoiceRecordList: [],
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
      /** 查询开票记录列表 */
      getList() {
        this.loading = true;
        if (!this.orderId && !this.orderNodeId) {
          this.orderInvoiceRecordList = [];
          this.total = 0;
          this.loading = false;
          return;
        }
        this.queryParams.orderId = this.orderId;
        this.queryParams.nodeId = this.nodeId;
        this.queryParams.orderNodeId = this.orderNodeId;
        listOrderInvoiceRecord(this.queryParams).then(response => {
          this.orderInvoiceRecordList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const recordIds = row.recordId;
        this.$modal.confirm('确认撤销？').then(function() {
          return delOrderInvoiceRecord(recordIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("撤销成功");
        }).catch(() => {});
      }
    }
  };
</script>
