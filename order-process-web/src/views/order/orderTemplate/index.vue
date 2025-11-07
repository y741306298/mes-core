<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入入库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="inInventoryStatus">
        <el-select v-model="queryParams.flowQueryAudit" placeholder="请选择">
          <el-option v-for="dict in dict.type.flow_query_audit" :key="dict.value" :label="dict.label" :value="dict.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleSendWork"
          :disabled="!selectOrderTemplateId" v-hasPermi="['order:orderTemplate:sendWork']">派工</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
          批量操作
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="copy" :disabled="!selectOrderTemplateId"
              v-hasPermi="['order:orderTemplate:copy']">新增加工单</el-dropdown-item>
            <el-dropdown-item command="stop" :disabled="!selectOrderTemplateId"
              v-hasPermi="['order:orderTemplate:stop']">停止加工</el-dropdown-item>
            <el-dropdown-item command="start" :disabled="!selectOrderTemplateId"
              v-hasPermi="['order:orderTemplate:start']">恢复加工</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>

    </el-row>

    <el-empty description="暂无数据" v-if="orderTemplateList == null || orderTemplateList.length <= 0"></el-empty>

    <div v-else @click="handleTemplateClick(item)" v-for="(item, index) in orderTemplateList"
      :class="selectOrderTemplateId == item.orderTemplateId ? 'marquee-border' : ''">
      <el-card class="box-card mb20 order-template" shadow="hover" v-loading="item.orderTemplateStatus == '2'"
        element-loading-text="已作废" element-loading-spinner="el-icon-close"
        element-loading-background="rgba(0, 0, 0, 0.6)">
        <div slot="header" class="clearfix" style="display: flex;">
          <el-button type="text" @click="handleToOrderDetails(item)">{{ item.orderNo }}</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" v-if="item.customerVo">{{ item.customerVo.customerName }}</el-button>
          <el-button type="text" v-if="item.supplierVo">{{ item.supplierVo.supplierName }}</el-button>
          <el-divider direction="vertical"></el-divider>

          <el-tooltip placement="top" v-if="item.detailsDesc != null">
            <div slot="content">
              <div v-for="(item, index) in item.detailsDesc.split(',')">{{ item }}</div>
            </div>
            <span>{{ item.detailsDesc.split(',')[0] }}</span>
          </el-tooltip>

          <div class="ml20 mr20">
            <span class="person">负责人：</span>
            <el-select size="mini" disabled v-model="item.flowTemplateVo.userId" placeholder="请选择负责人" filterable>
              <el-option v-for="(item, index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </div>



          <div class="ml20 mr20">
            <span class="person">审核人：</span>
            <el-select size="mini" disabled v-model="item.userId" placeholder="请选择业务员" filterable clearable>
              <el-option v-for="(item, index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </div>


        </div>

        <div style="display: flex;overflow-x: scroll;padding-bottom: 20px;">

          <template v-if="(item.orderType == '0' && !item.childId)">
            <div>
              <div class="arrow-first">
                <div class="first-center-active">
                  <el-button type="text" slot="title" @click="priceSheetNodeClick(item)">
                    <div style="color: aliceblue;">报价</div>
                  </el-button>
                </div>
                <div class="first-right-active"></div>
              </div>
              <div style="font-size: 12px;">
                <div class="mt5">
                  报价
                </div>
              </div>
            </div>
          </template>

          <div v-if="node.flowNodeVo != null" v-for="(node, nodeIndex) in item.orderNodeVoList" :key="nodeIndex">
            <!-- 首个 -->
            <div
              v-if="((nodeIndex == 0 && item.orderType != '0' && !item.childId) || (item.childId && nodeIndex == 0))">
              <div class="arrow-first">
                <div
                  :class="[!['2', '3'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'first-center-refuse' : 'first-center') : 'first-center-active']">
                  <el-button type="text" slot="title" @click="handleNodeClick(node, item.orderType)">
                    <div style="color: white;">{{ node.flowNodeVo.nodeName }}</div>
                  </el-button>
                </div>
                <div
                  :class="[!['2', '3'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'first-right-refuse' : 'first-right') : 'first-right-active']">
                </div>
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

            <!-- 中间 -->
            <div
              v-if="((nodeIndex > 0 && nodeIndex < item.orderNodeVoList.length - 1 && node.childId) || (!node.childId && ((nodeIndex > 0 && item.orderType != '0') || (nodeIndex >= 0 && item.orderType == '0'))))">
              <div class="arrow">
                <div
                  :class="[!['2', '3'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'arrow-left-refuse' : 'arrow-left') : 'arrow-left-active']">
                </div>
                <div
                  :class="[!['2', '3'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'arrow-center-refuse' : 'arrow-center') : 'arrow-center-active']">
                  <el-button type="text" slot="title" @click="handleNodeClick(node, item.orderType)">
                    <div style="color: white;">{{ node.flowNodeVo.nodeName }}</div>
                  </el-button>
                </div>
                <div
                  :class="[!['2', '3'].includes(node.nodeStatus) ? ((node.nodeStatus == '5'||(compareDates(node.complateDate)&& node.nodeStatus =='0')) ? 'arrow-right-refuse' : 'arrow-right') : 'arrow-right-active']">
                </div>
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

            <!-- <template v-if="item.childId"> -->
            <div v-if="(node.childId && nodeIndex == item.orderNodeVoList.length - 1)">
              <div class="arrow-last">
                <div :class="[['2', '3'].includes(node.nodeStatus) ? 'last-left-active' : ((compareDates(node.complateDate)&& node.nodeStatus =='0')?'last-left-refuse':'last-left')]"></div>
                <div :class="[['2', '3'].includes(node.nodeStatus) ? 'last-center-active' : ((compareDates(node.complateDate)&& node.nodeStatus =='0')?'last-center-refuse':'last-center')]">
                  <el-button type="text" slot="title" @click="handleNodeClick(node, item.orderType)">
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
            <!-- </template> -->

          </div>


          <template v-if="!item.childId">
            <!-- 下单 -->
            <template v-if="item.orderType == '0'">
              <div>
                <div class="arrow-last" @click="downOrderNodeClick(item)">
                  <div :class="[item.status == '2' ? 'last-left-active' : 'last-left']"></div>
                  <div :class="[item.status == '2' ? 'last-center-active' : 'last-center']">
                    <el-button type="text" slot="title">
                      <div style="color: white;">完成</div>
                    </el-button>
                  </div>
                  <div class="last-right"></div>
                </div>
                <div style="font-size: 12px;">
                  <div class="mt5">完成</div>
                </div>
              </div>
            </template>

            <!-- 归档 -->
            <template v-if="['1', '2'].includes(item.orderType)">
              <div>
                <div class="arrow-last" @click="beNotInUse(item)">
                  <div :class="[item.status == '5' ? 'last-left-active' : 'last-left']"></div>
                  <div :class="[item.status == '5' ? 'last-center-active' : 'last-center']">
                    <el-button type="text" slot="title">
                      <div style="color: white;">完成</div>
                    </el-button>
                  </div>
                  <div class="last-right"></div>
                </div>
                <div style="font-size: 12px;">
                  <div class="mt5">完成</div>
                </div>
              </div>
            </template>
          </template>
        </div>
      </el-card>
    </div>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />


    <!-- 派工弹框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="订单编号" prop="orderNum">
              <el-input v-model="form.orderNo" disabled placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单日期" prop="orderDate">
              <el-date-picker clearable v-model="form.orderDate" disabled type="date" value-format="yyyy-MM-dd"
                placeholder="请选择订单日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交货日期" prop="deliveryDate">
              <el-date-picker clearable v-model="form.deliveryDate" disabled type="date" value-format="yyyy-MM-dd"
                placeholder="请选择交货日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="form.orderNodeVoList" border>
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="步骤" prop="flowNodeVo.nodeName" width="200" />
          <el-table-column label="执行部门" width="200">
            <template slot-scope="scope">
              <el-select v-model="scope.row.deptId" :disabled="scope.row.flowNodeVo.nodeType == '0'"
                @change="deptChange(scope.$index)" filterable>
                <el-option v-for="(item, index) in deptList" :value="item.deptId.toString()"
                  :label="item.deptName"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="执行人员" width="200">
            <template slot-scope="scope">
              <el-select v-model="scope.row.userId" :disabled="scope.row.flowNodeVo.nodeType == '0'" filterable>
                <el-option v-for="(item, index) in scope.row.userList" :value="item.userId.toString()"
                  :label="item.userName"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="负责人" width="200">
            <template slot-scope="scope">
              <el-select v-model="scope.row.nodePrincipal" :disabled="scope.row.flowNodeVo.nodeType == '0'" filterable>
                <el-option v-for="(item, index) in scope.row.userList" :value="item.userId.toString()"
                  :label="item.userName"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="完成时间">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.complateDate" :disabled="scope.row.flowNodeVo.nodeType == '0'"
                style="width: 185px;" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 订单节点详情 -->
    <order-node-info :isEdit="true" ref="orderNodeInfoRef" @beNotInUseSubmit="beNotInUseSubmit"
      @toUpdatePrice="toUpdatePrice" @downOrder="downOrder" @refresh="refresh" :orderForm="orderForm"></order-node-info>
  </div>
</template>

<script>
import {
  listOrderTemplate,
  listOrderTemplateAll,
  getOrderTemplate,
  delOrderTemplate,
  addOrderTemplate,
  updateOrderTemplate,
  copyOrderTemplate,
  sendWork,
  getOrderId,
  beNotInUseSubmit,
  getOrderForm,
  sendWorkSelect
} from "@/api/order/orderTemplate";

import {
  listAllUser
} from "@/api/system/user";

import {
  listDept
} from "@/api/system/dept";

import orderNodeInfo from "@/views/order/orderNode/info"

export default {
  name: "OrderTemplate",
  dicts: ['order_template_status','flow_query_audit'],
  components: {
    orderNodeInfo
  },
  data() {
    return {
      orderForm: {},
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 订单模板表格数据
      orderTemplateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        flowQueryAudit: '0',
        orderNo:''
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 选中模板ID
      selectOrderTemplateId: null,
      // 用户列表
      userList: [],
      // 部门列表
      deptList: [],
    };
  },
  created() {
    this.getList();
    this.getUserList();
  },
  methods: {

    //归档
    beNotInUseSubmit(orderId, orderType, childId) {

      beNotInUseSubmit(
        {
          orderType: orderType,
          orderId: orderId,
          childId: childId
        }
      ).then(res => {
        this.$modal.msgSuccess("操作成功");
      })




    },

    //去报价
    toUpdatePrice(orderId, orderType, orderTemplateId) {
      this.$router.push({
        path: '/priceSheetOrder/add/index',
        query: {
          orderId: orderId
        }
      });

    },

    //去下单
    downOrder(orderId, orderType, orderTemplateId) {
      this.$router.push({
        path: '/salesOrder/add/index',
        query: {
          type: "price",
          orderId: orderId
        }
      });
    },

    // 报价节点点击事件
    priceSheetNodeClick(template) {
      this.$refs.orderNodeInfoRef.hanldeOpen(null, template.orderType, template.orderTemplateId);
      let _this = this;
      this.$nextTick(() => {
        _this.$refs.orderNodeInfoRef.getPriceRecordNum();
        let isPriceSheet = ['1', '2', '3'].includes(template.status) ? true : false;

        let data = {
          orderType: template.orderType,
          orderTemplateId: template.orderTemplateId
        }
        getOrderId(data).then(res => {
          _this.$refs.orderNodeInfoRef.flowPriceSheet(res.msg, isPriceSheet);
        })


      })
    },

    // 下单节点点击事件
    downOrderNodeClick(template) {
      this.$refs.orderNodeInfoRef.hanldeOpen(null, template.orderType, template.orderTemplateId);
      let _this = this;
      this.$nextTick(() => {
        let isDownOrder = ['1', '2', '3'].includes(template.status) ? false : true;
        let data = {
          orderType: template.orderType,
          orderTemplateId: template.orderTemplateId
        }
        getOrderId(data).then(res => {
          _this.$refs.orderNodeInfoRef.flowDownOrder(res.msg, isDownOrder);
        })
      })
    },

    /**
     * 流程归档按钮
     */
    beNotInUse(template) {
      this.$refs.orderNodeInfoRef.hanldeOpen(null, template.orderType, template.orderTemplateId);
      let _this = this;
      this.$nextTick(() => {
        let data = {
          orderType: template.orderType,
          orderTemplateId: template.orderTemplateId
        }
        getOrderId(data).then(res => {
          _this.$refs.orderNodeInfoRef.setBeNotInUse(res.msg, template.status);
        })


      })
    },

    /**
     * 跳转订单详情
     */
    handleToOrderDetails(orderTemplate) {
      let href = "";
      switch (orderTemplate.orderType) {
        case '0':
          href = "/priceSheetOrder/info/index/";
          break;
        case '1':
          href = "/salesOrder/info/index/";
          break;
        case '2':
          href = "/marketOrder/info/index/";
          break;
      }
      this.$router.push({
        path: href + orderTemplate.templateId,
        query: {
          orderId: orderTemplate.orderId
        }
      });
    },
    /**
     * 订单节点点击事件
     */
    handleNodeClick(orderNode, orderType) {
      let queryData = {
        orderType: orderType,
        orderId: orderNode.orderId
      }
      getOrderForm(queryData).then(res => {
        this.orderForm = res.data;
        this.$refs.orderNodeInfoRef.hanldeOpen(orderNode.orderNodeId, orderType, orderNode.orderTemplateId);
        this.$refs.orderNodeInfoRef.resetPrice();
        this.$refs.orderNodeInfoRef.resetBeNotInUse();
      })

    },
    /**
     * 批量操作按钮
     */
    handleDropdownClick(command) {
      switch (command) {
        case 'copy':
          this.handleCopy();
          break;
        case 'stop':
          this.hanldeUpdateStatus(1);
          break;
        case 'start':
          this.hanldeUpdateStatus(0);
          break;
      }
    },
    //刷新当前页
    refresh() {
      this.getList();
    },

    /**
     * 修改状态
     */
    hanldeUpdateStatus(status) {
      updateOrderTemplate({
        orderTemplateId: this.selectOrderTemplateId,
        orderTemplateStatus: status
      }).then(response => {
        this.$notify({
          title: '成功',
          message: '操作成功',
          type: 'success'
        });
        this.getList();
      });
    },
    /**
     * 复制加工单
     */
    handleCopy() {
      copyOrderTemplate({
        orderTemplateId: this.selectOrderTemplateId
      }).then(res => {
        this.$notify({
          title: '成功',
          message: '新增成功',
          type: 'success'
        });
        this.getList();
      })
    },
    /**
     * 监听部门选择事件
     */
    deptChange(index) {
      const {
        deptId
      } = this.form.orderNodeVoList[index];
      listAllUser({
        deptId: deptId
      }).then(res => {
        this.$set(this.form.orderNodeVoList[index], "userList", res.data);
      })
    },
    /**
     * 获取部门列表
     */
    getDeptList() {
      listDept().then(res => {
        this.deptList = res.data;
      })
    },
    /**
     * 派工
     */
    handleSendWork() {
      this.reset();
      this.getDeptList();
      let query = {
        orderTemplateId: this.selectOrderTemplateId
      }
      sendWorkSelect(query).then(response => {
        this.form = response.data;
        if (this.form.auditStatus != '2') {
          this.$notify({
            title: '警告',
            message: '该订单未审核',
            type: 'warning'
          });
          return;
        }
        this.open = true;
        this.title = "派工";

      });
    },
    /**
     * 获取用户列表
     */
    getUserList() {
      listAllUser().then(res => {
        this.userList = res.data
      })
    },
    /**
     * 模板点击事件
     */
    handleTemplateClick(template) {
      if (template.orderTemplateStatus == '2') {
        return;
      }
      this.selectOrderTemplateId = template.orderTemplateId;
    },
    /** 查询订单模板列表 */
    getList() {
      this.loading = true;
      listOrderTemplate(this.queryParams).then(response => {
        this.orderTemplateList = response.rows;
        this.total = response.total
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        orderTemplateId: null,
        orderId: null,
        orderNo: null,
        orderType: null,
        customerId: null,
        templateId: null,
        orderTemplateStatus: null,
        auditStatus: null,
        detailsDesc: null,
        userId: null,
        orderNum: null,
        orderAmount: null,
        orderDate: null,
        deliveryDate: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        orderNodeVoList: []
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      sendWork(this.form).then(res => {
        this.$notify({
          title: '成功',
          message: '派工成功',
          type: 'success'
        });
        this.open = false;
        this.getList();
      })
    },
    handleQuery(){
      this.getList();
    },
    resetQuery(){
      this.queryParams={
        pageNum: 1,
        pageSize: 10,
        flowQueryAudit: '0',
        orderNo:''
      }
      this.getList();
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
<style lang="scss">
.order-template {
  .el-loading-spinner {
    i {
      font-size: 1.5em;
      color: black !important;
      font-weight: 600;
    }

    .el-loading-text {
      font-size: 1.5em;
      color: black !important;
      font-weight: 600;
    }
  }
}
</style>
<style>
.marquee-border {
  height: 163px;
  position: relative;
  color: #fff;
  border: 3px solid white;
  margin-bottom: 20px;
}

.marquee-border::before {
  content: "";
  position: absolute;
  top: -6px;
  left: -6px;
  right: -6px;
  bottom: -6px;
  border: 3px solid;
  border-image-slice: 1;
  animation: div5Ani 5s;
  animation-iteration-count: infinite;
  animation-timing-function: linear;
  border-image-source: linear-gradient(to left,
      #759aeb,
      rgba(255, 255, 255, 0));
  clip-path: inset(0 0 98% 0);
}

@keyframes div5Ani {

  0%,
  100% {
    border-image-source: linear-gradient(to bottom,
        #759aeb,
        rgba(255, 255, 255, 0));
    clip-path: inset(0 98% 0 0);
  }

  25% {
    border-image-source: linear-gradient(to left,
        #759aeb,
        rgba(255, 255, 255, 0));
    clip-path: inset(0 0 98% 0);
  }

  50% {
    border-image-source: linear-gradient(to top,
        #759aeb,
        rgba(255, 255, 255, 0));
    clip-path: inset(0 0 0 97%);
  }

  75% {
    border-image-source: linear-gradient(to right,
        #759aeb,
        rgba(255, 255, 255, 0));
    clip-path: inset(98% 0 0 0);
  }
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
