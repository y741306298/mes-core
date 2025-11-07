import request from '@/utils/request'

// 查询入库单详情 入库单关联物料列表
export function listMateriel(query) {
  return request({
    url: '/order/inInventoryMateriel/list',
    method: 'get',
    params: query
  })
}

// 查询全部入库单详情 入库单关联物料列表
export function listMaterielAll(query) {
  return request({
    url: '/order/inInventoryMateriel/listAll',
    method: 'get',
    params: query
  })
}

// 查询入库单详情 入库单关联物料详细
export function getMateriel(inInventoryMaterielId) {
  return request({
    url: '/order/inInventoryMateriel/' + inInventoryMaterielId,
    method: 'get'
  })
}

// 新增入库单详情 入库单关联物料
export function addMateriel(data) {
  return request({
    url: '/order/inInventoryMateriel',
    method: 'post',
    data: data
  })
}

// 修改入库单详情 入库单关联物料
export function updateMateriel(data) {
  return request({
    url: '/order/inInventoryMateriel',
    method: 'put',
    data: data
  })
}

// 删除入库单详情 入库单关联物料
export function delMateriel(inInventoryMaterielId) {
  return request({
    url: '/order/inInventoryMateriel/' + inInventoryMaterielId,
    method: 'delete'
  })
}

// 复制入库单详情 入库单关联物料
export function copyMateriel(data) {
  return request({
    url: '/order/inInventoryMateriel/copy',
    method: 'post',
    data: data
  })
}

// 获取文件数据列表
export function getFileData(data) {
  return request({
    url: '/order/inInventoryMateriel/getFileData',
    method: 'post',
    data: data
  })
}

// 查询入库管理详细
export function queryByInInventoryId(inInventoryId) {
  return request({
    url: '/order/inInventoryMateriel/queryByInInventoryId',
    method: 'post',
    data: {
      inInventoryId: inInventoryId
    }
  })
}