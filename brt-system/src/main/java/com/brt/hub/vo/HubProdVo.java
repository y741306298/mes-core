package com.brt.hub.vo;

import com.brt.hub.domain.HubProd;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 产品信息Vo对象 brt_prod
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HubProdVo extends HubProd {
}
