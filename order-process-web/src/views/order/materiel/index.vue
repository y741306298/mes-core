<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编号" prop="materielNo">
        <el-input v-model="queryParams.materielNo" placeholder="请输入物料编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materielName">
        <el-input v-model="queryParams.materielName" placeholder="请输入物料名称" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['order:materiel:add']">新增物料</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['order:materiel:edit']">编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
          操作
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="warning" :disabled="multiple"
              v-hasPermi="['order:materiel:warning']">设置库存预警</el-dropdown-item>
            <el-dropdown-item command="download">下载模板</el-dropdown-item>
            <el-dropdown-item command="import" v-hasPermi="['order:materiel:import']">导入</el-dropdown-item>
            <el-dropdown-item command="export" v-hasPermi="['order:materiel:export']">导出</el-dropdown-item>
            <el-dropdown-item command="delete" v-hasPermi="['order:materiel:remove']">删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border show-summary :summary-method="getSummaries" ref="brtTable" v-loading="loading" :data="materielList"
      @selection-change="handleSelectionChange" :row-class-name="tableRowClassName">
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
              <el-button type="text" size="mini" v-if="item.prop == 'view'" @click="checkRecord(scope.row.materielId)">查看</el-button>
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['order:materiel:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['order:materiel:remove']">删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url"
        :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess"
        :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-drawer :visible.sync="open" size="50%" append-to-body>
        <materiel-info ref="materielInfo" @getList="getList" @close="close"></materiel-info>
    </el-drawer>

  </div>
</template>

<script>
  import {
    listMateriel,
    listMaterielAll,
    getMateriel,
    delMateriel,
    addMateriel,
    updateMateriel,
    warningMateriel
  } from "@/api/order/materiel";

  import materielInfo from "@/views/order/materiel/info";

  import {
    getToken
  } from "@/utils/auth";

  export default {
    name: "Materiel",
    components: {
      materielInfo
    },
    dicts: ["materiel_type"],
    data() {
      return {
        open:false,
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
        // 导入参数
        upload: {
          // 是否显示弹出层
          open: false,
          // 弹出层标题
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 设置上传的请求头部
          headers: {
            Authorization: "Bearer " + getToken()
          },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/order/materiel/importData"
        },
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "Materiel",
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
            prop: "typeId",
            visible: true,
            align: "center",
            type: "dict",
            dictType: "materiel_type"
          },
          {
            label: "型号规格",
            prop: "materielSpec",
            visible: true,
            align: "center",
          },
          {
            label: "单位",
            prop: "materielUnit",
            visible: true,
            align: "center",
          },
          {
            label: "库存数量",
            prop: "materielNum",
            visible: true,
            align: "center",
          },
          {
            label: "锁定数量",
            prop: "lockNum",
            visible: true,
            align: "center",
          },
          {
            label: "库存预警",
            prop: "warningNum",
            visible: true,
            align: "center",
          },
          {
            label: "库存详情",
            prop: "view",
            visible: true,
            align: "center",
            type: "other"
          },
          {
            label: "采购单价",
            prop: "purchasePrice",
            visible: true,
            align: "center",
          },
          {
            label: "总价",
            prop: "totalPrice",
            visible: true,
            align: "center",
          },
          {
            label: "库位",
            prop: "location",
            visible: true,
            align: "center",
          },
          {
            label: "供应商",
            prop: "supplier",
            visible: true,
            align: "center"
          },
          {
            label: "备注",
            prop: "materielRemark",
            visible: true,
            align: "center",
          },
        ]
      };
    },

    watch: {
      '$route': {
        handler: function(to,form){
          this.onLoad();
        },
        immediate: true
      }
    },

    created() {

    },
    methods: {

      onLoad(){
        this.getList();
        this.refreshCloumn(this);
        const type = this.$route.query.type;
        if(type!=undefined && type!=null && type == "add"){
          this.open = true;
        }
      },

      checkRecord(materielId){
        this.$router.push({
          path: '/materiel/checkRecord/index',
          query: {
            materielId: materielId
          }
        });
      },
      /**
       * 批量操作按钮点击事件
       */
      handleDropdownClick(command) {
        switch (command) {
          case 'warning':
            this.hanldeWarning();
            break;
          case 'download':
            this.importTemplate();
            break;
          case 'import':
            this.handleImport();
            break;
          case 'export':
            this.handleExport();
            break;
          case 'delete':
            this.handleDelete();
            break;
        }
      },
      /**
       * 设置库存预警
       */
      hanldeWarning() {
        this.$prompt('产品预警数量', '设置库存预警', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          // inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
          // inputErrorMessage: '邮箱格式不正确'
        }).then(({
          value
        }) => {
          const data = {
            materielIds: this.ids.join(','),
            warningNum: value
          };
          warningMateriel(data).then(res => {
            this.$notify({
              title: '成功',
              message: '设置成功',
              type: 'success'
            });
            this.getList();
          })
        }).catch(() => {});
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert(response.msg);
        this.getList();
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        this.download('order/materiel/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
      },
      // 合计
      getSummaries(param) {
        const {
          columns,
          data
        } = param;
        const sums = [];
        const sumColumnLabels = ['库存数量', '总价']; //需要合计的列名称
        columns.forEach((column, index) => {
          console.log()
          if (index === 0) {
            sums[index] = '合计';
            return;
          } else if (sumColumnLabels.includes(column.label)) {
            const values = data.map(item => Number(item[column.property]));
            if (!values.every(value => isNaN(value))) {
              sums[index] = values.reduce((prev, curr) => {
                const value = Number(curr);
                if (!isNaN(value)) {
                  return prev + curr;
                } else {
                  return prev;
                }
              }, 0);
              sums[index] += '';
            } else {
              sums[index] = '';
            }
          }

        });
        return sums;
      },
      //标注预警数据
      tableRowClassName({
        row,
        rowIndex
      }) {
        if (parseInt(row.warningNum) > parseInt(row.materielNum)) {
          return 'row-color-red'
        }
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
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.open = true;
        this.$nextTick(()=>{
          this.$refs.materielInfo.reset();
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {

        this.open = true;
        this.$nextTick(()=>{
          this.$refs.materielInfo.reset();
          this.$refs.materielInfo.handleUpdate(this.ids[0]);
          this.$refs.materielInfo.getMaterielTypeList();
        })

      },
      /** 删除按钮操作 */
      handleDelete() {
        const materielIds = this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delMateriel(materielIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/materiel/export', {
          ...this.queryParams,
          ids: this.ids
        }, `materiel_${new Date().getTime()}.xlsx`)
      },
      close(){
        this.open = false;
      }
    }
  };
</script>
<style>
  .row-color-red {
    background-color: #e87977 !important;
  }
</style>
