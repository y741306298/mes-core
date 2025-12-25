package com.brt.hub.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.brt.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 工艺信息对象 brt_proc
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("brt_proc")
public class HubProc extends BaseEntity {

    /**
     * 工艺编码
     */
    @TableId(value = "proc_code", type = IdType.INPUT)
    private String procCode;

    /**
     * 工艺名称
     */
    @TableField("proc_name")
    private String procName;

    /**
     * 附件类型列表
     */
    @TableField("proc_attachment_type_list")
    private String procAttachmentTypeList;

    /**
     * 度量单位（数字）
     */
    @TableField("measure_unit")
    private Integer measureUnit;

    /**
     * 度量单位（名称）
     */
    @TableField("measure_unit_str")
    private String measureUnitStr;

    /**
     * 工艺价格
     */
    @TableField("proc_price")
    private BigDecimal procPrice;

    /**
     * 是否有效
     */
    @TableField("is_valid")
    private Boolean valid;

    /**
     * 备注
     */
    @TableField("comments")
    private String comments;
}
