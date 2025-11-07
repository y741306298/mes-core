import request from '@/utils/request'

// 查询订单模板列表
export function listOrderTemplate(query) {
  return request({
    url: '/order/orderTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询全部订单模板列表
export function listOrderTemplateAll(query) {
  return request({
    url: '/order/orderTemplate/listAll',
    method: 'get',
    params: query
  })
}

// 查询订单模板详细
export function getOrderTemplate(orderTemplateId) {
  return request({
    url: '/order/orderTemplate/' + orderTemplateId,
    method: 'get'
  })
}

// 新增订单模板
export function addOrderTemplate(data) {
  return request({
    url: '/order/orderTemplate',
    method: 'post',
    data: data
  })
}

// 修改订单模板
export function updateOrderTemplate(data) {
  return request({
    url: '/order/orderTemplate',
    method: 'put',
    data: data
  })
}

// 删除订单模板
export function delOrderTemplate(orderTemplateId) {
  return request({
    url: '/order/orderTemplate/' + orderTemplateId,
    method: 'delete'
  })
}

// 复制订单模板
export function copyOrderTemplate(data) {
  return request({
    url: '/order/orderTemplate/copy',
    method: 'post',
    data: data
  })
}

// 派工
export function sendWork(data) {
  return request({
    url: '/order/orderTemplate/sendWork',
    method: 'post',
    data: data
  })
}

// 获取订单ID
export function getOrderId(data) {
  return request({
    url: '/order/orderTemplate/getOrderId',
    method: 'post',
    data: data
  })
}

// 订单归档
export function beNotInUseSubmit(data) {
  return request({
    url: '/order/orderTemplate/beNotInUseSubmit',
    method: 'post',
    data: data
  })
}

// 订单归档
export function getOrderForm(data) {
  return request({
    url: '/order/orderTemplate/getOrderForm',
    method: 'post',
    data: data
  })
}


// 查询模板和节点
export function getOrderTempleatAndOrderNode(query) {
  return request({
    url: '/order/orderTemplate/getOrderTempleatAndOrderNode',
    method: 'get',
    params: query
  })
}

// 查询模板和节点
export function sendWorkSelect(query) {
  return request({
    url: '/order/orderTemplate/sendWorkSelect',
    method: 'get',
    params: query
  })
}