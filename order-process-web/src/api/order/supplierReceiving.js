import request from '@/utils/request'

// 查询供应商收货单列表
export function listSupplierReceiving(query) {
  return request({
    url: '/order/supplierReceiving/list',
    method: 'get',
    params: query
  })
}

// 查询全部供应商收货单列表
export function listSupplierReceivingAll(query) {
  return request({
    url: '/order/supplierReceiving/listAll',
    method: 'get',
    params: query
  })
}

// 查询供应商收货单详细
export function getSupplierReceiving(receivingId) {
  return request({
    url: '/order/supplierReceiving/' + receivingId,
    method: 'get'
  })
}

// 新增供应商收货单
export function addSupplierReceiving(data) {
  return request({
    url: '/order/supplierReceiving',
    method: 'post',
    data: data
  })
}

// 修改供应商收货单
export function updateSupplierReceiving(data) {
  return request({
    url: '/order/supplierReceiving',
    method: 'put',
    data: data
  })
}

// 删除供应商收货单
export function delSupplierReceiving(receivingId) {
  return request({
    url: '/order/supplierReceiving/' + receivingId,
    method: 'delete'
  })
}

// 复制供应商收货单
export function copySupplierReceiving(data) {
  return request({
    url: '/order/supplierReceiving/copy',
    method: 'post',
    data: data
  })
}

// 供应商收货提醒表
export function receivingRemind(data) {
  return request({
    url: '/order/supplierReceiving/receivingRemind',
    method: 'post',
    data: data
  })
}

// 供应商收货提醒表
export function receivingByOrder(data) {
  return request({
    url: '/order/supplierReceiving/receivingByOrder',
    method: 'post',
    data: data
  })
}

// 供应商收货提醒表
export function receivingSubmit(data) {
  return request({
    url: '/order/supplierReceiving/receivingSubmit',
    method: 'post',
    data: data
  })
}

// 供应商收货提醒表
export function getPrintData(data) {
  return request({
    url: '/order/supplierReceiving/getPrintData',
    method: 'post',
    data: data
  })
}

