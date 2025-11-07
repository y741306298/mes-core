import request from '@/utils/request'



// 获取单号并且加一
export function getNoAndAdd(data) {
  return request({
    url: '/order/orderNo/getNoAndAdd',
    method: 'get',
    params: data
  })
}

// 获取单号但不加一
export function getNo(data) {
    return request({
      url: '/order/orderNo/getNo',
      method: 'get',
      params: data
    })
  }
