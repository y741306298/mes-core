import request from '@/utils/request'

// 查询销售单详情列表
export function listPriceSheetOrderDetails(query) {
  return request({
    url: '/order/priceSheetOrderDetails/list',
    method: 'get',
    params: query
  })
}

// 查询全部销售单详情列表
export function listPriceSheetOrderDetailsAll(query) {
  return request({
    url: '/order/priceSheetOrderDetails/listAll',
    method: 'get',
    params: query
  })
}

// 查询销售单详情详细
export function getPriceSheetOrderDetails(detailsId) {
  return request({
    url: '/order/priceSheetOrderDetails/' + detailsId,
    method: 'get'
  })
}

// 新增销售单详情
export function addPriceSheetOrderDetails(data) {
  return request({
    url: '/order/priceSheetOrderDetails',
    method: 'post',
    data: data
  })
}

// 修改销售单详情
export function updatePriceSheetOrderDetails(data) {
  return request({
    url: '/order/priceSheetOrderDetails',
    method: 'put',
    data: data
  })
}

// 删除销售单详情
export function delPriceSheetOrderDetails(detailsId) {
  return request({
    url: '/order/priceSheetOrderDetails/' + detailsId,
    method: 'delete'
  })
}

// 复制销售单详情
export function copyPriceSheetOrderDetails(data) {
  return request({
    url: '/order/priceSheetOrderDetails/copy',
    method: 'post',
    data: data
  })
}

// 查询销售报表列表
export function statementList(query) {
  return request({
    url: '/order/priceSheetOrderDetails/statement',
    method: 'get',
    params: query
  })
}