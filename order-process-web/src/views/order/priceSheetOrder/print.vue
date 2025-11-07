<template>
  <div>
    <el-dialog title="打印" :visible.sync="open" width="70%">
      <div id="printAre">
        <div style="text-align: center;">
          <h1>报价单</h1>
        </div>
        <el-descriptions title="">
          <el-descriptions-item label="销售单号">{{form.orderNo}}</el-descriptions-item>
          <el-descriptions-item label="客户名称"
            v-if="form.customerVo">{{form.customerVo.customerName}}</el-descriptions-item>
          <el-descriptions-item label="销售日期">{{parseTime(form.orderTime,'{y}-{m}-{d}')}}</el-descriptions-item>
          <el-descriptions-item label="送货日期">{{parseTime(form.deliveryTime,'{y}-{m}-{d}')}}</el-descriptions-item>
          <el-descriptions-item :span="2" label="送货地址"
            v-if="form.customerAddressVo">{{spliceCustomerAddress(form.customerAddressVo)}}</el-descriptions-item>
          <el-descriptions-item :span="3" label="备注">{{form.orderRemark}}</el-descriptions-item>
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
            <tr v-for="(item,index) in priceSheetOrderDetailsList">
              <td>{{index + 1}}</td>
              <td>{{item.materielVo==null?item.materielId:item.materielVo.materielName}}</td>
              <td>{{item.materielUnit}}</td>
              <td>{{item.detailsPrice}}</td>
              <td>{{item.detailsNum}}</td>
              <td>{{item.detailsAmount}}</td>
              <td>{{item.detailsRemark}}</td>
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
    listPriceSheetOrder,
    listPriceSheetOrderAll,
    getPriceSheetOrder,
    delPriceSheetOrder,
    addPriceSheetOrder,
    updatePriceSheetOrder
  } from "@/api/order/priceSheetOrder";

  import {
    listPriceSheetOrderDetailsAll
  } from "@/api/order/priceSheetOrderDetails";

  export default {
    name: "PriceSheetOrderAdd",
    dicts: ['currency_type', 'craft_type'],
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
        priceSheetOrderDetailsList: []
      };
    },
    methods: {
      /**
       * 获取订单详情列表
       */
      getPriceSheetOrderDetailsList(orderId) {
        const query = {
          orderId: orderId
        };
        listPriceSheetOrderDetailsAll(query).then(res => {
          this.priceSheetOrderDetailsList = res.data;
        })
      },
      /**
       * 拼接客户地址
       */
      spliceCustomerAddress(customer) {
        if(customer.addressType == '0'){
          const address =
            `${customer.addressShort}-${customer.addressProvince}${customer.addressCity}${customer.addressArea}${customer.addressDetails}`
          return address;
        }else{
          const address =
            `${customer.destination}-${customer.logisticsCompany}-${customer.addressProvince}${customer.addressCity}${customer.addressArea}${customer.addressDetails}`
          return address;
        }

      },
      /**
       * 打开弹框
       */
      handleOpen(orderId) {
        this.open = true;
        this.getOrderInfo(orderId);
        this.getPriceSheetOrderDetailsList(orderId);
      },
      /**
       * 获取订单详情
       */
      getOrderInfo(orderId) {
        this.reset();
        getPriceSheetOrder(orderId).then(res => {
          this.form = res.data
        })
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
