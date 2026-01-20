package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.order.domain.BrtSalesOrderItem;
import com.brt.order.mapper.BrtSalesOrderItemMapper;
import com.brt.order.service.IBrtSalesOrderItemService;
import org.springframework.stereotype.Service;

/**
 * 销售订单明细Service业务层处理
 *
 * @author Fgn
 * @date 2025-02-19
 */
@Service
public class BrtSalesOrderItemServiceImpl extends ServiceImpl<BrtSalesOrderItemMapper, BrtSalesOrderItem>
        implements IBrtSalesOrderItemService {
}
