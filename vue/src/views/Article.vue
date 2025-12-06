<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 240px; margin-right:10px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" round @click="load">查 询</el-button>
      <el-button type="primary" round @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="success" round @click="handleAdd">新 增</el-button>
      <el-button type="warning" round @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column type="selection" align="center" />
        <el-table-column label="标题" prop="title"  />
        <el-table-column label="封面" >
          <template #default="scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list=[scope.row.img] preview-teleported  style="width: 100px; display: block; height: 60px;"/>
          </template>
        </el-table-column>
        <el-table-column label="简介" prop="description" show-overflow-tooltip />
        <el-table-column label="内容" >  <!-- 添加这个列 -->
          <template #default="scope">
            <el-button type="primary" @click="view(scope.row.content)">查看内容</el-button>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" prop="time" />
        <el-table-column label="操作" align="center">
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

    <el-dialog title="文章信息" v-model="data.formVisible" width="800px" destroy-on-close>
      <el-form ref="formRef"  :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
        <el-form-item label="标题" prop="title">
          <el-input  v-model="data.form.title" autocomplete="off" placeholder="请输入标题"/>
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
              action="http://localhost:9090/files/upload"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" :row="3" v-model="data.form.description" autocomplete="off" placeholder="请输入简介"/>
        </el-form-item>
        <el-form-item label="内容" prop="description">
          <div style="border: 1px solid #ccc;width:100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden"
                v-model="data.form.content"
                :mode="mode"
                :default-config="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="内容" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="editor-content-view" style="padding:20px" v-html="data.content"></div>
      <template #footer>  <!-- 添加正确的slot -->
        <span class="dialog-footer">
      <el-button @click="data.viewVisible=false">关 闭</el-button>
    </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive, ref,onBeforeUnmount,shallowRef} from "vue";
import {Delete, Edit, Search} from "@element-plus/icons-vue";
import request from '@/utils/request';
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const formRef = ref()

const data = reactive({
  title: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  ids: [],
  content: null,
  viewVisible: false
})

// 查看内容
const view = (content) => {
  data.content = content
  data.viewVisible = true
}

// 编辑富文本
const editContent = (row) => {
  data.formContentVisible = true
  data.form = row
}

const editorRef = shallowRef()  // 编辑器示例

const mode = 'default'

const editorConfig = {
  MENU_CONF: {
    uploadImage: {
      server: 'http://localhost:9090/files/wang/upload',
      fieldName: 'file',
      maxFileSize: 10 * 1024 * 1024,
      allowedFileTypes: ['image/*'],
    }
  }
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if(editor == null) return
  editor.destroy()
})

// 记录 editor 实例
const handleCreated = (editor) => {
  editorRef.value = editor
}

// 保存富文本内容
const saveContent = () => {
  data.formContentVisible = false
}


// 加载函数（分页功能和查询功能）
const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
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
  data.title = null
  load()
}

// 增加员工，填写信息
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

// 保存员工信息，可能是增加员工也可能是修改员工信息
const save = () => {
  data.form.id ? update() : add()
}

// 更新,  区别在于更新的对象是有  id
const update = () => {
  request.put('/article/update',data.form).then(res => {
    if(res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 新增, 区别在于新增的对象是没有  id
const add = () => {
  request.post('/article/add',data.form).then(res => {
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
    request.delete('/article/deleteById/' + id).then(res => {
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
}

// 删除多行数据
const delBatch = () => {
  if(data.ids.length === 0) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除该数据将无法恢复','确认删除', {type:'warning'}).then(() => {
    request.delete('/article/deleteBatch',{data: data.ids}).then(res => {
      if(res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

// 文件上传,回调函数
const handleImgSuccess = (res) => {
  data.form.img = res.data
}
</script>

