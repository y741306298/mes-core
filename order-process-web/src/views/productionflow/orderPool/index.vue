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
         <el-table-column label="订单预处理" width="110">
           <template slot-scope="scope">
             <el-tag :type="preprocessTagType(scope.row)">
               {{ preprocessResult(scope.row) }}
             </el-tag>
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
               <el-input v-model="orderDialog.form.orderId" placeholder="请输入订单编号" />
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
         <el-button type="primary" @click="submitOrder">保 存</el-button>
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

         <h4 class="section-title">订单预处理进度</h4>
         <el-steps :active="preprocessActiveStep(viewOrderDialog.record)" align-center>
           <el-step
             v-for="(step, index) in viewOrderDialog.record.preprocessSteps"
             :key="step.name"
             :title="step.name"
             :status="stepStatus(step.status)"
           >
             <template slot="description">
               <div class="step-detail">
                 <el-tag size="mini" :type="stepStatusTag(step.status)">
                   {{ preprocessStatusText(step.status) }}
                 </el-tag>
                 <el-button
                   v-if="step.status === 'failed'"
                   type="text"
                   size="mini"
                   @click="openStepIntervention(viewOrderDialog.record, index)"
                 >人工处理</el-button>
                 <div class="step-remark" v-if="step.remark">{{ step.remark }}</div>
               </div>
             </template>
           </el-step>
         </el-steps>
       </div>
       <span slot="footer" class="dialog-footer">
         <el-button @click="viewOrderDialog.visible = false">关 闭</el-button>
       </span>
     </el-dialog>

     <!-- 预处理步骤人工介入 -->
     <el-dialog title="预处理步骤调整" :visible.sync="stepDialog.visible" width="480px">
       <el-form :model="stepDialog.form" label-width="100px">
         <el-form-item label="步骤名称">
           <el-input v-model="stepDialog.form.name" disabled />
         </el-form-item>
         <el-form-item label="状态">
           <el-select v-model="stepDialog.form.status" placeholder="请选择状态">
             <el-option label="待处理" value="pending" />
             <el-option label="成功" value="success" />
             <el-option label="失败" value="failed" />
           </el-select>
         </el-form-item>
         <el-form-item label="备注">
           <el-input v-model="stepDialog.form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
         </el-form-item>
       </el-form>
       <span slot="footer" class="dialog-footer">
         <el-button @click="stepDialog.visible = false">取 消</el-button>
         <el-button type="primary" @click="confirmStepIntervention">确 认</el-button>
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

         <h4 class="section-title">当前进度</h4>
         <el-table :data="flowCreationDialog.form.process" border size="mini">
           <el-table-column prop="name" label="步骤" />
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
         </el-table>
       </div>

       <span slot="footer" class="dialog-footer">
         <el-button @click="flowCreationDialog.visible = false">取 消</el-button>
         <el-button type="primary" @click="submitFlow">生 成</el-button>
       </span>
     </el-dialog>
   </div>
 </template>

 <script>
 import { loadOrders, saveOrders, loadFlows, saveFlows } from '../dataStore'

 const PRIORITY_WEIGHT = {
   low: 1,
   normal: 2,
   high: 3,
   urgent: 4
 }

 const DEFAULT_PREPROCESS_STEPS = [
   {
     name: '检查文件状态',
     status: 'pending',
     remark: ''
   },
   {
     name: '获取快递单号',
     status: 'pending',
     remark: ''
   },
   {
     name: '生成plt',
     status: 'pending',
     remark: ''
   }
 ]

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
     preprocessSteps: [
       { name: '检查文件状态', status: 'success', remark: '文件格式正确' },
       { name: '获取快递单号', status: 'pending', remark: '' },
       { name: '生成plt', status: 'pending', remark: '' }
     ]
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
     preprocessSteps: [
       { name: '检查文件状态', status: 'failed', remark: '缺少零件3的模型' },
       { name: '获取快递单号', status: 'pending', remark: '' },
       { name: '生成plt', status: 'pending', remark: '' }
     ]
   },
   {
     orderId: 'ORD-20240503-009',
     previewImage: '',
     quantity: 240,
     remark: '',
     mainMaterial: 'ABS',
     craftRequirements: '高温打印，需防翘边',
     orderStatus: '处理中',
     createdAt: '2024-05-03 08:40',
     updatedAt: '2024-05-05 15:10',
     customerInfo: '苏州华维电子',
     priority: 'urgent',
     deliveryDate: '2024-05-12 12:00',
     sizeRequirement: '150 x 150 x 20mm',
     colorRequirement: '黑色',
     fileFormat: 'IGES',
     preprocessSteps: [
       { name: '检查文件状态', status: 'success', remark: '' },
       { name: '获取快递单号', status: 'success', remark: '顺丰 SF123456789CN' },
       { name: '生成plt', status: 'pending', remark: '' }
     ]
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
       orderList: loadOrders(getDefaultOrders()),
       flowList: loadFlows(getDefaultFlows()),
       selectedOrders: [],
       orderDialog: {
         visible: false,
         title: '',
         form: {}
       },
       viewOrderDialog: {
         visible: false,
         title: '订单详情',
         record: null
       },
       stepDialog: {
         visible: false,
         record: null,
         index: -1,
         form: {
           name: '',
           status: 'pending',
           remark: ''
         }
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
       orderRules: {
         orderId: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
         quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
         mainMaterial: [{ required: true, message: '请输入主材料', trigger: 'blur' }],
         orderStatus: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
         priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
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
         totalQuantity: [{ required: true, message: '请输入订单总数量', trigger: 'change' }],
         priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
       }
     }
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
     handleOrderQuery() {
       // computed 自动处理，无需额外逻辑，此处预留接口
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
     preprocessResult(order) {
       const { preprocessSteps = [] } = order
       if (!preprocessSteps.length) return '待处理'
       if (preprocessSteps.some(step => step.status === 'failed')) return '失败'
       if (preprocessSteps.every(step => step.status === 'success')) return '成功'
       return '进行中'
     },
     preprocessTagType(order) {
       const result = this.preprocessResult(order)
       if (result === '成功') return 'success'
       if (result === '失败') return 'danger'
       if (result === '进行中') return 'warning'
       return 'info'
     },
     preprocessActiveStep(order) {
       const steps = order.preprocessSteps || []
       const completed = steps.filter(step => step.status === 'success').length
       if (!steps.length) return 0
       if (completed >= steps.length) {
         return steps.length
       }
       return completed + 1
     },
     preprocessStatusText(status) {
       const mapping = {
         pending: '待处理',
         success: '成功',
         failed: '失败',
         processing: '处理中'
       }
       return mapping[status] || '待处理'
     },
     stepStatusTag(status) {
       const mapping = {
         pending: 'info',
         processing: 'warning',
         success: 'success',
         failed: 'danger'
       }
       return mapping[status] || 'info'
     },
     stepStatus(status) {
       const mapping = {
         pending: 'wait',
         processing: 'process',
         success: 'success',
         failed: 'error'
       }
       return mapping[status] || 'wait'
     },
     handleOrderSelectionChange(val) {
       this.selectedOrders = val
     },
     openOrderDialog(order) {
       if (order) {
         this.orderDialog.title = '编辑订单'
         this.orderDialog.form = {
           ...order,
           preprocessSteps: (order.preprocessSteps || []).map(item => ({ ...item }))
         }
       } else {
         this.orderDialog.title = '新增订单'
         this.orderDialog.form = {
           orderId: '',
           previewImage: '',
           quantity: 1,
           remark: '',
           mainMaterial: '',
           craftRequirements: '',
           orderStatus: '待处理',
           createdAt: this.nowDateTime(),
           updatedAt: this.nowDateTime(),
           customerInfo: '',
           priority: 'normal',
           deliveryDate: '',
           sizeRequirement: '',
           colorRequirement: '',
           fileFormat: '',
           preprocessSteps: DEFAULT_PREPROCESS_STEPS.map(item => ({ ...item }))
         }
       }
       this.$nextTick(() => {
         if (this.$refs.orderForm) {
           this.$refs.orderForm.clearValidate()
         }
       })
       this.orderDialog.visible = true
     },
     submitOrder() {
       this.$refs.orderForm.validate(valid => {
         if (!valid) return
         const form = {
           ...this.orderDialog.form,
           preprocessSteps: (this.orderDialog.form.preprocessSteps || []).map(item => ({ ...item }))
         }
         form.deliveryDate = form.deliveryDate ? this.formatDateValue(form.deliveryDate) : ''
         form.updatedAt = this.nowDateTime()
         if (!this.orderDialog.form.preprocessSteps || !this.orderDialog.form.preprocessSteps.length) {
           form.preprocessSteps = DEFAULT_PREPROCESS_STEPS.map(item => ({ ...item }))
         }
         const index = this.orderList.findIndex(item => item.orderId === form.orderId)
         if (index > -1) {
           this.$set(this.orderList, index, { ...this.orderList[index], ...form })
         } else {
           form.createdAt = this.nowDateTime()
           this.orderList.push(form)
         }
         saveOrders(this.orderList)
         this.orderDialog.visible = false
         this.$message.success('保存成功')
       })
     },
     handleDeleteOrder(order) {
       this.$confirm(`确认删除订单【${order.orderId}】吗？`, '提示', {
         type: 'warning'
       }).then(() => {
         this.orderList = this.orderList.filter(item => item.orderId !== order.orderId)
         saveOrders(this.orderList)
         this.$message.success('删除成功')
       }).catch(() => {})
     },
     handleBatchDeleteOrders() {
       this.$confirm(`确认删除选中的 ${this.selectedOrders.length} 条订单吗？`, '提示', {
         type: 'warning'
       }).then(() => {
         const ids = this.selectedOrders.map(item => item.orderId)
         this.orderList = this.orderList.filter(item => !ids.includes(item.orderId))
         this.selectedOrders = []
         saveOrders(this.orderList)
         this.$message.success('删除成功')
       }).catch(() => {})
     },
     viewOrder(order) {
       this.viewOrderDialog.record = { ...order }
       this.viewOrderDialog.visible = true
     },
     openStepIntervention(order, index) {
       this.stepDialog.record = order
       this.stepDialog.index = index
       const step = order.preprocessSteps[index]
       this.stepDialog.form = { ...step }
       this.stepDialog.visible = true
     },
     confirmStepIntervention() {
       if (!this.stepDialog.record || this.stepDialog.index < 0) return
       const orderIndex = this.orderList.findIndex(item => item.orderId === this.stepDialog.record.orderId)
       if (orderIndex < 0) return
       const order = { ...this.orderList[orderIndex] }
       order.preprocessSteps = order.preprocessSteps.map((item, idx) => {
         if (idx === this.stepDialog.index) {
           return { ...item, ...this.stepDialog.form }
         }
         return item
       })
       order.updatedAt = this.nowDateTime()
       this.$set(this.orderList, orderIndex, order)
       if (this.viewOrderDialog.record && this.viewOrderDialog.record.orderId === order.orderId) {
         this.viewOrderDialog.record = { ...order }
       }
       saveOrders(this.orderList)
       this.$message.success('步骤状态已更新')
       this.stepDialog.visible = false
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
       this.flowCreationDialog.form = {
         flowId,
         orderIds: selected.map(item => item.orderId),
         flowStatus: 'pending',
         process: [
           { name: '文件准备', status: 'pending', remark: '' },
           { name: '排版设计', status: 'pending', remark: '' },
           { name: '打印执行', status: 'pending', remark: '' },
           { name: '后处理/质检', status: 'pending', remark: '' }
         ],
         totalQuantity,
         materialsSummary,
         priority,
         scheduledStart: '',
         scheduledEnd: '',
         actualStart: '',
         actualEnd: '',
         assignedOperator: '',
         productionNotes: '',
         createdAt: this.nowDateTime(),
         updatedAt: this.nowDateTime()
       }
       this.flowCreationDialog.title = '创建生产流'
       this.$nextTick(() => {
         if (this.$refs.flowForm) {
           this.$refs.flowForm.clearValidate()
         }
       })
       this.flowCreationDialog.visible = true
     },
     submitFlow() {
       this.$refs.flowForm.validate(valid => {
         if (!valid) return
         const form = { ...this.flowCreationDialog.form }
         form.scheduledStart = form.scheduledStart ? this.formatDateValue(form.scheduledStart) : ''
         form.scheduledEnd = form.scheduledEnd ? this.formatDateValue(form.scheduledEnd) : ''
         form.actualStart = form.actualStart ? this.formatDateValue(form.actualStart) : ''
         form.actualEnd = form.actualEnd ? this.formatDateValue(form.actualEnd) : ''
         const index = this.flowList.findIndex(item => item.flowId === form.flowId)
         if (index > -1) {
           this.$set(this.flowList, index, { ...this.flowList[index], ...form })
         } else {
           this.flowList.push(form)
         }
         saveFlows(this.flowList)
         this.flowCreationDialog.visible = false
         this.$message.success('生产流已生成')
         const ids = new Set(form.orderIds)
         this.orderList = this.orderList.map(order => {
           if (ids.has(order.orderId)) {
             return { ...order, orderStatus: '已入池', updatedAt: this.nowDateTime() }
           }
           return order
         })
         saveOrders(this.orderList)
         this.selectedOrders = []
       })
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
