package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtCheckType;
import com.brt.order.vo.BrtCheckTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账单类型Mapper接口
 * 
 * @author Fgn
 * @date 2024-05-15
 */
public interface BrtCheckTypeMapper extends BaseMapper<BrtCheckType> {

    /**
     * @description: TODO 分页查询账单类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: page
     * @param: brtCheckTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtCheckTypeVo> queryBrtCheckTypeList(Page<?> page, @Param("brtCheckTypeVo") BrtCheckTypeVo brtCheckTypeVo);

    /**
     * @description: TODO 查询账单类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtCheckTypeVo> queryBrtCheckTypeList(@Param("brtCheckTypeVo") BrtCheckTypeVo brtCheckTypeVo);

    /**
     * @description: TODO 根据typeId查询账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @return:
     * @return Vo
     **/
    BrtCheckTypeVo queryBrtCheckTypeByTypeId(@Param("TypeId") String typeId);

}
