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
 * 供应商信息对象 brt_supplier
 *
 * @author lf
 * @date 2024-04-27
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtSupplier extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String supplierId;

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称")
    private String supplierName;

    /**
     * 供应商编号
     */
    @Excel(name = "供应商编号")
    private String supplierNo;

    /**
     * 供应商类型
     */
    private String supplierType;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    private String linkman;

    /**
     * 电话
     */
    @Excel(name = "电话")
    private String phone;

    /**
     * 地址
     */
    @Excel(name = "地址")
    private String action;

    /**
     * 付款类型
     */
    private String paymentType;

    /**
     * 付款人
     */
    @Excel(name = "付款人")
    private String payer;

    /**
     * 付款名称
     */
    private String paymentName;

    /**
     * 账号
     */
    @Excel(name = "账号")
    private String account;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 附件
     */
    private String files;

    /**
     * 自定义字段
     */
    @Excel(name = "自定义字段")
    private String otherFields;

}
