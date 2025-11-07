package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtTestType;
import com.brt.order.vo.BrtTestTypeVo;

import java.util.List;

/**
 * 测试类型管理Service接口
 * 
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtTestTypeService extends IService<BrtTestType> {

    /**
     * @description: TODO 分页查询测试类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return TableDataInfo<BrtTestTypeVo>
     **/
    public TableDataInfo<BrtTestTypeVo> queryBrtTestTypeList(BrtTestTypeVo brtTestTypeVo);

    /**
     * @description: TODO 查询全部测试类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return java.util.List<BrtTestTypeVo>
     **/
    public List<BrtTestTypeVo> queryBrtTestTypeAll(BrtTestTypeVo brtTestTypeVo);

    /**
     * @description: TODO 根据typeId查询测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: typeId
     * @return:
     * @return BrtTestTypeVo
     **/
    public BrtTestTypeVo queryBrtTestTypeByTypeId(String typeId);

    /**
     * @description: TODO 新增测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return int
     **/
    public BrtTestTypeVo insertBrtTestType(BrtTestTypeVo brtTestTypeVo);

    /**
     * @description: TODO 修改测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo 测试类型管理
     * @return:
     * @return int
     **/
    public BrtTestTypeVo updateBrtTestType(BrtTestTypeVo brtTestTypeVo);

    /**
     * @description: TODO 批量删除测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  typeIds 需要删除的测试类型管理主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtTestTypeByTypeIds(String[] typeIds);

}
