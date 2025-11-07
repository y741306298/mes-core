import request from '@/utils/request'

// 查询订单流程节点列表
export function listOrderNode(query) {
  return request({
    url: '/order/orderNode/list',
    method: 'get',
    params: query
  })
}

// 查询订单流程节点列表
export function myTask(query) {
  return request({
    url: '/order/orderNode/myTask',
    method: 'get',
    params: query
  })
}

// 查询全部订单流程节点列表
export function listOrderNodeAll(query) {
  return request({
    url: '/order/orderNode/listAll',
    method: 'get',
    params: query
  })
}

// 查询订单流程节点详细
export function getOrderNode(orderNodeId) {
  return request({
    url: '/order/orderNode/' + orderNodeId,
    method: 'get'
  })
}

// 新增订单流程节点
export function addOrderNode(data) {
  return request({
    url: '/order/orderNode',
    method: 'post',
    data: data
  })
}

// 修改订单流程节点
export function updateOrderNode(data) {
  return request({
    url: '/order/orderNode',
    method: 'put',
    data: data
  })
}

// 删除订单流程节点
export function delOrderNode(orderNodeId) {
  return request({
    url: '/order/orderNode/' + orderNodeId,
    method: 'delete'
  })
}

// 完成订单流程节点
export function complateNode(data) {
  return request({
    url: '/order/orderNode/complateNode',
    method: 'post',
    data: data
  })
}

// 完成订单流程节点
export function submitRemark(data) {
  return request({
    url: '/order/orderNode/submitRemark',
    method: 'post',
    data: data
  })
}