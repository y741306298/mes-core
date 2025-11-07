<template>
  <div>
    <el-dialog title="打印" :visible.sync="open" width="70%">
      <div id="customerDeliveryPrint">
        <el-row style="text-align: center;margin-bottom: 10px;">
          <div style="font-size: 2em;font-weight: 700;">BRT</div>
          <div>
            <span style="font-size: 1em;">地址：高新区星火大道3号B3-1</span>
          </div>
          <div>
            <span style="font-size: 1em;">TEL：029-33112155</span>
          </div>
        </el-row>

        <div style="text-align: center;">
          <span style="font-size: 1.5em;">送货单</span>
          <span style="font-size: 0.8em; position: absolute;right:20%">时间：{{parseTime(printData.dataDate)}}</span>
        </div>
        <el-descriptions title="">
          <el-descriptions-item label="客户名称">{{ printData.customerName }}</el-descriptions-item>
          <el-descriptions-item label="客户电话">{{ printData.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="制单人员">{{ printData.contact }}</el-descriptions-item>

          <el-descriptions-item label="送货地址">{{ printData.address }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ printData.thisUser }}</el-descriptions-item>
          <el-descriptions-item label="送货日期">{{ parseTime(printData.dataDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        </el-descriptions>
        <table style="width: 100%;border-collapse: collapse;border-spacing: 0;text-align: center;" border="1">
          <thead>
            <tr>
              <td>序号</td>
              <td>产品名称</td>
              <td>料号</td>
              <td>规格</td>
              <td>单位</td>
              <td>数量</td>
              <td>单价</td>
              <td>金额</td>
              <td>备注</td>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in list">
              <td>{{ index + 1 }}</td>
              <td>{{ item.materielName }}</td>
              <td>{{ item.spec }}</td>
              <td>{{ item.spec }}</td>
              <td>{{ item.unit }}</td>
              <td>{{ item.detailsNum }}</td>
              <td>{{ item.price }}</td>
              <td>{{ item.amount }}</td>
              <td>{{ item.remark }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div >
        注：以上货品请核对数量，如有质量问题，请在收货后3天内通知本公司，逾期恕不负责。
      </div>
      <div style="width: 100%">
        送货单位及经手人（盖章）：
        <span style="position: absolute;right:20%">
           收货单位及经手人（盖章）：
        </span>
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
} from "@/api/order/customerDelivery";

export default {
  name: "CustomerDeliveryPrint",
  props: ['orderId'],
  data() {
    return {
      // 打印设置
      printAre: {
        id: "customerDeliveryPrint",
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

    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        orderNo: new Date().getTime(),
        customerId: null,
        contact: null,
        contactTel: null,
        customerAddressId: null,
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
