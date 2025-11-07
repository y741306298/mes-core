package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtPriceSheetOrderRecord;
import com.brt.order.vo.BrtPriceSheetOrderRecordVo;

import java.util.List;

/**
 * 报价单_报价记录Service接口
 *
 * @author Fgn
 * @date 2024-07-13
 */
public interface IBrtPriceSheetOrderRecordService extends IService<BrtPriceSheetOrderRecord> {

    /**
     * @description: TODO 分页查询报价单_报价记录列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return TableDataInfo<BrtPriceSheetOrderRecordVo>
     **/
    public TableDataInfo<BrtPriceSheetOrderRecordVo> queryBrtPriceSheetOrderRecordList(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo);

    /**
     * @description: TODO 查询全部报价单_报价记录列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return java.util.List<BrtPriceSheetOrderRecordVo>
     **/
    public List<BrtPriceSheetOrderRecordVo> queryBrtPriceSheetOrderRecordAll(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo);

    /**
     * @description: TODO 根据recordId查询报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: recordId
     * @return:
     * @return BrtPriceSheetOrderRecordVo
     **/
    public BrtPriceSheetOrderRecordVo queryBrtPriceSheetOrderRecordByRecordId(String recordId);

    /**
     * @description: TODO 新增报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderRecordVo insertBrtPriceSheetOrderRecord(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo);

    /**
     * @description: TODO 修改报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo 报价单_报价记录
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderRecordVo updateBrtPriceSheetOrderRecord(BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo);

    /**
     * @description: TODO 批量删除报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param:  recordIds 需要删除的报价单_报价记录主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtPriceSheetOrderRecordByRecordIds(String[] recordIds);




}
