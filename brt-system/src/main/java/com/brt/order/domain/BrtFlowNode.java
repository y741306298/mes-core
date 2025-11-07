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

import java.util.List;

/**
 * 模板节点对象 brt_flow_node
 *
 * @author Fgn
 * @date 2024-04-30
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtFlowNode extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String nodeId;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 进度名称
     */
    @Excel(name = "进度名称")
    private String nodeName;

    /**
     * 节点类型(0=审批,1=开票金额纪录任务,2=收款金额纪录任务,3=状态纪录任务,4=数量记录任务,5=自定义纪录任务,6=产品纪录任务)
     */
    @Excel(name = "节点类型(0=审批,1=开票金额纪录任务,2=收款金额纪录任务,3=状态纪录任务,4=数量记录任务,5=自定义纪录任务,6=产品纪录任务)")
    private String nodeType;

    /**
     * 节点状态(Y=正常,N=禁用)
     */
    @Excel(name = "节点状态(Y=正常,N=禁用)")
    private String nodeStatus;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 限制记录添加(1=是,0=否)
     */
    @Excel(name = "限制记录添加(1=是,0=否)")
    private Boolean limitAdd;

    /**
     * 默认截止日期(0=无,1=以开单日期来推算,2=以交货日期来推算,3=以任务节点完成时间来推算)
     */
    @Excel(name = "默认截止日期(0=无,1=以开单日期来推算,2=以交货日期来推算,3=以任务节点完成时间来推算)")
    private String deadlineType;

    /**
     * 开启自动完成(1=是,0=否)
     */
    @Excel(name = "开启自动完成(1=是,0=否)")
    private Boolean autoCompletion;

    /**
     * 其他设置
     */
    private String otherSetting;

    /**
     * 天
     */
    private String day;
    /**
     * 小时
     */
    private String hour;

    /**
     * 分钟
     */
    private String minute;

    /***************************自定义字段*****************************/

}
