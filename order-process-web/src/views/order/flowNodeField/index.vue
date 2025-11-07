<template>
  <div>

    <el-table border ref="brtTable" v-loading="loading" :data="flowNodeFieldList">
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-plus" v-if="scope.$index == flowNodeFieldList.length-1" @click="hanldeAdd(scope.row)"></el-button>
          <el-button type="text" icon="el-icon-delete" v-else @click="handleDelete(scope.row,scope.$index)"></el-button>
        </template>
      </el-table-column>
      <el-table-column label="字段类型" align="center">
        <template slot-scope="scope">
          <el-select v-model="scope.row.fieldType" placeholder="请选择">
            <el-option v-for="dict in dict.type.filed_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="字段名称" align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.fieldName" placeholder="请输入" />
        </template>
      </el-table-column>
      <el-table-column label="业务类型" align="center">
        <template slot-scope="scope">
          <el-select v-model="scope.row.businessType" :disabled="scope.row.fieldType != '9'" clearable filterable placeholder="请选择">
            <el-option v-for="dict in dict.type.business_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="字典值" align="center">
        <template slot-scope="scope">
          <el-select v-model="scope.row.dictType" clearable filterable placeholder="请选择">
            <el-option v-for="dict in dictOptions" :key="dict.dictType" :label="dict.dictName" :value="dict.dictType">
              <span style="float: left">{{ dict.dictName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.dictType }}</span>
            </el-option>
          </el-select>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {
    listFlowNodeField,
    listFlowNodeFieldAll,
    getFlowNodeField,
    delFlowNodeField,
    addFlowNodeField,
    updateFlowNodeField
  } from "@/api/order/flowNodeField";

  import {
    optionselect as getDictOptionselect
  } from "@/api/system/dict/type";

  export default {
    name: "FlowNodeField",
    dicts: ['node_type', 'filed_type','business_type'],
    props: ['nodeId'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 节点字段表格数据
        flowNodeFieldList: [],
        // 查询条件
        queryParams:{

        },
        dictOptions: [] //字典列表
      };
    },
    watch:{
      nodeId:function(){
        this.getList();
      }
    },
    created() {
      this.getDictList();
    },
    methods: {
      /** 查询字典下拉列表 */
      getDictList() {
        getDictOptionselect().then(response => {
          this.dictOptions = response.data;
        });
      },
      /** 查询节点字段列表 */
      getList() {
        this.loading = true;
        if(!this.nodeId){
          this.flowNodeFieldList = [];
          this.loading = false;
          this.hanldeAdd();
          return;
        }
        this.queryParams.nodeId = this.nodeId;
        listFlowNodeFieldAll(this.queryParams).then(response => {
          this.flowNodeFieldList = response.data;
          if(this.flowNodeFieldList == null || this.flowNodeFieldList.length<=0){
            this.hanldeAdd();
          }
          this.loading = false;
        });
      },
      // 新增一行
      hanldeAdd() {
        const item = {
          fieldId: null,
          templateId: null,
          nodeId: this.nodeId,
          nodeType: null,
          fieldName: null,
          fieldType: null,
          dictType: null,
          businessType: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.flowNodeFieldList.push(item);
      },
      /** 删除按钮操作 */
      handleDelete(row,index) {
        const fieldId = row.fieldId;
        if(fieldId){
          this.$modal.confirm('确认删除？').then(function() {
            return delFlowNodeField(fieldIds);
          }).then(() => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success'
            });
          }).catch(() => {});
        }
        this.flowNodeFieldList.splice(index,1)

      },
    }
  };
</script>
