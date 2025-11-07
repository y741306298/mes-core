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
              <el-form-item label="客户名称" prop="customerId">
                <el-select v-model="form.customerId" filterable class="drag-screenful-contnet">
                  <el-option v-for="(item,index) in customerList" :value="item.customerId"
                    :label="item.customerName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="订单编号" prop="orderNo">
                <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系人" prop="contact">
                <el-input v-model="form.contact" placeholder="请输入联系人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="contactTel">
                <el-input v-model="form.contactTel" placeholder="请输入联系方式" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="下单日期" prop="orderTime">
                <el-date-picker class="drag-screenful-contnet" v-model="form.orderTime" type="date"
                  value-format="yyyy-MM-dd" placeholder="请选择下单日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="交货日期" prop="deliveryTime">
                <el-date-picker class="drag-screenful-contnet" v-model="form.deliveryTime" type="date"
                  value-format="yyyy-MM-dd" placeholder="请选择交货日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="送货地址" prop="deliveryAddress">
                <el-input v-model="form.deliveryAddress" placeholder="请填写送货地址" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="进度模板" prop="templateId">
                <el-select v-model="form.templateId" filterable class="drag-screenful-contnet">
                  <el-option v-for="(item,index) in flowTemplateList" :value="item.templateId"
                    :label="item.templateName"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="币种" prop="currencyType">
                <el-select v-model="form.currencyType" class="drag-screenful-contnet" placeholder="请选择币种">
                  <el-option v-for="dict in dict.type.currency_type" :key="dict.value" :label="dict.label"
                    :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="工艺类型" prop="craftType">
                <el-radio-group v-model="form.craftType">
                  <el-radio v-for="dict in dict.type.craft_type" :label="dict.value">{{dict.label}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="showPrice">
              <el-form-item label="订单金额" prop="totalAmount">
                <el-input v-model="form.totalAmount" placeholder="请输入订单金额" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="业务员" prop="userId">
                <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
                  <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                    :label="item.nickName"></el-option>
                </el-select>
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
        <price-sheet-order-details-list :isEdit="false" :orderId="orderId" :userId="form.userId" class="mb20"
          ref="priceSheetDetailsRef"></price-sheet-order-details-list>
      </el-col>
    </el-row>

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
    listCustomerAll
  } from "@/api/order/customer";

  import {
    listCustomerAddressAll
  } from "@/api/order/customerAddress";

  import {
    listFlowTemplateAll
  } from "@/api/order/flowTemplate";

  import {
    listAllUser
  } from "@/api/system/user";

  import priceSheetOrderDetailsList from '@/views/order/priceSheetOrderDetails/index';
  import store from '@/store'

  export default {
    name: "PriceSheetOrderAdd",
    components: {
      priceSheetOrderDetailsList
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
        // 客户列表
        customerList: [],
        // 客户地址列表
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
      this.getCustomerList();
      this.getFlowTemplateList();
      this.getUserList();
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
        getPriceSheetOrder(this.orderId).then(res => {
          this.form = res.data
          //this.getCustomerAddressList(this.form.customerId)
        })
      },
      /**
       * 打开客户地址弹框
       */
      handleOpenCustomerAddress() {
        this.$refs.customerAddressInfoRef.handleOpen();
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
       * 拼接客户地址
       */
      spliceCustomerAddress(customer) {
        if (customer.addressType == '0') {
          const address =
            `${customer.addressShort}-${customer.addressProvince}${customer.addressCity}${customer.addressArea}${customer.addressDetails}`
          return address;
        } else {
          const address =
            `${customer.destination}-${customer.logisticsCompany}-${customer.addressProvince}${customer.addressCity}${customer.addressArea}${customer.addressDetails}`
          return address;
        }
      },
      /**
       * 获取客户地址
       */
      getCustomerAddressList(customerId) {
        listCustomerAddressAll({
          customerId: customerId
        }).then(res => {
          this.customerAddressList = res.data;
        })
      },
      /**
       * 获取客户列表
       */
      getCustomerList() {
        listCustomerAll().then(res => {
          this.customerList = res.data;
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
      isShow(){
        const permissions = store.getters && store.getters.permissions;
        if(permissions.includes("*:*:*")||permissions.includes("order:show:price")){
          this.showPrice = true;
        }
      }
    }
  };
</script>
