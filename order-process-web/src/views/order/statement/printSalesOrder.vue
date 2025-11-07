<template>
  <div>
    <el-dialog title="打印" :visible.sync="open" width="50%" append-to-body>
      <div id="printAre">
        <div style="text-align: center;">
          <h1>销售流水表</h1>
          <div class="statement-table-title">
            <div class="textl">销售总额:¥<span> {{otherData.totalAmount}}</span></div>
            <div>
              {{parseTime(queryParams.createTimeStart,'{y}.{m}.{d}')}}-{{parseTime(queryParams.createTimeEnd,'{y}.{m}.{d}')}}
            </div>
            <div class="textr">单位:元</div>
          </div>
        </div>

        <table id="table" style="width: 100%;border-collapse: collapse;border-spacing: 0;text-align: center;" border="1">
          <thead>
            <tr>
              <th>销售日期</th>
              <th>客户名称</th>
              <th>产品名称</th>
              <th>总数量</th>
              <th>总额</th>
              <th>业务员</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item,index) in salesOrderList">
              <td>{{parseTime(item.salesOrderVo.orderTime,'{y}-{m}-{d}')}}</td>
              <td>{{item.customerName}}</td>
              <td>{{item.materielVo?item.materielVo.materielName:item.materielId}}</td>
              <td>{{item.salesOrderVo.totalNum}}</td>
              <td>{{item.salesOrderVo.totalAmount}}</td>
              <td>{{item.user.userName}}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" v-print="printAre">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import {
    statementList
  } from "@/api/order/salesOrderDetails";


  export default {
    name: "printSalesOrder",
    props: ['queryParams'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 是否打开弹框
        open: false,
        // 销售单表格数据
        salesOrderList: [],
        otherData: {
          totalAmount: 0
        },
        // 打印设置
        printAre: {
          id: "printAre",
        },

      };
    },
    methods: {
      /**
       * 打开弹框
       */
      handleOpen() {
        this.open = true;
        this.getList();
      },
      /** 查询销售单列表 */
      getList() {
        this.loading = true;
        statementList(this.queryParams).then(response => {
          this.salesOrderList = response.rows;
          this.total = response.total;
          this.loading = false;
          this.otherData = response.otherData;
        });
      }
    }
  };
</script>
<style lang="scss">
  .statement-table-title {
    display: flex;

    div {
      flex: 1;
    }

    span {
      font-size: 1.3em;
    }
  }
  #table td{
    padding: 10px;
  }
</style>
<style>
  @media print {
    @page {
      margin: 8px 20px 0px 27px;
      /* margin-left: 30px; */
      size: auto;
    }

    @media print {
      .print-content {
        max-height: 50vh;
        /* 设置内容区域的最大高度为视口高度的 90% */
        /* 其他打印样式 */
      }
    }

    body {
      /* padding: 10mm; */
    }
  }
</style>
