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
 * 流程模板对象 brt_flow_template
 *
 * @author Fgn
 * @date 2024-04-30
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtFlowTemplate extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String templateId;

    /**
     * 模板名称
     */
    @Excel(name = "模板名称")
    private String templateName;

    /**
     * 负责人
     */
    @Excel(name = "负责人")
    private String userId;

    /**
     * 是否顺序执行(Y=是,N=否)
     */
    @Excel(name = "是否顺序执行(Y=是,N=否)")
    private Boolean isSeqExecute;

    /**
     * 是否自动延期(Y=是,N=否)
     */
    @Excel(name = "是否自动延期(Y=是,N=否)")
    private Boolean isAutoPostpone;

    /**
     * 模板状态(Y=正常,N=关闭)
     */
    @Excel(name = "模板状态(Y=正常,N=关闭)")
    private Boolean templateStatus;

    /**
     * 是否审核(Y=正常,N=关闭)
     */
    @Excel(name = "是否审核(Y=正常,N=关闭)")
    private String isAudit;

    /**
     * 审核用户ID
     */
    @Excel(name = "审核用户ID")
    private String auditUserId;


        /***************************自定义字段*****************************/

}
