import request from '@/utils/request'

// 查询数量记录列表
export function listMarketMateriel(query) {
  return request({
    url: '/order/marketMateriel/list',
    method: 'get',
    params: query
  })
}

// 查询全部数量记录列表
export function listMarketMaterielAll(query) {
  return request({
    url: '/order/marketMateriel/listAll',
    method: 'get',
    params: query
  })
}

// 查询数量记录详细
export function getMarketMateriel(recordId) {
  return request({
    url: '/order/marketMateriel/' + recordId,
    method: 'get'
  })
}

// 新增数量记录
export function addMarketMateriel(data) {
  return request({
    url: '/order/marketMateriel',
    method: 'post',
    data: data
  })
}

// 修改数量记录
export function updateMarketMateriel(data) {
  return request({
    url: '/order/marketMateriel',
    method: 'put',
    data: data
  })
}

// 删除数量记录
export function delMarketMateriel(recordId) {
  return request({
    url: '/order/marketMateriel/' + recordId,
    method: 'delete'
  })
}

// 复制数量记录
export function copyMarketMateriel(data) {
  return request({
    url: '/order/marketMateriel/copy',
    method: 'post',
    data: data
  })
}