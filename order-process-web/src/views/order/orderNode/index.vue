<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="截止日期" prop="complateDate">
        <el-date-picker clearable v-model="queryParams.complateDate" type="date" value-format="yyyy-MM-dd"
          placeholder="请选择截止日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="负责人" prop="userId" v-hasPermi="['order:orderNode:allList']">
        <el-select v-model="queryParams.userId" filterable clearable>
          <el-option v-for="(item,index) in userList" :label="item.userName" :value="item.userId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择任务" prop="nodeId">
        <el-select v-model="queryParams.nodeId" filterable clearable>
          <el-option v-for="(item,index) in flowNodeList" :label="item.nodeName" :value="item.nodeId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择状态" prop="nodeStatus">
        <el-select v-model="queryParams.nodeStatus" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.node_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="orderNodeList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column type="selection" width="55" align="center" />
      <!-- 循环字段 -->
      <template v-for="(item,index) in cacheCloumnList">
        <el-table-column v-if="item.visible" :label="item.label" :prop="item.prop" :width="item.width||''"
          :align="item.align||'center'">
          <template slot-scope="scope">
            <!-- 字典 -->
            <dict-tag v-if="item.type == 'dict'" :options="dict.type[item.dictType]" :value="scope.row[item.prop]" />
            <dict-tag v-else-if="item.type == 'objDict'" :options="dict.type[item.dictType]"
              :value="getObjAttr(scope.row, item.prop)" />
            <!-- 日期 -->
            <span v-else-if="item.type == 'date'">{{ parseTime(scope.row[item.prop], item.dateFormat) }}</span>
            <!-- 图片 -->
            <image-preview v-else-if="item.type == 'img'" :src="scope.row[item.prop]" :width="50" :height="50" />
            <!-- 多层对象 -->
            <span v-else-if="item.type == 'obj'">{{ getObjAttr(scope.row, item.prop) }}</span>
            <!-- 其他 -->
            <template v-else-if="item.type == 'other'">
              <el-button v-if="item.prop == 'nodeId'" @click="handleNodeClick(scope.row)" type="text">{{scope.row.flowNodeVo.nodeName}}</el-button>
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改订单流程节点对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="订单模板ID" prop="orderTemplateId">
              <el-input v-model="form.orderTemplateId" placeholder="请输入订单模板ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="订单ID" prop="orderId">
              <el-input v-model="form.orderId" placeholder="请输入订单ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="模板ID" prop="templateId">
              <el-input v-model="form.templateId" placeholder="请输入模板ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="节点ID" prop="nodeId">
              <el-input v-model="form.nodeId" placeholder="请输入节点ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="部门ID" prop="deptId">
              <el-input v-model="form.deptId" placeholder="请输入部门ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="form.userId" placeholder="请输入用户ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="完成时间" prop="complateDate">
              <el-date-picker clearable v-model="form.complateDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择完成时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="节点状态(0=未开始,1=进行中,2=已完成,3=已超时)" prop="nodeStatus">
              <el-radio-group v-model="form.nodeStatus">
                <el-radio v-for="dict in dict.type.node_status" :key="dict.value"
                  :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="节点备注" prop="nodeRemark">
              <el-input v-model="form.nodeRemark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" placeholder="请输入排序" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 订单节点详情 -->
    <order-node-info :isEdit="true" ref="orderNodeInfoRef"></order-node-info>
  </div>
</template>

<script>
  import {
    listOrderNode,
    myTask,
    listOrderNodeAll,
    getOrderNode,
    delOrderNode,
    addOrderNode,
    updateOrderNode
  } from "@/api/order/orderNode";

  import {
    listFlowNodeAll
  } from "@/api/order/flowNode";

  import {
    listAllUser
  } from "@/api/system/user";

  import orderNodeInfo from "@/views/order/orderNode/info"

  export default {
    name: "OrderNode",
    dicts: ['node_status'],
    components: {
      orderNodeInfo
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 订单流程节点表格数据
        orderNodeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          orderTemplateId: null,
          orderId: null,
          templateId: null,
          nodeId: null,
          deptId: null,
          userId: null,
          complateDate: null,
          nodeStatus: null,
          nodeRemark: null,
          sort: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "OrderNode",
        columnList: [{
            label: "任务名称",
            prop: "nodeId",
            visible: true,
            align: "center",
            type: "other"
          },
          {
            label: "截止日期",
            prop: "complateDate",
            visible: true,
            align: "center",
            type: "date",
            dateFormat: "{y}-{m}-{d}"
          },
          {
            label: "客户名称",
            prop: "orderTemplateVo.customerVo.customerName",
            visible: true,
            align: "center",
            type: "obj"
          },
          {
            label: "订单编号",
            prop: "orderTemplateVo.orderNo",
            visible: true,
            align: "center",
            type: "obj"
          },

          {
            label: "产品名称及数量",
            prop: "orderTemplateVo.detailsDesc",
            visible: true,
            align: "center",
            type: "obj"
          },
          {
            label: "状态",
            prop: "nodeStatus",
            visible: true,
            align: "center",
            type: "dict",
            dictType: "node_status"
          },
        ],
        // 节点列表
        flowNodeList: [],
        // 用户列表
        userList: []
      };
    },
    created() {
      this.getList();
      this.refreshCloumn(this);
      this.getFlowNodeList();
      this.getUserList();
    },
    methods: {
      /**
       * 订单节点点击事件
       */
      handleNodeClick(orderNode){
        this.$refs.orderNodeInfoRef.hanldeOpen(orderNode.orderNodeId,orderNode.orderTemplateVo.orderType,orderNode.orderTemplateVo.orderTemplateId);
      },
      /**
       * 获取用户列表
       */
      getUserList(){
        listAllUser().then(res => {
          this.userList = res.data;
        })
      },
      /**
       * 获取节点列表
       */
      getFlowNodeList(){
        listFlowNodeAll().then(res => {
          this.flowNodeList = res.data;
        })
      },
      /** 查询订单流程节点列表 */
      getList() {
        this.loading = true;
        myTask(this.queryParams).then(response => {
          this.orderNodeList = response.rows;
          this.total = response.total;
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
          orderNodeId: null,
          orderTemplateId: null,
          orderId: null,
          templateId: null,
          nodeId: null,
          deptId: null,
          userId: null,
          complateDate: null,
          nodeStatus: null,
          nodeRemark: null,
          sort: null,
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
        this.ids = selection.map(item => item.orderNodeId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加订单流程节点";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const orderNodeId = row.orderNodeId || this.ids
        getOrderNode(orderNodeId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改订单流程节点";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.orderNodeId != null) {
              updateOrderNode(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addOrderNode(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const orderNodeIds = row.orderNodeId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delOrderNode(orderNodeIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/orderNode/export', {
          ...this.queryParams
        }, `orderNode_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
