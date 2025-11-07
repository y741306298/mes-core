import request from '@/utils/request'

// 查询互转单列表
export function listIntertransferOrder(query) {
  return request({
    url: '/order/intertransferOrder/list',
    method: 'get',
    params: query
  })
}

// 查询全部互转单列表
export function listIntertransferOrderAll(query) {
  return request({
    url: '/order/intertransferOrder/listAll',
    method: 'get',
    params: query
  })
}

// 查询互转单详细
export function getIntertransferOrder(intertransferId) {
  return request({
    url: '/order/intertransferOrder/' + intertransferId,
    method: 'get'
  })
}

// 新增互转单
export function addIntertransferOrder(data) {
  return request({
    url: '/order/intertransferOrder',
    method: 'post',
    data: data
  })
}

// 修改互转单
export function updateIntertransferOrder(data) {
  return request({
    url: '/order/intertransferOrder',
    method: 'put',
    data: data
  })
}

// 删除互转单
export function delIntertransferOrder(intertransferId) {
  return request({
    url: '/order/intertransferOrder/' + intertransferId,
    method: 'delete'
  })
}

// 复制互转单
export function copyIntertransferOrder(data) {
  return request({
    url: '/order/intertransferOrder/copy',
    method: 'post',
    data: data
  })
}