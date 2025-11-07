package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtMaterielType;
import com.brt.order.vo.BrtMaterielTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料类型Mapper接口
 *
 * @author Fgn
 * @date 2024-05-07
 */
public interface BrtMaterielTypeMapper extends BaseMapper<BrtMaterielType> {

    /**
     * @description: TODO 分页查询物料类型列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: page
     * @param: brtMaterielTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtMaterielTypeVo> queryBrtMaterielTypeList(Page<?> page, @Param("brtMaterielTypeVo") BrtMaterielTypeVo brtMaterielTypeVo);

    /**
     * @description: TODO 查询物料类型列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtMaterielTypeVo> queryBrtMaterielTypeList(@Param("brtMaterielTypeVo") BrtMaterielTypeVo brtMaterielTypeVo);

    /**
     * @description: TODO 根据typeId查询物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @return:
     * @return Vo
     **/
    BrtMaterielTypeVo queryBrtMaterielTypeByTypeId(@Param("TypeId") String typeId);

    /**
     * 根据名称查询
     * @param typeName
     * @return
     */
    BrtMaterielTypeVo queryByName(@Param("typeName")String typeName);


}
