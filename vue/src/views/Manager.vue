<template>
  <div>
<!--    页面头部开始-->
    <div style="height: 60px; background-color:#3c7fff; display: flex; align-items: center">
      <div style="width: 200px; display: flex; align-items: center; padding-left: 15px">
        <img style="width: 40px" src="@/assets/data-log.png" alt="">
        <span style="font-size: 24px; color: white ">后台管理系统</span>
      </div>
      <div style="flex: 1"></div>
      <div style="width: fit-content; display: flex; align-items: center; padding-right:  10px">
        <img :src="data.user.avatar||'@/assets/user.png'" alt="" style="width: 40px; height: 40px; border-radius: 50%">
        <span style="color: white;margin-left: 5px">{{ data.user?.name||'用户' }}</span>
      </div>
    </div>
<!--    页面头部结束-->

<!--    下面部分开始-->
    <div style="display: flex">
<!--      左侧导航栏菜单开始-->
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1']" style="border:0">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>系统首页
          </el-menu-item>
          <el-menu-item index="/manager/data">
            <el-icon><DataAnalysis /></el-icon>数据统计
          </el-menu-item>
          <el-menu-item index="/manager/article">
            <el-icon><Document /></el-icon>文章管理
          </el-menu-item>
          <el-menu-item index="/manager/department">
            <el-icon><OfficeBuilding /></el-icon>部门管理
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/employee">员工信息</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/manager/person">
            <el-icon><UserFilled /></el-icon>个人信息
          </el-menu-item>
          <el-menu-item index="/manager/password">
            <el-icon><Lock /></el-icon>修改密码
          </el-menu-item>
          <el-menu-item @click="logout">
            <el-icon><SwitchButton /></el-icon>退出登录
          </el-menu-item>
        </el-menu>
      </div>
<!--      左侧导航栏结束-->

<!--      右侧主体区开始-->
      <div style="flex:1; width: 0;background-color:#f5f7ff ;padding:10px">
              <!--   @updateUser="updateUser" 该数据是从Person中存储的     -->
        <RouterView @updateUser="updateUser" />
      </div>
<!--      右侧主体区结束-->


    </div>
<!--    下面部分结束-->

  </div>
</template>


<script setup>
import router from "@/router/index.js";
import {reactive} from "vue";
import {DataAnalysis, Document, House, Lock, SwitchButton, User, UserFilled} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-pro-user'))
})

const logout = () => {
  localStorage.removeItem('xm-pro-user')  // 清除当前登录的用户缓存数据
  location.href = '/login'  // 退出到登录页面
}

// 从缓存中获取数据
const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-pro-user'))
}
</script>




<style>
.el-menu .is-active {
  background-color: #e6ecf7 !important ;
}

.el-sub-menu__title {
  background-color: white !important ;
}
</style>