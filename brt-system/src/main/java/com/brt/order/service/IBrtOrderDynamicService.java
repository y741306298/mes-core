package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderDynamic;
import com.brt.order.vo.BrtOrderDynamicVo;

import java.util.List;

/**
 * 订单动态Service接口
 * 
 * @author Fgn
 * @date 2024-05-12
 */
public interface IBrtOrderDynamicService extends IService<BrtOrderDynamic> {

    /**
     * @description: TODO 分页查询订单动态列表
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return TableDataInfo<BrtOrderDynamicVo>
     **/
    public TableDataInfo<BrtOrderDynamicVo> queryBrtOrderDynamicList(BrtOrderDynamicVo brtOrderDynamicVo);

    /**
     * @description: TODO 查询全部订单动态列表
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return java.util.List<BrtOrderDynamicVo>
     **/
    public List<BrtOrderDynamicVo> queryBrtOrderDynamicAll(BrtOrderDynamicVo brtOrderDynamicVo);

    /**
     * @description: TODO 根据dynamicId查询订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: dynamicId
     * @return:
     * @return BrtOrderDynamicVo
     **/
    public BrtOrderDynamicVo queryBrtOrderDynamicByDynamicId(String dynamicId);

    /**
     * @description: TODO 新增订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return int
     **/
    public BrtOrderDynamicVo insertBrtOrderDynamic(BrtOrderDynamicVo brtOrderDynamicVo);

    /**
     * @description: TODO 修改订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo 订单动态
     * @return:
     * @return int
     **/
    public BrtOrderDynamicVo updateBrtOrderDynamic(BrtOrderDynamicVo brtOrderDynamicVo);

    /**
     * @description: TODO 批量删除订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param:  dynamicIds 需要删除的订单动态主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderDynamicByDynamicIds(String[] dynamicIds);

}
