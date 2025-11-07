package com.brt.order.vo;

import java.util.Date;
import java.util.List;

import com.brt.common.core.domain.entity.SysUser;
import com.brt.order.domain.BrtMarketOrder;
import com.brt.system.domain.vo.SysUserVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.brt.order.domain.BrtInInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 入库管理Vo对象 brt_in_inventory
 *
 * @author Fgn
 * @date 2024-07-11
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtInInventoryVo extends BrtInInventory {
    /**
     * 采购单
     */
    private BrtMarketOrder marketOrder;

    private List<BrtInInventoryMaterielVo> inInventoryMaterielVos;

    private SysUser user;
}
