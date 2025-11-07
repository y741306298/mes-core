<template>
  <div class="app-container">
    <el-drawer title="订单详情" size="80%" :visible.sync="open" direction="rtl">
      <template #title>
        <el-row>
          <el-col :span="22">
            订单详情
          </el-col>
          <el-col :span="2" v-if="orderType == '1' || order_type == '1'">
            <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
              打印
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="1">打印工序流转卡</el-dropdown-item>
                <el-dropdown-item command="2">打印首件确认单</el-dropdown-item>
                <el-dropdown-item command="3">打印领料单</el-dropdown-item>
                <el-dropdown-item command="4">打印成品入库单</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
      </template>
      <el-tabs v-model="activeName" class="ml20 mr20">
        <el-tab-pane :label="form.flowNodeVo == null ? flowNodeName : form.flowNodeVo.nodeName" name="node">
          <el-form ref="form" :model="form" :rules="rules" :disabled="!isEdit" label-width="80px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="负责人" prop="userId">
                  <el-select disabled v-model="form.userId">
                    <el-option v-for="(item, index) in userList" :label="item.userName"
                      :value="item.userId.toString()"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="完成时间" prop="complateDate">
                  <el-date-picker disabled v-model="form.complateDate" type="date" value-format="yyyy-MM-dd"
                    placeholder="请选择完成时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-divider></el-divider>


            <template v-if="orderType == '0' || order_type == '0'">
              <template v-if="price.isToPriceNode">
                <el-row><!-- 去报价 -->
                  <el-col :span="24">
                    <el-form-item :label="price.title">
                      <div>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="" style="text-align: center;">
                      <div>
                        <el-button @click="toPrice()" type="primary" :disabled="price.isPriceSheet">去报价</el-button>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-divider></el-divider>
              </template>

              <template v-if="price.isDownOrderNode">
                <!-- 去下单 -->
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="" style="text-align: center;">
                      <div>
                        <el-button @click="downOrder()" type="primary" :disabled="price.isDownOrder">去下单</el-button>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-divider></el-divider>
              </template>
            </template>


            <template v-if="orderType != '0' && order_type != '0'">
              <template v-if="beNotInUse.isShow">
                <!-- 归档 -->
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="" style="text-align: center;">
                      <div>
                        <el-button v-if="beNotInUse.isDisabled == '5'" @click="beNotInUseSubmit()" type="primary"
                          :disabled="true">已归档</el-button>
                        <el-button v-else-if="beNotInUse.isDisabled == '2'" @click="beNotInUseSubmit()" type="primary"
                          :disabled="false">归档</el-button>
                        <el-button v-else @click="beNotInUseSubmit()" type="primary" :disabled="true">归档</el-button>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
                <!-- <el-divider></el-divider> -->
              </template>
            </template>


            <template v-if="form.flowNodeVo != null">

              <el-row v-if="form.flowNodeVo.nodeType == '0'">
                <el-col :span="24">
                  <el-form-item label="审批" prop="auditStatus" style="text-align: center;">
                    <img src="../../../assets/images/audit.jpg" width="150" height="150" />
                    <template v-if="form.nodeStatus == '5'">
                      <div v-if="$store.state.user.id == form.createId">
                        <el-button @click="handelOrderNodeAudit('1')">重新提交</el-button>
                      </div>
                      <div v-else>{{ form.nodeStatus == '1' ? '待审核' : (form.nodeStatus == '2' ? '已通过' : '审核未通过') }}
                      </div>
                    </template>
                    <template v-else>
                      <div
                        v-if="(checkRole(['admin']) || $store.state.user.id == form.userId) && form.nodeStatus == '1'">
                        <el-button @click="handelOrderNodeAudit('2')" type="primary">通过</el-button>
                        <el-button @click="handelOrderNodeAudit('5')">拒绝</el-button>
                      </div>
                      <div v-else>{{ form.nodeStatus == '1' ? '待审核' : (form.nodeStatus == '2' ? '已通过' : '审核未通过') }}
                      </div>
                    </template>

                  </el-form-item>
                </el-col>
              </el-row>
              <!-- 开票记录任务 -->
              <el-row v-else-if="form.flowNodeVo.nodeType == '1'">
                <el-col :span="24">
                  <invoice :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
                    :userId="form.userId" :nodeStatus="form.nodeStatus" :principal="form.principal"
                    @handleLater="handleLater"></invoice>
                </el-col>
              </el-row>
              <!-- 收款记录任务 -->
              <el-row v-else-if="form.flowNodeVo.nodeType == '2'">
                <el-col :span="24">
                  <collection :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
                    :userId="form.userId" :nodeStatus="form.nodeStatus" :principal="form.principal"
                    @handleLater="handleLater">
                  </collection>
                </el-col>
              </el-row>
              <!-- 状态记录任务 -->
              <el-row v-else-if="form.flowNodeVo.nodeType == '3'">
                <el-col :span="24">
                  <el-form-item label="任务状态" prop="nodeStatus">
                    <el-radio-group @change="nodeStatusChange" v-model="form.nodeStatus"
                      :disabled="(!checkRole(['admin']) && form.userId != $store.state.user.id && form.principal != $store.state.user.id) || ['-1', '2', '0'].includes(form.nodeStatus)">
                      <el-radio v-for="dict in dict.type.node_status" :key="dict.value" :label="dict.value"
                        :disabled="dict.value == '0' || dict.value == '1' || dict.value == '3'">{{ dict.label
                        }}</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- 数量记录任务 -->
              <el-row v-else-if="form.flowNodeVo.nodeType == '4'">
                <el-col :span="24">
                  <materiel :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
                    :userId="form.userId" :nodeStatus="form.nodeStatus" :principal="form.principal"
                    @handleLater="handleLater"></materiel>
                </el-col>
              </el-row>
              <!-- 子流程任务 -->
              <el-row v-else-if="form.flowNodeVo.nodeType == '7'">
                <el-col :span="24">
                  <order-child-process @closeOpen="closeOpen" :orderId="form.orderId" :nodeId="form.nodeId"
                    :orderNodeId="form.orderNodeId" :orderForm="orderForm" :orderNodeForm="form" :userId="form.userId"
                    :nodeStatus="form.nodeStatus" :orderType="order_type" :principal="form.principal"
                    :templateId="form.templateId"></order-child-process>
                </el-col>
              </el-row>

            </template>
            <el-divider></el-divider>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注" prop="nodeRemark">
                  <el-input v-model="form.nodeRemark" type="textarea" placeholder="请输入内容" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-divider></el-divider>
            <el-row>
              <el-col :span="24">
                <el-form-item label="动态" prop="dynamicContent">
                  <el-input v-model="dynamicForm.dynamicContent" type="textarea" placeholder="请输入内容" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="18">
                <el-form-item>
                  <file-upload-button v-model="dynamicForm.attachments"></file-upload-button>
                </el-form-item>
              </el-col>
              <el-col :span="2">
                <el-form-item>
                  <el-button @click="submitDynamic">发布</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <order-dynamic ref="orderDynamicRef" :orderId="form.orderId"></order-dynamic>
        </el-tab-pane>

        <el-tab-pane label="订单详情" name="order">
          <sales-order-details :orderId="form.orderId"
            v-if="orderType == '1' || order_type == '1'"></sales-order-details>
          <market-order-details :orderId="form.orderId"
            v-if="orderType == '2' || order_type == '2'"></market-order-details>
          <price-sheet-order-details :orderId="form.orderId"
            v-if="orderType == '0' || order_type == '0'"></price-sheet-order-details>
        </el-tab-pane>

        <el-tab-pane label="任务记录及计划" v-if="['1', '2', '4'].includes(form.flowNodeVo.nodeType)">
          <!-- 开票记录任务 -->
          <template v-if="form.flowNodeVo.nodeType == '1'">
            <order-invoice-record :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
              :userId="form.userId" :nodeStatus="form.nodeStatus"></order-invoice-record>
            <order-invoice-plan :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
              :userId="form.userId" :nodeStatus="form.nodeStatus"></order-invoice-plan>
          </template>

          <!-- 收款金额纪录任务 -->
          <template v-else-if="form.flowNodeVo.nodeType == '2'">
            <order-collection-record :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
              :userId="form.userId" :nodeStatus="form.nodeStatus"></order-collection-record>
            <order-collection-plan :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
              :userId="form.userId" :nodeStatus="form.nodeStatus"></order-collection-plan>
          </template>

          <!-- 数量记录任务 -->
          <template v-else-if="form.flowNodeVo.nodeType == '4'">
            <order-materiel-record :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
              :userId="form.userId" :nodeStatus="form.nodeStatus"></order-materiel-record>
            <order-materiel-plan :orderId="form.orderId" :nodeId="form.nodeId" :orderNodeId="form.orderNodeId"
              :userId="form.userId" :nodeStatus="form.nodeStatus"></order-materiel-plan>
          </template>
        </el-tab-pane>
      </el-tabs>
    </el-drawer>

    <!-- 工序流转卡  -->
    <productprint ref="productprintref"></productprint>
    <!-- 首件确认单  -->
    <firstarticle ref="firstarticleref"></firstarticle>
    <!-- 领料单 -->
    <pickingprint ref="pickingref"></pickingprint>
    <!-- 成品入库单 -->
    <porductnprint ref="porductInRef"></porductnprint>
  </div>
</template>

<script>
import {
  getOrderNode,
  complateNode,
  submitRemark
} from "@/api/order/orderNode";

import {
  listAllUser
} from "@/api/system/user";

import {
  addOrderDynamic
} from "@/api/order/orderDynamic";

import {
  nodeAudit
} from "@/api/order/orderExamine";

import {
  listPriceSheetOrderRecordAll
} from "@/api/order/priceSheetOrderRecord";

import orderInvoiceRecord from "@/views/order/orderInvoiceRecord/index"
import orderInvoicePlan from "@/views/order/orderInvoicePlan/index"
import invoice from "@/views/order/orderInvoicePlan/invoice"

import orderCollectionRecord from "@/views/order/orderCollectionRecord/index"
import orderCollectionPlan from "@/views/order/orderCollectionPlan/index"
import collection from "@/views/order/orderCollectionPlan/collection"

import orderMaterielRecord from "@/views/order/orderMaterielRecord/index"
import orderMaterielPlan from "@/views/order/orderMaterielPlan/index"
import materiel from "@/views/order/orderMaterielPlan/materiel"

import orderChildProcess from "@/views/order/orderChildProcess/index"

import salesOrderDetails from "@/views/order/salesOrder/details"
import marketOrderDetails from "@/views/order/marketOrder/details"
import priceSheetOrderDetails from "@/views/order/priceSheetOrder/details"

import orderDynamic from "@/views/order/orderDynamic/index"

// 工序流转卡
import productprint from '@/views/order/print/product'
import firstarticle from '@/views/order/print/firstarticle'
import pickingprint from '@/views/order/print/picking'
import porductnprint from '@/views/order/print/porductIn'

import {
  checkPermi,
  checkRole
} from "@/utils/permission"; // 权限判断函数

export default {
  name: "OrderNodeInfo",
  dicts: ['node_status'],
  props: ['isEdit', 'orderForm', 'orderType'],
  components: {
    salesOrderDetails,
    marketOrderDetails,
    priceSheetOrderDetails,
    orderDynamic,
    orderInvoiceRecord,
    orderInvoicePlan,
    invoice,
    orderCollectionRecord,
    orderCollectionPlan,
    collection,
    orderMaterielRecord,
    orderMaterielPlan,
    materiel,
    orderChildProcess,
    productprint,
    firstarticle,
    pickingprint,
    porductnprint
  },
  data() {
    return {
      beNotInUse: {
        isShow: false,
        isDisabled: "1",
      },
      order_type: null,
      price: {//报价数据
        isToPriceNode: false, //是否是报价节点 去报价按钮显示隐藏
        isDownOrderNode: false,//是否是下单节点 去下单按钮显示隐藏
        title: null,//去报价标题
        isPriceSheet: false,//当前是否可报价 去下单按钮是否禁用
        isDownOrder: false,//去下单是否禁用
      },
      // 是否显示弹出层
      open: false,
      // 标签页选中标识
      activeName: 'node',
      // 表单参数
      form: {
        flowNodeVo: {}
      },
      // 动态表单参数
      dynamicForm: {},
      // 表单校验
      rules: {},
      // 用户列表
      userList: [],
      //主流程模板ID
      templateId: null
    }
  },
  methods: {
    //返回备注
    handleLater() {
      let data = {
        orderId: this.form.orderId,
        orderNodeId: this.form.orderNodeId,
        remark: this.form.nodeRemark
      }
      submitRemark(data).then(res => {
        this.closeAndRefresh();
      })
    },
    //归档按钮的显示隐藏 禁用
    setBeNotInUse(orderId, isDisabled) {
      this.beNotInUse = {
        isShow: true,
        isDisabled: isDisabled
      }
      this.form.orderId = orderId;
      this.form.flowNodeVo.nodeName = "完成";
    },

    //报价节点点击事件
    flowPriceSheet(orderId, isPriceSheet) {
      this.price = {//报价数据
        isToPriceNode: true, //是否是报价节点 去报价按钮显示隐藏
        isDownOrderNode: false,//是否是下单节点 去下单按钮显示隐藏
        title: null,//去报价标题
        isPriceSheet: isPriceSheet,//当前是否可报价 报价按钮是否禁用
        isDownOrder: true,//去下单是否禁用
      }
      this.form.orderId = orderId;
      this.form.flowNodeVo.nodeName = "报价";
    },

    //下单节点点击事件
    flowDownOrder(orderId, isDownOrder) {
      this.price = {//报价数据
        isToPriceNode: false, //是否是报价节点 去报价按钮显示隐藏
        isDownOrderNode: true,//是否是下单节点 去下单按钮显示隐藏
        title: null,//去报价标题
        isPriceSheet: true,//当前是否可报价 报价按钮是否禁用
        isDownOrder: isDownOrder,//去下单是否禁用
      }
      this.form.orderId = orderId;
      this.form.flowNodeVo.nodeName = "完成";
    },

    resetPrice() {
      this.price = {//报价数据
        isToPriceNode: false, //是否是报价节点 去报价按钮显示隐藏
        isDownOrderNode: false,//是否是下单节点 去下单按钮显示隐藏
        title: null,//去报价标题
        isPriceSheet: true,//当前是否可报价 报价按钮是否禁用
        isDownOrder: true,//去下单是否禁用
      }
    },

    //归档按钮的显示隐藏 禁用
    resetBeNotInUse() {
      this.beNotInUse = {
        show: false,
        isDisabled: true,
      }
    },

    //归档按钮点击事件
    beNotInUseSubmit() {
      console.log(this.form);
      this.$emit("beNotInUseSubmit", this.form.orderId, this.order_type, this.form.childId);
      this.open = false;
    },

    //跳转到报价单
    toPrice() {
      this.open = false;
      if (this.orderType == '0' || this.order_type == '0') {
        this.$emit("toUpdatePrice", this.form.orderId, this.order_type, this.templateId);
      }

    },
    //去下单
    downOrder() {
      this.open = false;
      this.$emit("downOrder", this.form.orderId, this.order_type, this.templateId);
    },

    //获取报价次数
    getPriceRecordNum(orderId) {
      this.price.orderId = orderId;
      let data = {
        orderId: orderId
      }
      listPriceSheetOrderRecordAll(data).then(res => {
        this.price.title = "已报价" + res.data.length + "次"
      })
    },

    checkRole,
    /**
     * 修改节点状态
     */
    nodeStatusChange() {
      let nodeData = {
        orderId: this.form.orderId,
        orderNodeId: this.form.orderNodeId,
        nodeType: "3",
        nodeRemark: this.form.nodeRemark
      }
      complateNode(nodeData).then(res => {
        this.$notify({
          title: '成功',
          message: '操作成功',
          type: 'success'
        });
        this.closeAndRefresh();
      })
    },
    //关闭当前页并刷新父页
    closeAndRefresh() {
      this.$emit("refresh");
      this.open = false;
    },
    /**
     * 订单审批
     */
    handelOrderNodeAudit(auditStatus) {
      const data = {
        orderId: this.form.orderId,
        auditStatus: auditStatus,
        childId: this.form.childId,
        nodeRemark: this.form.nodeRemark
      };
      nodeAudit(data).then(res => {
        this.$notify({
          title: '成功',
          message: '操作成功',
          type: 'success'
        });
        // this.open = false;
        // const orderId = this.$route.query.orderId;
        // this.$emit('getList', orderId)
        this.closeAndRefresh();
      })
    },
    /**
     * 发布动态
     */
    submitDynamic() {
      this.dynamicForm.orderId = this.form.orderId;
      this.dynamicForm.orderNodeId = this.form.orderNodeId;
      this.dynamicForm.nodeId = this.form.nodeId;
      addOrderDynamic(this.dynamicForm).then(res => {
        this.$notify({
          title: '成功',
          message: '发布成功',
          type: 'success'
        });
        this.$refs.orderDynamicRef.getList();
        this.resetDynamicForm();
        this.$emit('getOrderDynamicList')
      })
    },
    /**
     * 获取用户列表
     */
    getUserList() {
      listAllUser().then(res => {
        this.userList = res.data
      })
    },
    // 表单重置
    resetDynamicForm() {
      this.dynamicForm = {
        orderId: null,
        orderNodeId: null,
        nodeId: null,
        userId: null,
        dynamicContent: null,
        attachments: null
      };
    },
    reset() {
      this.form = {
        orderNodeId: null,
        orderTemplateId: null,
        orderId: null,
        templateId: null,
        nodeId: null,
        deptId: null,
        userId: null,
        principal: null,
        complateDate: null,
        nodeStatus: null,
        nodeRemark: null,
        sort: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        flowNodeVo: {}
      };
      this.resetForm("form");
      this.resetDynamicForm();
    },
    /**
     * 获取订单节点信息
     */
    getOrderNodeInfo(orderNodeId) {
      this.reset();
      getOrderNode(orderNodeId).then(res => {
        this.form = res.data;
        if (res.data.complateDate && res.data.flowNodeVo.nodeType == '3' && res.data.nodeStatus == '0') {
          const complateDate = new Date(res.data.complateDate);
          const newDate = new Date(Date.now());
          if (newDate > complateDate) {
            this.form.nodeStatus = '3';
          }
        }
      })
    },
    /**
     * 打开弹框
     */
    hanldeOpen(orderNodeId, orderType, templateId) {
      this.templateId = templateId;
      this.reset();
      this.open = true;
      if (orderNodeId != null && orderNodeId != '') {
        this.getOrderNodeInfo(orderNodeId);
      }
      if (orderType) {
        this.order_type = orderType;
      }
      this.getUserList();
    },
    /**
     * 关闭弹框
     */
    closeOpen() {
      this.open = false;
    },
    // 打印
    handleDropdownClick(e) {
      // 打印工序流转卡
      if (e == 1) {
        this.$refs.productprintref.OnClick(this.form.orderId)
      }
      // 首件确认单
      if (e == 2) {
        this.$refs.firstarticleref.OnClick(this.form.orderId)
      }
      // 领料单
      if (e == 3) {
        this.$refs.pickingref.OnClick(this.form.orderId)
      }

      // 领料单
      if (e == 4) {
        this.$refs.porductInRef.OnClick(this.form.orderId)
      }

    }
  }
}
</script>

<style></style>
