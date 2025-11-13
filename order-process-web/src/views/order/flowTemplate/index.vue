<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['order:flowTemplate:add']">新增进度模板</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-collection" size="mini" @click="openTaskSelector">
          选择任务模板
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
          批量操作
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="copy" :disabled="!selectTemplateId" v-hasPermi="['order:flowTemplate:copy']">复制</el-dropdown-item>
            <el-dropdown-item command="edit"  :disabled="!selectTemplateId" v-hasPermi="['order:flowTemplate:edit']">编辑</el-dropdown-item>
            <el-dropdown-item command="delete" :disabled="!selectTemplateId" v-hasPermi="['order:flowTemplate:remove']">删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="1.5">
        <el-input v-model="queryParams.templateName" size="mini" placeholder="模板名称" clearable>
          <el-button @click="handleQuery" slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </el-col>
    </el-row>

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

    <div @click="handleTemplateClick(item)" v-for="(item,index) in flowTemplateList"
      :class="selectTemplateId == item.templateId?'marquee-border':''">
      <el-card class="box-card mb20" shadow="hover">
        <div slot="header" class="clearfix" style="display: flex;">
          <span class="template-name mr20">{{item.templateName}}</span>
          <el-checkbox @change="updateTemplate(item)" v-model="item.isSeqExecute">顺序执行</el-checkbox>
          <el-checkbox @change="updateTemplate(item)" v-model="item.isAutoPostpone">自动延期</el-checkbox>
          <el-checkbox @change="updateTemplate(item)" v-model="item.templateStatus">正常</el-checkbox>

          <div class="ml20 mr20">
            <span class="person">负责人：</span>
            <el-select @change="updateTemplate(item)" size="mini" v-model="item.userId"
              placeholder="请选择负责人" filterable>
              <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </div>

          <div class="ml20 mr20">
            <span class="person">审核人：</span>
            <el-select @change="auditUserChange(item)" size="mini" v-model="item.auditUserId" placeholder="请选择审批人"
              filterable clearable>
              <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </div>
        </div>

        <!-- <el-steps :active="item.flowNodeList.length" class="mt10">
          <el-step :title="node.nodeName" v-for="(node,nodeIndex) in item.flowNodeList" icon="el-icon-help">
            <el-button type="text" slot="title" :disabled="node.nodeType == '0'" @click="handleNodeClick(node)">{{node.nodeName}}</el-button>
          </el-step>
        </el-steps> -->

        <div style="display: flex;overflow-x: scroll;padding-bottom: 20px;">
          <div v-for="(node,nodeIndex) in item.flowNodeList" :key="nodeIndex">
            <!-- 首个 -->
            <div v-if="nodeIndex==0">
              <div class="arrow-first">
                <div class="first-center-active">
                  <el-button class="flowBtn" type="text" slot="title" v-if="node.nodeType == '0'">{{node.nodeName}}</el-button>
                  <el-button class="flowBtn" type="text" slot="title" v-else @click="handleNodeClick(node)">{{node.nodeName}}</el-button>
                </div>
                <div class="first-right-active"></div>
              </div>
            </div>

            <!-- 中间 -->
            <div v-if="nodeIndex>0&&nodeIndex!=item.flowNodeList.length-1">
              <div class="arrow">
                <div class="arrow-left-active"></div>
                <div class="arrow-center-active">
                  <el-button class="flowBtn" type="text" slot="title" :disabled="node.nodeType == '0'" @click="handleNodeClick(node)">{{node.nodeName}}</el-button>
                </div>
                <div class="arrow-right-active"></div>
              </div>
            </div>

            <!-- 最后 -->
            <div v-if="nodeIndex==item.flowNodeList.length-1">
              <div class="arrow-last">
                <div class="last-left-active"></div>
                <div class="last-center-active">
                  <el-button class="flowBtn" type="text" slot="title" :disabled="node.nodeType == '0'" @click="handleNodeClick(node)">{{node.nodeName}}</el-button>
                </div>
                <div class="last-right-active"></div>
              </div>
            </div>

          </div>
        </div>


      </el-card>
    </div>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

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

    <!-- 节点设置 -->
    <flow-setting ref="flowSettingRef"></flow-setting>

  </div>
</template>

<script>
  import {
    listFlowTemplate,
    listFlowTemplateAll,
    getFlowTemplate,
    delFlowTemplate,
    addFlowTemplate,
    updateFlowTemplate,
    copyFlowTemplate
  } from "@/api/order/flowTemplate";

  import {
    listAllUser
  } from "@/api/system/user";

  import {
    delFlowNode
  } from "@/api/order/flowNode";

  import { listTaskTemplateAll } from "@/api/order/taskTemplate";

  import flowSetting from "@/views/order/flowNode/setting"

  const TASK_TEMPLATE_TYPE_LABELS = {
    API: 'API调用任务模板',
    FUNCTION: '功能组合模板'
  }

  const TRIGGER_MODE_LABELS = {
    AUTO: '自动触发',
    MANUAL: '人工触发'
  }

  export default {
    name: "FlowTemplate",
    dicts: ['yes_no'],
    components: {
      flowSetting
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 流程模板表格数据
        flowTemplateList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          templateName: null,
          userId: null,
          isSeqExecute: null,
          isAutoPostpone: null,
          templateStatus: null,
          isAudit: null,
          auditUserId: null,
        },
        // 用户列表
        userList: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 选中的模板ID
        selectTemplateId: null,
        // 任务模板选择器
        selectedTaskTemplates: [],
        taskTemplateOptions: [],
        taskSelectorVisible: false,
        taskSelectorLoading: false,
        taskSelectorKeyword: '',
        taskSelectorSelectedIds: []
      };
    },
    watch: {
      '$route'(to,from){
        this.getList();
      }
    },
    computed: {
      filteredTaskTemplates() {
        const keyword = (this.taskSelectorKeyword || '').trim().toLowerCase();
        if (!keyword) {
          return this.taskTemplateOptions;
        }
        return this.taskTemplateOptions.filter(item => {
          const name = (item.templateName || '').toLowerCase();
          const remark = (item.remark || '').toLowerCase();
          return name.includes(keyword) || remark.includes(keyword);
        });
      }
    },
    created() {
      this.getList();
      this.getUserList();
    },
    methods: {
      openTaskSelector() {
        this.taskSelectorVisible = true;
        this.taskSelectorKeyword = '';
        if (!this.taskTemplateOptions.length) {
          this.fetchTaskTemplates();
        } else {
          this.syncTaskSelectorSelection();
        }
      },
      fetchTaskTemplates() {
        this.taskSelectorLoading = true;
        listTaskTemplateAll({ templateType: 'FUNCTION' }).then(res => {
          const list = res.data || res.rows || [];
          this.taskTemplateOptions = list.map(item => this.normalizeTaskTemplate(item));
          this.syncTaskSelectorSelection();
          this.taskSelectorLoading = false;
        }).catch(() => {
          this.taskSelectorLoading = false;
        });
      },
      normalizeTaskTemplate(item = {}) {
        const clone = { ...item };
        clone.statusLabels = this.safeParseStatuses(clone.resultStatuses);
        return clone;
      },
      safeParseStatuses(value) {
        if (!value) {
          return [];
        }
        let parsed = value;
        if (typeof value === 'string') {
          try {
            parsed = JSON.parse(value);
          } catch (e) {
            return [];
          }
        }
        if (!Array.isArray(parsed)) {
          return [];
        }
        return parsed.map(item => {
          if (!item) {
            return '';
          }
          if (typeof item === 'string') {
            return item;
          }
          return item.statusLabel || item.label || item.name || item.statusValue || item.value || '';
        }).filter(text => !!text);
      },
      toggleTaskSelection(item) {
        const templateId = item.templateId;
        if (!templateId) {
          return;
        }
        const index = this.taskSelectorSelectedIds.indexOf(templateId);
        if (index > -1) {
          this.taskSelectorSelectedIds.splice(index, 1);
        } else {
          this.taskSelectorSelectedIds.push(templateId);
        }
      },
      isTaskSelected(templateId) {
        return this.taskSelectorSelectedIds.includes(templateId);
      },
      confirmTaskSelection() {
        this.selectedTaskTemplates = this.taskTemplateOptions.filter(item =>
          this.taskSelectorSelectedIds.includes(item.templateId)
        );
        this.taskSelectorVisible = false;
      },
      removeSelectedTask(templateId) {
        this.selectedTaskTemplates = this.selectedTaskTemplates.filter(item => item.templateId !== templateId);
        this.taskSelectorSelectedIds = this.taskSelectorSelectedIds.filter(id => id !== templateId);
      },
      clearAllSelectedTasks() {
        this.selectedTaskTemplates = [];
        this.taskSelectorSelectedIds = [];
      },
      syncTaskSelectorSelection() {
        const ids = this.selectedTaskTemplates.map(item => item.templateId);
        this.taskSelectorSelectedIds = [...new Set(ids)];
      },
      renderTaskTemplateType(value) {
        return TASK_TEMPLATE_TYPE_LABELS[value] || value || '-';
      },
      renderTriggerMode(value) {
        return TRIGGER_MODE_LABELS[value] || value || '-';
      },
      /**
       * 复制模板
       */
      handleCopy(){
        copyFlowTemplate({templateId:this.selectTemplateId}).then(res => {
          this.$notify({
            title: '成功',
            message: '复制成功',
            type: 'success'
          });
          this.getList();
        })
      },
      /**
       * 监听审核用户选择
       */
      auditUserChange(item) {
        const index = item.flowNodeList.findIndex(item => item.nodeType == '0');
        if (item.auditUserId) {
          if (index < 0) {
            const obj = {
              nodeId: null,
              nodeName: "审批",
              nodeType: "0",
              nodeStatus: "Y",
            };
            item.flowNodeList.unshift(obj)
            item.isAudit = "Y";
          }
        } else {
          const flowNode = item.flowNodeList[index];
          if (flowNode.nodeId) {
            delFlowNode(flowNode.nodeId)
          }
          item.flowNodeList.splice(index, 1)
          item.isAudit = "N";
        }
        this.updateTemplate(item);
      },
      /**
       * 修改模板
       */
      updateTemplate(template) {
        updateFlowTemplate(template).then(response => {
          this.$notify({
            title: '成功',
            message: '修改成功',
            type: 'success'
          });
          this.getList();
        });
      },
      //是否选中
      isChecked(template, attr) {
        this.$set(template, attr + 'Flag', template[attr] == 'Y' ? true : false)
      },
      /**
       * 模板点击事件
       */
      handleTemplateClick(template) {
        this.selectTemplateId = template.templateId;
      },
      /**
       * 节点点击事件
       */
      handleNodeClick(node) {
        // alert(node)
        this.$refs.flowSettingRef.handleOpen(node.nodeId)
      },
      /**
       * 获取用户列表
       */
      getUserList() {
        listAllUser().then(res => {
          this.userList = res.data
        })
      },
      /**
       * 批量操作按钮点击事件
       */
      handleDropdownClick(command) {
        switch (command) {
          case 'copy':
            this.handleCopy();
            break;
          case 'edit':
            this.handleUpdate();
            break;
          case 'delete':
            this.handleDelete();
            break;
        }
      },
      /** 查询流程模板列表 */
      getList() {
        this.loading = true;
        listFlowTemplate(this.queryParams).then(response => {
          this.flowTemplateList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.$router.push({
          path: '/flowTemplate/info/index',
        });
      },
      /** 修改按钮操作 */
      handleUpdate() {
        this.$router.push({
          path: '/flowTemplate/info/index',
          query: {
            templateId: this.selectTemplateId
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const templateIds = this.selectTemplateId;
        this.$modal.confirm('确认删除？').then(function() {
          return delFlowTemplate(templateIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
    }
  };
</script>
<style>
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

  .template-name {
    font-weight: 700;
  }

  .marquee-border {
    height: 163px;
    position: relative;
    color: #fff;
    border: 3px solid white;
    margin-bottom: 20px;
  }

  .marquee-border::before {
    content: "";
    position: absolute;
    top: -6px;
    left: -6px;
    right: -6px;
    bottom: -6px;
    border: 3px solid;
    border-image-slice: 1;
    animation: div5Ani 5s;
    animation-iteration-count: infinite;
    animation-timing-function: linear;
    border-image-source: linear-gradient(to left, #759aeb, rgba(255, 255, 255, 0));
    clip-path: inset(0 0 98% 0);
  }


  @keyframes div5Ani {

    0%,
    100% {
      border-image-source: linear-gradient(to bottom, #759aeb, rgba(255, 255, 255, 0));
      clip-path: inset(0 98% 0 0);
    }

    25% {
      border-image-source: linear-gradient(to left, #759aeb, rgba(255, 255, 255, 0));
      clip-path: inset(0 0 98% 0);
    }

    50% {
      border-image-source: linear-gradient(to top, #759aeb, rgba(255, 255, 255, 0));
      clip-path: inset(0 0 0 97%);
    }

    75% {
      border-image-source: linear-gradient(to right, #759aeb, rgba(255, 255, 255, 0));
      clip-path: inset(98% 0 0 0);
    }

  }


  /* 第一个 */
.arrow-first {
  display: flex;
}

.first-center {
  width: 150px;
  background-color: #cbcdd4;
  text-align: center;
}

.first-center-active {
  width: 150px;
  background-color: #70eaa9;
  text-align: center;
}

.first-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}
.first-right-active {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #70eaa9;
}

/* 中间 */
.arrow {
  display: flex;
  margin-left: -25px;
}

.arrow-left {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.arrow-left-active {
  border-width: 19px;
  border-style: solid;
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}

.arrow-center {
  width: 150px;
  background-color: #cbcdd4;
  text-align: center;
}

.arrow-center-active {
  width: 150px;
  background-color: #70eaa9;
  text-align: center;
}

.arrow-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #cbcdd4;
}

.arrow-right-active {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent #70eaa9;
}

/* 最后 */
.arrow-last {
  display: flex;
  margin-left: -25px;
}

.last-left {
  border-width: 19px;
  border-style: solid;
  border-color: #cbcdd4 #cbcdd4 #cbcdd4 transparent;
}

.last-left-active {
  border-width: 19px;
  border-style: solid;
  border-color: #70eaa9 #70eaa9 #70eaa9 transparent;
}

.last-center {
  width: 150px;
  background-color: #cbcdd4;
  text-align: center;
}

.last-center-active {
  width: 150px;
  background-color: #70eaa9;
  text-align: center;
}

.last-right {
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent transparent;
}

.last-right-active {
  width: 50px;
  border-width: 19px;
  border-style: solid;
  border-color: transparent transparent transparent transparent;
}

.flowBtn{
  color: #ffffff;
  /* font-weight: bold; */
}

.person{
  font-size: 15px;
  position: relative;
  top: -2px;
}
</style>
