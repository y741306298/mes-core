package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.brt.order.domain.BrtSalesOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售订单明细Mapper接口
 *
 * @author Fgn
 * @date 2025-02-19
 */
@Mapper
public interface BrtSalesOrderItemMapper extends BaseMapper<BrtSalesOrderItem> {
}
