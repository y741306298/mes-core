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
          <el-form-item label="付款账户" prop="payAccountId">
            <el-select v-model="form.payAccountId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in accountList" :value="item.accountId"
                :label="item.accountName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="1">
          <el-button v-hasPermi="['order:account:add']" @click="handleOpenAccount" class="mt5 ml5" icon="el-icon-plus" size="mini" circle></el-button>
        </el-col>
        <el-col :span="7">
          <el-form-item label="收款账户" prop="collectionAccountId">
            <el-select v-model="form.collectionAccountId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in accountList" :value="item.accountId"
                :label="item.accountName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="1">
          <el-button v-hasPermi="['order:account:add']" @click="handleOpenAccount" class="mt5 ml5" icon="el-icon-plus" size="mini" circle></el-button>
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
          <el-form-item label="金额" prop="intertransferAmount">
            <el-input v-model="form.intertransferAmount" placeholder="请输入金额" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="日期" prop="intertransferDate">
            <el-date-picker class="drag-screenful-contnet" v-model="form.intertransferDate" type="date" value-format="yyyy-MM-dd"
              placeholder="请选择日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="intertransferRemark">
            <el-input v-model="form.intertransferRemark" type="textarea" placeholder="请输入内容" />
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

    <!-- 账户 -->
    <account @getList="getAccountList" ref="accountRef"></account>
  </div>
</template>

<script>
  import {
    listIntertransferOrder,
    listIntertransferOrderAll,
    getIntertransferOrder,
    delIntertransferOrder,
    addIntertransferOrder,
    updateIntertransferOrder
  } from "@/api/order/intertransferOrder";

  import {
    listAllUser
  } from "@/api/system/user";

  import {
    listAccountAll
  } from "@/api/order/account";

  import account from "@/views/order/account/index"

  export default {
    name: "IntertransferOrderInfo",
    components: {
      account
    },
    data() {
      return {
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 账户列表
        accountList: [],
        // 员工列表
        userList: []
      };
    },
    created() {
      this.reset();
      this.getAccountList();
      this.getUserList();
      this.getInfo();
    },
    methods: {

      getInfo(){
        this.reset();
        let intertransferId = this.$route.query.intertransferId;
        if(!intertransferId){
          return;
        }
        getIntertransferOrder(intertransferId).then(response => {
          this.form = response.data;
        });
      },
      /**
       * 打开账户弹框
       */
      handleOpenAccount() {
        this.$refs.accountRef.handleOpen();
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
      // 表单重置
      reset() {
        this.form = {
          intertransferId: null,
          intertransferNo: null,
          userId: null,
          intertransferAmount: null,
          payAccountId: null,
          collectionAccountId: null,
          intertransferDate: null,
          intertransferRemark: null,
          attachments: null,
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
            if (this.form.intertransferId != null) {
              updateIntertransferOrder(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.backPrice()
                this.getList();
              });
            } else {
              addIntertransferOrder(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.backPrice()
                this.getList();
              });
            }
          }
        });
      },
      // 返回上个页面
      backPrice() {
        this.reset();
        const obj = {
          path: "/check/intertransferOrder"
        };
        this.$tab.closeOpenPage(obj);
      },
    }
  };
</script>
