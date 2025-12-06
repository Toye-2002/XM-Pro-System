<template>
  <div class="login-container">
    <div class="login-box">
      <div style="padding:50px 30px; background-color: white; margin-left: 100px; border-radius: 5px;box-shadow: 0 0 10px rgba(0,0,0,0.2)">
        <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 330px">
          <div style="margin-bottom: 30px; font-size:24px; text-align: center; color: #0742b1; font-weight: bold ">欢迎登录后台管理系统</div>
          <el-form-item prop="username">
            <el-input size="large" v-model="data.form.username" placeholder="请输入账号" prefix-icon="User"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password size="large" v-model="data.form.password" placeholder="请输入密码" prefix-icon="lock"></el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-select  v-model="data.form.role" style="width:  100%" size="large" >
              <el-option value="ADMIN" label="管理员"></el-option>
              <el-option value="EMP" label="员工"></el-option>
            </el-select>
          </el-form-item>
          <div style="margin-bottom: 20px">
            <el-button @click="login" size="large" style="width: 100%" type="primary">登 录</el-button>
          </div>
          <div style="text-align: right">还没有账号？请<a style="color: #0742b1;text-decoration: none" href="/register">注册</a></div>
        </el-form>
      </div>
    </div>
  </div>
</template>


<script setup>
import request from '@/utils/request';
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";

const data = reactive({
  form:{role:'ADMIN'},
  rules:{
    username: [
      {required: true,message: '请输入账号', trigger: 'blur'}
    ],
    password: [
      {required: true,message: '请输入密码', trigger: 'blur'}
    ]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate((valid) => {
    if(valid) {
      request.post('/login',data.form).then(res => {
        if(res.code === '200') {
          // 存储后台返回的用户数据信息
          localStorage.setItem('xm-pro-user',JSON.stringify(res.data))
          ElMessage.success("登录成功")
          setTimeout(() => {
            location.href = '/manager/home'
          },500)
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}


</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/bg.jpeg");
  background-size: cover ;
  background-position: 0 -50px;
}

.login-box {
  width: 50%;
  height: 100%;
  display: flex;
  align-items: center;
  right: 0;
  position: absolute;
}
</style>