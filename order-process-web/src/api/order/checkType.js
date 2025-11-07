import request from '@/utils/request'

// 查询账单类型列表
export function listCheckType(query) {
  return request({
    url: '/order/checkType/list',
    method: 'get',
    params: query
  })
}

// 查询全部账单类型列表
export function listCheckTypeAll(query) {
  return request({
    url: '/order/checkType/listAll',
    method: 'get',
    params: query
  })
}

// 查询账单类型详细
export function getCheckType(typeId) {
  return request({
    url: '/order/checkType/' + typeId,
    method: 'get'
  })
}

// 新增账单类型
export function addCheckType(data) {
  return request({
    url: '/order/checkType',
    method: 'post',
    data: data
  })
}

// 修改账单类型
export function updateCheckType(data) {
  return request({
    url: '/order/checkType',
    method: 'put',
    data: data
  })
}

// 删除账单类型
export function delCheckType(typeId) {
  return request({
    url: '/order/checkType/' + typeId,
    method: 'delete'
  })
}

// 复制账单类型
export function copyCheckType(data) {
  return request({
    url: '/order/checkType/copy',
    method: 'post',
    data: data
  })
}