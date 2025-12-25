package com.brt.dto;

import java.math.BigDecimal;

public class Proc {
    private String proc_code;
    private String proc_name;
//    private String proc_attachmentType;
//    private Integer proc_attachment;
    private String proc_attachmentTypeList;
    private Integer measure_unit;
    private String measure_unitStr;
    private BigDecimal proc_price;
    private Boolean is_valid;
    private String comments;

    public String getProc_attachmentTypeList() {
        return proc_attachmentTypeList;
    }

    public void setProc_attachmentTypeList(String proc_attachmentTypeList) {
        this.proc_attachmentTypeList = proc_attachmentTypeList;
    }

    public Integer getMeasure_unit() {
        return measure_unit;
    }

    public void setMeasure_unit(Integer measure_unit) {
        this.measure_unit = measure_unit;
    }

    public String getMeasure_unitStr() {
        return measure_unitStr;
    }

    public void setMeasure_unitStr(String measure_unitStr) {
        this.measure_unitStr = measure_unitStr;
    }

    public String getProc_code() {
        return proc_code;
    }

    public void setProc_code(String proc_code) {
        this.proc_code = proc_code;
    }

    public String getProc_name() {
        return proc_name;
    }

    public void setProc_name(String proc_name) {
        this.proc_name = proc_name;
    }

    public BigDecimal getProc_price() {
        return proc_price;
    }

    public void setProc_price(BigDecimal proc_price) {
        this.proc_price = proc_price;
    }

    public Boolean getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(Boolean is_valid) {
        this.is_valid = is_valid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
