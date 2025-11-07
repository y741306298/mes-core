package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtTestMode;
import com.brt.order.vo.BrtTestModeVo;

import java.util.List;

/**
 * 测试方式管理Service接口
 * 
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtTestModeService extends IService<BrtTestMode> {

    /**
     * @description: TODO 分页查询测试方式管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return TableDataInfo<BrtTestModeVo>
     **/
    public TableDataInfo<BrtTestModeVo> queryBrtTestModeList(BrtTestModeVo brtTestModeVo);

    /**
     * @description: TODO 查询全部测试方式管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return java.util.List<BrtTestModeVo>
     **/
    public List<BrtTestModeVo> queryBrtTestModeAll(BrtTestModeVo brtTestModeVo);

    /**
     * @description: TODO 根据modeId查询测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: modeId
     * @return:
     * @return BrtTestModeVo
     **/
    public BrtTestModeVo queryBrtTestModeByModeId(String modeId);

    /**
     * @description: TODO 新增测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return int
     **/
    public BrtTestModeVo insertBrtTestMode(BrtTestModeVo brtTestModeVo);

    /**
     * @description: TODO 修改测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo 测试方式管理
     * @return:
     * @return int
     **/
    public BrtTestModeVo updateBrtTestMode(BrtTestModeVo brtTestModeVo);

    /**
     * @description: TODO 批量删除测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  modeIds 需要删除的测试方式管理主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtTestModeByModeIds(String[] modeIds);

}
