<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单号" prop="inInventoryNo">
        <el-input v-model="queryParams.inInventoryNo" placeholder="请输入入库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="inInventoryStatus">
        <el-select v-model="queryParams.inInventoryStatus" placeholder="请选择">
          <el-option
            v-for="dict in dict.type.in_inventory_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['order:inInventory:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleAffirm" v-hasPermi="['order:inInventory:affirm']">确认入库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button  type="danger" plain icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete" v-hasPermi="['order:inInventory:remove']">删除</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-delete" size="mini" :disabled="single1" @click="handlePrint">打印物料入库单</el-button>
      </el-col>


      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="inventoryList" @selection-change="handleSelectionChange">
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
              <el-button type="text" v-if="item.prop == 'materielInfo'" @click="checkMaterielInfo(scope.row)">查看</el-button>
              <el-button type="text" v-if="item.prop == 'inInventoryNo'" @click="handleUpdate(scope.row)">{{scope.row[item.prop]}}</el-button>
              <el-button type="text" v-if="item.prop == 'orderNo' && scope.row.marketOrder!=null" @click="toOrder(scope.row)">{{scope.row.marketOrder.orderNo}}</el-button>
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

      <el-dialog :title="checkMateriel.title" :visible.sync="checkMateriel.open">
        <el-table :data="checkMaterielList">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column property="materielName" label="名称"></el-table-column>
          <el-table-column property="materielNo" label="编号"></el-table-column>
          <el-table-column property="inInventoryNum" label="数量"></el-table-column>
          <el-table-column property="remark" label="备注"></el-table-column>
        </el-table>
      </el-dialog>

    <ininventoryprint ref="inInventoryPrint"></ininventoryprint>

  </div>

</template>

<script>
import {
  listInventory,
  listInventoryAll,
  getInventory,
  delInventory,
  addInventory,
  updateInventory,
  affirm
} from "@/api/order/inInventory";

import {
  queryByInInventoryId
} from "@/api/order/inInventoryMateriel";

import ininventoryprint from "@/views/order/inInventory/print.vue";

export default {
  name: "InInventoryIndex",
  dicts: ["in_inventory_type","in_inventory_status"],
  components:{
    ininventoryprint
  },
  watch: {
    '$route': {
      handler: function(to,form){
        this.onLoad();
      },
      immediate: true
    }
  },
  data() {
    return {
      //弹出层
      checkMateriel:{
        //弹出层显示隐藏
        open: false,
        // 标题
        title: "",
      },
      //弹出层表格数据
      checkMaterielList: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      //打印
      single1: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 入库管理表格数据
      inventoryList: [],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        inInventoryNo: null,
        inInventoryType: null,
        inInventoryStatus: null,
        applicat: null,
        applyTime: null,
        createTo: null,
        updateTo: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      cacheCloumnList: [], //缓存字段列表
      //缓存名称
      cacheKey: "Inventory",
      columnList:[
      {
        label: "单号",
        prop: "inInventoryNo",
        visible: true,
        align: "center",
        type: "other"
      },
      {
        label: "入库类型",
        prop: "inInventoryType",
        visible: true,
        align: "center",
        type: "dict",
        dictType: "in_inventory_type"
      },
      {
        label: "产品信息",
        prop: "materielInfo",
        visible: true,
        align: "center",
        type: "other"

      },
      {
        label: "关联单号",
        prop: "orderNo",
        visible: true,
        align: "center",
        type: "other"

      },
      {
        label: "入库状态",
        prop: "inInventoryStatus",
        visible: true,
        align: "center",
        type: "dict",
        dictType: "in_inventory_status"
      },
      {
        label: "申请人",
        prop: "user.nickName",
        visible: true,
        align: "center",
        type: "obj",

      },
      {
        label: "申请时间",
        prop: "applyTime",
        visible: true,
        align: "center",
        type: "date",
        dateFormat: "{y}-{m}-{d}"
      },
      {
        label: "备注",
        prop: "remark",
        visible: true,
        align: "center",
      }
      ]
    };
  },
  created() {
    // this.onLoad();
  },

  methods: {

    handlePrint(){
      this.$refs.inInventoryPrint.OnClick(this.ids[0])
    },

    onLoad(){
      this.getList();
      this.refreshCloumn(this);
    },
    /**
     * 表格中查看产品信息
     */
     checkMaterielInfo(row){
      queryByInInventoryId(row.inInventoryId).then(res=>{
        this.checkMaterielList = res.data;
        this.checkMateriel.open = true;
        this.checkMateriel.title = "产品信息（"+row.inInventoryNo+")"
      })

    },
    /**
     * 确认出库
     */
    handleAffirm(){
      affirm(this.ids[0]).then(res=>{
        this.$modal.msgSuccess("操作成功");
        this.getList();
      })
    },
    // 跳转到采购单
    toOrder(row){
      this.$router.push({
        path: '/marketOrder/info/index/'+row.marketOrder.templateId,
        query: {
          orderId: row.marketOrder.orderId
        }
      });
    },
    /** 查询入库管理列表 */
    getList() {
      this.loading = true;
      listInventory(this.queryParams).then(response => {
        this.inventoryList = response.rows;
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
        inInventoryId: null,
        orderId: null,
        inInventoryNo: null,
        inInventoryType: null,
        inInventoryStatus: null,
        applicat: null,
        applyTime: null,
        remark: null,
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
      this.ids = selection.map(item => item.inInventoryId)
      let status = selection.map(item => item.inInventoryStatus);
      this.single = selection.length!==1 || status.includes("1");
      this.single1 = selection.length!==1;
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({
        path: '/inInventory/add/index'
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path: '/inInventory/add/index',
        query: {
          inInventoryId: row.inInventoryId
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.inInventoryId != null) {
            updateInventory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInventory(this.form).then(response => {
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
      const inInventoryIds = row.inInventoryId || this.ids;
      this.$modal.confirm('确认删除？').then(function() {
        return delInventory(inInventoryIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/inventory/export', {
        ...this.queryParams
      }, `inventory_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
