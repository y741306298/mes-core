<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">新增采购单</div>
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
                <el-form-item label="供应商" prop="supplierId">
                  <el-select v-model="form.supplierId" filterable
                    class="drag-screenful-contnet" @change="supplierChange">
                    <el-option v-for="(item,index) in supplierList" :value="item.supplierId"
                      :label="item.supplierName"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="联系人" prop="contact">
                  <el-input v-model="form.contact" placeholder="请输入联系人" :disabled="true"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="联系方式" prop="contactTel">
                  <el-input v-model="form.contactTel" placeholder="请输入联系方式" :disabled="true"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="采购日期" prop="orderTime">
                  <el-date-picker class="drag-screenful-contnet" v-model="form.orderTime" type="date"
                    value-format="yyyy-MM-dd" placeholder="请选择采购日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="采购编号" prop="orderNo">
                  <el-input v-model="form.orderNo" placeholder="请输入订单编号" :disabled="true"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="收货日期" prop="receivingTime">
                  <el-date-picker class="drag-screenful-contnet" v-model="form.receivingTime" type="date"
                    value-format="yyyy-MM-dd" placeholder="请选择收货日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="付款日期" prop="paymentTime">
                  <el-date-picker class="drag-screenful-contnet" v-model="form.paymentTime" type="date"
                    value-format="yyyy-MM-dd" placeholder="请选择付款日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="收货地址" prop="deliveryAddress">
                  <el-autocomplete class="inline-input drag-screenful-contnet" v-model="form.deliveryAddress" :fetch-suggestions="querySearch"
                    placeholder="请输入收货地址"></el-autocomplete>
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
            <!-- <el-row>
              <el-col :span="24">
                <el-form-item label="工艺类型" prop="craftType">
                  <el-radio-group class="ml20" v-model="form.craftType">
                    <el-radio v-for="dict in dict.type.craft_type" :label="dict.value">{{dict.label}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row> -->
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
                <el-form-item label="采购员" prop="userId">
                  <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
                    <el-option v-for="(item,index) in userList" :value="item.userId.toString()" :label="item.nickName"></el-option>
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
            <!-- <el-row>
              <el-col :span="24">
                <el-form-item label="附件" prop="attachments">
                  <file-upload-button v-model="form.attachments"></file-upload-button>
                </el-form-item>
              </el-col>
            </el-row> -->
            <!-- 自定义字段列表 -->
            <form-field-list ref="formFieldListRef" :colSpan="24" businessType="BrtMarketOrder" :formFieldList="form.otherFields" :id="form.orderId"/>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card class="box-card">
          <div slot="header" class="mt10 mb10">
            <span>产品信息</span>
            <div class="fr">
              <el-button type="primary" @click="marketOrderDetailsSave" size="mini">保存</el-button>
              <el-dropdown class="ml20" split-button type="primary" @command="handleDropdownClick" size="mini">
                更多操作
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="add">新增物料</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
          <market-order-details :orderId="form.orderId" :isEdit="true" @setTotalInfo="setTotalInfo" class="mb20"
            ref="marketOrderDetailsRef"></market-order-details>
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
    listMarketOrder,
    listMarketOrderAll,
    getMarketOrder,
    delMarketOrder,
    addMarketOrder,
    updateMarketOrder
  } from "@/api/order/marketOrder";

  import {
    getSupplier,
    listSupplierAll
  } from "@/api/order/supplier";

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

  import {
    getNo,
    getNoAndAdd
  } from "@/api/order/orderNo";

  import customerAddressInfo from '@/views/order/customerAddress/info'
  import marketOrderDetails from '@/views/order/marketOrderDetails/index';

  export default {
    name: "MarketOrderAdd",
    components: {
      customerAddressInfo,
      marketOrderDetails
    },
    dicts: ['currency_type', 'craft_type'],
    data() {
      return {
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
          url: process.env.VUE_APP_BASE_API + "/order/marketOrderDetails/importData"
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
        this.getSupplierList();
        this.getFlowTemplateList();
        const orderId = this.$route.query.orderId;
        this.getOrderInfo(orderId);
        this.getUserList();
      },

      getNo(){
        let queryNo = {
          keyName:"CaiGouDan"
        }
        getNo(queryNo).then(res=>{
          this.form.orderNo = res;
        })
      },

      supplierChange(){
        getSupplier(this.form.supplierId).then(res=>{
            this.form.contact = res.data.linkman;
            this.form.contactTel = res.data.phone;
        })
      },

      addMateriel(){
        this.$router.push({
          path: '/materiel/index/index',
          query: {
            type: "add"
          }
        });
      },
      /**
       * 保存产品信息
       */
      marketOrderDetailsSave(){
        this.$refs.marketOrderDetailsRef.marketOrderDetailsSave();
      },
      /**
       * 批量操作按钮点击事件
       */
      handleDropdownClick(command) {
        switch (command) {
          case 'add':
            this.addMateriel();
            break;
        }
      },
      // 供应商地址下拉筛选
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
        getMarketOrder(orderId).then(response => {
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
        listSupplierAll({
          supplierStatus: 'Y'
        }).then(res => {
          this.supplierList = res.data;
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
          orderNo: new Date().getTime(),
          supplierId: null,
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
        this.form.supplierId = this.$route.query.supplierId;
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let fieldList = this.$refs.formFieldListRef.fieldList;
            this.$set(this.form,'otherFields',JSON.stringify(fieldList))
            this.$set(this.form, "marketOrderDetailsVoList", this.$refs.marketOrderDetailsRef.marketOrderDetailsList)
            if (this.form.orderId != null) {
              updateMarketOrder(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.backPrice();
              });
            } else {
              addMarketOrder(this.form).then(response => {
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
          path: "/marketOrder/"
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
        this.download('order/marketOrderDetails/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
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
        this.$refs.marketOrderDetailsRef.setMarketOrderDetailsList(response.data);
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
    }
  };
</script>
