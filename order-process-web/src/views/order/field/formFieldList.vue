<template>
  <div class="app-container">
    <el-row>
      <el-col :span="colSpan" v-for="(item,index) in fieldList">
        <el-form-item :label="item.fieldName">
          <!-- 下拉框 -->
          <el-select v-if="item.fieldType == '0'" v-model="item.value" filterable class="drag-screenful-contnet">
            <el-option v-for="(dict,dictIndex) in dictDataList" v-if="dict.dictType == item.dictType" :value="dict.dictValue" :label="dict.dictLabel"></el-option>
          </el-select>
          <!-- 多行文本框 -->
          <el-input v-else-if="item.fieldType == '1'" type="textarea" v-model="item.value"/>
          <!-- 单行文本框 -->
          <el-input v-else-if="item.fieldType == '2'" v-model="item.value"/>
          <!-- 单选框 -->
          <el-radio-group v-else-if="item.fieldType == '3'" v-model="item.value">
             <el-radio v-for="(dict,dictIndex) in dictDataList" v-if="dict.dictType == item.dictType" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
          </el-radio-group>
          <!-- 复选框 -->
          <el-checkbox-group v-else-if="item.fieldType == '4'" v-model="item.value">
            <el-checkbox v-for="(dict,dictIndex) in dictDataList" v-if="dict.dictType == item.dictType" :label="dict.dictValue" :key="dict.dictValue">{{dict.dictLabel}}</el-checkbox>
          </el-checkbox-group>
          <!-- 单行文本框 -->
          <el-input v-else-if="item.fieldType == '5'" type="number" v-model="item.value" />
          <!-- 日期 -->
          <el-date-picker v-else-if="item.fieldType == '6'" v-model="item.value" class="drag-screenful-contnet" type="date" value-format="yyyy-MM-dd">
          </el-date-picker>
          <!-- 富文本 -->
          <editor v-else-if="item.fieldType == '7'" v-model="item.value" :min-height="192"/>
          <!-- 文件上传 -->
          <file-upload v-else-if="item.fieldType == '8'" v-model="item.value" />
        </el-form-item>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {
    listFieldAll
  } from "@/api/order/field";

  import {
    getDictList
  } from "@/api/system/dict/data";

  let cityOptions = ['上海', '北京', '广州', '深圳'];

  export default {
    name: "formFieldList",
    props: ['colSpan', 'businessType', 'formFieldList', 'id'],
    data() {
      return {
        dxval: [],
        dx:cityOptions,
        // 字段列表
        fieldList: [],
        // 字典数据列表
        dictDataList: []
      };
    },
    watch:{
      id:function(){
        this.getFieldList();
      }
    },
    created() {
    },
    mounted(){
      this.getFieldList();

    },
    methods: {

        getFieldList() {
          let queryParams = {
            fieldStatus: true,
            businessType: this.businessType
          };
          listFieldAll(queryParams).then(res => {
          let fieldList = res.data;
          
          let dictTypes = fieldList.filter(item => item.dictType != null).map(item => item.dictType);
            if(dictTypes != null && dictTypes.length>0){
             this.getDictData(dictTypes);
            }
            if(this.id && this.formFieldList){
              let formFieldList = JSON.parse(this.formFieldList);
              for(let i = 0 ; i < fieldList.length ; i++){
                for(let j = 0 ; j< formFieldList.length ; j++){
                  
                  if(formFieldList[j].fieldName == fieldList[i].fieldName){
                    
                    if(fieldList[i].fieldType == '4' && !formFieldList[j].value){
                      this.$set(fieldList[i],"value",[]);
                      break;
                    }else{
                      fieldList[i].value = formFieldList[j].value;
                      break;
                    }
                  }
                }
              }
            }
            this.fieldList = fieldList;
            console.log("----------------");
            console.log(fieldList);
          })

       },

      /**
       * 获取字典列表
       */
      getDictData(dictTypes){
        let query = {
          dictTypes:dictTypes.join(',')
        };
        getDictList(query).then(res => {
          this.dictDataList = res.data;
        })
      }
    }
  };
</script>
