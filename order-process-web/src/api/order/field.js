import request from '@/utils/request'

// 查询自定义字段列表
export function listField(query) {
  return request({
    url: '/order/field/list',
    method: 'get',
    params: query
  })
}

// 查询全部自定义字段列表
export function listFieldAll(query) {
  return request({
    url: '/order/field/listAll',
    method: 'get',
    params: query
  })
}

// 查询自定义字段详细
export function getField(fieldId) {
  return request({
    url: '/order/field/' + fieldId,
    method: 'get'
  })
}

// 新增自定义字段
export function addField(data) {
  return request({
    url: '/order/field',
    method: 'post',
    data: data
  })
}

// 修改自定义字段
export function updateField(data) {
  return request({
    url: '/order/field',
    method: 'put',
    data: data
  })
}

// 删除自定义字段
export function delField(fieldId) {
  return request({
    url: '/order/field/' + fieldId,
    method: 'delete'
  })
}

// 复制自定义字段
export function copyField(data) {
  return request({
    url: '/order/field/copy',
    method: 'post',
    data: data
  })
}