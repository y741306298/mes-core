package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderCollectionRecord;
import com.brt.order.vo.BrtOrderCollectionRecordVo;

import java.util.List;

/**
 * 收款记录Service接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface IBrtOrderCollectionRecordService extends IService<BrtOrderCollectionRecord> {

    /**
     * @description: TODO 分页查询收款记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return TableDataInfo<BrtOrderCollectionRecordVo>
     **/
    public TableDataInfo<BrtOrderCollectionRecordVo> queryBrtOrderCollectionRecordList(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo);

    /**
     * @description: TODO 查询全部收款记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return java.util.List<BrtOrderCollectionRecordVo>
     **/
    public List<BrtOrderCollectionRecordVo> queryBrtOrderCollectionRecordAll(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo);

    /**
     * @description: TODO 根据recordId查询收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: recordId
     * @return:
     * @return BrtOrderCollectionRecordVo
     **/
    public BrtOrderCollectionRecordVo queryBrtOrderCollectionRecordByRecordId(String recordId);

    /**
     * @description: TODO 新增收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return int
     **/
    public BrtOrderCollectionRecordVo insertBrtOrderCollectionRecord(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo);

    /**
     * @description: TODO 修改收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo 收款记录
     * @return:
     * @return int
     **/
    public BrtOrderCollectionRecordVo updateBrtOrderCollectionRecord(BrtOrderCollectionRecordVo brtOrderCollectionRecordVo);

    /**
     * @description: TODO 批量删除收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param:  recordIds 需要删除的收款记录主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderCollectionRecordByRecordIds(String[] recordIds);

}
