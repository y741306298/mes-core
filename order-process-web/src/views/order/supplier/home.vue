<template>
  <div class="app-container">
    <div class="formHeader">
      <el-button type="text" @click="handleToInfo">
        <div class="formTitle">
          {{supplierName}}
        </div>
      </el-button>
    </div>

    <div class="amount-card-list">
      <div class="amount-card" :class="item.class" v-for="(item,index) in cardList">
        <div class="">
          <span v-if="item.prop != 'receivingNum'">¥ </span>
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
        <supplier-receiving ref="supplierReceiving"></supplier-receiving>
      </el-tab-pane>
      <el-tab-pane label="供应商信息" name="supplier">
        <supplier-info :isDisabled="true"></supplier-info>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>

  import {
    totalMarketOrder
  } from "@/api/order/marketOrder";

  import supplierReceiving from "@/views/order/supplierReceiving/index"
  // import salesOrder from "@/views/order/salesOrder/list"
  import supplierInfo from "@/views/order/supplier/info"

  export default {
    name: "SupplierHome",
    components: {
      // salesOrder,
      supplierInfo,
      supplierReceiving
    },
    data() {
      return {
        // 选中的标签页
        activeName: "saledOrder",
        // 供应商名称
        supplierId: null,
        // 供应商名称
        supplierName: "",
        cardList:[{
          title: "合同金额",
          prop: "totalAmount",
          class: "card-orange",
          data: 0
        },
        {
          title: "已收货",
          prop: "receivingNum",
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
      this.supplierId = this.$route.query.supplierId;
      this.supplierName = this.$route.query.supplierName;
      this.getTotalSalesOrder();
      this.$nextTick(()=>{
        this.$refs.supplierReceiving.setSupplierId(this.supplierId);
        this.$refs.supplierReceiving.getList();
      })
    },
    methods: {
      /**
       * 统计供应商销售单信息
       */
      getTotalSalesOrder(){
        const query = {
          supplierId: this.supplierId
        };
        totalMarketOrder(query).then(res => {
          const data = res.data;
          let _this = this;
          this.cardList.forEach((card,index) => {
            _this.$set(card,'data',data[card.prop]);
          })
        })
      },
      /**
       * 打开供应商信息编辑
       */
      handleToInfo() {
        this.$router.push({
          path: '/supplier/info/index',
          query: {
            supplierId: this.$route.query.supplierId
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
