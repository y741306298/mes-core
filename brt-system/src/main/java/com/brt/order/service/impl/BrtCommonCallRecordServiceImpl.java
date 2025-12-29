package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.order.domain.BrtCommonCallRecord;
import com.brt.order.mapper.BrtCommonCallRecordMapper;
import com.brt.order.service.IBrtCommonCallRecordService;
import org.springframework.stereotype.Service;

/**
 * 通用调用记录服务实现
 */
@Service
public class BrtCommonCallRecordServiceImpl extends ServiceImpl<BrtCommonCallRecordMapper, BrtCommonCallRecord>
    implements IBrtCommonCallRecordService {
}

