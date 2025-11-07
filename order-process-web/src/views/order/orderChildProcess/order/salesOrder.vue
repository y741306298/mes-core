<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="7">
        <el-card class="box-card">
          <div slot="header">
            <span>基本信息</span>
            <div class="fr">
              <el-button size="mini" @click="submitForm" type="primary">保存</el-button>
            </div>
          </div>
          <el-form ref="childPorcessForm" :model="childPorcessForm" label-width="80px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="流程名称" prop="childName">
                  <el-input v-model="childPorcessForm.childName" placeholder="请输入流程名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="流程编号" prop="childNo">
                  <el-input v-model="childPorcessForm.childNo" placeholder="请输入流程编号"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="关联节点" prop="nodeName">
                  <el-input v-model="childPorcessForm.nodeName" disabled placeholder="请输入流程名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="流程名称" prop="templateId">
                  <el-select v-model="childPorcessForm.templateId" filterable class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in flowTemplateList" :value="item.templateId"
                      :label="item.templateName"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form ref="orderForm" :model="orderForm" disabled label-width="80px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="客户名称" prop="customerId">
                  <el-select v-model="orderForm.customerId" @change="customerChange" filterable
                    class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in customerList" :value="item.customerId"
                      :label="item.customerName"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="联系人" prop="contact">
                  <el-input v-model="orderForm.contact" placeholder="请输入联系人" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="联系方式" prop="contactTel">
                  <el-input v-model="orderForm.contactTel" placeholder="请输入联系方式" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="下单日期" prop="orderTime">
                  <el-date-picker class="drag-screenful-contnet" v-model="orderForm.orderTime" type="date"
                    value-format="yyyy-MM-dd" placeholder="请选择下单日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="订单编号" prop="orderNo">
                  <el-input v-model="orderForm.orderNo" placeholder="请输入订单编号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="交货日期" prop="deliveryTime">
                  <el-date-picker class="drag-screenful-contnet" v-model="orderForm.deliveryTime" type="date"
                    value-format="yyyy-MM-dd" placeholder="请选择交货日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="送货地址" prop="deliveryAddress">
                  <el-autocomplete class="inline-input drag-screenful-contnet" v-model="orderForm.deliveryAddress" :fetch-suggestions="querySearch"
                    placeholder="请输入送货地址"></el-autocomplete>
                  <!-- <el-select v-model="form.deliveryAddress" filterable class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in customerAddressList" :value="item.addressId"
                      :label="spliceCustomerAddress(item)"></el-option>
                  </el-select> -->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="币种" prop="currencyType">
                  <el-select v-model="orderForm.currencyType" class="drag-screenful-contnet" placeholder="请选择币种">
                    <el-option v-for="dict in dict.type.currency_type" :key="dict.value" :label="dict.label"
                      :value="dict.value"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="工艺类型" prop="craftType">
                  <el-radio-group class="ml20" v-model="orderForm.craftType">
                    <el-radio v-for="dict in dict.type.craft_type" :label="dict.value">{{dict.label}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="进度模板" prop="templateId">
                  <el-select v-model="orderForm.templateId" filterable class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in flowTemplateList" :value="item.templateId"
                      :label="item.templateName"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="订单金额" prop="totalAmount">
                  <el-input disabled v-model="orderForm.totalAmount" placeholder="请输入订单金额" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="业务员" prop="userId">
                  <el-select v-model="orderForm.userId" filterable class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in userList" :value="item.userId" :label="item.nickName"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注" prop="orderRemark">
                  <el-input type="textarea" v-model="orderForm.orderRemark" placeholder="请输入订单备注" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="附件" prop="attachments">
                  <file-upload-button v-model="orderForm.attachments"></file-upload-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>产品信息</span>
          </div>
          <!-- 订单详情 -->
          <sales-order-details-list :isEdit="false" :orderId="orderForm.orderId" :userId="orderForm.userId" class="mb20"
            ref="salesOrderDetailsRef"></sales-order-details-list>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>

  import {
    addOrderChildProcess
  } from "@/api/order/orderChildProcess";

  import {
    getSalesOrder
  } from "@/api/order/salesOrder";

  import {
    getOrderNode
  } from "@/api/order/orderNode";

  import {
    listCustomerAll
  } from "@/api/order/customer";

  import {
    listFlowTemplateAll
  } from "@/api/order/flowTemplate";

  import {
    listAllUser
  } from "@/api/system/user";

  import salesOrderDetailsList from '@/views/order/salesOrderDetails/index';

  export default {
    name: "SalesOrderChildProcess",
    components: {
      salesOrderDetailsList
    },
    dicts: ['currency_type', 'craft_type'],
    data() {
      return {
        // 表单参数
        childPorcessForm: {},
        orderForm: {},
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
    created() {
      this.getCustomerList();
      this.getFlowTemplateList();
      const orderId = this.$route.query.orderId;
      this.getOrderInfo(orderId);
      this.getUserList();
      const orderNodeId = this.$route.query.orderNodeId;
      this.getOrderNodeInfo(orderNodeId);
    },
    methods: {
      /**
       * 获取订单节点信息
       */
      getOrderNodeInfo(orderNodeId){
        getOrderNode(orderNodeId).then(res => {
          this.childPorcessForm = {
            childId: null,
            childNo: null,
            parentChildId: null,
            templateId: null,
            childName: null,
            orderId: res.data.orderId,
            childStatus: null,
            nodeId: res.data.nodeId,
            nodeName: res.data.flowNodeVo == null ? '' : res.data.flowNodeVo.nodeName,
            orderNodeId: res.data.orderNodeId,
            nodeNum: null,
            complateNum: null,
            createTime: null,
            createBy: null,
            updateTime: null,
            updateBy: null
          }
          this.resetForm("childPorcessForm");
        })
      },
      /**
       * @param {Object} customerId 监听客户选择事件
       */
      customerChange(customerId) {
        if (!customerId) {
          return;
        }
        const customer = this.customerList.find(o => o.customerId == customerId);
        this.form.contact = customer.contact;
        this.form.contactTel = customer.contactTel;

        let customerAddressList = [{
          value: customer.customerAddress,
          address: customer.customerAddress,
        }, {
          value: customer.logisticsAddress,
          address: customer.logisticsAddress,
        }]
        this.customerAddressList = customerAddressList;
        // 获取客户地址列表
        //this.getCustomerAddressList(customerId);
      },
      // 客户地址下拉筛选
      querySearch(queryString, cb) {
        var restaurants = this.customerAddressList;
        var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      /**
       * 获取业务员列表
       */
      getUserList() {
        listAllUser().then(res => {
          this.userList = res.data;
        })
      },
      /** 获取订单详情 */
      getOrderInfo(orderId) {
        this.reset();
        if(!orderId){
          return;
        }
        getSalesOrder(orderId).then(response => {
          this.orderForm = response.data;
        });
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
       * 获取客户列表
       */
      getCustomerList() {
        listCustomerAll({
          customerStatus: 'Y'
        }).then(res => {
          this.customerList = res.data;
          this.customerChange(this.orderForm.customerId);
        })
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.orderForm = {
          orderId: null,
          orderNo: new Date().getTime(),
          customerId: null,
          contact: null,
          contactTel: null,
          deliveryAddress: null,
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
        this.resetForm("orderForm");
      },
      /** 提交按钮 */
      submitForm() {
        let form = {
          ...this.childPorcessForm,
          orderType:"1"
        }
        addOrderChildProcess(form).then(res => {
          this.$notify({
            title: '成功',
            message: '保存成功',
            type: 'success'
          });
          this.$emit("toOrder");
        })
      },
      // 返回上个页面
      backPrice() {
        this.reset();
        const obj = {
          path: "/salesOrder/"
        };
        this.$tab.closeOpenPage(obj);
      },
    }
  };
</script>
