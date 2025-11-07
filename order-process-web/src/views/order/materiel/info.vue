<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">基本信息</div>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
    <el-divider></el-divider>

    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="产品类型" prop="typeId">
            <el-select v-model="form.typeId" filterable class="drag-screenful-contnet">
              <el-option v-for="dict in dict.type.materiel_type" :key="dict.value" :label="dict.label"
                :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="产品编号" prop="materielNo">
            <el-input v-model="form.materielNo" placeholder="请输入产品编号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="产品名称" prop="materielName">
            <el-input v-model="form.materielName" placeholder="请输入产品名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="型号规格" prop="materielSpec">
            <el-input v-model="form.materielSpec" placeholder="请输入型号规格" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="数量" prop="materielNum">
            <el-input v-model="form.materielNum" @blur="computeTotalPrice" type="number" placeholder="请输入数量" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成本" prop="purchasePrice">
            <el-input v-model="form.purchasePrice" @blur="computeTotalPrice" type="number" placeholder="请输入采购单价" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="销售单价" prop="sellPrice">
            <el-input v-model="form.sellPrice" type="number" placeholder="请输入销售单价" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="materielUnit">
            <el-input v-model="form.materielUnit" placeholder="请输入单位" />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="8">
          <el-form-item label="总价" prop="totalPrice">
            <el-input v-model="form.totalPrice" type="number" placeholder="请输入总价" />
          </el-form-item>
        </el-col> -->
      </el-row>


      <el-row>
        <el-col :span="12">
          <el-form-item label="库位" prop="location">
            <el-input v-model="form.location" placeholder="请输入库位" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供应商" prop="supplierId">
            <el-input v-model="form.supplier" placeholder="请输供应商" />
          </el-form-item>
        </el-col>
      </el-row>


      <!-- <el-row>

      </el-row> -->
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="materielRemark">
            <el-input v-model="form.materielRemark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-row>
        <el-col :span="24">
          <el-form-item label="boom单" prop="boom">
            <file-upload-button v-model="form.boom" />
          </el-form-item>
        </el-col>
      </el-row> -->
      <el-row>
        <el-col :span="24">
          <el-form-item label="附件" prop="files">
            <file-upload-button v-model="form.files" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 自定义字段列表 -->
      <form-field-list ref="formFieldListRef" :colSpan="8" businessType="BrtMateriel" :formFieldList="form.otherFields"
        :id="form.materielId" />
    </el-form>

    <!-- 产品类型 -->
    <materiel-type @getList="getMaterielTypeList" ref="materielTypeRef"></materiel-type>

  </div>
</template>

<script>
  import {
    listMateriel,
    listMaterielAll,
    getMateriel,
    delMateriel,
    addMateriel,
    updateMateriel
  } from "@/api/order/materiel";

  import {
    listSupplierAll
  } from "@/api/order/supplier";

  import {
    listMaterielTypeAll
  } from "@/api/order/materielType";

  import materielType from "@/views/order/materielType/index"

  export default {
    name: "MaterielInfo",
    components: {
      materielType
    },
    dicts: ["materiel_type"],
    data() {
      return {
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 产品类型列表
        materielTypeList: [],
        supplierList: [],
      };
    },
    created() {
      // const materielId = this.materielId;
      // this.handleUpdate(materielId);
      // this.getMaterielTypeList();
      this.getListSupplierAll();
    },
    methods: {
      getListSupplierAll(){
        listSupplierAll().then(res=>{
          this.supplierList = res.data;
        })
      },
      /**
       * 计算总价
       */
      computeTotalPrice() {
        this.form.totalPrice = parseFloat(this.form.materielNum) * parseFloat(this.form.purchasePrice)
      },
      /** 修改按钮操作 */
      handleUpdate(materielId) {
        this.reset();
        if (!materielId) {
          return;
        }
        getMateriel(materielId).then(response => {
          this.form = response.data;
        });
      },
      /**
       * 打开产品类型弹框
       */
      handleOpenType() {
        this.$refs.materielTypeRef.handleOpen()
      },
      /**
       * 获取产品类型列表
       */
      getMaterielTypeList() {
        listMaterielTypeAll().then(res => {
          this.materielTypeList = res.data;
        })
      },
      // 表单重置
      reset() {
        this.form = {
          materielId: null,
          materielNo: new Date().getTime(),
          typeId: null,
          materielName: null,
          materielSpec: null,
          materielSize: null,
          materielNum: 0,
          warningNum: null,
          lockNum: null,
          sellPrice: 0,
          purchasePrice: 0,
          totalPrice: 0,
          materielRemark: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null,
          boom: null,
          files: null
        };
        this.resetForm("form");
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let fieldList = this.$refs.formFieldListRef.fieldList;
            this.$set(this.form,'otherFields',JSON.stringify(fieldList))
            if (this.form.materielId != null) {
              updateMateriel(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                // this.backPrice();
                this.$emit('getList');
                this.$emit('close');
              });
            } else {
              addMateriel(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                // this.backPrice();
                this.$emit('getList');
                this.$emit('close');
              });
            }
          }
        });
      },
      // 返回上个页面
      backPrice() {
        this.reset();
        const obj = {
          path: "/materiel/materiel"
        };
        this.$tab.closeOpenPage(obj);
      },
    }
  };
</script>
