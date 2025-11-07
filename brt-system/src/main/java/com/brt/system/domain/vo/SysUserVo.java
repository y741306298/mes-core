package com.brt.system.domain.vo;

import com.brt.common.core.domain.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户信息Vo对象 sys_user
 *
 * @author Fgn
 * @date 2024-01-13
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserVo extends SysUser {


}
