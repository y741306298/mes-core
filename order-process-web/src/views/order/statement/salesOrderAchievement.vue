<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['order:salesOrder:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-date-picker size="mini" v-model="queryParams.year" @change="getAchievement" value-format="yyyy" type="year" placeholder="选择年">
        </el-date-picker>
      </el-col>
      <!-- <right-toolbar :columns="columnList" :cacheKey="cacheKey" @queryTable="getList"></right-toolbar> -->
    </el-row>

    <div id="depreciation" style="width: 100%;height:380px;"></div>

    <el-table :data="achievementDataList" row-key="month" border lazy :load="load"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column label="月份" prop="month" align="center">
        <template slot-scope="scope">
          {{scope.row.month}}
        </template>
      </el-table-column>
      <el-table-column label="收入" prop="income" align="center"></el-table-column>
      <el-table-column label="支出" prop="disburse" align="center"></el-table-column>
      <el-table-column label="当月结余" align="center">
        <template slot-scope="scope">
          {{scope.row.income - scope.row.disburse}}
        </template>
      </el-table-column>
      <el-table-column label="累计结余" prop="totalBalance" align="center"></el-table-column>
    </el-table>

  </div>
</template>

<script>
  import {
    achievement,
    accountAchievement
  } from "@/api/order/check";

  import * as echarts from 'echarts'

  export default {
    name: "salesOrderAchievement",
    data() {
      return {
        // 查询参数
        queryParams: {
          year: '2024',
          orderType: '1',
          month: null
        },
        // 业绩列表
        achievementDataList: [],
      };
    },
    created() {
      this.getAchievement();

    },
    methods: {
      load(tree, treeNode, resolve) {
        this.queryParams.month = tree.month
        accountAchievement(this.queryParams).then(res => {
          resolve(res.data)
        })
      },
      initChart() {
        let data = [
          ['月份', '收入', '支出']
        ];
        this.achievementDataList.forEach((item, index) => {
          let o = [item.month + '月', item.income, item.disburse];
          data.push(o);
        })
        //2. 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('depreciation'));
        //3. 指定图表的配置项和数据
        var option = {

          legend: {},
          tooltip: {},
          dataset: {
            source: data
          },
          xAxis: {
            type: 'category'
          },
          yAxis: {},
          // Declare several bar series, each will be mapped
          // to a column of dataset.source by default.
          series: [{
            type: 'bar',
            color: '#ed702e'
          }, {
            type: 'bar',
            color: '#3e4ef5'
          }]
        };
        //4.使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

      },
      /**
       * 获取业绩统计
       */
      getAchievement() {
        achievement(this.queryParams).then(res => {
          this.achievementDataList = res.data;
          this.initChart();
        })
      },

      /** 导出按钮操作 */
      handleExport() {
        this.download('order/check/exportAchievement', {
          ...this.queryParams
        }, `销售业绩_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
