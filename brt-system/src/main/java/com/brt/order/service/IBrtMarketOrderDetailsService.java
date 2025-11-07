package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtMarketOrderDetails;
import com.brt.order.vo.BrtMarketOrderDetailsVo;

import java.util.List;

/**
 * 销售单详情Service接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtMarketOrderDetailsService extends IService<BrtMarketOrderDetails> {

    /**
     * @description: TODO 分页查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderDetailsVo 销售单详情
     * @return:
     * @return TableDataInfo<BrtMarketOrderDetailsVo>
     **/
    public TableDataInfo<BrtMarketOrderDetailsVo> queryBrtMarketOrderDetailsList(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

    /**
     * @description: TODO 查询全部销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderDetailsVo 销售单详情
     * @return:
     * @return java.util.List<BrtMarketOrderDetailsVo>
     **/
    public List<BrtMarketOrderDetailsVo> queryBrtMarketOrderDetailsAll(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

    /**
     * @description: TODO 根据detailsId查询销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: detailsId
     * @return:
     * @return BrtMarketOrderDetailsVo
     **/
    public BrtMarketOrderDetailsVo queryBrtMarketOrderDetailsByDetailsId(String detailsId);

    /**
     * @description: TODO 新增销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderDetailsVo 销售单详情
     * @return:
     * @return int
     **/
    public BrtMarketOrderDetailsVo insertBrtMarketOrderDetails(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

    /**
     * @description: TODO 修改销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderDetailsVo 销售单详情
     * @return:
     * @return int
     **/
    public BrtMarketOrderDetailsVo updateBrtMarketOrderDetails(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

    /**
     * @description: TODO 批量删除销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  detailsIds 需要删除的销售单详情主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtMarketOrderDetailsByDetailsIds(String[] detailsIds);

    /**
     * @description: TODO 查询报表
     * @author: FanGN
     * @date: 15:20 2024/5/18
     * @param:
     * @param brtMarketOrderDetailsVo
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<com.brt.order.vo.BrtMarketOrderDetailsVo>
     **/
    TableDataInfo<BrtMarketOrderDetailsVo> statementList(BrtMarketOrderDetailsVo brtMarketOrderDetailsVo);

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
    List<BrtMarketOrderDetailsVo> importData(List<BrtMarketOrderDetailsVo> dataList);

    /**
     * 根据订单ID查询销售单详情
     * @param
     * @return
     */
    List<BrtMarketOrderDetailsVo> getBrtMarketOrderDetailsVoListByOrderId(String orderId);
}
