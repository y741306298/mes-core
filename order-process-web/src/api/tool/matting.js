import request from '@/utils/request'

// svg抠图
export function svgMatting(data) {
  return request({
    url: '/img/svgMatting',
    method: 'post',
    data
  })
}

// svg抠图裁剪
export function svgMattingCutting(data) {
  return request({
    url: '/img/svgMattingCutting',
    method: 'post',
    data
  })
}

// 多边形排版
export function polygonNest(data) {
  return request({
    url: '/img/polygonNest',
    method: 'post',
    data
  })
}

// plt切割
export function cutPlt(data) {
  return request({
    url: '/img/cutPlt',
    method: 'post',
    data
  })
}

// svg附加模板
export function appendTemplate(data) {
  return request({
    url: '/img/appendTemplate',
    method: 'post',
    data
  })
}
