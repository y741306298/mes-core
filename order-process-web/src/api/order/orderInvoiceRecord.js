import request from '@/utils/request'

// 查询开票记录列表
export function listOrderInvoiceRecord(query) {
  return request({
    url: '/order/orderInvoiceRecord/list',
    method: 'get',
    params: query
  })
}

// 查询全部开票记录列表
export function listOrderInvoiceRecordAll(query) {
  return request({
    url: '/order/orderInvoiceRecord/listAll',
    method: 'get',
    params: query
  })
}

// 查询开票记录详细
export function getOrderInvoiceRecord(recordId) {
  return request({
    url: '/order/orderInvoiceRecord/' + recordId,
    method: 'get'
  })
}

// 新增开票记录
export function addOrderInvoiceRecord(data) {
  return request({
    url: '/order/orderInvoiceRecord',
    method: 'post',
    data: data
  })
}

// 修改开票记录
export function updateOrderInvoiceRecord(data) {
  return request({
    url: '/order/orderInvoiceRecord',
    method: 'put',
    data: data
  })
}

// 删除开票记录
export function delOrderInvoiceRecord(recordId) {
  return request({
    url: '/order/orderInvoiceRecord/' + recordId,
    method: 'delete'
  })
}

// 复制开票记录
export function copyOrderInvoiceRecord(data) {
  return request({
    url: '/order/orderInvoiceRecord/copy',
    method: 'post',
    data: data
  })
}