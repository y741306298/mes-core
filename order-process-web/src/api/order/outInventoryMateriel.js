import request from '@/utils/request'

// 查询入库单详情 入库单关联物料列表
export function listMateriel(query) {
  return request({
    url: '/order/materiel/list',
    method: 'get',
    params: query
  })
}

// 查询全部入库单详情 入库单关联物料列表
export function listMaterielAll(query) {
  return request({
    url: '/order/materiel/listAll',
    method: 'get',
    params: query
  })
}

// 查询入库单详情 入库单关联物料详细
export function getMateriel(outInventoryMaterielId) {
  return request({
    url: '/order/materiel/' + outInventoryMaterielId,
    method: 'get'
  })
}

// 新增入库单详情 入库单关联物料
export function addMateriel(data) {
  return request({
    url: '/order/materiel',
    method: 'post',
    data: data
  })
}

// 修改入库单详情 入库单关联物料
export function updateMateriel(data) {
  return request({
    url: '/order/materiel',
    method: 'put',
    data: data
  })
}

// 删除入库单详情 入库单关联物料
export function delMateriel(outInventoryMaterielId) {
  return request({
    url: '/order/materiel/' + outInventoryMaterielId,
    method: 'delete'
  })
}

// 复制入库单详情 入库单关联物料
export function copyMateriel(data) {
  return request({
    url: '/order/materiel/copy',
    method: 'post',
    data: data
  })
}

// 获取文件数据列表
export function getFileData(data) {
  return request({
    url: '/order/outInventoryMateriel/getFileData',
    method: 'post',
    data: data
  })
}

// 查询入库管理详细
export function queryByOutInventoryId(outInventoryId) {
  return request({
    url: '/order/outInventoryMateriel/queryByOutInventoryId',
    method: 'post',
    data: {
      outInventoryId: outInventoryId
    }
  })
}