import request from '@/utils/request'

// 查询订单子流程列表
export function listOrderChildProcess(query) {
  return request({
    url: '/order/orderChildProcess/list',
    method: 'get',
    params: query
  })
}

// 查询全部订单子流程列表
export function listOrderChildProcessAll(query) {
  return request({
    url: '/order/orderChildProcess/listAll',
    method: 'get',
    params: query
  })
}

// 查询订单子流程详细
export function getOrderChildProcess(childId) {
  return request({
    url: '/order/orderChildProcess/' + childId,
    method: 'get'
  })
}

// 新增订单子流程
export function addOrderChildProcess(data) {
  return request({
    url: '/order/orderChildProcess',
    method: 'post',
    data: data
  })
}

// 修改订单子流程
export function updateOrderChildProcess(data) {
  return request({
    url: '/order/orderChildProcess',
    method: 'put',
    data: data
  })
}

// 删除订单子流程
export function delOrderChildProcess(childId) {
  return request({
    url: '/order/orderChildProcess/' + childId,
    method: 'delete'
  })
}

// 复制订单子流程
export function copyOrderChildProcess(data) {
  return request({
    url: '/order/orderChildProcess/copy',
    method: 'post',
    data: data
  })
}