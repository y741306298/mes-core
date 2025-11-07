package com.brt.order.vo;

import com.brt.order.domain.BrtAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 账户类型Vo对象 brt_account
 *
 * @author Fgn
 * @date 2024-05-15
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtAccountVo extends BrtAccount {

}
