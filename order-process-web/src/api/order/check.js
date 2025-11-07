import request from '@/utils/request'

// 查询账单列表
export function listCheck(query) {
  return request({
    url: '/order/check/list',
    method: 'get',
    params: query
  })
}

// 查询全部账单列表
export function listCheckAll(query) {
  return request({
    url: '/order/check/listAll',
    method: 'get',
    params: query
  })
}

// 查询账单详细
export function getCheck(checkId) {
  return request({
    url: '/order/check/' + checkId,
    method: 'get'
  })
}

// 新增账单
export function addCheck(data) {
  return request({
    url: '/order/check',
    method: 'post',
    data: data
  })
}

// 修改账单
export function updateCheck(data) {
  return request({
    url: '/order/check',
    method: 'put',
    data: data
  })
}

// 删除账单
export function delCheck(checkId) {
  return request({
    url: '/order/check/' + checkId,
    method: 'delete'
  })
}

// 复制账单
export function copyCheck(data) {
  return request({
    url: '/order/check/copy',
    method: 'post',
    data: data
  })
}

// 业绩统计
export function achievement(data) {
  return request({
    url: '/order/check/achievement',
    method: 'get',
    params: data
  })
}

// 账户业绩统计
export function accountAchievement(data) {
  return request({
    url: '/order/check/accountAchievement',
    method: 'get',
    params: data
  })
}
