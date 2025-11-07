import request from '@/utils/request'

// 查询设备列表
export function listProdDevice(query) {
  return request({
    url: '/productionflow/device/list',
    method: 'get',
    params: query
  })
}

// 查询设备详情
export function getProdDevice(deviceId) {
  return request({
    url: `/productionflow/device/${deviceId}`,
    method: 'get'
  })
}

// 新增设备
export function addProdDevice(data) {
  return request({
    url: '/productionflow/device',
    method: 'post',
    data
  })
}

// 修改设备
export function updateProdDevice(data) {
  return request({
    url: '/productionflow/device',
    method: 'put',
    data
  })
}

// 删除设备
export function deleteProdDevice(deviceId) {
  return request({
    url: `/productionflow/device/${deviceId}`,
    method: 'delete'
  })
}
