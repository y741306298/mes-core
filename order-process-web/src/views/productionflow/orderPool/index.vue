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
           @click="openFlowCreationDialog"
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

        <h4 class="section-title">流程模板</h4>
        <div v-if="viewOrderDialog.record.flowTemplate">
          <div class="template-summary">
            模板：{{ viewOrderDialog.record.flowTemplate.templateName }}
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="node in (viewOrderDialog.record.flowTemplate.flowNodeList || [])"
              :key="node.nodeId || node.nodeName"
            >
              <div class="template-node">
                <span class="node-name">{{ node.nodeName }}</span>
                <el-tag
                  v-if="node.nodeType === '0'"
                  size="mini"
                  type="info"
                  class="node-type-tag"
                >系统</el-tag>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
        <div v-else class="template-empty">未绑定流程模板</div>
       </div>
       <span slot="footer" class="dialog-footer">
         <el-button @click="viewOrderDialog.visible = false">关 闭</el-button>
       </span>
     </el-dialog>

     <!-- 入池创建生产流 -->
     <el-dialog :title="flowCreationDialog.title" :visible.sync="flowCreationDialog.visible" width="780px">
       <el-form ref="flowForm" :model="flowCreationDialog.form" :rules="flowRules" label-width="120px">
         <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产流ID" prop="flowId">
              <el-input v-model="flowCreationDialog.form.flowId" placeholder="请输入生产流ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流程模板" prop="templateId">
              <el-select
                v-model="flowCreationDialog.form.templateId"
                placeholder="请选择流程模板"
                filterable
                clearable
                @change="handleFlowTemplateChange"
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
             <el-form-item label="生产状态" prop="flowStatus">
               <el-select v-model="flowCreationDialog.form.flowStatus" placeholder="请选择生产状态">
                 <el-option
                   v-for="item in flowStatusOptions"
                   :key="item"
                   :label="flowStatusLabels[item] || item"
                   :value="item"
                 />
               </el-select>
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="订单总数量" prop="totalQuantity">
               <el-input-number v-model="flowCreationDialog.form.totalQuantity" :min="1" :step="1" style="width: 100%;" />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="优先级" prop="priority">
               <el-select v-model="flowCreationDialog.form.priority" placeholder="请选择优先级">
                 <el-option v-for="item in priorityOptions" :key="item" :label="priorityLabels[item]" :value="item" />
               </el-select>
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="预计开始时间">
               <el-date-picker
                 v-model="flowCreationDialog.form.scheduledStart"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="预计完成时间">
               <el-date-picker
                 v-model="flowCreationDialog.form.scheduledEnd"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="实际开始时间">
               <el-date-picker
                 v-model="flowCreationDialog.form.actualStart"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="实际完成时间">
               <el-date-picker
                 v-model="flowCreationDialog.form.actualEnd"
                 type="datetime"
                 style="width: 100%;"
                 value-format="yyyy-MM-dd HH:mm"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="负责人">
               <el-input v-model="flowCreationDialog.form.assignedOperator" placeholder="请输入负责人" />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="生产备注">
               <el-input v-model="flowCreationDialog.form.productionNotes" placeholder="请输入生产备注" />
             </el-form-item>
           </el-col>
         </el-row>
       </el-form>

       <div class="flow-summary">
         <h4 class="section-title">关联订单</h4>
         <el-tag
           v-for="orderId in flowCreationDialog.form.orderIds"
           :key="orderId"
           type="info"
           effect="plain"
           class="mr5"
         >
           {{ orderId }}
         </el-tag>

         <h4 class="section-title">材料汇总</h4>
         <el-table :data="flowCreationDialog.form.materialsSummary" border size="mini">
           <el-table-column prop="material" label="材料" />
           <el-table-column prop="quantity" label="数量" width="120" />
         </el-table>

        <h4 class="section-title">流程节点</h4>
        <div v-if="flowCreationDialog.form.flowTemplate">
          <div class="template-summary">
            模板：{{ flowCreationDialog.form.flowTemplate.templateName }}
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="node in (flowCreationDialog.form.flowTemplate.flowNodeList || [])"
              :key="node.nodeId || node.nodeName"
            >
              <div class="template-node">
                <span class="node-name">{{ node.nodeName }}</span>
                <el-tag
                  v-if="node.nodeType === '0'"
                  size="mini"
                  type="info"
                  class="node-type-tag"
                >系统</el-tag>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
        <div v-else class="template-empty">请选择流程模板</div>
       </div>

       <span slot="footer" class="dialog-footer">
        <el-button @click="flowCreationDialog.visible = false">取 消</el-button>
        <el-button type="primary" :loading="flowCreationDialog.submitting" @click="submitFlow">生 成</el-button>
       </span>
     </el-dialog>
   </div>
 </template>

<script>
import {
  listOrderPool,
  addOrderPool,
  updateOrderPool,
  removeOrderPool,
  listProductionFlows,
  addProductionFlow,
  updateProductionFlow
} from '@/api/productionflow/orderPool'
import { listFlowTemplateAll } from '@/api/order/flowTemplate'

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

 const nowDateStampHelper = () => {
   const date = new Date()
   const y = date.getFullYear()
   const m = pad(date.getMonth() + 1)
   const d = pad(date.getDate())
   const h = pad(date.getHours())
   const min = pad(date.getMinutes())
   const s = pad(date.getSeconds())
   return `${y}${m}${d}-${h}${min}${s}`
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
      flowTemplateOptions: [],
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
        record: null
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
      flowCreationDialog: {
        visible: false,
        title: '创建生产流',
        isEdit: false,
        submitting: false,
        form: {
          flowId: '',
          orderIds: [],
          templateId: '',
          flowTemplate: null,
          flowStatus: 'pending',
          totalQuantity: 0,
          materialsSummary: [],
          priority: 'normal',
          scheduledStart: '',
          scheduledEnd: '',
          actualStart: '',
          actualEnd: '',
          assignedOperator: '',
          productionNotes: ''
        }
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
      },
      flowRules: {
        flowId: [{ required: true, message: '请输入生产流ID', trigger: 'blur' }],
        flowStatus: [{ required: true, message: '请选择状态', trigger: 'change' }],
        templateId: [{ required: true, message: '请选择流程模板', trigger: 'change' }],
        totalQuantity: [{ required: true, message: '请输入订单总数量', trigger: 'change' }],
        priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
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
     }
   },
  methods: {
    async initializeData() {
      await Promise.all([this.fetchFlowTemplates(), this.fetchOrders(), this.fetchFlows()])
    },
    async fetchFlowTemplates() {
      try {
        const response = await listFlowTemplateAll({})
        const list = Array.isArray(response.data) ? response.data : []
        this.flowTemplateOptions = list
      } catch (error) {
        this.flowTemplateOptions = []
        this.$message.error('获取流程模板失败')
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
    async fetchFlows() {
      try {
        const response = await listProductionFlows()
        const list = Array.isArray(response.data) ? response.data : []
        this.flowList = list.map(item => this.normalizeFlow(item))
      } catch (error) {
        this.flowList = []
        this.$message.error('获取生产流数据失败')
      }
    },
    handleOrderQuery() {
      this.fetchOrders()
    },
    normalizeOrder(order = {}) {
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
        flowTemplate: order.flowTemplate || null
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
      return {
        flowId: flow.flowId || '',
        orderIds: Array.isArray(flow.orderIds) ? flow.orderIds.slice() : [],
        templateId: flow.templateId || '',
        flowTemplate: flow.flowTemplate || null,
        flowStatus: flow.flowStatus || 'pending',
        totalQuantity: Number(flow.totalQuantity || 0),
        materialsSummary,
        priority: flow.priority || 'normal',
        scheduledStart: this.formatDateValue(flow.scheduledStart) || '',
        scheduledEnd: this.formatDateValue(flow.scheduledEnd) || '',
        actualStart: this.formatDateValue(flow.actualStart) || '',
        actualEnd: this.formatDateValue(flow.actualEnd) || '',
        assignedOperator: flow.assignedOperator || '',
        productionNotes: flow.productionNotes || '',
        createdAt: this.formatDateValue(flow.createdAt) || '',
        updatedAt: this.formatDateValue(flow.updatedAt) || ''
      }
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
        this.orderDialog.submitting = true
        try {
          if (this.orderDialog.isEdit) {
            await updateOrderPool(payload)
          } else {
            await addOrderPool(payload)
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
    handleDeleteOrder(order) {
      this.$confirm(`确认删除订单【${order.orderId}】吗？`, '提示', {
        type: 'warning'
      }).then(() => {
        removeOrderPool(order.orderId)
          .then(() => {
            this.$message.success('删除成功')
            this.fetchOrders()
          })
          .catch(() => {
            this.$message.error('删除失败')
          })
      }).catch(() => {})
    },
    handleBatchDeleteOrders() {
      this.$confirm(`确认删除选中的 ${this.selectedOrders.length} 条订单吗？`, '提示', {
        type: 'warning'
      }).then(() => {
        const ids = this.selectedOrders.map(item => item.orderId)
        if (!ids.length) {
          return
        }
        removeOrderPool(ids.join(','))
          .then(() => {
            this.$message.success('删除成功')
            this.selectedOrders = []
            this.fetchOrders()
          })
          .catch(() => {
            this.$message.error('删除失败')
          })
      }).catch(() => {})
    },
    viewOrder(order) {
      this.viewOrderDialog.record = { ...order }
      this.viewOrderDialog.visible = true
    },
    openFlowCreationDialog() {
      if (!this.selectedOrders.length) {
        this.$message.warning('请先选择至少一个订单')
        return
      }
      const selected = this.selectedOrders.map(item => ({ ...item }))
      const flowId = `FLOW-${this.nowDateStamp()}`
      const totalQuantity = selected.reduce((acc, cur) => acc + Number(cur.quantity || 0), 0)
      const materialsMap = {}
      selected.forEach(order => {
        const key = order.mainMaterial || '未知材料'
        materialsMap[key] = (materialsMap[key] || 0) + Number(order.quantity || 0)
      })
      const materialsSummary = Object.keys(materialsMap).map(key => ({
        material: key,
        quantity: materialsMap[key]
      }))
      const priority = selected.reduce((max, order) => {
        return PRIORITY_WEIGHT[order.priority] > PRIORITY_WEIGHT[max] ? order.priority : max
      }, 'low')
      const initialTemplateId = selected[0].templateId || ''
      const sameTemplate = selected.every(item => item.templateId === initialTemplateId)
      if (!sameTemplate) {
        this.$message.info('选中的订单使用的流程模板不同，请手动选择。')
      }
      const templateId = sameTemplate ? initialTemplateId : ''
      const flowTemplate = sameTemplate
        ? (selected[0].flowTemplate || this.findTemplateById(templateId))
        : null
      this.flowCreationDialog.form = {
        flowId,
        orderIds: selected.map(item => item.orderId),
        templateId,
        flowTemplate,
        flowStatus: 'pending',
        totalQuantity,
        materialsSummary,
        priority,
        scheduledStart: '',
        scheduledEnd: '',
        actualStart: '',
        actualEnd: '',
        assignedOperator: '',
        productionNotes: ''
      }
      this.flowCreationDialog.isEdit = false
      this.flowCreationDialog.submitting = false
      this.flowCreationDialog.title = '创建生产流'
      this.$nextTick(() => {
        if (this.$refs.flowForm) {
          this.$refs.flowForm.clearValidate()
        }
      })
      this.flowCreationDialog.visible = true
    },
    submitFlow() {
      if (!this.$refs.flowForm) return
      this.$refs.flowForm.validate(async valid => {
        if (!valid) return
        const payload = this.buildFlowPayload(this.flowCreationDialog.form)
        this.flowCreationDialog.submitting = true
        try {
          const exists = this.flowList.some(item => item.flowId === payload.flowId)
          if (exists) {
            await updateProductionFlow(payload)
          } else {
            await addProductionFlow(payload)
          }
          this.$message.success('生产流已生成')
          this.flowCreationDialog.visible = false
          this.selectedOrders = []
          await Promise.all([this.fetchFlows(), this.fetchOrders()])
        } catch (error) {
          this.$message.error('生成失败')
        } finally {
          this.flowCreationDialog.submitting = false
        }
      })
    },
    buildFlowPayload(form = {}) {
      return {
        flowId: form.flowId,
        templateId: form.templateId || '',
        orderIds: Array.isArray(form.orderIds) ? form.orderIds : [],
        flowStatus: form.flowStatus || 'pending',
        totalQuantity: Number(form.totalQuantity || 0),
        materialsSummary: Array.isArray(form.materialsSummary)
          ? form.materialsSummary.map((item, index) => ({
            materialId: item.materialId,
            material: item.material || '',
            quantity: Number(item.quantity || 0),
            sortOrder: item.sortOrder !== undefined ? item.sortOrder : index
          }))
          : [],
        priority: form.priority || 'normal',
        scheduledStart: form.scheduledStart ? this.formatDateValue(form.scheduledStart) : null,
        scheduledEnd: form.scheduledEnd ? this.formatDateValue(form.scheduledEnd) : null,
        actualStart: form.actualStart ? this.formatDateValue(form.actualStart) : null,
        actualEnd: form.actualEnd ? this.formatDateValue(form.actualEnd) : null,
        assignedOperator: form.assignedOperator || '',
        productionNotes: form.productionNotes || ''
      }
    },
    handleFlowTemplateChange(templateId) {
      this.flowCreationDialog.form.templateId = templateId
      this.flowCreationDialog.form.flowTemplate = this.findTemplateById(templateId)
    },
    handleOrderTemplateSelect(templateId) {
      this.orderDialog.form.templateId = templateId
      this.orderDialog.form.flowTemplate = this.findTemplateById(templateId)
    },
    findTemplateById(templateId) {
      if (!templateId) {
        return null
      }
      const fromOptions = this.flowTemplateOptions.find(item => item.templateId === templateId)
      if (fromOptions) {
        return fromOptions
      }
      const fromOrders = this.orderList.find(item => item.templateId === templateId && item.flowTemplate)
      return fromOrders ? fromOrders.flowTemplate : null
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
       return nowDateStampHelper()
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
  }

  .template-empty {
    color: #909399;
    font-size: 13px;
  }

   .mr5 {
     margin-right: 5px;
     margin-bottom: 4px;
   }
 }
 </style>
