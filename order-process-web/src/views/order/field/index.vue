<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="formHeader">
          <div class="formTitle">类型</div>
        </div>
        <el-divider></el-divider>
        <div v-for="dict in dict.type.business_type" class="mb5 p10 business-type-hover"
          :class="queryParams.businessType == dict.value ? 'business-type' : ''" @click="businessTypeClick(dict)">
          {{dict.label}}
        </div>
      </el-col>
      <el-col :span="20">
        <div class="formHeader">
          <div class="formTitle">字段</div>
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
            v-hasPermi="['order:field:add']">新增</el-button>
        </div>
        <el-divider></el-divider>
        <el-row :gutter="10" class="mb8">
          <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
            @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table border ref="brtTable" v-loading="loading" :data="fieldList" @selection-change="handleSelectionChange">
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-checkbox @change="handleUpdateStatus(scope.row)" v-model="scope.row.fieldStatus">启用</el-checkbox>
              <el-checkbox @change="handleUpdateStatus(scope.row)" v-model="scope.row.isMust">必输</el-checkbox>
            </template>
          </el-table-column>
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
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['order:field:edit']"></el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['order:field:remove']"></el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改自定义字段对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="字段名称" prop="fieldName">
              <el-input v-model="form.fieldName" placeholder="请输入字段名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="字段类型" prop="fieldType">
              <el-select v-model="form.fieldType" placeholder="请选择字段类型" filterable class="drag-screenful-contnet">
                <el-option v-for="dict in dict.type.filed_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-checkbox v-model="form.fieldStatus">启用</el-checkbox>
              <el-checkbox v-model="form.isMust">是否必输</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="后选项" prop="dictType">
              <el-select v-model="form.dictType" placeholder="请选择后选项" filterable class="drag-screenful-contnet">
                <el-option v-for="dict in dictOptions" :key="dict.dictType" :label="dict.dictName"
                  :value="dict.dictType">
                  <span style="float: left">{{ dict.dictName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.dictType }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="fieldRemark">
              <el-input v-model="form.fieldRemark" type="textarea" placeholder="请输入内容" />
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
  import {
    listField,
    listFieldAll,
    getField,
    delField,
    addField,
    updateField
  } from "@/api/order/field";

  import {
    optionselect as getDictOptionselect
  } from "@/api/system/dict/type";

  export default {
    name: "Field",
    dicts: ['filed_type', 'business_type'],
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
        // 自定义字段表格数据
        fieldList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          fieldName: null,
          fieldType: null,
          businessType: null,
          dictType: null,
          fieldRemark: null,
          fieldStatus: null,
          isMust: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          fieldName: [{
            required: true,
            message: "字段名称不能为空",
            trigger: "blur"
          }],
        },
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "Field",
        columnList: [{
            label: "字段名称",
            prop: "fieldName",
            visible: true,
            align: "center",
          },
          {
            label: "字段类型",
            prop: "fieldType",
            visible: true,
            align: "center",
            type: "dict",
            dictType: "filed_type"
          },
          {
            label: "备注",
            prop: "fieldRemark",
            visible: true,
            align: "center",
          }
        ],
        dictOptions: [] // 字典列表
      };
    },
    created() {
      this.getList();
      this.refreshCloumn(this);
    },
    methods: {
      /**
       * 修改状态信息
       */
      handleUpdateStatus(row) {
        updateField(row).then(response => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.getList();
        });
      },
      /**
       * 业务类型点击事件
       */
      businessTypeClick(dict) {
        this.queryParams.businessType = dict.value;
        this.getList();
      },
      /** 查询自定义字段列表 */
      getList() {
        this.loading = true;
        listField(this.queryParams).then(response => {
          this.fieldList = response.rows;
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
          fieldId: null,
          fieldName: null,
          fieldType: null,
          businessType: this.queryParams.businessType,
          dictType: null,
          fieldRemark: null,
          fieldStatus: true,
          isMust: false,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");

        /** 查询字典下拉列表 */
        getDictOptionselect().then(response => {
          this.dictOptions = response.data;
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
        this.ids = selection.map(item => item.fieldId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        if (!this.queryParams.businessType) {
          this.$notify.error({
            title: '错误',
            message: '请选择类型'
          });
          return;
        }
        this.reset();
        this.open = true;
        this.title = "添加自定义字段";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const fieldId = row.fieldId || this.ids
        getField(fieldId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改自定义字段";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.fieldId != null) {
              updateField(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addField(this.form).then(response => {
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
        const fieldIds = row.fieldId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delField(fieldIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/field/export', {
          ...this.queryParams
        }, `field_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
<style>
  .business-type-hover {
    transition: all 0.5s;
  }

  .business-type-hover:hover {
    background-color: #fef9f6;
    color: #ea904e;
  }

  .business-type {
    background-color: #fef9f6;
    color: #ea904e;
  }
</style>