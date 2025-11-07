<template>
  <div>
    <div class="formHeader">
      <div class="formTitle mb20">任务状态</div>
      <el-button type="text" icon="el-icon-plus" @click="handleAdd" v-if="(checkRole(['admin']) || userId == $store.state.user.id || principal == $store.state.user.id) && ['1'].includes(nodeStatus)">新建子流程</el-button>
    </div>

    <!-- <el-alert :title="️'当前状态为:'+orderNodeForm.nodeStatus == '0' ? '未开始' : (orderNodeForm.nodeStatus == '1' ? '进行中' : (orderNodeForm.nodeStatus == '2' ? '已完成' : (orderNodeForm.nodeStatus == '3' ? '已超时' : ''))) " type="info" :closable="false">
    </el-alert> -->

    <div v-if="orderNodeForm" class="node-status">
      <span>当前状态为:</span>
      <dict-tag :options="dict.type.node_status" :value="orderNodeForm.nodeStatus" />
    </div>

    <el-card class="box-card mt20" v-for="(item,index) in orderChildProcessList">
      <div slot="header" class="clearfix">
        <el-button type="text" @click="toOrderDetails(item)">{{item.childName}} | {{item.childNo}}</el-button>
        <span style="float: right;padding-top: 5px;" type="text">
          <dict-tag :options="dict.type.child_status" :value="item.childStatus" />
        </span>
      </div>

      <el-descriptions title="">
        <el-descriptions-item label="客户名称" v-if="orderType!='2'">{{getObjAttr(orderForm, 'customerVo.customerName')}}</el-descriptions-item>
        <el-descriptions-item label="供应商名称" v-else>{{getObjAttr(orderForm, 'supplierVo.supplierName')}}</el-descriptions-item>
        <el-descriptions-item label="订单编号">{{orderForm.orderNo}}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{getObjAttr(item, 'flowTemplateVo.dutyUser.nickName')}}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{parseTime(getObjAttr(orderForm, 'createTime'),'{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="交货时间">{{parseTime(getObjAttr(orderForm, 'deliveryTime'),'{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="进度模板">{{getObjAttr(item, 'flowTemplateVo.templateName')}}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <dict-tag :options="dict.type.child_status" :value="item.childStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">苏州市</el-descriptions-item>
      </el-descriptions>

      <div>
        <div class="progress-text">{{item.complateNum}}/{{item.nodeNum}}</div>
        <el-progress :text-inside="true" :stroke-width="15" :percentage="(item.complateNum / item.nodeNum) * 100" :format="progressFormat">
        </el-progress>
      </div>

    </el-card>

  </div>
</template>

<script>
  import {
    listOrderChildProcess,
    listOrderChildProcessAll,
    getOrderChildProcess,
    delOrderChildProcess,
    addOrderChildProcess,
    updateOrderChildProcess
  } from "@/api/order/orderChildProcess";

  import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数

  export default {
    name: "OrderChildProcessList",
    props: ['orderId','nodeId','orderNodeId','orderForm','orderNodeForm','userId','nodeStatus','orderType','principal','templateId'],
    dicts:["child_status","node_status"],
    data() {
      return {
        // 总条数
        total: 0,
        // 订单子流程表格数据
        orderChildProcessList: [],
        // 查询参数
        queryParams: {
          orderId: null,
          nodeId: null,
          orderNodeId: null
        }
      };
    },
    watch: {
      orderNodeId:function(){
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      checkRole,
      /**
       * 查看订单详情
       */
      toOrderDetails(process){
        let orderPath = "";
        if(this.orderType=='0'){
          orderPath = "/priceSheetOrder/info/index/";
        }else if(this.orderType=='1'){
          orderPath = "/salesOrder/info/index/";
        }else if(this.orderType=='2'){
          orderPath = "/marketOrder/info/index/";
        }

        this.$emit('closeOpen')
        this.$router.push({
          path: orderPath+process.templateId,
          query: {
            orderId: process.orderId,
            childId: process.childId
          }
        });
      },
      /**
       * 清空进度条内容
       */
      progressFormat(){
        return ''
      },
      /** 新增按钮操作 */
      handleAdd() {

        this.$emit('closeOpen');
        this.$router.push({
          path: '/orderChildProcess/info/index',
          query: {
            orderId: this.orderId,
            nodeId: this.nodeId,
            orderNodeId: this.orderNodeId,
            orderType: this.orderType,
            templateId: this.templateId
          }
        });
      },
      /** 查询订单子流程列表 */
      getList() {

        if(!this.orderNodeId){
          this.orderChildProcessList = [];
          return;
        }
        this.queryParams.orderId = this.orderId;
        this.queryParams.nodeId = this.nodeId;
        this.queryParams.orderNodeId = this.orderNodeId;
        listOrderChildProcessAll(this.queryParams).then(response => {
          this.orderChildProcessList = response.data;
          console.log("-------");
          console.log(this.orderChildProcessList);
        });
      }
    }
  };
</script>
<style>
  .node-status{
    display: flex;
    background-color: #f4f4f5;
    color: #94969c;
    font-size: 0.9em;
    padding: 10px 20px;
    border-radius: 5px;
  }
  .progress-text{
    text-align: center;
    color: #777681;
    position: relative;
    z-index: 999;
    top: 17px;
    font-size: 0.9em;
  }
</style>
