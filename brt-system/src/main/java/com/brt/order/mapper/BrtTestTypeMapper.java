package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtTestType;
import com.brt.order.vo.BrtTestTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试类型管理Mapper接口
 * 
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtTestTypeMapper extends BaseMapper<BrtTestType> {

    /**
     * @description: TODO 分页查询测试类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtTestTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtTestTypeVo> queryBrtTestTypeList(Page<?> page, @Param("brtTestTypeVo") BrtTestTypeVo brtTestTypeVo);

    /**
     * @description: TODO 查询测试类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtTestTypeVo> queryBrtTestTypeList(@Param("brtTestTypeVo") BrtTestTypeVo brtTestTypeVo);

    /**
     * @description: TODO 根据typeId查询测试类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtTestTypeVo queryBrtTestTypeByTypeId(@Param("TypeId") String typeId);

}
