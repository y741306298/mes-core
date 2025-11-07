package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.vo.BrtOrderBoomVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * boom单Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface BrtOrderBoomMapper extends BaseMapper<BrtOrderBoom> {

    /**
     * @description: TODO 分页查询boom单列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: page
     * @param: brtOrderBoomVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderBoomVo> queryBrtOrderBoomList(Page<?> page, @Param("brtOrderBoomVo") BrtOrderBoomVo brtOrderBoomVo);

    /**
     * @description: TODO 查询boom单列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderBoomVo> queryBrtOrderBoomList(@Param("brtOrderBoomVo") BrtOrderBoomVo brtOrderBoomVo);

    /**
     * @description: TODO 根据boomId查询boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderBoomVo queryBrtOrderBoomByBoomId(@Param("BoomId") String boomId);

    /**
     * @description: TODO 获取订单详情的boom单ID
     * @author: FanGN
     * @date: 14:49 2024/6/24
     * @param:
     * @param orderDetailsId
     * @return:
     * @return java.util.List<com.brt.order.vo.BrtOrderBoomVo>
     **/
    List<BrtOrderBoomVo> queryBrtOrderBoomByOrderDetailsId(@Param("orderDetailsId") String orderDetailsId);

}
