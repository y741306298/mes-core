package com.brt.order.vo;

import com.brt.order.domain.BrtTestType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 测试类型管理Vo对象 brt_test_type
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtTestTypeVo extends BrtTestType {

}
