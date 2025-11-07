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
 * 自定义字段对象 brt_field
 *
 * @author Fgn
 * @date 2024-06-15
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtField extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String fieldId;

    /**
     * 字段名称
     */
    @Excel(name = "字段名称")
    private String fieldName;

    /**
     * 字段类型
     */
    @Excel(name = "字段类型")
    private String fieldType;

    /**
     * 业务类型
     */
    @Excel(name = "业务类型")
    private String businessType;

    /**
     * 字典类型
     */
    @Excel(name = "字典类型")
    private String dictType;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String fieldRemark;

    /**
     * 字段状态
     */
    @Excel(name = "字段状态")
    private Boolean fieldStatus;

    /**
     * 是否必输
     */
    @Excel(name = "是否必输")
    private Boolean isMust;

    /**
     * 字段顺序
     */
    @Excel(name = "字段顺序")
    private Integer filedSort;


        /***************************自定义字段*****************************/

}
