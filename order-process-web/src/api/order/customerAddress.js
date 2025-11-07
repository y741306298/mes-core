import request from '@/utils/request'

// 查询用户地址列表
export function listCustomerAddress(query) {
  return request({
    url: '/order/customerAddress/list',
    method: 'get',
    params: query
  })
}

// 查询全部用户地址列表
export function listCustomerAddressAll(query) {
  return request({
    url: '/order/customerAddress/listAll',
    method: 'get',
    params: query
  })
}

// 查询用户地址详细
export function getCustomerAddress(addressId) {
  return request({
    url: '/order/customerAddress/' + addressId,
    method: 'get'
  })
}

// 新增用户地址
export function addCustomerAddress(data) {
  return request({
    url: '/order/customerAddress',
    method: 'post',
    data: data
  })
}

// 修改用户地址
export function updateCustomerAddress(data) {
  return request({
    url: '/order/customerAddress',
    method: 'put',
    data: data
  })
}

// 删除用户地址
export function delCustomerAddress(addressId) {
  return request({
    url: '/order/customerAddress/' + addressId,
    method: 'delete'
  })
}

// 复制用户地址
export function copyCustomerAddress(data) {
  return request({
    url: '/order/customerAddress/copy',
    method: 'post',
    data: data
  })
}