package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtMarketOrderDetails;
import com.brt.order.vo.BrtMarketOrderDetailsVo;
import com.brt.order.vo.BrtSalesOrderDetailsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购单详情Mapper接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtMarketOrderDetailsMapper extends BaseMapper<BrtMarketOrderDetails> {

    /**
     * @description: TODO 分页查询采购单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtMarketOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtMarketOrderDetailsVo> queryBrtMarketOrderDetailsList(Page<?> page, @Param("brtMarketOrderDetailsVo") BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

    /**
     * @description: TODO 查询采购单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtMarketOrderDetailsVo> queryBrtMarketOrderDetailsList(@Param("brtMarketOrderDetailsVo") BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

    /**
     * @description: TODO 根据detailsId查询采购单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtMarketOrderDetailsVo queryBrtMarketOrderDetailsByDetailsId(@Param("DetailsId") String detailsId);

    /**
     * @description: TODO 查询采购报表
     * @author: FanGN
     * @date: 15:21 2024/5/18
     * @param:
     * @param buildPage
     * @param brtMarketOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.brt.order.vo.BrtMarketOrderDetailsVo>
     **/
    Page<BrtMarketOrderDetailsVo> statementList(Page<?> buildPage,@Param("brtMarketOrderDetailsVo") BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

    /**
     * @description: TODO 根据订单ID查询销售单商品信息
     * @author: FanGN
     * @date: 15:21 2024/5/18
     * @param:
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.brt.order.vo.BrtSalesOrderDetailsVo>
     **/
    List<BrtMarketOrderDetailsVo>getBrtMarketOrderDetailsVoListByOrderId(String orderId);
}
