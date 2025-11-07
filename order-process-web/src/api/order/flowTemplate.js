import request from '@/utils/request'

// 查询流程模板列表
export function listFlowTemplate(query) {
  return request({
    url: '/order/flowTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询全部流程模板列表
export function listFlowTemplateAll(query) {
  return request({
    url: '/order/flowTemplate/listAll',
    method: 'get',
    params: query
  })
}

// 查询流程模板详细
export function getFlowTemplate(templateId) {
  return request({
    url: '/order/flowTemplate/' + templateId,
    method: 'get'
  })
}

// 新增流程模板
export function addFlowTemplate(data) {
  return request({
    url: '/order/flowTemplate',
    method: 'post',
    data: data
  })
}

// 修改流程模板
export function updateFlowTemplate(data) {
  return request({
    url: '/order/flowTemplate',
    method: 'put',
    data: data
  })
}

// 删除流程模板
export function delFlowTemplate(templateId) {
  return request({
    url: '/order/flowTemplate/' + templateId,
    method: 'delete'
  })
}

// 复制流程模板
export function copyFlowTemplate(data) {
  return request({
    url: '/order/flowTemplate/copy',
    method: 'post',
    data: data
  })
}