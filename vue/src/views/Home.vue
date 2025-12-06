<template>
  <div style="padding: 20px">
    <!-- 欢迎区域 -->
    <el-card style="margin-bottom: 20px; text-align: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white">
      <h1 style="margin: 0">欢迎使用员工管理系统</h1>
      <p style="margin: 10px 0 0; opacity: 0.9">高效管理员工信息，简化工作流程</p>
    </el-card>

    <!-- 功能卡片 -->
    <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 20px">
      <el-card shadow="hover" style="text-align: center; cursor: pointer" @click="router.push('/manager/employee')">
        <div style="font-size: 48px; color: #409EFF; margin-bottom: 10px">
          <i class="el-icon-user-solid"></i>
        </div>
        <h3 style="margin: 0; color: #303133">员工管理</h3>
        <p style="color: #606266; margin: 10px 0">管理员工基本信息、部门分配</p>
      </el-card>

      <el-card shadow="hover" style="text-align: center; cursor: pointer" @click="router.push('/manager/article')">
        <div style="font-size: 48px; color: #67C23A; margin-bottom: 10px">
          <i class="el-icon-document"></i>
        </div>
        <h3 style="margin: 0; color: #303133">文章管理</h3>
        <p style="color: #606266; margin: 10px 0">发布和管理公司文章通知</p>
      </el-card>

      <el-card shadow="hover" style="text-align: center; cursor: pointer" @click="router.push('/manager/person')">
        <div style="font-size: 48px; color: #E6A23C; margin-bottom: 10px">
          <i class="el-icon-s-custom"></i>
        </div>
        <h3 style="margin: 0; color: #303133">个人信息</h3>
        <p style="color: #606266; margin: 10px 0">查看和修改个人资料</p>
      </el-card>

      <el-card shadow="hover" style="text-align: center; cursor: pointer" @click="router.push('/manager/password')">
        <div style="font-size: 48px; color: #F56C6C; margin-bottom: 10px">
          <i class="el-icon-lock"></i>
        </div>
        <h3 style="margin: 0; color: #303133">修改密码</h3>
        <p style="color: #606266; margin: 10px 0">定期更新账户密码</p>
      </el-card>
    </div>

    <!-- 数据统计图表 -->
    <div style="display: flex; gap: 10px; margin-bottom: 20px">
      <div class="card" id="pieChart" style="padding: 20px; flex: 1; height: 400px"></div>
      <div class="card" id="lineChart" style="padding: 20px; flex: 1; height: 400px"></div>
    </div>

    <!-- 系统信息 -->
    <el-card>
      <template #header>
        <span style="font-weight: bold">系统信息</span>
      </template>
      <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; text-align: center">
        <div>
          <div style="font-size: 24px; color: #409EFF; font-weight: bold">{{ stats.employeeCount }}</div>
          <div style="color: #606266">员工总数</div>
        </div>
        <div>
          <div style="font-size: 24px; color: #67C23A; font-weight: bold">{{ stats.articleCount }}</div>
          <div style="color: #606266">文章总数</div>
        </div>
        <div>
          <div style="font-size: 24px; color: #E6A23C; font-weight: bold">{{ stats.departmentCount }}</div>
          <div style="color: #606266">部门数量</div>
        </div>
      </div>
    </el-card>

    <!-- 快速导航 -->
    <el-card style="margin-top: 20px">
      <template #header>
        <span style="font-weight: bold">快速导航</span>
      </template>
      <div style="display: flex; gap: 10px; flex-wrap: wrap">
        <el-button type="primary" @click="router.push('/manager/employee')">
          <i class="el-icon-user"></i>
          员工列表
        </el-button>
        <el-button type="warning" @click="router.push('/manager/article')">
          <i class="el-icon-document"></i>
          文章列表
        </el-button>
        <el-button @click="router.push('/manager/person')">
          <i class="el-icon-s-custom"></i>
          个人中心
        </el-button>
        <el-button type="success" @click="router.push('/manager/data')">
          <i class="el-icon-data-analysis"></i>
          详细统计
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import * as echarts from 'echarts';
import request from '@/utils/request';

const router = useRouter()

const stats = reactive({
  employeeCount: 0,
  articleCount: 0,
  departmentCount: 0
})

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

// 饼图配置
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
          shadowColor: 'rgba(255, 69, 0, 0.6)'
        }
      },
      itemStyle: {
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
  loadStats()
  initCharts()
})

const loadStats = async () => {
  try {
    // 使用现有接口获取数据
    const employeeRes = await request.get('/employee/selectAll')
    const articleRes = await request.get('/article/selectAll')

    if (employeeRes.data) {
      stats.employeeCount = employeeRes.data.length
      // 计算部门数量（去重）
      const departments = new Set(employeeRes.data.map(emp => emp.departmentName))
      stats.departmentCount = departments.size
    }

    if (articleRes.data) {
      stats.articleCount = articleRes.data.length
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    // 设置默认值
    stats.employeeCount = 0
    stats.articleCount = 0
    stats.departmentCount = 0
  }
}

const initCharts = () => {
  // 1、饼图的实例
  const pieChart = echarts.init(document.getElementById('pieChart'));
  request.get('/barData').then(res => {
    // 将柱状图数据转换为饼图数据格式
    const pieData = res.data.department.map((department, index) => ({
      value: res.data.count[index],
      name: department
    }))
    pieOption.series[0].data = pieData
    pieChart.setOption(pieOption)
  })

  // 2、折线图的实例
  const lineChart = echarts.init(document.getElementById('lineChart'));
  request.get('/lineData').then(res => {
    lineOption.xAxis.data = res.data.date
    lineOption.series[0].data = res.data.count
    lineChart.setOption(lineOption)
  })

  // 响应窗口大小变化
  window.addEventListener('resize', () => {
    pieChart.resize()
    lineChart.resize()
  })
}
</script>

<style scoped>
.el-card {
  border-radius: 8px;
  transition: all 0.3s;
}

.el-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>