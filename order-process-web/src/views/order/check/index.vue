<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="类别" prop="typeId">
        <el-select v-model="queryParams.typeId" filterable clearable>
          <el-option v-for="(item,index) in checkTypeList" :value="item.typeId" :label="item.typeName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账户" prop="accountId">
        <el-select v-model="queryParams.accountId" filterable clearable>
          <el-option v-for="(item,index) in accountList" :value="item.accountId" :label="item.accountName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="员工" prop="userId">
        <el-select v-model="queryParams.userId" filterable clearable>
          <el-option v-for="(item,index) in userList" :value="item.userId" :label="item.userName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="checkDateType">
        <el-select v-model="queryParams.checkDateType" filterable clearable>
          <el-option value="0" label="当日"></el-option>
          <el-option value="1" label="昨日"></el-option>
          <el-option value="2" label="当月"></el-option>
          <el-option value="3" label="本季度"></el-option>
          <el-option value="4" label="本年"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="">
        <el-date-picker v-model="queryParams.checkDates" value-format="yyyy-MM-dd" type="daterange" range-separator="至" start-placeholder="开始日期"
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
          v-hasPermi="['order:check:add']">新增流水</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['order:check:edit']">编 辑</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button  type="danger" plain icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete" v-hasPermi="['order:check:remove']">删除</el-button>
      </el-col>

      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="checkList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column type="selection" width="55" align="center" />
      <!-- 循环字段 -->
      <template v-for="(item,index) in cacheCloumnList">
        <el-table-column v-if="item.visible" :label="item.label" :prop="item.prop" :width="item.width||''"
          :align="item.align||'center'">
          <template slot="header" slot-scope="scope">
            {{item.label}}
            <el-button v-if="item.prop == 'accountVo.accountName'" v-hasPermi="['order:account:list']" @click="handleOpenAccount" class="mt5 ml5"
              icon="el-icon-plus" size="mini" circle></el-button>
          </template>
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
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 账户 -->
    <account ref="accountRef"></account>

  </div>
</template>

<script>
  import {
    listCheck,
    listCheckAll,
    getCheck,
    delCheck,
    addCheck,
    updateCheck
  } from "@/api/order/check";

  import {
    listCheckTypeAll
  } from "@/api/order/checkType";

  import {
    listAllUser
  } from "@/api/system/user";

  import {
    listAccountAll
  } from "@/api/order/account";

  import account from "@/views/order/account/index"

  export default {
    name: "Check",
    components: {
      account
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
        // 账单表格数据
        checkList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          typeId: null,
          orderId: null,
          orderNo: null,
          userId: null,
          checkAmount: null,
          accountId: null,
          checkDate: null,
          checkRemark: null,
          attachments: null,
          checkType: null,
          checkDateType: null,
          checkDates: [],
          checkDateStart: null,
          checkDateEnd: null,
        },
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "Check",
        columnList: [{
            label: "单号",
            prop: "checkNo",
            visible: true,
            align: "center",
          },
          {
            label: "关联单号",
            prop: "orderNo",
            visible: true,
            align: "center",
          },
          {
            label: "日期",
            prop: "checkDate",
            visible: true,
            align: "center",
            type: "date",
            dateFormat: "{y}-{m}-{d}"
          },
          {
            label: "类别",
            prop: "checkTypeVo.typeName",
            visible: true,
            align: "center",
            type: "obj"
          },
          {
            label: "账户",
            prop: "accountVo.accountName",
            visible: true,
            align: "center",
            type: "obj"
          },
          {
            label: "金额",
            prop: "checkAmount",
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
            prop: "checkRemark",
            visible: true,
            align: "center",
          }
        ],
        // 账单分类名称
        checkTypeList: [],
        // 获取账户列表
        accountList: [],
        // 员工列表
        userList: [],
      };
    },
    created() {
      this.getList();
      this.refreshCloumn(this);
      this.getCheckTypeList();
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
      /**
       * 获取账单类型列表
       */
      getCheckTypeList() {
        let checkType = this.$route.query.type;
        listCheckTypeAll({
          type: checkType
        }).then(res => {
          this.checkTypeList = res.data;
        })
      },
      /**
       * 打开账户弹框
       */
      handleOpenAccount() {
        this.$refs.accountRef.handleOpen();
      },
      /** 查询账单列表 */
      getList() {
        this.loading = true;
        this.queryParams.checkType = this.$route.query.type;
        if(this.queryParams.checkDates!=null && this.queryParams.checkDates.length>0){
          this.queryParams.checkDateStart = this.queryParams.checkDates[0]
          this.queryParams.checkDateEnd = this.queryParams.checkDates[1]
        }else{
          this.queryParams.checkDateStart = null
          this.queryParams.checkDateEnd = null
        }
        listCheck(this.queryParams).then(response => {
          this.checkList = response.rows;
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
          checkId: null,
          typeId: null,
          orderId: null,
          orderNo: null,
          userId: null,
          checkAmount: null,
          accountId: null,
          checkDate: null,
          checkRemark: null,
          attachments: null,
          checkType: null,
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
        this.ids = selection.map(item => item.checkId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.$router.push({
          path: '/check/info/index',
          query: {
            type: this.queryParams.checkType,
            
          }
        });
      },
      //删除
      handleDelete(){
        let ids = this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delCheck(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      //修改
      handleUpdate(){
        this.$router.push({
          path: '/check/info/index',
          query: {
            type: this.queryParams.checkType,
            checkId: this.ids[0]
          }
        });
      }
    }
  };
</script>
