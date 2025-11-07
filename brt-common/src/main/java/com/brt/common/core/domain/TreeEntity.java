package com.brt.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TreeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 父菜单ID
     */
    private Object parentId;

    /**
     * 显示顺序
     */
    @TableField(exist = false)
    private Integer orderNum;

    /**
     * 祖级列表
     */
    @TableField(exist = false)
    private String ancestors;

    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<?> children = new ArrayList<>();

}
