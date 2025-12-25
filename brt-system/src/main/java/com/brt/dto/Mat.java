package com.brt.dto;

import java.math.BigDecimal;

public class Mat {
    private String mat_code;
    private String mat_name;
    private String mat_category;
    private String mat_color;
    private String mat_brand;
    private String mat_supplier;
    private String mat_width;
    private String mat_length;
    private String mat_thickness;
    private String package_name;
    private Integer measure_unitInt;
    private String measure_unit;
    private Float unit_weight;
    private BigDecimal unit_price;
    private Boolean is_valid;
    private String comments;

    public Integer getMeasure_unitInt() {
        return measure_unitInt;
    }

    public void setMeasure_unitInt(Integer measure_unitInt) {
        this.measure_unitInt = measure_unitInt;
    }

    public String getMat_code() {
        return mat_code;
    }

    public void setMat_code(String mat_code) {
        this.mat_code = mat_code;
    }

    public String getMat_name() {
        return mat_name;
    }

    public void setMat_name(String mat_name) {
        this.mat_name = mat_name;
    }

    public String getMat_category() {
        return mat_category;
    }

    public void setMat_category(String mat_category) {
        this.mat_category = mat_category;
    }

    public String getMat_color() {
        return mat_color;
    }

    public void setMat_color(String mat_color) {
        this.mat_color = mat_color;
    }

    public String getMat_brand() {
        return mat_brand;
    }

    public void setMat_brand(String mat_brand) {
        this.mat_brand = mat_brand;
    }

    public String getMat_supplier() {
        return mat_supplier;
    }

    public void setMat_supplier(String mat_supplier) {
        this.mat_supplier = mat_supplier;
    }

    public String getMat_width() {
        return mat_width;
    }

    public void setMat_width(String mat_width) {
        this.mat_width = mat_width;
    }

    public String getMat_length() {
        return mat_length;
    }

    public void setMat_length(String mat_length) {
        this.mat_length = mat_length;
    }

    public String getMat_thickness() {
        return mat_thickness;
    }

    public void setMat_thickness(String mat_thickness) {
        this.mat_thickness = mat_thickness;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
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

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
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
