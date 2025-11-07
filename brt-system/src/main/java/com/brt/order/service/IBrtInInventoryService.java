package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtInInventory;
import com.brt.order.vo.BrtInInventoryVo;

import java.util.List;

/**
 * 入库管理Service接口
 *
 * @author Fgn
 * @date 2024-07-11
 */
public interface IBrtInInventoryService extends IService<BrtInInventory> {

    /**
     * @description: TODO 分页查询入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return TableDataInfo<BrtInInventoryVo>
     **/
    public TableDataInfo<BrtInInventoryVo> queryBrtInInventoryList(BrtInInventoryVo brtInInventoryVo);

    /**
     * @description: TODO 查询全部入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return java.util.List<BrtInInventoryVo>
     **/
    public List<BrtInInventoryVo> queryBrtInInventoryAll(BrtInInventoryVo brtInInventoryVo);

    /**
     * @description: TODO 根据inInventoryId查询入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: inInventoryId
     * @return:
     * @return BrtInInventoryVo
     **/
    public BrtInInventoryVo queryBrtInInventoryByInInventoryId(String inInventoryId);

    /**
     * @description: TODO 新增入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return int
     **/
    public BrtInInventoryVo insertBrtInInventory(BrtInInventoryVo brtInInventoryVo);

    /**
     * @description: TODO 修改入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo 入库管理
     * @return:
     * @return int
     **/
    public BrtInInventoryVo updateBrtInInventory(BrtInInventoryVo brtInInventoryVo);

    /**
     * @description: TODO 批量删除入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  inInventoryIds 需要删除的入库管理主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtInInventoryByInInventoryIds(String[] inInventoryIds);

    /**
     * @description: TODO 批量删除入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  inInventoryIds 需要删除的入库管理主键集合
     * @return:
     * @return int
     **/
    void affirm(String inInventoryId);

    /**
     * 生成出库单
     */
    void createInInventory(String orderId);

}
