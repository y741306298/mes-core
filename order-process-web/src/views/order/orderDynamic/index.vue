<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">
        <span class="mr20">动态信息</span>
      </div>
    </div>
    <el-divider></el-divider>

    <template v-for="(item,index) in orderDynamicList">
      <el-row>
        <el-col :span="4" style="text-align: center;">
          <img src="../../../assets/images/profile.jpg" width="70" height="70" style="border-radius: 50%;" />
        </el-col>
        <el-col :span="20">
          <div class="record">
            <div class="user-info">
              <div>
                <span class="mr20" v-if="item.user">{{item.user.userName}}</span>
                <span class="flow-node mr20" v-if="item.flowNodeVo">#{{item.flowNodeVo.nodeName}}#</span>
                <el-button type="text" icon="el-icon-delete" v-if="$store.state.user.id == item.userId" @click="handleDelete(item)"></el-button>
              </div>
              <div class="dynamic-time">{{parseTime(item.createTime,'{y}.{m}.{d} {h}:{i}')}}</div>
            </div>
            <div class="mt10">{{item.dynamicContent}}</div>
            <div class="mt10" v-if="item.attachments">
              <el-button @click="toFileUrl(file)" icon="el-icon-paperclip" type="text" v-for="(file,fileIndex) in item.attachments.split(',')">{{file.split('/').pop()}}</el-button>
            </div>
            <div class="mt10">
              <span>备注：</span>
              <span>{{item.remark}}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-divider></el-divider>
    </template>

  </div>
</template>

<script>
  import {
    listOrderDynamic,
    listOrderDynamicAll,
    getOrderDynamic,
    delOrderDynamic,
    addOrderDynamic,
    updateOrderDynamic
  } from "@/api/order/orderDynamic";

  export default {
    name: "OrderDynamic",
    props: ['orderId'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 订单动态表格数据
        orderDynamicList: [],
        // 查询参数
        queryParams: {
          orderId: null,
        },
        // 当前登录用户ID
        userId: null
      };
    },
    watch: {
      orderId: function() {
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      /**
       * 下载文件
       */
      toFileUrl(file){
        this.$download.resource(file);
      },
      /** 查询订单动态列表 */
      getList() {
        this.loading = true;
        if (!this.orderId) {
          this.orderDynamicList = [];
          this.loading = false;
          return;
        }
        this.queryParams.orderId = this.orderId;
        listOrderDynamicAll(this.queryParams).then(response => {
          this.orderDynamicList = response.data;
          this.loading = false;
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const dynamicIds = row.dynamicId;
        this.$modal.confirm('确认删除？').then(function() {
          return delOrderDynamic(dynamicIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
    }
  };
</script>
<style>
  .user-info {
    display: flex;
    justify-content: space-between;
  }

  .flow-node {
    padding: 5px 15px;
    background-color: #efefef;
    border-radius: 20px;
  }

  .dynamic-time {
    color: #a3a3a3;
    padding-top: 8px;
  }
</style>
