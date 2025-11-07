import request from '@/utils/request'

// 查询供应商信息列表
export function listSupplier(query) {
  return request({
    url: '/order/supplier/list',
    method: 'get',
    params: query
  })
}

// 查询全部供应商信息列表
export function listSupplierAll(query) {
  return request({
    url: '/order/supplier/listAll',
    method: 'get',
    params: query
  })
}

// 查询供应商信息详细
export function getSupplier(supplierId) {
  return request({
    url: '/order/supplier/' + supplierId,
    method: 'get'
  })
}

// 新增供应商信息
export function addSupplier(data) {
  return request({
    url: '/order/supplier',
    method: 'post',
    data: data
  })
}

// 修改供应商信息
export function updateSupplier(data) {
  return request({
    url: '/order/supplier',
    method: 'put',
    data: data
  })
}

// 删除供应商信息
export function delSupplier(supplierId) {
  return request({
    url: '/order/supplier/' + supplierId,
    method: 'delete'
  })
}

// 复制供应商信息
export function copySupplier(data) {
  return request({
    url: '/order/supplier/copy',
    method: 'post',
    data: data
  })
}

// 查询客户销售记录
export function queryPurchaseRecord(params) {
  return request({
    url: '/order/supplier/queryPurchaseRecord',
    method: 'get',
    params: params
  })
}