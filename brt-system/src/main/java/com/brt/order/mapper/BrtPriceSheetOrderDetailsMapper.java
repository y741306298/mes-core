package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtPriceSheetOrderDetails;
import com.brt.order.vo.BrtPriceSheetOrderDetailsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售单详情Mapper接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtPriceSheetOrderDetailsMapper extends BaseMapper<BrtPriceSheetOrderDetails> {

    /**
     * @description: TODO 分页查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtPriceSheetOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtPriceSheetOrderDetailsVo> queryBrtPriceSheetOrderDetailsList(Page<?> page, @Param("brtPriceSheetOrderDetailsVo") BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @description: TODO 查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtPriceSheetOrderDetailsVo> queryBrtPriceSheetOrderDetailsList(@Param("brtPriceSheetOrderDetailsVo") BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @description: TODO 根据detailsId查询销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtPriceSheetOrderDetailsVo queryBrtPriceSheetOrderDetailsByDetailsId(@Param("DetailsId") String detailsId);

    /**
     * @description: TODO 查询销售报表
     * @author: FanGN
     * @date: 15:21 2024/5/18
     * @param:
     * @param buildPage
     * @param brtPriceSheetOrderDetailsVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.brt.order.vo.BrtPriceSheetOrderDetailsVo>
     **/
    Page<BrtPriceSheetOrderDetailsVo> statementList(Page<?> buildPage,@Param("brtPriceSheetOrderDetailsVo") BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @param OrderId
     * @return
     */
    List<BrtPriceSheetOrderDetails> listAllByOrderId(@Param("orderId")String orderId);

    /**
     * 根据订单ID返回vo
     * @param orderId
     * @return
     */
    List<BrtPriceSheetOrderDetailsVo> queryByOrderId(@Param("orderId")String orderId);
}
