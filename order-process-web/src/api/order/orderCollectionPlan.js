import request from '@/utils/request'

// 查询收款计划列表
export function listOrderCollectionPlan(query) {
  return request({
    url: '/order/orderCollectionPlan/list',
    method: 'get',
    params: query
  })
}

// 查询全部收款计划列表
export function listOrderCollectionPlanAll(query) {
  return request({
    url: '/order/orderCollectionPlan/listAll',
    method: 'get',
    params: query
  })
}

// 查询收款计划详细
export function getOrderCollectionPlan(planId) {
  return request({
    url: '/order/orderCollectionPlan/' + planId,
    method: 'get'
  })
}

// 新增收款计划
export function addOrderCollectionPlan(data) {
  return request({
    url: '/order/orderCollectionPlan',
    method: 'post',
    data: data
  })
}

// 修改收款计划
export function updateOrderCollectionPlan(data) {
  return request({
    url: '/order/orderCollectionPlan',
    method: 'put',
    data: data
  })
}

// 删除收款计划
export function delOrderCollectionPlan(planId) {
  return request({
    url: '/order/orderCollectionPlan/' + planId,
    method: 'delete'
  })
}

// 复制收款计划
export function copyOrderCollectionPlan(data) {
  return request({
    url: '/order/orderCollectionPlan/copy',
    method: 'post',
    data: data
  })
}