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
