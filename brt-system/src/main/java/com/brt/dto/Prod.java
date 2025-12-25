package com.brt.dto;

import java.math.BigDecimal;

public class Prod {
    private String prod_code;
    private Integer prod_type;
    private String prod_name;
    private Float min_length;
    private Float max_length;
    private Float min_width;
    private Float max_width;
    private String material_code;
    private String material_name;
    private String material_color;
    private String material_brand;
    private String material_supplier;
    private Integer measure_unitInt;
    private String measure_unit;
    private Float unit_weight;
    private BigDecimal additional_unitfee;
    private String comments;
    private Boolean is_merchandise;

    public Boolean getIs_merchandise() {
        return is_merchandise;
    }

    public void setIs_merchandise(Boolean is_merchandise) {
        this.is_merchandise = is_merchandise;
    }

    public Integer getMeasure_unitInt() {
        return measure_unitInt;
    }

    public void setMeasure_unitInt(Integer measure_unitInt) {
        this.measure_unitInt = measure_unitInt;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public Integer getProd_type() {
        return prod_type;
    }

    public void setProd_type(Integer prod_type) {
        this.prod_type = prod_type;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public Float getMin_length() {
        return min_length;
    }

    public void setMin_length(Float min_length) {
        this.min_length = min_length;
    }

    public Float getMax_length() {
        return max_length;
    }

    public void setMax_length(Float max_length) {
        this.max_length = max_length;
    }

    public Float getMin_width() {
        return min_width;
    }

    public void setMin_width(Float min_width) {
        this.min_width = min_width;
    }

    public Float getMax_width() {
        return max_width;
    }

    public void setMax_width(Float max_width) {
        this.max_width = max_width;
    }

    public String getMaterial_code() {
        return material_code;
    }

    public void setMaterial_code(String material_code) {
        this.material_code = material_code;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMaterial_color() {
        return material_color;
    }

    public void setMaterial_color(String material_color) {
        this.material_color = material_color;
    }

    public String getMaterial_brand() {
        return material_brand;
    }

    public void setMaterial_brand(String material_brand) {
        this.material_brand = material_brand;
    }

    public String getMaterial_supplier() {
        return material_supplier;
    }

    public void setMaterial_supplier(String material_supplier) {
        this.material_supplier = material_supplier;
    }

    public String getMeasure_unit() {
        return measure_unit;
    }

    public void setMeasure_unit(String measure_unit) {
        this.measure_unit = measure_unit;
    }

    public Float getUnit_weight() {
        return unit_weight;
    }

    public void setUnit_weight(Float unit_weight) {
        this.unit_weight = unit_weight;
    }

    public BigDecimal getAdditional_unitfee() {
        return additional_unitfee;
    }

    public void setAdditional_unitfee(BigDecimal additional_unitfee) {
        this.additional_unitfee = additional_unitfee;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
