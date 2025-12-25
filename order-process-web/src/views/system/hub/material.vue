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
      <el-table-column label="材料编码" align="center" prop="mat_code" width="160" />
      <el-table-column label="材料名称" align="center" prop="mat_name" width="180" />
      <el-table-column label="类别" align="center" prop="mat_category" width="120" />
      <el-table-column label="颜色" align="center" prop="mat_color" width="120" />
      <el-table-column label="品牌" align="center" prop="mat_brand" width="120" />
      <el-table-column label="供应商" align="center" prop="mat_supplier" width="140" />
      <el-table-column label="宽度" align="center" prop="mat_width" width="120" />
      <el-table-column label="长度" align="center" prop="mat_length" width="120" />
      <el-table-column label="厚度" align="center" prop="mat_thickness" width="120" />
      <el-table-column label="计量单位" align="center" prop="measure_unit" width="120" />
      <el-table-column label="单位重量" align="center" prop="unit_weight" width="120" />
      <el-table-column label="单价" align="center" prop="unit_price" width="120" />
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
import { manuMatList } from '@/api/system/hub'

export default {
  name: 'HubMaterial',
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
      manuMatList(this.queryParams).then(response => {
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
