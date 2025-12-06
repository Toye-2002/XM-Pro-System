<template>
  <div>
   <div style="display: flex; grid-gap: 10px">
     <div  class="card" id="bar" style="padding: 20px; flex: 1; height: 400px"></div>
     <div  class="card" id="line" style="padding: 20px; flex: 1;  height: 400px"></div>
   </div>

    <div style="display: flex; grid-gap: 10px; margin-top: 10px">
      <div  class="card" id="pie" style="padding: 20px; width: 50%; height: 400px"></div>

    </div>
  </div>
</template>


<script setup>
import {reactive,onMounted} from "vue";
import * as echarts from 'echarts';
import request from '@/utils/request';

// 柱状图配置
const barOption = {
  title: {
    text: '各部门员工数量'
  },
  tooltip: {},
  legend: {
    data: ["人数"],
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '人数',
      type: 'bar',
      data: [],
      itemStyle: {
        color: function (params) {
          let colors = ['#5470c6','#91cc75','#fac858','#ee6666','#73c0de','#3ba272','#fc8452','#9a60b4','#ea7ccc']
          return colors[params.dataIndex % colors.length]
        }
      }
    }
  ]
};

// 折线图配置
const lineOption = {
  title: {
    text: '近7天的发布数量'
  },
  tooltip: {},
  legend: {
    trigger: 'item'
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '发布文章的数量',
      type: 'line',
      data: [],
      smooth: true
    }
  ]
};

// 饼图实例
const pieOption = {
  title: {
    text: '各部门员工数量比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '员工数量',
      type: 'pie',
      radius: '50%',
      data: [],
      label: {
        formatter: '比例 : {d}%'
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(255, 69, 0, 0.6)'  // 鲜艳的红橙色阴影
        }
      },
      itemStyle: {
        // 使用鲜艳的颜色数组
        color: function(params) {
          const colors = [
            '#DDA0DD', '#F8C471', '#82E0AA', '#F1948A', '#85C1E9', '#D7BDE2',
            '#F9E79F', '#A9DFBF', '#F5B7B1', '#AED6F1', '#E8DAEF'
          ];
          return colors[params.dataIndex % colors.length];
        },
      }
    }
  ]
};

onMounted(() => {
  // 1、柱状图的实例
  const barChart = echarts.init(document.getElementById('bar'));
  request.get('/barData').then(res => {
    barOption.xAxis.data = res.data.department
    barOption.series[0].data = res.data.count
    barChart.setOption(barOption)
  })

  // 2、折线图的实例
  const lineChart = echarts.init(document.getElementById('line'));
  request.get('/lineData').then(res => {
    lineOption.xAxis.data = res.data.date
    lineOption.series[0].data = res.data.count
    lineChart.setOption(lineOption)
  })

  // 3、饼图的实例
  const pieChart = echarts.init(document.getElementById('pie'));
  request.get('/barData').then(res => {
    pieOption.series[0].data = res.data.count
    pieChart.setOption(pieOption)
  })
})
</script>

