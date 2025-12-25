package com.brt.dto;

import java.util.List;

public class InputOrderItem {
    private String orderItem_sn;
    private String prod_code;
    private Boolean bigOrder;
    private List<ProcessList> processList;

    public String getOrderItem_sn() {
        return orderItem_sn;
    }

    public void setOrderItem_sn(String orderItem_sn) {
        this.orderItem_sn = orderItem_sn;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public Boolean getBigOrder() {
        return bigOrder;
    }

    public void setBigOrder(Boolean bigOrder) {
        this.bigOrder = bigOrder;
    }

    public List<ProcessList> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessList> processList) {
        this.processList = processList;
    }
}
