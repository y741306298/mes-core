package com.brt.order.domain;

import com.brt.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.brt.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户地址对象 brt_customer_address
 *
 * @author Fgn
 * @date 2024-04-27
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCustomerAddress extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String addressId;

    /**
     * 客户ID
     */
    @Excel(name = "客户ID")
    private String customerId;

    /**
     * 地址类型(0=客户地址,1=物流地址)
     */
    @Excel(name = "地址类型(0=客户地址,1=物流地址)")
    private String addressType;

    /**
     * 地址简称
     */
    @Excel(name = "地址简称")
    private String addressShort;

    /**
     * 目的地
     */
    @Excel(name = "目的地")
    private String destination;

    /**
     * 物流公司
     */
    @Excel(name = "物流公司")
    private String logisticsCompany;

    /**
     * 省
     */
    @Excel(name = "省")
    private String addressProvince;

    /**
     * 市
     */
    @Excel(name = "市")
    private String addressCity;

    /**
     * 区
     */
    @Excel(name = "区")
    private String addressArea;

    /**
     * 详细地址
     */
    @Excel(name = "详细地址")
    private String addressDetails;

    /**
     * 邮编
     */
    @Excel(name = "邮编")
    private String postcode;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String addressRemark;


        /***************************自定义字段*****************************/

}
