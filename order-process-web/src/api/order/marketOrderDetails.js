import request from '@/utils/request'

// 查询销售单详情列表
export function listMarketOrderDetails(query) {
  return request({
    url: '/order/marketOrderDetails/list',
    method: 'get',
    params: query
  })
}

// 查询全部销售单详情列表
export function listMarketOrderDetailsAll(query) {
  return request({
    url: '/order/marketOrderDetails/listAll',
    method: 'get',
    params: query
  })
}

// 查询销售单详情详细
export function getMarketOrderDetails(detailsId) {
  return request({
    url: '/order/marketOrderDetails/' + detailsId,
    method: 'get'
  })
}

// 新增销售单详情
export function addMarketOrderDetails(data) {
  return request({
    url: '/order/marketOrderDetails',
    method: 'post',
    data: data
  })
}

// 修改销售单详情
export function updateMarketOrderDetails(data) {
  return request({
    url: '/order/marketOrderDetails',
    method: 'put',
    data: data
  })
}

// 删除销售单详情
export function delMarketOrderDetails(detailsId) {
  return request({
    url: '/order/marketOrderDetails/' + detailsId,
    method: 'delete'
  })
}

// 复制销售单详情
export function copyMarketOrderDetails(data) {
  return request({
    url: '/order/marketOrderDetails/copy',
    method: 'post',
    data: data
  })
}

// 查询销售报表列表
export function statementList(query) {
  return request({
    url: '/order/marketOrderDetails/statement',
    method: 'get',
    params: query
  })
}