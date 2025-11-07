package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtIntertransferOrder;
import com.brt.order.vo.BrtIntertransferOrderVo;

import java.util.List;

/**
 * 互转单Service接口
 * 
 * @author Fgn
 * @date 2024-05-16
 */
public interface IBrtIntertransferOrderService extends IService<BrtIntertransferOrder> {

    /**
     * @description: TODO 分页查询互转单列表
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return TableDataInfo<BrtIntertransferOrderVo>
     **/
    public TableDataInfo<BrtIntertransferOrderVo> queryBrtIntertransferOrderList(BrtIntertransferOrderVo brtIntertransferOrderVo);

    /**
     * @description: TODO 查询全部互转单列表
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return java.util.List<BrtIntertransferOrderVo>
     **/
    public List<BrtIntertransferOrderVo> queryBrtIntertransferOrderAll(BrtIntertransferOrderVo brtIntertransferOrderVo);

    /**
     * @description: TODO 根据intertransferId查询互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: intertransferId
     * @return:
     * @return BrtIntertransferOrderVo
     **/
    public BrtIntertransferOrderVo queryBrtIntertransferOrderByIntertransferId(String intertransferId);

    /**
     * @description: TODO 新增互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return int
     **/
    public BrtIntertransferOrderVo insertBrtIntertransferOrder(BrtIntertransferOrderVo brtIntertransferOrderVo);

    /**
     * @description: TODO 修改互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo 互转单
     * @return:
     * @return int
     **/
    public BrtIntertransferOrderVo updateBrtIntertransferOrder(BrtIntertransferOrderVo brtIntertransferOrderVo);

    /**
     * @description: TODO 批量删除互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param:  intertransferIds 需要删除的互转单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtIntertransferOrderByIntertransferIds(String[] intertransferIds);

}
