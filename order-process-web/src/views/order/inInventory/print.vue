<!-- 领料单 -->
<template>
    <div>
        <el-dialog title="打印" :visible.sync="open" width="60%">
            <div id="inInventoryPrint">
                <el-row style="text-align: center;margin-bottom: 10px;">
                    <div style="font-size: 2em;font-weight: 700;">BRT</div>
                    <div>
                        <span style="font-size: 1.6em;">入库单</span>
                    </div>
                </el-row>
                <el-row style="margin-bottom: 10px;">
                    <el-col :span="18">生成单号：{{printData.orderNo}}</el-col>
                    <el-col :span="4">入库日期：{{parseTime(printData.dataDate,'{y}-{m}-{d}')}}</el-col>
                </el-row>

                <div>
                    <table style="width: 100%;text-align: center;">

                        <tr>
                            <td>序号</td>
                            <td>物料编码</td>
                            <td>物料名称</td>
                            <td>规格型号</td>
                            <td>单位</td>
                            <td>数量</td>
                            <td>供应商</td>
                            <td>批号</td>
                        </tr>

                        <tr v-for="(item, index) in list" :key="index">
                            <td>{{ index+1 }}</td>
                            <td>{{ item.materielNo }}</td>
                            <td>{{ item.materielName }}</td>
                            <td>{{ item.materielSpec }}</td>
                            <td>{{ item.materielUnit }}</td>
                            <td>{{ item.inInventoryNum }}</td>
                            <td>{{ item.val1 }}</td>
                            <td>{{ item.val1 }}</td>
                        </tr>

                    </table>

                    <el-row style="margin-top: 10px;">
                        <el-col :span="10">出库人：</el-col>
                        <el-col :span="8">领料部门：</el-col>
                        <el-col :span="4">领料人：</el-col>
                    </el-row>

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
  inInventory
} from "@/api/order/statistics";

export default {
    name: "inInventoryPrint",
    props: ['orderId'],
    data() {
        return {
            // 打印设置
            printAre: {
                id: "inInventoryPrint",
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {},
            // 是否打开弹框
            open: false,
            // 订单详情列表
            printData: {},
            list: [
            ]
        };
    },
    methods: {

        OnClick(orderId) {
            console.log(orderId);
            this.open = true;
            this.getPrintData(orderId);
        },
        /**
         * 获取订单详情列表
         */
        getPrintData(orderId) {

          inInventory(orderId).then(res => {
                this.printData = res.data;
                this.list = res.data.list;
            })
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
    width: 12%;
}
</style>
