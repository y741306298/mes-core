package com.brt.order.vo;

import com.brt.order.domain.BrtTestMode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 测试方式管理Vo对象 brt_test_mode
 *
 * @author Fgn
 * @date 2024-05-09
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtTestModeVo extends BrtTestMode {

}
