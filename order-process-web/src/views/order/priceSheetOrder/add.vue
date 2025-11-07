<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">新增报价单</div>
      <!-- <el-button type="primary" @click="submitForm">提 交</el-button> -->
    </div>
    <el-divider></el-divider>
    <el-row :gutter="20">
      <el-col :span="7">
        <el-card class="box-card">
          <div slot="header" class="mt10 mb10">
            <span>基本信息</span>
            <div class="fr">
              <el-button size="mini" @click="submitForm" type="primary">保存</el-button>
            </div>

          </div>
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="客户名称" prop="customerId">
                  <el-select v-model="form.customerId" @change="customerChange" filterable
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
                  <el-input v-model="form.contact" placeholder="请输入联系人" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="联系方式" prop="contactTel">
                  <el-input v-model="form.contactTel" placeholder="请输入联系方式" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="报价日期" prop="orderTime">
                  <el-date-picker class="drag-screenful-contnet" v-model="form.orderTime" type="date"
                    value-format="yyyy-MM-dd" placeholder="请选择报价日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="报价单号" prop="orderNo">
                  <el-input v-model="form.orderNo" placeholder="请输入报价单号" :disabled="true"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
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
                  <el-autocomplete class="inline-input drag-screenful-contnet" v-model="form.deliveryAddress" :fetch-suggestions="querySearch"
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
                  <el-select v-model="form.currencyType" class="drag-screenful-contnet" placeholder="请选择币种">
                    <el-option v-for="dict in dict.type.currency_type" :key="dict.value" :label="dict.label"
                      :value="dict.value"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="工艺类型" prop="craftType">
                  <el-radio-group class="ml20" v-model="form.craftType">
                    <el-radio v-for="dict in dict.type.craft_type" :label="dict.value">{{dict.label}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="进度模板" prop="templateId">
                  <el-select v-model="form.templateId" filterable class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in flowTemplateList" :value="item.templateId"
                      :label="item.templateName"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="订单金额" prop="totalAmount">
                  <el-input disabled v-model="form.totalAmount" placeholder="请输入订单金额" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="业务员" prop="userId">
                  <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in userList" :value="item.userId" :label="item.nickName"></el-option>
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
            <!-- 自定义字段列表 -->
            <form-field-list ref="formFieldListRef" :colSpan="24" businessType="BrtPriceSheet" :formFieldList="form.otherFields" :id="form.orderId"/>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card class="box-card">
          <div slot="header" class="mt10 mb10">
            <span>产品信息</span>
            <div class="fr">
              <el-button type="primary" @click="priceSheetOrderDetailsSave" size="mini">保存</el-button>
              <!-- <el-dropdown class="ml20" split-button type="primary" @command="handleDropdownClick" size="mini">
                批量操作
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="add">新增产品</el-dropdown-item>
                  <el-dropdown-item command="exportOffer">导入报价单</el-dropdown-item>
                  <el-dropdown-item command="exportMateriel">从系统中添加</el-dropdown-item>
                  <el-dropdown-item command="exportExcel">导入Excel</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown> -->
            </div>
          </div>
          <price-sheet-order-details :orderId="form.orderId" :isEdit="true" @setTotalInfo="setTotalInfo" class="mb20"
            ref="priceSheetOrderDetailsRef"></price-sheet-order-details>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url"
        :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess"
        :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
            @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
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
    listCustomerAll
  } from "@/api/order/customer";

  import {
    getNo,
    getNoAndAdd
  } from "@/api/order/orderNo";

  // import {
  //   listCustomerAddressAll
  // } from "@/api/order/customerAddress";

  import {
    listFlowTemplateAll
  } from "@/api/order/flowTemplate";

  import {
    listAllUser
  } from "@/api/system/user";

  import {
    getToken
  } from "@/utils/auth";

  import customerAddressInfo from '@/views/order/customerAddress/info'
  import priceSheetOrderDetails from '@/views/order/priceSheetOrderDetails/index';

  export default {
    name: "PriceSheetOrderAdd",
    components: {
      customerAddressInfo,
      priceSheetOrderDetails
    },
    dicts: ['currency_type', 'craft_type'],
    data() {
      return {
        isDisabled:false,
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
        userList: [],
        // 导入参数
        upload: {
          // 是否显示弹出层
          open: false,
          // 弹出层标题
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 设置上传的请求头部
          headers: {
            Authorization: "Bearer " + getToken()
          },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/order/priceSheetOrderDetails/importData"
        },

        // 表单校验
        rules: {
          templateId: [{
            required: true,
            message: "请选择进度模板",
            trigger: "blur"
          }]
        },

      };
    },

    watch: {
      '$route': {
        handler: function(to,form){
          this.onLoad();
        },
        immediate: true
      }
    },

    created() {


    },
    methods: {

      onLoad(){
        this.getCustomerList();
        this.getFlowTemplateList();
        const orderId = this.$route.query.orderId;
        this.getOrderInfo(orderId);
        this.getUserList();
      },

      getNo(){
        let queryNo = {
          keyName:"BaoJiaDan"
        }
        getNo(queryNo).then(res=>{
          this.form.orderNo = res;
        })
      },

      /**
       * 保存产品信息
       */
      priceSheetOrderDetailsSave(){
        this.$refs.priceSheetOrderDetailsRef.priceSheetOrderDetailsSave();
      },
      /**
       * 批量操作按钮点击事件
       */
      handleDropdownClick(command) {
        switch (command) {
          case 'add':
            this.$refs.priceSheetOrderDetailsRef.handleAddPriceSheetOrderDetails();
            break;
          case 'exportOffer':
            break;
          case 'exportMateriel':
            this.$refs.priceSheetOrderDetailsRef.handleSelectMaterie();
            break;
          case 'exportExcel':
            this.handleImport();
            break;
        }
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
          this.getNo();
          return;
        }
        getPriceSheetOrder(orderId).then(response => {
          this.form = response.data;
          this.getNo();
        });
      },
      /**
       * 设置合计值
       */
      setTotalInfo(totalObj) {
        this.form.totalNum = totalObj.totalNum;
        this.form.totalAmount = totalObj.totalAmount;
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
      // getCustomerAddressList(customerId) {
      //   listCustomerAddressAll({
      //     customerId: this.form.customerId
      //   }).then(res => {
      //     this.customerAddressList = res.data;
      //   })
      // },
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
      /**
       * 获取客户列表
       */
      getCustomerList() {
        listCustomerAll({
          customerStatus: 'Y'
        }).then(res => {
          this.customerList = res.data;
          this.customerChange(this.form.customerId);
        })
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          orderId: null,
          orderNo: null,
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
        this.resetForm("form");
        this.form.customerId = this.$route.query.customerId;

      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let fieldList = this.$refs.formFieldListRef.fieldList;
            this.$set(this.form,'otherFields',JSON.stringify(fieldList))
            this.$set(this.form, "priceSheetOrderDetailsVoList", this.$refs.priceSheetOrderDetailsRef.priceSheetOrderDetailsList)
            if (this.form.orderId != null) {
              updatePriceSheetOrder(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.backPrice();
              });
            } else {
              addPriceSheetOrder(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.backPrice();
              });
            }
          }
        });
      },
      // 返回上个页面
      backPrice() {
        this.reset();
        const obj = {
          path: "/priceSheetOrder/"
        };
        this.$tab.closeOpenPage(obj);
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        this.download('order/priceSheetOrderDetails/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        console.log(response.data);
        this.$refs.priceSheetOrderDetailsRef.setPriceSheetOrderDetailsList(response.data);
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
    }
  };
</script>
