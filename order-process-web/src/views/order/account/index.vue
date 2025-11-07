<template>
  <div>
    <!-- 添加或修改账户类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="" prop="accountName">
              <el-input v-model="form.accountName" placeholder="请输入账户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="submitForm">新 增</el-button>
          </el-col>
        </el-row>
        <el-table border ref="brtTable" v-loading="loading" :data="accountList">
          <el-table-column label="账户名称" align="center">
            <template slot-scope="scope">
              <el-input v-if="scope.row.isEdit" v-model="scope.row.accountName" placeholder="请输入账户名称" />
              <span v-else>{{scope.row.accountName}}</span>
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
    listAccount,
    listAccountAll,
    getAccount,
    delAccount,
    addAccount,
    updateAccount
  } from "@/api/order/account";

  export default {
    name: "Account",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 账户类型表格数据
        accountList: [],
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
        updateAccount(row).then(response => {
          this.$modal.msgSuccess("修改成功");
          this.getList();
          this.$emit('getList');
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
        this.title = "账户"
      },
      /** 查询账户类型列表 */
      getList() {
        this.loading = true;
        listAccountAll().then(response => {
          this.accountList = response.data;
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
          accountId: null,
          accountName: null,
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
        addAccount(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.getList();
          this.$emit('getList');
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const accountIds = row.accountId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delAccount(accountIds);
        }).then(() => {
          this.getList();
          this.$emit('getList');
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
    }
  };
</script>
