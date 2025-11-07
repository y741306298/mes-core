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
 * 节点字段对象 brt_flow_node_field
 *
 * @author Fgn
 * @date 2024-04-30
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtFlowNodeField extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String fieldId;

    /**
     * 模板ID
     */
    @Excel(name = "模板ID")
    private String templateId;

    /**
     * 节点ID
     */
    @Excel(name = "节点ID")
    private String nodeId;

    /**
     * 节点类型(0=审批,1=开票金额纪录任务,2=收款金额纪录任务,3=状态纪录任务,4=数量记录任务,5=自定义纪录任务,6=产品纪录任务)
     */
    @Excel(name = "节点类型(0=审批,1=开票金额纪录任务,2=收款金额纪录任务,3=状态纪录任务,4=数量记录任务,5=自定义纪录任务,6=产品纪录任务)")
    private String nodeType;

    /**
     * 字段名称
     */
    @Excel(name = "字段名称")
    private String fieldName;

    /**
     * 字段类型(0=下拉框,1=多行文本框,2=单行文本框,3=单选框,4=复选框,5=数字框,6=日期,7=富文本,8=文件上传,9=按钮)
     */
    @Excel(name = "字段类型(0=下拉框,1=多行文本框,2=单行文本框,3=单选框,4=复选框,5=数字框,6=日期,7=富文本,8=文件上传,9=按钮)")
    private String fieldType;

    /**
     * 字典类型
     */
    @Excel(name = "字典类型")
    private String dictType;

    /**
     * 业务类型()
     */
    @Excel(name = "业务类型")
    private String businessType;


        /***************************自定义字段*****************************/

}
