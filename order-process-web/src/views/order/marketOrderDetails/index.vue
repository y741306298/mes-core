<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <template v-if="isEdit">
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="materielId">
              <el-select v-model="form.materielId" filterable placeholder="请选择" @change="materielChange" style="width: 100%">
                <el-option v-for="item in materielList" :key="item.materielId" :label="item.materielName" :value="item.materielId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料编号" prop="materielNo">
              <el-select v-model="form.materielNo" filterable placeholder="请选择" @change="materielNoChange" style="width: 100%">
                <el-option v-for="item in materielList" :key="item.materielNo" :label="item.materielNo" :value="item.materielNo"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="规格型号" prop="materielSpec">
              <el-input v-model="form.materielSpec" placeholder="请输入规格型号" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料类型" prop="materielType">
              <el-select v-model="form.materielType" filterable placeholder="请选择" :disabled="true" style="width: 100%">
                <el-option v-for="dict in dict.type.materiel_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="编码" prop="coding">
              <el-input v-model="form.coding" placeholder="请输入编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="材质" prop="texture">
              <el-input v-model="form.texture" placeholder="请输入材质" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数量" prop="detailsNum">
              <el-input v-model="form.detailsNum" type="number" @blur="calculateTotalPrice()" placeholder="请输入数量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="materielUnit">
              <el-input v-model="form.materielUnit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="单价" prop="detailsPrice">
              <el-input v-model="form.detailsPrice" type="number" @blur="calculateTotalPrice()" placeholder="请输入单价" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总价" prop="detailsAmount">
              <el-input v-model="form.detailsAmount" placeholder="请输入总价" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品附件" prop="attachments">
              <file-upload-button v-model="form.attachments" s></file-upload-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="detailsRemark">
              <el-input v-model="form.detailsRemark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>

      </template>

      <div v-for="(item,index) in marketOrderDetailsList" class="order-details-list">
        <el-row>
          <el-col :span="23">
            <el-descriptions title="" :column="2">
              <el-descriptions-item label="产品名称">
                <template v-if="isEdit">
                  {{item.materielName}}
                </template>
                <template else>
                  {{item.materielVo == null ? item.materielId : item.materielVo.materielName}}
                </template>
              </el-descriptions-item>
              <el-descriptions-item label="物料编号">{{item.materielNo}}</el-descriptions-item>
              <el-descriptions-item label="规格型号">{{item.materielSpec}}</el-descriptions-item>
              <el-descriptions-item label="物料类型">
                <dict-tag  :options="dict.type.materiel_type" :value="item.materielType" />
              </el-descriptions-item>
              <el-descriptions-item label="编码">{{item.coding}}</el-descriptions-item>
              <el-descriptions-item label="材质">{{item.texture}}</el-descriptions-item>
              <template v-if="foldOpen && index == foldIndex">
                <el-descriptions-item label="数量" v-if="show">{{item.detailsNum}}</el-descriptions-item>

                <el-descriptions-item label="单位">{{item.materielUnit}}</el-descriptions-item>
                <el-descriptions-item label="单价" v-if="show">{{item.detailsPrice}}</el-descriptions-item>
                <el-descriptions-item label="总价" v-if="show">{{item.detailsAmount}}</el-descriptions-item>

                <el-descriptions-item label="备注">{{item.detailsRemark}}</el-descriptions-item>

                <el-descriptions-item label="产品附件">
                  <el-tag class="mr5 mb5"
                    v-for="(file,fileIndex) in getFileNamesFromUrl(item.attachments)">{{file}}</el-tag>
                </el-descriptions-item>

              </template>
            </el-descriptions>
          </el-col>
          <el-col :span="1" v-if="isEdit">
            <div>
              <el-button icon="el-icon-edit" @click="handleEdit(index)" type="text"></el-button>
            </div>
            <div>
              <el-button icon="el-icon-document-copy" @click="handleCopy(index)" type="text"></el-button>
            </div>
            <div>
              <el-button icon="el-icon-remove-outline" @click="handleDelete(item,index)" type="text"></el-button>
            </div>
          </el-col>
        </el-row>
        <!-- <el-table v-if="foldOpen && index == foldIndex" :data="item.boomVoList" border>
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column prop="materielName" label="产品名称" />
          <el-table-column prop="materielSpec" label="规格" />
          <el-table-column prop="isCriterion" label="类型">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.is_criterion" :value="scope.row.isCriterion" />
            </template>
          </el-table-column>
          <el-table-column prop="boomNum" label="数量" />
          <el-table-column prop="totalNum" label="总数量" />
        </el-table> -->

        <div class="foldBtn">
          <el-button size="mini" type="text" @click="handleFold(index)">
            {{(foldOpen && index == foldIndex) ? '收 起' : '展 开'}}
          </el-button>
        </div>
      </div>
    </el-form>

    <!-- 封装类型弹框 -->
    <package-type @getList="getPackageTypeList" ref="packageTypeRef"></package-type>

    <!-- 测试类型弹框 -->
    <test-type @getList="getTestTypeList" ref="testTypeRef"></test-type>

    <!-- 测试方式弹框 -->
    <test-mode @getList="getTestModeList" ref="testModeRef"></test-mode>

    <!-- 选择物料 -->
    <materiel-select ref="materielSelectRef"></materiel-select>

  </div>
</template>

<script>
  import {
    listMarketOrderDetails,
    listMarketOrderDetailsAll,
    getMarketOrderDetails,
    delMarketOrderDetails,
    addMarketOrderDetails,
    updateMarketOrderDetails
  } from "@/api/order/marketOrderDetails";

  import {
    listMaterielAll,
    getMateriel
  } from "@/api/order/materiel";

  import {
    listPackageTypeAll
  } from "@/api/order/packageType";

  import {
    listTestTypeAll
  } from "@/api/order/testType";

  import {
    listTestModeAll
  } from "@/api/order/testMode";

  import {
    getFileData
  } from "@/api/order/orderBoom";

  import packageType from "@/views/order/packageType/index"
  import testType from "@/views/order/testType/index"
  import testMode from "@/views/order/testMode/index"
  import materielSelect from "@/views/order/materiel/select"

  import {
    checkPermi,
    checkRole
  } from "@/utils/permission";
  import store from '@/store' // 权限判断函数


  export default {
    name: "MarketOrderDetails",
    props: ['isEdit', 'orderId', 'userId'],
    dicts: ['package_type', 'test_type', 'test_mode', 'is_criterion','materiel_type'],
    components: {
      packageType,
      testType,
      testMode,
      materielSelect
    },
    data() {
      return {
        show: false,
        // 销售单详情表格数据
        marketOrderDetailsList: [],
        // 查询参数
        queryParams: {
          orderId: null
        },
        // 物料列表
        materielList: [],
        // 封装类型列表
        packageTypeList: [],
        // 测试类型列表
        testTypeList: [],
        // 测试方式列表
        testModeList: [],
        // 折叠收起开关
        foldOpen: false,
        foldIndex: null,
        form: {},
        // 表单校验
        rules: {
          detailsPrice: [{
            required: true,
            message: "单价不能为空",
            trigger: "blur"
          }],
          detailsNum: [{
            required: true,
            message: "数量不能为空",
            trigger: "blur"
          }],
          materielId: [{
            required: true,
            message: "产品名称不能为空",
            trigger: "blur"
          }],
        },
      };
    },
    watch: {
      orderId: function() {
        this.getList();
      }
    },
    created() {
      this.getList();
      this.reset();
      this.getMaterielList();
      this.isShow();
      // this.getPackageTypeList();
      // this.getTestTypeList();
      // this.getTestModeList();
    },
    methods: {
      checkRole,

      materielChange(materielId){
        this.materielList.forEach(item=>{
          if(item.materielId == materielId){
            this.form.materielNo = item.materielNo;
            this.form.materielSpec = item.materielSpec;
            this.form.materielType = item.typeId;
          }
        })
      },

      materielNoChange(materielNo){
        // alert(materielNo);
        this.materielList.forEach(item=>{
          if(item.materielNo == materielNo){
            this.form.materielNo = item.materielNo;
            this.form.materielSpec = item.materielSpec;
            this.form.materielType = item.typeId;
            this.form.materielId = item.materielId;
          }
        })
      },

      /**
       * 获取boom单文件数据列表
       */
      getBoomFileData(fileUrl) {
        return new Promise((resolve, reject) => {
          let data = {
            fileUrl: fileUrl
          };
          getFileData(data).then(res => {
            resolve(res.data);
          })
        })

      },
      /** 下载模板操作 */
      boomImportTemplate() {
        this.download('order/orderBoom/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
      },
      /**
       * 统计订单总金额
       */
      totalOrderAmount() {
        let totalNum = this.marketOrderDetailsList.reduce((total, item) => total + Number(item.detailsNum || 0), 0);
        let totalAmount = this.marketOrderDetailsList.reduce((total, item) => total + Number(item.detailsAmount || 0),
          0);

        this.$emit('setTotalInfo', {
          totalNum: totalNum,
          totalAmount: totalAmount
        })
      },
      /**
       * 编辑
       */
      handleEdit(index) {
        this.handleCopy(index);
        this.marketOrderDetailsList.splice(index, 1)
        this.totalOrderAmount();
      },
      /**
       * 复制
       */
      handleCopy(index) {
        let orderDetails = this.marketOrderDetailsList[index];
        orderDetails.materielName = "";
        this.form = JSON.parse(JSON.stringify(orderDetails));
      },
      /**
       * 新增产品
       */
      async handleAddMarketOrderDetails() {
        let bool = await this.formDetection(this.form);
        if (bool) {
          this.reset();
        }
      },
      /**
       * 保存产品信息
       */
      async marketOrderDetailsSave() {

        let boomList = null;
        if(this.form.boomFile){
          boomList =  await this.getBoomFileData(this.form.boomFile);
        }

        this.$refs["form"].validate(valid => {
          if (valid) {

            if (this.form.boomFile) {
              // 计算总数量
              for (var i = 0; i < boomList.length; i++) {
                let totalNum = parseInt(this.form.detailsNum) * parseInt(boomList[i].boomNum);
                this.$set(boomList[i], 'totalNum', totalNum)
              }
              this.$set(this.form, 'boomVoList', boomList);
            }

            let item = JSON.parse(JSON.stringify(this.form));
            this.marketOrderDetailsList.push(item);
            // 重置表单
            this.$nextTick(() => {
              this.reset();
            })
            this.totalOrderAmount();

          }
        })

      },
      /**
       * 展开收起
       */
      handleFold(index) {
        if (index == this.foldIndex) {
          this.foldOpen = !this.foldOpen;
        } else {
          this.foldOpen = true;
        }
        this.foldIndex = index;
      },
      // 表单重置
      reset() {
        this.form = {
          detailsId: null,
          detailsNo: null,
          orderId: null,
          materielId: null,
          materielName: null,
          materielType: null,
          materielSize: null,
          yieldNo: null,
          drawingNo: null,
          supplierPn: null,
          detailsNum: null,
          materielUnit: null,
          detailsPrice: null,
          detailsAmount: null,
          packageModel: null,
          packageTypeId: null,
          packageSize: null,
          pagkageNum: null,
          testTypeId: null,
          testMode: null,
          detailsRemark: null,
          attachments: null,
          boomFile: null,
          jobDrawing: null,
          jobProgram: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /**
       * 获取选中的物料
       */
      getSelectMateriel(ids) {
        if (ids.length > 1) {
          this.$notify({
            title: '警告',
            message: '每次最多可选择一个物料',
            type: 'warning'
          });
          return;
        }
        // 获取物料详情
        getMateriel(ids).then(res => {
          this.$set(this.form, 'materielName', res.data.materielName)
          this.$set(this.form, 'materielId', res.data.materielId)
          this.$set(this.form, 'materielType', res.data.materielTypeVo != null ? res.data.materielTypeVo.typeName :
            '')
          this.$set(this.form, 'materielSize', res.data.materielSize)
          this.$set(this.form, 'detailsPrice', res.data.sellPrice)
          this.$set(this.form, 'boomFile', res.data.boom)
        })
      },
      /**
       * 选择物料
       */
      handleSelectMaterie() {
        this.$refs.materielSelectRef.handleOpen()
      },
      /**
       * 打开测试方式弹框
       */
      handleOpenTestMode() {
        this.$refs.testModeRef.handleOpen();
      },
      /**
       * 获取测试方式列表
       */
      getTestModeList() {
        listTestModeAll().then(res => {
          this.testModeList = res.data;
        })
      },
      /**
       * 打开测试类型弹框
       */
      handleOpenTestType() {
        this.$refs.testTypeRef.handleOpen();
      },
      /**
       * 获取测试类型列表
       */
      getTestTypeList() {
        listTestTypeAll().then(res => {
          this.testTypeList = res.data;
        })
      },
      /**
       * 打开封装类型弹框
       */
      handleOpenPackageType() {
        this.$refs.packageTypeRef.handleOpen();
      },
      /**
       * 获取封装类型列表
       */
      getPackageTypeList() {
        listPackageTypeAll().then(res => {
          this.packageTypeList = res.data;
        })
      },
      /**
       * 计算总价
       */
      calculateTotalPrice() {
        const {
          detailsPrice,
          detailsNum
        } = this.form;
        this.form.detailsAmount = parseFloat(detailsPrice) * parseFloat(detailsNum)
      },
      /**
       * 获取物料列表
       */
      getMaterielList() {
        listMaterielAll().then(res => {
          this.materielList = res.data;
        })
      },
      /** 查询销售单详情列表 */
      getList() {
        this.loading = true;
        if (!this.orderId) {
          this.marketOrderDetailsList = [];
          this.loading = false;
          return
        }
        this.queryParams.orderId = this.orderId;
        listMarketOrderDetailsAll(this.queryParams).then(response => {
          this.marketOrderDetailsList = response.data;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.detailsId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        const item = {
          detailsId: null,
          detailsNo: null,
          orderId: null,
          materielId: null,
          materielType: null,
          materielSize: null,
          yieldNo: null,
          drawingNo: null,
          supplierPn: null,
          detailsNum: 0,
          materielUnit: null,
          detailsPrice: 0,
          detailsAmount: 0,
          packageModel: null,
          packageTypeId: null,
          packageSize: null,
          pagkageNum: null,
          testTypeId: null,
          testMode: null,
          detailsRemark: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.marketOrderDetailsList.push(item);
      },
      // /** 修改按钮操作 */
      // handleUpdate(row) {
      //   this.reset();
      //   const detailsId = row.detailsId || this.ids
      //   getMarketOrderDetails(detailsId).then(response => {
      //     this.form = response.data;
      //     this.open = true;
      //     this.title = "修改销售单详情";
      //   });
      // },
      /** 删除按钮操作 */
      handleDelete(row, index) {
        const detailsIds = row.detailsId;
        if (detailsIds) {
          this.$modal.confirm('确认删除？').then(function() {
            return delMarketOrderDetails(detailsIds);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(() => {});
        } else {
          this.marketOrderDetailsList.splice(index, 1)
          this.totalOrderAmount();
        }

      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/marketOrderDetails/export', {
          ...this.queryParams
        }, `marketOrderDetails_${new Date().getTime()}.xlsx`)
      },
      setMarketOrderDetailsList(sodl){
        this.marketOrderDetailsList = sodl;
      },
      isShow(){
        const permissions = store.getters && store.getters.permissions;
        if(permissions.includes("*:*:*")||permissions.includes("order:show:price")){
          this.show = true;
        }
      }
    }
  };
</script>
<style>
  .foldBtn {
    padding: 2px;
    width: 100%;
    text-align: center;
    /* border: #eceef4 solid 1px;
    border-top: 0px; */
  }
</style>
