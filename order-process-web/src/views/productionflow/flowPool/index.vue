 <template>
   <div class="app-container productionflow-flow-page">
     <el-card shadow="hover" class="flow-card">
       <div slot="header" class="card-header">
         <span>生产池</span>
         <div class="header-actions">
           <el-input
             v-model="flowSearch.keyword"
             placeholder="搜索生产池ID/负责人"
             size="small"
             clearable
             class="header-search"
             @clear="handleFlowQuery"
             @keyup.enter.native="handleFlowQuery"
           >
             <el-button slot="append" icon="el-icon-search" @click="handleFlowQuery"></el-button>
           </el-input>
           <el-select
             v-model="flowSearch.status"
             placeholder="生产状态"
             size="small"
             clearable
             @change="handleFlowQuery"
           >
             <el-option
               v-for="item in flowStatusOptions"
               :key="item"
               :label="flowStatusLabels[item] || item"
               :value="item"
             />
           </el-select>
         </div>
       </div>

       <div class="table-toolbar">
         <el-button type="primary" size="small" icon="el-icon-plus" @click="openFlowDialog()">
           新增生产池
         </el-button>
         <el-button type="info" size="small" icon="el-icon-refresh" @click="resetFlowQuery">
           重置筛选
         </el-button>
       </div>

      <el-table
        :data="filteredFlows"
        border
        height="600"
        stripe
        :row-key="row => row.flowId"
        v-loading="loading"
      >
        <el-table-column prop="flowId" label="生产池ID" width="160" show-overflow-tooltip />
        <el-table-column label="流程模板" prop="templateId" width="160" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ (scope.row.flowTemplate && scope.row.flowTemplate.templateName) || scope.row.templateId || '—' }}
          </template>
        </el-table-column>
        <el-table-column label="关联订单" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ Array.isArray(scope.row.orderIds) && scope.row.orderIds.length ? scope.row.orderIds.join(', ') : '—' }}
          </template>
        </el-table-column>
         <el-table-column prop="flowStatus" label="状态" width="120">
           <template slot-scope="scope">
             <el-tag :type="flowStatusTagType(scope.row.flowStatus)">
               {{ flowStatusLabels[scope.row.flowStatus] || scope.row.flowStatus }}
             </el-tag>
           </template>
         </el-table-column>
         <el-table-column prop="totalQuantity" label="订单总数" width="100" align="center" />
         <el-table-column prop="priority" label="优先级" width="90">
           <template slot-scope="scope">
             <el-tag :type="priorityTagType(scope.row.priority)">
               {{ priorityLabels[scope.row.priority] }}
             </el-tag>
           </template>
         </el-table-column>
         <el-table-column prop="scheduledEnd" label="预计完成" width="160" show-overflow-tooltip />
         <el-table-column prop="assignedOperator" label="负责人" width="120" show-overflow-tooltip />
         <el-table-column label="操作" width="200" fixed="right">
           <template slot-scope="scope">
             <el-button type="text" size="mini" @click="viewFlow(scope.row)">查看</el-button>
             <el-button type="text" size="mini" @click="openFlowDialog(scope.row)">编辑</el-button>
             <el-button type="text" size="mini" @click="handleDeleteFlow(scope.row)">删除</el-button>
           </template>
         </el-table-column>
       </el-table>
     </el-card>

     <!-- 新增/编辑生产池 -->
     <el-dialog :title="flowDialog.title" :visible.sync="flowDialog.visible" width="880px">
      <el-form ref="flowForm" :model="flowDialog.form" :rules="flowRules" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产池ID" prop="flowId">
              <el-input v-model="flowDialog.form.flowId" placeholder="请输入生产池ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流程模板" prop="templateId">
              <el-select
                v-model="flowDialog.form.templateId"
                placeholder="请选择流程模板"
                filterable
                @change="handleTemplateChange"
              >
                <el-option
                  v-for="item in templateOptions"
                  :key="item.templateId"
                  :label="item.templateName"
                  :value="item.templateId"
                />
              </el-select>
            </el-form-item>
          </el-col>
           <el-col :span="12">
             <el-form-item label="生产状态" prop="flowStatus">
              <el-select v-model="flowDialog.form.flowStatus" placeholder="请选择生产状态">
                 <el-option
                   v-for="item in flowStatusOptions"
                   :key="item"
                   :label="flowStatusLabels[item] || item"
                   :value="item"
                 />
               </el-select>
             </el-form-item>
           </el-col>
           <el-col :span="24">
             <el-form-item label="关联订单">
               <el-select
                 v-model="flowDialog.form.orderIds"
                 multiple
                 filterable
                 allow-create
                 default-first-option
                 placeholder="请选择或输入关联订单"
                 @change="updateFlowMetrics"
               >
                 <el-option
                   v-for="order in orderList"
                   :key="order.orderId"
                   :label="`${order.orderId}（${order.customerInfo}）`"
                   :value="order.orderId"
                 />
               </el-select>
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="优先级" prop="priority">
               <el-select v-model="flowDialog.form.priority" placeholder="请选择优先级">
                 <el-option v-for="item in priorityOptions" :key="item" :label="priorityLabels[item]" :value="item" />
               </el-select>
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="预计开始时间">
               <el-date-picker
                 v-model="flowDialog.form.scheduledStart"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="预计完成时间">
               <el-date-picker
                 v-model="flowDialog.form.scheduledEnd"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="实际开始时间">
               <el-date-picker
                 v-model="flowDialog.form.actualStart"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="实际完成时间">
               <el-date-picker
                 v-model="flowDialog.form.actualEnd"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
          <el-col :span="12">
            <el-form-item label="负责人">
              <el-input v-model="flowDialog.form.assignedOperator" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产备注">
              <el-input v-model="flowDialog.form.productionNotes" placeholder="请输入生产备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

       <div class="flow-summary">
         <div class="summary-header">
           <h4 class="section-title">关联订单</h4>
         </div>
         <el-tag
           v-for="orderId in flowDialog.form.orderIds"
           :key="orderId"
           type="info"
           effect="plain"
           class="mr5"
         >
           {{ orderId }}
         </el-tag>

         <div v-if="dialogTemplate && dialogAssociatedOrders.length" class="order-flow-preview">
           <h4 class="section-title">关联订单流程</h4>
           <el-collapse>
             <el-collapse-item
               v-for="order in dialogAssociatedOrders"
               :key="order.orderId"
               :title="`${order.orderId}（${order.customerInfo || '未填写'}）`"
             >
               <el-steps :active="orderFlowActiveStep(order.nodes)" align-center finish-status="success">
                 <el-step
                   v-for="(step, idx) in order.nodes"
                   :key="`${order.orderId}-${idx}`"
                   :title="step.stepName || `步骤${idx + 1}`"
                   :status="flowStepStatus(step.stepStatus)"
                 >
                   <template slot="description">
                     <div class="step-detail">
                       <el-tag size="mini" :type="flowStepStatusTag(step.stepStatus)">
                         {{ flowProcessStatusText(step.stepStatus) }}
                       </el-tag>
                       <div class="step-remark" v-if="step.remark">{{ step.remark }}</div>
                     </div>
                   </template>
                 </el-step>
               </el-steps>
             </el-collapse-item>
           </el-collapse>
         </div>
       </div>

       <span slot="footer" class="dialog-footer">
         <el-button @click="flowDialog.visible = false">取 消</el-button>
         <el-button type="primary" @click="submitFlow">保 存</el-button>
       </span>
     </el-dialog>

     <!-- 生产池详情 -->
     <el-dialog :title="viewFlowDialog.title" :visible.sync="viewFlowDialog.visible" width="820px">
       <div v-if="viewFlowDialog.record">
         <el-descriptions :column="2" border label-class-name="desc-label">
           <el-descriptions-item label="生产池ID">{{ viewFlowDialog.record.flowId }}</el-descriptions-item>
           <el-descriptions-item label="状态">{{ flowStatusLabels[viewFlowDialog.record.flowStatus] || viewFlowDialog.record.flowStatus }}</el-descriptions-item>
           <el-descriptions-item label="订单总数量">{{ viewFlowDialog.record.totalQuantity }}</el-descriptions-item>
           <el-descriptions-item label="优先级">{{ priorityLabels[viewFlowDialog.record.priority] }}</el-descriptions-item>
           <el-descriptions-item label="负责人">{{ viewFlowDialog.record.assignedOperator || '—' }}</el-descriptions-item>
           <el-descriptions-item label="预计完成时间">{{ formatDateDisplay(viewFlowDialog.record.scheduledEnd) }}</el-descriptions-item>
           <el-descriptions-item label="实际开始时间">{{ formatDateDisplay(viewFlowDialog.record.actualStart) }}</el-descriptions-item>
           <el-descriptions-item label="实际完成时间">{{ formatDateDisplay(viewFlowDialog.record.actualEnd) }}</el-descriptions-item>
           <el-descriptions-item label="创建时间">{{ formatDateDisplay(viewFlowDialog.record.createdAt) }}</el-descriptions-item>
           <el-descriptions-item label="更新时间">{{ formatDateDisplay(viewFlowDialog.record.updatedAt) }}</el-descriptions-item>
           <el-descriptions-item label="生产备注" :span="2">{{ viewFlowDialog.record.productionNotes || '—' }}</el-descriptions-item>
         </el-descriptions>

         <h4 class="section-title">生产池流程模板</h4>
         <div v-if="viewFlowTemplateNodes.length" class="flow-template-visual">
           <div class="template-summary">
             模板：{{ (viewFlowDialog.record.flowTemplate && viewFlowDialog.record.flowTemplate.templateName) || '—' }}
           </div>
           <div class="flow-track">
             <div
               class="flow-node-wrapper"
               v-for="(node, nodeIndex) in viewFlowTemplateNodes"
               :key="node.nodeId || node.nodeName || nodeIndex"
             >
               <div v-if="nodeIndex === 0" class="arrow-first">
                 <div :class="flowNodeSegmentClass(node, 'firstCenter')">
                   <span class="flow-node-name">{{ node.nodeName }}</span>
                 </div>
                 <div :class="flowNodeSegmentClass(node, 'firstRight')"></div>
               </div>
               <div
                 v-else-if="nodeIndex === viewFlowTemplateNodes.length - 1"
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
               </div>
             </div>
           </div>
         </div>
         <div v-else class="template-empty">未绑定流程模板</div>

         <h4 class="section-title">关联订单</h4>
         <el-tag
           v-for="orderId in viewFlowDialog.record.orderIds"
           :key="orderId"
           type="info"
           effect="plain"
           class="mr5"
         >
           {{ orderId }}
         </el-tag>

         <div v-if="viewFlowOrders.length" class="order-flow-preview">
           <h4 class="section-title">关联订单流程</h4>
           <el-collapse>
             <el-collapse-item
               v-for="order in viewFlowOrders"
               :key="order.orderId"
               :title="`${order.orderId}（${order.customerInfo || '未填写'}）`"
             >
               <div v-if="order.flowNodes && order.flowNodes.length" class="flow-template-visual">
                 <div class="template-summary">模板：{{ order.flowTemplate && order.flowTemplate.templateName }}</div>
                 <div class="flow-track">
                   <div
                     class="flow-node-wrapper"
                     :class="{ 'manual-node-clickable': isManualOnlyFlowNode(node) }"
                     v-for="(node, nodeIndex) in order.flowNodes"
                     :key="node.nodeId || node.nodeName || nodeIndex"
                     @click.stop="handleFlowNodeClick(node, order, $event)"
                   >
                     <div v-if="nodeIndex === 0" class="arrow-first">
                       <div :class="flowNodeSegmentClass(node, 'firstCenter')">
                         <span class="flow-node-name">{{ node.nodeName }}</span>
                       </div>
                       <div :class="flowNodeSegmentClass(node, 'firstRight')"></div>
                     </div>
                     <div v-else-if="nodeIndex === order.flowNodes.length - 1" class="arrow-last">
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
                       <div class="node-meta" v-if="node.taskExecution && node.taskExecution.lastTriggeredAt">
                         最近执行：{{ node.taskExecution.lastTriggeredAt }}
                       </div>
                       <el-button
                         v-if="shouldShowManualButton(node, order.orderId)"
                         type="text"
                         size="mini"
                         class="manual-handle-btn"
                         @click.stop="openManualDialogForManualNode({ node, record: order })"
                       >
                         人工处理
                       </el-button>
                     </div>
                   </div>
                 </div>
               </div>
               <div v-else class="template-empty">未绑定流程模板</div>
             </el-collapse-item>
           </el-collapse>
         </div>
       </div>
       <span slot="footer" class="dialog-footer">
         <el-button @click="viewFlowDialog.visible = false">关 闭</el-button>
       </span>
     </el-dialog>

     <!-- 进度步骤人工介入 -->
     <el-dialog
       title="进度步骤调整"
       :visible.sync="flowStepDialog.visible"
       width="480px"
       @close="resetFlowStepDialog"
     >
     <el-form :model="flowStepDialog.form" label-width="100px">
       <el-form-item label="步骤名称">
         <el-input v-model="flowStepDialog.form.name" disabled />
         </el-form-item>
         <el-form-item label="状态">
           <el-select v-model="flowStepDialog.form.status" placeholder="请选择状态">
             <el-option label="待开始" value="pending" />
             <el-option label="进行中" value="processing" />
             <el-option label="已完成" value="completed" />
             <el-option label="异常" value="exception" />
           </el-select>
         </el-form-item>
         <el-form-item label="备注">
           <el-input v-model="flowStepDialog.form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
         </el-form-item>
       </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="flowStepDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="confirmFlowStepIntervention">确 认</el-button>
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
          已人工理
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listFlowPool, getFlowPool, addFlowPool, updateFlowPool, removeFlowPool } from '@/api/productionflow/flowPool'
import { listOrderPool, getOrderPool } from '@/api/productionflow/orderPool'
import { listFlowTemplateAll, getFlowTemplate } from '@/api/order/flowTemplate'
import { getTaskTemplate, listTaskTemplateAll } from '@/api/order/taskTemplate'
import { complateNode, submitRemark } from '@/api/order/orderNode'
import request from '@/utils/request'

const PRIORITY_WEIGHT = {
  low: 1,
  normal: 2,
  high: 3,
  urgent: 4
}

const FLOW_STATUS_OPTIONS = ['flowing', 'frozen']

const FLOW_STATUS_LABELS = {
  flowing: '流动',
  frozen: '冻结'
}

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

const FLOW_SEGMENT_CLASS_MAP = {
  firstCenter: { default: 'first-center', success: 'first-center-active', failed: 'first-center-refuse' },
  firstRight: { default: 'first-right', success: 'first-right-active', failed: 'first-right-refuse' },
  arrowLeft: { default: 'arrow-left', success: 'arrow-left-active', failed: 'arrow-left-refuse' },
  arrowCenter: { default: 'arrow-center', success: 'arrow-center-active', failed: 'arrow-center-refuse' },
  arrowRight: { default: 'arrow-right', success: 'arrow-right-active', failed: 'arrow-right-refuse' },
  lastLeft: { default: 'last-left', success: 'last-left-active', failed: 'last-left-refuse' },
  lastCenter: { default: 'last-center', success: 'last-center-active', failed: 'last-center-refuse' }
}

const deepClone = data => {
  if (data === null || data === undefined) {
    return data
  }
  return JSON.parse(JSON.stringify(data))
}

const SYSTEM_NODE_TYPES = new Set(['0', '1', '2', '3', '4', '5', '6', '7'])

const createEmptyFlowForm = () => ({
  flowId: '',
  templateId: '',
  flowStatus: 'flowing',
  orderIds: [],
  totalQuantity: 0,
  priority: 'normal',
  scheduledStart: '',
  scheduledEnd: '',
  actualStart: '',
  actualEnd: '',
  assignedOperator: '',
  productionNotes: '',
  materialsSummary: [],
  process: [],
  createdAt: '',
  updatedAt: ''
})

export default {
  name: 'ProductionFlowPool',
  data() {
    return {
      loading: false,
      priorityOptions: ['low', 'normal', 'high', 'urgent'],
      priorityLabels: {
        low: '低',
        normal: '普通',
        high: '高',
        urgent: '紧急'
      },
      flowStatusOptions: FLOW_STATUS_OPTIONS,
      flowStatusLabels: FLOW_STATUS_LABELS,
      flowList: [],
      orderList: [],
      templateOptions: [],
      templateCache: {},
      taskTemplateMap: {},
      flowSearch: {
        keyword: '',
        status: ''
      },
      flowDialog: {
        visible: false,
        title: '',
        isEdit: false,
        form: createEmptyFlowForm()
      },
      flowRules: {
        flowId: [{ required: true, message: '请输入生产池ID', trigger: 'blur' }],
        templateId: [{ required: true, message: '请选择流程模板', trigger: 'change' }],
        flowStatus: [{ required: true, message: '请选择生产状态', trigger: 'change' }],
        priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
      },
      viewFlowDialog: {
        visible: false,
        title: '生产池详情',
        record: null
      },
      flowStepDialog: {
        visible: false,
        record: null,
        index: -1,
        form: {
          name: '',
          status: 'pending',
          remark: ''
        }
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
      orderAutomationState: {}
    }
  },
  computed: {
    filteredFlows() {
      return this.flowList
    },
    dialogTemplate() {
      const templateId = this.flowDialog.form.templateId
      return this.templateCache[templateId] || this.templateOptions.find(item => item.templateId === templateId)
    },
    dialogAssociatedOrders() {
      const template = this.dialogTemplate
      if (!template) return []
      return this.flowDialog.form.orderIds.map(orderId => {
        const base = this.orderList.find(item => item.orderId === orderId) || { orderId }
        return {
          ...base,
          nodes: this.buildOrderFlowNodes(base, template)
        }
      })
    },
    viewFlowOrders() {
      const record = this.viewFlowDialog.record
      if (!record || !Array.isArray(record.orderIds) || !record.orderIds.length) {
        return []
      }
      const template = record.flowTemplate || (record.templateId && this.templateCache[record.templateId])
      if (!template) return []
      return record.orderIds.map(orderId => {
        const base = this.orderList.find(item => item.orderId === orderId) || { orderId }
        return {
          ...base,
          flowTemplate: base.flowTemplate || template,
          flowNodes: this.buildOrderFlowNodes(base, template)
        }
      })
    },
    viewFlowTemplateNodes() {
      const record = this.viewFlowDialog.record
      const template = record && (record.flowTemplate || this.templateCache[record.templateId])
      if (!template || !Array.isArray(template.flowNodeList)) {
        return []
      }
      return deepClone(template.flowNodeList)
        .filter(node => node && node.nodeStatus !== 'N')
        .sort((a, b) => (a.sort || 0) - (b.sort || 0))
    }
  },
  created() {
    this.fetchInitialData()
  },
  methods: {
    async fetchInitialData() {
      await Promise.all([
        this.fetchFlowList(),
        this.fetchOrders(),
        this.fetchTemplates(),
        this.fetchTaskTemplates()
      ])
    },
    async fetchFlowList() {
      this.loading = true
      try {
        const params = {
          keyword: this.flowSearch.keyword || undefined,
          status: this.flowSearch.status || undefined
        }
        const { data } = await listFlowPool(params)
        this.flowList = Array.isArray(data) ? data : []
        this.flowList.forEach(flow => {
          if (flow && flow.flowTemplate && flow.flowTemplate.templateId) {
            this.templateCache[flow.flowTemplate.templateId] = flow.flowTemplate
          }
        })
      } catch (error) {
        console.error(error)
        this.$message.error('加载生产池数据失败')
      } finally {
        this.loading = false
      }
    },
    async fetchOrders() {
      try {
        const { data } = await listOrderPool({})
        this.orderList = Array.isArray(data) ? data.map(item => this.normalizeOrder(item)) : []
      } catch (error) {
        console.error(error)
        this.$message.error('加载订单数据失败')
      }
    },
    async fetchTaskTemplates() {
      try {
        const { data } = await listTaskTemplateAll({})
        const map = {}
        ;(Array.isArray(data) ? data : []).forEach(item => {
          if (item && item.taskTemplateId !== undefined && item.taskTemplateId !== null) {
            map[`${item.taskTemplateId}`] = {
              ...item,
              parsedConfig: this.parseTaskTemplateConfig(item.config)
            }
          }
        })
        this.taskTemplateMap = map
      } catch (error) {
        console.error(error)
        this.$message.error('加载任务模板失败')
      }
    },
    async fetchTemplates() {
      try {
        const { data } = await listFlowTemplateAll({})
        this.templateOptions = Array.isArray(data) ? data : []
        this.templateOptions.forEach(item => {
          if (item && item.templateId) {
            this.templateCache[item.templateId] = item
          }
        })
      } catch (error) {
        console.error(error)
        this.$message.error('加载流程模板失败')
      }
    },
    async ensureTemplate(templateId) {
      if (!templateId) return null
      if (this.templateCache[templateId] && this.templateCache[templateId].flowNodeList) {
        return this.templateCache[templateId]
      }
      try {
        const { data } = await getFlowTemplate(templateId)
        if (data) {
          this.templateCache[templateId] = data
          return data
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取流程模板详情失败')
      }
      return this.templateCache[templateId] || null
    },
    async ensureTaskTemplate(templateId) {
      if (!templateId) return null
      const existing = this.taskTemplateMap[templateId]
      if (existing) {
        return existing
      }
      try {
        const { data } = await getTaskTemplate(templateId)
        if (data) {
          const normalized = {
            ...data,
            parsedConfig: this.parseTaskTemplateConfig(data.config)
          }
          this.$set(this.taskTemplateMap, `${templateId}`, normalized)
          return normalized
        }
      } catch (error) {
        console.error('获取任务模板失败', error)
        this.$message.error('获取任务模板失败')
      }
      return null
    },
    normalizeOrder(order = {}) {
      const orderNodes = this.normalizeOrderNodes(order.orderNodes)
      return {
        ...order,
        orderId: order.orderId || '',
        orderNodes,
        flowTemplate: order.flowTemplate || null,
        templateId: order.templateId || (order.flowTemplate && order.flowTemplate.templateId) || ''
      }
    },
    handleFlowQuery() {
      this.fetchFlowList()
    },
    resetFlowQuery() {
      this.flowSearch = { keyword: '', status: '' }
      this.fetchFlowList()
    },
    flowStatusTagType(status) {
      const mapping = {
        flowing: 'success',
        frozen: 'info'
      }
      return mapping[status] || 'info'
    },
    flowProcessStatusText(status) {
      const mapping = {
        pending: '待开始',
        processing: '进行中',
        completed: '已完成',
        exception: '异常'
      }
      return mapping[status] || '待开始'
    },
    orderFlowActiveStep(nodes) {
      if (!Array.isArray(nodes) || !nodes.length) return 0
      const exceptionIndex = nodes.findIndex(step => step.stepStatus === 'exception')
      if (exceptionIndex !== -1) return exceptionIndex + 1
      const completedCount = nodes.filter(step => step.stepStatus === 'completed').length
      if (completedCount >= nodes.length) {
        return nodes.length
      }
      const processingIndex = nodes.findIndex(step => step.stepStatus === 'processing')
      if (processingIndex !== -1) {
        return processingIndex + 1
      }
      return completedCount + 1
    },
    flowStepStatusTag(status) {
      const mapping = {
        pending: 'info',
        processing: 'warning',
        completed: 'success',
        exception: 'danger'
      }
      return mapping[status] || 'info'
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
      if (!node) return false
      const triggerMode = this.normalizeTriggerMode(
        (node.orderNode && node.orderNode.triggerMode) || node.triggerMode
      )
      return triggerMode === 'AUTO'
    },
    isManualTriggerNode(node) {
      if (!node) return false
      const triggerMode = this.normalizeTriggerMode(
        (node.orderNode && node.orderNode.triggerMode) || node.triggerMode
      )
      return triggerMode === 'MANUAL'
    },
    isManualOnlyFlowNode(node) {
      if (!node) {
        return false
      }
      return this.isManualTriggerNode(node)
    },
    buildOrderFlowNodes(order, template) {
      const resolvedTemplate = (order && order.flowTemplate && Array.isArray(order.flowTemplate.flowNodeList))
        ? order.flowTemplate
        : template
      if (!resolvedTemplate || !Array.isArray(resolvedTemplate.flowNodeList)) return []
      const orderNodes = this.normalizeOrderNodes(order && order.orderNodes)
      return resolvedTemplate.flowNodeList
        .filter(node => node && node.nodeStatus !== 'N')
        .sort((a, b) => (a.sort || 0) - (b.sort || 0))
        .map((node, index) => {
          const matchedNode = orderNodes.find(item => item.nodeId && item.nodeId === node.nodeId)
            || orderNodes[index]
            || null
          return Object.assign({}, node, {
            orderNode: matchedNode,
            triggerMode: this.normalizeTriggerMode((matchedNode && matchedNode.triggerMode) || node.triggerMode),
            stepStatus: this.mapOrderNodeStatus(matchedNode && matchedNode.nodeStatus),
            remark: (matchedNode && matchedNode.nodeRemark) || ''
          })
        })
    },
    mapOrderNodeStatus(status) {
      const value = `${status || '0'}`
      if (value === '2') return 'completed'
      if (value === '1') return 'processing'
      if (value === '3') return 'exception'
      return 'pending'
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
    flowStepStatus(status) {
      const mapping = {
        pending: 'wait',
        processing: 'process',
        completed: 'success',
        exception: 'error'
      }
      return mapping[status] || 'wait'
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
          return 'success'
        }
        if (status === '3') {
          return 'failed'
        }
        return 'pending'
      }
      if (!node || !node.taskExecution) {
        return 'pending'
      }
      if (node.taskExecution.success) {
        return 'success'
      }
      if (node.taskExecution.success === false || node.taskExecution.error) {
        return 'failed'
      }
      return 'pending'
    },
    flowNodeSegmentClass(node, segment) {
      const config = FLOW_SEGMENT_CLASS_MAP[segment]
      if (!config) {
        return ''
      }
      const state = this.nodeVisualState(node)
      if (state === 'success' && config.success) {
        return config.success
      }
      if (state === 'failed' && config.failed) {
        return config.failed
      }
      return config.default
    },
    shouldShowManualButton(node, orderId) {
      if (!node || !orderId) {
        return false
      }
      const orderNode = node.orderNode
      const triggerMode = this.normalizeTriggerMode((orderNode && orderNode.triggerMode) || node.triggerMode)
      const isManualTrigger = triggerMode === 'MANUAL'
      const isAutoTrigger = triggerMode === 'AUTO'
      if (isManualTrigger) {
        return orderNode && `${orderNode.nodeStatus || '0'}` !== '2'
      }
      if (!isAutoTrigger) return false
      const state = this.getOrderAutomationState(orderId)
      return Boolean(
        state
        && state.status === 'failed'
        && this.isSameFlowNode(state.failedNode, node)
      )
    },
    flowProcessActiveStep(flow) {
      const steps = (flow && Array.isArray(flow.process)) ? flow.process : []
      if (!steps.length) return 0
      const exceptionIndex = steps.findIndex(step => step.stepStatus === 'exception')
      if (exceptionIndex !== -1) return exceptionIndex + 1
      const completedCount = steps.filter(step => step.stepStatus === 'completed').length
      if (completedCount >= steps.length) {
        return steps.length
      }
      const processingIndex = steps.findIndex(step => step.stepStatus === 'processing')
      if (processingIndex !== -1) {
        return processingIndex + 1
      }
      return completedCount + 1
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
    getNodeTaskTemplateId(node) {
      if (!node) {
        return ''
      }
      const explicitTemplateId = node.taskTemplateId
        || (node.orderNode && node.orderNode.taskTemplateId)
      if (explicitTemplateId !== undefined && explicitTemplateId !== null) {
        return `${explicitTemplateId}`
      }
      const nodeType = node.nodeType
      if (nodeType == null) {
        return ''
      }
      const nodeTypeStr = `${nodeType}`
      if (SYSTEM_NODE_TYPES.has(nodeTypeStr)) {
        return ''
      }
      return nodeTypeStr
    },
    isTaskTemplateNode(node) {
      const templateId = this.getNodeTaskTemplateId(node)
      if (!templateId) {
        return false
      }
      const templateFromMap = this.taskTemplateMap[templateId]
      if (templateFromMap) {
        return true
      }
      if (Object.prototype.hasOwnProperty.call(this.taskTemplateMap, templateId) && templateFromMap === null) {
        return true
      }
      return Boolean((node && node.taskTemplate) || (node && node.orderNode && node.orderNode.taskTemplate) || templateId)
    },
    async handleFlowNodeClick(node, record, event) {
      if (event && typeof event.stopPropagation === 'function') {
        event.stopPropagation()
      }
      if (this.nodeClickHandling) {
        return
      }
      this.nodeClickHandling = true
      try {
        if (!node || !record) {
          return
        }
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
          return
        }
        if (this.isTaskTemplateNode(node) && this.isAutoTriggerNode(node)) {
          const result = await this.executeTaskNode(node, record)
          if (!result || result.success !== true) {
            this.$message.error((result && (result.error || result.message)) || '任务执行失败')
          }
        }
      } finally {
        this.nodeClickHandling = false
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
    resolveOrderValue(source, path) {
      if (!source || !path) {
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
    prepareEmptyForm() {
      const form = createEmptyFlowForm()
      form.flowId = `FLOW-${this.nowDateStamp()}`
      form.createdAt = this.nowDateTime()
      form.updatedAt = this.nowDateTime()
      return form
    },
    normalizeFlowForm(raw) {
      const base = createEmptyFlowForm()
      const merged = Object.assign(base, JSON.parse(JSON.stringify(raw || {})))
      merged.orderIds = Array.isArray(merged.orderIds) ? merged.orderIds : []
      merged.materialsSummary = Array.isArray(merged.materialsSummary) ? merged.materialsSummary : []
      merged.process = Array.isArray(merged.process) ? merged.process : []
      return merged
    },
    async openFlowDialog(flow) {
      if (flow && flow.flowId) {
        this.flowDialog.title = '编辑生产池'
        this.flowDialog.isEdit = true
        try {
          const { data } = await getFlowPool(flow.flowId)
          this.flowDialog.form = this.normalizeFlowForm(data || flow)
        } catch (error) {
          console.error(error)
          this.$message.error('获取生产池详情失败，将使用现有数据')
          this.flowDialog.form = this.normalizeFlowForm(flow)
        }
        await this.ensureTemplate(this.flowDialog.form.templateId)
      } else {
        this.flowDialog.title = '新增生产池'
        this.flowDialog.isEdit = false
        this.flowDialog.form = this.prepareEmptyForm()
      }
      this.$nextTick(() => {
        if (this.$refs.flowForm) {
          this.$refs.flowForm.clearValidate()
        }
      })
      this.flowDialog.visible = true
    },
    closeFlowDialog() {
      this.flowDialog.visible = false
    },
    async handleTemplateChange(templateId) {
      this.flowDialog.form.templateId = templateId
      const template = await this.ensureTemplate(templateId)
      if (template && Array.isArray(template.flowNodeList)) {
        this.flowDialog.form.process = template.flowNodeList
          .filter(node => node && node.nodeStatus !== 'N')
          .sort((a, b) => (a.sort || 0) - (b.sort || 0))
          .map((node, index) => ({
            nodeId: node.nodeId,
            stepName: node.nodeName,
            stepStatus: 'pending',
            remark: '',
            sortOrder: node.sort != null ? node.sort : index
          }))
      }
      if (template) {
        this.applyTemplateToOrders(this.flowDialog.form.orderIds, template)
      }
    },
    addMaterialRow() {
      if (!Array.isArray(this.flowDialog.form.materialsSummary)) {
        this.$set(this.flowDialog.form, 'materialsSummary', [])
      }
      this.flowDialog.form.materialsSummary.push({ material: '', quantity: 0, sortOrder: this.flowDialog.form.materialsSummary.length })
    },
    removeMaterialRow(index) {
      if (!Array.isArray(this.flowDialog.form.materialsSummary)) return
      this.flowDialog.form.materialsSummary.splice(index, 1)
    },
    addProcessRow() {
      if (!Array.isArray(this.flowDialog.form.process)) {
        this.$set(this.flowDialog.form, 'process', [])
      }
      this.flowDialog.form.process.push({
        nodeId: '',
        stepName: '',
        stepStatus: 'pending',
        remark: '',
        sortOrder: this.flowDialog.form.process.length
      })
    },
    removeProcessRow(index) {
      if (!Array.isArray(this.flowDialog.form.process)) return
      this.flowDialog.form.process.splice(index, 1)
    },
    applyTemplateToOrders(orderIds, defaultTemplate) {
      if (!Array.isArray(orderIds) || (!defaultTemplate && !this.orderList.length)) return
      this.orderList = this.orderList.map(order => {
        if (!orderIds.includes(order.orderId)) return order
        const orderTemplate = (order && order.flowTemplate && Array.isArray(order.flowTemplate.flowNodeList))
          ? order.flowTemplate
          : (order && order.templateId && this.templateCache[order.templateId])
        const templateToUse = orderTemplate || defaultTemplate
        const templateNodes = templateToUse && Array.isArray(templateToUse.flowNodeList)
          ? templateToUse.flowNodeList
            .filter(node => node && node.nodeStatus !== 'N')
            .sort((a, b) => (a.sort || 0) - (b.sort || 0))
          : []
        const normalizedOrderNodes = this.normalizeOrderNodes(order && order.orderNodes)
        const mappedNodes = templateNodes.map((node, index) => {
          const matched = normalizedOrderNodes.find(item => item.nodeId === node.nodeId)
            || normalizedOrderNodes[index]
            || null
          return {
            ...node,
            triggerMode: (matched && matched.triggerMode) || node.triggerMode || 'MANUAL',
            nodeStatus: (matched && matched.nodeStatus) || '0',
            nodeRemark: (matched && matched.nodeRemark) || '',
            orderNodeId: (matched && matched.orderNodeId) || '',
            sort: node.sort != null ? node.sort : index
          }
        })
        return {
          ...order,
          templateId: templateToUse ? templateToUse.templateId : order.templateId,
          flowTemplate: templateToUse || order.flowTemplate,
          orderNodes: mappedNodes
        }
      })
    },
    async applyFlowTemplateToOrders(flowDetail, orderIds = []) {
      if (!flowDetail || !flowDetail.templateId || !Array.isArray(orderIds) || !orderIds.length) {
        return
      }
      const template = flowDetail.flowTemplate || await this.ensureTemplate(flowDetail.templateId)
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
    updateFlowMetrics() {
      const selected = this.flowDialog.form.orderIds
      if (!selected || !selected.length) {
        this.flowDialog.form.totalQuantity = 0
        this.flowDialog.form.materialsSummary = []
        this.flowDialog.form.priority = 'normal'
        return
      }
      const ordersMap = this.orderList.reduce((acc, order) => {
        acc[order.orderId] = order
        return acc
      }, {})
      let total = 0
      const materials = {}
      let priority = 'low'
      selected.forEach(id => {
        const order = ordersMap[id]
        if (order) {
          total += Number(order.quantity || 0)
          const key = order.mainMaterial || '未知材料'
          materials[key] = (materials[key] || 0) + Number(order.quantity || 0)
          if (PRIORITY_WEIGHT[order.priority] > PRIORITY_WEIGHT[priority]) {
            priority = order.priority
          }
        }
      })
      this.flowDialog.form.totalQuantity = total
      this.flowDialog.form.materialsSummary = Object.keys(materials).map((key, index) => ({
        material: key,
        quantity: materials[key],
        sortOrder: index
      }))
      this.flowDialog.form.priority = priority
    },
    prepareFlowPayload(form) {
      const payload = JSON.parse(JSON.stringify(form))
      payload.orderIds = Array.isArray(form.orderIds) ? form.orderIds.slice() : []
      payload.materialsSummary = (form.materialsSummary || []).map((item, index) => ({
        materialId: item.materialId,
        flowId: form.flowId,
        material: item.material,
        quantity: item.quantity,
        sortOrder: item.sortOrder != null ? item.sortOrder : index
      }))
      payload.process = (form.process || []).map((step, index) => ({
        stepId: step.stepId,
        nodeId: step.nodeId,
        stepName: step.stepName,
        stepStatus: step.stepStatus || 'pending',
        remark: step.remark,
        sortOrder: step.sortOrder != null ? step.sortOrder : index
      }))
      payload.scheduledStart = form.scheduledStart ? this.formatDateValue(form.scheduledStart) : ''
      payload.scheduledEnd = form.scheduledEnd ? this.formatDateValue(form.scheduledEnd) : ''
      payload.actualStart = form.actualStart ? this.formatDateValue(form.actualStart) : ''
      payload.actualEnd = form.actualEnd ? this.formatDateValue(form.actualEnd) : ''
      payload.createdAt = form.createdAt || ''
      payload.updatedAt = this.nowDateTime()
      if (!payload.flowId) {
        payload.flowId = `FLOW-${this.nowDateStamp()}`
      }
      return payload
    },
    async submitFlow() {
      this.$refs.flowForm.validate(async valid => {
        if (!valid) return
        const form = this.flowDialog.form
        const payload = this.prepareFlowPayload(form)
        try {
          if (this.flowDialog.isEdit) {
            await updateFlowPool(payload)
          } else {
            await addFlowPool(payload)
          }
          this.$message.success('保存成功')
          this.flowDialog.visible = false
          const template = await this.ensureTemplate(payload.templateId)
          this.applyTemplateToOrders(payload.orderIds, template)
          await this.applyFlowTemplateToOrders({ ...payload, flowTemplate: template }, payload.orderIds)
          await Promise.all([this.fetchFlowList(), this.fetchOrders()])
        } catch (error) {
          console.error(error)
          this.$message.error('保存失败，请重试')
        }
      })
    },
    async viewFlow(flow) {
      try {
        const { data } = await getFlowPool(flow.flowId)
        this.viewFlowDialog.record = data || JSON.parse(JSON.stringify(flow))
        await this.ensureTemplate(this.viewFlowDialog.record.templateId)
        if (Array.isArray(this.viewFlowDialog.record.orderIds)) {
          await Promise.all(
            this.viewFlowDialog.record.orderIds.map(orderId => this.refreshOrderRecord(orderId, { updateDialog: true }))
          )
        }
      } catch (error) {
        console.error(error)
        this.viewFlowDialog.record = JSON.parse(JSON.stringify(flow))
      }
      this.viewFlowDialog.visible = true
    },
    async openFlowStepIntervention(flow, index) {
      const steps = (flow && Array.isArray(flow.process)) ? flow.process : []
      const step = steps[index]
      if (!step) return
      this.flowStepDialog.record = flow
      this.flowStepDialog.index = index
      this.flowStepDialog.form = {
        name: step.stepName || `步骤${index + 1}`,
        status: step.stepStatus || 'pending',
        remark: step.remark || ''
      }
      this.flowStepDialog.visible = true
    },
    async confirmFlowStepIntervention() {
      if (!this.flowStepDialog.record || this.flowStepDialog.index < 0) return
      const flowIndex = this.flowList.findIndex(item => item.flowId === this.flowStepDialog.record.flowId)
      if (flowIndex < 0) return
      const flow = JSON.parse(JSON.stringify(this.flowList[flowIndex]))
      flow.process = (flow.process || []).map((item, idx) => {
        if (idx === this.flowStepDialog.index) {
          return {
            ...item,
            stepName: this.flowStepDialog.form.name,
            stepStatus: this.flowStepDialog.form.status,
            remark: this.flowStepDialog.form.remark
          }
        }
        return item
      })
      try {
        await updateFlowPool(this.prepareFlowPayload(flow))
        this.$message.success('进度步骤已更新')
        this.flowStepDialog.visible = false
        await this.fetchFlowList()
        if (this.viewFlowDialog.visible && this.viewFlowDialog.record && this.viewFlowDialog.record.flowId === flow.flowId) {
          this.viewFlowDialog.record = flow
        }
      } catch (error) {
        console.error(error)
        this.$message.error('更新步骤失败，请重试')
      }
    },
    resetFlowStepDialog() {
      this.flowStepDialog.record = null
      this.flowStepDialog.index = -1
      this.flowStepDialog.form = {
        name: '',
        status: 'pending',
        remark: ''
      }
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
      const normalizedPendingNodes = Array.isArray(payload.pendingNodes)
        ? payload.pendingNodes.slice()
        : (Array.isArray(previous.pendingNodes) ? previous.pendingNodes.slice() : [])
      const next = {
        templateId: payload.templateId !== undefined ? payload.templateId : previous.templateId,
        templateInstance: payload.templateInstance || previous.templateInstance,
        orderForm: payload.orderForm || previous.orderForm,
        status: payload.status || previous.status || 'pending',
        pendingNodes: normalizedPendingNodes,
        failedNode: payload.failedNode !== undefined ? payload.failedNode : previous.failedNode,
        errorMessage: payload.errorMessage !== undefined ? payload.errorMessage : previous.errorMessage,
        responsePreview: payload.responsePreview !== undefined ? payload.responsePreview : previous.responsePreview
      }
      this.$set(this.orderAutomationState, key, next)
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
    async executeTaskNode(node, orderForm) {
      const templateId = this.getNodeTaskTemplateId(node)
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
      if (!templateId) {
        return finalizeResult({ success: false, message: '节点类型为空' })
      }
      let taskTemplate = this.taskTemplateMap[templateId]
        || node.taskTemplate
        || (node.orderNode && node.orderNode.taskTemplate)
      if (!taskTemplate) {
        taskTemplate = await this.ensureTaskTemplate(templateId)
      }
      if (!taskTemplate) {
        return finalizeResult({ success: false, message: '未找到任务模板配置' })
      }
      if (taskTemplate.triggerMode && taskTemplate.triggerMode !== 'AUTO') {
        return finalizeResult({ success: false, message: '任务模板未设置为自动触发' })
      }
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
          message: success ? '任务执行成功' : '接口返回未满足成功条件'
        }
        if (success && orderId && orderNodeId) {
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
            resultPayload.success = false
            resultPayload.error = (error && error.message) || '节点完成失败'
          }
        }
        return finalizeResult(resultPayload)
      } catch (error) {
        const errorMessage = (error && error.responseData && (error.responseData.message || error.responseData.msg))
          || (error && error.message)
          || '接口调用失败'
        return finalizeResult({
          success: false,
          error: errorMessage,
          response: (error && (error.responseData || (error.response && error.response.data))) || null
        })
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
    async triggerTemplateTasksForOrder(templateId, orderForm) {
      if (!templateId || !orderForm || !orderForm.orderId) {
        return
      }
      try {
        const template = await this.ensureTemplate(templateId)
        if (!template || !Array.isArray(template.flowNodeList)) {
          return
        }
        const templateInstance = deepClone(template)
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
            responsePreview: ''
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
          responsePreview: ''
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
      const template = orderDetail.flowTemplate || await this.ensureTemplate(orderDetail.templateId)
      if (!template || !Array.isArray(template.flowNodeList) || !template.flowNodeList.length) {
        return
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
        responsePreview: ''
      })
      this.runTaskNodesSequence({
        templateId: template.templateId,
        template,
        nodes: queue,
        orderForm: orderDetail,
        orderId
      })
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
            failedNode: null
          })
        }
        return
      }
      const queue = nodes.slice()
      while (queue.length) {
        const currentNode = queue.shift()
        let result
        if (this.isTaskTemplateNode(currentNode)) {
          console.log("executeTaskNode------------------")
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
      }
      if (targetOrderId) {
        this.setOrderAutomationState(targetOrderId, {
          templateId,
          templateInstance: template,
          status: 'success',
          pendingNodes: [],
          failedNode: null,
          errorMessage: '',
          responsePreview: ''
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
          responsePreview: this.formatTaskResponsePreview(result.response)
        })
        this.refreshOrderRecord(orderId, { silent: true, updateDialog: true }).catch(() => {})
      }
      if (failedNode && failedNode.nodeName) {
        this.$message.error(`节点「${failedNode.nodeName}」自动执行失败，请在生产池详情中人工处理`)
      } else {
        this.$message.error('自动任务执行失败，请人工处理')
      }
      if (orderId && failedNode) {
        this.$nextTick(() => {
          this.openManualTaskDialogForOrder(orderId)
        })
      }
    },
    openManualTaskDialogForOrder(orderId) {
      if (!orderId) {
        return
      }
      const state = this.getOrderAutomationState(orderId)
      if (state && state.failedNode && state.status !== 'failed') {
        this.setOrderAutomationState(orderId, { failedNode: null, errorMessage: '', responsePreview: '' })
      }
      if (!state || state.status !== 'failed' || !state.failedNode) {
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
    async refreshOrderRecord(orderId, { silent = true, updateDialog = true } = {}) {
      if (!orderId) {
        return null
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
          && this.viewFlowDialog.visible
          && this.viewFlowDialog.record
          && Array.isArray(this.viewFlowDialog.record.orderIds)
          && this.viewFlowDialog.record.orderIds.includes(orderId)
        ) {
          this.viewFlowDialog.record = {
            ...this.viewFlowDialog.record,
            flowTemplate: this.viewFlowDialog.record.flowTemplate || normalized.flowTemplate || this.viewFlowDialog.record.flowTemplate,
            templateId: this.viewFlowDialog.record.templateId || normalized.templateId || (normalized.flowTemplate && normalized.flowTemplate.templateId)
          }
        }
        this.autoTriggerFromOrder(normalized)
        return normalized
      } catch (error) {
        console.error('刷新订单详情失败', error)
        if (!silent) {
          this.$message.error('刷新订单详情失败')
        }
        return null
      }
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
      let pendingNodes = (() => {
        if (Array.isArray(this.manualTaskDialog.pendingNodes) && this.manualTaskDialog.pendingNodes.length) {
          return this.manualTaskDialog.pendingNodes.slice()
        }
        const state = orderId && this.getOrderAutomationState(orderId)
        if (state && Array.isArray(state.pendingNodes)) {
          return state.pendingNodes.slice()
        }
        return []
      })()
      const remark = (this.manualTaskDialog.remark || '').trim() || '人工处理完成'
      const orderNodesFromForm = (orderForm && Array.isArray(orderForm.orderNodes))
        ? this.normalizeOrderNodes(orderForm.orderNodes)
        : []
      const fallbackOrderNodeById = node => {
        if (!node) {
          return null
        }
        const targetNodeId = node.nodeId || (node.orderNode && node.orderNode.nodeId) || ''
        if (!targetNodeId) {
          return null
        }
        return orderNodesFromForm.find(item => item.nodeId === targetNodeId) || null
      }
      const matchedOrderNode = fallbackOrderNodeById(this.manualTaskDialog.node)
      if (matchedOrderNode && !this.manualTaskDialog.node.orderNode) {
        this.$set(this.manualTaskDialog.node, 'orderNode', matchedOrderNode)
      }
      const pickValidId = (...candidates) => candidates.find(id => id !== undefined && id !== null && `${id}` !== '')
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
      if (matchedOrderNode) {
        this.$set(matchedOrderNode, 'nodeStatus', '2')
        this.$set(matchedOrderNode, 'nodeRemark', remark)
        if (orderForm && Array.isArray(orderForm.orderNodes)) {
          const nodeIndex = orderForm.orderNodes.findIndex(
            item => item && (item.orderNodeId === matchedOrderNode.orderNodeId || item.nodeId === matchedOrderNode.nodeId)
          )
          if (nodeIndex !== -1) {
            const updatedNode = {
              ...orderForm.orderNodes[nodeIndex],
              nodeStatus: '2',
              nodeRemark: remark
            }
            this.$set(orderForm.orderNodes, nodeIndex, updatedNode)
          }
        }
      }
      let refreshedOrder = null
      if (orderId) {
        refreshedOrder = await this.refreshOrderRecord(orderId, { silent: true, updateDialog: true })
      }
      const automationOrderForm = refreshedOrder || orderForm
      if (!Array.isArray(pendingNodes) || !pendingNodes.length) {
        if (automationOrderForm && template) {
          const queue = this.buildAutoTriggerQueue(automationOrderForm, template)
          pendingNodes = Array.isArray(queue) ? queue.slice() : []
        } else {
          pendingNodes = []
        }
      }
      if (orderId) {
        this.setOrderAutomationState(orderId, {
          templateId,
          templateInstance: template,
          orderForm: automationOrderForm,
          status: pendingNodes.length ? 'running' : 'success',
          pendingNodes,
          failedNode: null,
          errorMessage: '',
          responsePreview: ''
        })
      }
      const nextPendingNode = pendingNodes[0]
      const shouldAutoRunNext = nextPendingNode && this.isAutoTriggerNode(nextPendingNode)
      this.resetManualTaskDialog()
      console.log("templateId",templateId);
      console.log("template",template);
      console.log("orderForm",automationOrderForm);
      console.log("pendingNodes.length",pendingNodes.length);
      console.log("shouldAutoRunNext",shouldAutoRunNext);
      if (templateId && template && automationOrderForm && pendingNodes.length && shouldAutoRunNext) {
        console.log("runTaskNodesSequence-------------------------")
        this.runTaskNodesSequence({
          templateId,
          template,
          nodes: pendingNodes,
          orderForm: automationOrderForm,
          orderId
        })
      } else if (templateId && orderId && !pendingNodes.length) {
        await this.triggerTemplateTasksForOrder(templateId, refreshedOrder || orderForm)
      }
    },
    handleDeleteFlow(flow) {
      this.$confirm(`确认删除生产池【${flow.flowId}】吗？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          const { data } = await getFlowPool(flow.flowId)
          const detail = data || flow || {}
          const payload = this.prepareFlowPayload({ ...detail, orderIds: [] })
          await updateFlowPool(payload)
          await removeFlowPool(flow.flowId)
          this.$message.success('删除成功')
          await Promise.all([this.fetchFlowList(), this.fetchOrders()])
        } catch (error) {
          console.error(error)
          this.$message.error('删除失败，请重试')
        }
      }).catch(() => {})
    },
    formatDateDisplay(value) {
      const result = this.formatDateValue(value)
      return result || '—'
    },
    formatDateValue(value) {
      return formatDateHelper(value)
    },
    nowDateTime() {
      return nowDateTimeHelper()
    },
    nowDateStamp() {
      const date = new Date()
      const y = date.getFullYear()
      const m = pad(date.getMonth() + 1)
      const d = pad(date.getDate())
      const h = pad(date.getHours())
      const min = pad(date.getMinutes())
      const s = pad(date.getSeconds())
      return `${y}${m}${d}-${h}${min}${s}`
    }
  }
}
</script>

 <style lang="scss" scoped>
 .productionflow-flow-page {
   display: flex;
   flex-direction: column;
   height: 100%;

   .flow-card {
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

   .section-title {
     margin: 18px 0 10px;
     font-size: 15px;
     font-weight: 600;
   }

  .summary-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 18px;
  }

  .order-flow-preview {
    margin-top: 12px;

    .el-collapse-item__header {
      font-weight: 600;
    }
  }

  .template-summary {
    margin-bottom: 12px;
    font-size: 14px;
    color: #606266;
  }

  .flow-template-visual {
    overflow-x: auto;
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
    position: relative;
    transition: transform 0.2s ease;
  }

  .flow-node-wrapper.manual-node-clickable:hover {
    transform: translateY(-2px);
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
      margin-top: 4px;
      padding: 0;
    }
  }

  .template-empty {
    color: #909399;
    font-size: 13px;
  }

  .step-detail {
    display: flex;
    align-items: center;

     .el-tag {
       margin-right: 8px;
     }

     .step-remark {
       margin-left: 8px;
       color: #606266;
  }
}

.arrow-first {
  display: flex;
}

.first-center,
.first-center-active,
.first-center-refuse {
  width: 100px;
  text-align: center;
  color: #fff;
}

.first-center {
  background-color: #cbcdd4;
}

.first-center-active {
  background-color: #70eaa9;
}

.first-center-refuse {
  background-color: #ca5f41;
}

.first-right,
.first-right-active,
.first-right-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}

.first-right-active {
  border-color: transparent transparent transparent #70eaa9;
}

.first-right-refuse {
  border-color: transparent transparent transparent #ca5f41;
}

.arrow {
  display: flex;
  margin-left: -25px;
}

.arrow-left,
.arrow-left-active,
.arrow-left-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.arrow-left-active {
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}

.arrow-left-refuse {
  border-color: #ca5f41 #ca5f41 #ca5f41 transparent;
}

.arrow-center,
.arrow-center-active,
.arrow-center-refuse {
  width: 100px;
  text-align: center;
}

.arrow-center {
  background-color: #cbcdd4;
}

.arrow-center-active {
  background-color: #70eaa9;
}

.arrow-center-refuse {
  background-color: #ca5f41;
}

.arrow-right,
.arrow-right-active,
.arrow-right-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}

.arrow-right-active {
  border-color: transparent transparent transparent #70eaa9;
}

.arrow-right-refuse {
  border-color: transparent transparent transparent #ca5f41;
}

.arrow-last {
  display: flex;
  margin-left: -25px;
}

.last-left,
.last-left-active,
.last-left-refuse {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.last-left-active {
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}

.last-left-refuse {
  border-color: #ca5f41 #ca5f41 #ca5f41 transparent;
}

.last-center,
.last-center-active,
.last-center-refuse {
  width: 100px;
  text-align: center;
}

.last-center {
  background-color: #cbcdd4;
}

.last-center-active {
  background-color: #70eaa9;
}

.last-center-refuse {
  background-color: #ca5f41;
}

.last-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent transparent;
}

.manual-task-dialog {
  .manual-tip {
    margin-bottom: 10px;
    color: #606266;
  }

  .response-preview {
    margin-top: 10px;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 4px;
    border: 1px solid #ebeef5;
    max-height: 200px;
    overflow: auto;

    .preview-title {
      font-weight: 600;
      margin-bottom: 6px;
    }

    pre {
      white-space: pre-wrap;
      word-break: break-all;
      margin: 0;
    }
  }
}

   .mr5 {
     margin-right: 5px;
     margin-bottom: 4px;
   }
 }
</style>
