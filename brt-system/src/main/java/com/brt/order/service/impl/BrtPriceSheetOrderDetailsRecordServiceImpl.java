package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtPriceSheetOrderDetailsRecord;
import com.brt.order.vo.BrtPriceSheetOrderDetailsRecordVo;
import com.brt.order.mapper.BrtPriceSheetOrderDetailsRecordMapper;
import com.brt.order.service.IBrtPriceSheetOrderDetailsRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 报价记录详情Service业务层处理
 * 
 * @author Fgn
 * @date 2024-07-13
 */
@Service
public class BrtPriceSheetOrderDetailsRecordServiceImpl extends ServiceImpl<BrtPriceSheetOrderDetailsRecordMapper, BrtPriceSheetOrderDetailsRecord> implements IBrtPriceSheetOrderDetailsRecordService {

    @Override
    public TableDataInfo<BrtPriceSheetOrderDetailsRecordVo> queryBrtPriceSheetOrderDetailsRecordList(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtPriceSheetOrderDetailsRecordList(PageUtils.buildPage(), brtPriceSheetOrderDetailsRecordVo));
    }

    @Override
    public List<BrtPriceSheetOrderDetailsRecordVo> queryBrtPriceSheetOrderDetailsRecordAll(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        return this.baseMapper.queryBrtPriceSheetOrderDetailsRecordList(brtPriceSheetOrderDetailsRecordVo);
    }

    @Override
    public BrtPriceSheetOrderDetailsRecordVo queryBrtPriceSheetOrderDetailsRecordByDetailsId(String detailsId) {
        return this.baseMapper.queryBrtPriceSheetOrderDetailsRecordByDetailsId(detailsId);
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderDetailsRecordVo insertBrtPriceSheetOrderDetailsRecord(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        saveBefore(brtPriceSheetOrderDetailsRecordVo);
        int i = this.baseMapper.insert(brtPriceSheetOrderDetailsRecordVo);
        return brtPriceSheetOrderDetailsRecordVo;
    }

    @Transactional
    @Override
    public BrtPriceSheetOrderDetailsRecordVo updateBrtPriceSheetOrderDetailsRecord(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo) {
        saveBefore(brtPriceSheetOrderDetailsRecordVo);
        int i = this.baseMapper.updateById(brtPriceSheetOrderDetailsRecordVo);
        return brtPriceSheetOrderDetailsRecordVo;
    }

    @Transactional
    @Override
    public int deleteBrtPriceSheetOrderDetailsRecordByDetailsIds(String[] detailsIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(detailsIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param brtPriceSheetOrderDetailsRecordVo
     * @return:
     **/
    public void saveBefore(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo){

    }

}
