import request from '@/utils/request'

// 查询入库管理列表
export function listInventory(query) {
  return request({
    url: '/order/inInventory/list',
    method: 'get',
    params: query
  })
}

// 查询全部入库管理列表
export function listInventoryAll(query) {
  return request({
    url: '/order/inInventory/listAll',
    method: 'get',
    params: query
  })
}

// 查询入库管理详细
export function getInventory(inInventoryId) {
  return request({
    url: '/order/inInventory/' + inInventoryId,
    method: 'get'
  })
}

// 新增入库管理
export function addInventory(data) {
  return request({
    url: '/order/inInventory',
    method: 'post',
    data: data
  })
}

// 修改入库管理
export function updateInventory(data) {
  return request({
    url: '/order/inInventory',
    method: 'put',
    data: data
  })
}

// 删除入库管理
export function delInventory(inInventoryId) {
  return request({
    url: '/order/inInventory/' + inInventoryId,
    method: 'delete'
  })
}

// 复制入库管理
export function copyInventory(data) {
  return request({
    url: '/order/inInventory/copy',
    method: 'post',
    data: data
  })
}

// 查询入库管理详细
export function affirm(inInventoryId) {
  return request({
    url: '/order/inInventory/affirm',
    method: 'post',
    data: {
      inInventoryId: inInventoryId
    }
  })
}


