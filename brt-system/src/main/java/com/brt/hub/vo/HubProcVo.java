package com.brt.hub.vo;

import com.brt.hub.domain.HubProc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 工艺信息Vo对象 brt_proc
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HubProcVo extends HubProc {
}
