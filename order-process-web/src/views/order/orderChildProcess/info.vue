<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">新增子流程</div>
      <!-- <el-button type="primary" @click="submitForm">提 交</el-button> -->
    </div>
    <el-divider></el-divider>
    
    <!-- 报价单 -->
    <price-sheet-order v-if="orderType=='0'" @toOrder="toOrder"></price-sheet-order>
    <!-- 销售单 -->
    <sales-order v-if="orderType=='1'" @toOrder="toOrder"></sales-order>
    <!-- 采购单 -->
    <market-order v-if="orderType=='2'" @toOrder="toOrder"></market-order>

  </div>
</template>

<script>
  import {
    listOrderChildProcess,
    listOrderChildProcessAll,
    getOrderChildProcess,
    delOrderChildProcess,
    addOrderChildProcess,
    updateOrderChildProcess
  } from "@/api/order/orderChildProcess";

  import salesOrder from '@/views/order/orderChildProcess/order/salesOrder';
  import marketOrder from '@/views/order/orderChildProcess/order/marketOrder';
  import priceSheetOrder from '@/views/order/orderChildProcess/order/priceSheetOrder';

  export default {
    name: "OrderChildProcess",
    components: {
      salesOrder,
      marketOrder,
      priceSheetOrder
    },
    data() {
      return {
        orderType: '',
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
      };
    },
    created() {
      this.orderType = this.$route.query.orderType;
    },
    methods: {
      toOrder(){
        
        const templateId = this.$route.query.templateId;
        const orderId = this.$route.query.orderId;
        const orderType = this.$route.query.orderType;
        let path = "";
        if(orderType == '0'){
          path = "/priceSheetOrder/info/index/";
        }else if(orderType == '1'){
          path = "/salesOrder/info/index/";
        }else if(orderType == '2'){
          path = "/marketOrder/info/index/";
        }
        this.$router.push({
          path: path+templateId,
          query: {
            orderId: orderId
          }
        });
      }
    }
  };
</script>
