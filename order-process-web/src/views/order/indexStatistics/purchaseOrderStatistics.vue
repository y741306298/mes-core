<template>
  <div class="statistics-main">
    <div class="formHeader">
      <div class="formTitle">采购统计</div>
      <div>
        <el-date-picker value-format="yyyy" size="mini" type="year" placeholder="选择年" style="width: 100px;" v-model="queryData.queryYear" @change="getDataList"/>
      </div>
    </div>

    <div id="purchaseOrderStatistics" style="width: 100%;height:314px;"></div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';

  import {
  marketStatistics,
  } from "@/api/order/statistics";

  export default {
    name: "purchaseOrderStatistics",
    data() {
      return {
        marketList:[], //采购数据
        receivedList:[], // 已收货
        marketMax: 500,
        marketInterval: 100,
        receivedMax: 500,
        receivedInterval: 100,
        queryData:{
          queryYear:null
        }
      };
    },
    mounted() {
      // this.initChart();
      this.getDataList()
    },
    methods: {
      getDataList(){
        marketStatistics(this.queryData).then(res=>{
          this.marketList = res.data.marketList;
          this.receivedList =res.data.receivedList;
          this.marketMax = res.data.marketMax;
          this.marketInterval = res.data.marketInterval;
          this.receivedMax = res.data.receivedMax;
          this.receivedInterval = res.data.receivedInterval;

          this.initChart();
        })
      },


      async initChart() {

        //2. 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('purchaseOrderStatistics'));
        // const numArr = [this.data.assetNum, this.data.disposeNum, this.data.stayNum];

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
            data: ['采购数量', '已收货数量']
          },
          xAxis: [{
            type: 'category',
            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            axisPointer: {
              type: 'shadow'
            }
          }],
          yAxis: [{
              type: 'value',
              name: '采购数量',
              min: 0,
              max: this.marketMax,
              interval: this.marketInterval,
            },
            {
              type: 'value',
              name: '已收货数量',
              min: 0,
              max: this.receivedMax,
              interval: this.receivedInterval,
            }
          ],
          series: [
            {
              name: '采购数量',
              type: 'bar',
              color: '#ed702e',
              data: this.marketList
            },
            {
              name: '已收货数量',
              type: 'line',
              color: '#3e4ef5',
              yAxisIndex: 1,
              data: this.receivedList
            }
          ]
        };
        //4.使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

      }
    }
  };
</script>
