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
  removeOrderPool,
  listProductionFlows,
  addProductionFlow,
  updateProductionFlow
} from '@/api/productionflow/orderPool'
import { listFlowTemplateAll, getFlowTemplate } from '@/api/order/flowTemplate'
import { listTaskTemplateAll } from '@/api/order/taskTemplate'
import { submitRemark } from '@/api/order/orderNode'
import request from '@/utils/request'

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

const SYSTEM_NODE_TYPES = new Set(['0', '1', '2', '3', '4', '5', '6', '7'])

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
    },
    viewOrderFlowNodes() {
      if (!this.viewOrderDialog.record || !this.viewOrderDialog.record.flowTemplate) {
        return []
      }
      const nodes = this.viewOrderDialog.record.flowTemplate.flowNodeList
      return Array.isArray(nodes) ? nodes : []
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
      if (orderForm.orderId && payload.orderId == null) {
        payload.orderId = orderForm.orderId
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
      const config = taskTemplate.parsedConfig || this.parseTaskTemplateConfig(taskTemplate.config)
      if (!config.requestUrl) {
        return finalizeResult({ success: false, message: '任务模板未配置接口URL' })
      }
      try {
        const responseData = await this.executeTaskTemplate(taskTemplate, orderForm, config)
        const success = this.isTaskResponseSuccess(responseData)
        return finalizeResult({
          success,
          response: responseData,
          message: success ? '任务执行成功' : '接口返回未满足成功条件'
        })
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
        const flowNodes = Array.isArray(templateInstance.flowNodeList)
          ? templateInstance.flowNodeList
          : []
        const hasAutoNodes = flowNodes.some(node => this.isTaskTemplateNode(node))
        if (!hasAutoNodes) {
          return
        }
        this.setOrderAutomationState(orderForm.orderId, {
          templateId,
          templateInstance,
          orderForm: deepClone(orderForm),
          status: 'running',
          failedNode: null,
          pendingNodes: [],
          errorMessage: '',
          responsePreview: ''
        })
        await this.runTaskNodesSequence({
          templateId,
          template: templateInstance,
          nodes: flowNodes,
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
      const templateId = this.manualTaskDialog.templateId
      const template = this.manualTaskDialog.template
      const orderForm = this.manualTaskDialog.orderForm
      const pendingNodes = Array.isArray(this.manualTaskDialog.pendingNodes)
        ? this.manualTaskDialog.pendingNodes.slice()
        : []
      const orderId = this.manualTaskDialog.orderId
      const remark = (this.manualTaskDialog.remark || '').trim() || '人工处理完成'
      const orderNodeId = this.manualTaskDialog.node.orderNodeId || ''
      if (orderId && orderNodeId) {
        try {
          await submitRemark({ orderId, orderNodeId, remark })
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
          responsePreview: ''
        })
        await this.refreshOrderRecord(orderId, { silent: true, updateDialog: true })
      }
      this.resetManualTaskDialog()
      if (templateId && template && orderForm && pendingNodes.length) {
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
      return false
    },
    parseStatusCode(value) {
      if (value === undefined || value === null) {
        return null
      }
      const code = typeof value === 'string' ? Number.parseInt(value, 10) : value
      if (Number.isNaN(code)) {
        return null
      }
      return code === 200
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
      const state = this.orderAutomationState[orderId]
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
        const state = this.orderAutomationState[orderId]
        if (state && state.failedNode && this.isSameFlowNode(state.failedNode, node)) {
          this.openManualTaskDialogForOrder(orderId)
          return
        }
        if (!this.isManualOnlyFlowNode(node)) {
          return
        }
        this.openManualDialogForManualNode({ node, record })
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
      const state = this.orderAutomationState[orderId]
      if (!state || !state.failedNode) {
        this.$message.warning('暂无需要人工处理的节点')
        return
      }
      this.manualTaskDialog.visible = true
      this.manualTaskDialog.node = state.failedNode
      this.manualTaskDialog.template = state.templateInstance
      this.manualTaskDialog.templateId = state.templateId || ''
      this.manualTaskDialog.orderForm = state.orderForm || null
      this.manualTaskDialog.pendingNodes = Array.isArray(state.pendingNodes) ? state.pendingNodes.slice() : []
      this.manualTaskDialog.errorMessage = state.errorMessage || ''
      this.manualTaskDialog.responsePreview = state.responsePreview || ''
      this.manualTaskDialog.remark = ''
      this.manualTaskDialog.orderId = orderId
    },
    setOrderAutomationState(orderId, payload = {}) {
      if (!orderId) {
        return
      }
      const previous = this.orderAutomationState[orderId] || {}
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
        responsePreview: payload.responsePreview !== undefined ? payload.responsePreview : previous.responsePreview
      }
      this.$set(this.orderAutomationState, orderId, next)
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
      const state = this.orderAutomationState[row.orderId]
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
    async viewOrder(order) {
      if (!order || !order.orderId) {
        this.$message.warning('订单数据异常，请稍后重试')
        return
      }
      this.viewOrderDialog.record = this.buildViewOrderRecord(order)
      this.viewOrderDialog.visible = true
      await this.refreshOrderRecord(order.orderId, { silent: true, updateDialog: true, withLoading: true })
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
    async handleFlowTemplateChange(templateId) {
      this.flowCreationDialog.form.templateId = templateId
      const template = await this.ensureFlowTemplateDetails(templateId)
      this.flowCreationDialog.form.flowTemplate = template || this.findTemplateById(templateId)
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
      const automationState = record.orderId ? this.orderAutomationState[record.orderId] : null
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
}

::v-deep .order-row-failed td {
  background-color: #fde2e2 !important;
}

.arrow-first {
  display: flex;
}

.first-center,
.first-center-active,
.first-center-refuse {
  width: 100px;
  text-align: center;
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
