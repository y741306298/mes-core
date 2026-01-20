package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.order.domain.BrtSalesOrderPackage;
import com.brt.order.mapper.BrtSalesOrderPackageMapper;
import com.brt.order.service.IBrtSalesOrderPackageService;
import org.springframework.stereotype.Service;

/**
 * 销售订单包裹Service业务层处理
 *
 * @author Fgn
 * @date 2025-02-19
 */
@Service
public class BrtSalesOrderPackageServiceImpl extends ServiceImpl<BrtSalesOrderPackageMapper, BrtSalesOrderPackage>
        implements IBrtSalesOrderPackageService {
}
