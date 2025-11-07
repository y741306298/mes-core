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
 * 账单类型对象 brt_check_type
 *
 * @author Fgn
 * @date 2024-05-15
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCheckType extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String typeId;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String typeName;

    /**
     * 类型(0=支出,1=收入)
     */
    @Excel(name = "类型(0=支出,1=收入)")
    private String type;


        /***************************自定义字段*****************************/

}
