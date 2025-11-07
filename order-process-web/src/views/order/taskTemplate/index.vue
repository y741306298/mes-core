 <template>
   <div class="app-container task-template-page">
     <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
       <el-form-item label="模板名称" prop="templateName">
         <el-input v-model="queryParams.templateName" placeholder="请输入模板名称" clearable @keyup.enter.native="handleQuery" />
       </el-form-item>
       <el-form-item>
         <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
         <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
       </el-form-item>
     </el-form>

     <el-row :gutter="10" class="mb8">
       <el-col :span="1.5">
         <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
           v-hasPermi="['order:taskTemplate:add']">新增任务模板</el-button>
       </el-col>
     </el-row>

     <el-table v-loading="loading" :data="taskTemplateList" border>
       <el-table-column label="模板名称" align="center" prop="templateName" min-width="160" />
       <el-table-column label="模板类型" align="center" min-width="140">
         <template slot-scope="scope">{{ renderTemplateType(scope.row.templateType) }}</template>
       </el-table-column>
       <el-table-column label="触发模式" align="center" min-width="120">
         <template slot-scope="scope">{{ renderTriggerMode(scope.row.triggerMode) }}</template>
       </el-table-column>
       <el-table-column label="结果状态" align="center" min-width="200">
         <template slot-scope="scope">{{ renderResultStatuses(scope.row.resultStatuses) }}</template>
       </el-table-column>
       <el-table-column label="更新时间" align="center" prop="updateTime" min-width="160" />
       <el-table-column label="操作" align="center" width="180">
         <template slot-scope="scope">
           <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
             v-hasPermi="['order:taskTemplate:edit']">修改</el-button>
           <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
             v-hasPermi="['order:taskTemplate:remove']">删除</el-button>
         </template>
       </el-table-column>
     </el-table>

     <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
       @pagination="getList" />

     <el-dialog :title="title" :visible.sync="open" width="960px" append-to-body>
       <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="task-template-form">
         <el-form-item label="模板名称" prop="templateName">
           <el-input v-model="form.templateName" placeholder="请输入模板名称" />
         </el-form-item>

         <el-form-item label="模板类型" prop="templateType">
           <el-radio-group v-model="form.templateType">
             <el-radio-button label="API">API调用任务模板</el-radio-button>
             <el-radio-button label="FUNCTION">功能组合模板</el-radio-button>
           </el-radio-group>
         </el-form-item>

         <el-form-item label="触发模式" prop="triggerMode">
           <el-radio-group v-model="form.triggerMode">
             <el-radio-button label="AUTO">自动触发</el-radio-button>
             <el-radio-button label="MANUAL">人工触发</el-radio-button>
           </el-radio-group>
         </el-form-item>

         <el-form-item label="结果状态">
           <div class="table-toolbar">
             <el-button type="primary" size="mini" plain @click="handleAddResultStatus">新增状态</el-button>
           </div>
           <el-table :data="form.resultStatuses" border size="mini" class="status-table">
             <el-table-column label="状态名称" align="center" min-width="160">
               <template slot-scope="scope">
                 <el-input v-model="scope.row.statusLabel" placeholder="请输入状态名称" size="mini" />
               </template>
             </el-table-column>
             <el-table-column label="状态编码" align="center" min-width="160">
               <template slot-scope="scope">
                 <el-input v-model="scope.row.statusValue" placeholder="请输入状态编码" size="mini" />
               </template>
             </el-table-column>
             <el-table-column label="操作" align="center" width="120">
               <template slot-scope="scope">
                 <el-button type="text" size="mini" @click="handleRemoveResultStatus(scope.$index)"
                   :disabled="form.resultStatuses.length <= 1">删除</el-button>
               </template>
             </el-table-column>
           </el-table>
           <div class="status-tip">建议至少保留“成功”和“失败”等关键状态，用于流程判断。</div>
         </el-form-item>

         <template v-if="isApiTemplate">
           <el-form-item label="请求接口URL" prop="requestUrl">
             <el-input v-model="form.requestUrl" placeholder="请输入接口URL" />
           </el-form-item>

           <el-form-item label="请求入参">
             <div class="table-toolbar">
               <el-button type="primary" size="mini" plain @click="handleAddRequestParam">新增入参</el-button>
             </div>
             <el-table :data="form.requestParams" border size="mini" class="param-table">
               <el-table-column label="参数名称" align="center" min-width="150">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.paramName" placeholder="请输入参数名称" size="mini" />
                 </template>
               </el-table-column>
               <el-table-column label="参数编码" align="center" min-width="150">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.paramKey" placeholder="请输入参数编码" size="mini" />
                 </template>
               </el-table-column>
               <el-table-column label="参数类型" align="center" min-width="140">
                 <template slot-scope="scope">
                   <el-select v-model="scope.row.paramType" placeholder="请选择类型" size="mini">
                     <el-option label="字符串" value="string" />
                     <el-option label="数字" value="number" />
                     <el-option label="布尔" value="boolean" />
                     <el-option label="对象" value="object" />
                     <el-option label="数组" value="array" />
                   </el-select>
                 </template>
               </el-table-column>
               <el-table-column label="是否必填" align="center" width="110">
                 <template slot-scope="scope">
                   <el-switch v-model="scope.row.required" active-value="1" inactive-value="0" />
                 </template>
               </el-table-column>
               <el-table-column label="描述" align="center" min-width="200">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.remark" placeholder="请输入描述" size="mini" />
                 </template>
               </el-table-column>
               <el-table-column label="操作" align="center" width="120">
                 <template slot-scope="scope">
                   <el-button type="text" size="mini" @click="handleRemoveRequestParam(scope.$index)">删除</el-button>
                 </template>
               </el-table-column>
             </el-table>
           </el-form-item>

           <el-form-item label="接收出参">
             <div class="table-toolbar">
               <el-button type="primary" size="mini" plain @click="handleAddResponseParam">新增出参</el-button>
             </div>
             <el-table :data="form.responseParams" border size="mini" class="param-table">
               <el-table-column label="参数名称" align="center" min-width="150">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.paramName" placeholder="请输入参数名称" size="mini" />
                 </template>
               </el-table-column>
               <el-table-column label="参数编码" align="center" min-width="150">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.paramKey" placeholder="请输入参数编码" size="mini" />
                 </template>
               </el-table-column>
               <el-table-column label="参数类型" align="center" min-width="140">
                 <template slot-scope="scope">
                   <el-select v-model="scope.row.paramType" placeholder="请选择类型" size="mini">
                     <el-option label="字符串" value="string" />
                     <el-option label="数字" value="number" />
                     <el-option label="布尔" value="boolean" />
                     <el-option label="对象" value="object" />
                     <el-option label="数组" value="array" />
                   </el-select>
                 </template>
               </el-table-column>
               <el-table-column label="描述" align="center" min-width="200">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.remark" placeholder="请输入描述" size="mini" />
                 </template>
               </el-table-column>
               <el-table-column label="操作" align="center" width="120">
                 <template slot-scope="scope">
                   <el-button type="text" size="mini" @click="handleRemoveResponseParam(scope.$index)">删除</el-button>
                 </template>
               </el-table-column>
             </el-table>
           </el-form-item>
         </template>

         <template v-else>
           <el-form-item label="功能卡片">
             <div class="function-card-selector">
               <el-select v-model="selectedFunctionCard" placeholder="请选择功能卡片" filterable size="small">
                 <el-option v-for="item in functionCardOptions" :key="item.cardId" :label="item.cardName"
                   :value="item.cardId" :disabled="isCardSelected(item.cardId)" />
               </el-select>
               <el-button type="primary" size="mini" @click="handleAppendFunctionCard">添加卡片</el-button>
             </div>
             <el-empty v-if="!form.functionCards.length" description="请先选择功能卡片" />
             <el-table v-else :data="form.functionCards" border size="mini" class="function-card-table">
               <el-table-column label="顺序" width="80" align="center">
                 <template slot-scope="scope">{{ scope.$index + 1 }}</template>
               </el-table-column>
               <el-table-column label="功能卡片" min-width="200" align="center">
                 <template slot-scope="scope">{{ scope.row.cardName }}</template>
               </el-table-column>
               <el-table-column label="描述" min-width="240" align="center">
                 <template slot-scope="scope">
                   <span>{{ scope.row.description || '-' }}</span>
                 </template>
               </el-table-column>
               <el-table-column label="操作" width="180" align="center">
                 <template slot-scope="scope">
                   <el-button type="text" size="mini" @click="handleMoveCard(scope.$index, -1)"
                     :disabled="scope.$index === 0">上移</el-button>
                   <el-button type="text" size="mini" @click="handleMoveCard(scope.$index, 1)"
                     :disabled="scope.$index === form.functionCards.length - 1">下移</el-button>
                   <el-button type="text" size="mini" @click="handleRemoveCard(scope.$index)">删除</el-button>
                 </template>
               </el-table-column>
             </el-table>
           </el-form-item>
         </template>
       </el-form>
       <div slot="footer" class="dialog-footer">
         <el-button type="primary" @click="submitForm">确 定</el-button>
         <el-button @click="cancel">取 消</el-button>
       </div>
     </el-dialog>
   </div>
 </template>

 <script>
 import {
   listTaskTemplate,
   getTaskTemplate,
   addTaskTemplate,
   updateTaskTemplate,
   delTaskTemplate,
   listTaskFunctionCards
 } from '@/api/order/taskTemplate'

 const defaultResultStatuses = () => ([
   { statusLabel: '成功', statusValue: 'SUCCESS' },
   { statusLabel: '失败', statusValue: 'FAILED' }
 ])

 const createEmptyRequestParam = () => ({
   paramName: '',
   paramKey: '',
   paramType: 'string',
   required: '0',
   remark: ''
 })

 const createEmptyResponseParam = () => ({
   paramName: '',
   paramKey: '',
   paramType: 'string',
   remark: ''
 })

 export default {
   name: 'TaskTemplate',
   data() {
     return {
       loading: false,
       total: 0,
       taskTemplateList: [],
       title: '',
       open: false,
       queryParams: {
         pageNum: 1,
         pageSize: 10,
         templateName: null
       },
       form: {},
       rules: {
         templateName: [{ required: true, message: '模板名称不能为空', trigger: 'blur' }],
         templateType: [{ required: true, message: '请选择模板类型', trigger: 'change' }],
         triggerMode: [{ required: true, message: '请选择触发模式', trigger: 'change' }]
       },
       functionCardOptions: [],
       selectedFunctionCard: null
     }
   },
   computed: {
     isApiTemplate() {
       return this.form.templateType === 'API'
     }
   },
   watch: {
     'form.templateType'(value, oldValue) {
       if (!oldValue || value === oldValue) return
       if (value === 'API') {
         this.form.requestUrl = ''
         this.form.requestParams = []
         this.form.responseParams = []
       } else {
         this.form.functionCards = []
       }
     }
   },
   created() {
     this.getList()
     this.loadFunctionCards()
     this.reset()
   },
   methods: {
     loadFunctionCards() {
       listTaskFunctionCards().then(res => {
         const data = res.rows || res.data || []
         this.functionCardOptions = data.map(item => ({
           cardId: item.cardId || item.id,
           cardName: item.cardName || item.name,
           cardCode: item.cardCode || item.code,
           description: item.description || item.remark || ''
         }))
         if (this.form && Array.isArray(this.form.functionCards) && this.form.functionCards.length) {
           this.form.functionCards = this.form.functionCards.map(card => {
             const cardId = card.cardId || card.id || card
             const option = this.functionCardOptions.find(item => item.cardId == cardId)
             if (!option) {
               return card
             }
             return {
               cardId: option.cardId,
               cardName: option.cardName,
               cardCode: option.cardCode,
               description: card.description || option.description || ''
             }
           })
         }
       }).catch(() => {
         this.functionCardOptions = []
       })
     },
     getList() {
       this.loading = true
       listTaskTemplate(this.queryParams).then(response => {
         this.taskTemplateList = response.rows || []
         this.total = response.total || 0
         this.loading = false
       }).catch(() => {
         this.loading = false
       })
     },
     handleQuery() {
       this.queryParams.pageNum = 1
       this.getList()
     },
     resetQuery() {
       this.resetForm('queryForm')
       this.handleQuery()
     },
     reset() {
       this.form = {
         templateId: null,
         templateName: '',
         templateType: 'API',
         triggerMode: 'AUTO',
         requestUrl: '',
         requestParams: [],
         responseParams: [],
         functionCards: [],
         resultStatuses: defaultResultStatuses()
       }
       this.selectedFunctionCard = null
       if (this.$refs.form) {
         this.resetForm('form')
       }
     },
     handleAdd() {
       this.reset()
       this.open = true
       this.title = '新增任务模板'
     },
     handleUpdate(row) {
       const templateId = row.templateId || row.id
       if (!templateId) return
       this.reset()
       getTaskTemplate(templateId).then(response => {
         const data = response.data || response
         this.applyServerForm(data)
         this.open = true
         this.title = '修改任务模板'
       })
     },
     applyServerForm(data) {
       const config = this.parseJsonField(data.config)
       const resultStatuses = this.parseJsonField(data.resultStatuses, true)
       this.form.templateId = data.templateId || data.id
       this.form.templateName = data.templateName
       this.form.templateType = data.templateType || 'API'
       this.form.triggerMode = data.triggerMode || 'AUTO'
       if (this.form.templateType === 'API') {
         this.form.requestUrl = config.requestUrl || ''
         this.form.requestParams = Array.isArray(config.requestParams) ? config.requestParams : []
         this.form.responseParams = Array.isArray(config.responseParams) ? config.responseParams : []
       } else {
         const cards = Array.isArray(config.functionCards) ? config.functionCards : []
         this.form.functionCards = cards.map(item => {
           if (typeof item === 'string' || typeof item === 'number') {
             const option = this.functionCardOptions.find(opt => opt.cardId == item)
             return option ? { ...option } : { cardId: item, cardName: item }
           }
           return {
             cardId: item.cardId || item.id,
             cardName: item.cardName || item.name,
             cardCode: item.cardCode || item.code,
             description: item.description || item.remark || ''
           }
         })
       }
       this.form.resultStatuses = Array.isArray(resultStatuses) && resultStatuses.length
         ? resultStatuses.map(item => ({
           statusLabel: item.statusLabel || item.label || item.name,
           statusValue: item.statusValue || item.value || item.code
         }))
         : defaultResultStatuses()
     },
     parseJsonField(value, allowArray = false) {
       if (value == null) return allowArray ? [] : {}
       if (typeof value === 'object') return value
       try {
         const parsed = JSON.parse(value)
         return parsed
       } catch (e) {
         return allowArray ? [] : {}
       }
     },
     cancel() {
       this.open = false
     },
     submitForm() {
       this.$refs.form.validate(valid => {
         if (!valid) {
           return
         }
         if (!this.form.resultStatuses.length) {
           this.$modal.msgError('请至少保留一个结果状态')
           return
         }
         if (this.isApiTemplate && !this.form.requestUrl) {
           this.$modal.msgError('请填写请求接口URL')
           return
         }
         const config = this.isApiTemplate
           ? {
             requestUrl: this.form.requestUrl,
             requestParams: this.form.requestParams.map(item => ({ ...item })),
             responseParams: this.form.responseParams.map(item => ({ ...item }))
           }
           : {
             functionCards: this.form.functionCards.map(item => ({
               cardId: item.cardId,
               cardName: item.cardName,
               cardCode: item.cardCode,
               description: item.description
             }))
           }
         const payload = {
           templateId: this.form.templateId,
           templateName: this.form.templateName,
           templateType: this.form.templateType,
           triggerMode: this.form.triggerMode,
           config: config,
           resultStatuses: this.form.resultStatuses.map(item => ({
             statusLabel: item.statusLabel,
             statusValue: item.statusValue
           }))
         }
         const request = payload.templateId ? updateTaskTemplate : addTaskTemplate
         request(payload).then(() => {
           this.$modal.msgSuccess(payload.templateId ? '修改成功' : '新增成功')
           this.open = false
           this.getList()
         })
       })
     },
     handleDelete(row) {
       const templateId = row.templateId || row.id
       if (!templateId) return
       this.$modal.confirm('是否确认删除任务模板编号为"' + templateId + '"的数据项？').then(() => {
         return delTaskTemplate(templateId)
       }).then(() => {
         this.getList()
         this.$modal.msgSuccess('删除成功')
       }).catch(() => { })
     },
     handleAddRequestParam() {
       this.form.requestParams.push(createEmptyRequestParam())
     },
     handleRemoveRequestParam(index) {
       this.form.requestParams.splice(index, 1)
     },
     handleAddResponseParam() {
       this.form.responseParams.push(createEmptyResponseParam())
     },
     handleRemoveResponseParam(index) {
       this.form.responseParams.splice(index, 1)
     },
     handleAddResultStatus() {
       this.form.resultStatuses.push({ statusLabel: '', statusValue: '' })
     },
     handleRemoveResultStatus(index) {
       if (this.form.resultStatuses.length <= 1) {
         this.$modal.msgWarning('至少需要保留一个结果状态')
         return
       }
       this.form.resultStatuses.splice(index, 1)
     },
     handleAppendFunctionCard() {
       if (!this.selectedFunctionCard) {
         this.$modal.msgWarning('请选择功能卡片')
         return
       }
       const option = this.functionCardOptions.find(item => item.cardId === this.selectedFunctionCard)
       if (option) {
         this.form.functionCards.push({ ...option })
       }
       this.selectedFunctionCard = null
     },
     handleRemoveCard(index) {
       this.form.functionCards.splice(index, 1)
     },
     handleMoveCard(index, step) {
       const newIndex = index + step
       if (newIndex < 0 || newIndex >= this.form.functionCards.length) return
       const list = [...this.form.functionCards]
       const temp = list[index]
       list.splice(index, 1)
       list.splice(newIndex, 0, temp)
       this.form.functionCards = list
     },
     isCardSelected(cardId) {
       return this.form.functionCards.some(item => item.cardId === cardId)
     },
     renderTemplateType(value) {
       switch (value) {
         case 'API':
           return 'API调用任务模板'
         case 'FUNCTION':
           return '功能组合模板'
         default:
           return value || '-'
       }
     },
     renderTriggerMode(value) {
       switch (value) {
         case 'AUTO':
           return '自动触发'
         case 'MANUAL':
           return '人工触发'
         default:
           return value || '-'
       }
     },
     renderResultStatuses(value) {
       let result = value
       if (typeof value === 'string') {
         result = this.parseJsonField(value, true)
       }
       if (!Array.isArray(result)) {
         return '-'
       }
       return result.map(item => item.statusLabel || item.label || item.name || item.statusValue).join(' / ')
     }
   }
 }
 </script>

 <style lang="scss" scoped>
 .task-template-page {
   .table-toolbar {
     margin-bottom: 10px;
   }

   .status-table,
   .param-table,
   .function-card-table {
     margin-bottom: 10px;
   }

   .status-tip {
     font-size: 12px;
     color: #909399;
   }

   .function-card-selector {
     display: flex;
     align-items: center;
     margin-bottom: 10px;

     .el-select {
       flex: 1;
       margin-right: 10px;
     }
   }
 }
 </style>
