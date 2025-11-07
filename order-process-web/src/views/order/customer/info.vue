<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">基本信息</div>
      <el-button type="primary" v-if="!isDisabled" @click="submitForm">确 定</el-button>
      <div v-else>
        <el-button type="primary" @click="handlePrint">打 印</el-button>
        <el-button v-if="form.customerStatus == 'Y'" @click="handleForbidden('N')">禁 用</el-button>
        <el-button v-else @click="handleForbidden('Y')">恢 复</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <el-form ref="form" :disabled="isDisabled" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="客户名称" prop="customerName">
            <el-input v-model="form.customerName" placeholder="请输入客户名称" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="客户编号" prop="customerNo">
            <el-input v-model="form.customerNo" placeholder="请输入客户编号"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="客户类型" prop="typeId">
            <el-select v-model="form.typeId" filterable class="drag-screenful-contnet">
              <el-option v-for="dict in dict.type.customer_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="1">
          <el-button @click="handleOpenCustomerType" class="mt5 ml5" icon="el-icon-plus" size="mini" circle></el-button>
        </el-col> -->
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="客户级别" prop="gradeId">
            <el-select v-model="form.gradeId" filterable class="drag-screenful-contnet">
              <el-option v-for="dict in dict.type.customer_grade" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="1">
          <el-button @click="handleOpenCustomerGrade" class="mt5 ml5" icon="el-icon-plus" size="mini"
            circle></el-button>
        </el-col> -->
        <el-col :span="8">
          <el-form-item label="联系人" prop="contact">
            <el-input v-model="form.contact" placeholder="请输入联系人" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="联系电话" prop="contactTel">
            <el-input v-model="form.contactTel" placeholder="请输入联系电话" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="业务员" prop="userId">
            <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in salesmanUserList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="客户地址" prop="customerAddress">
            <el-input v-model="form.customerAddress" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="物流地址" prop="logisticsAddress">
            <el-input v-model="form.logisticsAddress" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="customerRemark">
            <el-input v-model="form.customerRemark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-row>
        <el-col :span="24">
          <el-form-item label="地址">
            <el-button v-hasPermi="['order:customerAddress:add']" icon="el-icon-plus"
              @click="handleOpenCustomerAddress">添加地址</el-button>
            <customer-address-list :customerId="form.customerId" class="mt10"
              ref="customerAddressListRef"></customer-address-list>
          </el-form-item>
        </el-col>
      </el-row> -->
      <el-row>
        <el-col :span="24">
          <el-form-item label="附件" prop="attachments">
            <file-upload v-model="form.attachments" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 自定义字段列表 -->
      <form-field-list ref="formFieldListRef" :colSpan="8" businessType="BrtCustomer" :formFieldList="form.otherFields" :id="form.customerId"/>
    </el-form>

    <!-- 客户类型弹框 -->
    <customer-type @getList="getCustomerTypeList" ref="customerTypeRef"></customer-type>

    <!-- 客户等级弹框 -->
    <customer-grade @getList="getCustomerGradeList" ref="customerGreadeRef"></customer-grade>

    <!-- 客户地址弹框 -->
    <customer-address-info @getCustomerAddressList="getCustomerAddressList" :customerId="form.customerId"
      ref="customerAddressInfoRef"></customer-address-info>

    <!-- 打印客户信息 -->
    <customer-print ref="customerPrintRef"></customer-print>

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
    listCustomerGradeAll
  } from "@/api/order/customerGrade";

  import {
    listCustomerTypeAll
  } from "@/api/order/customerType";

  import {
    listAllUser
  } from "@/api/system/user";


  import customerType from '@/views/order/customerType/index'
  import customerGrade from '@/views/order/customerGrade/index'
  import customerAddressInfo from '@/views/order/customerAddress/info'
  import customerAddressList from '@/views/order/customerAddress/list'
  import customerPrint from '@/views/order/customer/print'

  export default {
    name: "CustomerInfo",
    props: ["isDisabled"],
    dicts:["customer_type","customer_grade"],
    components: {
      customerType,
      customerGrade,
      customerAddressInfo,
      customerAddressList,
      customerPrint
    },
    data() {
      return {
        // 表单参数
        form: {},
        // 客户类型列表
        customerTypeList: [],
        // 客户等级列表
        customerGradeList: [],
        // 业务员列表
        salesmanUserList: [],
        // 表单校验
        rules: {
          customerName: [{
            required: true,
            message: "请填写客户名称",
            trigger: "blur"
          }]
        },
      };
    },

    watch: {
      '$route': {
        handler: function(to,form){
          console.log(form);
          this.onLoad();
        },
        immediate: true
      }
    },

    created() {
      this.getCustomerTypeList();
      this.getCustomerGradeList();
      this.getSalesmanUserList();
      // this.onLoad();
    },
    methods: {
      onLoad(){
        this.handleUpdate();

      },


      /**
       * 禁用客户
       */
      handleForbidden(status) {
        this.$set(this.form, 'customerStatus', status);
        this.submitForm();
      },
      /**
       * 打印
       */
      handlePrint() {
        this.$refs.customerPrintRef.handleOpen(this.form.customerId)
      },
      /**
       * 获取客户地址列表
       */
      getCustomerAddressList() {
        this.$refs.customerAddressListRef.getList();
      },
      /**
       * 打开客户地址弹框
       */
      handleOpenCustomerAddress() {
        if (!this.form.customerId) {
          this.$notify({
            title: '温馨提示',
            message: '请先保存客户信息',
            type: 'warning'
          });
          return;
        }
        this.$refs.customerAddressInfoRef.handleOpen();
      },
      /**
       * 获取业务员列表
       */
      getSalesmanUserList() {
        listAllUser({
          roleId: 2
        }).then(res => {
          this.salesmanUserList = res.data
        })
      },
      /**
       * 获取客户等级列表
       */
      getCustomerGradeList() {
        listCustomerGradeAll().then(res => {
          this.customerGradeList = res.data;
        })
      },
      /**
       * 打开客户等级弹框
       */
      handleOpenCustomerGrade() {
        this.$refs.customerGreadeRef.handleOpen();
      },
      /**
       * 获取客户类型列表
       */
      getCustomerTypeList() {
        listCustomerTypeAll().then(res => {
          this.customerTypeList = res.data;
        })
      },
      /**
       * 打开客户等级弹框
       */
      handleOpenCustomerType() {
        this.$refs.customerTypeRef.handleOpen();
      },
      // 取消按钮
      cancel() {
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          customerId: null,
          customerName: null,
          customerNo: null,
          typeId: null,
          gradeId: null,
          contact: null,
          contactTel: null,
          userId: null,
          customerRemark: null,
          attachments: null,
          customerStatus: null,
          customerAddress: null,
          logisticsAddress: null,
          otherFields: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /** 修改按钮操作 */
      handleUpdate() {
        const customerId = this.$route.query.customerId;
        this.reset();
        if (!customerId) {
          return;
        }
        getCustomer(customerId).then(response => {
          this.form = response.data;
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let fieldList = this.$refs.formFieldListRef.fieldList;
            this.$set(this.form,'otherFields',JSON.stringify(fieldList))
            if (this.form.customerId != null) {
              updateCustomer(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.form = response.data;
                this.backPrice();
              });
            } else {
              addCustomer(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.backPrice();
                this.form = response.data;
              });
            }
          }
        });
      },
      // 返回上个页面
      backPrice() {
        this.reset();
        const obj = {
          path: "/customer/"
        };
        this.$tab.closeOpenPage(obj);
      },
    }
  };
</script>
