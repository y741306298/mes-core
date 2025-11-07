<template>
  <div>
    <el-drawer title="节点设置" size="50%" :visible.sync="open" direction="rtl">
      <el-tabs v-model="activeName">
        <el-tab-pane :label="form.nodeName+'设置'" name="first">
          <!-- <div class="node-title">记录添加设置</div>
          <el-checkbox v-model="form.limitAdd">限制记录添加</el-checkbox>
          <div class="node-desc">提示:开启后,数量记录/金额记录/开票记录的总计数值<span style="color: red;">不能超过</span>相应计划数值</div>
          <el-divider></el-divider> -->

          <div class="node-title">默认截止日期</div>
          <el-row class="node-row">
            <el-radio-group v-model="form.deadlineType">
              <el-radio :label="0" class="node-desc">无</el-radio>
            </el-radio-group>
          </el-row>

          <el-row class="node-row">
            <el-col :span="6">
              <el-radio-group v-model="form.deadlineType">
                <el-radio :label="'1'" class="node-desc">以开单日期来推算</el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="3">
              <div class="node-desc text-keep-right">距离开单日后&nbsp;&nbsp;</div>
            </el-col>
            <el-col :span="3">
              <el-input size="mini" placeholder="请输入" v-model="form.day"></el-input>
            </el-col>
            <el-col :span="1">
              <div class="node-desc">天</div>
            </el-col>
            <el-col :span="3">
              <div class="node-desc text-keep-right">当天时间&nbsp;&nbsp;</div>
            </el-col>
            <el-col :span="3">
              <el-select v-model="form.hour" placeholder="小时" size="mini">
                <el-option v-for="item in 23" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>
            <el-col :span="3">
              <el-select v-model="form.minute" placeholder="分钟" size="mini">
                <el-option v-for="item in 59" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-col>
          </el-row>

          <el-row class="node-row">
            <el-col :span="6">
              <el-radio-group v-model="form.deadlineType" class="node-desc">
                <el-radio :label="'2'">以交货日期来推算</el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="3">
              <div class="node-desc text-keep-right">在交货日前&nbsp;&nbsp;</div>
            </el-col>
            <el-col :span="3">
              <el-input size="mini" placeholder="请输入" v-model="form.day"></el-input>
            </el-col>
            <el-col :span="1">
              <div class="node-desc">天</div>
            </el-col>
            <el-col :span="3">
              <div class="node-desc text-keep-right">当天时间&nbsp;&nbsp;</div>
            </el-col>
            <el-col :span="3">
              <el-select v-model="form.hour" placeholder="小时" size="mini">
                <el-option v-for="item in 23" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>
            <el-col :span="3">
              <el-select v-model="form.minute" placeholder="分钟" size="mini">
                <el-option v-for="item in 59" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-col>
          </el-row>




          <el-divider></el-divider>

          <div class="node-title">自动完成设置</div>
          <el-checkbox v-model="form.autoCompletion">开启自动完成</el-checkbox>
          <div class="node-desc">提示:开启自动完成后,到了截止日期当天任务就会自动变成已完成状态;如果任务没有截止日期,则不会自动改变状态</div>
          <el-divider></el-divider>

          <div class="node-title">其他设置</div>
          <el-checkbox-group v-model="form.otherSetting">
            <el-checkbox label="0">生成收货单</el-checkbox>
            <el-checkbox label="1">生成送货单</el-checkbox>
            <el-checkbox label="2">减库存</el-checkbox>
            <el-checkbox label="3">加库存</el-checkbox>
          </el-checkbox-group>
          <div class="node-desc">提示:开启生成发货单后，当前节点状态变为进行中时在发货提醒表生成发货提醒单；</div>
          <div class="node-desc">开启删除送货单后，当前节点状态变为进行中时在送货提醒表生成送货提醒单；</div>
          <div class="node-desc">开启减库存后，当前节点状态变为进行中时，产品库中扣减对应的产品数量；</div>
          <el-divider></el-divider>
        </el-tab-pane>

      </el-tabs>

      <div class="demo-drawer__footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
  import {
    listFlowNode,
    listFlowNodeAll,
    getFlowNode,
    delFlowNode,
    addFlowNode,
    updateFlowNode
  } from "@/api/order/flowNode";

  import flowNodeField from "@/views/order/flowNodeField/index"

  export default {
    name: "FlowNodeSetting",
    components: {
      flowNodeField
    },
    data() {
      return {
        otherSetting: [],
        // 弹框
        open: false,
        activeName: "first",
        // 当前节点信息
        nodeId: null,
        // 节点详情
        form: {}
      };
    },
    methods: {
      /**
       * 提交表单
       */
      submitForm() {
        // let otherSetting = [];
        // if (this.otherSetting.includes("生成发货单")) {
        //   otherSetting.push("1");
        // }
        // if (this.otherSetting.includes("生成送货单")) {
        //   otherSetting.push("2");
        // }
        // if (this.otherSetting.includes("减库存")) {
        //   otherSetting.push("3");
        // }
        // if (this.otherSetting.includes("加库存")) {
        //   otherSetting.push("4");
        // }
        // this.$set(this.form, 'otherSetting', otherSetting)

        if(this.form.otherSetting!=null && this.form.otherSetting.length>0){
          this.$set(this.form, 'otherSetting', this.form.otherSetting.join(','))
        }else{
          this.$set(this.form, 'otherSetting', null)
        }

        updateFlowNode(this.form).then(res => {
          this.$notify({
            title: '成功',
            message: '操作成功',
            type: 'success'
          });
          this.open = false;
        })
      },
      // 表单重置
      reset() {
        this.form = {
          nodeId: null,
          nodeName: null,
          nodeType: null,
          nodeStatus: null,
          sort: null,
          limitAdd: null,
          deadlineType: null,
          autoCompletion: null,
          otherSetting:[],
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
        };
        this.resetForm("form");
      },
      /**
       * 获取节点详情
       */
      getNodeInfo() {
        this.reset();
        getFlowNode(this.nodeId).then(res => {
          this.form = res.data;

          this.$set(this.form, 'otherSetting',this.form.otherSetting ? this.form.otherSetting.split(',') : [])
        })
      },
      //打开弹框
      handleOpen(nodeId) {
        this.open = true;
        this.nodeId = nodeId;
        this.getNodeInfo();
      }
    },
  };
</script>
<style>
  .node-title {
    margin-bottom: 20px;
    color: #747474;
  }

  .node-text {
    font-size: 14px;
    /* margin-left: 90px; */
    color: #747474;
  }

  .node-row {
    margin-top: 5px;
  }

  .text-keep-right {
    text-align: right;
  }

  .node-desc {
    font-size: 0.8em;
    color: #747474;
    margin-top: 10px;
  }

  .el-drawer__body {
    margin: 20px !important;
  }
</style>
