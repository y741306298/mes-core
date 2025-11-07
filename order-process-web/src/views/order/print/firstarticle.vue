<!-- 首件确认表 -->
<template>
    <div>
        <el-dialog title="打印" :visible.sync="open" width="60%">
            <div id="firstarticlePrint">
                <el-row style="text-align: center;margin-bottom: 10px;">
                    <div style="font-size: 2em;font-weight: 700;">BRT</div>
                    <div>
                        <span style="font-size: 1.6em;">首件确认表</span>
                        <span style="position: absolute;right: 20%;">编号: </span>
                    </div>
                </el-row>

                <div>
                    <table style="width: 100%;text-align: center;">

                        <tr>
                            <td>产品名称</td>
                            <td>{{printData.materielName}}</td>
                            <td>AP单号</td>
                            <td>{{printData.orderNo}}</td>
                            <td>生产日期</td>
                            <td></td>
                        </tr>

                        <tr>
                            <td>机器编号</td>
                            <td></td>
                            <td>产品料号</td>
                            <td></td>
                            <td>作业员</td>
                            <td></td>
                        </tr>

                        <tr>
                            <td rowspan="2">类型</td>
                            <td><input type="checkbox">每班首件</td>
                            <td><input type="checkbox">物料变更</td>
                            <td><input type="checkbox">新机型试产</td>
                            <td colspan="2"><input type="checkbox">设计变更</td>
                        </tr>


                        <tr>
                            <td><input type="checkbox">程序变更</td>
                            <td><input type="checkbox">设备参数变更</td>
                            <td><input type="checkbox">工艺更改</td>
                            <td colspan="2"><input type="checkbox">二次首件</td>
                        </tr>



                        <tr>
                            <td>项目</td>
                            <td colspan="4">检查项目</td>
                            <td>结论</td>
                        </tr>

                        <!-- 前制准备 -->
                        <tr>
                            <td rowspan="3">前制准备</td>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">验证图纸 - 正确的版本，正确的零件号
                                P/N,验证材料单
                                BOM</td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">验证IQA,机加工车间，过程中质量检验,QC 签字
                            </td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">按照文件夹要求记录信息</td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <!-- 装配检查 -->
                        <tr>
                            <td rowspan="5">装配检查</td>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">
                                肉眼可见裂缝、崩角、毛刺均不可以接受,承座干净，无污染物。
                            </td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">验证刻字: 零件号, 序号,
                                A1,LOGO,表面一致性
                            </td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">与底座齐平，螺丝拧紧并平或低于表面。</td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">检查装配查检表是否记录每个插座弹簧探针批号</td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">检查安装方向X/Y正反是否正确</td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <!-- 尺寸、公差测量 -->
                        <tr>
                            <td>尺寸、公差测量</td>
                            <td colspan="4" style="text-align-last: left;text-indent: 20px;">根据图纸数据判定是否合格</td>
                            <td>
                                <input type="checkbox">是
                                <input type="checkbox" style="margin-left: 10px;">否
                            </td>
                        </tr>

                        <!-- 首件检验结果 -->
                        <tr>
                            <td rowspan="2">首件检验结果</td>
                            <td>判定结果</td>
                            <td>是否同意继续生产</td>
                            <td>检验员签字</td>
                            <td>品质部主管复核</td>
                            <td>备注</td>
                        </tr>

                        <tr>
                            <td>
                                <input type="checkbox">OK
                                <input type="checkbox" style="margin-left: 10px;">NG
                            </td>
                            <td>
                                <input type="checkbox">生产
                                <input type="checkbox" style="margin-left: 10px;">停产
                            </td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>


                        <!-- 异常描述 -->
                        <tr style="height: 150px;">
                            <td>异常描述</td>
                            <td colspan="5">
                                <el-row>
                                    <el-col :span="10">&nbsp;</el-col>
                                    <el-col :span="10" style="padding-top: 30px;">检验员：</el-col>
                                </el-row>
                            </td>
                        </tr>

                        <!-- 原因分析 -->
                        <tr style="height: 150px;">
                            <td>原因分析</td>
                            <td colspan="5">
                                <el-row>
                                    <el-col :span="10">&nbsp;</el-col>
                                    <el-col :span="10" style="padding-top: 30px;">品质部主管：</el-col>
                                </el-row>
                            </td>
                        </tr>


                        <!-- 矫正措施 -->
                        <tr style="height: 150px;">
                            <td>矫正措施</td>
                            <td colspan="5">
                                <el-row>
                                    <el-col :span="8">&nbsp;</el-col>
                                    <el-col :span="6" style="padding-top: 30px;">生产部主管：</el-col>
                                    <el-col :span="4" style="padding-top: 30px;">品质部主管：</el-col>
                                </el-row>
                            </td>
                        </tr>

                        <!-- 备注 -->
                        <tr style="text-align: left;height: 150px;text-indent: 1em;">
                            <td colspan="6">
                                备注：
                            </td>
                        </tr>



                    </table>

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
  firstarticle
} from "@/api/order/statistics";

export default {
    name: "CustomerDeliveryPrint",
    props: ['orderId'],
    data() {
        return {
            // 打印设置
            printAre: {
                id: "firstarticlePrint",
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {},
            // 是否打开弹框
            open: false,
            // 订单详情列表
            printData: {},
            list: []
        };
    },
    methods: {

        OnClick(e) {
            console.log(e);
            this.open = true;
            this.getPrintData(e);
        },
        /**
         * 获取订单详情列表
         */
        getPrintData(orderId) {
            firstarticle(orderId).then(res => {
                this.printData = res.data;
            })
        },

        /**
         * 打开弹框
         */
        handleOpen(orderId) {
            this.open = true;
            this.getPrintData(orderId);
        },

        // 表单重置
        reset() {
            this.form = {
                orderId: null,
                orderNo: new Date().getTime(),
                customerId: null,
                contact: null,
                contactTel: null,
                customerAddressId: null,
                orderTime: null,
                deliveryTime: null,
                templateId: null,
                craftType: null,
                totalNum: 0,
                totalAmount: 0,
                currencyType: null,
                attachments: null,
                userId: null,
                createTime: null,
                createBy: null,
                updateTime: null,
                updateBy: null
            };
            this.resetForm("form");
        },
    }
};
</script>

<style scoped>
table,
th,
td {
    border: 2px solid black;
    border-collapse: collapse;
    /* 使得相邻的边框合并为一个单一的边框 */
}

td {
    padding: 5px 0;
    width: 10%;
}
</style>
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


<style scoped>
table,
th,
td {
    border: 2px solid black;
    border-collapse: collapse;
    /* 使得相邻的边框合并为一个单一的边框 */
}

td {
    padding: 10px 0;
    width: 15%;
}
</style>
