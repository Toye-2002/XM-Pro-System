<template>
  <div style="text-align: center">
    <img src="@/assets/404.png" alt="">
    <div>
      <el-button type="primary" @click="goToHome">返回主页</el-button>
    </div>
  </div>
</template>

<script setup>

import router from "@/router/index.js";

const goToHome = () => {
  router.push(`/manager/home`)
}


</script>

