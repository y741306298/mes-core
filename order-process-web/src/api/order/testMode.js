import request from '@/utils/request'

// 查询测试方式管理列表
export function listTestMode(query) {
  return request({
    url: '/order/testMode/list',
    method: 'get',
    params: query
  })
}

// 查询全部测试方式管理列表
export function listTestModeAll(query) {
  return request({
    url: '/order/testMode/listAll',
    method: 'get',
    params: query
  })
}

// 查询测试方式管理详细
export function getTestMode(modeId) {
  return request({
    url: '/order/testMode/' + modeId,
    method: 'get'
  })
}

// 新增测试方式管理
export function addTestMode(data) {
  return request({
    url: '/order/testMode',
    method: 'post',
    data: data
  })
}

// 修改测试方式管理
export function updateTestMode(data) {
  return request({
    url: '/order/testMode',
    method: 'put',
    data: data
  })
}

// 删除测试方式管理
export function delTestMode(modeId) {
  return request({
    url: '/order/testMode/' + modeId,
    method: 'delete'
  })
}

// 复制测试方式管理
export function copyTestMode(data) {
  return request({
    url: '/order/testMode/copy',
    method: 'post',
    data: data
  })
}