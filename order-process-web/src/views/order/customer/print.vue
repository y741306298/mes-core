<template>
  <div class="app-container">
    <el-dialog title="打印" :visible.sync="open" width="70%">
      <div id="printAre">
        <div style="text-align: center;">
          <h1>客 户</h1>
        </div>
        <hr class="mt20 mb20"/>
        <el-descriptions title="" size="medium" :column="2">
          <el-descriptions-item label="客户名称">{{form.customerName}}</el-descriptions-item>
          <el-descriptions-item label="客户类型" v-if="form.typeVo">{{form.typeVo.typeName}}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{form.contact}}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{form.contactTel}}</el-descriptions-item>
          <el-descriptions-item :span="2" label="业务员" v-if="form.user">{{form.user.userName}}</el-descriptions-item>
        </el-descriptions>
        <hr class="mt20 mb20"/>
        <div class="mb20">备注:{{form.customerRemark}}</div>
        <div class="mt20">
          <span>地址:</span>
          <div class="mt10">
            {{form.customerAddress}}
          </div>
          <div class="mt10">
            {{form.logisticsAddress}}
          </div>
        </div>

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
    listCustomer,
    listCustomerAll,
    getCustomer,
    delCustomer,
    addCustomer,
    updateCustomer
  } from "@/api/order/customer";

  import {
    listCustomerAddressAll
  } from "@/api/order/customerAddress";

  export default {
    name: "CustomerPrint",
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
        // 客户地址列表
        customerAddressList: []
      };
    },
    created() {},
    methods: {
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
      handleOpen(customerId) {
        this.open = true;
        this.getCustomerInfo(customerId);
        this.getCustomerAddressList(customerId);
      },
      /**
       * 获取客户信息
       */
      getCustomerInfo(customerId) {
        getCustomer(customerId).then(res => {
          this.form = res.data;
        })
      },
      /**
       * 获取客户地址列表
       */
      getCustomerAddressList(customerId){
        const query = {
          customerId: customerId
        };
        listCustomerAddressAll(query).then(res => {
          this.customerAddressList = res.data;
        })
      }
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
