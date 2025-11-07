/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */
/**
 * @param {Object} url 根据URL获取文件名称
 */
export function getFileNamesFromUrl(url) {
  let fileNames = [];
  
  if(!url){
    return fileNames;
  }
  // 根据逗号分隔URL段
  let urlSegments = url.split(',');


  // 遍历每个URL段，提取文件名
  urlSegments.forEach(segment => {
    // 从每个段中提取文件名
    let startIndex = segment.lastIndexOf('/') + 1;
    let endIndex = segment.lastIndexOf('?') !== -1 ? segment.lastIndexOf('?') : segment.length;
    let fileName = segment.substring(startIndex, endIndex);

    // 如果文件名不为空则添加到列表
    if (fileName.trim() !== '') {
      fileNames.push(fileName.trim());
    }
  });

  return fileNames;
}

// 校验表单是否有数据
export function formDetection(form) {
  /* 第二步，定义一个初始标识为0，遍历form对象，此时的对象就三种情况，null或空字符串
        或者有输入内容值。如果遍历获取的属性值为null或空字符串就让num不变，如果不为null
            不为空字符串就说明用户输入内容了，就把标识num加上一。最终去判断这个num的值
            如果num的值最终还是0，说明用户始终没有输入内容，就允许用户直接离开。如果num的
            值最终大于0，就说明用户输入内容了，然后就询问用户是否要保留刚刚输入的内容。
  */
  return new Promise((resolve, reject) => {
    let num = 0
    for (const key in form) {
      if (!form[key]) {
        num = num + 0
      } else {
        num = num + 1
      }
    }
    // 第三步，根据标识num的最终值，去做流程逻辑控制判断
    if (num > 0) {
      this.$confirm('检测到未保存的内容，是否确认操作', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '取消',
          cancelButtonText: '确认'
        })
        .then(() => {
          resolve(false);
        })
        .catch(action => {
          resolve(true);
        });
    } else {
      resolve(false);
    }
  })

}

//获取对象属性--支出多级获取
export function getObjAttr(row, prop) {
  var lists = prop.split('.');
  lists = lists || [];
  //默认对象
  var currentObj = row;
  lists.forEach(element => {
    if (currentObj == null) {
      return null
    }
    //把当前对象赋值到全局变量，用来下次循环获取属性
    currentObj = currentObj[element];

  });
  return currentObj;
}

/**
 * 刷新列表
 */
export function refreshCloumn(_this) {
  const cacheVal = _this.$cache.local.get(this.cacheKey);
  if (cacheVal != null) {
    _this.cacheCloumnList = JSON.parse(cacheVal)
  } else {
    _this.cacheCloumnList = _this.columnList;
  }
  // console.log(this.cacheCloumnList)
  _this.$nextTick(() => {
    if (_this.$refs.brtTable) {
      _this.$refs.brtTable.doLayout();
    }

  })
}

// 日期格式化
export function parseTime(time, pattern) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    } else if (typeof time === 'string') {
      time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

// 表单重置
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields();
  }
}

// 添加日期范围
export function addDateRange(params, dateRange, propName) {
  let search = params;
  search.params = typeof(search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search
    .params : {};
  dateRange = Array.isArray(dateRange) ? dateRange : [];
  if (typeof(propName) === 'undefined') {
    search.params['beginTime'] = dateRange[0];
    search.params['endTime'] = dateRange[1];
  } else {
    search.params['begin' + propName] = dateRange[0];
    search.params['end' + propName] = dateRange[1];
  }
  return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
  if (value === undefined) {
    return "";
  }
  var actions = [];
  Object.keys(datas).some((key) => {
    if (datas[key].value == ('' + value)) {
      actions.push(datas[key].label);
      return true;
    }
  })
  if (actions.length === 0) {
    actions.push(value);
  }
  return actions.join('');
}

// 回显数据字典（字符串、数组）
export function selectDictLabels(datas, value, separator) {
  if (value === undefined || value.length === 0) {
    return "";
  }
  if (Array.isArray(value)) {
    value = value.join(",");
  }
  var actions = [];
  var currentSeparator = undefined === separator ? "," : separator;
  var temp = value.split(currentSeparator);
  Object.keys(value.split(currentSeparator)).some((val) => {
    var match = false;
    Object.keys(datas).some((key) => {
      if (datas[key].value == ('' + temp[val])) {
        actions.push(datas[key].label + currentSeparator);
        match = true;
      }
    })
    if (!match) {
      actions.push(temp[val] + currentSeparator);
    }
  })
  return actions.join('').substring(0, actions.join('').length - 1);
}

// 字符串格式化(%s )
export function sprintf(str) {
  var args = arguments,
    flag = true,
    i = 1;
  str = str.replace(/%s/g, function() {
    var arg = args[i++];
    if (typeof arg === 'undefined') {
      flag = false;
      return '';
    }
    return arg;
  });
  return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str) {
  if (!str || str == "undefined" || str == "null") {
    return "";
  }
  return str;
}

// 数据合并
export function mergeRecursive(source, target) {
  for (var p in target) {
    try {
      if (target[p].constructor == Object) {
        source[p] = mergeRecursive(source[p], target[p]);
      } else {
        source[p] = target[p];
      }
    } catch (e) {
      source[p] = target[p];
    }
  }
  return source;
};

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  };

  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + "=";
    if (value !== null && value !== "" && typeof(value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== "" && typeof(value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']';
            var subPart = encodeURIComponent(params) + "=";
            result += subPart + encodeURIComponent(value[key]) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result
}

// 验证是否为blob格式
export function blobValidate(data) {
  return data.type !== 'application/json'
}

//左右拖拽
export function dragControllerDiv() {
  let left = document.getElementById('drag-left-content')

  let line = document.getElementById('drag-resize')
  let right = document.getElementById('drag-right-content')
  // 鼠标按下事件
  line.onmousedown = function(e) {
    left.classList.remove("popup-motion");
    let startX = e.clientX
    line.left = line.offsetLeft
    // 鼠标拖动事件
    document.onmousemove = function(e) {
      let moveLen = line.left + (e.clientX - startX)
      // if (
      //   moveLen >= document.body.clientWidth * 0.1 &&
      //   moveLen <= document.body.clientWidth * 0.4
      // ) {
      line.style.left = moveLen + 'px'
      left.style.width = moveLen + 'px'
      right.style.width = document.body.clientWidth - moveLen + 'px'
      // }
    }
    document.onmouseup = function() {
      document.onmousemove = null
      document.onmouseup = null
    }
  }
  // left.classList.add("popup-motion")
}

export function dragChangeHeight(drag, panel) {
  var dragEl = document.getElementById(drag)
  var panelEl = document.getElementById(panel)
  dragEl.onmousedown = function(ev) {
    var disH = panelEl.offsetHeight
    var disY = ev.clientY
    var disT = panelEl.offsetTop
    var b = ''

    document.onmousemove = function(ev) {
      panelEl.style.height = disH + (ev.clientY - disY) + 'px'
      // panelEl.style.top = disL - (ev.clientY - disY) + 'px'
    }

    document.onmouseup = function() {
      document.onmousemove = document.onmouseup = null
    }
    return false
  }
}
