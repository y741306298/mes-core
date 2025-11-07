import request from '@/utils/request'

// 查询节点字段列表
export function listFlowNodeField(query) {
  return request({
    url: '/order/flowNodeField/list',
    method: 'get',
    params: query
  })
}

// 查询全部节点字段列表
export function listFlowNodeFieldAll(query) {
  return request({
    url: '/order/flowNodeField/listAll',
    method: 'get',
    params: query
  })
}

// 查询节点字段详细
export function getFlowNodeField(fieldId) {
  return request({
    url: '/order/flowNodeField/' + fieldId,
    method: 'get'
  })
}

// 新增节点字段
export function addFlowNodeField(data) {
  return request({
    url: '/order/flowNodeField',
    method: 'post',
    data: data
  })
}

// 修改节点字段
export function updateFlowNodeField(data) {
  return request({
    url: '/order/flowNodeField',
    method: 'put',
    data: data
  })
}

// 删除节点字段
export function delFlowNodeField(fieldId) {
  return request({
    url: '/order/flowNodeField/' + fieldId,
    method: 'delete'
  })
}

// 复制节点字段
export function copyFlowNodeField(data) {
  return request({
    url: '/order/flowNodeField/copy',
    method: 'post',
    data: data
  })
}