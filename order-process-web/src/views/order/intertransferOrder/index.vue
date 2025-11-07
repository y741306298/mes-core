<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单号" prop="intertransferNo">
        <el-input v-model="queryParams.intertransferNo" placeholder="请输入单号" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="员工" prop="userId">
        <el-select v-model="queryParams.userId" filterable clearable>
          <el-option v-for="(item,index) in userList" :value="item.userId" :label="item.userName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账户" prop="payAccountId">
        <el-select v-model="queryParams.payAccountId" filterable clearable>
          <el-option v-for="(item,index) in accountList" :value="item.accountId" :label="item.accountName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账户" prop="collectionAccountId">
        <el-select v-model="queryParams.collectionAccountId" filterable clearable>
          <el-option v-for="(item,index) in accountList" :value="item.accountId" :label="item.accountName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="intertransferDateType">
        <el-select v-model="queryParams.intertransferDateType" filterable clearable>
          <el-option value="0" label="当日"></el-option>
          <el-option value="1" label="昨日"></el-option>
          <el-option value="2" label="当月"></el-option>
          <el-option value="3" label="本季度"></el-option>
          <el-option value="4" label="本年"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="">
        <el-date-picker v-model="queryParams.intertransferDateTypes" value-format="yyyy-MM-dd" type="daterange" range-separator="至" start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['order:intertransferOrder:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['order:intertransferOrder:edit']">编 辑</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button  type="danger" plain icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete" v-hasPermi="['order:intertransferOrder:remove']">删除</el-button>
      </el-col>

      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="intertransferOrderList"
      @selection-change="handleSelectionChange">
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
              <span v-if="item.prop == 'account' && scope.row.payAccountVo!=null">
                {{scope.row.payAccountVo.accountName}} -
              </span>
              <span v-if="item.prop == 'account' && scope.row.collectionAccountVo!=null">
                - {{scope.row.collectionAccountVo.accountName}}
              </span>
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />


  </div>
</template>

<script>
  import {
    listIntertransferOrder,
    listIntertransferOrderAll,
    getIntertransferOrder,
    delIntertransferOrder,
    addIntertransferOrder,
    updateIntertransferOrder
  } from "@/api/order/intertransferOrder";

  import {
    listAllUser
  } from "@/api/system/user";

  import {
    listAccountAll
  } from "@/api/order/account";

  export default {
    name: "IntertransferOrder",
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
        // 互转单表格数据
        intertransferOrderList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          intertransferNo: null,
          userId: null,
          intertransferAmount: null,
          payAccountId: null,
          collectionAccountId: null,
          intertransferDate: null,
          intertransferRemark: null,
          attachments: null,
          intertransferDateType: null,
          intertransferDateTypes: [],
          intertransferDateStart: null,
          intertransferDateEnd: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "IntertransferOrder",
        columnList: [{
            label: "单号",
            prop: "intertransferNo",
            visible: true,
            align: "center",
          },
          {
            label: "日期",
            prop: "intertransferDate",
            visible: true,
            align: "center",
            type: "date",
            dateFormat: "{y}-{m}-{d}"
          },
          {
            label: "账户",
            prop: "account",
            visible: true,
            align: "center",
            type: "other"
          },
          {
            label: "金额",
            prop: "intertransferAmount",
            visible: true,
            align: "center",
          },
          {
            label: "制单人",
            prop: "createBy",
            visible: true,
            align: "center",
          },
          {
            label: "备注",
            prop: "intertransferRemark",
            visible: true,
            align: "center",
          }
        ],
        // 获取账户列表
        accountList: [],
        // 员工列表
        userList: [],
      };
    },
    created() {
      this.getList();
      this.refreshCloumn(this);
      this.getUserList();
      this.getAccountList();
    },
    methods: {
      /**
       * 获取账户列表
       */
      getAccountList() {
        listAccountAll().then(res => {
          this.accountList = res.data;
        })
      },
      /**
       * 获取员工列表
       */
      getUserList() {
        listAllUser().then(res => {
          this.userList = res.data;
        })
      },
      /** 查询互转单列表 */
      getList() {
        this.loading = true;
        if(this.queryParams.intertransferDateTypes!=null && this.queryParams.intertransferDateTypes.length>0){
          this.queryParams.intertransferDateStart = this.queryParams.intertransferDateTypes[0]
          this.queryParams.intertransferDateEnd = this.queryParams.intertransferDateTypes[1]
        }else{
          this.queryParams.intertransferDateStart = null
          this.queryParams.intertransferDateEnd = null
        }
        listIntertransferOrder(this.queryParams).then(response => {
          this.intertransferOrderList = response.rows;
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
          intertransferId: null,
          intertransferNo: null,
          userId: null,
          intertransferAmount: null,
          payAccountId: null,
          collectionAccountId: null,
          intertransferDate: null,
          intertransferRemark: null,
          attachments: null,
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
        this.ids = selection.map(item => item.intertransferId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.$router.push({
          path: '/intertransferOrder/info/index',
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {

        this.$router.push({
          path: '/intertransferOrder/info/index',
          query: {
            intertransferId: this.ids[0]
          }
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.intertransferId != null) {
              updateIntertransferOrder(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addIntertransferOrder(this.form).then(response => {
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
        const intertransferIds = row.intertransferId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delIntertransferOrder(intertransferIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/intertransferOrder/export', {
          ...this.queryParams
        }, `intertransferOrder_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
