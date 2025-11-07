package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtCustomerType;
import com.brt.order.vo.BrtCustomerTypeVo;

import java.util.List;

/**
 * 客户类型管理Service接口
 * 
 * @author Fgn
 * @date 2024-04-27
 */
public interface IBrtCustomerTypeService extends IService<BrtCustomerType> {

    /**
     * @description: TODO 分页查询客户类型管理列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return TableDataInfo<BrtCustomerTypeVo>
     **/
    public TableDataInfo<BrtCustomerTypeVo> queryBrtCustomerTypeList(BrtCustomerTypeVo brtCustomerTypeVo);

    /**
     * @description: TODO 查询全部客户类型管理列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return java.util.List<BrtCustomerTypeVo>
     **/
    public List<BrtCustomerTypeVo> queryBrtCustomerTypeAll(BrtCustomerTypeVo brtCustomerTypeVo);

    /**
     * @description: TODO 根据typeId查询客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: typeId
     * @return:
     * @return BrtCustomerTypeVo
     **/
    public BrtCustomerTypeVo queryBrtCustomerTypeByTypeId(String typeId);

    /**
     * @description: TODO 新增客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return int
     **/
    public BrtCustomerTypeVo insertBrtCustomerType(BrtCustomerTypeVo brtCustomerTypeVo);

    /**
     * @description: TODO 修改客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo 客户类型管理
     * @return:
     * @return int
     **/
    public BrtCustomerTypeVo updateBrtCustomerType(BrtCustomerTypeVo brtCustomerTypeVo);

    /**
     * @description: TODO 批量删除客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param:  typeIds 需要删除的客户类型管理主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtCustomerTypeByTypeIds(String[] typeIds);

}
