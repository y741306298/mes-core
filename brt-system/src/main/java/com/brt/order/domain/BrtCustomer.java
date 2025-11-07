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
 * 客户信息对象 brt_customer
 *
 * @author Fgn
 * @date 2024-04-27
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCustomer extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String customerId;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String customerName;

    /**
     * 客户编号
     */
    @Excel(name = "客户编号")
    private String customerNo;

    /**
     * 类型ID
     */
    @Excel(name = "客户类型",dictType = "customer_type")
    private String typeId;

    /**
     * 级别ID
     */
    /**
     * 类型ID
     */
    @Excel(name = "客户级别",dictType = "customer_grade")
    private String gradeId;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    private String contact;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String contactTel;

    /**
     * 业务员ID
     */
    private String userId;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String customerRemark;

    /**
     * 客户状态(Y=正常,N=禁用)
     */
    //@Excel(name = "客户状态")
    private String customerStatus;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String attachments;

    /**
     * 客户地址
     */
    @Excel(name = "客户地址")
    private String customerAddress;

    /**
     * 物流地址
     */
    @Excel(name = "物流地址")
    private String logisticsAddress;

    /**
     * 自定义字段
     */
    @Excel(name = "自定义字段")
    private String otherFields;


        /***************************自定义字段*****************************/

}
