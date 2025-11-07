package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtMarketMateriel;
import com.brt.order.vo.BrtMarketMaterielVo;

import java.util.List;

/**
 * 数量记录Service接口
 *
 * @author Fgn
 * @date 2024-06-20
 */
public interface IBrtMarketMaterielService extends IService<BrtMarketMateriel> {

    /**
     * @description: TODO 分页查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtMarketMaterielVo 数量记录
     * @return:
     * @return TableDataInfo<BrtMarketMaterielVo>
     **/
    public TableDataInfo<BrtMarketMaterielVo> queryBrtMarketMaterielList(BrtMarketMaterielVo brtMarketMaterielVo);

    /**
     * @description: TODO 查询全部数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtMarketMaterielVo 数量记录
     * @return:
     * @return java.util.List<BrtMarketMaterielVo>
     **/
    public List<BrtMarketMaterielVo> queryBrtMarketMaterielAll(BrtMarketMaterielVo brtMarketMaterielVo);

    /**
     * @description: TODO 根据recordId查询数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: recordId
     * @return:
     * @return BrtMarketMaterielVo
     **/
    public BrtMarketMaterielVo queryBrtMarketMaterielByRecordId(String recordId);

    /**
     * @description: TODO 新增数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtMarketMaterielVo 数量记录
     * @return:
     * @return int
     **/
    public BrtMarketMaterielVo insertBrtMarketMateriel(BrtMarketMaterielVo brtMarketMaterielVo);

    /**
     * @description: TODO 修改数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtMarketMaterielVo 数量记录
     * @return:
     * @return int
     **/
    public BrtMarketMaterielVo updateBrtMarketMateriel(BrtMarketMaterielVo brtMarketMaterielVo);

    /**
     * @description: TODO 批量删除数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param:  recordIds 需要删除的数量记录主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtMarketMaterielByRecordIds(String[] recordIds);

    /**
     * @description: TODO 批量保存
     * @author: FanGN
     * @date: 23:47 2024/6/20
     * @param:
     * @param brtMarketMaterielVoList
     * @return:
     * @return int
     **/
    int insertBrtMarketMaterielList(List<BrtMarketMaterielVo> brtMarketMaterielVoList);
}
