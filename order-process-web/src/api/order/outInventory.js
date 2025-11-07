import request from '@/utils/request'

// 查询出库管理列表
export function listInventory(query) {
  return request({
    url: '/order/outInventory/list',
    method: 'get',
    params: query
  })
}

// 查询全部出库管理列表
export function listInventoryAll(query) {
  return request({
    url: '/order/outInventory/listAll',
    method: 'get',
    params: query
  })
}

// 查询出库管理详细
export function getInventory(outInventoryId) {
  return request({
    url: '/order/outInventory/' + outInventoryId,
    method: 'get'
  })
}

// 新增出库管理
export function addInventory(data) {
  return request({
    url: '/order/outInventory',
    method: 'post',
    data: data
  })
}

// 修改出库管理
export function updateInventory(data) {
  return request({
    url: '/order/outInventory',
    method: 'put',
    data: data
  })
}

// 删除出库管理
export function delInventory(outInventoryId) {
  return request({
    url: '/order/outInventory/' + outInventoryId,
    method: 'delete'
  })
}

// 复制出库管理
export function copyInventory(data) {
  return request({
    url: '/order/outInventory/copy',
    method: 'post',
    data: data
  })
}

// 查询出库管理详细
export function affirm(outInventoryId) {
  return request({
    url: '/order/outInventory/affirm',
    method: 'post',
    data: {
      outInventoryId: outInventoryId
    }
  })
}


