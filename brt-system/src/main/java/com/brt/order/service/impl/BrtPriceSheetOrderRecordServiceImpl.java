package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtPriceSheetOrderRecord;
import com.brt.order.vo.BrtPriceSheetOrderRecordVo;
import com.brt.order.mapper.BrtPriceSheetOrderRecordMapper;
import com.brt.order.service.IBrtPriceSheetOrderRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 报价单_报价记录Service业务层处理
 * 
 * @author Fgn
 * @date 2024-07-13
 */
@Service
public class BrtPriceSheetOrderRecordServiceImpl extends ServiceImpl<BrtPriceSheetOrderRecordMapper, BrtPriceSheetOrderRecord> implements IBrtPriceSheetOrderRecordService {

    @Override
    public TableDataInfo<BrtPriceSheetOrderRecordVo> queryBrtPriceSheetOrderRecordList(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtPriceSheetOrderRecordList(PageUtils.buildPage(), brtPriceSheetOrderRecordVo));
    }

    @Override
    public List<BrtPriceSheetOrderRecordVo> queryBrtPriceSheetOrderRecordAll(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        return this.baseMapper.queryBrtPriceSheetOrderRecordList(brtPriceSheetOrderRecordVo);
    }

    @Override
    public BrtPriceSheetOrderRecordVo queryBrtPriceSheetOrderRecordByRecordId(String recordId) {
        return this.baseMapper.queryBrtPriceSheetOrderRecordByRecordId(recordId);
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderRecordVo insertBrtPriceSheetOrderRecord(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        saveBefore(brtPriceSheetOrderRecordVo);
        int i = this.baseMapper.insert(brtPriceSheetOrderRecordVo);
        return brtPriceSheetOrderRecordVo;
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderRecordVo updateBrtPriceSheetOrderRecord(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo) {
        saveBefore(brtPriceSheetOrderRecordVo);
        int i = this.baseMapper.updateById(brtPriceSheetOrderRecordVo);
        return brtPriceSheetOrderRecordVo;
    }

    @Transactional
    @Override
    public int deleteBrtPriceSheetOrderRecordByRecordIds(String[] recordIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(recordIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param brtPriceSheetOrderRecordVo
     * @return:
     **/
    public void saveBefore(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo){

    }

}
