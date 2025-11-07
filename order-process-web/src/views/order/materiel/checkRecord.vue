<template>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

        <el-form-item label="时间" prop="createTimes">
            <el-date-picker v-model="queryParams.createTimes" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"> </el-date-picker>
        </el-form-item>

        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="queryParams.orderNo" placeholder="请输入物料名称"/>
        </el-form-item>

        <el-form-item label="库存类型" prop="type">
            <el-select v-model="queryParams.type" placeholder="请选择">
                <el-option label="全部" value=""> </el-option>
                <el-option label="出库" value="出库"> </el-option>
                <el-option label="入库" value="入库"> </el-option>
            </el-select>
        </el-form-item>

        <el-form-item label="操作人" prop="userId">
            <el-select v-model="queryParams.userId" placeholder="请选择">
                <el-option v-for="item in userList" :key="item.userId" :label="item.nickName" :value="item.userId"> </el-option>
            </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
  
      <el-table  ref="brtTable" v-loading="loading" :data="materielRecordList">
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
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
        @pagination="getList" />
  
    </div>
  </template>
  
  <script>
    import {
        selectRecord
    } from "@/api/order/materiel";

    import {
        listAllUser
    } from "@/api/system/user";
  
  
    export default {
      name: "CheckRecord",
      data() {
        return {
          userList: [],
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
          // 物料出入库记录表格数据
          materielRecordList: [],
          // 查询参数
          queryParams: {
            pageNum: 1,
            pageSize: 10,
            type: null,
            orderNo: null,
            userId: null,
            createTimes: [],
            timeStart: null,
            timeEnd: null,
            materielId: null
          },

          cacheCloumnList: [], //缓存字段列表
          //缓存名称
          cacheKey: "CheckRecord",
          columnList: [
            {
              label: "单号",
              prop: "orderNo",
              visible: true,
              align: "center",
            },
            {
              label: "库存类型",
              prop: "type",
              visible: true,
              align: "center",
            },
            {
              label: "时间",
              prop: "updateTime",
              visible: true,
              align: "center",
              type: "date",
              dateFormat: "{y}-{m}-{d}"
            },
            {
              label: "数量",
              prop: "num",
              visible: true,
              align: "center",
            },
            {
              label: "库存结余",
              prop: "residueNum",
              visible: true,
              align: "center",
            },
            {
              label: "操作人",
              prop: "userName",
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
          this.getUserList();
          this.getList();
          this.refreshCloumn(this);
        },

        /** 查询物料信息列表 */
        getList() {
            if(this.queryParams.createTimes != null && this.queryParams.createTimes.length>0){
                this.queryParams.timeStart = this.queryParams.createTimes[0];
                this.queryParams.timeEnd = this.queryParams.createTimes[1];
            }
            this.loading = true;
            const materielId = this.$route.query.materielId;
            this.queryParams.materielId = materielId;
            this.materielRecordList = [];
            selectRecord(this.queryParams).then(response => {
                this.materielRecordList = response.data.rows;
                this.total = response.data.total;
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

        getUserList(){
            listAllUser().then(res=>{
                this.userList = res.data;
            })
        }

      }
    };
  </script>
  <style>
  </style>
  