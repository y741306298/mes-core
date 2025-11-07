<template>
  <div style="padding: 20px;background-color: #f9f9f9;">
    <div class="num-card-list">
      <div class="num-card" v-for="(item,index) in cardList">
        <div class="card-title">{{item.title}}</div>
        <div class="mt10 card-num-logo">
          <div class="num">{{dataObj[item.prop]}}</div>
          <div class="num-logo">
            <img src="../assets/logo/logo.png" width="40" height="40"/>
          </div>
        </div>
        <el-progress class="mt10" :percentage="item.percentage" :color="item.color" :show-text="false"></el-progress>
      </div>
    </div>
    <div >
      <el-row class="mt20" :gutter="20">
        <el-col :span="12">
          <sales-order-statistics></sales-order-statistics>
        </el-col>
        <el-col :span="12">
            <dept-complate></dept-complate>
        </el-col>
      </el-row>

      <div v-hasPermi="['order:home:statistics']">
        <el-row class="mt20" :gutter="20">
          <el-col :span="12">
            <annual-report></annual-report>
          </el-col>
          <el-col :span="12">
            <purchase-order-statistics></purchase-order-statistics>
          </el-col>
        </el-row>
      </div>
    </div>


  </div>
</template>

<script>

  import salesOrderStatistics from "@/views/order/indexStatistics/salesOrderStatistics"
  import purchaseOrderStatistics from "@/views/order/indexStatistics/purchaseOrderStatistics"
  import annualReport from "@/views/order/indexStatistics/annualReport"
  import deptComplate from "@/views/order/indexStatistics/deptComplate"

  import {
    getHederData
  } from "@/api/order/statistics";

  export default {
    name: "Index",
    components: {
      salesOrderStatistics,
      purchaseOrderStatistics,
      annualReport,
      deptComplate
    },
    data() {
      return {
        dataObj:{},
        cardList: [{
            title: "订单总数",
            prop: "orderNum",
            color: "#ed702e",
            percentage: 50,
            data: 0
          },
          {
            title: "未出货订单",
            prop: "noDelivery",
            color: "#f5c278",
            percentage: 10,
            data: 0
          },
          // {
          //   title: "返工产品数量",
          //   prop: "notCollectionAmount",
          //   color: "#3e4ef5",
          //   percentage: 30,
          //   data: 0
          // },
          {
            title: "准时发货数量",
            prop: "punctualityDelivery",
            color: "#5ecdad",
            percentage: 80,
            data: 0
          }
        ]
      };
    },
    created(){
      this.getHederData();
    },
    methods: {
      getHederData(){
        getHederData().then(res=>{
          this.dataObj = res.data;
        })
      }
    }
  };
</script>
<style>
  .home{
    background-color: #f9f9f9;
  }
  .num-card-list {
    display: flex;
  }

  .num-card {
    border-radius: 10px;
    padding: 15px 20px;
    flex: 1;
    margin: 5px;
    background-color: white;
  }

  .card-title {
    color: #a7a7a7;
    font-size: 0.9em;
  }

  .card-num-logo{
    display: flex;
    justify-content: space-between;
    height: 40px;
    line-height: 40px;
  }

  .num {
    /* color: white; */
    font-weight: 600;
    font-size: 1.3em;
  }

  .statistics-main{
    background-color: white;
    padding: 15px 20px;
    border-radius: 10px;
  }
</style>
