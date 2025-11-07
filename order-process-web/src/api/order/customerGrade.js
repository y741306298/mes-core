import request from '@/utils/request'

// 查询客户等级列表
export function listCustomerGrade(query) {
  return request({
    url: '/order/customerGrade/list',
    method: 'get',
    params: query
  })
}

// 查询全部客户等级列表
export function listCustomerGradeAll(query) {
  return request({
    url: '/order/customerGrade/listAll',
    method: 'get',
    params: query
  })
}

// 查询客户等级详细
export function getCustomerGrade(gradeId) {
  return request({
    url: '/order/customerGrade/' + gradeId,
    method: 'get'
  })
}

// 新增客户等级
export function addCustomerGrade(data) {
  return request({
    url: '/order/customerGrade',
    method: 'post',
    data: data
  })
}

// 修改客户等级
export function updateCustomerGrade(data) {
  return request({
    url: '/order/customerGrade',
    method: 'put',
    data: data
  })
}

// 删除客户等级
export function delCustomerGrade(gradeId) {
  return request({
    url: '/order/customerGrade/' + gradeId,
    method: 'delete'
  })
}

// 复制客户等级
export function copyCustomerGrade(data) {
  return request({
    url: '/order/customerGrade/copy',
    method: 'post',
    data: data
  })
}