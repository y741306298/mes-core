import request from '@/utils/request'

// 查询开票计划列表
export function listOrderInvoicePlan(query) {
  return request({
    url: '/order/orderInvoicePlan/list',
    method: 'get',
    params: query
  })
}

// 查询全部开票计划列表
export function listOrderInvoicePlanAll(query) {
  return request({
    url: '/order/orderInvoicePlan/listAll',
    method: 'get',
    params: query
  })
}

// 查询开票计划详细
export function getOrderInvoicePlan(planId) {
  return request({
    url: '/order/orderInvoicePlan/' + planId,
    method: 'get'
  })
}

// 新增开票计划
export function addOrderInvoicePlan(data) {
  return request({
    url: '/order/orderInvoicePlan',
    method: 'post',
    data: data
  })
}

// 修改开票计划
export function updateOrderInvoicePlan(data) {
  return request({
    url: '/order/orderInvoicePlan',
    method: 'put',
    data: data
  })
}

// 删除开票计划
export function delOrderInvoicePlan(planId) {
  return request({
    url: '/order/orderInvoicePlan/' + planId,
    method: 'delete'
  })
}

// 复制开票计划
export function copyOrderInvoicePlan(data) {
  return request({
    url: '/order/orderInvoicePlan/copy',
    method: 'post',
    data: data
  })
}