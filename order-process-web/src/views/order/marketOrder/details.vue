<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="formHeader">
          <div class="formTitle">基本信息</div>
        </div>
        <el-divider></el-divider>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" disabled>
          <el-row>
            <el-col :span="12">
              <el-form-item label="供应商名称" prop="supplierId">
                <el-select v-model="form.supplierId" filterable class="drag-screenful-contnet">
                  <el-option v-for="(item,index) in supplierList" :value="item.supplierId"
                    :label="item.supplierName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="采购单号" prop="orderNo">
                <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="采购日期" prop="orderTime">
                <el-date-picker class="drag-screenful-contnet" v-model="form.orderTime" type="date"
                  value-format="yyyy-MM-dd" placeholder="请选择下单日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计划付款日期" prop="receivingTime">
                <el-date-picker class="drag-screenful-contnet" v-model="form.receivingTime" type="date"
                  value-format="yyyy-MM-dd" placeholder="请选择计划付款日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="收货日期" prop="receivingTime">
                <el-date-picker class="drag-screenful-contnet" v-model="form.receivingTime" type="date"
                  value-format="yyyy-MM-dd" placeholder="请选择收货日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="进度模板" prop="templateId">
                <el-select v-model="form.templateId" filterable class="drag-screenful-contnet">
                  <el-option v-for="(item,index) in flowTemplateList" :value="item.templateId"
                    :label="item.templateName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
              <el-col :span="12" v-if="showPrice">
                <el-form-item label="订单金额" prop="totalAmount"  v-hasPermi="['order:show:price']">
                  <el-input v-model="form.totalAmount" placeholder="请输入订单金额"/>
                </el-form-item>
              </el-col>

            <el-col :span="12">
              <el-form-item label="采购员" prop="userId">
                <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
                  <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                    :label="item.nickName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="收货地址" prop="deliveryAddress">
                <el-input v-model="form.deliveryAddress" placeholder="请填写收货地址" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="orderRemark">
                <el-input type="textarea" v-model="form.orderRemark" placeholder="请输入订单备注" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="附件" prop="attachments">
                <file-upload-button v-model="form.attachments"></file-upload-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
      <el-col :span="12">
        <div class="formHeader">
          <div class="formTitle">
            <span class="mr20">订单信息</span>
          </div>
        </div>
        <el-divider></el-divider>
        <!-- 订单详情 -->
        <market-order-details-list :isEdit="false" :orderId="orderId" :userId="form.userId" class="mb20"
          ref="marketOrderDetailsRef"></market-order-details-list>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  import {
    listMarketOrder,
    listMarketOrderAll,
    getMarketOrder,
    delMarketOrder,
    addMarketOrder,
    updateMarketOrder
  } from "@/api/order/marketOrder";

  import {
    listSupplierAll
  } from "@/api/order/supplier";

  import {
    listSupplierAddressAll
  } from "@/api/order/customerAddress";

  import {
    listFlowTemplateAll
  } from "@/api/order/flowTemplate";

  import {
    listAllUser
  } from "@/api/system/user";

  import marketOrderDetailsList from '@/views/order/marketOrderDetails/index';
  import store from '@/store'

  export default {
    name: "MarketOrderAdd",
    components: {
      marketOrderDetailsList
    },
    dicts: ['currency_type', 'craft_type'],
    props: ['orderId'],
    data() {
      return {
        showPrice: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 供应商列表
        supplierList: [],
        // 供应商地址列表
        customerAddressList: [],
        // 模板列表
        flowTemplateList: [],
        // 业务员列表
        userList: []
      };
    },
    watch: {
      orderId: function() {
        this.getOrderInfo();
      }
    },
    created() {
      this.getOrderInfo();
      this.getSupplierList();
      this.getFlowTemplateList();
      this.getUserList();
      this.isShow();
    },
    methods: {
      /**
       * 获取业务员列表
       */
      getUserList() {
        listAllUser().then(res => {
          this.userList = res.data;
        })
      },
      /**
       * 获取订单详情
       */
      getOrderInfo() {
        this.reset();
        if (!this.orderId) {
          return;
        }
        getMarketOrder(this.orderId).then(res => {
          this.form = res.data
          //this.getSupplierAddressList(this.form.supplierId)
        })
      },

      /**
       * 获取模板列表
       */
      getFlowTemplateList() {
        listFlowTemplateAll({
          templateStatus: true
        }).then(res => {
          this.flowTemplateList = res.data;
        })
      },

      /**
       * 获取供应商列表
       */
      getSupplierList() {
        listSupplierAll().then(res => {
          this.supplierList = res.data;
        })
      },
      // 表单重置
      reset() {
        this.form = {
          orderId: null,
          orderNo: new Date().getTime(),
          supplierId: null,
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

      isShow(){
        const permissions = store.getters && store.getters.permissions;
        if(permissions.includes("*:*:*")||permissions.includes("order:show:price")){
          this.showPrice = true;
          alert(this.showPrice);
        }
      }
    }
  };
</script>
