<template>
  <div>
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="物料编号" prop="materielNo">
          <el-input v-model="queryParams.materielNo" placeholder="请输入物料编号" clearable
            @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="物料名称" prop="materielName">
          <el-input v-model="queryParams.materielName" placeholder="请输入物料名称" clearable
            @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>

        <el-table border ref="brtTable" v-loading="loading" :data="materielList"
          @selection-change="handleSelectionChange">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column type="selection" width="55" align="center" />
          <!-- 循环字段 -->
          <template v-for="(item,index) in cacheCloumnList">
            <el-table-column v-if="item.visible" :label="item.label" :prop="item.prop" :width="item.width||''"
              :align="item.align||'center'">
              <template slot-scope="scope">
                <!-- 字典 -->
                <dict-tag v-if="item.type == 'dict'" :options="dict.type[item.dictType]"
                  :value="scope.row[item.prop]" />
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
                </template>
                <span v-else>{{scope.row[item.prop]}}</span>
              </template>
            </el-table-column>
          </template>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-form>



      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="multiple" @click="submitSelect">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {
    listMateriel,
    listMaterielAll,
    getMateriel,
    delMateriel,
    addMateriel,
    updateMateriel
  } from "@/api/order/materiel";

  export default {
    name: "MaterielSelect",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 物料信息表格数据
        materielList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          materielNo: null,
          typeId: null,
          materielName: null,
          materielSpec: null,
          materielNum: null,
          warningNum: null,
          lockNum: null,
          sellPrice: null,
          purchasePrice: null,
          totalPrice: null,
          materielRemark: null,
        },
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "MaterielSelect",
        columnList: [{
            label: "物料编号",
            prop: "materielNo",
            visible: true,
            align: "center",
          },
          {
            label: "物料名称",
            prop: "materielName",
            visible: true,
            align: "center",
          },
          {
            label: "物料类型",
            prop: "materielTypeVo.typeName",
            visible: true,
            align: "center",
            type: "obj"
          },
          {
            label: "单位",
            prop: "materielTypeVo.typeName",
            visible: true,
            align: "center",
            type: "obj"
          },
          {
            label: "采购单价",
            prop: "purchasePrice",
            visible: true,
            align: "center",
          },
        ],
        // 是否显示弹出框
        open: false,
        // 弹出框标题
        title: null
      };
    },
    created() {
      this.refreshCloumn(this);
    },
    methods: {
      /**
       * 提交选中事件
       */
      submitSelect() {
        this.$parent.getSelectMateriel(this.ids)
        this.open = false;
        this.ids = []
      },
      /**
       * 打开弹出框
       */
      handleOpen() {
        this.open = true;
        this.title = "选择物料";
        this.getList();
      },
      /** 查询物料信息列表 */
      getList() {
        this.loading = true;
        listMateriel(this.queryParams).then(response => {
          this.materielList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
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
        this.ids = selection.map(item => item.materielId)
        this.multiple = !selection.length
      }
    }
  };
</script>
<style>
  .row-color-red {
    background-color: #e87977 !important;
  }
</style>
