package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderInvoiceRecord;
import com.brt.order.vo.BrtOrderInvoiceRecordVo;

import java.util.List;

/**
 * 开票记录Service接口
 * 
 * @author Fgn
 * @date 2024-06-19
 */
public interface IBrtOrderInvoiceRecordService extends IService<BrtOrderInvoiceRecord> {

    /**
     * @description: TODO 分页查询开票记录列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return TableDataInfo<BrtOrderInvoiceRecordVo>
     **/
    public TableDataInfo<BrtOrderInvoiceRecordVo> queryBrtOrderInvoiceRecordList(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo);

    /**
     * @description: TODO 查询全部开票记录列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return java.util.List<BrtOrderInvoiceRecordVo>
     **/
    public List<BrtOrderInvoiceRecordVo> queryBrtOrderInvoiceRecordAll(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo);

    /**
     * @description: TODO 根据recordId查询开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: recordId
     * @return:
     * @return BrtOrderInvoiceRecordVo
     **/
    public BrtOrderInvoiceRecordVo queryBrtOrderInvoiceRecordByRecordId(String recordId);

    /**
     * @description: TODO 新增开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return int
     **/
    public BrtOrderInvoiceRecordVo insertBrtOrderInvoiceRecord(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo);

    /**
     * @description: TODO 修改开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo 开票记录
     * @return:
     * @return int
     **/
    public BrtOrderInvoiceRecordVo updateBrtOrderInvoiceRecord(BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo);

    /**
     * @description: TODO 批量删除开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param:  recordIds 需要删除的开票记录主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderInvoiceRecordByRecordIds(String[] recordIds);

}
