<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="80px"
    >
      <el-form-item label="工艺编码" prop="procCode">
        <el-input
          v-model="queryParams.procCode"
          placeholder="请输入工艺编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工艺名称" prop="procName">
        <el-input
          v-model="queryParams.procName"
          placeholder="请输入工艺名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />

    <el-table v-loading="loading" :data="list">
      <el-table-column label="工艺编码" align="center" prop="procCode" width="160" />
      <el-table-column label="工艺名称" align="center" prop="procName" width="180" />
      <el-table-column label="附件类型" align="center" prop="procAttachmentTypeList" width="160" />
      <el-table-column label="计量单位" align="center" prop="measureUnitStr" width="120" />
      <el-table-column label="工艺单价" align="center" prop="procPrice" width="120" />
      <el-table-column label="是否有效" align="center" prop="valid" width="100">
        <template slot-scope="scope">
          <dict-tag :options="validOptions" :value="scope.row.valid" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="comments" min-width="180" show-overflow-tooltip />
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { manuProcList } from '@/api/system/hub'

export default {
  name: 'HubProcess',
  data() {
    return {
      loading: false,
      showSearch: true,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        procCode: '',
        procName: ''
      },
      validOptions: [
        { label: '是', value: true },
        { label: '否', value: false }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      manuProcList(this.queryParams).then(response => {
        this.list = response.rows || []
        this.total = response.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.pageNum = 1
      this.handleQuery()
    }
  }
}
</script>
