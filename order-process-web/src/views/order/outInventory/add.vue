<template>
    <div class="app-container">
      <div class="mt10 mb10">
        <span class="title">基本信息</span>

        <div class="fr">
          <el-button type="primary" @click="saveInventoryy" size="mini">保存</el-button>
          <el-dropdown class="ml20" split-button type="primary" @command="handleDropdownClick" size="mini">
            操作
            <el-dropdown-menu slot="dropdown">
              <!-- <el-dropdown-item command="save">保存</el-dropdown-item> -->
              <el-dropdown-item command="print">打印</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <el-divider></el-divider>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="出库单号" prop="outInventoryNo">
              <el-input v-model="form.outInventoryNo" placeholder="请输入" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请人" prop="applicat">
              <el-select v-model="form.applicat" filterable class="drag-screenful-contnet">
                <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                  :label="item.nickName"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请时间" prop="applyTime">
              <el-date-picker v-model="form.applyTime"
                              class="drag-screenful-contnet"
                              type="date" placeholder="选择日期"
                              value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="form.remark"> </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="5">
            <el-form-item label="上传类型" prop="uploadType">
              <el-radio-group v-model="form.uploadType" @input="uploadTypeChange" :disabled="form.outInventoryId!=null">
                <el-radio :label="'0'">附件</el-radio>
                <el-radio :label="'1'">选择物料</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="5" v-if="form.uploadType=='0'">
            <el-form-item label="附件" prop="files">
              <file-upload-button v-model="form.files"></file-upload-button>
              <el-button type="text" @click="boomImportTemplate">下载模板</el-button>
            </el-form-item>
          </el-col>
        </el-row>


        <template v-if="form.uploadType=='0'">
          <el-table :data="materielList" style="width: 100%">
            <el-table-column label="序号" type="index" width="50" align="center" />
            <el-table-column prop="materielNo" label="物料编号"> </el-table-column>
            <el-table-column prop="materielName" label="物料名称"> </el-table-column>
            <el-table-column prop="typeId" label="物料类型">
                <template slot-scope="scope">
                    <dict-tag  class="tag-style" :options="dict.type.materiel_type" :value="scope.row.typeId" />
                </template>
            </el-table-column>
            <el-table-column prop="materielSpec" label="型号规格"> </el-table-column>
            <el-table-column prop="outInventoryNum" label="数量"> </el-table-column>
            <el-table-column prop="remark" label="备注"> </el-table-column>
          </el-table>
        </template>

        <template v-if="form.uploadType=='1'">
          <el-table :data="materielList" style="width: 100%">
            <el-table-column label="操作" width="90px">
              <template slot-scope="scope">
                <el-button type="text" v-if="scope.$index <= materielList.length-1 && materielList.length > 1"
                  @click="dltMateriel(scope.row,scope.$index)">删除</el-button>
                <el-button type="text" v-if="scope.$index == materielList.length-1" @click="addMateriel">新增</el-button>
              </template>
            </el-table-column>
            <el-table-column label="序号" type="index" width="50" align="center" />
            <el-table-column label="物料编号">
              <template slot-scope="scope">
                <el-select v-model="scope.row.materielId" filterable class="drag-screenful-contnet"
                  @change="materielNoChange(scope.$index,scope.row.materielId)">
                  <el-option v-for="(item,index) in materielBoxList" :value="item.materielId" :label="item.materielNo"
                    :key="item.materielId"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="物料名称">
              <template slot-scope="scope">
                <el-select v-model="scope.row.materielId" filterable class="drag-screenful-contnet"
                           @change="materielNoChange(scope.$index,scope.row.materielId)">
                  <el-option v-for="(item,index) in materielBoxList" :value="item.materielId" :label="item.materielName"
                             :key="item.materielId"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="物料类型">
                <template slot-scope="scope">
                    <el-select v-model="scope.row.typeId" placeholder="选择物料编号带出">
                        <el-option v-for="dict in dict.type.materiel_type" :key="dict.value" :label="dict.label" :value="dict.value" :disabled="true"> </el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column label="型号规格">
              <template slot-scope="scope">
                <el-input v-model="scope.row.materielSpec" placeholder="选择物料编号自动带出" :disabled="true"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="数量" width="160px">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.outInventoryNum" :min="1" label="请输入数量"
                  class="numBox"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="备注">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark" placeholder="请输入"></el-input>
              </template>
            </el-table-column>
          </el-table>
        </template>

      </el-form>

    </div>
  </template>

  <script>
    import {
      getToken
    } from "@/utils/auth";

    import {
      listInventory,
      listInventoryAll,
      getInventory,
      delInventory,
      addInventory,
      updateInventory
    } from "@/api/order/outInventory";

    import {
      listAllUser
    } from "@/api/system/user";

    import {
      listMaterielAll,
      getMateriel
    } from "@/api/order/materiel";

    import {
      getFileData,
      delMateriel
    } from "@/api/order/outInventoryMateriel";

    import {
    getNo,
    getNoAndAdd
  } from "@/api/order/orderNo";


    export default {
      name: "OnInventoryAdd",
      dicts: ['materiel_type'],
      data() {
        return {
          outInventoryId: null,
          //是否跳过第一次
          disabledFile: false,
          //物料
          materielList: [],
          // 导入参数
          upload: {
            // 是否显示弹出层
            open: false,
            // 弹出层标题
            title: "",
            // 是否禁用上传
            isUploading: false,
            // 设置上传的请求头部
            headers: {
              Authorization: "Bearer " + getToken()
            },
            // 上传的地址
            url: process.env.VUE_APP_BASE_API + "/order/outInventory/importData"
          },
          // 表单参数
          form: {
            uploadType: '0',
            outInventoryId: null,
            outInventoryNo: null, //出库单号
            applicat: null, //申请人
            applyTime: null, //申请时间
            remark: null, //备注
            files: null
          },
          // 表单校验
          rules: {},
          // 业务员列表
          userList: [],
          //物料编号下拉框数据填充
          materielBoxList: []

        };
      },
      watch: {

        "form.files": function(value) {
          this.filesChange(value);
        },

        '$route': {
          handler: function(to,form){
            this.onLoad();
          },
          immediate: true
        }

      },
      created() {


      },
      methods: {

        onLoad(){
          this.getUserList();
          this.getMaterielList();
          this.updateGetData(); //读取出库单的详细信息
        },

        getNo(){
        let queryNo = {
          keyName:"ChuKuDan"
        }
        getNo(queryNo).then(res=>{
          this.form.outInventoryNo = res;
        })
      },

        updateGetData() { //读取出库单的详细信息
          this.reset();
          const outInventoryId = this.$route.query.outInventoryId;
          if (!outInventoryId) { //如果outInventoryId存在说明是修改
            this.getNo();
            this.materielList = [];
            return
          }
          getInventory(outInventoryId).then(res => {

            if (res.data.files != null) {
              this.disabledFile = true;
            }

            this.form = res.data;
            this.$nextTick(()=>{
                if (res.data.outInventoryMaterielVos != undefined && res.data.outInventoryMaterielVos != null) {
                    this.materielList = [];
                    let materielList = res.data.outInventoryMaterielVos;
                    for (let i = 0; i < materielList.length; i++) {
                        this.materielList.push({
                        outInventoryMaterielId: materielList[i].outInventoryMaterielId,
                        materielId: materielList[i].materielId,
                        materielNo: materielList[i].materielNo,
                        materielName: materielList[i].materielName,
                        typeId: materielList[i].typeId,
                        materielSpec: materielList[i].materielSpec,
                        outInventoryNum: materielList[i].outInventoryNum,
                        remark: materielList[i].remark,
                        });
                    }
                }
            })


          })
        },

        /**
         * files值变化监听事件
         * @param value
         */
        async filesChange(value) {
          /**
           * 这里判断此页面是新增还是修改，如果是修改 跳转过来后填充数据时也会触发filesChange事件 从而发生冲突，故：跳过第一次 filesChange事件
           * */
          if (this.disabledFile) {
            this.disabledFile = false;
            return;
          }

          this.materielList = [];
          if (value != null && value != "") {
            let boomFiles = value.split(",");
            for (let i = 0; i < boomFiles.length; i++) {
              let boomList = await this.getBoomFileData(boomFiles[i]);
              for (let j = 0; j < boomList.length; j++) {
                this.materielList.push({
                  outInventoryMaterielId: null,
                  materielId: boomList[i].materielId,
                  materielNo: boomList[i].materielNo,
                  materielName: boomList[i].materielName,
                  typeId: boomList[i].typeId,
                  materielSpec: boomList[i].materielSpec,
                  outInventoryNum: boomList[i].outInventoryNum,
                  remark: boomList[i].remark,
                });
              }
            }
          }
        },

        materielNoChange(index, materielId) {
          let typeId = null;
          getMateriel(materielId).then(res => {
            this.materielList[index].materielNo = res.data.materielNo;
            this.materielList[index].materielName = res.data.materielName;
            this.materielList[index].materielSpec = res.data.materielSpec;
            this.materielList[index].typeId = res.data.typeId;
          });

        },
        /**
         * 物料下拉框数据填充
         */
        getMaterielList() {
          listMaterielAll().then(res => {
            this.materielBoxList = res.data;
          })
        },

        uploadTypeChange() {
          this.form.files = null;
          if (this.form.uploadType == '0') {
            this.materielList = [];
          } else if (this.form.uploadType == '1') {
            this.materielList = [];
            this.addMateriel();
          }
        },
        /**
         * 新增物料
         */
        addMateriel() {
          this.materielList.push({
            outInventoryMaterielId: null,
            materielId: null,
            materielNo: null,
            materielName: null,
            typeId: null,
            materielSpec: null,
            outInventoryNum: 0,
            remark: null
          });
        },
        dltMateriel(row,index) {
          if (this.materielList.length > 1) {
            if(row.outInventoryMaterielId!=null){
              delMateriel(row.outInventoryMaterielId);
            }
            this.materielList.splice(index, 1);
          }
        },

        /**
         * 批量操作按钮点击事件
         */
        handleDropdownClick(command) {
          switch (command) {
            case 'save':
              this.saveInventoryy();
              break;
            case 'print':

              break;
          }
        },
        saveInventoryy() {
          let addData = {
            ...this.form,
            "outInventoryMaterielVos": this.materielList
          }
          if (this.form.outInventoryId != null && this.form.outInventoryId != "") {
            updateInventory(addData).then(res => {
              this.materielList = res.data.outInventoryMaterielVos;
              this.$modal.msgSuccess("保存成功");
            });
          } else {
            addInventory(addData).then(res => {
              this.form.outInventoryId = res.data.outInventoryId;
              this.materielList = res.data.outInventoryMaterielVos;
              this.$modal.msgSuccess("保存成功");
            });
          }
          this.backPrice();

        },

        // 返回上个页面
        backPrice() {
          const obj = {
            path: "/materiel/outInventory/"
          };
          this.$tab.closeOpenPage(obj);
        },

        /**
         * 获取业务员列表
         */
        getUserList() {
          listAllUser().then(res => {
            this.userList = res.data;
          })
        },

        /** 下载模板操作 */
        boomImportTemplate() {
          this.download('order/outInventoryMateriel/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
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

        reset(){
            this.form = {
            uploadType: '0',
            outInventoryId: null,
            outInventoryNo: null, //出库单号
            applicat: null, //申请人
            applyTime: null, //申请时间
            remark: null, //备注
            files: null
          };
        }

      }
    };
  </script>
  <style>
    .title {
      font-size: 20px;
      font-weight: bold;
      color: black;
    }

    .numBox {
      width: 150px;
    }
  </style>
