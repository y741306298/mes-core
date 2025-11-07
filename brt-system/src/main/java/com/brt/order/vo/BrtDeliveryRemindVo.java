package com.brt.order.vo;

import com.brt.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class BrtDeliveryRemindVo {

    @Excel(name = "计划送货日期",dateFormat="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String deliveryTime;// 计划送货日期

    @Excel(name = "客户名称")
    private String customerName; // 客户名称

    @Excel(name = "送货日期",dateFormat="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime; // 送货日期

    private String imgPath; //图片

    @Excel(name = "产品名称")
    private String materielName; // 产品名称

    @Excel(name = "销售数量")
    private BigDecimal detailsNum; // 销售数量

    @Excel(name = "已送货数量")
    private BigDecimal fulfillNum; // 已送货数量

    @Excel(name = "剩余数量")
    private BigDecimal surplusNum; // 剩余数量

    @Excel(name = "备注")
    private String remark; // 备注

    @Excel(name = "送货地址")
    private String address;// 送货地址

    @Excel(name = "关联业务")
    private String orderNo;  // 关联业务

    private String orderId;//销售单ID
    private String customerId;//客户ID
    private String handle;
    private String materielId;
    private String timeStart;
    private String timeEnd;
    private String templateId;
    private String planId;
    private BigDecimal thisNum;

    private String customerType;//供应商类别  查询
    private List<String> status;// 收货状态 --查询

}
