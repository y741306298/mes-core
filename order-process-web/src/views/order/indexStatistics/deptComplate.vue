<template>
  <div class="statistics-main">
    <div class="formHeader">
      <div class="formTitle">各部门完成情况</div>
      <div>
        <el-date-picker size="mini" v-model="queryData.queryDates" type="daterange" range-separator="至" start-placeholder="开始日期"
          end-placeholder="结束日期" style="width: 230px;" value-format="yyyy-MM-dd" @change="getDataList">
        </el-date-picker>
      </div>
    </div>

    <div id="deptComplate" style="width: 100%;height:314px;"></div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';

  import {
  deptOnTime
  } from "@/api/order/statistics";

  export default {
    name: "deptComplate",
    data() {
      return {
        resultData : {
          deptList: [],
          onTimeList: [],
          unOnTimeList: []
        },
        queryData:{
          queryDates: [],
          queryDateStart: null,
          queryDateEnd: null
        }
      };
    },
    mounted() {
      this.getDataList();
    },
    methods: {
      getDataList(){
        this.queryData.queryDateStart = this.queryData.queryDates[0];
        this.queryData.queryDateEnd = this.queryData.queryDates[1];
        deptOnTime(this.queryData).then(res=>{
          this.resultData = res.data;
          this.initChart();
        })
      },
      async initChart() {

        //2. 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('deptComplate'));
        // const numArr = [this.data.assetNum, this.data.disposeNum, this.data.stayNum];


        //3. 指定图表的配置项和数据
        var option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {},
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [{
            type: 'category',
            data: this.resultData.deptList
          }],
          yAxis: [{
            type: 'value'
          }],
          series: [{
              name: '按时完成',
              type: 'bar',
              stack: 'Ad',
              emphasis: {
                focus: 'series'
              },
              data: this.resultData.onTimeList,
              color: '#ed702e'
            },
            {
              name: '超时完成',
              type: 'bar',
              stack: 'Ad',
              emphasis: {
                focus: 'series'
              },
              data: this.resultData.unOnTimeList,
              color: '#414eec'
            }
          ]
        };
        //4.使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

      }
    }
  };
</script>
