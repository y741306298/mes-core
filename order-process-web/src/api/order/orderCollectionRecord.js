import request from '@/utils/request'

// 查询收款记录列表
export function listOrderCollectionRecord(query) {
  return request({
    url: '/order/orderCollectionRecord/list',
    method: 'get',
    params: query
  })
}

// 查询全部收款记录列表
export function listOrderCollectionRecordAll(query) {
  return request({
    url: '/order/orderCollectionRecord/listAll',
    method: 'get',
    params: query
  })
}

// 查询收款记录详细
export function getOrderCollectionRecord(recordId) {
  return request({
    url: '/order/orderCollectionRecord/' + recordId,
    method: 'get'
  })
}

// 新增收款记录
export function addOrderCollectionRecord(data) {
  return request({
    url: '/order/orderCollectionRecord',
    method: 'post',
    data: data
  })
}

// 修改收款记录
export function updateOrderCollectionRecord(data) {
  return request({
    url: '/order/orderCollectionRecord',
    method: 'put',
    data: data
  })
}

// 删除收款记录
export function delOrderCollectionRecord(recordId) {
  return request({
    url: '/order/orderCollectionRecord/' + recordId,
    method: 'delete'
  })
}

// 复制收款记录
export function copyOrderCollectionRecord(data) {
  return request({
    url: '/order/orderCollectionRecord/copy',
    method: 'post',
    data: data
  })
}