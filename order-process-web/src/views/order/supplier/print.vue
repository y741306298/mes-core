<template>
  <div class="app-container">
    <el-dialog title="打印" :visible.sync="open" width="70%">
      <div id="printAre">
        <div style="text-align: center;">
          <h1>客 户</h1>
        </div>
        <hr class="mt20 mb20"/>
        <el-descriptions title="" size="medium" :column="2">
          <el-descriptions-item label="供应商名称">{{form.supplierName}}</el-descriptions-item>
          <el-descriptions-item label="供应商类型">
            <dict-tag :options="dict.type.supplier_type" :value="form.supplierType" />
          </el-descriptions-item>
          <el-descriptions-item label="联系人">{{form.linkman}}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{form.phone}}</el-descriptions-item>
          <el-descriptions-item :span="2" label="业务员" v-if="form.user">{{form.user.userName}}</el-descriptions-item>
        </el-descriptions>
        <hr class="mt20 mb20"/>
        <div class="mb20">备注:{{form.remark}}</div>
        <div class="mt20">
          <span>地址:</span>
          <span class="mt10">
            {{form.action}}
          </span>
        </div>

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" v-print="printAre">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import {
    listSupplier,
    listSupplierAll,
    getSupplier,
    delSupplier,
    addSupplier,
    updateSupplier
  } from "@/api/order/supplier";


  export default {
    name: "SupplierPrint",
    dicts:['supplier_type'],
    data() {
      return {
        // 打印设置
        printAre: {
          id: "printAre",
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 是否打开弹框
        open: false,
        // 供应商地址列表
        supplierAddressList: []
      };
    },
    created() {},
    methods: {

      /**
       * 打开弹框
       */
      handleOpen(supplierId) {
        this.open = true;
        this.getSupplierInfo(supplierId);
        this.getSupplierAddressList(supplierId);
      },
      /**
       * 获取供应商信息
       */
      getSupplierInfo(supplierId) {
        getSupplier(supplierId).then(res => {
          this.form = res.data;
        })
      },

    }
  };
</script>
<style>
  .el-dialog__body {
    background-color: white !important;
  }

  @media print {
    @page {
      margin: 8px 20px 0px 27px;
      /* margin-left: 30px; */
      /* size: auto; */
    }

    @media print {
      .print-content {
        max-height: 50vh;
        /* 设置内容区域的最大高度为视口高度的 90% */
        /* 其他打印样式 */
      }
    }

    body {
      /* padding: 10mm; */
    }
  }
</style>
