package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtSalesOrderDetails;
import com.brt.order.vo.BrtSalesOrderDetailsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售单详情Mapper接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtSalesOrderDetailsMapper extends BaseMapper<BrtSalesOrderDetails> {

    /**
     * @description: TODO 分页查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtSalesOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtSalesOrderDetailsVo> queryBrtSalesOrderDetailsList(Page<?> page, @Param("brtSalesOrderDetailsVo") BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtSalesOrderDetailsVo> queryBrtSalesOrderDetailsList(@Param("brtSalesOrderDetailsVo") BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 根据detailsId查询销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtSalesOrderDetailsVo queryBrtSalesOrderDetailsByDetailsId(@Param("DetailsId") String detailsId);

    /**
     * @description: TODO 查询销售报表
     * @author: FanGN
     * @date: 15:21 2024/5/18
     * @param:
     * @param buildPage
     * @param brtSalesOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.brt.order.vo.BrtSalesOrderDetailsVo>
     **/
    Page<BrtSalesOrderDetailsVo> statementList(Page<?> buildPage,@Param("brtSalesOrderDetailsVo") BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 根据订单ID查询销售单商品信息
     * @author: FanGN
     * @date: 15:21 2024/5/18
     * @param:
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.brt.order.vo.BrtSalesOrderDetailsVo>
     **/
    List<BrtSalesOrderDetailsVo>getBrtSalesOrderDetailsVoListByOrderId(String orderId);
}
