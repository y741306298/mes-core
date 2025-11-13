<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">基本信息</div>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
    <el-divider></el-divider>

    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model="form.templateName" placeholder="请输入模板名称" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="负责人" prop="userId">
            <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="审核人" prop="auditUserId">
            <el-select v-model="form.auditUserId" @change="auditUserChange" filterable clearable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="formHeader flow-progress-header">
      <div class="formTitle">流程进度</div>
      <el-button type="success" plain icon="el-icon-collection" size="mini" @click="openTaskSelector">
        选择任务模板
      </el-button>
    </div>
    <el-divider></el-divider>

    <div class="selected-task-wrapper" v-if="selectedTaskTemplates.length">
      <div class="selected-task-header">
        <span>已选择的任务模板</span>
        <el-button type="text" size="mini" @click="clearAllSelectedTasks">清空</el-button>
      </div>
      <div class="selected-task-grid">
        <el-card v-for="item in selectedTaskTemplates" :key="item.templateId" class="selected-task-card" shadow="hover">
          <div class="card-title-row">
            <span class="card-title" :title="item.templateName">{{ item.templateName }}</span>
            <el-tag size="mini" type="info">{{ renderTaskTemplateType(item.templateType) }}</el-tag>
          </div>
          <div class="card-body">
            <div class="card-desc">{{ item.remark || '暂无描述' }}</div>
            <div class="card-meta">触发方式：{{ renderTriggerMode(item.triggerMode) }}</div>
            <div class="card-status" v-if="item.statusLabels && item.statusLabels.length">
              状态：{{ item.statusLabels.join(' / ') }}
            </div>
          </div>
          <div class="card-actions">
            <el-button type="text" size="mini" @click="removeSelectedTask(item.templateId)">移除</el-button>
          </div>
        </el-card>
      </div>
    </div>

    <flow-node ref="flowNodeRef" :templateId="this.form.templateId"></flow-node>


    <div class="formHeader mt20">
      <div class="formTitle">小贴士</div>
    </div>
    <el-divider></el-divider>
    <!-- 小贴士列表 -->
    <div class="post-list">
      <div class="post-item" v-for="(item,index) in postList">
        <div class="post-title">{{index+1}}.{{item.title}}</div>
        <div class="post-desc">{{item.desc}}</div>
        <div class="post-img">
          <img :src="item.img" style="width: 350px; height: 200px;"/>
        </div>

      </div>
    </div>

    <el-dialog title="选择任务模板" :visible.sync="taskSelectorVisible" width="960px" append-to-body>
      <div class="task-selector-dialog" v-loading="taskSelectorLoading">
        <div class="task-selector-toolbar">
          <el-input
            v-model="taskSelectorKeyword"
            size="small"
            clearable
            placeholder="输入模板名称或备注搜索">
            <i slot="prefix" class="el-icon-search"></i>
          </el-input>
        </div>
        <div class="task-selector-grid" v-if="filteredTaskTemplates.length">
          <el-card
            v-for="item in filteredTaskTemplates"
            :key="item.templateId"
            shadow="hover"
            class="task-selector-card"
            :class="{ 'is-selected': isTaskSelected(item.templateId) }"
            @click="toggleTaskSelection(item)"
          >
            <div class="card-title-row">
              <span class="card-title" :title="item.templateName">{{ item.templateName }}</span>
              <el-tag size="mini" :type="item.templateType === 'FUNCTION' ? 'success' : 'info'">
                {{ renderTaskTemplateType(item.templateType) }}
              </el-tag>
            </div>
            <div class="card-body">
              <div class="card-desc">{{ item.remark || '暂无描述' }}</div>
              <div class="card-meta">触发方式：{{ renderTriggerMode(item.triggerMode) }}</div>
              <div class="card-status" v-if="item.statusLabels && item.statusLabels.length">
                状态：{{ item.statusLabels.join(' / ') }}
              </div>
            </div>
            <div class="card-actions">
              <el-checkbox :value="isTaskSelected(item.templateId)">已选择</el-checkbox>
            </div>
          </el-card>
        </div>
        <el-empty v-else description="暂无可用任务模板" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="taskSelectorVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmTaskSelection">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listFlowTemplate,
    listFlowTemplateAll,
    getFlowTemplate,
    delFlowTemplate,
    addFlowTemplate,
    updateFlowTemplate
  } from "@/api/order/flowTemplate";

  import {
    listAllUser
  } from "@/api/system/user";

  import flowNode from "@/views/order/flowNode/index"

  import { listTaskTemplateAll } from "@/api/order/taskTemplate"

  const TASK_TEMPLATE_TYPE_LABELS = {
    API: 'API调用任务模板',
    FUNCTION: '功能组合模板'
  }

  const TRIGGER_MODE_LABELS = {
    AUTO: '自动触发',
    MANUAL: '人工触发'
  }

  export default {
    name: "FlowTemplateInfo",
    dicts: ['yes_no'],
    components: {
      flowNode
    },
    data() {
      return {
        imgPath: "../../../assets/images/numTask.png",
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 用户列表
        userList: [],
        // 小贴士列表
        postList: [
          // {
          //   title: "自定义记录任务",
          //   desc: "在流程进度上通过记录自定义字段完成情况体现其进度，适用于自定义等对产品分配处理的环节。",
          //   img: ""
          // },
          {
            title: "数量记录任务",
            desc: "在流程进度上通过记录特定产品完成数体现其进度，适用于图纸、编程、生产、检验等对产品分配处理的环节。",
            img: require("../../../assets/images/numTask.png")
          },
          {
            title: "子流程记录任务",
            desc: "在流程进度条上通过子流程体现其进度，适用于对产品分配处理的环节。",
            img: require("../../../assets/images/sunFlowTask.png")
          },
          {
            title: "金额记录任务",
            desc: "在流程进度条上通过记录金额体现其进度，适用于收款、入账、开票等对金额分配处理的环节。",
            img: require("../../../assets/images/amountTask.png")
          }
        ],
        // 任务模板选择
        selectedTaskTemplates: [],
        taskTemplateOptions: [],
        taskSelectorVisible: false,
        taskSelectorLoading: false,
        taskSelectorKeyword: '',
        taskSelectorSelectedIds: []
      };
    },
    created() {

    },
    watch: {
      '$route': {
        handler: function(to,form){
          this.onLoad();
        },
        immediate: true
      }
    },
    
    computed: {
      filteredTaskTemplates() {
        const keyword = (this.taskSelectorKeyword || '').trim().toLowerCase()
        if (!keyword) {
          return this.taskTemplateOptions
        }
        return this.taskTemplateOptions.filter(item => {
          const name = (item.templateName || '').toLowerCase()
          const remark = (item.remark || '').toLowerCase()
          return name.includes(keyword) || remark.includes(keyword)
        })
      }
    },

    methods: {

      onLoad(){
        this.getUserList();
        const templateId = this.$route.query.templateId;
        this.handleUpdate(templateId);
      },
      /**
       * 审核用户选择事件
       */
      auditUserChange(e){
        if(e){
          this.form.isAudit = 'Y'
          this.$refs.flowNodeRef.addAuditNode();
        }else{
          this.form.isAudit = 'N'
          this.$refs.flowNodeRef.delAuditNode();
        }
      },
      /**
       * 获取用户列表
       */
      getUserList() {
        listAllUser().then(res => {
          this.userList = res.data
        })
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          templateId: null,
          templateName: null,
          userId: null,
          isSeqExecute: null,
          isAutoPostpone: null,
          templateStatus: null,
          isAudit: null,
          auditUserId: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.templateId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加流程模板";
      },
      /** 修改按钮操作 */
      handleUpdate(templateId) {
        this.reset();
        if(!templateId){
          return;
        }
        getFlowTemplate(templateId).then(response => {
          this.form = response.data;
        });
      },
      openTaskSelector() {
        this.taskSelectorVisible = true
        this.taskSelectorKeyword = ''
        if (!this.taskTemplateOptions.length) {
          this.fetchTaskTemplates()
        } else {
          this.syncTaskSelectorSelection()
        }
      },
      fetchTaskTemplates() {
        this.taskSelectorLoading = true
        listTaskTemplateAll().then(res => {
          const list = res.data || res.rows || []
          this.taskTemplateOptions = list.map(item => this.normalizeTaskTemplate(item))
          this.syncTaskSelectorSelection()
          this.taskSelectorLoading = false
        }).catch(() => {
          this.taskSelectorLoading = false
        })
      },
      normalizeTaskTemplate(item = {}) {
        const clone = { ...item }
        clone.statusLabels = this.safeParseStatuses(clone.resultStatuses)
        return clone
      },
      safeParseStatuses(value) {
        if (!value) {
          return []
        }
        let parsed = value
        if (typeof value === 'string') {
          try {
            parsed = JSON.parse(value)
          } catch (e) {
            return []
          }
        }
        if (!Array.isArray(parsed)) {
          return []
        }
        return parsed.map(item => {
          if (!item) {
            return ''
          }
          if (typeof item === 'string') {
            return item
          }
          return item.statusLabel || item.label || item.name || item.statusValue || item.value || ''
        }).filter(text => !!text)
      },
      toggleTaskSelection(item) {
        const templateId = item.templateId
        if (!templateId) {
          return
        }
        const index = this.taskSelectorSelectedIds.indexOf(templateId)
        if (index > -1) {
          this.taskSelectorSelectedIds.splice(index, 1)
        } else {
          this.taskSelectorSelectedIds.push(templateId)
        }
      },
      isTaskSelected(templateId) {
        return this.taskSelectorSelectedIds.includes(templateId)
      },
      confirmTaskSelection() {
        this.selectedTaskTemplates = this.taskTemplateOptions.filter(item =>
          this.taskSelectorSelectedIds.includes(item.templateId)
        )
        this.taskSelectorVisible = false
      },
      removeSelectedTask(templateId) {
        this.selectedTaskTemplates = this.selectedTaskTemplates.filter(item => item.templateId !== templateId)
        this.taskSelectorSelectedIds = this.taskSelectorSelectedIds.filter(id => id !== templateId)
      },
      clearAllSelectedTasks() {
        this.selectedTaskTemplates = []
        this.taskSelectorSelectedIds = []
      },
      syncTaskSelectorSelection() {
        const ids = this.selectedTaskTemplates.map(item => item.templateId)
        this.taskSelectorSelectedIds = [...new Set(ids)]
      },
      renderTaskTemplateType(value) {
        return TASK_TEMPLATE_TYPE_LABELS[value] || value || '-'
      },
      renderTriggerMode(value) {
        return TRIGGER_MODE_LABELS[value] || value || '-'
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let flowNodeList = this.$refs.flowNodeRef.flowNodeList;
            let b = true;
            flowNodeList.forEach(item => {
              if(!item.nodeName){
                  b=false;
              }
            });
            if(b){
              this.$set(this.form,"flowNodeList",flowNodeList)
              if (this.form.templateId != null) {
                updateFlowTemplate(this.form).then(response => {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.backPrice();
                  this.getList();
                  
                });
              } else {
                addFlowTemplate(this.form).then(response => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.backPrice();
                  this.getList();
                });
              }
            }else{
              this.$modal.msgError("进度名称和任务名称不可为空");
            }
            
          }
        });

      },

      // 返回上个页面
      backPrice() {
        const obj = {
          path: "/flowTemplate"
        };
        this.$tab.closeOpenPage(obj);
      },

      /** 删除按钮操作 */
      handleDelete(row) {
        const templateIds = row.templateId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delFlowTemplate(templateIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/flowTemplate/export', {
          ...this.queryParams
        }, `flowTemplate_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
<style lang="scss">
  .flow-progress-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .selected-task-wrapper {
    margin-bottom: 20px;
    padding: 15px;
    background: #f9fafc;
    border: 1px solid #ebeef5;
    border-radius: 6px;
  }

  .selected-task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    font-weight: 600;
  }

  .selected-task-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 12px;
  }

  .selected-task-card {
    min-height: 140px;
  }

  .card-title-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;
  }

  .card-title {
    font-weight: 600;
    font-size: 14px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-right: 8px;
  }

  .card-body {
    font-size: 12px;
    color: #606266;
    line-height: 1.6;
  }

  .card-desc {
    margin-bottom: 6px;
  }

  .card-meta,
  .card-status {
    color: #909399;
    font-size: 12px;
  }

  .card-actions {
    text-align: right;
    margin-top: 10px;
  }

  .task-selector-dialog {
    min-height: 240px;
  }

  .task-selector-toolbar {
    margin-bottom: 15px;
  }

  .task-selector-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 12px;
  }

  .task-selector-card {
    cursor: pointer;
    border: 1px solid transparent;
    transition: border-color 0.2s, box-shadow 0.2s;
  }

  .task-selector-card.is-selected {
    border-color: #409eff;
    box-shadow: 0 0 6px rgba(64, 158, 255, 0.4);
  }

  .post-list {
    display: flex;


    .post-item {
      // padding: 10px;
      flex: 1;

      .post-title {
        padding-bottom: 5px;
      }

      .post-desc {
        color: #aaa1b0;
        font-size: 0.8em;
        padding-bottom: 5px;
      }

      .post-img{
        width: 100%;
      }
    }
  }
</style>
