 <template>
   <div class="app-container productionflow-order-page">
     <el-card shadow="hover" class="order-card">
       <div slot="header" class="card-header">
         <span>订单池</span>
         <div class="header-actions">
           <el-input
             v-model="orderSearch.keyword"
             placeholder="搜索订单编号/客户/材料"
             size="small"
             clearable
             class="header-search"
             @clear="handleOrderQuery"
             @keyup.enter.native="handleOrderQuery"
           >
             <el-button slot="append" icon="el-icon-search" @click="handleOrderQuery"></el-button>
           </el-input>
           <el-select
             v-model="orderSearch.status"
             placeholder="订单状态"
             size="small"
             clearable
             @change="handleOrderQuery"
           >
             <el-option
               v-for="item in orderStatusOptions"
               :key="item"
               :label="item"
               :value="item"
             />
           </el-select>
           <el-select
             v-model="orderSearch.priority"
             placeholder="优先级"
             size="small"
             clearable
             @change="handleOrderQuery"
           >
             <el-option
               v-for="item in priorityOptions"
               :key="item"
               :label="priorityLabels[item]"
               :value="item"
             />
           </el-select>
         </div>
       </div>

       <div class="table-toolbar">
         <el-button type="primary" size="small" icon="el-icon-plus" @click="openOrderDialog()">
           新增订单
         </el-button>
         <el-button
           type="warning"
           size="small"
           icon="el-icon-edit"
           :disabled="selectedOrders.length !== 1"
           @click="openOrderDialog(selectedOrders[0])"
         >
           修改订单
         </el-button>
         <el-button
           type="danger"
           size="small"
           icon="el-icon-delete"
           :disabled="selectedOrders.length === 0"
           @click="handleBatchDeleteOrders"
         >
           删除订单
         </el-button>
        <el-button
          type="success"
          size="small"
          icon="el-icon-upload"
          :disabled="selectedOrders.length === 0"
          @click="openPoolAssignmentDialog"
        >
          入池
        </el-button>
      </div>

      <el-table
        ref="orderTable"
        :data="filteredOrders"
        border
        height="600"
        stripe
        @selection-change="handleOrderSelectionChange"
        :row-key="row => row.orderId"
        :row-class-name="orderRowClassName"
      >
         <el-table-column type="selection" width="50" />
         <el-table-column prop="orderId" label="订单编号" width="140" show-overflow-tooltip />
         <el-table-column label="预览图" width="110">
           <template slot-scope="scope">
             <el-image
               v-if="scope.row.previewImage"
               :src="scope.row.previewImage"
               fit="cover"
               class="order-preview"
               :preview-src-list="[scope.row.previewImage]"
             />
             <div v-else class="order-preview placeholder">无预览</div>
           </template>
         </el-table-column>
         <el-table-column prop="quantity" label="数量" width="80" align="center" />
         <el-table-column prop="mainMaterial" label="主材料" width="110" show-overflow-tooltip />
         <el-table-column prop="craftRequirements" label="工要求" width="130" show-overflow-tooltip />
         <el-table-column prop="orderStatus" label="订单状态" width="110">
           <template slot-scope="scope">
             <el-tag :type="statusTagType(scope.row.orderStatus)">
               {{ scope.row.orderStatus }}
             </el-tag>
           </template>
         </el-table-column>
         <el-table-column prop="priority" label="优先级" width="90">
           <template slot-scope="scope">
             <el-tag :type="priorityTagType(scope.row.priority)">
               {{ priorityLabels[scope.row.priority] }}
             </el-tag>
           </template>
         </el-table-column>
        <el-table-column label="流程模板" width="160">
          <template slot-scope="scope">
            {{ scope.row.flowTemplate ? scope.row.flowTemplate.templateName : '未绑定' }}
          </template>
        </el-table-column>
         <el-table-column label="操作" width="200" fixed="right">
           <template slot-scope="scope">
             <el-button type="text" size="mini" @click="viewOrder(scope.row)">查看</el-button>
             <el-button type="text" size="mini" @click="openOrderDialog(scope.row)">编辑</el-button>
             <el-button type="text" size="mini" @click="handleDeleteOrder(scope.row)">删除</el-button>
           </template>
         </el-table-column>
       </el-table>
     </el-card>

     <!-- 新增/编辑订单 -->
     <el-dialog :title="orderDialog.title" :visible.sync="orderDialog.visible" width="700px">
       <el-form ref="orderForm" :model="orderDialog.form" :rules="orderRules" label-width="110px">
         <el-row :gutter="20">
           <el-col :span="12">
            <el-form-item label="订单编号" prop="orderId">
              <el-input
                v-model="orderDialog.form.orderId"
                placeholder="请输入订单编号"
                :disabled="orderDialog.isEdit"
              />
            </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="数量" prop="quantity">
               <el-input-number v-model="orderDialog.form.quantity" :min="1" :step="1" style="width: 100%;" />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="主材料" prop="mainMaterial">
               <el-input v-model="orderDialog.form.mainMaterial" placeholder="请输入主材料" />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="订单状态" prop="orderStatus">
               <el-select v-model="orderDialog.form.orderStatus" placeholder="请选择订单状态">
                 <el-option v-for="item in orderStatusOptions" :key="item" :label="item" :value="item" />
               </el-select>
             </el-form-item>
           </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="orderDialog.form.priority" placeholder="请选择优先级">
                <el-option v-for="item in priorityOptions" :key="item" :label="priorityLabels[item]" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流程模板" prop="templateId">
              <el-select
                v-model="orderDialog.form.templateId"
                placeholder="请选择流程模板"
                filterable
                clearable
                @change="handleOrderTemplateSelect"
              >
                <el-option
                  v-for="item in flowTemplateOptions"
                  :key="item.templateId"
                  :label="item.templateName"
                  :value="item.templateId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户信息" prop="customerInfo">
              <el-input v-model="orderDialog.form.customerInfo" placeholder="请输入客户信息" />
            </el-form-item>
          </el-col>
           <el-col :span="12">
             <el-form-item label="交付日期" prop="deliveryDate">
               <el-date-picker
                 v-model="orderDialog.form.deliveryDate"
                 type="datetime"
                 placeholder="请选择交付日期"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="颜色要求" prop="colorRequirement">
               <el-input v-model="orderDialog.form.colorRequirement" placeholder="请输入颜色要求" />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="尺寸规格" prop="sizeRequirement">
               <el-input v-model="orderDialog.form.sizeRequirement" placeholder="请输入尺寸规格" />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="文件格式" prop="fileFormat">
               <el-input v-model="orderDialog.form.fileFormat" placeholder="请输入文件格式" />
             </el-form-item>
           </el-col>
           <el-col :span="24">
             <el-form-item label="工艺要求" prop="craftRequirements">
               <el-input
                 v-model="orderDialog.form.craftRequirements"
                 type="textarea"
                 :rows="2"
                 placeholder="请输入工艺要求"
               />
             </el-form-item>
           </el-col>
           <el-col :span="24">
             <el-form-item label="备注">
               <el-input v-model="orderDialog.form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
             </el-form-item>
           </el-col>
           <el-col :span="24">
             <el-form-item label="预览图路径">
               <el-input v-model="orderDialog.form.previewImage" placeholder="请输入预览图路径" />
             </el-form-item>
           </el-col>
         </el-row>
       </el-form>
       <span slot="footer" class="dialog-footer">
        <el-button @click="orderDialog.visible = false">取 消</el-button>
        <el-button type="primary" :loading="orderDialog.submitting" @click="submitOrder">保 存</el-button>
       </span>
     </el-dialog>

    <!-- 订单详情 -->
    <el-dialog :title="viewOrderDialog.title" :visible.sync="viewOrderDialog.visible" width="800px">
      <div class="order-detail-body" v-loading="viewOrderDialog.loading">
        <div v-if="viewOrderDialog.record">
          <el-descriptions :column="2" border label-class-name="desc-label">
            <el-descriptions-item label="订单编号">{{ viewOrderDialog.record.orderId }}</el-descriptions-item>
            <el-descriptions-item label="订单状态">{{ viewOrderDialog.record.orderStatus }}</el-descriptions-item>
            <el-descriptions-item label="客户信息">{{ viewOrderDialog.record.customerInfo }}</el-descriptions-item>
            <el-descriptions-item label="优先级">{{ priorityLabels[viewOrderDialog.record.priority] }}</el-descriptions-item>
            <el-descriptions-item label="数量">{{ viewOrderDialog.record.quantity }}</el-descriptions-item>
            <el-descriptions-item label="主材料">{{ viewOrderDialog.record.mainMaterial }}</el-descriptions-item>
            <el-descriptions-item label="交付日期">{{ formatDateDisplay(viewOrderDialog.record.deliveryDate) }}</el-descriptions-item>
            <el-descriptions-item label="颜色要求">{{ viewOrderDialog.record.colorRequirement }}</el-descriptions-item>
            <el-descriptions-item label="尺寸规格">{{ viewOrderDialog.record.sizeRequirement }}</el-descriptions-item>
            <el-descriptions-item label="文件格式">{{ viewOrderDialog.record.fileFormat }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDateDisplay(viewOrderDialog.record.createdAt) }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ formatDateDisplay(viewOrderDialog.record.updatedAt) }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ viewOrderDialog.record.remark || '—' }}</el-descriptions-item>
          </el-descriptions>

          <h4 class="section-title">生产池流转</h4>
          <div v-if="viewOrderFlowRelations.length" class="flow-pool-track">
            <template v-for="(flow, index) in viewOrderFlowRelations">
              <div :key="flow.flowId || index" class="flow-pool-chip">
                <div class="flow-pool-title">
                  <span class="flow-pool-name">{{ flow.flowId || '未命名生产池' }}</span>
                  <span v-if="flow.flowTemplate" class="flow-pool-template">
                    （{{ flow.flowTemplate.templateName }}）
                  </span>
                </div>
                <el-tag size="mini" :type="flowStatusTagType(flow.flowStatus)">
                  {{ flowStatusLabels[flow.flowStatus] || flow.flowStatus || '未知状态' }}
                </el-tag>
                <div v-if="flow.createdAt" class="flow-pool-time">{{ formatDateDisplay(flow.createdAt) }}</div>
              </div>
              <div
                v-if="index < viewOrderFlowRelations.length - 1"
                :key="`${flow.flowId || index}-arrow`"
                class="flow-pool-arrow"
              >
                →
              </div>
            </template>
          </div>
          <div v-else class="template-empty">暂无生产池流转记录</div>

          <h4 class="section-title">流程模板</h4>
          <div v-if="viewOrderFlowNodes.length">
            <div class="template-summary">
              模板：{{ viewOrderDialog.record.flowTemplate.templateName }}
            </div>
            <div class="flow-template-visual">
              <div class="flow-track">
                <div
                  class="flow-node-wrapper"
                  :class="{ 'manual-node-clickable': isManualOnlyFlowNode(node) }"
                  v-for="(node, nodeIndex) in viewOrderFlowNodes"
                  :key="node.nodeId || node.nodeName || nodeIndex"
                  @click.stop="handleFlowNodeClick(node, $event)"
                >
                  <div v-if="nodeIndex === 0" class="arrow-first">
                    <div :class="flowNodeSegmentClass(node, 'firstCenter')">
                      <span class="flow-node-name">{{ node.nodeName }}</span>
                    </div>
                    <div :class="flowNodeSegmentClass(node, 'firstRight')"></div>
                  </div>
                  <div
                    v-else-if="nodeIndex === viewOrderFlowNodes.length - 1"
                    class="arrow-last"
                  >
                    <div :class="flowNodeSegmentClass(node, 'lastLeft')"></div>
                    <div :class="flowNodeSegmentClass(node, 'lastCenter')">
                      <span class="flow-node-name">{{ node.nodeName }}</span>
                    </div>
                    <div class="last-right"></div>
                  </div>
                  <div v-else class="arrow">
                    <div :class="flowNodeSegmentClass(node, 'arrowLeft')"></div>
                    <div :class="flowNodeSegmentClass(node, 'arrowCenter')">
                      <span class="flow-node-name">{{ node.nodeName }}</span>
                    </div>
                    <div :class="flowNodeSegmentClass(node, 'arrowRight')"></div>
                  </div>
                  <div class="node-extra">
                    <div class="node-status-text">{{ renderNodeStatusText(node) }}</div>
                    <div
                      class="node-meta"
                      v-if="node.taskExecution && node.taskExecution.lastTriggeredAt"
                    >
                      最近执行：{{ node.taskExecution.lastTriggeredAt }}
                    </div>
                    <el-button
                      v-if="shouldShowManualButton(node, viewOrderDialog.record.orderId)"
                      type="text"
                      size="mini"
                      class="manual-handle-btn"
                      @click.stop="openManualTaskDialogForOrder(viewOrderDialog.record.orderId)"
                    >
                      人工处理
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="template-empty">未绑定流程模板</div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewOrderDialog.visible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 订单分配到生产池 -->
    <el-dialog
      title="分配生产池"
      :visible.sync="poolAssignmentDialog.visible"
      width="820px"
    >
      <div class="pool-assignment" v-loading="poolAssignmentDialog.submitting">
        <el-alert
          title="选择生产池并为每个订单分配进入池子的数量，可拆分到多个生产池。"
          type="info"
          :closable="false"
          show-icon
          class="mb12"
        />

        <div v-if="!poolAssignmentDialog.orders.length" class="empty-assignment">
          请选择订单后再进行入池操作。
        </div>

        <div v-else>
          <div
            v-for="order in poolAssignmentDialog.orders"
            :key="order.orderId"
            class="order-allocation-card"
          >
            <div class="order-allocation-header">
              <div class="order-basic">
                <div class="order-id">订单：{{ order.orderId }}</div>
                <div class="order-customer">客户：{{ order.customerInfo || '未填写' }}</div>
              </div>
              <div class="order-quantity">
                数量：{{ order.quantity }}，已分配 {{ allocationTotal(order.orderId) }}，剩余 {{ Math.max(0, order.quantity - allocationTotal(order.orderId)) }}
              </div>
            </div>

            <el-table
              :data="allocationsByOrder(order.orderId)"
              border
              size="small"
              class="allocation-table"
              empty-text="请为该订单分配生产池"
            >
              <el-table-column label="生产池" min-width="220">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.flowId"
                    placeholder="请选择生产池"
                    filterable
                    clearable
                    style="width: 100%;"
                  >
                    <el-option
                      v-for="pool in flowPoolOptions"
                      :key="pool.flowId"
                      :label="`${pool.flowId}（${(pool.flowTemplate && pool.flowTemplate.templateName) || '未绑定模板'}）`"
                      :value="pool.flowId"
                    />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="分配数量" width="160" align="center">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.quantity"
                    :min="1"
                    :max="order.quantity"
                    :step="1"
                    controls-position="right"
                  />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    @click="removeAllocation(order.orderId, scope.$index)"
                  >移除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <div class="allocation-actions">
              <el-button type="primary" size="mini" icon="el-icon-plus" @click="addAllocation(order)">
                继续分配
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="poolAssignmentDialog.visible = false">取 消</el-button>
        <el-button type="primary" :loading="poolAssignmentDialog.submitting" @click="submitPoolAssignments">
          确 定
        </el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="任务人工处理"
      :visible.sync="manualTaskDialog.visible"
      width="520px"
      @close="resetManualTaskDialog"
    >
      <div class="manual-task-dialog">
        <p class="manual-tip">
          节点「{{ manualTaskDialog.node && manualTaskDialog.node.nodeName }}」自动执行失败，请人工确认后继续。
        </p>
        <el-alert
          v-if="manualTaskDialog.errorMessage"
          :title="manualTaskDialog.errorMessage"
          type="error"
          :closable="false"
          class="mb12"
        />
        <el-input
          v-model="manualTaskDialog.remark"
          type="textarea"
          :rows="3"
          placeholder="请填写人工处理备注"
        />
        <div v-if="manualTaskDialog.responsePreview" class="response-preview">
          <div class="preview-title">接口返回</div>
          <pre>{{ manualTaskDialog.responsePreview }}</pre>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="manualTaskDialog.visible = false">取 消</el-button>
        <el-button
          type="primary"
          :loading="manualTaskDialog.submitting"
          @click="confirmManualTaskHandling"
        >
          已人工处理
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  listOrderPool,
  getOrderPool,
  addOrderPool,
  updateOrderPool,
  removeOrderPool
} from '@/api/productionflow/orderPool'
import { listFlowPool, getFlowPool as getFlowPoolDetail, updateFlowPool } from '@/api/productionflow/flowPool'
import { listFlowTemplateAll, getFlowTemplate } from '@/api/order/flowTemplate'
import { listTaskTemplateAll } from '@/api/order/taskTemplate'
import { complateNode, submitRemark } from '@/api/order/orderNode'
import request from '@/utils/request'

const pad = value => `${value}`.padStart(2, '0')

const formatDateHelper = value => {
  if (!value) return ''
  const date = value instanceof Date ? value : new Date(value)
  if (Number.isNaN(date.getTime())) return ''
  const y = date.getFullYear()
  const m = pad(date.getMonth() + 1)
  const d = pad(date.getDate())
  const h = pad(date.getHours())
  const min = pad(date.getMinutes())
  return `${y}-${m}-${d} ${h}:${min}`
}

const nowDateTimeHelper = () => formatDateHelper(new Date())

const SYSTEM_NODE_TYPES = new Set(['0', '1', '2', '3', '4', '5', '6', '7'])

const NODE_FAILED_STATUS = '3'

const FLOW_SEGMENT_CLASS_MAP = {
  firstCenter: {
    default: 'first-center',
    processing: 'first-center-processing',
    completed: 'first-center-completed',
    timeout: 'first-center-timeout'
  },
  firstRight: {
    default: 'first-right',
    processing: 'first-right-processing',
    completed: 'first-right-completed',
    timeout: 'first-right-timeout'
  },
  arrowLeft: {
    default: 'arrow-left',
    processing: 'arrow-left-processing',
    completed: 'arrow-left-completed',
    timeout: 'arrow-left-timeout'
  },
  arrowCenter: {
    default: 'arrow-center',
    processing: 'arrow-center-processing',
    completed: 'arrow-center-completed',
    timeout: 'arrow-center-timeout'
  },
  arrowRight: {
    default: 'arrow-right',
    processing: 'arrow-right-processing',
    completed: 'arrow-right-completed',
    timeout: 'arrow-right-timeout'
  },
  lastLeft: {
    default: 'last-left',
    processing: 'last-left-processing',
    completed: 'last-left-completed',
    timeout: 'last-left-timeout'
  },
  lastCenter: {
    default: 'last-center',
    processing: 'last-center-processing',
    completed: 'last-center-completed',
    timeout: 'last-center-timeout'
  }
}

const deepClone = data => {
  if (data === null || data === undefined) {
    return data
  }
  return JSON.parse(JSON.stringify(data))
}

export default {
  name: 'ProductionFlowOrderPool',
  data() {
    return {
      orderStatusOptions: ['待处理', '处理中', '待入池', '已入池', '生产中', '已完成', '已取消'],
      priorityOptions: ['low', 'normal', 'high', 'urgent'],
      priorityLabels: {
        low: '低',
        normal: '普通',
        high: '高',
        urgent: '紧急'
      },
      orderSearch: {
        keyword: '',
        status: '',
        priority: ''
      },
      orderList: [],
      flowList: [],
      flowPoolOptions: [],
      flowPoolDetailMap: {},
      flowTemplateOptions: [],
      flowTemplateDetailMap: {},
      taskTemplateMap: {},
      selectedOrders: [],
      orderDialog: {
        visible: false,
        title: '',
        isEdit: false,
        submitting: false,
        form: {}
      },
      viewOrderDialog: {
        visible: false,
        title: '订单详情',
        record: null,
        loading: false
      },
      manualTaskDialog: {
        visible: false,
        node: null,
        template: null,
        templateId: '',
        orderForm: null,
        pendingNodes: [],
        errorMessage: '',
        responsePreview: '',
        remark: '',
        orderId: '',
        submitting: false
      },
      nodeClickHandling: false,
      orderAutomationState: {},
       flowStatusOptions: [
         'pending',
         'file_preparing',
         'file_ready',
         'layout_designing',
         'layout_approved',
         'printing',
         'printed',
         'cutting',
         'cut_completed',
         'quality_check',
         'completed',
         'cancelled'
       ],
      flowStatusLabels: {
        pending: '待开始',
        file_preparing: '文件准备中',
        file_ready: '文件就绪',
        layout_designing: '排版设计中',
        layout_approved: '排版已确认',
        printing: '打印中',
        printed: '已打印',
        cutting: '切割中',
        cut_completed: '切割完成',
        quality_check: '质检中',
        completed: '已完成',
        cancelled: '已取消'
      },
      poolAssignmentDialog: {
        visible: false,
        submitting: false,
        orders: [],
        allocations: []
      },
      orderRules: {
        orderId: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
        quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
        mainMaterial: [{ required: true, message: '请输入主材料', trigger: 'blur' }],
        orderStatus: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
        priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
        templateId: [{ required: true, message: '请选择流程模板', trigger: 'change' }],
        customerInfo: [{ required: true, message: '请输入客户信息', trigger: 'blur' }],
        deliveryDate: [{ required: true, message: '请选择交付日期', trigger: 'change' }],
        colorRequirement: [{ required: true, message: '请输入颜色要求', trigger: 'blur' }],
        sizeRequirement: [{ required: true, message: '请输入尺寸规格', trigger: 'blur' }],
        fileFormat: [{ required: true, message: '请输入文件格式', trigger: 'blur' }],
        craftRequirements: [{ required: true, message: '请输入工艺要求', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.initializeData()
  },
  computed: {
    filteredOrders() {
      let list = this.orderList.slice()
      if (this.orderSearch.keyword) {
        const keyword = this.orderSearch.keyword.toLowerCase()
         list = list.filter(order => {
           return (
             order.orderId.toLowerCase().includes(keyword) ||
             (order.customerInfo && order.customerInfo.toLowerCase().includes(keyword)) ||
             (order.mainMaterial && order.mainMaterial.toLowerCase().includes(keyword))
           )
         })
       }
       if (this.orderSearch.status) {
         list = list.filter(order => order.orderStatus === this.orderSearch.status)
       }
      if (this.orderSearch.priority) {
        list = list.filter(order => order.priority === this.orderSearch.priority)
     }
     return list
    },
    viewOrderFlowNodes() {
      const record = this.viewOrderDialog.record
      if (!record) {
        return []
      }
      const templateNodes = record.flowTemplate && Array.isArray(record.flowTemplate.flowNodeList)
        ? deepClone(record.flowTemplate.flowNodeList)
        : []
      const orderNodes = this.normalizeOrderNodes(record.orderNodes)
      if (!templateNodes.length && orderNodes.length) {
        return orderNodes.map(node => ({
          nodeName: node.nodeName || node.nodeRemark || node.nodeId,
          orderNode: node
        }))
      }
      return templateNodes.map((node, index) => {
        const matchedNode = orderNodes.find(item => item.nodeId && item.nodeId === node.nodeId)
          || orderNodes[index]
          || null
        return Object.assign({}, node, { orderNode: matchedNode })
      })
    },
    viewOrderFlowRelations() {
      const record = this.viewOrderDialog.record
      if (!record || !record.orderId) {
        return []
      }
      const flows = this.flowList.filter(flow => Array.isArray(flow.orderIds) && flow.orderIds.includes(record.orderId))
      return flows.slice().sort((a, b) => this.toTimestamp(a.createdAt) - this.toTimestamp(b.createdAt))
    }
  },
  methods: {
    async initializeData() {
      await Promise.all([
        this.fetchFlowTemplates(),
        this.fetchOrders(),
        this.fetchFlows(),
        this.fetchTaskTemplates()
      ])
    },
    async fetchFlowTemplates() {
      try {
        const response = await listFlowTemplateAll({})
        const list = Array.isArray(response.data) ? response.data : []
        this.flowTemplateOptions = list.map(item => deepClone(item))
        this.flowTemplateOptions.forEach(item => {
          if (item && item.templateId && Array.isArray(item.flowNodeList) && item.flowNodeList.length) {
            this.cacheTemplateDetail(item)
          }
        })
      } catch (error) {
        this.flowTemplateOptions = []
        this.$message.error('获取流程模板失败')
      }
    },
    async fetchTaskTemplates() {
      try {
        const response = await listTaskTemplateAll({})
        const list = Array.isArray(response.data) ? response.data : (Array.isArray(response.rows) ? response.rows : [])
        const map = {}
        list.forEach(item => {
          if (item && item.templateId) {
            map[item.templateId] = {
              ...item,
              parsedConfig: this.parseTaskTemplateConfig(item.config || item.parsedConfig)
            }
          }
        })
        this.taskTemplateMap = map
      } catch (error) {
        this.taskTemplateMap = {}
        console.error(error)
        this.$message.error('加载任务模板失败')
      }
    },
    async fetchOrders() {
      const params = {
        keyword: this.orderSearch.keyword,
        status: this.orderSearch.status,
        priority: this.orderSearch.priority
      }
      try {
        const response = await listOrderPool(params)
        const list = Array.isArray(response.data) ? response.data : []
        this.orderList = list.map(item => this.normalizeOrder(item))
      } catch (error) {
        this.orderList = []
        this.$message.error('获取订单列表失败')
      } finally {
        this.selectedOrders = []
        this.$nextTick(() => {
          if (this.$refs.orderTable) {
            this.$refs.orderTable.clearSelection()
          }
        })
      }
    },
    async refreshOrderRecord(orderId, { silent = true, updateDialog = true, withLoading = false } = {}) {
      if (!orderId) {
        return null
      }
      const shouldToggleLoading = withLoading
        && this.viewOrderDialog.visible
        && this.viewOrderDialog.record
        && this.viewOrderDialog.record.orderId === orderId
      if (shouldToggleLoading) {
        this.$set(this.viewOrderDialog, 'loading', true)
      }
      try {
        const response = await getOrderPool(orderId)
        const data = response && response.data ? response.data : null
        if (!data) {
          return null
        }
        const normalized = this.normalizeOrder(data)
        const index = this.orderList.findIndex(item => item.orderId === orderId)
        if (index !== -1) {
          this.$set(this.orderList, index, normalized)
        } else {
          this.orderList.unshift(normalized)
        }
        if (
          updateDialog
          && this.viewOrderDialog.visible
          && this.viewOrderDialog.record
          && this.viewOrderDialog.record.orderId === orderId
        ) {
          this.viewOrderDialog.record = this.buildViewOrderRecord(normalized)
        }
        this.autoTriggerFromOrder(normalized)
        return normalized
      } catch (error) {
        console.error('刷新订单详情失败', error)
        if (!silent) {
          this.$message.error('刷新订单详情失败')
        }
        return null
      } finally {
        if (shouldToggleLoading) {
          this.$set(this.viewOrderDialog, 'loading', false)
        }
      }
    },
    async fetchFlows() {
      try {
        const response = await listFlowPool({})
        const list = Array.isArray(response.data) ? response.data : []
        const normalized = list.map(item => this.normalizeFlow(item))
        this.flowList = normalized
        this.flowPoolOptions = normalized
        const map = {}
        normalized.forEach(item => {
          if (item && item.flowId) {
            map[item.flowId] = item
          }
        })
        this.flowPoolDetailMap = map
      } catch (error) {
        this.flowList = []
        this.flowPoolOptions = []
        this.flowPoolDetailMap = {}
        this.$message.error('获取生产池数据失败')
      }
    },
    handleOrderQuery() {
      this.fetchOrders()
    },
    normalizeOrder(order = {}) {
      const orderNodes = this.normalizeOrderNodes(order.orderNodes)
      return {
        orderId: order.orderId || '',
        previewImage: order.previewImage || '',
        quantity: Number(order.quantity || 0),
        remark: order.remark || '',
        mainMaterial: order.mainMaterial || '',
        craftRequirements: order.craftRequirements || '',
        orderStatus: order.orderStatus || '待处理',
        createdAt: this.formatDateValue(order.createdAt) || '',
        updatedAt: this.formatDateValue(order.updatedAt) || '',
        customerInfo: order.customerInfo || '',
        priority: order.priority || 'normal',
        deliveryDate: this.formatDateValue(order.deliveryDate) || '',
        sizeRequirement: order.sizeRequirement || '',
        colorRequirement: order.colorRequirement || '',
        fileFormat: order.fileFormat || '',
        templateId: order.templateId || '',
        flowTemplate: order.flowTemplate || null,
        orderTemplate: order.orderTemplate || null,
        orderNodes
      }
    },
    normalizeOrderNodes(nodes = []) {
      if (!Array.isArray(nodes)) {
        return []
      }
      return nodes.map((node, index) => ({
        ...node,
        nodeStatus: node && node.nodeStatus != null ? `${node.nodeStatus}` : '0',
        nodeRemark: node && node.nodeRemark ? node.nodeRemark : '',
        triggerMode: (node && node.triggerMode) ? `${node.triggerMode}` : 'MANUAL',
        sort: node && node.sort != null ? node.sort : index
      }))
    },
    markNodeAsFailed(node, orderForm, remark) {
      if (!node) {
        return
      }
      const failureRemark = remark || '自动节点完成失败'
      this.$set(node, 'nodeStatus', NODE_FAILED_STATUS)
      if (node.orderNode) {
        this.$set(node.orderNode, 'nodeStatus', NODE_FAILED_STATUS)
        this.$set(node.orderNode, 'nodeRemark', failureRemark)
      }
      if (orderForm && Array.isArray(orderForm.orderNodes)) {
        const targetOrderNodeId = node.orderNodeId || (node.orderNode && node.orderNode.orderNodeId)
        const targetNodeId = node.nodeId || (node.orderNode && node.orderNode.nodeId)
        const nodeIndex = orderForm.orderNodes.findIndex(
          item => item && (item.orderNodeId === targetOrderNodeId || item.nodeId === targetNodeId)
        )
        if (nodeIndex !== -1) {
          const updatedNode = {
            ...orderForm.orderNodes[nodeIndex],
            nodeStatus: NODE_FAILED_STATUS,
            nodeRemark: failureRemark
          }
          this.$set(orderForm.orderNodes, nodeIndex, updatedNode)
        }
      }
    },
    normalizeFlow(flow = {}) {
      const materialsSummary = Array.isArray(flow.materialsSummary)
        ? flow.materialsSummary.map((item, index) => ({
          ...item,
          material: item.material || '',
          quantity: Number(item.quantity || 0),
          sortOrder: item.sortOrder !== undefined ? item.sortOrder : index
        }))
        : []
      const orderAllocations = Array.isArray(flow.orderAllocations)
        ? flow.orderAllocations
          .filter(item => item && item.orderId)
          .map(item => ({ orderId: item.orderId, quantity: Number(item.quantity || 0) }))
        : []
      return {
        flowId: flow.flowId || '',
        orderIds: Array.isArray(flow.orderIds) ? flow.orderIds.slice() : [],
        templateId: flow.templateId || '',
        flowTemplate: flow.flowTemplate || null,
        flowStatus: flow.flowStatus || 'pending',
        totalQuantity: Number(flow.totalQuantity || 0),
        materialsSummary,
        orderAllocations,
        priority: flow.priority || 'normal',
        scheduledStart: this.formatDateValue(flow.scheduledStart) || '',
        scheduledEnd: this.formatDateValue(flow.scheduledEnd) || '',
        actualStart: this.formatDateValue(flow.actualStart) || '',
        actualEnd: this.formatDateValue(flow.actualEnd) || '',
        assignedOperator: flow.assignedOperator || '',
        productionNotes: flow.productionNotes || '',
        process: Array.isArray(flow.process)
          ? flow.process.map((step, index) => ({
            ...step,
            sortOrder: step.sortOrder !== undefined ? step.sortOrder : index
          }))
          : [],
        createdAt: this.formatDateValue(flow.createdAt) || '',
        updatedAt: this.formatDateValue(flow.updatedAt) || ''
      }
    },
    parseTaskTemplateConfig(rawConfig) {
      if (!rawConfig) {
        return { requestUrl: '', requestParams: [], responseParams: [] }
      }
      if (typeof rawConfig === 'string') {
        try {
          return this.parseTaskTemplateConfig(JSON.parse(rawConfig))
        } catch (error) {
          return { requestUrl: '', requestParams: [], responseParams: [] }
        }
      }
      return {
        requestUrl: rawConfig.requestUrl || '',
        requestParams: Array.isArray(rawConfig.requestParams) ? rawConfig.requestParams : [],
        responseParams: Array.isArray(rawConfig.responseParams) ? rawConfig.responseParams : [],
        requestMethod: (rawConfig.requestMethod || rawConfig.method || 'POST').toUpperCase()
      }
    },
    cacheTemplateDetail(template) {
      if (!template || !template.templateId) {
        return
      }
      this.$set(this.flowTemplateDetailMap, template.templateId, deepClone(template))
    },
    async ensureFlowTemplateDetails(templateId) {
      if (!templateId) {
        return null
      }
      const cached = this.flowTemplateDetailMap[templateId]
      if (cached && Array.isArray(cached.flowNodeList) && cached.flowNodeList.length) {
        return deepClone(cached)
      }
      const fromOptions = this.flowTemplateOptions.find(item => item.templateId === templateId)
      if (fromOptions && Array.isArray(fromOptions.flowNodeList) && fromOptions.flowNodeList.length) {
        this.cacheTemplateDetail(fromOptions)
        return deepClone(fromOptions)
      }
      try {
        const { data } = await getFlowTemplate(templateId)
        if (data) {
          this.cacheTemplateDetail(data)
          return deepClone(data)
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取流程模板详情失败')
      }
      return null
    },
    applyTemplateDetailUpdate(templateId, templateData) {
      if (!templateId || !templateData) {
        return
      }
      const cloned = deepClone(templateData)
      this.$set(this.flowTemplateDetailMap, templateId, cloned)
      const optionIndex = this.flowTemplateOptions.findIndex(item => item.templateId === templateId)
      if (optionIndex !== -1) {
        const updatedOption = Object.assign({}, this.flowTemplateOptions[optionIndex], {
          flowNodeList: deepClone(templateData.flowNodeList || [])
        })
        this.$set(this.flowTemplateOptions, optionIndex, updatedOption)
      }
      if (this.orderDialog.form && this.orderDialog.form.templateId === templateId) {
        this.$set(this.orderDialog.form, 'flowTemplate', deepClone(templateData))
      }
    },
    isTaskTemplateNode(node) {
      if (!node || node.nodeType == null) {
        return false
      }
      const nodeType = `${node.nodeType}`
      if (SYSTEM_NODE_TYPES.has(nodeType)) {
        return false
      }
      return Boolean(this.taskTemplateMap[nodeType])
    },
    normalizeTriggerMode(value) {
      if (!value) {
        return 'MANUAL'
      }
      const normalized = `${value}`.toUpperCase()
      if (normalized === 'AUTO' || normalized === '自动触发') {
        return 'AUTO'
      }
      if (normalized === 'MANUAL' || normalized === '人工触发') {
        return 'MANUAL'
      }
      return normalized
    },
    isAutoTriggerNode(node) {
      if (!node) {
        return false
      }
      const triggerMode = this.normalizeTriggerMode(
        (node.orderNode && node.orderNode.triggerMode)
        || node.triggerMode
      )
      return triggerMode === 'AUTO'
    },
    extractNodeIdentity(node) {
      if (!node) {
        return ''
      }
      const candidates = [
        node.orderNodeId,
        node.nodeId,
        node.id,
        node.orderNode && node.orderNode.orderNodeId,
        node.orderNode && node.orderNode.nodeId
      ]
      const matched = candidates.find(item => item !== undefined && item !== null && `${item}` !== '')
      return matched !== undefined && matched !== null ? `${matched}` : ''
    },
    normalizeInterfaceType(value) {
      if (!value) {
        return 'SYNC'
      }
      const normalized = `${value}`.toUpperCase()
      return normalized === 'ASYNC' ? 'ASYNC' : 'SYNC'
    },
    getNodeInterfaceType(node, taskTemplate, orderNode) {
      const candidates = [
        node && node.interfaceType,
        node && node.interface_type,
        orderNode && orderNode.interfaceType,
        orderNode && orderNode.interface_type,
        taskTemplate && taskTemplate.interfaceType,
        taskTemplate && taskTemplate.interface_type
      ]
      const matched = candidates.find(item => item !== undefined && item !== null && `${item}` !== '')
      return this.normalizeInterfaceType(matched || 'SYNC')
    },
    findAsyncPendingNode(order = {}, template) {
      const flowTemplate = template || order.flowTemplate || null
      if (!flowTemplate || !Array.isArray(flowTemplate.flowNodeList) || !flowTemplate.flowNodeList.length) {
        return null
      }
      const flowNodes = flowTemplate.flowNodeList.slice().sort((a, b) => {
        const aSort = a && a.sort != null ? Number(a.sort) : 0
        const bSort = b && b.sort != null ? Number(b.sort) : 0
        return aSort - bSort
      })
      const orderNodes = this.normalizeOrderNodes(order.orderNodes)
      const orderNodeMap = {}
      orderNodes.forEach(item => {
        if (item && item.nodeId) {
          orderNodeMap[item.nodeId] = item
        }
      })
      for (let i = 0; i < flowNodes.length; i += 1) {
        const flowNode = flowNodes[i]
        const mapped = flowNode && flowNode.nodeId ? orderNodeMap[flowNode.nodeId] : null
        const statusStr = `${(mapped && mapped.nodeStatus) || '0'}`
        const interfaceType = this.getNodeInterfaceType(flowNode, this.taskTemplateMap[`${flowNode.nodeType || ''}`], mapped)
        if (interfaceType === 'ASYNC' && statusStr === '1') {
          return {
            flowNode,
            orderNode: mapped,
            waitingNodeId: this.extractNodeIdentity(mapped || flowNode)
          }
        }
      }
      return null
    },
    isOrderNodeCompleted(order = {}, nodeKey) {
      if (!nodeKey) {
        return false
      }
      const nodes = this.normalizeOrderNodes(order.orderNodes)
      return nodes.some(item => {
        if (!item) return false
        const key = item.orderNodeId || item.nodeId
        return key && `${key}` === `${nodeKey}` && `${item.nodeStatus || '0'}` === '2'
      })
    },
    markNodeAsProcessing(node, orderForm, remark) {
      const applyStatus = target => {
        if (!target) return
        this.$set(target, 'nodeStatus', '1')
        if (remark) {
          this.$set(target, 'nodeRemark', remark)
        }
      }
      if (node && node.orderNode) {
        applyStatus(node.orderNode)
      }
      const nodes = Array.isArray(orderForm && orderForm.orderNodes) ? orderForm.orderNodes : []
      if (nodes.length) {
        const matched = nodes.find(
          item =>
            item &&
            (item.orderNodeId === node.orderNodeId
              || item.nodeId === node.nodeId
              || (node.orderNode && item.orderNodeId === node.orderNode.orderNodeId)
              || (node.orderNode && item.nodeId === node.orderNode.nodeId))
        )
        applyStatus(matched)
      }
    },
    buildTaskRequestPayload(config = {}, orderForm = {}) {
      const payload = {}
      const params = Array.isArray(config.requestParams) ? config.requestParams : []
      params.forEach(param => {
        if (!param || !param.paramKey) {
          return
        }
        const value = this.resolveOrderValue(orderForm, param.paramKey)
        if (value !== undefined) {
          payload[param.paramKey] = value
        }
      })
      if (orderForm.orderId) {
        payload.orderId = orderForm.orderId
      }
      const flowId = orderForm.flowId
        || orderForm.flowPoolId
        || (orderForm.flowPool && orderForm.flowPool.flowId)
      if (flowId) {
        payload.flowId = flowId
      }
      return payload
    },
    resolveOrderValue(source, path) {
      if (!path) {
        return undefined
      }
      const segments = path.split('.').filter(Boolean)
      return segments.reduce((current, key) => {
        if (current === undefined || current === null) {
          return undefined
        }
        return current[key]
      }, source)
    },
    updateNodeExecutionState(node, result = {}) {
      if (!node) {
        return
      }
      const execution = Object.assign({}, node.taskExecution || {}, {
        lastTriggeredAt: this.nowDateTime(),
        ...result
      })
      this.$set(node, 'taskExecution', execution)
      if (result.response) {
        const status = result.response.nodeStatus || result.response.status || result.response.statusValue
        if (status !== undefined) {
          this.$set(node, 'nodeStatus', status)
        }
        if (result.response.nodeRemark || result.response.remark) {
          this.$set(node, 'nodeRemark', result.response.nodeRemark || result.response.remark)
        }
      }
    },
    async executeTaskNode(node, orderForm) {
      const nodeType = node && node.nodeType != null ? `${node.nodeType}` : ''
      const orderId = orderForm && orderForm.orderId ? orderForm.orderId : ''
      const orderNodeId = node.orderNodeId
        || (node.orderNode && node.orderNode.orderNodeId)
        || ''
      const flowNodeId = node.nodeId || (node.orderNode && node.orderNode.nodeId) || ''
      const finalizeResult = async result => {
        this.updateNodeExecutionState(node, result)
        if (orderId) {
          await this.refreshOrderRecord(orderId, { silent: true, updateDialog: true })
        }
        return result
      }
      if (!nodeType) {
        return finalizeResult({ success: false, message: '节点类型为空' })
      }
      const taskTemplate = this.taskTemplateMap[nodeType]
      if (!taskTemplate) {
        return finalizeResult({ success: false, message: '未找到任务模板配置' })
      }
      if (taskTemplate.triggerMode && taskTemplate.triggerMode !== 'AUTO') {
        return finalizeResult({ success: false, message: '任务模板未设置为自动触发' })
      }
      const interfaceType = this.getNodeInterfaceType(node, taskTemplate, node.orderNode)
      const config = taskTemplate.parsedConfig || this.parseTaskTemplateConfig(taskTemplate.config)
      if (!config.requestUrl) {
        return finalizeResult({ success: false, message: '任务模板未配置接口URL' })
      }
      try {
        const responseData = await this.executeTaskTemplate(taskTemplate, orderForm, config)
        const success = this.isTaskResponseSuccess(responseData)
        const resultPayload = {
          success,
          response: responseData,
          message: success ? '任务执行成功' : '接口返回未满足成功条件',
          interfaceType
        }
        const isAsyncInterface = interfaceType === 'ASYNC'
        if (success && isAsyncInterface) {
          resultPayload.asyncPending = true
          resultPayload.message = resultPayload.message || '异步接口已触发，等待回调完成'
          const responseWrapper = responseData && typeof responseData === 'object' ? responseData : {}
          resultPayload.response = {
            ...responseWrapper,
            nodeStatus: '1',
            nodeRemark: resultPayload.message
          }
          this.markNodeAsProcessing(node, orderForm, resultPayload.message)
        } else if (success && orderId && orderNodeId) {
          try {
            await complateNode({
              orderId,
              orderNodeId,
              nodeId: flowNodeId,
              nodeRemark: resultPayload.message || '自动触发完成',
              nodeType: '3'
            })
          } catch (error) {
            console.error('自动节点完成失败', error)
            if (this.isDuplicateSubmissionError(error)) {
              resultPayload.success = true
              resultPayload.message = resultPayload.message || '节点已完成'
            } else {
              resultPayload.success = false
              resultPayload.error = this.extractErrorMessage(error) || '节点完成失败'
              this.markNodeAsFailed(node, orderForm, resultPayload.error)
            }
          }
        }
        return finalizeResult(resultPayload)
      } catch (error) {
        const errorMessage = this.extractErrorMessage(error) || '接口调用失败'
        return finalizeResult({
          success: false,
          error: errorMessage,
          response: (error && (error.responseData || (error.response && error.response.data))) || null
        })
      }
    },
    async executeTaskTemplate(taskTemplate, orderForm, config = {}) {
      const payload = this.buildTaskRequestPayload(config, orderForm)
      const method = (config.requestMethod || 'POST').toLowerCase()
      const requestOptions = {
        url: config.requestUrl,
        method
      }
      if (method === 'get' || method === 'delete') {
        requestOptions.params = payload
      } else {
        requestOptions.data = payload
      }
      try {
        const response = await request(requestOptions)
        return response && response.data !== undefined ? response.data : response
      } catch (error) {
        if (error && error.response && error.response.data) {
          error.responseData = error.response.data
        }
        throw error
      }
    },
    buildAutoTriggerQueue(order = {}, template) {
      const flowTemplate = template || order.flowTemplate || null
      if (!flowTemplate || !Array.isArray(flowTemplate.flowNodeList) || !flowTemplate.flowNodeList.length) {
        return []
      }
      const flowNodes = flowTemplate.flowNodeList.slice().sort((a, b) => {
        const aSort = a && a.sort != null ? Number(a.sort) : 0
        const bSort = b && b.sort != null ? Number(b.sort) : 0
        return aSort - bSort
      })
      const orderNodes = this.normalizeOrderNodes(order.orderNodes)
      if (!orderNodes.length) {
        return []
      }
      const orderNodeMap = {}
      orderNodes.forEach(item => {
        if (item && item.nodeId) {
          orderNodeMap[item.nodeId] = item
        }
      })

      let shouldStartQueue = false
      let previousNodesCompleted = true
      const queue = []

      for (let i = 0; i < flowNodes.length; i += 1) {
        const flowNode = flowNodes[i]
        const mapped = flowNode && flowNode.nodeId ? orderNodeMap[flowNode.nodeId] : null
        const triggerMode = this.normalizeTriggerMode((mapped && mapped.triggerMode) || flowNode.triggerMode)
        const statusStr = `${(mapped && mapped.nodeStatus) || '0'}`
        const isCompleted = statusStr === '2'
        const interfaceType = this.getNodeInterfaceType(flowNode, this.taskTemplateMap[`${flowNode.nodeType || ''}`], mapped)
        if (interfaceType === 'ASYNC' && statusStr === '1') {
          break
        }

        if (!mapped) {
          previousNodesCompleted = false
        }

        if (!shouldStartQueue) {
          if (!previousNodesCompleted) {
            break
          }
          if (!mapped || isCompleted) {
            continue
          }
          if (triggerMode === 'AUTO') {
            shouldStartQueue = true
            queue.push({ ...flowNode, orderNode: mapped, orderNodeId: mapped.orderNodeId })
            continue
          }
          previousNodesCompleted = false
        } else {
          if (!mapped || isCompleted) {
            continue
          }
          if (triggerMode !== 'AUTO') {
            break
          }
          queue.push({ ...flowNode, orderNode: mapped, orderNodeId: mapped.orderNodeId })
        }
      }

      return queue
    },
    hasPendingManualNode(order = {}, template) {
      const flowTemplate = template || order.flowTemplate || null
      if (!flowTemplate || !Array.isArray(flowTemplate.flowNodeList) || !flowTemplate.flowNodeList.length) {
        return false
      }
      const flowNodes = flowTemplate.flowNodeList.slice().sort((a, b) => {
        const aSort = a && a.sort != null ? Number(a.sort) : 0
        const bSort = b && b.sort != null ? Number(b.sort) : 0
        return aSort - bSort
      })
      const orderNodes = this.normalizeOrderNodes(order.orderNodes)
      if (!orderNodes.length) {
        return false
      }
      const orderNodeMap = {}
      orderNodes.forEach(item => {
        if (item && item.nodeId) {
          orderNodeMap[item.nodeId] = item
        }
      })
      let previousNodesCompleted = true
      for (let i = 0; i < flowNodes.length; i += 1) {
        const flowNode = flowNodes[i]
        const mapped = flowNode && flowNode.nodeId ? orderNodeMap[flowNode.nodeId] : null
        const triggerMode = this.normalizeTriggerMode((mapped && mapped.triggerMode) || flowNode.triggerMode)
        const statusStr = `${(mapped && mapped.nodeStatus) || '0'}`
        const isCompleted = statusStr === '2'
        if (!mapped) {
          previousNodesCompleted = false
        }
        if (!previousNodesCompleted) {
          break
        }
        if (!mapped || isCompleted) {
          continue
        }
        if (triggerMode !== 'AUTO') {
          return true
        }
        previousNodesCompleted = false
      }
      return false
    },
    async autoTriggerFromOrder(order) {
      const orderId = order && order.orderId
      if (!orderId) {
        return
      }
      const automationState = this.getOrderAutomationState(orderId)
      if (automationState && (automationState.status === 'running' || automationState.status === 'failed')) {
        return
      }
      let orderDetail = order
      if (!orderDetail || !Array.isArray(orderDetail.orderNodes) || !orderDetail.orderNodes.length) {
        try {
          const { data } = await getOrderPool(orderId)
          if (data) {
            orderDetail = this.normalizeOrder(data)
            const listIndex = this.orderList.findIndex(item => item.orderId === orderId)
            if (listIndex !== -1) {
              this.$set(this.orderList, listIndex, orderDetail)
            }
          }
        } catch (error) {
          console.error('获取订单详情失败', error)
        }
      }
      const template = orderDetail.flowTemplate || await this.ensureFlowTemplateDetails(orderDetail.templateId)
      if (!template || !Array.isArray(template.flowNodeList) || !template.flowNodeList.length) {
        return
      }
      const pendingAsyncNode = this.findAsyncPendingNode(orderDetail, template)
      if (pendingAsyncNode) {
        this.setOrderAutomationState(orderId, {
          templateId: template.templateId,
          templateInstance: template,
          orderForm: orderDetail,
          status: 'pending_async',
          failedNode: null,
          pendingNodes: [],
          errorMessage: '',
          responsePreview: '',
          waitingNodeId: pendingAsyncNode.waitingNodeId
        })
        return
      }
      if (automationState && automationState.status === 'pending_async') {
        this.setOrderAutomationState(orderId, { waitingNodeId: null, status: 'pending_resume' })
      }
      if (this.hasPendingManualNode(orderDetail, template)) {
        return
      }
      const queue = this.buildAutoTriggerQueue(orderDetail, template)
      if (!queue.length) {
        return
      }
      this.setOrderAutomationState(orderId, {
        templateId: template.templateId,
        templateInstance: template,
        orderForm: orderDetail,
        status: 'running',
        failedNode: null,
        pendingNodes: queue.slice(),
        errorMessage: '',
        responsePreview: '',
        waitingNodeId: null
      })
      this.runTaskNodesSequence({
        templateId: template.templateId,
        template,
        nodes: queue,
        orderForm: orderDetail,
        orderId
      })
    },
    async triggerTemplateTasksForOrder(templateId, orderForm) {
      if (!templateId || !orderForm || !orderForm.orderId) {
        return
      }
      try {
        const template = await this.ensureFlowTemplateDetails(templateId)
        if (!template || !Array.isArray(template.flowNodeList)) {
          return
        }
        const templateInstance = deepClone(template)
        const pendingAsyncNode = this.findAsyncPendingNode(orderForm, templateInstance)
        if (pendingAsyncNode) {
          this.setOrderAutomationState(orderForm.orderId, {
            templateId,
            templateInstance,
            orderForm: deepClone(orderForm),
            status: 'pending_async',
            pendingNodes: [],
            failedNode: null,
            errorMessage: '',
            responsePreview: '',
            waitingNodeId: pendingAsyncNode.waitingNodeId
          })
          return
        }
        const flowNodes = Array.isArray(templateInstance.flowNodeList)
          ? templateInstance.flowNodeList
          : []
        if (this.hasPendingManualNode(orderForm, templateInstance)) {
          return
        }
        const queue = this.buildAutoTriggerQueue(orderForm, templateInstance)
        if (!queue.length) {
          this.setOrderAutomationState(orderForm.orderId, {
            templateId,
            templateInstance,
            orderForm: deepClone(orderForm),
            status: 'success',
            pendingNodes: [],
            failedNode: null,
            errorMessage: '',
            responsePreview: '',
            waitingNodeId: null
          })
          return
        }
        this.setOrderAutomationState(orderForm.orderId, {
          templateId,
          templateInstance,
          orderForm: deepClone(orderForm),
          status: 'running',
          failedNode: null,
          pendingNodes: queue.slice(),
          errorMessage: '',
          responsePreview: '',
          waitingNodeId: null
        })
        await this.runTaskNodesSequence({
          templateId,
          template: templateInstance,
          nodes: queue,
          orderForm,
          orderId: orderForm.orderId
        })
      } catch (error) {
        console.error('自动任务执行失败', error)
        this.$message.error('自动任务执行失败')
      }
    },
    async runTaskNodesSequence({ templateId, template, nodes, orderForm, orderId }) {
      const targetOrderId = orderId || (orderForm && orderForm.orderId) || ''
      if (!template || !Array.isArray(nodes) || !nodes.length) {
        if (targetOrderId) {
          this.setOrderAutomationState(targetOrderId, {
            templateId,
            templateInstance: template,
            status: 'success',
            pendingNodes: [],
            failedNode: null,
            waitingNodeId: null
          })
        }
        return
      }
      const queue = nodes.slice()
      while (queue.length) {
        const currentNode = queue.shift()
        let result
        if (this.isTaskTemplateNode(currentNode)) {
          result = await this.executeTaskNode(currentNode, orderForm)
        } else {
          result = {
            success: false,
            message: '节点需人工处理（未配置自动触发）'
          }
          this.updateNodeExecutionState(currentNode, result)
          if (targetOrderId) {
            await this.refreshOrderRecord(targetOrderId, { silent: true, updateDialog: true })
          }
        }
        if (!result || result.success !== true) {
          this.handleTaskNodeFailure({
            templateId,
            template,
            failedNode: currentNode,
            orderForm,
            pendingNodes: queue.slice(),
            result: result || { success: false, message: '任务执行失败' },
            orderId: targetOrderId
          })
          return
        }
        if (result && result.asyncPending) {
          this.handleAsyncNodeWaiting({
            templateId,
            template,
            waitingNode: currentNode,
            orderForm,
            pendingNodes: queue.slice(),
            result,
            orderId: targetOrderId
          })
          return
        }
      }
      if (targetOrderId) {
        this.setOrderAutomationState(targetOrderId, {
          templateId,
          templateInstance: template,
          status: 'success',
          pendingNodes: [],
          failedNode: null,
          errorMessage: '',
          responsePreview: '',
          waitingNodeId: null
        })
      }
    },
    handleTaskNodeFailure({ templateId, template, failedNode, orderForm, pendingNodes = [], result = {}, orderId }) {
      const errorMessage = result.error || result.message || '任务执行失败'
      if (orderId) {
        this.setOrderAutomationState(orderId, {
          templateId,
          templateInstance: template,
          orderForm,
          status: 'failed',
          failedNode,
          pendingNodes,
          errorMessage,
          responsePreview: this.formatTaskResponsePreview(result.response),
          waitingNodeId: null
        })
        this.refreshOrderRecord(orderId, { silent: true, updateDialog: true }).catch(() => {})
      }
      if (failedNode && failedNode.nodeName) {
        this.$message.error(`节点「${failedNode.nodeName}」自动执行失败，请在订单详情中人工处理`)
      } else {
        this.$message.error('自动任务执行失败，请人工处理')
      }
      if (orderId && failedNode) {
        this.$nextTick(() => {
          this.openManualTaskDialogForOrder(orderId)
        })
      }
    },
    handleAsyncNodeWaiting({ templateId, template, waitingNode, orderForm, pendingNodes = [], result = {}, orderId }) {
      const message = (result && result.message) || '异步接口已触发，等待回调完成'
      if (waitingNode && orderForm) {
        this.markNodeAsProcessing(waitingNode, orderForm, message)
      }
      if (orderId) {
        this.setOrderAutomationState(orderId, {
          templateId,
          templateInstance: template,
          orderForm,
          status: 'pending_async',
          pendingNodes,
          failedNode: null,
          errorMessage: '',
          responsePreview: this.formatTaskResponsePreview(result.response),
          waitingNodeId: this.extractNodeIdentity(waitingNode)
        })
      }
      this.$message.success(message)
    },
    resetManualTaskDialog() {
      this.manualTaskDialog.visible = false
      this.manualTaskDialog.node = null
      this.manualTaskDialog.template = null
      this.manualTaskDialog.templateId = ''
      this.manualTaskDialog.orderForm = null
      this.manualTaskDialog.pendingNodes = []
      this.manualTaskDialog.errorMessage = ''
      this.manualTaskDialog.responsePreview = ''
      this.manualTaskDialog.remark = ''
      this.manualTaskDialog.orderId = ''
      this.manualTaskDialog.submitting = false
    },
    async confirmManualTaskHandling() {
      if (this.manualTaskDialog.submitting) {
        return
      }
      if (!this.manualTaskDialog.node) {
        this.resetManualTaskDialog()
        return
      }
      this.manualTaskDialog.submitting = true
      const orderId = this.manualTaskDialog.orderId
      const templateId = this.manualTaskDialog.templateId
      const template = this.manualTaskDialog.template
      const orderForm = this.manualTaskDialog.orderForm
      const pendingNodes = (() => {
        if (Array.isArray(this.manualTaskDialog.pendingNodes) && this.manualTaskDialog.pendingNodes.length) {
          return this.manualTaskDialog.pendingNodes.slice()
        }
        const state = orderId && this.getOrderAutomationState(orderId)
        console.log("orderId",orderId)
        console.log("this.getOrderAutomationState(orderId)",this.getOrderAutomationState(orderId))
        if (state && Array.isArray(state.pendingNodes)) {
          return state.pendingNodes.slice()
        }
        return []
      })()
      console.log("pendingNodes",pendingNodes)
      const remark = (this.manualTaskDialog.remark || '').trim() || '人工处理完成'
      const orderNodesFromForm = (orderForm && Array.isArray(orderForm.orderNodes))
        ? this.normalizeOrderNodes(orderForm.orderNodes)
        : []
      const fallbackOrderNodeById = node => {
        if (!node) {
          return null
        }
        const targetNodeId = node.nodeId
          || (node.orderNode && node.orderNode.nodeId)
          || ''
        if (!targetNodeId) {
          return null
        }
        return orderNodesFromForm.find(item => item.nodeId === targetNodeId) || null
      }
      const matchedOrderNode = fallbackOrderNodeById(this.manualTaskDialog.node)
      if (matchedOrderNode && !this.manualTaskDialog.node.orderNode) {
        this.$set(this.manualTaskDialog.node, 'orderNode', matchedOrderNode)
      }
      console.log("this.manualTaskDialog.node",this.manualTaskDialog.node)
      const pickValidId = (...candidates) => candidates.find(
        id => id !== undefined && id !== null && `${id}` !== ''
      )
      const orderNodeId = pickValidId(
        this.manualTaskDialog.node.orderNodeId,
        this.manualTaskDialog.node.orderNode && this.manualTaskDialog.node.orderNode.orderNodeId,
        matchedOrderNode && matchedOrderNode.orderNodeId,
        matchedOrderNode && matchedOrderNode.orderNode && matchedOrderNode.orderNode.orderNodeId
      ) || ''
      const nodeId = pickValidId(
        this.manualTaskDialog.node.nodeId,
        this.manualTaskDialog.node.orderNode && this.manualTaskDialog.node.orderNode.nodeId,
        matchedOrderNode && matchedOrderNode.nodeId,
        matchedOrderNode && matchedOrderNode.orderNode && matchedOrderNode.orderNode.nodeId
      ) || ''
      if (orderNodeId && !this.manualTaskDialog.node.orderNodeId) {
        this.$set(this.manualTaskDialog.node, 'orderNodeId', orderNodeId)
      }
      if (orderId && (orderNodeId || nodeId)) {
        try {
          await submitRemark({ orderId, orderNodeId, remark })
          await complateNode({
            orderId,
            orderNodeId,
            nodeId,
            nodeRemark: remark,
            nodeType: '3'
          })
          this.$message.success('人工处理已提交')
        } catch (error) {
          console.error('人工处理提交失败', error)
          this.$message.error('人工处理提交失败，请稍后重试')
          this.manualTaskDialog.submitting = false
          return
        }
      }
      this.updateNodeExecutionState(this.manualTaskDialog.node, {
        success: true,
        manual: true,
        message: remark
      })
      if (orderId) {
        this.setOrderAutomationState(orderId, {
          templateId,
          templateInstance: template,
          orderForm,
          status: pendingNodes.length ? 'running' : 'success',
          pendingNodes,
          failedNode: null,
          errorMessage: '',
          responsePreview: '',
          waitingNodeId: null
        })
        await this.refreshOrderRecord(orderId, { silent: true, updateDialog: true })
      }
      const nextPendingNode = pendingNodes[0]
      const shouldAutoRunNext = nextPendingNode && this.isAutoTriggerNode(nextPendingNode)
      this.resetManualTaskDialog()
      if (templateId && template && orderForm && pendingNodes.length && shouldAutoRunNext) {
        this.runTaskNodesSequence({
          templateId,
          template,
          nodes: pendingNodes,
          orderForm,
          orderId
        })
      }
    },
    formatTaskResponsePreview(value) {
      if (value === undefined || value === null) {
        return ''
      }
      if (typeof value === 'string') {
        return value
      }
      try {
        return JSON.stringify(value, null, 2)
      } catch (error) {
        return String(value)
      }
    },
    isTaskResponseSuccess(response) {
      if (response === undefined || response === null) {
        return false
      }
      if (typeof response === 'boolean') {
        return response
      }
      const direct = this.parseBooleanFlag(response.success)
      if (direct !== null) {
        return direct
      }
      const directCode = this.parseStatusCode(response.code)
      if (directCode !== null) {
        return directCode
      }
      if (response.data) {
        const nested = this.parseBooleanFlag(response.data.success)
        if (nested !== null) {
          return nested
        }
        const nestedCode = this.parseStatusCode(response.data.code)
        if (nestedCode !== null) {
          return nestedCode
        }
      }
      const message = response.message || response.msg
      if (typeof message === 'string' && message.indexOf('成功') !== -1) {
        return true
      }
      return true
    },
    extractErrorMessage(error) {
      if (!error) {
        return ''
      }
      if (typeof error === 'string') {
        return error
      }
      const responseData = error.responseData || (error.response && error.response.data) || {}
      return (
        responseData.message
        || responseData.msg
        || error.message
        || ''
      )
    },
    isDuplicateSubmissionError(error) {
      const message = this.extractErrorMessage(error)
      if (!message) {
        return false
      }
      return message.includes('重复提交')
    },
    parseStatusCode(value) {
      if (value === undefined || value === null) {
        return null
      }
      const code = typeof value === 'string' ? Number.parseInt(value, 10) : value
      if (Number.isNaN(code)) {
        return null
      }
      return code === 200 || code === 0
    },
    parseBooleanFlag(value) {
      if (value === undefined || value === null) {
        return null
      }
      if (typeof value === 'boolean') {
        return value
      }
      if (typeof value === 'number') {
        if (value === 1) return true
        if (value === 0) return false
      }
      if (typeof value === 'string') {
        const lower = value.toLowerCase()
        if (lower === 'true' || lower === 'success' || lower === 'y' || lower === 'yes') {
          return true
        }
        if (lower === 'false' || lower === 'fail' || lower === 'failed' || lower === 'n' || lower === 'no') {
          return false
        }
      }
      return null
    },
    renderNodeStatusText(node) {
      if (node && node.orderNode) {
        const status = `${node.orderNode.nodeStatus || '0'}`
        if (status === '2') {
          return node.orderNode.nodeRemark || '已完成'
        }
        if (status === '1') {
          return node.orderNode.nodeRemark || '进行中'
        }
        if (status === '3') {
          return node.orderNode.nodeRemark || '已超时'
        }
        return node.orderNode.nodeRemark || '未开始'
      }
      const execution = node && node.taskExecution
      if (!execution) {
        return '未执行'
      }
      if (execution.success) {
        return execution.manual ? '人工完成' : '自动完成'
      }
      if (execution.error) {
        return '执行失败'
      }
      return execution.message || '待确认'
    },
    nodeVisualState(node) {
      if (node && node.orderNode) {
        const status = `${node.orderNode.nodeStatus || '0'}`
        if (status === '2') {
          return 'completed'
        }
        if (status === '1') {
          return 'processing'
        }
        if (status === '3') {
          return 'timeout'
        }
        return 'pending'
      }
      if (!node || !node.taskExecution) {
        return 'pending'
      }
      if (node.taskExecution.success) {
        return 'completed'
      }
      if (node.taskExecution.success === false || node.taskExecution.error) {
        return 'timeout'
      }
      return 'pending'
    },
    flowNodeSegmentClass(node, segment) {
      const config = FLOW_SEGMENT_CLASS_MAP[segment]
      if (!config) {
        return ''
      }
      const state = this.nodeVisualState(node)
      return config[state] || config.default
    },
    isSameFlowNode(nodeA, nodeB) {
      if (!nodeA || !nodeB) {
        return false
      }
      const extractKey = node =>
        node.nodeId ||
        node.id ||
        (node.taskTemplate && node.taskTemplate.taskTemplateId) ||
        node.taskTemplateId ||
        node.nodeName ||
        node.name ||
        ''
      const keyA = extractKey(nodeA)
      const keyB = extractKey(nodeB)
      if (keyA && keyB) {
        return keyA === keyB
      }
      return nodeA === nodeB
    },
    shouldShowManualButton(node, orderId) {
      if (!node || !orderId) {
        return false
      }
      const orderNode = node.orderNode
      const triggerMode = this.normalizeTriggerMode((orderNode && orderNode.triggerMode) || node.triggerMode)
      const isAutoTrigger = triggerMode === 'AUTO'
      if (!isAutoTrigger) {
        return orderNode && `${orderNode.nodeStatus || '0'}` !== '2'
      }
      const state = this.getOrderAutomationState(orderId)
      return Boolean(
        state &&
        state.status === 'failed' &&
        this.isSameFlowNode(state.failedNode, node)
      )
    },
    isManualOnlyFlowNode(node) {
      if (!node) {
        return false
      }
      return !this.isTaskTemplateNode(node)
    },
    handleFlowNodeClick(node, event) {
      if (event && typeof event.stopPropagation === 'function') {
        event.stopPropagation()
      }
      if (this.nodeClickHandling) {
        return
      }
      this.nodeClickHandling = true
      try {
        if (!node) {
          return
        }
        const record = this.viewOrderDialog.record
        const orderId = record && record.orderId
        if (!orderId) {
          return
        }
        if (node.orderNode && `${node.orderNode.nodeStatus || '0'}` === '2') {
          return
        }
        const state = this.getOrderAutomationState(orderId)
        if (state && state.failedNode && this.isSameFlowNode(state.failedNode, node)) {
          this.openManualTaskDialogForOrder(orderId)
          return
        }
        const pendingOrderNode = node.orderNode && `${node.orderNode.nodeStatus || '0'}` !== '2'
        if (pendingOrderNode || this.isManualOnlyFlowNode(node)) {
          this.openManualDialogForManualNode({ node, record })
        }
      } finally {
        this.nodeClickHandling = false
      }
    },
    openManualDialogForManualNode({ node, record }) {
      if (!node || !record || !record.flowTemplate) {
        return
      }
      const template = record.flowTemplate
      const templateId = template.templateId || ''
      const defaultMessage = '节点需人工处理（未配置自动触发）'
      const normalizedOrderNodes = this.normalizeOrderNodes(record.orderNodes)
      const matchedOrderNode = this.findOrderNodeForFlowNode(node, normalizedOrderNodes)
      if (matchedOrderNode && !node.orderNode) {
        this.$set(node, 'orderNode', matchedOrderNode)
      }
      if (matchedOrderNode && !node.orderNodeId) {
        this.$set(node, 'orderNodeId', matchedOrderNode.orderNodeId)
      }
      this.manualTaskDialog.visible = true
      this.manualTaskDialog.node = node
      this.manualTaskDialog.template = template
      this.manualTaskDialog.templateId = templateId
      this.manualTaskDialog.orderForm = deepClone(record)
      this.manualTaskDialog.pendingNodes = []
      this.manualTaskDialog.errorMessage =
        (node.taskExecution && (node.taskExecution.error || node.taskExecution.message)) ||
        defaultMessage
      this.manualTaskDialog.responsePreview = ''
      this.manualTaskDialog.remark = ''
      this.manualTaskDialog.orderId = record.orderId || ''
    },
    openManualTaskDialogForOrder(orderId) {
      if (!orderId) {
        return
      }
      const state = this.getOrderAutomationState(orderId)
      if (!state || !state.failedNode) {
        this.$message.warning('暂无需要人工处理的节点')
        return
      }
      this.manualTaskDialog.visible = true
      this.manualTaskDialog.node = state.failedNode
      this.manualTaskDialog.template = state.templateInstance
      this.manualTaskDialog.templateId = state.templateId || ''
      const normalizedOrderForm = state.orderForm ? { ...state.orderForm, orderNodes: this.normalizeOrderNodes(state.orderForm.orderNodes) } : null
      this.manualTaskDialog.orderForm = normalizedOrderForm
      const pendingNodes = Array.isArray(state.pendingNodes) ? state.pendingNodes.slice() : []
      this.manualTaskDialog.pendingNodes = pendingNodes.map(node => this.enrichFlowNodeWithOrderNode(node, normalizedOrderForm && normalizedOrderForm.orderNodes))
      const failedNode = this.enrichFlowNodeWithOrderNode(state.failedNode, normalizedOrderForm && normalizedOrderForm.orderNodes)
      this.manualTaskDialog.node = failedNode
      this.manualTaskDialog.errorMessage = state.errorMessage || ''
      this.manualTaskDialog.responsePreview = state.responsePreview || ''
      this.manualTaskDialog.remark = ''
      this.manualTaskDialog.orderId = orderId
    },
    findOrderNodeForFlowNode(flowNode, orderNodes = []) {
      if (!flowNode || !Array.isArray(orderNodes) || !orderNodes.length) {
        return null
      }
      const targetNodeId = flowNode.nodeId || (flowNode.orderNode && flowNode.orderNode.nodeId)
      if (!targetNodeId) {
        return null
      }
      return orderNodes.find(item => item.nodeId === targetNodeId) || null
    },
    enrichFlowNodeWithOrderNode(flowNode, orderNodes = []) {
      if (!flowNode) {
        return flowNode
      }
      const matched = this.findOrderNodeForFlowNode(flowNode, orderNodes)
      if (matched && !flowNode.orderNode) {
        this.$set(flowNode, 'orderNode', matched)
      }
      if (matched && !flowNode.orderNodeId) {
        this.$set(flowNode, 'orderNodeId', matched.orderNodeId)
      }
      return flowNode
    },
    normalizeOrderAutomationKey(orderId) {
      if (orderId === undefined || orderId === null) {
        return ''
      }
      if (typeof orderId === 'object') {
        const numericId = orderId.orderId || orderId.id || orderId.value
        if (numericId !== undefined && numericId !== null) {
          return `${numericId}`
        }
      }
      return `${orderId}`
    },
    getOrderAutomationState(orderId) {
      const key = this.normalizeOrderAutomationKey(orderId)
      console.log("key",key)
      if (!key) {
        return null
      }
      return this.orderAutomationState[key] || null
    },
    setOrderAutomationState(orderId, payload = {}) {
      const key = this.normalizeOrderAutomationKey(orderId)
      if (!key) {
        return
      }
      const previous = this.orderAutomationState[key] || {}
      const next = {
        templateId: payload.templateId !== undefined ? payload.templateId : previous.templateId,
        templateInstance: payload.templateInstance || previous.templateInstance,
        orderForm: payload.orderForm || previous.orderForm,
        status: payload.status || previous.status || 'pending',
        pendingNodes: payload.pendingNodes !== undefined
          ? payload.pendingNodes.slice()
          : (previous.pendingNodes ? previous.pendingNodes.slice() : []),
        failedNode: payload.failedNode !== undefined ? payload.failedNode : previous.failedNode,
        errorMessage: payload.errorMessage !== undefined ? payload.errorMessage : previous.errorMessage,
        responsePreview: payload.responsePreview !== undefined ? payload.responsePreview : previous.responsePreview,
        waitingNodeId: payload.waitingNodeId !== undefined ? payload.waitingNodeId : previous.waitingNodeId
      }
      this.$set(this.orderAutomationState, key, next)
    },
    statusTagType(status) {
      const mapping = {
        待处理: 'info',
        处理中: 'warning',
        待入池: 'default',
         已入池: 'success',
         生产中: 'warning',
         已完成: 'success',
         已取消: 'danger'
       }
       return mapping[status] || 'info'
     },
    priorityTagType(priority) {
      const mapping = {
        low: 'info',
        normal: 'primary',
        high: 'warning',
        urgent: 'danger'
      }
      return mapping[priority] || 'info'
    },
    orderRowClassName({ row }) {
      if (!row || !row.orderId) {
        return ''
      }
      const state = this.getOrderAutomationState(row.orderId)
      return state && state.status === 'failed' ? 'order-row-failed' : ''
    },
    handleOrderSelectionChange(val) {
      this.selectedOrders = val
    },
    openOrderDialog(order) {
      if (order) {
        this.orderDialog.title = '编辑订单'
        this.orderDialog.isEdit = true
        this.orderDialog.form = this.normalizeOrder(order)
        if (!this.orderDialog.form.flowTemplate) {
          this.orderDialog.form.flowTemplate = this.findTemplateById(this.orderDialog.form.templateId)
        }
      } else {
        this.orderDialog.title = '新增订单'
        this.orderDialog.isEdit = false
        const defaultTemplateId = this.flowTemplateOptions.length === 1 ? this.flowTemplateOptions[0].templateId : ''
        this.orderDialog.form = {
          orderId: '',
          previewImage: '',
          quantity: 1,
          remark: '',
          mainMaterial: '',
          craftRequirements: '',
          orderStatus: '待处理',
          createdAt: '',
          updatedAt: '',
          customerInfo: '',
          priority: 'normal',
          deliveryDate: '',
          sizeRequirement: '',
          colorRequirement: '',
          fileFormat: '',
          templateId: defaultTemplateId,
          flowTemplate: this.findTemplateById(defaultTemplateId)
        }
      }
      if (this.orderDialog.form.templateId) {
        this.ensureFlowTemplateDetails(this.orderDialog.form.templateId).then(template => {
          if (template) {
            this.$set(this.orderDialog.form, 'flowTemplate', template)
          }
        })
      }
      this.orderDialog.submitting = false
      this.$nextTick(() => {
        if (this.$refs.orderForm) {
          this.$refs.orderForm.clearValidate()
        }
      })
      this.orderDialog.visible = true
    },
    submitOrder() {
      if (!this.$refs.orderForm) return
      this.$refs.orderForm.validate(async valid => {
        if (!valid) return
        const payload = this.buildOrderPayload(this.orderDialog.form)
        const orderFormSnapshot = deepClone(this.orderDialog.form)
        this.orderDialog.submitting = true
        try {
          if (this.orderDialog.isEdit) {
            await updateOrderPool(payload)
          } else {
            await addOrderPool(payload)
          }
          if (!this.orderDialog.isEdit) {
            this.triggerTemplateTasksForOrder(payload.templateId, orderFormSnapshot)
              .catch(error => console.error('自动任务执行失败', error))
          }
          this.$message.success('保存成功')
          this.orderDialog.visible = false
          await this.fetchOrders()
        } catch (error) {
          this.$message.error('保存失败')
        } finally {
          this.orderDialog.submitting = false
        }
      })
    },
    buildOrderPayload(form = {}) {
      return {
        orderId: form.orderId,
        previewImage: form.previewImage || '',
        quantity: Number(form.quantity || 0),
        remark: form.remark || '',
        mainMaterial: form.mainMaterial || '',
        craftRequirements: form.craftRequirements || '',
        orderStatus: form.orderStatus || '待处理',
        customerInfo: form.customerInfo || '',
        priority: form.priority || 'normal',
        deliveryDate: form.deliveryDate ? this.formatDateValue(form.deliveryDate) : null,
        sizeRequirement: form.sizeRequirement || '',
        colorRequirement: form.colorRequirement || '',
        fileFormat: form.fileFormat || '',
        templateId: form.templateId || ''
      }
    },
    async deleteOrdersAndProcesses(orderIds = []) {
      const ids = Array.isArray(orderIds)
        ? orderIds.filter(id => !!id)
        : []
      if (!ids.length) {
        return
      }
      await removeOrderPool(ids.join(','))
    },
    handleDeleteOrder(order) {
      this.$confirm(`确认删除订单【${order.orderId}】吗？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await this.deleteOrdersAndProcesses([order.orderId])
          this.$message.success('删除成功')
          await this.fetchOrders()
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    handleBatchDeleteOrders() {
      this.$confirm(`确认删除选中的 ${this.selectedOrders.length} 条订单吗？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.selectedOrders.map(item => item.orderId)
          if (!ids.length) {
            return
          }
          await this.deleteOrdersAndProcesses(ids)
          this.$message.success('删除成功')
          this.selectedOrders = []
          await this.fetchOrders()
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async viewOrder(order) {
      if (!order || !order.orderId) {
        this.$message.warning('订单数据异常，请稍后重试')
        return
      }
      this.viewOrderDialog.record = this.buildViewOrderRecord(order)
      this.viewOrderDialog.visible = true
      await this.refreshOrderRecord(order.orderId, { silent: true, updateDialog: true, withLoading: true })
    },
    async openPoolAssignmentDialog() {
      if (!this.selectedOrders.length) {
        this.$message.warning('请先选择至少一个订单')
        return
      }
      if (!this.flowPoolOptions.length) {
        this.fetchFlows()
      }
      const refreshedOrders = []
      const missingOrders = []
      for (const item of this.selectedOrders) {
        const detail = await this.refreshOrderRecord(item.orderId, { silent: true, updateDialog: false })
        if (detail) {
          refreshedOrders.push({ ...detail, quantity: Number(detail.quantity || 0) })
        } else {
          missingOrders.push(item.orderId)
        }
      }
      if (missingOrders.length) {
        this.$message.warning(`以下订单已不存在或已被移除：${missingOrders.join('、')}`)
      }
      if (!refreshedOrders.length) {
        return
      }
      this.selectedOrders = refreshedOrders
      const orders = refreshedOrders
      const timestamp = Date.now()
      this.poolAssignmentDialog.orders = orders
      this.poolAssignmentDialog.allocations = orders.map((order, index) => ({
        key: `${order.orderId}-${timestamp}-${index}`,
        orderId: order.orderId,
        flowId: '',
        quantity: order.quantity
      }))
      this.poolAssignmentDialog.submitting = false
      this.poolAssignmentDialog.visible = true
    },
    allocationsByOrder(orderId) {
      return this.poolAssignmentDialog.allocations.filter(item => item.orderId === orderId)
    },
    allocationTotal(orderId) {
      return this.allocationsByOrder(orderId).reduce((sum, item) => sum + Number(item.quantity || 0), 0)
    },
    addAllocation(order) {
      if (!order || !order.orderId) return
      const remaining = Math.max(1, Number(order.quantity || 0) - this.allocationTotal(order.orderId))
      this.poolAssignmentDialog.allocations.push({
        key: `${order.orderId}-${Date.now()}-${Math.random()}`,
        orderId: order.orderId,
        flowId: '',
        quantity: remaining
      })
    },
    removeAllocation(orderId, index) {
      const list = this.allocationsByOrder(orderId)
      const target = list[index]
      if (!target) return
      const globalIndex = this.poolAssignmentDialog.allocations.findIndex(item => item === target)
      if (globalIndex !== -1) {
        this.poolAssignmentDialog.allocations.splice(globalIndex, 1)
      }
    },
    validateAllocations() {
      if (!this.poolAssignmentDialog.orders.length) {
        this.$message.warning('请选择订单')
        return false
      }
      if (!this.poolAssignmentDialog.allocations.length) {
        this.$message.warning('请为订单创建分配记录')
        return false
      }
      for (const order of this.poolAssignmentDialog.orders) {
        const allocations = this.allocationsByOrder(order.orderId).filter(item => item.flowId && Number(item.quantity || 0) > 0)
        if (!allocations.length) {
          this.$message.warning(`请为订单【${order.orderId}】选择至少一个生产池`)
          return false
        }
        const total = allocations.reduce((sum, item) => sum + Number(item.quantity || 0), 0)
        if (total > Number(order.quantity || 0)) {
          this.$message.warning(`订单【${order.orderId}】分配数量不能超过订单数量`)
          return false
        }
      }
      const flowIds = this.poolAssignmentDialog.allocations.map(item => item.flowId).filter(Boolean)
      if (!flowIds.length) {
        this.$message.warning('请选择生产池后再提交')
        return false
      }
      return true
    },
    mergeOrderAllocations(existing = [], extra = {}) {
      const map = {}
      const normalizedExisting = Array.isArray(existing)
        ? existing.filter(item => item && item.orderId).map(item => ({
          orderId: item.orderId,
          quantity: Number(item.quantity || 0)
        }))
        : []
      normalizedExisting.forEach(item => {
        map[item.orderId] = (map[item.orderId] || 0) + Number(item.quantity || 0)
      })
      Object.keys(extra || {}).forEach(orderId => {
        map[orderId] = (map[orderId] || 0) + Number(extra[orderId] || 0)
      })
      return Object.keys(map).map(orderId => ({ orderId, quantity: map[orderId] }))
    },
    async ensureFlowPoolDetail(flowId) {
      if (!flowId) return null
      if (this.flowPoolDetailMap[flowId]) {
        return deepClone(this.flowPoolDetailMap[flowId])
      }
      try {
        const { data } = await getFlowPoolDetail(flowId)
        const normalized = this.normalizeFlow(data || {})
        this.flowPoolDetailMap[flowId] = normalized
        return deepClone(normalized)
      } catch (error) {
        console.error(error)
        return null
      }
    },
    buildFlowPoolPayload(flow = {}, orderQuantities = {}) {
      const base = this.normalizeFlow(flow)
      const templateId = base.templateId
        || (base.flowTemplate && base.flowTemplate.templateId)
        || ''
      const orderAllocations = this.mergeOrderAllocations(base.orderAllocations, orderQuantities)
      const orderIds = Array.from(new Set([...(base.orderIds || []), ...Object.keys(orderQuantities || {})]))
      const totalQuantity = orderAllocations.reduce((sum, item) => sum + Number(item.quantity || 0), 0) || base.totalQuantity
      return {
        flowId: base.flowId,
        templateId,
        flowStatus: base.flowStatus || 'pending',
        orderIds,
        totalQuantity,
        materialsSummary: Array.isArray(base.materialsSummary)
          ? base.materialsSummary.map((item, index) => ({
            ...item,
            material: item.material || '',
            quantity: Number(item.quantity || 0),
            sortOrder: item.sortOrder !== undefined ? item.sortOrder : index
          }))
          : [],
        priority: base.priority || 'normal',
        scheduledStart: base.scheduledStart ? this.formatDateValue(base.scheduledStart) : null,
        scheduledEnd: base.scheduledEnd ? this.formatDateValue(base.scheduledEnd) : null,
        actualStart: base.actualStart ? this.formatDateValue(base.actualStart) : null,
        actualEnd: base.actualEnd ? this.formatDateValue(base.actualEnd) : null,
        assignedOperator: base.assignedOperator || '',
        productionNotes: base.productionNotes || '',
        flowTemplate: base.flowTemplate || null,
        process: Array.isArray(base.process)
          ? base.process.map((step, index) => ({
            stepId: step.stepId,
            nodeId: step.nodeId,
            stepName: step.stepName,
            stepStatus: step.stepStatus || 'pending',
            remark: step.remark,
            sortOrder: step.sortOrder !== undefined ? step.sortOrder : index
          }))
          : [],
        createdAt: base.createdAt || '',
        updatedAt: this.nowDateTime(),
        orderAllocations
      }
    },
    async applyFlowTemplateToOrders(flowDetail, orderIds = []) {
      if (!flowDetail || !flowDetail.templateId || !Array.isArray(orderIds) || !orderIds.length) {
        return
      }
      const template = flowDetail.flowTemplate
        || await this.ensureFlowTemplateDetails(flowDetail.templateId)
      if (!template) {
        return
      }
      for (const orderId of orderIds) {
        let orderDetail = this.orderList.find(item => item.orderId === orderId) || null
        if (!orderDetail || !Array.isArray(orderDetail.orderNodes) || !orderDetail.orderNodes.length) {
          orderDetail = await this.refreshOrderRecord(orderId, { silent: true, updateDialog: false }) || orderDetail
        }
        if (!orderDetail) {
          continue
        }
        const orderForm = Object.assign({}, deepClone(orderDetail), {
          flowTemplate: template,
          templateId: flowDetail.templateId,
          flowPoolId: flowDetail.flowId,
          flowPool: flowDetail
        })
        await this.triggerTemplateTasksForOrder(flowDetail.templateId, orderForm)
      }
    },
    async submitPoolAssignments() {
      if (!this.validateAllocations()) {
        return
      }
      const grouped = {}
      this.poolAssignmentDialog.allocations.forEach(item => {
        if (!item.flowId || !item.orderId) return
        const quantity = Number(item.quantity || 0)
        if (!grouped[item.flowId]) {
          grouped[item.flowId] = { total: 0, orders: {} }
        }
        grouped[item.flowId].total += quantity
        grouped[item.flowId].orders[item.orderId] = (grouped[item.flowId].orders[item.orderId] || 0) + quantity
      })
      await this.removeInvalidOrderAllocations(grouped)
      const flowIds = Object.keys(grouped)
      if (!flowIds.length) {
        this.$message.warning('请选择生产池后再提交')
        return
      }
      this.poolAssignmentDialog.submitting = true
      try {
        for (const flowId of flowIds) {
          const detail = await this.ensureFlowPoolDetail(flowId)
          if (!detail) {
            this.$message.warning(`生产池【${flowId}】不存在或已删除`)
            continue
          }
          const flowTemplate = detail.flowTemplate || await this.ensureFlowTemplateDetails(detail.templateId)
          const payload = this.buildFlowPoolPayload({
            ...detail,
            templateId: detail.templateId || (flowTemplate && flowTemplate.templateId) || '',
            flowTemplate
          }, grouped[flowId].orders)
          await updateFlowPool(payload)
          await this.applyFlowTemplateToOrders({
            ...detail,
            templateId: payload.templateId,
            flowTemplate: payload.flowTemplate || flowTemplate
          }, Object.keys(grouped[flowId].orders || {}))
        }
        this.$message.success('分配成功')
        this.poolAssignmentDialog.visible = false
        this.selectedOrders = []
        await Promise.all([this.fetchFlows(), this.fetchOrders()])
      } catch (error) {
        console.error(error)
        this.$message.error('分配失败，请稍后重试')
      } finally {
        this.poolAssignmentDialog.submitting = false
      }
    },
    async removeInvalidOrderAllocations(grouped) {
      const orderIds = new Set()
      Object.values(grouped).forEach(item => {
        Object.keys(item.orders || {}).forEach(orderId => orderIds.add(orderId))
      })
      if (!orderIds.size) return
      const invalid = []
      for (const orderId of orderIds) {
        const detail = await this.refreshOrderRecord(orderId, { silent: true, updateDialog: false })
        if (!detail) {
          invalid.push(orderId)
        }
      }
      if (!invalid.length) return
      invalid.forEach(orderId => {
        Object.keys(grouped).forEach(flowId => {
          const orders = grouped[flowId].orders || {}
          if (orders[orderId]) {
            grouped[flowId].total -= Number(orders[orderId] || 0)
            delete orders[orderId]
          }
          if (!Object.keys(orders).length) {
            delete grouped[flowId]
          }
        })
      })
      this.poolAssignmentDialog.allocations = this.poolAssignmentDialog.allocations.filter(
        item => !invalid.includes(item.orderId)
      )
      this.$message.warning(`以下订单已不存在或已被移除，已从分配中删除：${invalid.join('、')}`)
    },
    async handleOrderTemplateSelect(templateId) {
      this.orderDialog.form.templateId = templateId
      const template = await this.ensureFlowTemplateDetails(templateId)
      this.orderDialog.form.flowTemplate = template || this.findTemplateById(templateId)
    },
    findTemplateById(templateId) {
      if (!templateId) {
        return null
      }
      if (this.flowTemplateDetailMap[templateId]) {
        return deepClone(this.flowTemplateDetailMap[templateId])
      }
      const fromOptions = this.flowTemplateOptions.find(item => item.templateId === templateId)
      if (fromOptions) {
        return deepClone(fromOptions)
      }
      const fromOrders = this.orderList.find(item => item.templateId === templateId && item.flowTemplate)
      return fromOrders && fromOrders.flowTemplate ? deepClone(fromOrders.flowTemplate) : null
    },
    buildViewOrderRecord(order) {
      if (!order) {
        return null
      }
      const record = deepClone(order)
      if (!record.flowTemplate && record.templateId) {
        record.flowTemplate = this.findTemplateById(record.templateId)
      }
      const automationState = record.orderId ? this.getOrderAutomationState(record.orderId) : null
      if (
        (!record.flowTemplate || !Array.isArray(record.flowTemplate.flowNodeList) || !record.flowTemplate.flowNodeList.length)
        && automationState
        && automationState.templateInstance
      ) {
        record.flowTemplate = automationState.templateInstance
      } else if (automationState && automationState.templateInstance) {
        record.flowTemplate = this.mergeTemplateExecutionState(
          record.flowTemplate || automationState.templateInstance,
          automationState.templateInstance
        )
      }
      return record
    },
    mergeTemplateExecutionState(targetTemplate, stateTemplate) {
      if (!targetTemplate && !stateTemplate) {
        return null
      }
      if (!targetTemplate) {
        return deepClone(stateTemplate)
      }
      if (!stateTemplate) {
        return deepClone(targetTemplate)
      }
      const merged = deepClone(targetTemplate)
      const targetNodes = Array.isArray(merged.flowNodeList) ? merged.flowNodeList : []
      const stateNodes = Array.isArray(stateTemplate.flowNodeList) ? stateTemplate.flowNodeList : []
      const findStateNode = targetNode => stateNodes.find(item => this.isSameFlowNode(item, targetNode))
      targetNodes.forEach((node, index) => {
        const matched = findStateNode(node)
        if (!matched) {
          return
        }
        if (matched.taskExecution) {
          this.$set(node, 'taskExecution', deepClone(matched.taskExecution))
        }
        if (matched.nodeStatus !== undefined) {
          this.$set(node, 'nodeStatus', matched.nodeStatus)
        }
        if (matched.nodeRemark !== undefined) {
          this.$set(node, 'nodeRemark', matched.nodeRemark)
        }
        this.$set(targetNodes, index, node)
      })
      merged.flowNodeList = targetNodes
      return merged
    },
    formatDateDisplay(value) {
      const result = this.formatDateValue(value)
      return result || '—'
    },
    formatDateValue(value) {
      return formatDateHelper(value)
    },
    toTimestamp(value) {
      if (!value) return 0
      const date = value instanceof Date ? value : new Date(value)
      return Number.isNaN(date.getTime()) ? 0 : date.getTime()
    },
    flowStatusTagType(status) {
      const map = {
        pending: 'info',
        file_preparing: 'warning',
        file_ready: 'warning',
        layout_designing: 'warning',
        layout_approved: 'warning',
        printing: 'warning',
        printed: 'info',
        cutting: 'warning',
        cut_completed: 'success',
        quality_check: 'warning',
        completed: 'success',
        cancelled: 'info'
      }
      return map[status] || 'info'
    },
    nowDateTime() {
      return nowDateTimeHelper()
    }
  }
}
</script>

 <style lang="scss" scoped>
 .productionflow-order-page {
   display: flex;
   flex-direction: column;
   height: 100%;

   .order-card {
     flex: 1;
   }

   .card-header {
     display: flex;
     justify-content: space-between;
     align-items: center;
   }

   .header-actions {
     display: flex;
     align-items: center;

     > * {
       margin-left: 10px;
     }

     .header-search {
       width: 220px;
     }
   }

   .table-toolbar {
     margin-bottom: 12px;

     .el-button + .el-button {
       margin-left: 10px;
     }
   }

   .order-preview {
     width: 80px;
     height: 60px;
     border-radius: 4px;
     background-color: #f5f7fa;

     &.placeholder {
       display: flex;
       align-items: center;
       justify-content: center;
       color: #909399;
       font-size: 12px;
     }
   }

  .section-title {
    margin: 18px 0 10px;
    font-size: 15px;
    font-weight: 600;
  }

 .template-summary {
    margin-bottom: 12px;
    font-size: 14px;
    color: #606266;
  }

  .flow-template-visual {
    overflow-x: auto;
  }

  .flow-pool-track {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 8px;
    padding: 6px 0 10px;
  }

  .flow-pool-chip {
    display: flex;
    flex-direction: column;
    padding: 8px 10px;
    background: #f5f7fa;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    min-width: 160px;
    box-sizing: border-box;
  }

  .flow-pool-title {
    font-weight: 600;
    color: #303133;
    margin-bottom: 4px;
  }

  .flow-pool-template {
    color: #606266;
    font-weight: 400;
    margin-left: 6px;
  }

  .flow-pool-time {
    margin-top: 4px;
    color: #909399;
    font-size: 12px;
  }

  .flow-pool-arrow {
    color: #c0c4cc;
    font-size: 18px;
    padding: 0 4px;
  }

  .flow-track {
    display: flex;
    padding: 10px 0;
  }

  .flow-node-wrapper {
    margin-right: 10px;
    min-width: 140px;
  }

  .flow-node-wrapper.manual-node-clickable {
    cursor: pointer;
  }

  .flow-node-wrapper.manual-node-clickable .flow-node-name {
    text-decoration: underline;
  }

  .flow-node-name {
    color: #fff;
    font-size: 13px;
  }

  .node-extra {
    font-size: 12px;
    color: #606266;
    margin-top: 6px;

    .node-status-text {
      font-weight: 500;
    }

    .node-meta {
      color: #909399;
      margin-top: 4px;
    }

    .manual-handle-btn {
      padding: 0;
      margin-top: 4px;
    }
  }

  .template-node {
    display: flex;
    align-items: center;

    .node-name {
      font-weight: 500;
      margin-right: 8px;
    }

    .node-type-tag {
      margin-left: 4px;
    }

    .node-status-tag {
      margin-left: 8px;
    }
  }

  .node-status-message {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
  }

  .template-empty {
    color: #909399;
    font-size: 13px;
  }

  .order-detail-body {
    min-height: 200px;
  }

  .mr5 {
    margin-right: 5px;
    margin-bottom: 4px;
  }

  .pool-assignment {
    min-height: 160px;
  }

  .mb12 {
    margin-bottom: 12px;
  }

  .empty-assignment {
    padding: 20px 0;
    text-align: center;
    color: #909399;
  }

  .order-allocation-card {
    border: 1px solid #ebeef5;
    border-radius: 6px;
    padding: 14px;
    margin-bottom: 14px;
  }

  .order-allocation-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    font-weight: 600;
  }

  .order-basic {
    display: flex;
    flex-direction: column;
    gap: 4px;
    font-size: 13px;
  }

  .order-quantity {
    color: #606266;
    font-size: 13px;
  }

  .allocation-table {
    margin-bottom: 8px;
  }

  .allocation-actions {
    text-align: right;
  }
}

::v-deep .order-row-failed td {
  background-color: #fde2e2 !important;
}

.arrow-first {
  display: flex;
}

.first-center,
.first-center-processing,
.first-center-completed,
.first-center-timeout {
  width: 100px;
  text-align: center;
}

.first-center {
  background-color: #cbcdd4;
}

.first-center-processing {
  background-color: #409eff;
}

.first-center-completed {
  background-color: #70eaa9;
}

.first-center-timeout {
  background-color: #f56c6c;
}

.first-right,
.first-right-processing,
.first-right-completed,
.first-right-timeout {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}

.first-right-processing {
  border-color: transparent transparent transparent #409eff;
}

.first-right-completed {
  border-color: transparent transparent transparent #70eaa9;
}

.first-right-timeout {
  border-color: transparent transparent transparent #f56c6c;
}

.arrow {
  display: flex;
  margin-left: -25px;
}

.arrow-left,
.arrow-left-processing,
.arrow-left-completed,
.arrow-left-timeout {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.arrow-left-processing {
  border-color: #409eff #409eff #409eff transparent;
}

.arrow-left-completed {
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}

.arrow-left-timeout {
  border-color: #f56c6c #f56c6c #f56c6c transparent;
}

.arrow-center,
.arrow-center-processing,
.arrow-center-completed,
.arrow-center-timeout {
  width: 100px;
  text-align: center;
}

.arrow-center {
  background-color: #cbcdd4;
}

.arrow-center-processing {
  background-color: #409eff;
}

.arrow-center-completed {
  background-color: #70eaa9;
}

.arrow-center-timeout {
  background-color: #f56c6c;
}

.arrow-right,
.arrow-right-processing,
.arrow-right-completed,
.arrow-right-timeout {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}

.arrow-right-processing {
  border-color: transparent transparent transparent #409eff;
}

.arrow-right-completed {
  border-color: transparent transparent transparent #70eaa9;
}

.arrow-right-timeout {
  border-color: transparent transparent transparent #f56c6c;
}

.arrow-last {
  display: flex;
  margin-left: -25px;
}

.last-left,
.last-left-processing,
.last-left-completed,
.last-left-timeout {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.last-left-processing {
  border-color: #409eff #409eff #409eff transparent;
}

.last-left-completed {
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}

.last-left-timeout {
  border-color: #f56c6c #f56c6c #f56c6c transparent;
}

.last-center,
.last-center-processing,
.last-center-completed,
.last-center-timeout {
  width: 100px;
  text-align: center;
}

.last-center {
  background-color: #cbcdd4;
}

.last-center-processing {
  background-color: #409eff;
}

.last-center-completed {
  background-color: #70eaa9;
}

.last-center-timeout {
  background-color: #f56c6c;
}

.last-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent transparent;
}

.manual-task-dialog {
  .manual-tip {
    font-size: 13px;
    color: #606266;
    margin-bottom: 10px;
  }

  .response-preview {
    margin-top: 12px;
    background: #f7f8fa;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    padding: 10px;

    .preview-title {
      font-size: 13px;
      color: #909399;
      margin-bottom: 6px;
    }

    pre {
      margin: 0;
      font-size: 12px;
      color: #606266;
      white-space: pre-wrap;
      word-break: break-all;
    }
  }
}

.mb12 {
  margin-bottom: 12px;
}
 </style>
