package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderDynamic;
import com.brt.order.vo.BrtOrderDynamicVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单动态Mapper接口
 * 
 * @author Fgn
 * @date 2024-05-12
 */
public interface BrtOrderDynamicMapper extends BaseMapper<BrtOrderDynamic> {

    /**
     * @description: TODO 分页查询订单动态列表
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: page
     * @param: brtOrderDynamicVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderDynamicVo> queryBrtOrderDynamicList(Page<?> page, @Param("brtOrderDynamicVo") BrtOrderDynamicVo brtOrderDynamicVo);

    /**
     * @description: TODO 查询订单动态列表
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param: brtOrderDynamicVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderDynamicVo> queryBrtOrderDynamicList(@Param("brtOrderDynamicVo") BrtOrderDynamicVo brtOrderDynamicVo);

    /**
     * @description: TODO 根据dynamicId查询订单动态
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderDynamicVo queryBrtOrderDynamicByDynamicId(@Param("DynamicId") String dynamicId);

}
