import request from '@/utils/request'

// 查询销售单列表
export function listSalesOrder(query) {
  return request({
    url: '/order/salesOrder/list',
    method: 'get',
    params: query
  })
}

// 查询全部销售单列表
export function listSalesOrderAll(query) {
  return request({
    url: '/order/salesOrder/listAll',
    method: 'get',
    params: query
  })
}

// 查询销售单详细
export function getSalesOrder(orderId) {
  return request({
    url: '/order/salesOrder/' + orderId,
    method: 'get'
  })
}

// 新增销售单
export function addSalesOrder(data) {
  return request({
    url: '/order/salesOrder',
    method: 'post',
    data: data
  })
}

// 修改销售单
export function updateSalesOrder(data) {
  return request({
    url: '/order/salesOrder',
    method: 'put',
    data: data
  })
}

// 删除销售单
export function delSalesOrder(orderId) {
  return request({
    url: '/order/salesOrder/' + orderId,
    method: 'delete'
  })
}


// 统计客户销售单信息
export function totalSalesOrder(query) {
  return request({
    url: '/order/salesOrder/totalSalesOrder',
    method: 'get',
    params: query
  })
}

// 复制销售单
export function copySalesOrder(data) {
  return request({
    url: '/order/salesOrder/copySalesOrder',
    method: 'post',
    data: data
  })
}

// 复制销售单
export function beNotInUseSubmit(data) {
  return request({
    url: '/order/salesOrder/beNotInUseSubmit',
    method: 'post',
    data: data
  })
}
