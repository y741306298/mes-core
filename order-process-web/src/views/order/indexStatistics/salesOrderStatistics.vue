<template>
  <div class="statistics-main">
    <div class="formHeader">
      <div class="formTitle">销售统计</div>
      <div>
        <el-select class="mr10" size="mini" style="width: 100px;" v-model="queryData.userId" @change="getData">
          <el-option
            v-for="item in userList"
            :key="item.userId"
            :label="item.userName"
            :value="item.userId">
          </el-option>
        </el-select>
        <el-date-picker value-format="yyyy-MM" size="mini" type="month" placeholder="选择月" style="width: 100px;" v-model="queryData.queryDate" @change="getData"/>
      </div>
    </div>

    <div id="salesOrderStatistics" style="width: 100%;height:314px;"></div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';

  import {
  salesStatistics
  } from "@/api/order/statistics";

  import {
    listAllUser
  } from "@/api/system/user";

  export default {
    name: "salesOrderStatistics",
    data() {
      return {
        userList: [],
        queryData:{
          userId: null,
          queryDate: null
        },
        dataList:[],
        data: []
      };
    },
    mounted() {
      this.getListAllUser();
      this.getData();
      
    },
    methods: {

      getData(){
        salesStatistics(this.queryData).then(res=>{
          this.data = res.data;
          this.initChart();
        })
        
      },

      getListAllUser(){
        listAllUser().then(res=>{
          this.userList = res.data;
        })
      },

      async initChart() {

        //2. 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('salesOrderStatistics'));
        // const numArr = [this.data.assetNum, this.data.disposeNum, this.data.stayNum];
       
        // let data = [
        // ];

        // await salesStatistics(this.queryData).then(res=>{
        //   this.data = res.data;
        // })
        //3. 指定图表的配置项和数据
        var option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '35%',
            left: 'left',
            orient: 'vertical',
          },
          series: [{
            name: '销售统计',
            type: 'pie',
            radius: ['40%', '70%'],
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            // label: {
            //   show: false,
            //   position: 'center'
            // },
            // emphasis: {
            //   itemStyle: {
            //     shadowBlur: 10,
            //     shadowOffsetX: 0,
            //     shadowColor: 'rgba(0, 0, 0, 0.5)'
            //   }
            // },
            label: {
              alignTo: 'edge',
              formatter: '{name|{b}}\n{time|{c} 万元}',
              // minMargin: 5,
              // edgeDistance: 5,
              lineHeight: 15,
              rich: {
                time: {
                  fontSize: 13,
                  color: '#999'
                }
              }
            },
            labelLine: {
              length: 20,
              length2: 0,
              // maxSurfaceAngle: 80
            },
            data: this.data
          }]
        };
        //4.使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

      }
    }
  };
</script>
