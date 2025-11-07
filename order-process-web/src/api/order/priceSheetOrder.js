import request from '@/utils/request'

// 查询销售单列表
export function listPriceSheetOrder(query) {
  return request({
    url: '/order/priceSheetOrder/list',
    method: 'get',
    params: query
  })
}

// 查询全部销售单列表
export function listPriceSheetOrderAll(query) {
  return request({
    url: '/order/priceSheetOrder/listAll',
    method: 'get',
    params: query
  })
}

// 查询销售单详细
export function getPriceSheetOrder(orderId) {
  return request({
    url: '/order/priceSheetOrder/' + orderId,
    method: 'get'
  })
}

// 新增销售单
export function addPriceSheetOrder(data) {
  return request({
    url: '/order/priceSheetOrder',
    method: 'post',
    data: data
  })
}

// 修改销售单
export function updatePriceSheetOrder(data) {
  return request({
    url: '/order/priceSheetOrder',
    method: 'put',
    data: data
  })
}

// 删除销售单
export function delPriceSheetOrder(orderId) {
  return request({
    url: '/order/priceSheetOrder/' + orderId,
    method: 'delete'
  })
}


// 统计客户销售单信息
export function totalPriceSheetOrder(query) {
  return request({
    url: '/order/priceSheetOrder/totalPriceSheetOrder',
    method: 'get',
    params: query
  })
}

// 复制销售单
export function copyPriceSheetOrder(data) {
  return request({
    url: '/order/priceSheetOrder/copyPriceSheetOrder',
    method: 'post',
    data: data
  })
}

// 复制报价单_报价记录
export function priceSheetToSales(data) {
  return request({
    url: '/order/priceSheetOrder/priceSheetToSales',
    method: 'post',
    data: data
  })
}

// 复制报价单_报价记录
export function exportPriceList(orderNo) {
  return request({
    url: '/order/priceSheetOrder/exportPriceList',
    method: 'post',
    data: {
      orderNo:orderNo
    }
  })
}