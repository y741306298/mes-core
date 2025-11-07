<template>
  <div class="customerAddress">
    <el-table :data="customerAddressList" style="width: 70%">
      <el-table-column label="地址类型" align="center" width="100">
        <template slot-scope="scope">
          <div style="display: flex;">
            <i class="el-icon-location-outline mt5 mr5"></i>
            <dict-tag :options="dict.type.address_type" :value="scope.row.addressType" />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="详细地址" align="center">
        <template slot-scope="scope">
          <span>
            {{scope.row.addressShort}}-{{scope.row.destination}}-{{scope.row.logisticsCompany}}-{{scope.row.addressProvince}}{{scope.row.addressCity}}{{scope.row.addressArea}}{{scope.row.addressDetails}}-{{scope.row.addressRemark}}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="邮编" prop="postcode" width="100"/>
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['order:customerAddress:edit']"></el-button>
          <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['order:customerAddress:remove']"></el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 客户地址弹框 -->
    <customer-address-info  @getCustomerAddressList="getList" ref="customerAddressInfoRef"></customer-address-info>

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

  import customerAddressInfo from '@/views/order/customerAddress/info'

  export default {
    name: "CustomerAddressList",
    dicts: ['address_type'],
    props: ['customerId'],
    components: {
      customerAddressInfo
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 用户地址表格数据
        customerAddressList: [],
        queryParams: {
          customerId: null
        }
      };
    },
    watch:{
      customerId:function(){
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询用户地址列表 */
      getList() {
        this.loading = true;
        if(!this.customerId){
          this.customerAddressList = [];
          this.loading = false;
          return;
        }
        this.queryParams.customerId = this.customerId;
        listCustomerAddressAll(this.queryParams).then(response => {
          this.customerAddressList = response.data;
          this.loading = false;
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.$refs.customerAddressInfoRef.handleOpen(row.addressId)
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const addressIds = row.addressId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delCustomerAddress(addressIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
    }
  };
</script>
<style lang="scss">
  .customerAddress{
    .el-table__header-wrapper {
      display: none;
    }
  }

</style>
