 import request from '@/utils/request'

 // 查询任务模板列表
 export function listTaskTemplate(query) {
   return request({
     url: '/order/taskTemplate/list',
     method: 'get',
     params: query
   })
 }

 // 查询全部任务模板列表
 export function listTaskTemplateAll(query) {
   return request({
     url: '/order/taskTemplate/listAll',
     method: 'get',
     params: query
   })
 }

 // 查询任务模板详细
 export function getTaskTemplate(templateId) {
   return request({
     url: '/order/taskTemplate/' + templateId,
     method: 'get'
   })
 }

 // 新增任务模板
 export function addTaskTemplate(data) {
   return request({
     url: '/order/taskTemplate',
     method: 'post',
     data: data
   })
 }

 // 修改任务模板
 export function updateTaskTemplate(data) {
   return request({
     url: '/order/taskTemplate',
     method: 'put',
     data: data
   })
 }

 // 删除任务模板
 export function delTaskTemplate(templateId) {
   return request({
     url: '/order/taskTemplate/' + templateId,
     method: 'delete'
   })
 }

 // 查询功能卡片列表
 export function listTaskFunctionCards(query) {
   return request({
     url: '/order/taskTemplate/functionCard/list',
     method: 'get',
     params: query
   })
 }
