import request from '@/utils/request'

// 查询物料类型列表
export function listMaterielType(query) {
  return request({
    url: '/order/materielType/list',
    method: 'get',
    params: query
  })
}

// 查询全部物料类型列表
export function listMaterielTypeAll(query) {
  return request({
    url: '/order/materielType/listAll',
    method: 'get',
    params: query
  })
}

// 查询物料类型详细
export function getMaterielType(typeId) {
  return request({
    url: '/order/materielType/' + typeId,
    method: 'get'
  })
}

// 新增物料类型
export function addMaterielType(data) {
  return request({
    url: '/order/materielType',
    method: 'post',
    data: data
  })
}

// 修改物料类型
export function updateMaterielType(data) {
  return request({
    url: '/order/materielType',
    method: 'put',
    data: data
  })
}

// 删除物料类型
export function delMaterielType(typeId) {
  return request({
    url: '/order/materielType/' + typeId,
    method: 'delete'
  })
}

// 复制物料类型
export function copyMaterielType(data) {
  return request({
    url: '/order/materielType/copy',
    method: 'post',
    data: data
  })
}