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
             <el-form-item label="关联订单" prop="orderIds">
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
            <el-form-item label="订单总数量" prop="totalQuantity">
              <el-input-number v-model="flowDialog.form.totalQuantity" :min="1" :step="1" style="width: 100%;" />
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

         <h4 class="section-title">材料汇总</h4>
         <el-table :data="viewFlowDialog.record.materialsSummary" border size="mini">
           <el-table-column prop="material" label="材料" />
           <el-table-column prop="quantity" label="数量" width="120" />
         </el-table>

         <h4 class="section-title">当前进度</h4>
        <el-steps :active="flowProcessActiveStep(viewFlowDialog.record)" align-center>
          <el-step
            v-for="(step, index) in viewFlowDialog.record.process || []"
            :key="`${viewFlowDialog.record.flowId}-${index}`"
            :title="step.stepName || `步骤${index + 1}`"
            :status="flowStepStatus(step.stepStatus)"
          >
            <template slot="description">
              <div class="step-detail">
                <el-tag size="mini" :type="flowStepStatusTag(step.stepStatus)">
                  {{ flowProcessStatusText(step.stepStatus) }}
                </el-tag>
                <el-button
                  v-if="step.stepStatus === 'exception'"
                  type="text"
                  size="mini"
                  @click="openFlowStepIntervention(viewFlowDialog.record, index)"
                 >人工处理</el-button>
                 <div class="step-remark" v-if="step.remark">{{ step.remark }}</div>
               </div>
             </template>
           </el-step>
         </el-steps>
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
   </div>
 </template>

<script>
import { listFlowPool, getFlowPool, addFlowPool, updateFlowPool, removeFlowPool } from '@/api/productionflow/flowPool'
import { listOrderPool } from '@/api/productionflow/orderPool'
import { listFlowTemplateAll, getFlowTemplate } from '@/api/order/flowTemplate'

const PRIORITY_WEIGHT = {
  low: 1,
  normal: 2,
  high: 3,
  urgent: 4
}

const FLOW_STATUS_OPTIONS = [
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
]

const FLOW_STATUS_LABELS = {
  pending: '待开始',
  file_preparing: '文件准备中',
  file_ready: '文件已完成',
  layout_designing: '排版设计中',
  layout_approved: '排版已确认',
  printing: '打印中',
  printed: '打印完成',
  cutting: '后处理中',
  cut_completed: '后处理完成',
  quality_check: '质检中',
  completed: '已完成',
  cancelled: '已取消'
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

const createEmptyFlowForm = () => ({
  flowId: '',
  templateId: '',
  flowStatus: 'pending',
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
        orderIds: [{ required: true, message: '请选择关联订单', trigger: 'change' }],
        totalQuantity: [{ required: true, message: '请输入订单总数量', trigger: 'change' }],
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
      }
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
          nodes: this.buildOrderNodesForTemplate(base, template)
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
          nodes: this.buildOrderNodesForTemplate(base, template)
        }
      })
    }
  },
  created() {
    this.fetchInitialData()
  },
  methods: {
    async fetchInitialData() {
      await Promise.all([this.fetchFlowList(), this.fetchOrders(), this.fetchTemplates()])
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
        this.orderList = Array.isArray(data) ? data : []
      } catch (error) {
        console.error(error)
        this.$message.error('加载订单数据失败')
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
    handleFlowQuery() {
      this.fetchFlowList()
    },
    resetFlowQuery() {
      this.flowSearch = { keyword: '', status: '' }
      this.fetchFlowList()
    },
    flowStatusTagType(status) {
      const mapping = {
        pending: 'info',
        file_preparing: 'primary',
        file_ready: 'primary',
        layout_designing: 'warning',
        layout_approved: 'success',
        printing: 'warning',
        printed: 'success',
        cutting: 'warning',
        cut_completed: 'success',
        quality_check: 'warning',
        completed: 'success',
        cancelled: 'danger'
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
    buildOrderNodesForTemplate(order, template) {
      if (!template || !Array.isArray(template.flowNodeList)) return []
      const existingNodes = Array.isArray(order && order.orderNodes) ? order.orderNodes : []
      return template.flowNodeList
        .filter(node => node && node.nodeStatus !== 'N')
        .sort((a, b) => (a.sort || 0) - (b.sort || 0))
        .map((node, index) => {
          const matched = existingNodes.find(item => item.nodeId === node.nodeId) || {}
          return {
            nodeId: node.nodeId,
            stepName: node.nodeName,
            stepStatus: matched.stepStatus || 'pending',
            remark: matched.remark || '',
            sortOrder: node.sort != null ? node.sort : index
          }
        })
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
    applyTemplateToOrders(orderIds, template) {
      if (!template || !Array.isArray(orderIds)) return
      const clonedTemplate = JSON.parse(JSON.stringify(template))
      this.orderList = this.orderList.map(order => {
        if (orderIds.includes(order.orderId)) {
          return {
            ...order,
            templateId: clonedTemplate.templateId,
            flowTemplate: clonedTemplate
          }
        }
        return order
      })
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
    handleDeleteFlow(flow) {
      this.$confirm(`确认删除生产池【${flow.flowId}】吗？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await removeFlowPool(flow.flowId)
          this.$message.success('删除成功')
          this.fetchFlowList()
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

   .mr5 {
     margin-right: 5px;
     margin-bottom: 4px;
   }
 }
</style>
