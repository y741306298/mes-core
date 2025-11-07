package com.brt.order.vo;

import java.util.Date;
import java.util.List;

import com.brt.common.core.domain.entity.SysUser;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.domain.BrtOutInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 出库管理Vo对象 brt_out_inventory
 *
 * @author Fgn
 * @date 2024-07-11
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtOutInventoryVo extends BrtOutInventory {
    /**
     * 销售单
     */
    private BrtSalesOrder salesOrder;

    private List<BrtOutInventoryMaterielVo> outInventoryMaterielVos;

    private SysUser user;

}
