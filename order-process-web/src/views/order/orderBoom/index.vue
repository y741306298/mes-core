<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品id" prop="materielId">
        <el-input
          v-model="queryParams.materielId"
          placeholder="请输入产品id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数量" prop="boomNum">
        <el-input
          v-model="queryParams.boomNum"
          placeholder="请输入数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总数量" prop="totalNum">
        <el-input
          v-model="queryParams.totalNum"
          placeholder="请输入总数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['order:orderBoom:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['order:orderBoom:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['order:orderBoom:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['order:orderBoom:export']">导出</el-button>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="orderBoomList" @selection-change="handleSelectionChange">
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
            <image-preview v-else-if="item.type == 'img'" :src="scope.row[item.prop]" :width="50" :height="50"/>
            <!-- 多层对象 -->
            <span v-else-if="item.type == 'obj'">{{ getObjAttr(scope.row, item.prop) }}</span>
            <!-- 其他 -->
            <template v-else-if="item.type == 'other'">
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:orderBoom:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['order:orderBoom:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改boom单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="订单ID" prop="orderId">
                  <el-input v-model="form.orderId" placeholder="请输入订单ID" />
                </el-form-item>
             </el-col>
           </el-row>
            <el-row>
              <el-col :span="24">
             </el-col>
           </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="产品id" prop="materielId">
                  <el-input v-model="form.materielId" placeholder="请输入产品id" />
                </el-form-item>
             </el-col>
           </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="数量" prop="boomNum">
                  <el-input v-model="form.boomNum" placeholder="请输入数量" />
                </el-form-item>
             </el-col>
           </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="总数量" prop="totalNum">
                  <el-input v-model="form.totalNum" placeholder="请输入总数量" />
                </el-form-item>
             </el-col>
           </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrderBoom, listOrderBoomAll, getOrderBoom, delOrderBoom, addOrderBoom, updateOrderBoom } from "@/api/order/orderBoom";

export default {
  name: "OrderBoom",
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
      // boom单表格数据
      orderBoomList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        orderType: null,
        materielId: null,
        boomNum: null,
        totalNum: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      cacheCloumnList: [], //缓存字段列表
      //缓存名称
      cacheKey: "OrderBoom",
      columnList:[
      {
        label: "主键",
        prop: "boomId",
        visible: true,
        align: "center",
      },
      {
        label: "订单ID",
        prop: "orderId",
        visible: true,
        align: "center",
      },
      {
        label: "订单类型(0=报价单,1=销售单,2=采购单)",
        prop: "orderType",
        visible: true,
        align: "center",
      },
      {
        label: "产品id",
        prop: "materielId",
        visible: true,
        align: "center",
      },
      {
        label: "数量",
        prop: "boomNum",
        visible: true,
        align: "center",
      },
      {
        label: "总数量",
        prop: "totalNum",
        visible: true,
        align: "center",
      },
      ]
    };
  },
  created() {
    this.getList();
    this.refreshCloumn(this);
  },
  methods: {
    /** 查询boom单列表 */
    getList() {
      this.loading = true;
      listOrderBoom(this.queryParams).then(response => {
        this.orderBoomList = response.rows;
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
        boomId: null,
        orderId: null,
        orderType: null,
        materielId: null,
        boomNum: null,
        totalNum: null,
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
      this.ids = selection.map(item => item.boomId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加boom单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const boomId = row.boomId || this.ids
      getOrderBoom(boomId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改boom单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.boomId != null) {
            updateOrderBoom(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrderBoom(this.form).then(response => {
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
      const boomIds = row.boomId || this.ids;
      this.$modal.confirm('确认删除？').then(function() {
        return delOrderBoom(boomIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/orderBoom/export', {
        ...this.queryParams
      }, `orderBoom_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
