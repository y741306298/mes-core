<template>
  <div class="statistics-main">
    <div class="formHeader">
      <div class="formTitle">收支年报</div>
      <div>
        <el-date-picker value-format="yyyy" size="mini" type="year" placeholder="选择年" style="width: 100px;" v-model="queryData.queryYear" @change="getDataList"/>
      </div>
    </div>

    <div id="annualReport" style="width: 100%;height:314px;"></div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';

  import {
  salesStatistics,
  marketStatistics,
  earning,
  deptOnTime
  } from "@/api/order/statistics";

  export default {
    name: "annualReport",
    data() {
      return {
        //收入上限值
        earningMax: 500,
        //收入区间
        earningInterval: 100,
        //支出上限值
        expendMax: 500,
        //支出区间值
        expendInterval: 100,
        //收入列表
        earningList:[],
        //支出列表
        expendList:[],
        queryData:{
            queryYear: null
        }
      };
    },
    mounted() {
      this.getDataList();
    },
    methods: {

      getDataList(){
        earning(this.queryData).then(res=>{
          this.earningList = res.data.earningList;
          this.expendList = res.data.expendList;
          this.earningMax = res.data.earningMax;
          this.earningInterval = res.data.earningInterval;
          this.expendMax = res.data.expendMax;
          this.expendInterval = res.data.expendInterval;

          this.initChart();
        })
      },

      async initChart() {

        //2. 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('annualReport'));

        //3. 指定图表的配置项和数据
        var option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999'
              }
            }
          },
          legend: {
            data: ['收入', '支出']
          },
          xAxis: [{
            type: 'category',
            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月', ],
            axisPointer: {
              type: 'shadow'
            }
          }],
          yAxis: [{
              type: 'value',
              name: '收入',
              min: 0,
              max: this.earningMax,
              interval: this.earningInterval,
            },
            {
              type: 'value',
              name: '支出',
              min: 0,
              max: this.expendMax,
              interval: this.expendInterval,
            }
          ],
          series: [
            {
              name: '收入',
              type: 'bar',
              color: '#ed702e',
              data: this.earningList
            },
            {
              name: '支出',
              type: 'bar',
              yAxisIndex: 1,
              color: '#3e4ef5',
              data: this.expendList
            }
          ]
        };
        //4.使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

      }
    }
  };
</script>
