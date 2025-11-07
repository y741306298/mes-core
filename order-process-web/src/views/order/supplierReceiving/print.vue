<template>
  <div>
    <el-dialog title="打印" :visible.sync="open" width="70%">
      <div id="printAre">
        <div style="text-align: center;">
          <h1>收货单</h1>
        </div>
        <el-descriptions title="">
          <el-descriptions-item label="采购单号">{{printData.orderNo}}</el-descriptions-item>
          <el-descriptions-item label="供应商名称">{{printData.supplierName}}</el-descriptions-item>
          <el-descriptions-item label="采购日期">{{parseTime(printData.orderTime,'{y}-{m}-{d}')}}</el-descriptions-item>
          <el-descriptions-item label="收货日期">{{parseTime(printData.deliveryTime,'{y}-{m}-{d}')}}</el-descriptions-item>
          <el-descriptions-item :span="2" label="收货地址">{{printData.address}}</el-descriptions-item>
          <el-descriptions-item :span="3" label="备注">{{printData.remark}}</el-descriptions-item>
        </el-descriptions>
        <table style="width: 100%;border-collapse: collapse;border-spacing: 0;text-align: center;" border="1">
          <thead>
            <tr>
              <td>序号</td>
              <td>产品</td>
              <td>单位</td>
              <td>单价</td>
              <td>送货数量</td>
              <td>金额</td>
              <td>备注</td>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item,index) in list">
              <td>{{index + 1}}</td>
              <td>{{item.materielName}}</td>
              <td>{{item.unit}}</td>
              <td>{{item.price}}</td>
              <td>{{item.detailsNum}}</td>
              <td>{{item.amount}}</td>
              <td>{{item.remark}}</td>
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
    getPrintData
  } from "@/api/order/supplierReceiving";

  export default {
    name: "supplierReceiving",
    props: ['orderId'],
    data() {
      return {
        // 打印设置
        printAre: {
          id: "printAre",
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 是否打开弹框
        open: false,
        // 订单详情列表
        printData: {},
        list: []
      };
    },
    methods: {
      /**
       * 获取订单详情列表
       */
       getPrintData(orderId) {
        const query = {
          orderId: orderId
        };
        getPrintData(query).then(res => {
          this.printData = res.data;
          this.list = res.data.list;
        })
      },

      /**
       * 打开弹框
       */
      handleOpen(orderId) {
        this.open = true;
        this.getPrintData(orderId);
      },
      /**
       * 打开弹框
       */
      handleOpen(orderId) {
        this.open = true;
        this.getPrintData(orderId);
      },

      // 表单重置
      reset() {
        this.form = {
          orderId: null,
          orderNo: new Date().getTime(),
          supplierId: null,
          contact: null,
          contactTel: null,
          orderTime: null,
          deliveryTime: null,
          templateId: null,
          craftType: null,
          totalNum: 0,
          totalAmount: 0,
          currencyType: null,
          attachments: null,
          userId: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
    }
  };
</script>
<style>
  .el-dialog__body {
    background-color: white !important;
  }
  @media print {
    @page {
      margin: 8px 20px 0px 27px;
      /* margin-left: 30px; */
      /* size: auto; */
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
