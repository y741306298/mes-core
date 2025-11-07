import request from '@/utils/request'

// 查询boom单列表
export function listOrderBoom(query) {
  return request({
    url: '/order/orderBoom/list',
    method: 'get',
    params: query
  })
}

// 查询全部boom单列表
export function listOrderBoomAll(query) {
  return request({
    url: '/order/orderBoom/listAll',
    method: 'get',
    params: query
  })
}

// 查询boom单详细
export function getOrderBoom(boomId) {
  return request({
    url: '/order/orderBoom/' + boomId,
    method: 'get'
  })
}

// 新增boom单
export function addOrderBoom(data) {
  return request({
    url: '/order/orderBoom',
    method: 'post',
    data: data
  })
}

// 修改boom单
export function updateOrderBoom(data) {
  return request({
    url: '/order/orderBoom',
    method: 'put',
    data: data
  })
}

// 删除boom单
export function delOrderBoom(boomId) {
  return request({
    url: '/order/orderBoom/' + boomId,
    method: 'delete'
  })
}

// 复制boom单
export function copyOrderBoom(data) {
  return request({
    url: '/order/orderBoom/copy',
    method: 'post',
    data: data
  })
}

// 获取文件数据列表
export function getFileData(data) {
  return request({
    url: '/order/orderBoom/getFileData',
    method: 'post',
    data: data
  })
}
