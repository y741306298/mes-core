<template>
  <div>

    <el-table border ref="brtTable" v-loading="loading" :data="flowNodeList" @selection-change="handleSelectionChange">
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button v-if="scope.$index == (flowNodeList.length-1)" type="text" icon="el-icon-plus"
            @click="handleAdd"></el-button>
          <el-button v-else-if="scope.row.nodeType != '0'" type="text" icon="el-icon-delete"
            @click="handleDelete(scope.row,scope.$index)"></el-button>
        </template>
      </el-table-column>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="进度名称" align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.nodeName" placeholder="请输入" />
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center">
        <template slot-scope="scope">
          <el-select
            v-model="scope.row.nodeType"
            :disabled="scope.row.nodeType == '0'"
            class="drag-screenful-contnet"
            placeholder="请选择"
            filterable
            :loading="taskTemplateLoading"
          >
            <el-option-group label="系统任务" v-if="dict.type.node_type && dict.type.node_type.length">
              <el-option
                v-for="dict in dict.type.node_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
                :disabled="dict.value == '0'"
              ></el-option>
            </el-option-group>
            <el-option-group label="任务模板" v-if="taskTemplateOptions.length">
              <el-option
                v-for="item in taskTemplateOptions"
                :key="item.templateId"
                :label="renderTaskTemplateLabel(item)"
                :value="item.templateId"
              ></el-option>
            </el-option-group>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="启用" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.nodeStatus" active-color="#13ce66" :active-value="'Y'" inactive-color="#ff4949"
            :inactive-value="'N'" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {
    listFlowNode,
    listFlowNodeAll,
    getFlowNode,
    delFlowNode,
    addFlowNode,
    updateFlowNode
  } from "@/api/order/flowNode";

  import { listTaskTemplateAll } from "@/api/order/taskTemplate"

  const TASK_TEMPLATE_TYPE_LABELS = {
    API: 'API调用任务模板',
    FUNCTION: '功能组合模板'
  }

  export default {
    name: "FlowNode",
    dicts: ['node_type', 'yes_no'],
    props: ['templateId'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 模板节点表格数据
        flowNodeList: [],
        // 任务模板
        taskTemplateOptions: [],
        taskTemplateLoading: false,
        // 查询参数
        queryParams: {
          templateId: null,
          nodeName: null,
          nodeType: null,
          nodeStatus: null,
        },
      };
    },
    watch: {
      templateId: function() {
        this.getList();
      }
    },
    created() {
      this.getList();
      this.fetchTaskTemplates();
    },
    methods: {
      /**
       * 添加审批节点
       */
      addAuditNode() {
        const index = this.flowNodeList.findIndex(item => item.nodeType == '0');
        if (index < 0) {
          const item = {
            nodeId: null,
            nodeName: "审批",
            nodeType: "0",
            nodeStatus: "Y",
          };
          this.flowNodeList.unshift(item)
        }

      },
      fetchTaskTemplates() {
        this.taskTemplateLoading = true
        listTaskTemplateAll().then(res => {
          const list = res.data || res.rows || []
          this.taskTemplateOptions = list
          this.taskTemplateLoading = false
        }).catch(() => {
          this.taskTemplateLoading = false
        })
      },
      /**
       * 删除审批节点
       */
      delAuditNode() {
        const index = this.flowNodeList.findIndex(item => item.nodeType == '0');
        const flowNode = this.flowNodeList[index];
        if (flowNode.nodeId) {
          delFlowNode(flowNode.nodeId)
        } else {
          this.flowNodeList.splice(index, 1)
        }
      },
      /** 查询模板节点列表 */
      getList() {
        this.loading = true;

        if (!this.templateId) {
          this.flowNodeList = [];
          this.handleAdd();
          this.loading = false;
          return;
        }

        this.queryParams.templateId = this.templateId;
        listFlowNodeAll(this.queryParams).then(response => {
          this.flowNodeList = response.data;
          if (this.flowNodeList == null || this.flowNodeList.length <= 0) {
            this.handleAdd();
          }
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          nodeId: null,
          nodeName: null,
          nodeType: null,
          nodeStatus: null,
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
        this.ids = selection.map(item => item.nodeId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        const item = {
          nodeId: null,
          nodeName: null,
          nodeType: null,
          nodeStatus: "Y",
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.flowNodeList.push(item);
      },
      /** 删除按钮操作 */
      handleDelete(row, index) {
        const nodeId = row.nodeId;
        if (nodeId) {
          this.$modal.confirm('确认删除？').then(function() {
            return delFlowNode(nodeId);
          }).then(() => {
            this.getList();
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success'
            });
          }).catch(() => {});
        } else {
          this.flowNodeList.splice(index, 1)
        }

      },
      renderTaskTemplateLabel(item = {}) {
        const name = item.templateName || '未命名模板'
        const typeLabel = TASK_TEMPLATE_TYPE_LABELS[item.templateType] || item.templateType || '模板'
        return `${name}（${typeLabel}）`
      },
    }
  };
</script>
