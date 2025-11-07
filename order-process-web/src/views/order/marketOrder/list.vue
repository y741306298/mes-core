<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="业务员" prop="userId">
        <el-select v-model="queryParams.userId" clearable filterable class="drag-screenful-contnet">
          <el-option v-for="(item,index) in marketmanUserList" :value="item.userId" :label="item.userName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['order:marketOrder:add']">新增</el-button>
      </el-col>
      <right-toolbar :columns="columnList" :cacheKey="cacheKey" :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border ref="brtTable" v-loading="loading" :data="marketOrderList"
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
              <el-button v-if="item.prop == 'orderNo'" type="text" @click="toOrderDetails(scope.row)">{{scope.row.orderNo}}</el-button>
              <span v-if="item.prop == 'notCollectionAmount'">
                {{scope.row.totalAmount - scope.row.collectionAmount}}
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
    listMarketOrder,
    listMarketOrderAll,
    getMarketOrder,
    delMarketOrder,
    addMarketOrder,
    updateMarketOrder
  } from "@/api/order/marketOrder";

  import {
    listAllUser
  } from "@/api/system/user";

  export default {
    name: "MarketOrderList",
    dicts: ['collection_status'],
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
        // 销售单表格数据
        marketOrderList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          orderNo: null,
          supplierId: null,
          contact: null,
          contactTel: null,
          customerAddressId: null,
          orderTime: null,
          deliveryTime: null,
          templateId: null,
          craftType: null,
          totalNum: null,
          totalAmount: null,
          currencyType: null,
          attachments: null,
          userId: null,
          dateType: null,
          createTimes: [],
          createTimeStart: null,
          createTimeEnd: null
        },
        cacheCloumnList: [], //缓存字段列表
        //缓存名称
        cacheKey: "MarketOrderList",
        columnList: [{
            label: "单号",
            prop: "orderNo",
            visible: true,
            align: "center",
            type: "other",
            width: 150
          },
          {
            label: "日期",
            prop: "orderTime",
            visible: true,
            align: "center",
            type: "date",
            dateFormat: "{y}-{m}-{d}"
          },
          {
            label: "合同金额",
            prop: "totalAmount",
            visible: true,
            align: "center",
          },
          {
            label: "未收款",
            prop: "notCollectionAmount",
            visible: true,
            align: "center",
            type: "other"
          },
          {
            label: "已收款",
            prop: "collectionAmount",
            visible: true,
            align: "center",
          },
          {
            label: "收款状态",
            prop: "collectionStatus",
            visible: true,
            align: "center",
            dictType: "collection_status",
            type: "dict"
          },
        ],
        // 业务员列表
        marketmanUserList: [],
      };
    },
    created() {
      this.getList();
      this.refreshCloumn(this);
      this.getMarketmanUserList();
    },
    methods: {
      /**
       * 获取业务员列表
       */
      getMarketmanUserList() {
        listAllUser({
          roleId: 2
        }).then(res => {
          this.marketmanUserList = res.data
        })
      },
      /**
       * 跳转订单详情
       */
      toOrderDetails(row){
        this.$router.push({
          path: '/marketOrder/info/index',
          query: {
            orderId: row.orderId
          }
        });
      },
      /** 查询销售单列表 */
      getList() {
        this.loading = true;
        if(this.queryParams.createTimes!=null && this.queryParams.createTimes.length>0){
          this.queryParams.createTimeStart = this.queryParams.createTimes[0]
          this.queryParams.createTimeEnd = this.queryParams.createTimes[1]
        }else{
          this.queryParams.createTimeStart = null
          this.queryParams.createTimeEnd = null
        }
        this.queryParams.supplierId = this.$route.query.supplierId;
        listMarketOrder(this.queryParams).then(response => {
          this.marketOrderList = response.rows;
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
        this.ids = selection.map(item => item.orderId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.$router.push({
          path: '/marketOrder/add/index',
          query: {
            supplierId: this.queryParams.supplierId
          }
        });
      }
    }
  };
</script>
