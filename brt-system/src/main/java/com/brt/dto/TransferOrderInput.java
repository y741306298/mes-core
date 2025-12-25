package com.brt.dto;

import java.io.Serializable;
import java.util.List;

public class TransferOrderInput implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 转单数据列表
     */
    private List<TransferData> transferDataList;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public List<TransferData> getTransferDataList() {
        return transferDataList;
    }

    public void setTransferDataList(List<TransferData> transferDataList) {
        this.transferDataList = transferDataList;
    }

    public static class TransferData {
        /**
         * 接收方输出中心编码
         */
        private String manufacturerCode;
        /**
         * 订单项列表
         */
        private List<ItemData> itemDataList;

        public String getManufacturerCode() {
            return manufacturerCode;
        }

        public void setManufacturerCode(String manufacturerCode) {
            this.manufacturerCode = manufacturerCode;
        }

        public List<ItemData> getItemDataList() {
            return itemDataList;
        }

        public void setItemDataList(List<ItemData> itemDataList) {
            this.itemDataList = itemDataList;
        }
    }

    public static class ItemData {
        /**
         * 订单项编码
         */
        private String orderItemSn;
        /**
         * 数量
         */
        private Integer quantity;

        public String getOrderItemSn() {
            return orderItemSn;
        }

        public void setOrderItemSn(String orderItemSn) {
            this.orderItemSn = orderItemSn;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
