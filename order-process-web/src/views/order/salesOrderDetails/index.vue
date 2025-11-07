<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <template v-if="isEdit">
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="materielName">
              <el-input placeholder="请选择产品" v-model="form.materielName">
                <!-- <el-button slot="suffix" type="text" @click="handleSelectMaterie()">选择</el-button> -->
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目编号" prop="detailsNo">
              <el-input v-model="form.detailsNo" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品尺寸" prop="materielSize">
              <el-input v-model="form.materielSize" placeholder="请输入产品尺寸" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产单号" prop="yieldNo">
              <el-input v-model="form.yieldNo" placeholder="请输入生产单号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="图纸编号" prop="drawingNo">
              <el-input v-model="form.drawingNo" placeholder="请输入图纸编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产料号" prop="customerPn">
              <el-input v-model="form.customerPn" placeholder="请输入生产料号" />
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
            <el-form-item label="封装型号" prop="packageModel">
              <el-input v-model="form.packageModel" placeholder="请输入封装型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封装类型" prop="packageTypeId">
              <el-select v-model="form.packageTypeId" class="drag-screenful-contnet" filterable>
                <el-option v-for="dict in dict.type.package_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="封装尺寸" prop="packageSize">
              <el-input v-model="form.packageSize" placeholder="请输入封装尺寸" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测试类型" prop="testTypeId">
              <el-select v-model="form.testTypeId" filterable class="drag-screenful-contnet">
                <el-option v-for="dict in dict.type.test_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="测试方式" prop="testMode">
              <el-select v-model="form.testMode" filterable class="drag-screenful-contnet">
                <el-option v-for="dict in dict.type.test_mode" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="detailsRemark">
              <el-input v-model="form.detailsRemark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="boom单" prop="boomFile">
              <file-upload-button class="mr10" v-model="form.boomFile" :limit="1" :isDelete="false"></file-upload-button>
              <el-button type="text" @click="boomImportTemplate">下载模板</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工件图纸" prop="jobDrawing">
              <file-upload-button v-model="form.jobDrawing" s></file-upload-button>
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
            <el-form-item label="工件程序" prop="jobProgram">
              <file-upload-button v-model="form.jobProgram" s></file-upload-button>
            </el-form-item>
          </el-col>
        </el-row>
      </template>

      <div v-for="(item,index) in salesOrderDetailsList" class="order-details-list">
        <el-button size="mini" @click="submitForm(index)" type="primary" style="margin-left: 90%;">保存</el-button>
        <el-row>
          <el-col :span="23">
            <el-descriptions title="" :column="2">
              <el-descriptions-item label="产品名称">
                <template v-if="isEdit">
                  {{item.materielName}}
                </template>
                <template v-else>
                  {{item.materielVo == null ? item.materielId : item.materielVo.materielName}}
                </template>
              </el-descriptions-item>
              <el-descriptions-item label="项目编号">{{item.detailsNo}}</el-descriptions-item>
              <el-descriptions-item label="产品尺寸">{{item.materielSize}}</el-descriptions-item>
              <el-descriptions-item label="生产单号">{{item.yieldNo}}</el-descriptions-item>
              <el-descriptions-item label="图纸编号">{{item.drawingNo}}</el-descriptions-item>
              <el-descriptions-item label="生产料号">{{item.customerPn}}</el-descriptions-item>
              <template v-if="foldOpen && index == foldIndex">
                <el-descriptions-item label="数量" v-if="show">{{item.detailsNum}}</el-descriptions-item>

                <el-descriptions-item label="单位">{{item.materielUnit}}</el-descriptions-item>
                <el-descriptions-item label="单价" v-if="show">{{item.detailsPrice}}</el-descriptions-item>
                <el-descriptions-item label="总价" v-if="show">{{item.detailsAmount}}</el-descriptions-item>


                <el-descriptions-item label="封装型号">{{item.packageModel}}</el-descriptions-item>
                <el-descriptions-item label="封装类型">
                  <dict-tag :options="dict.type.package_type" :value="item.packageTypeId" />
                </el-descriptions-item>
                <el-descriptions-item label="封装尺寸">{{item.packageSize}}</el-descriptions-item>
                <el-descriptions-item label="测试类型">
                  <dict-tag :options="dict.type.test_type" :value="item.testTypeId" />
                </el-descriptions-item>
                <el-descriptions-item label="测试方式">
                  <dict-tag :options="dict.type.test_mode" :value="item.testMode" />
                </el-descriptions-item>
                <el-descriptions-item label="备注">{{item.detailsRemark}}</el-descriptions-item>
                <el-descriptions-item label="boom单" >
                  <div class="gtyl">

                  <file-upload-button v-if="!isEdit" class="mr10 " v-model="item.boomFile" :limit="1"></file-upload-button>
                  </div>

                  <!-- <el-tag class="mr5 mb5"
                    v-for="(file,fileIndex) in getFileNamesFromUrl(item.boomFile)">{{file}}</el-tag> -->
                </el-descriptions-item>
                <el-descriptions-item label="工件图纸">
                  <file-upload-button v-if="!isEdit" v-model="item.jobDrawing"></file-upload-button>
                  <!-- <el-tag class="mr5 mb5"
                    v-for="(file,fileIndex) in getFileNamesFromUrl(item.jobDrawing)">{{file}}</el-tag> -->
                </el-descriptions-item>
                <el-descriptions-item label="产品附件">
                  <file-upload-button v-if="!isEdit" v-model="item.attachments"></file-upload-button>
                  <!-- <el-tag class="mr5 mb5"
                    v-for="(file,fileIndex) in getFileNamesFromUrl(item.attachments)">{{file}}</el-tag> -->
                </el-descriptions-item>
                <el-descriptions-item label="工件程序">
                  <file-upload-button v-if="!isEdit" v-model="item.jobProgram"></file-upload-button>
                  <!-- <el-tag class="mr5 mb5"
                    v-for="(file,fileIndex) in getFileNamesFromUrl(item.jobProgram)">{{file}}</el-tag> -->
                </el-descriptions-item>
              </template>
              <!-- <el-descriptions-item label="联系地址">江苏省苏州市吴中区吴中大道 1188 号</el-descriptions-item> -->
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
        <el-table v-if="foldOpen && index == foldIndex" :data="item.boomVoList" border>
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
        </el-table>

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
    listSalesOrderDetails,
    listSalesOrderDetailsAll,
    getSalesOrderDetails,
    delSalesOrderDetails,
    addSalesOrderDetails,
    updateSalesOrderDetails,
    updateAndLock
  } from "@/api/order/salesOrderDetails";

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
    name: "SalesOrderDetails",
    props: ['isEdit', 'orderId', 'userId'],
    dicts: ['package_type', 'test_type', 'test_mode', 'is_criterion'],
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
        salesOrderDetailsList: [],
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
          materielName: [{
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
      this.isShow();
      // this.getMaterielList();
      // this.getPackageTypeList();
      // this.getTestTypeList();
      // this.getTestModeList();
    },
    methods: {

      checkRole,

      // async submitForm(index){
      //     let item = this.salesOrderDetailsList[index];
      //     if (item.boomFile) {
      //       // 识别boom文件列表
      //       let boomList = await this.getBoomFileData(item.boomFile);
      //       // 计算总数量
      //       for (var i = 0; i < boomList.length; i++) {
      //         let totalNum = parseInt(item.detailsNum) * parseInt(boomList[i].boomNum);
      //         this.$set(boomList[i], 'totalNum', totalNum)
      //       }
      //       this.$set(item, 'boomVoList', boomList);
      //       item = JSON.parse(JSON.stringify(item));
      //     }
      //     updateAndLock(item).then(res=>{
      //       this.$modal.msgSuccess("保存成功");
      //     })
      // },


      async submitForm(index){
        let item = this.salesOrderDetailsList[index];
        if (item.boomFile) {
          // 识别boom文件列表
          let boomList = await this.getBoomFileData(item);

          // 计算总数量
          for (var i = 0; i < boomList.length; i++) {
            let totalNum = parseInt(item.detailsNum) * parseInt(boomList[i].boomNum);
            this.$set(boomList[i], 'totalNum', totalNum)
          }
          this.$set(item, 'boomVoList', boomList);
          item = JSON.parse(JSON.stringify(item));
          this.$modal.msgSuccess("保存成功");
        }
        // updateAndLock(item).then(res=>{
        //   this.$modal.msgSuccess("保存成功");
        // })
      },


      /**
       * 获取boom单文件数据列表
       */
      getBoomFileData(item) {
        return new Promise((resolve, reject) => {
          // let data = {
          //   fileUrl: fileUrl
          // };
          // getFileData(data).then(res => {
          //   resolve(res.data);
          // })
          updateAndLock(item).then(res=>{
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
        let totalNum = this.salesOrderDetailsList.reduce((total, item) => total + Number(item.detailsNum || 0), 0);
        let totalAmount = this.salesOrderDetailsList.reduce((total, item) => total + Number(item.detailsAmount || 0),
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
        this.salesOrderDetailsList.splice(index, 1)
        this.totalOrderAmount();
      },
      /**
       * 复制
       */
      handleCopy(index) {
        let orderDetails = this.salesOrderDetailsList[index];
        // orderDetails.materielName = [];
        this.form = JSON.parse(JSON.stringify(orderDetails));
      },
      /**
       * 新增产品
       */
      async handleAddSalesOrderDetails() {
        let bool = await this.formDetection(this.form);
        if (bool) {
          this.reset();
        }
      },
      /**
       * 保存产品信息
       */
      async salesOrderDetailsSave() {

        let boomList = null;
        if(this.form.boomFile){
          boomList =  await this.getBoomFileData(this.form.boomFile);
        }

        this.$refs["form"].validate(valid => {
          if (valid) {

            if (boomList!=null) {
              // 计算总数量
              for (var i = 0; i < boomList.length; i++) {
                let totalNum = parseInt(this.form.detailsNum) * parseInt(boomList[i].boomNum);
                this.$set(boomList[i], 'totalNum', totalNum)
              }
              this.$set(this.form, 'boomVoList', boomList);
            }

            let item = JSON.parse(JSON.stringify(this.form));
            this.salesOrderDetailsList.push(item);
            // 重置表单
            this.$nextTick(() => {
              this.reset();
            })
            this.totalOrderAmount();
          }

        });






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
          customerPn: null,
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
          this.salesOrderDetailsList = [];
          this.loading = false;
          return
        }
        this.queryParams.orderId = this.orderId;
        listSalesOrderDetailsAll(this.queryParams).then(response => {
          this.salesOrderDetailsList = response.data;
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
          customerPn: null,
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
        this.salesOrderDetailsList.push(item);
      },
      // /** 修改按钮操作 */
      // handleUpdate(row) {
      //   this.reset();
      //   const detailsId = row.detailsId || this.ids
      //   getSalesOrderDetails(detailsId).then(response => {
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
            return delSalesOrderDetails(detailsIds);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(() => {});
        } else {
          this.salesOrderDetailsList.splice(index, 1)
          this.totalOrderAmount();
        }

      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/salesOrderDetails/export', {
          ...this.queryParams
        }, `salesOrderDetails_${new Date().getTime()}.xlsx`)
      },

      setSalesOrderDetailsList(sodl){
        this.salesOrderDetailsList = sodl;
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

  .gtyl>.upload-file>.upload-file-list>.el-upload-list__item>.ele-upload-list__item-content-action {
   display: none !important;
  }
</style>

