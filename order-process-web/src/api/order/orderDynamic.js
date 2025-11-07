import request from '@/utils/request'

// 查询订单动态列表
export function listOrderDynamic(query) {
  return request({
    url: '/order/orderDynamic/list',
    method: 'get',
    params: query
  })
}

// 查询全部订单动态列表
export function listOrderDynamicAll(query) {
  return request({
    url: '/order/orderDynamic/listAll',
    method: 'get',
    params: query
  })
}

// 查询订单动态详细
export function getOrderDynamic(dynamicId) {
  return request({
    url: '/order/orderDynamic/' + dynamicId,
    method: 'get'
  })
}

// 新增订单动态
export function addOrderDynamic(data) {
  return request({
    url: '/order/orderDynamic',
    method: 'post',
    data: data
  })
}

// 修改订单动态
export function updateOrderDynamic(data) {
  return request({
    url: '/order/orderDynamic',
    method: 'put',
    data: data
  })
}

// 删除订单动态
export function delOrderDynamic(dynamicId) {
  return request({
    url: '/order/orderDynamic/' + dynamicId,
    method: 'delete'
  })
}

// 复制订单动态
export function copyOrderDynamic(data) {
  return request({
    url: '/order/orderDynamic/copy',
    method: 'post',
    data: data
  })
}