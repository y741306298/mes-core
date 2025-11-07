import request from '@/utils/request'

// 查询模板节点列表
export function listFlowNode(query) {
  return request({
    url: '/order/flowNode/list',
    method: 'get',
    params: query
  })
}

// 查询全部模板节点列表
export function listFlowNodeAll(query) {
  return request({
    url: '/order/flowNode/listAll',
    method: 'get',
    params: query
  })
}

// 查询模板节点详细
export function getFlowNode(nodeId) {
  return request({
    url: '/order/flowNode/' + nodeId,
    method: 'get'
  })
}

// 新增模板节点
export function addFlowNode(data) {
  return request({
    url: '/order/flowNode',
    method: 'post',
    data: data
  })
}

// 修改模板节点
export function updateFlowNode(data) {
  return request({
    url: '/order/flowNode',
    method: 'put',
    data: data
  })
}

// 删除模板节点
export function delFlowNode(nodeId) {
  return request({
    url: '/order/flowNode/' + nodeId,
    method: 'delete'
  })
}

// 复制模板节点
export function copyFlowNode(data) {
  return request({
    url: '/order/flowNode/copy',
    method: 'post',
    data: data
  })
}