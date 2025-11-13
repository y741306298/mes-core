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
          <el-select v-model="scope.row.nodeType" :disabled="scope.row.nodeType == '0'" class="drag-screenful-contnet"
            placeholder="请选择">
            <el-option v-for="dict in dict.type.node_type" :key="dict.value" :label="dict.label" :value="dict.value"
              :disabled="dict.value == '0'"></el-option>
            <el-option-group v-if="taskTemplateOptions.length" label="任务模板">
              <el-option
                v-for="item in taskTemplateOptions"
                :key="`task-template-${item.templateId}`"
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

  import { listTaskTemplateAll } from "@/api/order/taskTemplate";

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
      this.getTaskTemplateOptions();
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
      /** 查询任务模板 */
      getTaskTemplateOptions() {
        listTaskTemplateAll().then(response => {
          const list = response.data || response.rows || []
          this.taskTemplateOptions = list.map(item => this.normalizeTaskTemplate(item))
        })
      },
      normalizeTaskTemplate(item = {}) {
        return {
          templateId: item.templateId || item.id,
          templateName: item.templateName || '',
          templateType: item.templateType || '',
          triggerMode: item.triggerMode || ''
        }
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
      renderTaskTemplateLabel(item) {
        if (!item) {
          return ''
        }
        const typeMap = {
          API: 'API调用',
          FUNCTION: '功能组合'
        }
        const triggerMap = {
          AUTO: '自动触发',
          MANUAL: '人工触发'
        }
        const typeLabel = typeMap[item.templateType] || item.templateType || ''
        const triggerLabel = triggerMap[item.triggerMode] || item.triggerMode || ''
        const meta = [typeLabel, triggerLabel].filter(Boolean)
        if (!meta.length) {
          return item.templateName || ''
        }
        return `${item.templateName || ''}（${meta.join('/')}）`
      }
    }
  };
</script>
