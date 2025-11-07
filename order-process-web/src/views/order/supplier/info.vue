<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">基本信息</div>
      <el-button type="primary" v-if="!isDisabled" @click="submitForm">确 定</el-button>
      <div v-else>
        <el-button type="primary" @click="handlePrint">打 印</el-button>
        <el-button v-if="form.supplierStatus == 'Y'" @click="handleForbidden('N')">禁 用</el-button>
        <el-button v-else @click="handleForbidden('Y')">恢 复</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <el-form ref="form" :disabled="isDisabled" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="供应商" prop="supplierName">
            <el-input v-model="form.supplierName" placeholder="请输入供应商" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="供应商编号" prop="supplierNo">
            <el-input v-model="form.supplierNo" placeholder="请输入供应商编号" />
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="供应商类型" prop="supplierType">
            <el-select v-model="form.supplierType" filterable class="drag-screenful-contnet">
              <el-option v-for="dict in dict.type.supplier_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="联系人" prop="linkman">
            <el-input v-model="form.linkman" placeholder="请输入联系人" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="付款方式" prop="paymentType">
            <el-select v-model="form.paymentType" filterable class="drag-screenful-contnet">
              <el-option v-for="dict in dict.type.supplier_payment_type" :key="dict.value" :value="dict.value"
                :label="dict.label"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="付款名称" prop="paymentName">
            <el-input v-model="form.paymentName" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="付款账号" prop="account">
            <el-input v-model="form.account" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="地址" prop="action">
            <el-input v-model="form.action" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="附件" prop="files">
            <file-upload v-model="form.files" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 自定义字段列表 -->
      <form-field-list ref="formFieldListRef" :colSpan="8" businessType="BrtSupplier" :formFieldList="form.otherFields" :id="form.supplierId"/>

      <!-- 打印客户信息 -->
    <supplier-print ref="supplierPrintRef"></supplier-print>

    </el-form>

  </div>
</template>

<script>
  import {
    listSupplier,
    listSupplierAll,
    getSupplier,
    delSupplier,
    addSupplier,
    updateSupplier
  } from "@/api/order/supplier";

  import {
    listAllUser
  } from "@/api/system/user";

  import supplierPrint from '@/views/order/supplier/print'

  export default {
    name: "SupplierInfo",
    props: ["isDisabled"],
    dicts:["supplier_type","supplier_grade","supplier_payment_type"],
    components: {
      supplierPrint
    },
    data() {
      return {
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          supplierName: [{
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
          this.onLoad();
        },
        immediate: true
      }
    },

    created() {
      // this.onLoad();
    },

    methods: {
      onLoad(){
        const supplierId = this.$route.query.supplierId;
        this.handleUpdate(supplierId);
      },
      /**
       * 禁用供应商
       */
      handleForbidden(status) {
        this.$set(this.form, 'supplierStatus', status);
        this.submitForm();
      },
      /**
       * 打印
       */
      handlePrint() {
        this.$refs.supplierPrintRef.handleOpen(this.form.supplierId)
      },

      // 取消按钮
      cancel() {
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          supplierId: null,
          supplierName: null,
          supplierNo: "GYS" + new Date().getTime(),
          typeId: null,
          gradeId: null,
          contact: null,
          contactTel: null,
          userId: null,
          supplierRemark: null,
          attachments: null,
          supplierStatus: null,
          supplierAddress: null,
          logisticsAddress: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /** 修改按钮操作 */
      handleUpdate(supplierId) {
        this.reset();
        if (!supplierId) {
          return;
        }
        getSupplier(supplierId).then(response => {
          this.form = response.data;
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let fieldList = this.$refs.formFieldListRef.fieldList;
            this.$set(this.form,'otherFields',JSON.stringify(fieldList))
            if (this.form.supplierId != null) {
              updateSupplier(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.form = response.data;
                this.backPrice();
              });
            } else {
              addSupplier(this.form).then(response => {
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
          path: "/supplier/"
        };
        this.$tab.closeOpenPage(obj);
      },
    }
  };
</script>
