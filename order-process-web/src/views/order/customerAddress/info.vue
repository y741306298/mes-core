<template>
  <div>
    <!-- 添加或修改用户地址对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-tabs v-model="form.addressType">
          <el-tab-pane label="客户地址" name="0">
            <el-row>
              <el-col :span="24">
                <el-form-item label="地址简称" prop="addressShort">
                  <el-input v-model="form.addressShort" placeholder="请输入地址简称" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="物流地址" name="1">
            <el-row>
              <el-col :span="24">
                <el-form-item label="目的地" prop="destination">
                  <el-input v-model="form.destination" placeholder="请输入目的地" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="物流公司" prop="logisticsCompany">
                  <el-input v-model="form.logisticsCompany" placeholder="请输入物流公司" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>

        <el-row>
          <el-col :span="24">
            <el-form-item label="地址" prop="addressProvince">
              <div style="display: flex;">
                <el-input class="mr5" v-model="form.addressProvince" placeholder="请输入省" />
                <el-input class="mr5" v-model="form.addressCity" placeholder="请输入市" />
                <el-input v-model="form.addressArea" placeholder="请输入区" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="详细地址" prop="addressDetails">
              <el-input v-model="form.addressDetails" placeholder="请输入详细地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="邮编" prop="postcode">
              <el-input v-model="form.postcode" placeholder="请输入邮编" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="addressRemark">
              <el-input v-model="form.addressRemark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listCustomerAddress,
    listCustomerAddressAll,
    getCustomerAddress,
    delCustomerAddress,
    addCustomerAddress,
    updateCustomerAddress
  } from "@/api/order/customerAddress";

  export default {
    name: "CustomerAddressInfo",
    props: ['customerId'],
    data() {
      return {
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
      };
    },
    created() {

    },
    methods: {
      /**
       * 打开地址弹框
       */
      handleOpen(addressId) {
        this.reset();
        if(addressId){
          this.handleUpdate(addressId)
        }
        this.open = true;
        this.title = "客户地址"
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          addressId: null,
          customerId: this.customerId,
          addressType: "0",
          addressShort: null,
          destination: null,
          logisticsCompany: null,
          addressProvince: null,
          addressCity: null,
          addressArea: null,
          addressDetails: null,
          postcode: null,
          addressRemark: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加用户地址";
      },
      /** 修改按钮操作 */
      handleUpdate(addressId) {
        getCustomerAddress(addressId).then(response => {
          this.form = response.data;
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.addressId != null) {
              updateCustomerAddress(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.$emit('getCustomerAddressList');
              });
            } else {
              addCustomerAddress(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.$emit('getCustomerAddressList');
              });
            }
          }
        });
      }
    }
  };
</script>
