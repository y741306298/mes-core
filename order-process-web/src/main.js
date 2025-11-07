import Vue from 'vue'
import VueTemplateCompiler from 'vue-template-compiler';
import Print from 'vue-print-nb'
//引入ECharts
import echarts from 'echarts';

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import {
  download,
  urlToFile
} from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import {
  getDicts
} from "@/api/system/dict/data";
import {
  getConfigKey
} from "@/api/system/config";
import {
  parseTime,
  resetForm,
  addDateRange,
  selectDictLabel,
  selectDictLabels,
  handleTree,
  dragControllerDiv,
  dragChangeHeight,
  refreshCloumn,
  getObjAttr,
  formDetection,
  getFileNamesFromUrl
} from "@/utils/ruoyi";
import directives from '@/utils/directives.js'
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload/index"
import FileUploadButton from "@/components/FileUpload/fileButton"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'
import RecursiveSubMenu from '@/components/TopNav/RecursiveSubMenu'
import FileList from '@/components/FileList/index'
import FormFieldList from '@/views/order/field/formFieldList'
//分屏组件
import {
  Splitpanes,
  Pane
} from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.urlToFile = urlToFile
Vue.prototype.handleTree = handleTree
Vue.prototype.dragControllerDiv = dragControllerDiv
Vue.prototype.dragChangeHeight = dragChangeHeight
Vue.prototype.refreshCloumn = refreshCloumn
Vue.prototype.getObjAttr = getObjAttr
Vue.prototype.formDetection = formDetection
Vue.prototype.getFileNamesFromUrl = getFileNamesFromUrl
Vue.prototype.$compiler = VueTemplateCompiler;
Vue.prototype.$echarts = echarts


// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('FileUploadButton', FileUploadButton)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)
Vue.component('RecursiveSubMenu', RecursiveSubMenu)
Vue.component('Splitpanes', Splitpanes)
Vue.component('Pane', Pane)
Vue.component('FileList', FileList)
Vue.component('FormFieldList', FormFieldList)


Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(Print)
DictData.install()

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
