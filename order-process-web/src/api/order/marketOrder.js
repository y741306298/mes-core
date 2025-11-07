import request from '@/utils/request'

// 查询销售单列表
export function listMarketOrder(query) {
  return request({
    url: '/order/marketOrder/list',
    method: 'get',
    params: query
  })
}

// 查询全部销售单列表
export function listMarketOrderAll(query) {
  return request({
    url: '/order/marketOrder/listAll',
    method: 'get',
    params: query
  })
}

// 查询销售单详细
export function getMarketOrder(orderId) {
  return request({
    url: '/order/marketOrder/' + orderId,
    method: 'get'
  })
}

// 新增销售单
export function addMarketOrder(data) {
  return request({
    url: '/order/marketOrder',
    method: 'post',
    data: data
  })
}

// 修改销售单
export function updateMarketOrder(data) {
  return request({
    url: '/order/marketOrder',
    method: 'put',
    data: data
  })
}

// 删除销售单
export function delMarketOrder(orderId) {
  return request({
    url: '/order/marketOrder/' + orderId,
    method: 'delete'
  })
}


// 统计客户销售单信息
export function totalMarketOrder(query) {
  return request({
    url: '/order/marketOrder/totalMarketOrder',
    method: 'get',
    params: query
  })
}

// 复制销售单
export function copyMarketOrder(data) {
  return request({
    url: '/order/marketOrder/copyMarketOrder',
    method: 'post',
    data: data
  })
}

// 复制销售单
export function beNotInUseSubmit(data) {
  return request({
    url: '/order/marketOrder/beNotInUseSubmit',
    method: 'post',
    data: data
  })
}