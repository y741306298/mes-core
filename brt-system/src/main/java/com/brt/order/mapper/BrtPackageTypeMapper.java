package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtPackageType;
import com.brt.order.vo.BrtPackageTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 封装类型管理Mapper接口
 * 
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtPackageTypeMapper extends BaseMapper<BrtPackageType> {

    /**
     * @description: TODO 分页查询封装类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtPackageTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtPackageTypeVo> queryBrtPackageTypeList(Page<?> page, @Param("brtPackageTypeVo") BrtPackageTypeVo brtPackageTypeVo);

    /**
     * @description: TODO 查询封装类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtPackageTypeVo> queryBrtPackageTypeList(@Param("brtPackageTypeVo") BrtPackageTypeVo brtPackageTypeVo);

    /**
     * @description: TODO 根据typeId查询封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtPackageTypeVo queryBrtPackageTypeByTypeId(@Param("TypeId") String typeId);

}
