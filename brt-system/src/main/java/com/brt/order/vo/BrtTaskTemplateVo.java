package com.brt.order.vo;

import com.brt.order.domain.BrtTaskTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 任务模板视图对象
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtTaskTemplateVo extends BrtTaskTemplate {
    private static final long serialVersionUID = 1L;
}
