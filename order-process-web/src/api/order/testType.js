import request from '@/utils/request'

// 查询测试类型管理列表
export function listTestType(query) {
  return request({
    url: '/order/testType/list',
    method: 'get',
    params: query
  })
}

// 查询全部测试类型管理列表
export function listTestTypeAll(query) {
  return request({
    url: '/order/testType/listAll',
    method: 'get',
    params: query
  })
}

// 查询测试类型管理详细
export function getTestType(typeId) {
  return request({
    url: '/order/testType/' + typeId,
    method: 'get'
  })
}

// 新增测试类型管理
export function addTestType(data) {
  return request({
    url: '/order/testType',
    method: 'post',
    data: data
  })
}

// 修改测试类型管理
export function updateTestType(data) {
  return request({
    url: '/order/testType',
    method: 'put',
    data: data
  })
}

// 删除测试类型管理
export function delTestType(typeId) {
  return request({
    url: '/order/testType/' + typeId,
    method: 'delete'
  })
}

// 复制测试类型管理
export function copyTestType(data) {
  return request({
    url: '/order/testType/copy',
    method: 'post',
    data: data
  })
}