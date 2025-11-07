import request from '@/utils/request'

// 查询数量记录列表
export function listOrderMaterielRecord(query) {
  return request({
    url: '/order/orderMaterielRecord/list',
    method: 'get',
    params: query
  })
}

// 查询全部数量记录列表
export function listOrderMaterielRecordAll(query) {
  return request({
    url: '/order/orderMaterielRecord/listAll',
    method: 'get',
    params: query
  })
}

// 查询数量记录详细
export function getOrderMaterielRecord(recordId) {
  return request({
    url: '/order/orderMaterielRecord/' + recordId,
    method: 'get'
  })
}

// 新增数量记录
export function addOrderMaterielRecord(data) {
  return request({
    url: '/order/orderMaterielRecord',
    method: 'post',
    data: data
  })
}

// 修改数量记录
export function updateOrderMaterielRecord(data) {
  return request({
    url: '/order/orderMaterielRecord',
    method: 'put',
    data: data
  })
}

// 删除数量记录
export function delOrderMaterielRecord(recordId) {
  return request({
    url: '/order/orderMaterielRecord/' + recordId,
    method: 'delete'
  })
}

// 复制数量记录
export function copyOrderMaterielRecord(data) {
  return request({
    url: '/order/orderMaterielRecord/copy',
    method: 'post',
    data: data
  })
}