import request from '@/utils/request'

// 查询生产流列表
export function listFlowPool(query) {
  return request({
    url: '/productionflow/flowPool/list',
    method: 'get',
    params: query
  })
}

// 查询生产流详情
export function getFlowPool(flowId) {
  return request({
    url: `/productionflow/flowPool/${flowId}`,
    method: 'get'
  })
}

// 新增生产流
export function addFlowPool(data) {
  return request({
    url: '/productionflow/flowPool',
    method: 'post',
    data
  })
}

// 更新生产流
export function updateFlowPool(data) {
  return request({
    url: '/productionflow/flowPool',
    method: 'put',
    data
  })
}

// 删除生产流
export function removeFlowPool(flowIds) {
  return request({
    url: `/productionflow/flowPool/${flowIds}`,
    method: 'delete'
  })
}
