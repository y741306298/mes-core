<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="120px"
    >
      <el-form-item label="生产方编码" prop="manufacturerCode">
        <el-input
          v-model="queryParams.manufacturerCode"
          placeholder="请输入生产方编码"
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
      <el-table-column label="工艺编码" align="center" prop="proc_code" width="160" />
      <el-table-column label="工艺名称" align="center" prop="proc_name" width="180" />
      <el-table-column label="附件类型" align="center" prop="proc_attachmentTypeList" width="160" />
      <el-table-column label="计量单位" align="center" prop="measure_unitStr" width="120" />
      <el-table-column label="工艺单价" align="center" prop="proc_price" width="120" />
      <el-table-column label="是否有效" align="center" prop="is_valid" width="100">
        <template slot-scope="scope">
          <dict-tag :options="validOptions" :value="scope.row.is_valid" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="comments" min-width="180" show-overflow-tooltip />
    </el-table>
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
      queryParams: {
        manufacturerCode: ''
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
        this.list = response.data || []
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    }
  }
}
</script>
