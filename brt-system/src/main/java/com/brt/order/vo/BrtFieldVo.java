package com.brt.order.vo;

import com.brt.order.domain.BrtField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 自定义字段Vo对象 brt_field
 *
 * @author Fgn
 * @date 2024-06-15
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtFieldVo extends BrtField {

}
