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
      <el-form-item label="产品编码" prop="prodCode">
        <el-input
          v-model="queryParams.prodCode"
          placeholder="请输入产品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="prodName">
        <el-input
          v-model="queryParams.prodName"
          placeholder="请输入产品名称"
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
      <el-table-column label="产品编码" align="center" prop="prodCode" width="160" />
      <el-table-column label="产品名称" align="center" prop="prodName" width="180" />
      <el-table-column label="产品类型" align="center" prop="prodType" width="120" />
      <el-table-column label="最小长度" align="center" prop="minLength" width="120" />
      <el-table-column label="最大长度" align="center" prop="maxLength" width="120" />
      <el-table-column label="最小宽度" align="center" prop="minWidth" width="120" />
      <el-table-column label="最大宽度" align="center" prop="maxWidth" width="120" />
      <el-table-column label="材料编码" align="center" prop="materialCode" width="160" />
      <el-table-column label="材料名称" align="center" prop="materialName" width="160" />
      <el-table-column label="材料颜色" align="center" prop="materialColor" width="140" />
      <el-table-column label="材料品牌" align="center" prop="materialBrand" width="140" />
      <el-table-column label="材料供应商" align="center" prop="materialSupplier" width="160" />
      <el-table-column label="计量单位" align="center" prop="measureUnit" width="120" />
      <el-table-column label="单位重量" align="center" prop="unitWeight" width="120" />
      <el-table-column label="附加单价" align="center" prop="additionalUnitfee" width="120" />
      <el-table-column label="是否商品" align="center" prop="merchandise" width="100">
        <template slot-scope="scope">
          <dict-tag :options="validOptions" :value="scope.row.merchandise" />
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
import { manuProdList } from '@/api/system/hub'

export default {
  name: 'HubProduct',
  data() {
    return {
      loading: false,
      showSearch: true,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        prodCode: '',
        prodName: ''
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
      manuProdList(this.queryParams).then(response => {
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
