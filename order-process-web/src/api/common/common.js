import request from '@/utils/request'

export function getFile(data) {
  return request({
    url: '/common/download',
    method: 'get',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    params: data
  })
}

// 文件替换
export function uploadReplace(data) {
  return request({
    url: '/common/uploadReplace',
    headers: {
      "Content-Type": "multipart/form-data"
    },
    method: 'post',
    data: data
  })
}

// 文件替换
export function urlToFile(data) {
  return request({
    url: '/common/urlToFile',
    method: 'post',
    data: data
  })
}

// 文件上传
export function uploadFile(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data
  })
}
