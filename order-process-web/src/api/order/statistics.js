import request from '@/utils/request'


// 复制销售单
export function salesStatistics(data) {
    return request({
      url: '/order/statistics/salesStatistics',
      method: 'post',
      data: data
    })
  }

  // 复制销售单
export function marketStatistics(data) {
    return request({
      url: '/order/statistics/marketStatistics',
      method: 'post',
      data: data
    })
  }

  // 复制销售单
export function earning(data) {
    return request({
      url: '/order/statistics/earning',
      method: 'post',
      data: data
    })
  }

  // 复制销售单
export function deptOnTime(data) {
    return request({
      url: '/order/statistics/deptOnTime',
      method: 'post',
      data: data
    })
  }

    // 复制销售单
export function getHederData(data) {
  return request({
    url: '/order/statistics/getHederData',
    method: 'post',
    data: data
  })
}

export function picking(orderId) {
  return request({
    url: '/order/statistics/picking?orderId='+orderId,
    method: 'get',
  })
}

export function inInventory(inInventoryId) {
  return request({
    url: '/order/statistics/inInventory?inInventoryId='+inInventoryId,
    method: 'get',
  })
}


export function outInventory(outInventoryId) {
  return request({
    url: '/order/statistics/outInventory?outInventoryId='+outInventoryId,
    method: 'get',
  })
}

export function productInInventory(orderId) {
  return request({
    url: '/order/statistics/productInInventory?orderId='+orderId,
    method: 'get',
  })
}

export function firstarticle(orderId) {
  return request({
    url: '/order/statistics/firstarticle?orderId='+orderId,
    method: 'get',
  })
}

export function product(orderId) {
  return request({
    url: '/order/statistics/product?orderId='+orderId,
    method: 'get',
  })
}
