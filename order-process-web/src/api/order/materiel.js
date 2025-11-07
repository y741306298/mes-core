import request from '@/utils/request'

// 查询物料信息列表
export function listMateriel(query) {
  return request({
    url: '/order/materiel/list',
    method: 'get',
    params: query
  })
}

// 查询全部物料信息列表
export function listMaterielAll(query) {
  return request({
    url: '/order/materiel/listAll',
    method: 'get',
    params: query
  })
}

// 查询物料信息详细
export function getMateriel(materielId) {
  return request({
    url: '/order/materiel/' + materielId,
    method: 'get'
  })
}

// 新增物料信息
export function addMateriel(data) {
  return request({
    url: '/order/materiel',
    method: 'post',
    data: data
  })
}

// 修改物料信息
export function updateMateriel(data) {
  return request({
    url: '/order/materiel',
    method: 'put',
    data: data
  })
}

// 删除物料信息
export function delMateriel(materielId) {
  return request({
    url: '/order/materiel/' + materielId,
    method: 'delete'
  })
}

// 复制物料信息
export function copyMateriel(data) {
  return request({
    url: '/order/materiel/copy',
    method: 'post',
    data: data
  })
}

// 设置预警
export function warningMateriel(data) {
  return request({
    url: '/order/materiel/warning',
    method: 'post',
    data: data
  })
}

// 查询出入库记录
export function selectRecord(data) {
  return request({
    url: '/order/materiel/selectRecord',
    method: 'post',
    data: data
  })
}