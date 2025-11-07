<template>
  <div>
    <div id="luckysheet" class="luckysheet-wrap"></div>
  </div>
</template>

<script>
  import {
    exportSheetExcel
  } from "@/utils/excelutils"

  import {
    uploadFile
  } from "@/api/common/common"

  export default {
    props:['url'],
    data() {
      return {
        serverUrl: process.env.VUE_APP_BASE_API,
        // 配置项
        options: {
          container: 'luckysheet', // DOM容器的ID
          title: '', // 工作簿名称
          lang: 'zh', // 设定表格的语言
          showtoolbarConfig: {
            print: false, // 工具栏隐藏打印按钮
          },
          showsheetbarConfig: {
            add: false, // 底部sheet页隐藏新增sheet按钮
            menu: false, // 底部sheet页隐藏管理按钮
          },
          sheetRightClickConfig: {
            hide: false, // 隐藏，取消隐藏
            move: false, // 向左移，向右移
          },
          data: [],
        }
      };
    },
    watch:{
      url:function(){
        this.openSheetExcel(this.serverUrl+this.url);
      }
    },
    created() {

    },
    mounted() {
      this.openSheetExcel(this.serverUrl+this.url);
    },
    methods: {
      // 创建工作表
      createSheet() {
        // 初始化表格
        this.luckysheet.create(this.options)
        // 获取Luckysheet表格文件的数据
        this.sheetfile = this.luckysheet.getluckysheetfile()
        console.log(this.sheetfile)
      },
      //打开xlsx
      openSheetExcel(url) {
        let _this = this;
        if(!url){
          return
        }
        this.urlToFile(url, 'test.xlsx', 'application/xml').then(file => {
            LuckyExcel.transformExcelToLucky(file, function(exportJson, luckysheetfile) {
              if (exportJson.sheets == null || exportJson.sheets.length == 0) {
                alert("Failed to read the content of the excel file, currently does not support xls files!");
                return;
              }
              _this.$set(_this.options,"data",exportJson.sheets)
              window.luckysheet.create(_this.options);
            });
          })
          .catch(error => {
            console.error(error+"=======");
          });
      },
      //导出excel
      exportSheetExcel(name) {
        window.luckysheet.exitEditMode()
        const blob = exportSheetExcel(window.luckysheet, 'excel_template.xlsx')
        const _this = this;
        blob.then((res) => {
          //上传文件
          let formData = new FormData();
          formData.append('file', res,'excel_template.xlsx');
          // formData.append('fileUrl', this.url);
          uploadFile(formData).then(r => {
            _this.$emit('saveExcelFile',r.fileName)
            // _this.$modal.msgSuccess("保存成功");
          })

        })

      }
    }
  }
</script>
<style scoped>
  .luckysheet-wrap {
    margin: 0px;
    padding: 0px;
    /* position: absolute; */
    width: 100%;
    height: 91.3vh;
    left: 0px;
    /* top: 0px; */

  }
</style>
