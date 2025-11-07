import request from '@/utils/request'

// 查询报价单_报价记录列表
export function listPriceSheetOrderRecord(query) {
  return request({
    url: '/order/priceSheetOrderRecord/list',
    method: 'get',
    params: query
  })
}

// 查询全部报价单_报价记录列表
export function listPriceSheetOrderRecordAll(query) {
  return request({
    url: '/order/priceSheetOrderRecord/listAll',
    method: 'get',
    params: query
  })
}

// 查询报价单_报价记录详细
export function getPriceSheetOrderRecord(recordId) {
  return request({
    url: '/order/priceSheetOrderRecord/' + recordId,
    method: 'get'
  })
}

// 新增报价单_报价记录
export function addPriceSheetOrderRecord(data) {
  return request({
    url: '/order/priceSheetOrderRecord',
    method: 'post',
    data: data
  })
}

// 修改报价单_报价记录
export function updatePriceSheetOrderRecord(data) {
  return request({
    url: '/order/priceSheetOrderRecord',
    method: 'put',
    data: data
  })
}

// 删除报价单_报价记录
export function delPriceSheetOrderRecord(recordId) {
  return request({
    url: '/order/priceSheetOrderRecord/' + recordId,
    method: 'delete'
  })
}

// 复制报价单_报价记录
export function copyPriceSheetOrderRecord(data) {
  return request({
    url: '/order/priceSheetOrderRecord/copy',
    method: 'post',
    data: data
  })
}
