<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 240px; margin-right:10px" v-model="data.name" placeholder="请输入名字查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" round @click="load">查 询</el-button>
      <el-button type="primary" round @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="success" round @click="handleAdd">新 增</el-button>
      <el-button type="warning" round @click="delBatch">批量删除</el-button>
      <el-upload
          style="display: inline-block; margin: 0 10px"
          action="http://localhost:9090/employee/import"
          :show-file-list="false"
          :on-success="importSuccess"
      >
        <el-button type="success" round >导 入</el-button>
      </el-upload>
      <el-button type="warning" round @click="exportData" >导 出</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="账号" prop="username" width="120" align="center" />
        <el-table-column label="头像" >
          <template #default="scope">
            <img v-if="scope.row.avatar" :src="scope.row.avatar" alt="" style="width: 40px; display: block; height: 40px; border-radius: 50%">
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="name" width="120" align="center" />
        <el-table-column label="性别" prop="sex" width="120" align="center" />
        <el-table-column label="工号" prop="no" width="120" align="center" />
        <el-table-column label="年龄" prop="age" width="120" align="center" />
        <el-table-column label="个人介绍" prop="descr" min-width="100" show-overflow-tooltip align="center" />
        <el-table-column label="部门" prop="departmentName" width="170" align="center" />
        <el-table-column label="操作" width="210" align="center">
          <template #default="scope">
            <el-button @click="handleUpdate(scope.row)" type="primary" :icon="Edit" circle></el-button>
            <el-button @click="del(scope.row.id)" type="danger" :icon="Delete" circle></el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 15px">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="[5,10,15,20]"
            :background="true"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.total"
        />
      </div>
    </div>

    <el-dialog title="员工信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
        <el-form-item label="账号" prop="username">
          <el-input :disabled="data.form.id" v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="部门">
          <el-select style="width: 100%" v-model="data.form.departmentId">
            <el-option v-for="item in data.departmentList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              action="http://localhost:9090/files/upload"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="data.form.sex">
            <el-radio value="男" label="男"></el-radio>
            <el-radio value=女 label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="工号" prop="no">
          <el-input v-model="data.form.no" autocomplete="off" placeholder="请输入工号"/>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number style="width:180px" :min="18" v-model="data.form.age"  autocomplete="off" placeholder="请输入年龄"/>
        </el-form-item>
        <el-form-item label="个人介绍">
          <el-input :rows="3" type="textarea" v-model="data.form.descr" autocomplete="off" placeholder="请输入个人介绍"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {Delete, Edit, Search} from "@element-plus/icons-vue";
import request from '@/utils/request';
import {ElMessage, ElMessageBox} from "element-plus";


const data = reactive({
  name: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  ids: [],
  departmentList: [],
  rules: {
    username:[
      {required: true, message: '请输入账号', trigger: 'blur'}
    ],
    name:[
      {required: true, message: '请输入姓名', trigger: 'blur'}
    ],
    no:[
      {required: true, message: '请输入工号', trigger: 'blur'}
    ]
  }
})

// 导出数据, 是通过流的形式下载的 excel
const exportData = () => {
  // 打开流文件的链接，浏览器会自动帮我们下载文件
  window.open('http://localhost:9090/employee/export')
}

// 导出数据
const importSuccess = () => {
  ElMessage.success("导入数据成功")
  load()
}

// 表单规则
const formRef = ref()

request.get('/department/selectAll').then(res => {
  data.departmentList = res.data
})

// 加载函数（分页功能和查询功能）
const load = () => {
  request.get('/employee/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total || 0 // 确保设置总条数
  })
}

// 调用方法
load()

// 页大小改变事件
const handleSizeChange = (newSize) => {
  data.pageSize = newSize
  data.pageNum = 1 // 切换页大小时通常回到第一页
  load()
}

// 当前页改变事件
const handleCurrentChange = (newPage) => {
  data.pageNum = newPage
  load()
}

// 重置事件
const reset = () => {
  data.name = null
  load()
}

// 增加员工，填写信息
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

// 保存员工信息，可能是增加员工也可能是修改员工信息
const save = () => {
  // 表单校验逻辑
  formRef.value.validate((valid) => {
    if(valid) {
      data.form.id ? update() : add()
    }
  })
}

// 更新,  区别在于更新的对象是有  id
const update = () => {
  request.put('/employee/update',data.form).then(res => {
    if(res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()  // 更新后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 新增, 区别在于新增的对象是没有  id
const add = () => {
  request.post('/employee/add',data.form).then(res => {
    if(res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()  // 新增后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}


// 编辑数据
const handleUpdate = (row) => {
  data.formVisible = true
  data.form = JSON.parse(JSON.stringify(row))    // 深拷贝一个新的对象 用于编辑
}

// 删除单个数据
const del = (id) => {
  ElMessageBox.confirm('删除该数据将无法恢复','确认删除', {type:'warning'}).then(() => {
    request.delete('/employee/deleteById/' + id).then(res => {
      if(res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

// 选中勾选的行数组id组成一个新的数组
const handleSelectionChange = (rows) => {
  // 从选中的行数组里面去除所有行的id组成一个数组
  data.ids =  rows.map(row => row.id)
  console.log(data.ids)
}

// 删除多行数据
const delBatch = () => {
  if(data.ids.length === 0) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除该数据将无法恢复','确认删除', {type:'warning'}).then(() => {
    request.delete('/employee/deleteBatch',{data: data.ids}).then(res => {
      if(res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleAvatarSuccess = (res) => {
  data.form.avatar = res.data
}
</script>

