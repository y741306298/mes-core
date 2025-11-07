import request from '@/utils/request'

// 查询数量记录列表
export function listPriceSheetMateriel(query) {
  return request({
    url: '/order/priceSheetMateriel/list',
    method: 'get',
    params: query
  })
}

// 查询全部数量记录列表
export function listPriceSheetMaterielAll(query) {
  return request({
    url: '/order/priceSheetMateriel/listAll',
    method: 'get',
    params: query
  })
}

// 查询数量记录详细
export function getPriceSheetMateriel(recordId) {
  return request({
    url: '/order/priceSheetMateriel/' + recordId,
    method: 'get'
  })
}

// 新增数量记录
export function addPriceSheetMateriel(data) {
  return request({
    url: '/order/priceSheetMateriel',
    method: 'post',
    data: data
  })
}

// 修改数量记录
export function updatePriceSheetMateriel(data) {
  return request({
    url: '/order/priceSheetMateriel',
    method: 'put',
    data: data
  })
}

// 删除数量记录
export function delPriceSheetMateriel(recordId) {
  return request({
    url: '/order/priceSheetMateriel/' + recordId,
    method: 'delete'
  })
}

// 复制数量记录
export function copyPriceSheetMateriel(data) {
  return request({
    url: '/order/priceSheetMateriel/copy',
    method: 'post',
    data: data
  })
}