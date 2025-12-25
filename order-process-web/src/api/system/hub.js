import request from '@/utils/request'

// 获取材料列表
export function manuMatList(query) {
  return request({
    url: '/hub/manuMatList',
    method: 'get',
    params: query
  })
}

// 获取产品列表
export function manuProdList(query) {
  return request({
    url: '/hub/manuProdList',
    method: 'get',
    params: query
  })
}

// 获取工艺列表
export function manuProcList(query) {
  return request({
    url: '/hub/manuProcList',
    method: 'get',
    params: query
  })
}
