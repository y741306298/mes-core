package com.brt.order.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 出库管理对象 brt_out_inventory
 *
 * @author Fgn
 * @date 2024-07-11
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOutInventory extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String outInventoryId;

    /**
     * 销售单ID
     */
    @Excel(name = "销售单ID")
    private String orderId;

    /**
     * 出库单号
     */
    @Excel(name = "出库单号")
    private String outInventoryNo;

    /**
     * 入库类型/出库类型（0:手动出库，1:销售出库）
     */
    @Excel(name = "入库类型/出库类型", readConverterExp = "0=:手动出库，1:销售出库")
    private String outInventoryType;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String outInventoryStatus;

    /**
     * 申请人
     */
    @Excel(name = "申请人")
    private String applicat;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String files;

    /**
     * 记录详情页单选按钮状态
     */
    private String uploadType;

        /***************************自定义字段*****************************/

}
