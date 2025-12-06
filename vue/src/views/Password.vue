<template>
  <div class="card" style="width: 50%; padding: 40px 20px">
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="100px" style="padding-right:40px; padding-top: 20px">
      <el-form-item label="原密码" prop="password">
        <el-input v-model="data.form.password" autocomplete="off" placeholder="请输入原密码"/>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input show-password v-model="data.form.newPassword" autocomplete="off" placeholder="请设置新密码"/>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input show-password v-model="data.form.confirmPassword" autocomplete="off" placeholder="请再次确认新密码"/>
      </el-form-item>
      <div style="text-align: center">
        <el-button @click="updatePassword" type="primary" style="padding: 20px 30px">立即修改</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>


import {reactive, ref} from "vue";
import request from '@/utils/request';
import {ElMessage} from "element-plus";

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次确认新密码'))
  } else if (value !== data.form.newPassword) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const formRef = ref()
const data = reactive({
  form: {},
  user: JSON.parse(localStorage.getItem('xm-pro-user') ),
  rules: {
    password:[
      {required: true, message: '请输入原密码', trigger: 'blur'}
    ],
    newPassword:[
      {required: true, message: '请设置新密码', trigger: 'blur'}
    ],
    confirmPassword:[
      {validator: validatePass, message: '请再次确认新密码', trigger: 'blur'}
    ],
  }
})

// 先判断，再进行修改密码
const updatePassword = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      ElMessage.error('请正确填写表单')
      return
    }

    // 修复5：确保发送给后端的字段名正确
    const submitData = {
      id: data.user.id,
      role: data.user.role,
      password: data.form.password,  // 原密码
      newPassword: data.form.newPassword  // 新密码
    }

    request.put('/updatePassword', submitData).then(res => {
      if(res.code === '200'){
        ElMessage.success("修改成功")
        localStorage.removeItem('xm-pro-user')
        setTimeout(() => {
          location.href = '/login'
        }, 500)
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(error => {
      ElMessage.error('修改失败')
    })
  })
}
</script>