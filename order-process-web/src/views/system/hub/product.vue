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
      <el-table-column label="产品编码" align="center" prop="prod_code" width="160" />
      <el-table-column label="产品名称" align="center" prop="prod_name" width="180" />
      <el-table-column label="产品类型" align="center" prop="prod_type" width="120" />
      <el-table-column label="最小长度" align="center" prop="min_length" width="120" />
      <el-table-column label="最大长度" align="center" prop="max_length" width="120" />
      <el-table-column label="最小宽度" align="center" prop="min_width" width="120" />
      <el-table-column label="最大宽度" align="center" prop="max_width" width="120" />
      <el-table-column label="材料编码" align="center" prop="material_code" width="160" />
      <el-table-column label="材料名称" align="center" prop="material_name" width="160" />
      <el-table-column label="材料颜色" align="center" prop="material_color" width="140" />
      <el-table-column label="材料品牌" align="center" prop="material_brand" width="140" />
      <el-table-column label="材料供应商" align="center" prop="material_supplier" width="160" />
      <el-table-column label="计量单位" align="center" prop="measure_unit" width="120" />
      <el-table-column label="单位重量" align="center" prop="unit_weight" width="120" />
      <el-table-column label="附加单价" align="center" prop="additional_unitfee" width="120" />
      <el-table-column label="是否商品" align="center" prop="is_merchandise" width="100">
        <template slot-scope="scope">
          <dict-tag :options="validOptions" :value="scope.row.is_merchandise" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="comments" min-width="180" show-overflow-tooltip />
    </el-table>
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
      manuProdList(this.queryParams).then(response => {
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
