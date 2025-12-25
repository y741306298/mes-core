package com.brt.dto;

import java.util.List;

public class GetAvailableDeliveryDatesInput {
    private String manufacturer_code;
    private Integer delivery_prefer;
    private List<InputOrderItem> orderItemList;
    private Integer addr_id;

    public String getManufacturer_code() {
        return manufacturer_code;
    }

    public void setManufacturer_code(String manufacturer_code) {
        this.manufacturer_code = manufacturer_code;
    }

    public Integer getDelivery_prefer() {
        return delivery_prefer;
    }

    public void setDelivery_prefer(Integer delivery_prefer) {
        this.delivery_prefer = delivery_prefer;
    }

    public List<InputOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<InputOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Integer getAddr_id() {
        return addr_id;
    }

    public void setAddr_id(Integer addr_id) {
        this.addr_id = addr_id;
    }
}
