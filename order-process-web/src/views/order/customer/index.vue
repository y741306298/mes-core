<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="搜索" prop="keyWords">
        <el-input v-model="queryParams.keyWords" placeholder="请输入客户名称/客户编号" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactTel">
        <el-input v-model="queryParams.contactTel" placeholder="请输入联系电话" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户类型" prop="typeId">
        <el-select v-model="queryParams.typeId" clearable filterable class="drag-screenful-contnet">
          <el-option v-for="dict in dict.type.customer_type" :key="dict.value" :label="dict.label"
          :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="客户级别" prop="gradeId">
        <el-select v-model="queryParams.gradeId" clearable filterable class="drag-screenful-contnet">
          <el-option v-for="dict in dict.type.customer_grade" :key="dict.value" :label="dict.label"
          :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="业务员" prop="userId">
        <el-select v-model="queryParams.userId" clearable filterable class="drag-screenful-contnet">
          <el-option v-for="(item,index) in salesmanUserList" :value="item.userId" :label="item.userName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="dateType">
        <el-select v-model="queryParams.dateType" filterable clearable>
          <el-option value="0" label="当日"></el-option>
          <el-option value="1" label="昨日"></el-option>
          <el-option value="2" label="当月"></el-option>
          <el-option value="3" label="本季度"></el-option>
          <el-option value="4" label="本年"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="">
        <el-date-picker v-model="queryParams.createTimes" value-format="yyyy-MM-dd" type="daterange" range-separator="至" start-placeholder="开始日期"
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
          v-hasPermi="['order:customer:add']">新增客户</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['order:customer:edit']">编辑</el-button>
      </el-col>

      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-dropdown split-button type="primary" @command="handleDropdownClick" size="mini">
          批量操作
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="download">下载模板</el-dropdown-item>
            <el-dropdown-item command="import" v-hasPermi="['order:customer:import']">导入客户</el-dropdown-item>
            <el-dropdown-item command="export" v-hasPermi="['order:customer:export']">导出客户</el-dropdown-item>
            <el-dropdown-item command="delete" :disabled="multiple" v-hasPermi="['order:customer:remove']">删除</el-dropdown-item>
            <el-dropdown-item command="transfer" :disabled="single" v-hasPermi="['order:customer:transfer']">转移客户</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column type="selection" width="55" align="center" />

      <!-- 循环字段 -->
      <template v-for="(item,index) in cacheCloumnList">
        <el-table-column v-if="item.visible" :label="item.label" :prop="item.prop" :width="item.width||''"
          :align="item.align||'center'">
          <!-- <template slot="header" slot-scope="scope">
            {{item.label}}
            <el-button v-if="item.prop == 'typeVo.typeName'" @click="handleOpenCustomerType" class="mt5 ml5"
              icon="el-icon-plus" size="mini" circle v-hasPermi="['order:customerType:list']"></el-button>
            <el-button v-if="item.prop == 'gradeVo.gradeName'" @click="handleOpenCustomerGrade" class="mt5 ml5"
              icon="el-icon-plus" size="mini" circle v-hasPermi="['order:customerGrade:list']"></el-button>
          </template> -->
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
              <el-button v-if="item.prop == 'customerNo'" type="text"
                @click="hanldeToHome(scope.row)">{{scope.row.customerNo}}</el-button>
              <el-button v-if="item.prop == 'attachments'" type="text"
                @click="hanldeOpenFileList(scope.row)">查看</el-button>
            </template>
            <span v-else>{{scope.row[item.prop]}}</span>
          </template>
        </el-table-column>
      </template>
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['order:customer:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['order:customer:remove']">删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 客户类型弹框 -->
    <!-- <customer-type ref="customerTypeRef"></customer-type> -->

    <!-- 客户等级弹框 -->
    <!-- <customer-grade ref="customerGreadeRef"></customer-grade> -->

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

    <el-dialog title="转移业务员" :visible.sync="transfer.open" width="400px" append-to-body>
      <el-form :model="transfer.form" ref="transferForm" size="small" label-width="68px">
        <el-form-item label="业务员" prop="userId">
          <el-select v-model="transfer.form.userId" clearable filterable class="drag-screenful-contnet">
            <el-option v-for="(item,index) in salesmanUserList" :value="item.userId" :label="item.userName"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTransferForm">确 定</el-button>
        <el-button @click="transfer.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 文件列表 -->
    <file-list @updateFile="updateCustomerInfo" ref="fileListRef"></file-list>

  </div>
</template>

<script>
  import {
    listCustomer,
    listCustomerAll,
    getCustomer,
    delCustomer,
    addCustomer,
    updateCustomer
  } from "@/api/order/customer";

  import {
    getToken
  } from "@/utils/auth";

  import {
    listCustomerGradeAll
  } from "@/api/order/customerGrade";

  import {
    listCustomerTypeAll
  } from "@/api/order/customerType";

  import {
    listAllUser
  } from "@/api/system/user";

  import customerType from '@/views/order/customerType/index'
  import customerGrade from '@/views/order/customerGrade/index'

  export default {
    name: "Customer",
    dicts:["customer_type","customer_grade"],
    components: {
      customerType,
      customerGrade
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
        // 客户信息表格数据
        customerList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          customerName: null,
          customerNo: null,
          typeId: null,
          gradeId: null,
          contact: null,
          contactTel: null,
          userId: null,
          customerRemark: null,
          attachments: null,
          keyWords: null,
          dateType: null,
          createTimes:[],
          createTimeStart: null,
          craeteTimeEnd: null
        },
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "Customer",
        columnList: [{
            label: "客户编号",
            prop: "customerNo",
            visible: true,
            align: "center",
            type: "other"
          },
          {
            label: "客户名称",
            prop: "customerName",
            visible: true,
            align: "center",
          },
          {
            label: "客户类型",
            prop: "typeId",
            visible: true,
            align: "center",
            type: "dict",
            dictType: "customer_type"
          },
          {
            label: "客户级别",
            prop: "gradeId",
            visible: true,
            align: "center",
            type: "dict",
            dictType: "customer_grade"
          },
          {
            label: "联系人",
            prop: "contact",
            visible: true,
            align: "center",
          },
          {
            label: "电话",
            prop: "contactTel",
            visible: true,
            align: "center",
          },
          {
            label: "业务员",
            prop: "user.userName",
            visible: true,
            align: "center",
            type: "obj"
          },
          {
            label: "客户地址",
            prop: "customerAddress",
            visible: true,
            align: "center",
          },
          {
            label: "备注",
            prop: "customerRemark",
            visible: true,
            align: "center",
          },
          {
            label: "附件",
            prop: "attachments",
            visible: true,
            align: "center",
            type: "other"
          },
        ],
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
          url: process.env.VUE_APP_BASE_API + "/order/customer/importData"
        },
        // 转移参数
        transfer: {
          // 是否显示弹出层
          open: false,
          // 表单参数
          form: {
            customerId: null,
            userId: null
          }
        },
        // 客户类型列表
        customerTypeList: [],
        // 客户等级列表
        customerGradeList: [],
        // 业务员列表
        salesmanUserList: [],
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
      this.onLoad();
    },
    methods: {

      onLoad(){
        this.getList();
        this.refreshCloumn(this);
        this.getSalesmanUserList();
      },
      /**
       * 去主页
       */
      hanldeToHome(row){
        this.$router.push({
          path: '/customer/home/index',
          query: {
            customerId: row.customerId,
            customerName: row.customerName
          }
        });
      },
      /**
       * 修改客户信息
       */
      updateCustomerInfo(customer) {
        updateCustomer(customer).then(res => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.getList();
        })
      },
      /**
       * 打开文件弹框列表
       */
      hanldeOpenFileList(row) {
        this.$refs.fileListRef.handleOpen(row,'attachments')
      },
      /**
       * 提交转移
       */
      submitTransferForm() {
        updateCustomer(this.transfer.form).then(res => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.transfer.open = false;
          this.getList();
        })
      },
      /**
       * 转移客户
       */
      handleTransfer() {
        this.transfer = {
          open: true,
          // 表单参数
          form: {
            customerId: this.ids[0],
            userId: null
          }
        }
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
      /**
       * 打开客户等级弹框
       */
      handleOpenCustomerGrade() {
        this.$refs.customerGreadeRef.handleOpen();
      },
      /**
       * 打开客户等级弹框
       */
      handleOpenCustomerType() {
        this.$refs.customerTypeRef.handleOpen();
      },
      /**
       * 获取业务员列表
       */
      getSalesmanUserList() {
        listAllUser({
          roleId: 2
        }).then(res => {
          this.salesmanUserList = res.data
        })
      },
      // /**
      //  * 获取客户等级列表
      //  */
      // getCustomerGradeList() {
      //   listCustomerGradeAll().then(res => {
      //     this.customerGradeList = res.data;
      //   })
      // },
      // /**
      //  * 获取客户类型列表
      //  */
      // getCustomerTypeList() {
      //   listCustomerTypeAll().then(res => {
      //     this.customerTypeList = res.data;
      //   })
      // },
      /**
       * 批量操作按钮点击事件
       */
      handleDropdownClick(command) {
        switch (command) {
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
          case 'transfer':
            this.handleTransfer();
            break;
        }
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        this.download('order/customer/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
      },
      /** 查询客户信息列表 */
      getList() {
        this.loading = true;
        if(this.queryParams.createTimes!=null && this.queryParams.createTimes.length>0){
          this.queryParams.createTimeStart = this.queryParams.createTimes[0]
          this.queryParams.createTimeEnd = this.queryParams.createTimes[1]
        }else{
          this.queryParams.createTimeStart = null
          this.queryParams.createTimeEnd = null
        }
        listCustomer(this.queryParams).then(response => {
          this.customerList = response.rows;
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
        this.ids = selection.map(item => item.customerId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.$router.push({
          path: '/customer/info/index',
        });
      },
      /** 修改按钮操作 */
      handleUpdate() {
        const customerId = this.ids[0]
        this.$router.push({
          path: '/customer/info/index',
          query: {
            customerId: customerId
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const customerIds = this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delCustomer(customerIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        console.log(this.ids);
        this.download('order/customer/export', {
          ...this.queryParams,
          ids: this.ids
        }, `customer_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
