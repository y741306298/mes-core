package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.domain.BrtOutInventory;
import com.brt.order.vo.BrtOutInventoryVo;

import java.util.List;

/**
 * 入库管理Service接口
 *
 * @author Fgn
 * @date 2024-07-11
 */
public interface IBrtOutInventoryService extends IService<BrtOutInventory> {

    /**
     * @description: TODO 分页查询入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 入库管理
     * @return:
     * @return TableDataInfo<BrtOutInventoryVo>
     **/
    public TableDataInfo<BrtOutInventoryVo> queryBrtOutInventoryList(BrtOutInventoryVo brtOutInventoryVo);

    /**
     * @description: TODO 查询全部入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 入库管理
     * @return:
     * @return java.util.List<BrtOutInventoryVo>
     **/
    public List<BrtOutInventoryVo> queryBrtOutInventoryAll(BrtOutInventoryVo brtOutInventoryVo);

    /**
     * @description: TODO 根据outInventoryId查询入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: outInventoryId
     * @return:
     * @return BrtOutInventoryVo
     **/
    public BrtOutInventoryVo queryBrtOutInventoryByOutInventoryId(String outInventoryId);

    /**
     * @description: TODO 新增入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 入库管理
     * @return:
     * @return int
     **/
    public BrtOutInventoryVo insertBrtOutInventory(BrtOutInventoryVo brtOutInventoryVo);

    /**
     * @description: TODO 修改入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo 入库管理
     * @return:
     * @return int
     **/
    public BrtOutInventoryVo updateBrtOutInventory(BrtOutInventoryVo brtOutInventoryVo);

    /**
     * @description: TODO 批量删除入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  outInventoryIds 需要删除的入库管理主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOutInventoryByOutInventoryIds(String[] outInventoryIds);

    /**
     * @description: TODO 批量删除入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  outInventoryIds 需要删除的入库管理主键集合
     * @return:
     * @return int
     **/
    void affirm(String outInventoryId);

    /**
     * 生成入库单
     */
    void createOutInventory(String orderId, List<BrtOrderBoom> orderBoomList);

}
