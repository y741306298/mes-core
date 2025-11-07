package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtCustomerType;
import com.brt.order.vo.BrtCustomerTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户类型管理Mapper接口
 * 
 * @author Fgn
 * @date 2024-04-27
 */
public interface BrtCustomerTypeMapper extends BaseMapper<BrtCustomerType> {

    /**
     * @description: TODO 分页查询客户类型管理列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: page
     * @param: brtCustomerTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtCustomerTypeVo> queryBrtCustomerTypeList(Page<?> page, @Param("brtCustomerTypeVo") BrtCustomerTypeVo brtCustomerTypeVo);

    /**
     * @description: TODO 查询客户类型管理列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtCustomerTypeVo> queryBrtCustomerTypeList(@Param("brtCustomerTypeVo") BrtCustomerTypeVo brtCustomerTypeVo);

    /**
     * @description: TODO 根据typeId查询客户类型管理
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return Vo
     **/
    BrtCustomerTypeVo queryBrtCustomerTypeByTypeId(@Param("TypeId") String typeId);

}
