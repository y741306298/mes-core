import request from '@/utils/request'

// 查询销售单详情列表
export function listSalesOrderDetails(query) {
  return request({
    url: '/order/salesOrderDetails/list',
    method: 'get',
    params: query
  })
}

// 查询全部销售单详情列表
export function listSalesOrderDetailsAll(query) {
  return request({
    url: '/order/salesOrderDetails/listAll',
    method: 'get',
    params: query
  })
}

// 查询销售单详情详细
export function getSalesOrderDetails(detailsId) {
  return request({
    url: '/order/salesOrderDetails/' + detailsId,
    method: 'get'
  })
}

// 新增销售单详情
export function addSalesOrderDetails(data) {
  return request({
    url: '/order/salesOrderDetails',
    method: 'post',
    data: data
  })
}

// 修改销售单详情
export function updateSalesOrderDetails(data) {
  return request({
    url: '/order/salesOrderDetails',
    method: 'put',
    data: data
  })
}

// 删除销售单详情
export function delSalesOrderDetails(detailsId) {
  return request({
    url: '/order/salesOrderDetails/' + detailsId,
    method: 'delete'
  })
}

// 复制销售单详情
export function copySalesOrderDetails(data) {
  return request({
    url: '/order/salesOrderDetails/copy',
    method: 'post',
    data: data
  })
}

// 查询销售报表列表
export function statementList(query) {
  return request({
    url: '/order/salesOrderDetails/statement',
    method: 'get',
    params: query
  })
}

// 修改销售单详情
export function updateAndLock(data) {
  return request({
    url: '/order/salesOrderDetails/updateAndLock',
    method: 'post',
    data: data
  })
}
