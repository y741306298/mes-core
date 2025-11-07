package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderTemplate;
import com.brt.order.vo.BrtOrderTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单模板Mapper接口
 *
 * @author Fgn
 * @date 2024-05-10
 */
public interface BrtOrderTemplateMapper extends BaseMapper<BrtOrderTemplate> {

    /**
     * @description: TODO 分页查询订单模板列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: page
     * @param: brtOrderTemplateVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderTemplateVo> queryBrtOrderTemplateList(Page<?> page, @Param("brtOrderTemplateVo") BrtOrderTemplateVo brtOrderTemplateVo);

    /**
     * @description: TODO 查询订单模板列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderTemplateVo> queryBrtOrderTemplateList(@Param("brtOrderTemplateVo") BrtOrderTemplateVo brtOrderTemplateVo);

    /**
     * @description: TODO 根据orderTemplateId查询订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderTemplateVo queryBrtOrderTemplateByOrderTemplateId(@Param("OrderTemplateId") String orderTemplateId);

    /**
     * 获取订单类型
     * @param orderNodeId
     * @return
     */
    String getOrderType(@Param("orderNodeId")String orderNodeId);

    /**
     * 查询订单ID
     * @param orderType
     * @param orderTemplateId
     * @return
     */
    String getOrderId(@Param("orderType")String orderType,@Param("orderTemplateId")String orderTemplateId);

}
