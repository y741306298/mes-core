import request from '@/utils/request'

// 查询客户送货单列表
export function listCustomerDelivery(query) {
  return request({
    url: '/order/customerDelivery/list',
    method: 'get',
    params: query
  })
}

// 查询全部客户送货单列表
export function listCustomerDeliveryAll(query) {
  return request({
    url: '/order/customerDelivery/listAll',
    method: 'get',
    params: query
  })
}

// 查询客户送货单详细
export function getCustomerDelivery(deliveryId) {
  return request({
    url: '/order/customerDelivery/' + deliveryId,
    method: 'get'
  })
}

// 新增客户送货单
export function addCustomerDelivery(data) {
  return request({
    url: '/order/customerDelivery',
    method: 'post',
    data: data
  })
}

// 修改客户送货单
export function updateCustomerDelivery(data) {
  return request({
    url: '/order/customerDelivery',
    method: 'put',
    data: data
  })
}

// 删除客户送货单
export function delCustomerDelivery(deliveryId) {
  return request({
    url: '/order/customerDelivery/' + deliveryId,
    method: 'delete'
  })
}

// 复制客户送货单
export function copyCustomerDelivery(data) {
  return request({
    url: '/order/customerDelivery/copy',
    method: 'post',
    data: data
  })
}

// 客户送货提醒表
export function deliveryRemind(data) {
  return request({
    url: '/order/customerDelivery/deliveryRemind',
    method: 'post',
    data: data
  })
}

// 客户送货提醒表
export function deliveryByOrder(data) {
  return request({
    url: '/order/customerDelivery/deliveryByOrder',
    method: 'post',
    data: data
  })
}

// 客户送货提醒表
export function deliverySubmit(data) {
  return request({
    url: '/order/customerDelivery/deliverySubmit',
    method: 'post',
    data: data
  })
}

// 供应商收货提醒表
export function getPrintData(data) {
  return request({
    url: '/order/customerDelivery/getPrintData',
    method: 'post',
    data: data
  })
}


