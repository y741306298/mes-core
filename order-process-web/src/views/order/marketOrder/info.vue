<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">
        <span>{{ form.orderNo }}</span>
        <span v-if="form.user" class="marketman ml10">业务员: {{ form.user.nickName }}</span>
      </div>

      <!-- <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
        操作
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="add" v-hasPermi="['order:marketOrder:add']">新增采购单</el-dropdown-item>
          <el-dropdown-item command="export" v-hasPermi="['order:marketOrder:export']">导出</el-dropdown-item>
          <el-dropdown-item command="print" v-hasPermi="['order:marketOrder:print']">打印</el-dropdown-item>
          <el-dropdown-item command="delete" v-hasPermi="['order:marketOrder:remove']">删除</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown> -->

    </div>

    <div style="display: flex;overflow-x: scroll;padding: 10px 0;">

      <div v-for="(node, nodeIndex) in orderNodeList" :key="nodeIndex">
        <!-- 首个 -->
        <div v-if="nodeIndex == 0">
          <div class="arrow-first">
            <div
              :class="[!['3', '2'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||node.nodeStatus == '3') ? 'first-center-refuse' : 'first-center') : 'first-center-active']">
              <el-button type="text" slot="title" @click="handleNodeClick(node)">
                <div style="color: white;">{{ node.flowNodeVo.nodeName }}</div>
              </el-button>
            </div>
            <div
              :class="[!['3', '2'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||node.nodeStatus == '3') ? 'first-right-refuse' : 'first-right') : 'first-right-active']">
            </div>
          </div>

          <!-- // -->
          <div style="font-size: 12px;" v-if="!['2', '3'].includes(node.nodeStatus)">
            <div class="mt5" v-if="!node.nodePrincipal && !node.userId">未指派</div>
            <div v-else>
              <!-- 负责人  -->
              <div v-if="node.nodePrincipal">
                {{ node.nodePrincipalVo.userName }}{{ parseTime(node.complateDate, '{m}月{d}日') }}
              </div>
              <!-- 执行人 -->
              <div class="mt5" v-else>
                {{ node.user.userName }}{{ parseTime(node.complateDate, '{m}月{d}日') }}
              </div>
            </div>
          </div>

          <div style="font-size: 12px;" v-else>
            <div class="mt5" v-if="node.nodePrincipal">
              {{ node.nodePrincipalVo.userName }}{{ parseTime(node.updateTime, '{m}月{d}日') }}</div>
            <div class="mt5" v-else>{{ node.updateBy }}{{ parseTime(node.updateTime, '{m}月{d}日') }}</div>
          </div>
          <!-- // -->
        </div>

        <!-- 中间 -->
        <div
          v-if="(!orderTemplate.childId && nodeIndex > 0) || (orderTemplate.childId && nodeIndex > 0 && nodeIndex < orderNodeList.length - 1)">
          <div class="arrow">
            <div :class="[!['3', '2'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'arrow-left-refuse' : 'arrow-left') : 'arrow-left-active']"></div>
            <div :class="[!['3', '2'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'arrow-center-refuse' : 'arrow-center') : 'arrow-center-active']">
              <el-button type="text" slot="title" @click="handleNodeClick(node, node.orderType)">
                <div style="color: white;">{{ node.flowNodeVo.nodeName }}</div>
              </el-button>
            </div>
            <div :class="[!['3', '2'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'arrow-right-refuse' : 'arrow-right') : 'arrow-right-active']"></div>
          </div>

          <div style="font-size: 12px;" v-if="!['2', '3'].includes(node.nodeStatus)">
            <div class="mt5" v-if="!node.nodePrincipal && !node.userId">未指派</div>
            <div v-else>
              <!-- 负责人  -->
              <div v-if="node.nodePrincipal">
                {{ node.nodePrincipalVo.userName }}{{ parseTime(node.complateDate, '{m}月{d}日') }}
              </div>
              <!-- 执行人 -->
              <div class="mt5" v-else>
                {{ node.user.userName }}{{ parseTime(node.complateDate, '{m}月{d}日') }}
              </div>
            </div>
          </div>

          <div style="font-size: 12px;" v-else>
            <div class="mt5" v-if="node.nodePrincipal">
              {{ node.nodePrincipalVo.userName }}{{ parseTime(node.updateTime, '{m}月{d}日') }}</div>
            <div class="mt5" v-else>{{ node.updateBy }}{{ parseTime(node.updateTime, '{m}月{d}日') }}</div>
          </div>
          <!-- // -->


        </div>

      </div>

      <!-- 最后 -->
      <div v-if="(orderTemplate.childId && nodeIndex == orderNodeList.length - 1)">
        <div class="arrow-last">
          <div :class="[['3', '2'].includes(node.nodeStatus) ? 'last-left-active' : ((compareDates(node.complateDate)&& node.nodeStatus =='0')?'last-left-refuse':'last-left')]"></div>
          <div :class="[['3', '2'].includes(node.nodeStatus) ? 'last-center-active' : ((compareDates(node.complateDate)&& node.nodeStatus =='0')?'last-center-refuse':'last-center')]">
            <el-button type="text" slot="title" @click="handleNodeClick(node, node.orderType)">
              <div style="color: white;">{{ node.flowNodeVo.nodeName }}</div>
            </el-button>
          </div>
          <div class="last-right"></div>
        </div>


        <div style="font-size: 12px;" v-if="!['2', '3'].includes(node.nodeStatus)">
            <div class="mt5" v-if="!node.nodePrincipal && !node.userId">未指派</div>
            <div v-else>
              <!-- 负责人  -->
              <div v-if="node.nodePrincipal">
                {{ node.nodePrincipalVo.userName }}{{ parseTime(node.complateDate, '{m}月{d}日') }}
              </div>
              <!-- 执行人 -->
              <div class="mt5" v-else>
                {{ node.user.userName }}{{ parseTime(node.complateDate, '{m}月{d}日') }}
              </div>
            </div>
          </div>

          <div style="font-size: 12px;" v-else>
            <div class="mt5" v-if="node.nodePrincipal">
              {{ node.nodePrincipalVo.userName }}{{ parseTime(node.updateTime, '{m}月{d}日') }}</div>
            <div class="mt5" v-else>{{ node.updateBy }}{{ parseTime(node.updateTime, '{m}月{d}日') }}</div>
          </div>


      </div>

      <!-- 最后 -->
      <div>
        <div class="arrow-last" @click="beNotInUse()">
          <div :class="[orderTemplate.status == '5' ? 'last-left-active' : 'last-left']"></div>
          <div :class="[orderTemplate.status == '5' ? 'last-center-active' : 'last-center']">
            <el-button type="text" slot="title">
              <div style="color: white;">归档</div>
            </el-button>
          </div>
          <div class="last-right"></div>
        </div>
        <div style="font-size: 12px;">
          <div class="mt5">归档</div>
        </div>
      </div>

    </div>

    <market-order-details :orderId="form.orderId"></market-order-details>
    <el-row>
      <el-col :span="12">
        <order-dynamic ref="orderDynamicRef" :orderId="form.orderId"></order-dynamic>
      </el-col>
    </el-row>

    <!-- 订单节点详情 -->
    <order-node-info @getOrderDynamicList="getOrderDynamicList" @getList="getOrderNodeList" :isEdit="true"
      :orderForm="form" ref="orderNodeInfoRef" orderType="2" @beNotInUseSubmit="beNotInUseSubmit"
      @refresh="onLoad"></order-node-info>

    <!-- 打印 -->
    <print ref="printRef"></print>

  </div>
</template>

<script>
import {
  listMarketOrder,
  listMarketOrderAll,
  getMarketOrder,
  delMarketOrder,
  addMarketOrder,
  updateMarketOrder,
  beNotInUseSubmit
} from "@/api/order/marketOrder";

import {
  getOrderTempleatAndOrderNode
} from "@/api/order/orderTemplate";

import marketOrderDetails from "@/views/order/marketOrder/details"
import orderDynamic from "@/views/order/orderDynamic/index"
import orderNodeInfo from "@/views/order/orderNode/info"
import print from "@/views/order/marketOrder/print"

export default {
  name: "MarketOrderInfo",
  components: {
    marketOrderDetails,
    orderDynamic,
    orderNodeInfo,
    print
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      //订单流程
      orderTemplate: {
      },
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 销售单表格数据
      marketOrderList: [],
      // 表单信息
      form: {
        // 业务员信息
        user: {}
      },
      // 节点列表
      orderNodeList: []
    };
  },

  watch: {
    '$route': {
      handler: function (to, form) {
        this.onLoad();
      },
      immediate: true
    }
  },

  created() {
    this.onLoad();
  },
  methods: {

    onLoad() {
      const orderId = this.$route.query.orderId;
      this.getOrderInfo(orderId);
      this.getOrderNodeList(orderId);
    },

    /**
     * 流程归档
     */
    beNotInUse() {
      this.$refs.orderNodeInfoRef.hanldeOpen(null, "2", this.form.templateId);
      let _this = this;
      this.$nextTick(() => {
        _this.$refs.orderNodeInfoRef.setBeNotInUse(this.form.orderId, _this.orderTemplate.status);
      })

    },
    /**
     * 提交归档
     */
    beNotInUseSubmit() {
      beNotInUseSubmit(
        {
          orderId: this.form.orderId,
          childId: this.orderTemplate.childId
        }
      )
    },
    /**
     * 获取动态信息列表
     */
    getOrderDynamicList() {
      this.$refs.orderDynamicRef.getList();
    },
    // 打印
    handlePrint() {
      this.$refs.printRef.handleOpen(this.form.orderId);
    },
    /** 删除按钮操作 */
    handleDelete() {
      const orderIds = this.form.orderId;
      this.$modal.confirm('确认删除？').then(function () {
        return delMarketOrder(orderIds);
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/marketOrder/export', {
        ...this.queryParams
      }, `marketOrder_${new Date().getTime()}.xlsx`)
    },
    /**
     * 批量操作按钮点击事件
     */
    handleDropdownClick(command) {
      switch (command) {
        case 'add':
          this.$router.push({
            path: '/marketOrder/add/index',
          });
          break;
        case 'export':
          this.handleExport();
          break;
        case 'delete':
          this.handleDelete();
          break;
        case 'print':
          this.handlePrint();
          break;
      }
    },
    /**
     * 节点点击事件
     */
    handleNodeClick(orderNode) {
      this.$refs.orderNodeInfoRef.hanldeOpen(orderNode.orderNodeId, "2", this.form.templateId);
      this.$refs.orderNodeInfoRef.resetBeNotInUse();
    },
    /**
     * 获取订单节点列表
     */
    getOrderNodeList() {
      const orderId = this.$route.query.orderId;
      const templateId = this.$route.params.templateId;
      const childId = this.$route.query.childId;
      getOrderTempleatAndOrderNode({
        orderId: orderId,
        templateId: templateId,
        childId: childId ? childId : "1",
        isFilterVoid: 'Y'
      }).then(res => {
        this.orderTemplate = res.data.orderTemplate;
        this.orderNodeList = res.data.orderNodeList.slice().sort((a, b) => a.sort - b.sort);
      })
    },
    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        orderNo: new Date().getTime(),
        customerId: null,
        contact: null,
        contactTel: null,
        customerAddressId: null,
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
    },
    /** 获取订单详情 */
    getOrderInfo(orderId) {
      this.reset();
      getMarketOrder(orderId).then(response => {
        this.form = response.data;
      });
    },
    compareDates(dateStr2) {
      if(dateStr2 && dateStr2 != null){
        let date1 = new Date();
        let date2 = new Date(dateStr2);
        return date1.getTime() - date2.getTime();
      }
      return false;
    }

  }
};
</script>
<style>
.marketman {
  font-size: 12px !important;
  color: #b1b1b1;
  font-weight: 500;
}
</style>

<style lang="scss">
/* 第一个 */
.arrow-first {
  display: flex;
}

.first-center {
  width: 100px;
  background-color: #cbcdd4;
  text-align: center;
}

.first-center-active {
  width: 100px;
  background-color: #70eaa9;
  text-align: center;
}



.first-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}

.first-right-active {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #70eaa9;
}


/* 中间 */
.arrow {
  display: flex;
  margin-left: -25px;
}

.arrow-left {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.arrow-left-active {
  border-width: 19px;
  border-style: solid;
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}


.arrow-center {
  width: 100px;
  background-color: #cbcdd4;
  text-align: center;
}

.arrow-center-active {
  width: 100px;
  background-color: #70eaa9;
  text-align: center;
}



.arrow-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}

.arrow-right-active {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #70eaa9;
}

/* 最后 */
.arrow-last {
  display: flex;
  margin-left: -25px;
}

.last-left {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.last-left-active {
  border-width: 19px;
  border-style: solid;
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}

.last-center {
  width: 100px;
  background-color: #cbcdd4;
  text-align: center;
}

.last-center-active {
  width: 100px;
  background-color: #70eaa9;
  text-align: center;
}

.last-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent transparent;
}

.last-right-active {
  width: 50px;
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent transparent;
}

.arrow-right-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #ca5f41;
}

.arrow-center-refuse {
  width: 100px;
  background-color: #ca5f41;
  text-align: center;
}

.arrow-left-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: #ca5f41 #ca5f41 #ca5f41 transparent;
}

.first-center-refuse {
  width: 100px;
  background-color: #ca5f41;
  text-align: center;
}

.first-right-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #ca5f41;
}

.person {
  font-size: 15px;
  position: relative;
  top: 5px;
}

.last-center-refuse {
  width: 100px;
  background-color: #ca5f41;
  text-align: center;
}

.last-left-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: #ca5f41 #ca5f41 #ca5f41 transparent;
}
</style>
