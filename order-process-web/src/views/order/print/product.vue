<!-- 产品工序流转卡 -->

<template>
    <div>
        <el-dialog title="打印" :visible.sync="open" width="1300px">

          <div id="productPrint">
            <div style="display: flex">
              <table style="width: 50%;text-align: center;">
                <tr>
                  <td colspan="8">
                    <div style="font-size: 2em;font-weight: 700;">BRT</div>
                  </td>
                </tr>

                <tr>
                  <td colspan="8">
                    <span style="font-size: 1.6em;">产品工序流转卡(生产)</span>
                  </td>
                </tr>
                <tr>
                  <td colspan="8">
                    <div style="text-align: left;padding-left: 10px">编号: SMT-QR-063</div>
                  </td>
                </tr>

                <tr>
                  <td colspan="2" style="height: 80px;">产品名称</td>
                  <td colspan="2">{{printData.materielName}}</td>
                  <td colspan="2">AP单号</td>
                  <td colspan="2">{{printData.orderNo}}</td>
                </tr>

                <tr>
                  <td colspan="2" style="height: 100px;">产品料号</td>
                  <td colspan="2">{{printData.customerPn}}</td>
                  <td colspan="2">客户料号</td>
                  <td colspan="2"></td>
                </tr>

                <tr>
                  <td colspan="2" rowspan="2" style="height: 80px;">生产数量</td>
                  <td rowspan="2">{{printData.num}}</td>
                  <td rowspan="2">计划完成日期</td>
                  <td rowspan="2" colspan="2"></td>
                  <td>客户代码</td>
                  <td>
<!--                    <span v-if="printData&&printData.orderNo">{{printData.orderNo.replace('AP','KH')}}</span>-->
                  </td>
                </tr>

                <tr>
                  <td>备注</td>
                  <td></td>
                </tr>


                <tr>
                  <td>序号</td>
                  <td>工序</td>
                  <td>工艺内容及技术要求</td>
                  <td colspan="3">文件下发确认(签字)/日期</td>
                  <td colspan="2">检验员/日期</td>
                </tr>

                <tr>
                  <td>1</td>
                  <td>设计</td>
                  <td>按照图纸尺寸尺寸及要求</td>
                  <td colspan="3"></td>
                  <td colspan="2"></td>
                </tr>

                <tr>
                  <td>2</td>
                  <td>编程</td>
                  <td>按照图纸尺寸尺寸及要求</td>
                  <td colspan="3"></td>
                  <td colspan="2"></td>
                </tr>

                <tr>
                  <td rowspan="3">3</td>
                  <td rowspan="3">CNC加工</td>
                  <td rowspan="3">按照图纸尺寸尺寸和CNC作业指导书要求加工</td>
                  <td>加工数</td>
                  <td>合格数</td>
                  <td>不合格/返工/返修/报废</td>
                  <td>机台编号</td>
                  <td>操作者/日期</td>
                </tr>

                <tr>
                  <td rowspan="3" style="height: 80px;"></td>
                  <td rowspan="3"></td>
                  <td rowspan="3"></td>
                  <td rowspan="3"></td>
                  <td rowspan="2"></td>
                </tr>

                <tr></tr>
                <tr></tr>

                <tr>
                  <td>4</td>
                  <td>首件检验确认</td>
                  <td>按照首件确认表确认</td>
                  <td colspan="5"></td>
                </tr>

                <tr>
                  <td rowspan="2">5</td>
                  <td rowspan="2">半成品外观检查</td>
                  <td>互检,自检</td>
                  <td>尺寸</td>
                  <td>外观</td>
                  <td>装配可行性</td>
                  <td>内腔</td>
                  <td>检验员/日期</td>
                </tr>

                <tr>
                  <td style="height: 80px;"></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>


                <tr>
                  <td colspan="2">计划员:</td>
                  <td colspan="2"></td>
                  <td colspan="2">下单日期:</td>
                  <td colspan="2"></td>
                </tr>
              </table>


              <table style="width: 50%;text-align: center;">
                <tr>
                  <td colspan="8">
                    <div style="font-size: 2em;font-weight: 700;">BRT</div>
                  </td>
                </tr>

                <tr>
                  <td colspan="8">
                    <span style="font-size: 1.6em;">产品工序流转卡(装配)</span>
                  </td>
                </tr>
                <tr>
                  <td colspan="8">
                    <div style="text-align: left;padding-left: 10px">编号: SMT-QR-063</div>
                  </td>
                </tr>

                <tr>
                  <td colspan="2" style="height: 80px;">产品名称</td>
                  <td colspan="2">{{printData.materielName}}</td>
                  <td colspan="2">AP单号</td>
                  <td colspan="2">{{printData.orderNo}}</td>
                </tr>

                <tr>
                  <td colspan="2" style="height: 100px;">产品料号</td>
                  <td colspan="2">{{printData.customerPn}}</td>
                  <td colspan="2">客户料号</td>
                  <td colspan="2"></td>
                </tr>

                <tr>
                  <td colspan="2" rowspan="2" style="height: 80px;">生产数量</td>
                  <td rowspan="2">{{printData.num}}</td>
                  <td rowspan="2">计划完成日期</td>
                  <td rowspan="2" colspan="2"></td>
                  <td>客户代码</td>
                  <td>
<!--                    <span v-if="printData&&printData.orderNo">{{printData.orderNo.replace('AP','KH')}}</span>-->
                  </td>
                </tr>

                <tr>
                  <td>备注</td>
                  <td></td>
                </tr>


                <tr>
                  <td>序号</td>
                  <td>工序</td>
                  <td>工艺内容及技术要求</td>
                  <td colspan="3">文件下发确认(签字)/日期</td>
                  <td colspan="2">检验员/日期</td>
                </tr>

                <tr>
                  <td>1</td>
                  <td>设计</td>
                  <td>按照图纸尺寸尺寸及要求</td>
                  <td colspan="3"></td>
                  <td colspan="2"></td>
                </tr>

                <tr>
                  <td rowspan="4">2</td>
                  <td rowspan="4">组装各零部件及电子配件</td>
                  <td rowspan="4">按照图纸及作业指导书要求</td>
                  <td>加工数</td>
                  <td>合格数</td>
                  <td>不合格/返工/返修/报废</td>
                  <td>机台编号</td>
                  <td>操作者/日期</td>
                </tr>

                <tr>
                  <td rowspan="2" style="height: 80px;"></td>
                  <td rowspan="2"></td>
                  <td rowspan="2"></td>
                  <td rowspan="2"></td>
                  <td rowspan="2"></td>
                </tr>

                <tr style="height: 130px;"></tr>
                <tr></tr>

                <tr>
                  <td rowspan="4">3</td>
                  <td rowspan="4">成品外观检查</td>
                  <td>装配完整性</td>
                  <td>尺寸</td>
                  <td>外观</td>
                  <td>装配可行性</td>
                  <td colspan="2">检验员/日期</td>
                </tr>

                <tr>
                  <td rowspan="3" style="height: 80px;"></td>
                  <td rowspan="3"></td>
                  <td rowspan="3"></td>
                  <td rowspan="3"></td>
                  <td colspan="2" rowspan="3"></td>
                </tr>

                <tr style="height: 70px;"></tr>
                <tr style="height: 80px;"></tr>

                <tr>
                  <td colspan="2">计划员:</td>
                  <td colspan="2"></td>
                  <td colspan="2">下单日期:</td>
                  <td colspan="2"></td>
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
  product
} from "@/api/order/statistics";

export default {
    name: "CustomerDeliveryPrint",
    props: ['orderId'],
    data() {
        return {
            // 打印设置
            printAre: {
                id: "productPrint",
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
            this.getPrintData(e)
        },
        /**
         * 获取订单详情列表
         */
        getPrintData(orderId) {
            product(orderId).then(res => {
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
