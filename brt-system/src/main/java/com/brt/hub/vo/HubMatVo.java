package com.brt.hub.vo;

import com.brt.hub.domain.HubMat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 材料信息Vo对象 brt_mat
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HubMatVo extends HubMat {
}
