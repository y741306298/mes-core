package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderMaterielRecord;
import com.brt.order.vo.BrtOrderMaterielRecordVo;

import java.util.List;

/**
 * 数量记录Service接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface IBrtOrderMaterielRecordService extends IService<BrtOrderMaterielRecord> {

    /**
     * @description: TODO 分页查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return TableDataInfo<BrtOrderMaterielRecordVo>
     **/
    public TableDataInfo<BrtOrderMaterielRecordVo> queryBrtOrderMaterielRecordList(BrtOrderMaterielRecordVo brtOrderMaterielRecordVo);

    /**
     * @description: TODO 查询全部数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return java.util.List<BrtOrderMaterielRecordVo>
     **/
    public List<BrtOrderMaterielRecordVo> queryBrtOrderMaterielRecordAll(BrtOrderMaterielRecordVo brtOrderMaterielRecordVo);

    /**
     * @description: TODO 根据recordId查询数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: recordId
     * @return:
     * @return BrtOrderMaterielRecordVo
     **/
    public BrtOrderMaterielRecordVo queryBrtOrderMaterielRecordByRecordId(String recordId);

    /**
     * @description: TODO 新增数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return int
     **/
    public BrtOrderMaterielRecordVo insertBrtOrderMaterielRecord(BrtOrderMaterielRecordVo brtOrderMaterielRecordVo);

    /**
     * @description: TODO 修改数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo 数量记录
     * @return:
     * @return int
     **/
    public BrtOrderMaterielRecordVo updateBrtOrderMaterielRecord(BrtOrderMaterielRecordVo brtOrderMaterielRecordVo);

    /**
     * @description: TODO 批量删除数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param:  recordIds 需要删除的数量记录主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderMaterielRecordByRecordIds(String[] recordIds);

    /**
     * @description: TODO 批量保存
     * @author: FanGN
     * @date: 23:47 2024/6/20
     * @param:
     * @param brtOrderMaterielRecordVoList
     * @return:
     * @return int
     **/
    int insertBrtOrderMaterielRecordList(List<BrtOrderMaterielRecordVo> brtOrderMaterielRecordVoList);
}
