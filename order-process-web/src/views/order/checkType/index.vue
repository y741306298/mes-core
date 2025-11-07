<template>
  <div >
    <!-- 添加或修改账单类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="" prop="typeName">
              <el-input v-model="form.typeName" placeholder="请输入类型名称" />
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="submitForm">新 增</el-button>
          </el-col>
        </el-row>
        <el-table border ref="brtTable" v-loading="loading" :data="checkTypeList">
          <el-table-column label="账单类型" align="center">
            <template slot-scope="scope">
              <el-input v-if="scope.row.isEdit" v-model="scope.row.typeName" placeholder="请输入账单类型" />
              <span v-else>{{scope.row.typeName}}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <template v-if="scope.row.isEdit">
                <el-button size="mini" type="text" @click="handleEditSubmit(scope.row)">保存</el-button>
                <el-button size="mini" type="text" @click="handleCancel()">取消</el-button>
              </template>
              <template v-else>
                <el-button size="mini" type="text" @click="handleUpdate(scope.row)"
                  v-hasPermi="['order:checkType:edit']">修改</el-button>
                <el-button size="mini" type="text" @click="handleDelete(scope.row)"
                  v-hasPermi="['order:checkType:remove']">删除</el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listCheckType,
    listCheckTypeAll,
    getCheckType,
    delCheckType,
    addCheckType,
    updateCheckType
  } from "@/api/order/checkType";

  export default {
    name: "CheckType",
    props: ['checkType'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 账单类型表格数据
        checkTypeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
      };
    },
    methods: {
      /**
       * 修改保存
       */
      handleEditSubmit(row) {
        updateCheckType(row).then(response => {
          this.$modal.msgSuccess("修改成功");
          this.getList();
          this.$emit('getList')
        });
      },
      /**
       * 取消编辑
       */
      handleCancel() {
        this.getList();
      },
      /**
       * 打开弹框
       */
      handleOpen() {
        this.getList();
        this.reset();
        this.open = true;
        this.title = "账单类型"
      },
      /** 查询账单类型列表 */
      getList() {
        this.loading = true;
        const queryParams = {
          type: this.checkType
        }
        listCheckTypeAll(queryParams).then(response => {
          this.checkTypeList = response.data;
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
          typeId: null,
          typeName: null,
          type: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.$set(row, 'isEdit', true)
      },
      /** 提交按钮 */
      submitForm() {
        this.form.type = this.checkType;
        addCheckType(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.getList();
          this.$emit('getList')
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const typeIds = row.typeId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delCheckType(typeIds);
        }).then(() => {
          this.getList();
          this.$emit('getList')
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
    }
  };
</script>
