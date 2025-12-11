import request from '@/utils/request'

// 查询订单池列表
export function listOrderPool(query) {
  return request({
    url: '/productionflow/orderPool/list',
    method: 'get',
    params: query
  })
}

// 查询订单详情
export function getOrderPool(orderId) {
  return request({
    url: `/productionflow/orderPool/${orderId}`,
    method: 'get'
  })
}

// 新增订单
export function addOrderPool(data) {
  return request({
    url: '/productionflow/orderPool',
    method: 'post',
    data
  })
}

// 更新订单
export function updateOrderPool(data) {
  return request({
    url: '/productionflow/orderPool',
    method: 'put',
    data
  })
}

// 删除订单
export function removeOrderPool(orderIds) {
  return request({
    url: `/productionflow/orderPool/${orderIds}`,
    method: 'delete'
  })
}

// 删除订单流程数据
export function clearOrderProcesses(orderIds) {
  return request({
    url: `/productionflow/orderPool/process/${orderIds}`,
    method: 'delete'
  })
}

// 查询生产流列表
export function listProductionFlows(query) {
  return request({
    url: '/productionflow/orderPool/flow/list',
    method: 'get',
    params: query
  })
}

// 新增生产流
export function addProductionFlow(data) {
  return request({
    url: '/productionflow/orderPool/flow',
    method: 'post',
    data
  })
}

// 更新生产流
export function updateProductionFlow(data) {
  return request({
    url: '/productionflow/orderPool/flow',
    method: 'put',
    data
  })
}
