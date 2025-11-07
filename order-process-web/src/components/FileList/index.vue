<template>
  <div>
    <el-dialog title="文件列表" :visible.sync="open" width="50%" append-to-body>
      <el-table :data="fileList" border>
        <el-table-column label="附件名称" prop="fileName" align="center">
          <template slot-scope="scope">
              <el-button type="text" @click="handleView(scope.row)">{{scope.row.fileName}}</el-button>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="文件类型" prop="fileType" align="center" width="100"></el-table-column>
        <el-table-column label="上传状态" prop="uploadStatus" align="center" width="100"></el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFile">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "FileList",
    data() {
      return {
        baseUrl: process.env.VUE_APP_BASE_API,
        open: false,
        // 文件列表
        fileList: [],
        // 表单数据
        form: {},
        // 文件属性
        attr: null
      };
    },
    methods: {
      /**
       * 查看文件
       */
      handleView(row){
        window.open(this.baseUrl+row.url)
      },
      /**
       * 提交文件
       */
      submitFile() {
        let urls = this.fileList.map(item => item.url);
        this.$set(this.form, this.attr, urls.join(','));
        this.$emit('updateFile', this.form);
        this.open = false;
      },
      /**
       * 删除文件
       */
      handleDelete(index) {
        this.fileList.splice(index, 1)
      },
      /**
       * @param {Object} fileUrls 打开弹框
       */
      handleOpen(obj, attr) {
        this.fileList = [];
        if (obj != null && !obj[attr]) {
          this.$notify.error({
            title: '错误',
            message: '没有文件可展示'
          });
          return;
        }
        this.open = true;
        this.form = obj;
        this.attr = attr;

        let fileUrls = obj[attr];
        let fileList = fileUrls.split(',');
        let _this = this;
        fileList.forEach((item, index) => {
          let file = {
            url: item,
            fileName: _this.getFileName(item),
            fileType: _this.getExtension(item),
            uploadStatus: '成功'
          };
          _this.fileList.push(file);
        })
      },
      // 获取文件名称
      getFileName(name) {
        if (name.lastIndexOf("/") > -1) {
          return name.slice(name.lastIndexOf("/") + 1);
        } else {
          return "";
        }
      },
      // 获取文件后缀名
      getExtension(name) {
        return name.substring(name.lastIndexOf('.') + 1);
      },
    }
  };
</script>
