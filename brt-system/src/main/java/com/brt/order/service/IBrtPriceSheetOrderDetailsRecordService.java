package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtPriceSheetOrderDetailsRecord;
import com.brt.order.vo.BrtPriceSheetOrderDetailsRecordVo;

import java.util.List;

/**
 * 报价记录详情Service接口
 * 
 * @author Fgn
 * @date 2024-07-13
 */
public interface IBrtPriceSheetOrderDetailsRecordService extends IService<BrtPriceSheetOrderDetailsRecord> {

    /**
     * @description: TODO 分页查询报价记录详情列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return TableDataInfo<BrtPriceSheetOrderDetailsRecordVo>
     **/
    public TableDataInfo<BrtPriceSheetOrderDetailsRecordVo> queryBrtPriceSheetOrderDetailsRecordList(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo);

    /**
     * @description: TODO 查询全部报价记录详情列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return java.util.List<BrtPriceSheetOrderDetailsRecordVo>
     **/
    public List<BrtPriceSheetOrderDetailsRecordVo> queryBrtPriceSheetOrderDetailsRecordAll(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo);

    /**
     * @description: TODO 根据detailsId查询报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: detailsId
     * @return:
     * @return BrtPriceSheetOrderDetailsRecordVo
     **/
    public BrtPriceSheetOrderDetailsRecordVo queryBrtPriceSheetOrderDetailsRecordByDetailsId(String detailsId);

    /**
     * @description: TODO 新增报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderDetailsRecordVo insertBrtPriceSheetOrderDetailsRecord(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo);

    /**
     * @description: TODO 修改报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo 报价记录详情
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderDetailsRecordVo updateBrtPriceSheetOrderDetailsRecord(BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo);

    /**
     * @description: TODO 批量删除报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param:  detailsIds 需要删除的报价记录详情主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtPriceSheetOrderDetailsRecordByDetailsIds(String[] detailsIds);

}
