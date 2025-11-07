 <template>
   <div class="app-container productionflow-flow-page">
     <el-card shadow="hover" class="flow-card">
       <div slot="header" class="card-header">
         <span>生产池</span>
         <div class="header-actions">
           <el-input
             v-model="flowSearch.keyword"
             placeholder="搜索生产流ID/负责人"
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
           新增生产流
         </el-button>
         <el-button type="info" size="small" icon="el-icon-refresh" @click="resetFlowQuery">
           重置筛选
         </el-button>
       </div>

       <el-table :data="filteredFlows" border height="600" stripe :row-key="row => row.flowId">
         <el-table-column prop="flowId" label="生产流ID" width="160" show-overflow-tooltip />
         <el-table-column label="关联订单" width="200" show-overflow-tooltip>
           <template slot-scope="scope">
             {{ scope.row.orderIds.join(', ') }}
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

     <!-- 新增/编辑生产流 -->
     <el-dialog :title="flowDialog.title" :visible.sync="flowDialog.visible" width="880px">
       <el-form ref="flowForm" :model="flowDialog.form" :rules="flowRules" label-width="140px">
         <el-row :gutter="20">
           <el-col :span="12">
             <el-form-item label="生产流ID" prop="flowId">
               <el-input v-model="flowDialog.form.flowId" placeholder="请输入生产流ID" />
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
           <h4 class="section-title">材料汇总</h4>
           <el-button type="text" size="mini" icon="el-icon-plus" @click="addMaterialRow">新增材料</el-button>
         </div>
         <el-table :data="flowDialog.form.materialsSummary" border size="mini">
           <el-table-column label="材料">
             <template slot-scope="scope">
               <el-input v-model="scope.row.material" size="mini" placeholder="材料" />
             </template>
           </el-table-column>
           <el-table-column label="数量" width="140">
             <template slot-scope="scope">
               <el-input-number v-model="scope.row.quantity" :min="0" :step="1" size="mini" />
             </template>
           </el-table-column>
           <el-table-column label="操作" width="100">
             <template slot-scope="scope">
               <el-button type="text" size="mini" @click="removeMaterialRow(scope.$index)">删除</el-button>
             </template>
           </el-table-column>
         </el-table>

         <div class="summary-header">
           <h4 class="section-title">当前进度</h4>
           <el-button type="text" size="mini" icon="el-icon-plus" @click="addProcessRow">新增步骤</el-button>
         </div>
         <el-table :data="flowDialog.form.process" border size="mini">
           <el-table-column label="步骤">
             <template slot-scope="scope">
               <el-input v-model="scope.row.name" size="mini" placeholder="步骤名称" />
             </template>
           </el-table-column>
           <el-table-column label="状态" width="160">
             <template slot-scope="scope">
               <el-select v-model="scope.row.status" placeholder="请选择状态" size="mini">
                 <el-option label="待开始" value="pending" />
                 <el-option label="进行中" value="processing" />
                 <el-option label="已完成" value="completed" />
                 <el-option label="异常" value="exception" />
               </el-select>
             </template>
           </el-table-column>
           <el-table-column label="备注">
             <template slot-scope="scope">
               <el-input v-model="scope.row.remark" placeholder="备注" size="mini" />
             </template>
           </el-table-column>
           <el-table-column label="操作" width="100">
             <template slot-scope="scope">
               <el-button type="text" size="mini" @click="removeProcessRow(scope.$index)">删除</el-button>
             </template>
           </el-table-column>
         </el-table>
       </div>

       <span slot="footer" class="dialog-footer">
         <el-button @click="flowDialog.visible = false">取 消</el-button>
         <el-button type="primary" @click="submitFlow">保 存</el-button>
       </span>
     </el-dialog>

     <!-- 生产流详情 -->
     <el-dialog :title="viewFlowDialog.title" :visible.sync="viewFlowDialog.visible" width="820px">
       <div v-if="viewFlowDialog.record">
         <el-descriptions :column="2" border label-class-name="desc-label">
           <el-descriptions-item label="生产流ID">{{ viewFlowDialog.record.flowId }}</el-descriptions-item>
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
             :title="step.name || `步骤${index + 1}`"
             :status="flowStepStatus(step.status)"
           >
             <template slot="description">
               <div class="step-detail">
                 <el-tag size="mini" :type="flowStepStatusTag(step.status)">
                   {{ flowProcessStatusText(step.status) }}
                 </el-tag>
                 <el-button
                   v-if="step.status === 'exception'"
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
 import { loadFlows, saveFlows, loadOrders } from '../dataStore'

 const PRIORITY_WEIGHT = {
   low: 1,
   normal: 2,
   high: 3,
   urgent: 4
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

 const getDefaultOrders = () => ([
   {
     orderId: 'ORD-20240501-001',
     previewImage: '',
     quantity: 120,
     remark: '需确认配色',
     mainMaterial: '树脂',
     craftRequirements: '高精度打印，后处理抛光',
     orderStatus: '待处理',
     createdAt: '2024-05-01 09:30',
     updatedAt: '2024-05-01 09:30',
     customerInfo: '上海晨光科技有限公司',
     priority: 'high',
     deliveryDate: '2024-05-15 18:00',
     sizeRequirement: '200 x 150 x 30mm',
     colorRequirement: '潘通 186C',
     fileFormat: 'STEP',
     preprocessSteps: []
   },
   {
     orderId: 'ORD-20240502-006',
     previewImage: '',
     quantity: 60,
     remark: '客户提供样品',
     mainMaterial: 'PLA',
     craftRequirements: '大尺寸排版，注意支撑',
     orderStatus: '待处理',
     createdAt: '2024-05-02 10:15',
     updatedAt: '2024-05-04 11:40',
     customerInfo: '宁波海创股份',
     priority: 'normal',
     deliveryDate: '2024-05-22 17:30',
     sizeRequirement: '300 x 300 x 50mm',
     colorRequirement: '本色',
     fileFormat: 'STL',
     preprocessSteps: []
   }
 ])

 const getDefaultFlows = () => ([
   {
     flowId: 'FLOW-202405-001',
     orderIds: ['ORD-20240501-001'],
     flowStatus: 'file_preparing',
     process: [
       { name: '文件准备', status: 'processing', remark: '正在清理模型' },
       { name: '排版设计', status: 'pending', remark: '' },
       { name: '打印执行', status: 'pending', remark: '' }
     ],
     totalQuantity: 120,
     materialsSummary: [
       { material: '树脂', quantity: 120 }
     ],
     priority: 'high',
     scheduledStart: '2024-05-06 09:00',
     scheduledEnd: '2024-05-10 20:00',
     actualStart: null,
     actualEnd: null,
     assignedOperator: '陈工',
     productionNotes: '排期紧张，优先安排',
     createdAt: '2024-05-05 10:00',
     updatedAt: '2024-05-06 09:20'
   }
 ])

 export default {
   name: 'ProductionFlowPool',
   data() {
     return {
       priorityOptions: ['low', 'normal', 'high', 'urgent'],
       priorityLabels: {
         low: '低',
         normal: '普通',
         high: '高',
         urgent: '紧急'
       },
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
       flowSearch: {
         keyword: '',
         status: ''
       },
       orderList: loadOrders(getDefaultOrders()),
       flowList: loadFlows(getDefaultFlows()),
       flowDialog: {
         visible: false,
         title: '',
         form: {
           flowId: '',
           orderIds: [],
           flowStatus: 'pending',
           process: [],
           totalQuantity: 0,
           materialsSummary: [],
           priority: 'normal',
           scheduledStart: null,
           scheduledEnd: null,
           actualStart: null,
           actualEnd: null,
           assignedOperator: '',
           productionNotes: '',
           createdAt: '',
           updatedAt: ''
         }
       },
       viewFlowDialog: {
         visible: false,
         title: '生产流详情',
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
       flowRules: {
         flowId: [{ required: true, message: '请输入生产流ID', trigger: 'blur' }],
         flowStatus: [{ required: true, message: '请选择状态', trigger: 'change' }],
         orderIds: [{ required: true, message: '请选择关联订单', trigger: 'change' }],
         totalQuantity: [{ required: true, message: '请输入订单总数量', trigger: 'change' }],
         priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
       }
     }
   },
   computed: {
     filteredFlows() {
       let list = this.flowList.slice()
       if (this.flowSearch.keyword) {
         const keyword = this.flowSearch.keyword.toLowerCase()
         list = list.filter(flow => {
           return (
             flow.flowId.toLowerCase().includes(keyword) ||
             (flow.assignedOperator && flow.assignedOperator.toLowerCase().includes(keyword))
           )
         })
       }
       if (this.flowSearch.status) {
         list = list.filter(flow => flow.flowStatus === this.flowSearch.status)
       }
       return list
     }
   },
   methods: {
     handleFlowQuery() {
       // computed 自动处理
     },
     resetFlowQuery() {
       this.flowSearch = { keyword: '', status: '' }
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
     flowStepStatusTag(status) {
       const mapping = {
         pending: 'info',
         processing: 'warning',
         completed: 'success',
         exception: 'danger'
       }
       return mapping[status] || 'info'
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
       const exceptionIndex = steps.findIndex(step => step.status === 'exception')
       if (exceptionIndex !== -1) return exceptionIndex + 1
       const completedCount = steps.filter(step => step.status === 'completed').length
       if (completedCount >= steps.length) {
         return steps.length
       }
       const processingIndex = steps.findIndex(step => step.status === 'processing')
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
     openFlowDialog(flow) {
       if (flow) {
         this.flowDialog.title = '编辑生产流'
         this.flowDialog.form = JSON.parse(JSON.stringify(flow))
         if (!Array.isArray(this.flowDialog.form.materialsSummary)) {
           this.flowDialog.form.materialsSummary = []
         }
         if (!Array.isArray(this.flowDialog.form.process)) {
           this.flowDialog.form.process = []
         }
       } else {
         const flowId = `FLOW-${this.nowDateStamp()}`
         this.flowDialog.title = '新增生产流'
         this.flowDialog.form = {
           flowId,
           orderIds: [],
           flowStatus: 'pending',
           process: [
             { name: '文件准备', status: 'pending', remark: '' },
             { name: '排版设计', status: 'pending', remark: '' },
             { name: '打印执行', status: 'pending', remark: '' }
           ],
           totalQuantity: 0,
           materialsSummary: [],
           priority: 'normal',
           scheduledStart: '',
           scheduledEnd: '',
           actualStart: '',
           actualEnd: '',
           assignedOperator: '',
           productionNotes: '',
           createdAt: this.nowDateTime(),
           updatedAt: this.nowDateTime()
         }
       }
       this.$nextTick(() => {
         if (this.$refs.flowForm) {
           this.$refs.flowForm.clearValidate()
         }
       })
       this.flowDialog.visible = true
     },
     addMaterialRow() {
       if (!Array.isArray(this.flowDialog.form.materialsSummary)) {
         this.$set(this.flowDialog.form, 'materialsSummary', [])
       }
       this.flowDialog.form.materialsSummary.push({ material: '', quantity: 0 })
     },
     removeMaterialRow(index) {
       if (!Array.isArray(this.flowDialog.form.materialsSummary)) return
       this.flowDialog.form.materialsSummary.splice(index, 1)
     },
     addProcessRow() {
       if (!Array.isArray(this.flowDialog.form.process)) {
         this.$set(this.flowDialog.form, 'process', [])
       }
       this.flowDialog.form.process.push({ name: '', status: 'pending', remark: '' })
     },
     removeProcessRow(index) {
       if (!Array.isArray(this.flowDialog.form.process)) return
       this.flowDialog.form.process.splice(index, 1)
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
       if (total > 0) {
         this.flowDialog.form.totalQuantity = total
       }
       if (Object.keys(materials).length) {
         this.flowDialog.form.materialsSummary = Object.keys(materials).map(key => ({
           material: key,
           quantity: materials[key]
         }))
       }
       this.flowDialog.form.priority = priority
     },
     submitFlow() {
       this.$refs.flowForm.validate(valid => {
         if (!valid) return
         const form = JSON.parse(JSON.stringify(this.flowDialog.form))
         form.scheduledStart = form.scheduledStart ? this.formatDateValue(form.scheduledStart) : ''
         form.scheduledEnd = form.scheduledEnd ? this.formatDateValue(form.scheduledEnd) : ''
         form.actualStart = form.actualStart ? this.formatDateValue(form.actualStart) : ''
         form.actualEnd = form.actualEnd ? this.formatDateValue(form.actualEnd) : ''
         form.updatedAt = this.nowDateTime()
         const index = this.flowList.findIndex(item => item.flowId === form.flowId)
         if (index > -1) {
           this.$set(this.flowList, index, { ...this.flowList[index], ...form })
         } else {
           if (!form.createdAt) {
             form.createdAt = this.nowDateTime()
           }
           this.flowList.push(form)
         }
         saveFlows(this.flowList)
         this.flowDialog.visible = false
         this.$message.success('保存成功')
       })
     },
     viewFlow(flow) {
       this.viewFlowDialog.record = JSON.parse(JSON.stringify(flow))
       this.viewFlowDialog.visible = true
     },
     openFlowStepIntervention(flow, index) {
       const steps = (flow && Array.isArray(flow.process)) ? flow.process : []
       const step = steps[index]
       if (!step) return
       this.flowStepDialog.record = flow
       this.flowStepDialog.index = index
       this.flowStepDialog.form = {
         name: step.name || `步骤${index + 1}`,
         status: step.status || 'pending',
         remark: step.remark || ''
       }
       this.flowStepDialog.visible = true
     },
     confirmFlowStepIntervention() {
       if (!this.flowStepDialog.record || this.flowStepDialog.index < 0) return
       const flowIndex = this.flowList.findIndex(item => item.flowId === this.flowStepDialog.record.flowId)
       if (flowIndex < 0) return
       const flow = JSON.parse(JSON.stringify(this.flowList[flowIndex]))
       flow.process = (flow.process || []).map((item, idx) => {
         if (idx === this.flowStepDialog.index) {
           return { ...item, ...this.flowStepDialog.form }
         }
         return item
       })
       flow.updatedAt = this.nowDateTime()
       this.$set(this.flowList, flowIndex, flow)
       if (this.viewFlowDialog.record && this.viewFlowDialog.record.flowId === flow.flowId) {
         this.viewFlowDialog.record = JSON.parse(JSON.stringify(flow))
       }
       saveFlows(this.flowList)
       this.$message.success('进度步骤已更新')
       this.flowStepDialog.visible = false
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
       this.$confirm(`确认删除生产流【${flow.flowId}】吗？`, '提示', {
         type: 'warning'
       }).then(() => {
         this.flowList = this.flowList.filter(item => item.flowId !== flow.flowId)
         saveFlows(this.flowList)
         this.$message.success('删除成功')
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
asdasd
