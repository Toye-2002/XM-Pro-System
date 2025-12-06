import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/assets/global.css'

const app = createApp(App)

// 先注册图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 再使用 Element Plus 和路由
app.use(ElementPlus, {
    locale: zhCn,
})
app.use(router)

// 添加全局错误处理
app.config.errorHandler = (err, instance, info) => {
    console.error('Vue 错误:', err)
    console.error('组件实例:', instance)
    console.error('错误信息:', info)
}

app.mount('#app')