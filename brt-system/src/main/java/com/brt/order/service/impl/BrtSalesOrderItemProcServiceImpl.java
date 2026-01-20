package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.order.domain.BrtSalesOrderItemProc;
import com.brt.order.mapper.BrtSalesOrderItemProcMapper;
import com.brt.order.service.IBrtSalesOrderItemProcService;
import org.springframework.stereotype.Service;

/**
 * 销售订单工艺Service业务层处理
 *
 * @author Fgn
 * @date 2025-02-19
 */
@Service
public class BrtSalesOrderItemProcServiceImpl extends ServiceImpl<BrtSalesOrderItemProcMapper, BrtSalesOrderItemProc>
        implements IBrtSalesOrderItemProcService {
}
