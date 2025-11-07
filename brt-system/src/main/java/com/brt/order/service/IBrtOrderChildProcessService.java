package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderChildProcess;
import com.brt.order.vo.BrtOrderChildProcessVo;

import java.util.List;

/**
 * 订单子流程Service接口
 * 
 * @author Fgn
 * @date 2024-06-21
 */
public interface IBrtOrderChildProcessService extends IService<BrtOrderChildProcess> {

    /**
     * @description: TODO 分页查询订单子流程列表
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return TableDataInfo<BrtOrderChildProcessVo>
     **/
    public TableDataInfo<BrtOrderChildProcessVo> queryBrtOrderChildProcessList(BrtOrderChildProcessVo brtOrderChildProcessVo);

    /**
     * @description: TODO 查询全部订单子流程列表
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return java.util.List<BrtOrderChildProcessVo>
     **/
    public List<BrtOrderChildProcessVo> queryBrtOrderChildProcessAll(BrtOrderChildProcessVo brtOrderChildProcessVo);

    /**
     * @description: TODO 根据childId查询订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: childId
     * @return:
     * @return BrtOrderChildProcessVo
     **/
    public BrtOrderChildProcessVo queryBrtOrderChildProcessByChildId(String childId);

    /**
     * @description: TODO 新增订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return int
     **/
    public BrtOrderChildProcessVo insertBrtOrderChildProcess(BrtOrderChildProcessVo brtOrderChildProcessVo);

    /**
     * @description: TODO 修改订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo 订单子流程
     * @return:
     * @return int
     **/
    public BrtOrderChildProcessVo updateBrtOrderChildProcess(BrtOrderChildProcessVo brtOrderChildProcessVo);

    /**
     * @description: TODO 批量删除订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param:  childIds 需要删除的订单子流程主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderChildProcessByChildIds(String[] childIds);

}
