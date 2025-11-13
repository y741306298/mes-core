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

        <el-form-item label="查询SQL" prop="querySql">
          <el-input
            v-model="form.querySql"
            type="textarea"
            :rows="3"
            placeholder="请输入用于查询任务执行结果的SQL语句"
          />
        </el-form-item>

        <el-form-item label="存储SQL" prop="storageSql">
          <el-input
            v-model="form.storageSql"
            type="textarea"
            :rows="3"
            placeholder="请输入用于存储任务执行结果的SQL语句"
          />
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
        <el-alert
          v-else
          class="mt10"
          type="warning"
          :closable="false"
          title="功能组合模板暂不支持在此处配置，如需调整请联系管理员"
          show-icon
        />

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
  delTaskTemplate
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
        triggerMode: [{ required: true, message: '请选择触发模式', trigger: 'change' }],
        querySql: [{ required: true, message: '查询SQL不能为空', trigger: 'blur' }],
        storageSql: [{ required: true, message: '存储SQL不能为空', trigger: 'blur' }]
      }
    }
  },
  computed: {
    isApiTemplate() {
      return this.form.templateType === 'API'
    }
  },
  created() {
    this.getList()
    this.reset()
  },
   methods: {
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
        resultStatuses: defaultResultStatuses(),
        querySql: '',
        storageSql: ''
      }
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
      this.form.querySql = data.querySql || ''
      this.form.storageSql = data.storageSql || ''
      this.form.requestUrl = config.requestUrl || ''
      this.form.requestParams = Array.isArray(config.requestParams) ? config.requestParams : []
      this.form.responseParams = Array.isArray(config.responseParams) ? config.responseParams : []
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
        if (!this.isApiTemplate) {
          this.$modal.msgWarning('功能组合模板暂不支持在此处编辑')
          return
        }
        if (this.isApiTemplate && !this.form.requestUrl) {
          this.$modal.msgError('请填写请求接口URL')
          return
        }
        const config = {
          requestUrl: this.form.requestUrl,
          requestParams: this.form.requestParams.map(item => ({ ...item })),
          responseParams: this.form.responseParams.map(item => ({ ...item }))
        }
        const resultStatusesPayload = this.form.resultStatuses.map(item => ({
          statusLabel: item.statusLabel,
          statusValue: item.statusValue
        }))
        const payload = {
          templateId: this.form.templateId,
          templateName: this.form.templateName,
          templateType: this.form.templateType,
          triggerMode: this.form.triggerMode,
          config: JSON.stringify(config),
          resultStatuses: JSON.stringify(resultStatusesPayload),
          querySql: (this.form.querySql || '').trim(),
          storageSql: (this.form.storageSql || '').trim()
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

}
</style>
