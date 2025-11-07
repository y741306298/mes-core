<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">基本信息</div>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
    <el-divider></el-divider>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="7">
          <el-form-item label="类别" prop="typeId">
            <el-select v-model="form.typeId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in checkTypeList" :value="item.typeId" :label="item.typeName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="1">
          <el-button v-hasPermi="['order:checkType:add']" @click="handleOpenCheckType" class="mt5 ml5"
            icon="el-icon-plus" size="mini" circle></el-button>
        </el-col>
        <el-col :span="8">
          <el-form-item label="关联单号" prop="orderId">
            <el-select v-model="form.orderId" filterable class="drag-screenful-contnet" @change="orderChange">
              <el-option v-for="(item,index) in orderList" :value="item.orderId" :label="item.orderNo"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="员工" prop="userId">
            <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in userList" :value="item.userId" :label="item.userName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="金额" prop="checkAmount">
            <el-input v-model="form.checkAmount" placeholder="请输入金额" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="账户" prop="accountId">
            <el-select v-model="form.accountId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in accountList" :value="item.accountId"
                :label="item.accountName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="1">
          <el-button v-hasPermi="['order:account:add']" @click="handleOpenAccount" class="mt5 ml5" icon="el-icon-plus"
            size="mini" circle></el-button>
        </el-col>
        <el-col :span="8">
          <el-form-item label="账单日期" prop="checkDate">
            <el-date-picker class="drag-screenful-contnet" v-model="form.checkDate" type="date"
              value-format="yyyy-MM-dd" placeholder="请选择账单日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="checkRemark">
            <el-input v-model="form.checkRemark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="附件" prop="attachments">
            <file-upload v-model="form.attachments" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!-- 账单类型 -->
    <check-type @getList="getCheckTypeList" :checkType="checkType" ref="checkTypeRef"></check-type>

    <!-- 账户 -->
    <account @getList="getAccountList" ref="accountRef"></account>
  </div>
</template>

<script>
  import {
    listCheck,
    listCheckAll,
    getCheck,
    delCheck,
    addCheck,
    updateCheck
  } from "@/api/order/check";

  import {
    listCheckTypeAll
  } from "@/api/order/checkType";

  import {
    listAllUser
  } from "@/api/system/user";

  import {
    listAccountAll
  } from "@/api/order/account";

  import {
    orderAllList
  } from "@/api/order/orderExamine";

  import checkType from "@/views/order/checkType/index"

  import account from "@/views/order/account/index"

  export default {
    name: "CheckInfo",
    components: {
      checkType,
      account
    },
    data() {
      return {
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 账单分类名称
        checkTypeList: [],
        // 获取账户列表
        accountList: [],
        // 员工列表
        userList: [],
        // 账单类型
        checkType: null,
        // 订单列表
        orderList: []
      };
    },
    created() {
      this.checkType = this.$route.query.type;
      this.reset();
      this.getCheckTypeList();
      this.getUserList();
      this.getAccountList();
      this.getOrderList();
      this.getInfo();
    },
    methods: {

      getInfo(){
        this.reset();
        let checkId = this.$route.query.checkId;
        if(!checkId){
          return;
        }
        getCheck(checkId).then(res=>{
            this.form = res.data;
        })
      },
      /**
       * 监听订单选择事件
       */
      orderChange(orderId) {
        const order = this.orderList.find(item => item.orderId == orderId);
        this.$set(this.form, 'orderNo', order.orderNo);
        this.$set(this.form, 'orderType', order.orderType);
      },
      /**
       * 获取订单列表
       */
      getOrderList() {
        orderAllList().then(res => {
          this.orderList = res.data;
        })
      },
      /**
       * 打开账户弹框
       */
      handleOpenAccount() {
        this.$refs.accountRef.handleOpen();
      },
      /**
       * 打开账单分类弹框
       */
      handleOpenCheckType() {
        this.$refs.checkTypeRef.handleOpen();
      },
      /**
       * 获取账户列表
       */
      getAccountList() {
        listAccountAll().then(res => {
          this.accountList = res.data;
        })
      },
      /**
       * 获取员工列表
       */
      getUserList() {
        listAllUser().then(res => {
          this.userList = res.data;
        })
      },
      /**
       * 获取账单类型列表
       */
      getCheckTypeList() {
        listCheckTypeAll({
          type: this.checkType
        }).then(res => {
          this.checkTypeList = res.data;
        })
      },
      // 表单重置
      reset() {
        this.form = {
          checkId: null,
          typeId: null,
          orderId: null,
          orderType: null,
          orderNo: null,
          userId: null,
          checkAmount: 0,
          accountId: null,
          checkDate: null,
          checkRemark: null,
          attachments: null,
          checkType: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.checkId != null) {
              updateCheck(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.backPrice();
                this.getList();
              });
            } else {
              this.form.checkType = this.checkType;
              addCheck(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.backPrice();
                this.getList();
              });
            }
          }
        });
      },
      // 返回上个页面
      backPrice() {
        this.reset();
        if (this.checkType == '0') {
          const obj = {
            path: "/check/checkc?type=0"
          };
          this.$tab.closeOpenPage(obj);
        } else {
          const obj = {
            path: "/check/checks?type=1"
          };
          this.$tab.closeOpenPage(obj);
        }

      },
    }
  };
</script>
