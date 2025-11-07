import request from '@/utils/request'

// 查询订单审批列表
export function listOrderExamine(query) {
  return request({
    url: '/order/orderExamine/list',
    method: 'get',
    params: query
  })
}

// 查询全部订单审批列表
export function listOrderExamineAll(query) {
  return request({
    url: '/order/orderExamine/listAll',
    method: 'get',
    params: query
  })
}

// 查询订单审批详细
export function getOrderExamine(examineId) {
  return request({
    url: '/order/orderExamine/' + examineId,
    method: 'get'
  })
}

// 新增订单审批
export function addOrderExamine(data) {
  return request({
    url: '/order/orderExamine',
    method: 'post',
    data: data
  })
}

// 修改订单审批
export function updateOrderExamine(data) {
  return request({
    url: '/order/orderExamine',
    method: 'put',
    data: data
  })
}

// 删除订单审批
export function delOrderExamine(examineId) {
  return request({
    url: '/order/orderExamine/' + examineId,
    method: 'delete'
  })
}

// 复制订单审批
export function copyOrderExamine(data) {
  return request({
    url: '/order/orderExamine/copy',
    method: 'post',
    data: data
  })
}

// 订单审批
export function audit(data) {
  return request({
    url: '/order/orderExamine/audit',
    method: 'post',
    data: data
  })
}

// 获取全部订单列表
export function orderAllList(data) {
  return request({
    url: '/order/orderExamine/orderAllList',
    method: 'get',
    params: data
  })
}

// 订单审批
export function nodeAudit(data) {
  return request({
    url: '/order/orderExamine/nodeAudit',
    method: 'post',
    data: data
  })
}

