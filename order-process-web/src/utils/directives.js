import Vue from 'vue';
 
function getheigh(height){
      var tableH= document.getElementsByClassName('movetable')[0].offsetHeight
        console.log('tableH',tableH)
      return tableH
 }
 
 
// v-dialogDragWidth: 弹窗宽度拖大 拖小
Vue.directive('dialogDragWidth', {
    bind(el, binding, vnode, oldVnode) {
      // var oDiv = document.getElementById('div1');
      const oDiv = el.querySelector('.cursor');
      const table = el.getElementsByClassName('el-table')[0];
 
      oDiv.onmousedown = function(ev) {
        // var he= setTimeout(()=>{getheigh()},200)
        var tableH= getheigh()
 
        // 获取event对象，兼容性写法
        var ev = ev || event;
        // 鼠标按下时的位置
        var mouseDownX = ev.clientX;
        var mouseDownY = ev.clientY;
        var clientHeights=document.body.clientHeight //屏幕高度
        // 方块上下左右四个边的位置和方块的长宽
        var T0 = this.offsetTop;
        var B0 = this.offsetTop + this.offsetHeight;
        var L0 = this.offsetLeft;
        var R0 = this.offsetLeft + this.offsetWidth;
        var W = this.offsetWidth;
        var H = this.offsetHeight;
        // 设置方块的识别范围
        var areaT = T0 + 10;
        var areaB = B0 - 10;
        var areaL = L0 + 10;
        var areaR = R0 - 10;
        // 判断改变方块的大小的方向
        // 左
        var changeL = mouseDownX < areaL;
        // 右
        var changeR = mouseDownX > areaR;
        // 上
        var changeT = mouseDownY < areaT;
        // 下
        var changeB = mouseDownY > areaB;
        // IE8 取消默认行为-设置全局捕获
        if (oDiv.setCapture) {
          oDiv.setCapture();
        }
 
        document.onmousemove = function(ev) {
          var ev = ev || event;
          // 鼠标移动时的鼠标位置
          var mouseMoveX = ev.clientX;
          var mouseMoveY = ev.clientY;
 
          if (parseInt(clientHeights-mouseMoveY) < 20) {
 
          }else{
             table.style.height=(mouseMoveY -   mouseDownY) +tableH + 'px';
          }
        }
        document.onmouseup = function() {
          document.onmousemove = null;
          // 释放全局捕获
          if (oDiv.releaseCapture) {
            oDiv.releaseCapture();
          }
        }
        // return false;
      }
 
 
  }
})
 
export default getheigh