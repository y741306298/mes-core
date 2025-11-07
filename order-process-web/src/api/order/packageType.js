import request from '@/utils/request'

// 查询封装类型管理列表
export function listPackageType(query) {
  return request({
    url: '/order/packageType/list',
    method: 'get',
    params: query
  })
}

// 查询全部封装类型管理列表
export function listPackageTypeAll(query) {
  return request({
    url: '/order/packageType/listAll',
    method: 'get',
    params: query
  })
}

// 查询封装类型管理详细
export function getPackageType(typeId) {
  return request({
    url: '/order/packageType/' + typeId,
    method: 'get'
  })
}

// 新增封装类型管理
export function addPackageType(data) {
  return request({
    url: '/order/packageType',
    method: 'post',
    data: data
  })
}

// 修改封装类型管理
export function updatePackageType(data) {
  return request({
    url: '/order/packageType',
    method: 'put',
    data: data
  })
}

// 删除封装类型管理
export function delPackageType(typeId) {
  return request({
    url: '/order/packageType/' + typeId,
    method: 'delete'
  })
}

// 复制封装类型管理
export function copyPackageType(data) {
  return request({
    url: '/order/packageType/copy',
    method: 'post',
    data: data
  })
}