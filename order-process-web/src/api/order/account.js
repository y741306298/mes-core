import request from '@/utils/request'

// 查询账户类型列表
export function listAccount(query) {
  return request({
    url: '/order/account/list',
    method: 'get',
    params: query
  })
}

// 查询全部账户类型列表
export function listAccountAll(query) {
  return request({
    url: '/order/account/listAll',
    method: 'get',
    params: query
  })
}

// 查询账户类型详细
export function getAccount(accountId) {
  return request({
    url: '/order/account/' + accountId,
    method: 'get'
  })
}

// 新增账户类型
export function addAccount(data) {
  return request({
    url: '/order/account',
    method: 'post',
    data: data
  })
}

// 修改账户类型
export function updateAccount(data) {
  return request({
    url: '/order/account',
    method: 'put',
    data: data
  })
}

// 删除账户类型
export function delAccount(accountId) {
  return request({
    url: '/order/account/' + accountId,
    method: 'delete'
  })
}

// 复制账户类型
export function copyAccount(data) {
  return request({
    url: '/order/account/copy',
    method: 'post',
    data: data
  })
}