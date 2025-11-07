package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtPriceSheetOrderDetails;
import com.brt.order.vo.BrtPriceSheetOrderDetailsVo;

import java.util.List;

/**
 * 销售单详情Service接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtPriceSheetOrderDetailsService extends IService<BrtPriceSheetOrderDetails> {

    /**
     * @description: TODO 分页查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return TableDataInfo<BrtPriceSheetOrderDetailsVo>
     **/
    public TableDataInfo<BrtPriceSheetOrderDetailsVo> queryBrtPriceSheetOrderDetailsList(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @description: TODO 查询全部销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return java.util.List<BrtPriceSheetOrderDetailsVo>
     **/
    public List<BrtPriceSheetOrderDetailsVo> queryBrtPriceSheetOrderDetailsAll(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @description: TODO 根据detailsId查询销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: detailsId
     * @return:
     * @return BrtPriceSheetOrderDetailsVo
     **/
    public BrtPriceSheetOrderDetailsVo queryBrtPriceSheetOrderDetailsByDetailsId(String detailsId);

    /**
     * @description: TODO 新增销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderDetailsVo insertBrtPriceSheetOrderDetails(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @description: TODO 修改销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderDetailsVo 销售单详情
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderDetailsVo updateBrtPriceSheetOrderDetails(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @description: TODO 批量删除销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  detailsIds 需要删除的销售单详情主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtPriceSheetOrderDetailsByDetailsIds(String[] detailsIds);

    /**
     * @description: TODO 查询报表
     * @author: FanGN
     * @date: 15:20 2024/5/18
     * @param:
     * @param brtPriceSheetOrderDetailsVo
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<com.brt.order.vo.BrtPriceSheetOrderDetailsVo>
     **/
    TableDataInfo<BrtPriceSheetOrderDetailsVo> statementList(BrtPriceSheetOrderDetailsVo brtPriceSheetOrderDetailsVo);

    /**
     * @description: TODO 根据OrderIds删除订单详情
     * @author: FanGN
     * @date: 18:42 2024/6/20
     * @param:
     * @param orderIds
     * @return:
     * @return int
     **/
    int removeByOrderIds(String[] orderIds);

    /**
     * 识别Excel文件数据
     * @param dataList
     * @return
     */
    List<BrtPriceSheetOrderDetailsVo> importData(List<BrtPriceSheetOrderDetailsVo> dataList);

    /**
     * listAllByOrderId
     * @param OrderId
     * @return
     */
    List<BrtPriceSheetOrderDetails> listAllByOrderId(String OrderId);

    /**
     * 根据订单ID返回vo
     * @param orderId
     * @return
     */
    List<BrtPriceSheetOrderDetailsVo> queryByOrderId(String orderId);
}
