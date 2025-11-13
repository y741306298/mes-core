<template>
  <div class="app-container">
    <div class="formHeader">
      <div class="formTitle">基本信息</div>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
    <el-divider></el-divider>

    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model="form.templateName" placeholder="请输入模板名称" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="负责人" prop="userId">
            <el-select v-model="form.userId" filterable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="审核人" prop="auditUserId">
            <el-select v-model="form.auditUserId" @change="auditUserChange" filterable clearable class="drag-screenful-contnet">
              <el-option v-for="(item,index) in userList" :value="item.userId.toString()"
                :label="item.userName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <flow-node ref="flowNodeRef" :templateId="this.form.templateId"></flow-node>


    <div class="formHeader mt20">
      <div class="formTitle">小贴士</div>
    </div>
    <el-divider></el-divider>
    <!-- 小贴士列表 -->
    <div class="post-list">
      <div class="post-item" v-for="(item,index) in postList">
        <div class="post-title">{{index+1}}.{{item.title}}</div>
        <div class="post-desc">{{item.desc}}</div>
        <div class="post-img">
          <img :src="item.img" style="width: 350px; height: 200px;"/>
        </div>

      </div>
    </div>

  </div>
</template>

<script>
  import {
    listFlowTemplate,
    listFlowTemplateAll,
    getFlowTemplate,
    delFlowTemplate,
    addFlowTemplate,
    updateFlowTemplate
  } from "@/api/order/flowTemplate";

  import {
    listAllUser
  } from "@/api/system/user";

  import flowNode from "@/views/order/flowNode/index"

  export default {
    name: "FlowTemplateInfo",
    dicts: ['yes_no'],
    components: {
      flowNode
    },
    data() {
      return {
        imgPath: "../../../assets/images/numTask.png",
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        // 用户列表
        userList: [],
        // 小贴士列表
        postList: [
          // {
          //   title: "自定义记录任务",
          //   desc: "在流程进度上通过记录自定义字段完成情况体现其进度，适用于自定义等对产品分配处理的环节。",
          //   img: ""
          // },
          {
            title: "数量记录任务",
            desc: "在流程进度上通过记录特定产品完成数体现其进度，适用于图纸、编程、生产、检验等对产品分配处理的环节。",
            img: require("../../../assets/images/numTask.png")
          },
          {
            title: "子流程记录任务",
            desc: "在流程进度条上通过子流程体现其进度，适用于对产品分配处理的环节。",
            img: require("../../../assets/images/sunFlowTask.png")
          },
          {
            title: "金额记录任务",
            desc: "在流程进度条上通过记录金额体现其进度，适用于收款、入账、开票等对金额分配处理的环节。",
            img: require("../../../assets/images/amountTask.png")
          }
        ],
      };
    },
    created() {

    },
    watch: {
      '$route': {
        handler: function(to,form){
          this.onLoad();
        },
        immediate: true
      }
    },

    methods: {

      onLoad(){
        this.getUserList();
        const templateId = this.$route.query.templateId;
        this.handleUpdate(templateId);
      },
      /**
       * 审核用户选择事件
       */
      auditUserChange(e){
        if(e){
          this.form.isAudit = 'Y'
          this.$refs.flowNodeRef.addAuditNode();
        }else{
          this.form.isAudit = 'N'
          this.$refs.flowNodeRef.delAuditNode();
        }
      },
      /**
       * 获取用户列表
       */
      getUserList() {
        listAllUser().then(res => {
          this.userList = res.data
        })
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          templateId: null,
          templateName: null,
          userId: null,
          isSeqExecute: null,
          isAutoPostpone: null,
          templateStatus: null,
          isAudit: null,
          auditUserId: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
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
        this.ids = selection.map(item => item.templateId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加流程模板";
      },
      /** 修改按钮操作 */
      handleUpdate(templateId) {
        this.reset();
        if(!templateId){
          return;
        }
        getFlowTemplate(templateId).then(response => {
          this.form = response.data;
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let flowNodeList = this.$refs.flowNodeRef.flowNodeList;
            let b = true;
            flowNodeList.forEach(item => {
              if(!item.nodeName){
                  b=false;
              }
            });
            if(b){
              this.$set(this.form,"flowNodeList",flowNodeList)
              if (this.form.templateId != null) {
                updateFlowTemplate(this.form).then(response => {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.backPrice();
                  this.getList();
                  
                });
              } else {
                addFlowTemplate(this.form).then(response => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.backPrice();
                  this.getList();
                });
              }
            }else{
              this.$modal.msgError("进度名称和任务名称不可为空");
            }
            
          }
        });

      },

      // 返回上个页面
      backPrice() {
        const obj = {
          path: "/flowTemplate"
        };
        this.$tab.closeOpenPage(obj);
      },

      /** 删除按钮操作 */
      handleDelete(row) {
        const templateIds = row.templateId || this.ids;
        this.$modal.confirm('确认删除？').then(function() {
          return delFlowTemplate(templateIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('order/flowTemplate/export', {
          ...this.queryParams
        }, `flowTemplate_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
<style lang="scss">
  .post-list {
    display: flex;


    .post-item {
      // padding: 10px;
      flex: 1;

      .post-title {
        padding-bottom: 5px;
      }

      .post-desc {
        color: #aaa1b0;
        font-size: 0.8em;
        padding-bottom: 5px;
      }

      .post-img{
        width: 100%;
      }
    }
  }
</style>
