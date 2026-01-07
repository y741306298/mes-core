import request from '@/utils/request'

// 查询排版池列表
export function listComposeTypePool(query) {
  return request({
    url: '/productionflow/composeTypePool/list',
    method: 'get',
    params: query
  })
}

// 查询排版详情
export function getComposeTypePool(composeId) {
  return request({
    url: `/productionflow/composeTypePool/${composeId}`,
    method: 'get'
  })
}

// 新增排版
export function addComposeTypePool(data) {
  return request({
    url: '/productionflow/composeTypePool',
    method: 'post',
    data
  })
}

// 更新排版
export function updateComposeTypePool(data) {
  return request({
    url: '/productionflow/composeTypePool',
    method: 'put',
    data
  })
}

// 删除排版
export function removeComposeTypePool(composeIds) {
  return request({
    url: `/productionflow/composeTypePool/${composeIds}`,
    method: 'delete'
  })
}

// 删除排版流程数据
export function clearComposeProcesses(composeIds) {
  return request({
    url: `/productionflow/composeTypePool/process/${composeIds}`,
    method: 'delete'
  })
}

// 查询排版生产流列表
export function listComposeFlows(query) {
  return request({
    url: '/productionflow/composeTypePool/flow/list',
    method: 'get',
    params: query
  })
}

// 查询排版生产流详情
export function getComposeFlow(flowId) {
  return request({
    url: `/productionflow/composeTypePool/flow/${flowId}`,
    method: 'get'
  })
}

// 新增排版生产流
export function addComposeFlow(data) {
  return request({
    url: '/productionflow/composeTypePool/flow',
    method: 'post',
    data
  })
}

// 更新排版生产流
export function updateComposeFlow(data) {
  return request({
    url: '/productionflow/composeTypePool/flow',
    method: 'put',
    data
  })
}
