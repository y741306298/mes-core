<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">新增销售单</div>
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
                <el-form-item label="下单日期" prop="orderTime">
                  <el-date-picker class="drag-screenful-contnet" v-model="form.orderTime" type="date"
                    value-format="yyyy-MM-dd" placeholder="请选择下单日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="订单编号" prop="orderNo">
                  <el-input v-model="form.orderNo" placeholder="请输入订单编号" :disabled="true"/>
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
                  <el-select v-model="form.userId" filterable class="drag-screenful-contnet" :disabled="checkRole(['admin'])?false:true">
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
            <form-field-list ref="formFieldListRef" :colSpan="24" businessType="BrtSalesOrder" :formFieldList="form.otherFields" :id="form.orderId"/>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card class="box-card">
          <div slot="header" class="mt10 mb10">
            <span>产品信息</span>
            <div class="fr">
              <el-button type="primary" @click="salesOrderDetailsSave" size="mini">保存</el-button>
              <el-dropdown class="ml20" split-button type="primary" @command="handleDropdownClick" size="mini">
                批量操作
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="add">新增产品</el-dropdown-item>
                  <el-dropdown-item command="exportPriceSheet">导入报价单</el-dropdown-item>
                  <el-dropdown-item command="exportMateriel">从系统中添加</el-dropdown-item>
                  <el-dropdown-item command="exportExcel">导入Excel</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
          <sales-order-details :orderId="form.orderId" :isEdit="true" @setTotalInfo="setTotalInfo" class="mb20"
            ref="salesOrderDetailsRef"></sales-order-details>
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

    <el-dialog title="报价单列表" :visible.sync="price.open" width="700px" append-to-body>
      <el-row>
        <el-col :span="8">
          <el-input v-model="price.priceSheetOrderNo" placeholder="请输报价单号"></el-input>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="2">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="qeuryPriceList">搜索</el-button>
        </el-col>
      </el-row>
      <el-row>

      <el-table :data="priceList" style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column prop="orderNo" label="报价单号" width="180"></el-table-column>
          <el-table-column prop="num" label="产品数量" width="180"></el-table-column>
          <el-table-column prop="price" label="产品总价" width="180"></el-table-column>
          <el-table-column prop="remark" label="备注" width="180"></el-table-column>
      </el-table>

      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="exportPriceSheet" :disabled="single">确 定</el-button>
        <el-button @click="price.open = false">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
  import {
    priceSheetToSales,
    exportPriceList
  } from "@/api/order/priceSheetOrder";

  import {
    checkPermi,
    checkRole
  } from "@/utils/permission"; // 权限判断函数


  import {
    listSalesOrder,
    listSalesOrderAll,
    getSalesOrder,
    delSalesOrder,
    addSalesOrder,
    updateSalesOrder
  } from "@/api/order/salesOrder";

  import {
    listCustomerAll
  } from "@/api/order/customer";

  // import {
  //   listCustomerAddressAll
  // } from "@/api/order/customerAddress";

  import {
    listFlowTemplateAll
  } from "@/api/order/flowTemplate";

  import {
    getNo,
    getNoAndAdd
  } from "@/api/order/orderNo";

  import {
    listAllUser
  } from "@/api/system/user";

  import {
    getToken
  } from "@/utils/auth";

  import customerAddressInfo from '@/views/order/customerAddress/info'
  import salesOrderDetails from '@/views/order/salesOrderDetails/index';

  export default {
    name: "SalesOrderAdd",
    components: {
      customerAddressInfo,
      salesOrderDetails
    },
    dicts: ['currency_type', 'craft_type'],
    data() {
      return {
        price:{
          open: false,
          priceSheetOrderNo: null
        },
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        //报价单列表
        priceList:[],
        // 表单参数
        form: {},
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
          url: process.env.VUE_APP_BASE_API + "/order/salesOrderDetails/importData"
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
      checkRole,

      onLoad(){
        this.getCustomerList();
        this.getFlowTemplateList();
        this.getUserList();
        this.getOrderData();
      },

      getOrderData(){
        const orderId = this.$route.query.orderId;
        const type = this.$route.query.type;
        if(type != undefined && type != null && type == "price"){
          this.getPriceOrderInfo(orderId);
        }else{
          if(orderId != undefined && orderId != null){
            this.getOrderInfo(orderId);
          }else{
            this.reset();
            this.getNo();
          }
        }
      },
      getNo(){
        let queryNo = {
          keyName:"XiaoShouDan"
        }
        getNo(queryNo).then(res=>{
          this.form.orderNo = res;
        })
      },

      exportPriceSheet(){
        this.getPriceOrderInfo(this.ids[0]);
        this.price.open = false;
      },

      qeuryPriceList(){
        exportPriceList(this.price.priceSheetOrderNo).then(res=>{
            this.priceList = res.data;
        })
      },
      priceHandleQuery(){

      },
      getPriceOrderInfo(orderId){
          let data = {
            orderId:orderId
          }
          priceSheetToSales(data).then(res=>{
            this.reset();
            this.form = res.data.salesOrder;
            this.getNo();
            this.$nextTick(()=>{
              let list = res.data.salesOrderDetailsList;
              for(let i = 0 ; i< list.length ; i++){
                if(list[i].materielVo==null){
                  list[i].materielName = list[i].materielId
                }
              }
              this.$refs.salesOrderDetailsRef.setSalesOrderDetailsList(list);
            })
          })
      },
      /**
       * 保存产品信息
       */
      salesOrderDetailsSave(){
        this.$refs.salesOrderDetailsRef.salesOrderDetailsSave();
      },
      /**
       * 批量操作按钮点击事件
       */
      handleDropdownClick(command) {
        switch (command) {
          case 'add':
            this.$refs.salesOrderDetailsRef.handleAddSalesOrderDetails();
            break;
          case 'exportPriceSheet':
            this.price.open = true;
            this.qeuryPriceList();
            break;
          case 'exportMateriel':
            this.$refs.salesOrderDetailsRef.handleSelectMaterie();
            break;
          case 'exportExcel':
            this.handleImport();
            break;
        }
      },

      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.orderId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
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
          this.form = response.data;
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
          orderTime: new Date(),
          deliveryTime: null,
          templateId: null,
          craftType: null,
          totalNum: 0,
          totalAmount: 0,
          currencyType: null,
          attachments: null,
          userId: this.$store.state.user.id,
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
            this.$set(this.form, "salesOrderDetailsVoList", this.$refs.salesOrderDetailsRef.salesOrderDetailsList)
            if (this.form.orderId != null) {
              updateSalesOrder(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.backPrice();
              });
            } else {
              addSalesOrder(this.form).then(response => {
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
          path: "/salesOrder/"
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
        this.download('order/salesOrderDetails/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
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
        this.$refs.salesOrderDetailsRef.setSalesOrderDetailsList(response.data);
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
    }
  };
</script>
