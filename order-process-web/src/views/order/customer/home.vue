<template>
  <div class="app-container">
    <div class="formHeader">
      <el-button type="text" @click="handleToInfo">
        <div class="formTitle">
          {{customerName}}
        </div>
      </el-button>
    </div>

    <div class="amount-card-list">
      <div class="amount-card" :class="item.class" v-for="(item,index) in cardList">
        <div class="">
          <span v-if="item.prop != 'deliveryNum'">¥ </span>
          <span class="amount"> {{item.data}}</span>
        </div>
        <div class="card-desc">
          <div>{{item.title}}</div>
          <img width="50" height="50" src="../../../assets/logo/logo.png"/>
        </div>
      </div>
    </div>

    <el-tabs v-model="activeName">
      <el-tab-pane label="销售记录" name="saledOrder">
        <customer-delivery ref="customerDelivery"></customer-delivery>
      </el-tab-pane>
      <el-tab-pane label="客户信息" name="customer">
        <customer-info :isDisabled="true"></customer-info>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>

  import {
    totalSalesOrder
  } from "@/api/order/salesOrder";

  import customerDelivery from "@/views/order/customerDelivery/index"
  // import salesOrder from "@/views/order/salesOrder/list"
  import customerInfo from "@/views/order/customer/info"

  export default {
    name: "CustomerHome",
    components: {
      customerDelivery,
      customerInfo
    },
    data() {
      return {
        // 选中的标签页
        activeName: "saledOrder",
        // 客户名称
        customerId: null,
        // 客户名称
        customerName: "",
        cardList:[{
          title: "合同金额",
          prop: "totalAmount",
          class: "card-orange",
          data: 0
        },
        {
          title: "已送货",
          prop: "deliveryNum",
          class: "card-blue",
          data: 0
        },
        {
          title: "未收款",
          prop: "notCollectionAmount",
          class: "card-purple",
          data: 0
        },
        {
          title: "已收款",
          prop: "collectionAmount",
          class: "card-cyan",
          data: 0
        }],
      };
    },
    created() {
      this.customerId = this.$route.query.customerId;
      this.customerName = this.$route.query.customerName;
      this.getTotalSalesOrder();
      this.$nextTick(()=>{
        this.$refs.customerDelivery.setCustomerId(this.customerId);
        this.$refs.customerDelivery.getList();
      })
      
    },
    methods: {
      /**
       * 统计客户销售单信息
       */
      getTotalSalesOrder(){
        const query = {
          customerId: this.customerId
        };
        totalSalesOrder(query).then(res => {
          const data = res.data;
          let _this = this;
          this.cardList.forEach((card,index) => {
            _this.$set(card,'data',data[card.prop]);
          })
        })
      },
      /**
       * 打开客户信息编辑
       */
      handleToInfo() {
        this.$router.push({
          path: '/customer/info/index',
          query: {
            customerId: this.$route.query.customerId
          }
        });
      },
    }
  };
</script>
<style>
  .amount-card-list{
    display: flex;
  }
  .amount-card{
    border-radius: 10px;
    padding: 15px 20px;
    color: white;
    flex: 1;
    margin: 5px;
  }
  .amount{
    color: white;
    font-weight: 600;
    font-size: 1.3em;
  }
  .card-desc{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .card-orange{
    background-color: #ed702d;
  }
  .card-blue{
    background-color: #3d4df4;
  }
  .card-purple{
    background-color: #6e2dec;
  }
  .card-cyan{
    background-color: #5ecdad;
  }
</style>
