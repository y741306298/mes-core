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
      <el-form-item label="材料编码" prop="matCode">
        <el-input
          v-model="queryParams.matCode"
          placeholder="请输入材料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="材料名称" prop="matName">
        <el-input
          v-model="queryParams.matName"
          placeholder="请输入材料名称"
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
      <el-table-column label="材料编码" align="center" prop="matCode" width="160" />
      <el-table-column label="材料名称" align="center" prop="matName" width="180" />
      <el-table-column label="类别" align="center" prop="matCategory" width="120" />
      <el-table-column label="颜色" align="center" prop="matColor" width="120" />
      <el-table-column label="品牌" align="center" prop="matBrand" width="120" />
      <el-table-column label="供应商" align="center" prop="matSupplier" width="140" />
      <el-table-column label="宽度" align="center" prop="matWidth" width="120" />
      <el-table-column label="长度" align="center" prop="matLength" width="120" />
      <el-table-column label="厚度" align="center" prop="matThickness" width="120" />
      <el-table-column label="计量单位" align="center" prop="measureUnit" width="120" />
      <el-table-column label="单位重量" align="center" prop="unitWeight" width="120" />
      <el-table-column label="单价" align="center" prop="unitPrice" width="120" />
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
import { manuMatList } from '@/api/system/hub'

export default {
  name: 'HubMaterial',
  data() {
    return {
      loading: false,
      showSearch: true,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        matCode: '',
        matName: ''
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
      manuMatList(this.queryParams).then(response => {
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
