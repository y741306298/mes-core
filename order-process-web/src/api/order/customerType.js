import request from '@/utils/request'

// 查询客户类型管理列表
export function listCustomerType(query) {
  return request({
    url: '/order/customerType/list',
    method: 'get',
    params: query
  })
}

// 查询全部客户类型管理列表
export function listCustomerTypeAll(query) {
  return request({
    url: '/order/customerType/listAll',
    method: 'get',
    params: query
  })
}

// 查询客户类型管理详细
export function getCustomerType(typeId) {
  return request({
    url: '/order/customerType/' + typeId,
    method: 'get'
  })
}

// 新增客户类型管理
export function addCustomerType(data) {
  return request({
    url: '/order/customerType',
    method: 'post',
    data: data
  })
}

// 修改客户类型管理
export function updateCustomerType(data) {
  return request({
    url: '/order/customerType',
    method: 'put',
    data: data
  })
}

// 删除客户类型管理
export function delCustomerType(typeId) {
  return request({
    url: '/order/customerType/' + typeId,
    method: 'delete'
  })
}

// 复制客户类型管理
export function copyCustomerType(data) {
  return request({
    url: '/order/customerType/copy',
    method: 'post',
    data: data
  })
}