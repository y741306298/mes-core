import request from '@/utils/request'

// 查询物料数量计划列表
export function listOrderMaterielPlan(query) {
  return request({
    url: '/order/orderMaterielPlan/list',
    method: 'get',
    params: query
  })
}

// 查询全部物料数量计划列表
export function listOrderMaterielPlanAll(query) {
  return request({
    url: '/order/orderMaterielPlan/listAll',
    method: 'get',
    params: query
  })
}

// 查询物料数量计划详细
export function getOrderMaterielPlan(planId) {
  return request({
    url: '/order/orderMaterielPlan/' + planId,
    method: 'get'
  })
}

// 新增物料数量计划
export function addOrderMaterielPlan(data) {
  return request({
    url: '/order/orderMaterielPlan',
    method: 'post',
    data: data
  })
}

// 修改物料数量计划
export function updateOrderMaterielPlan(data) {
  return request({
    url: '/order/orderMaterielPlan',
    method: 'put',
    data: data
  })
}

// 删除物料数量计划
export function delOrderMaterielPlan(planId) {
  return request({
    url: '/order/orderMaterielPlan/' + planId,
    method: 'delete'
  })
}

// 复制物料数量计划
export function copyOrderMaterielPlan(data) {
  return request({
    url: '/order/orderMaterielPlan/copy',
    method: 'post',
    data: data
  })
}