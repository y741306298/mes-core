package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtPriceSheetOrder;
import com.brt.order.vo.BrtCheckVo;
import com.brt.order.vo.BrtPriceSheetOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 销售单Service接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtPriceSheetOrderService extends IService<BrtPriceSheetOrder> {

    /**
     * @description: TODO 分页查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return TableDataInfo<BrtPriceSheetOrderVo>
     **/
    public TableDataInfo<BrtPriceSheetOrderVo> queryBrtPriceSheetOrderList(BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * @description: TODO 查询全部销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return java.util.List<BrtPriceSheetOrderVo>
     **/
    public List<BrtPriceSheetOrderVo> queryBrtPriceSheetOrderAll(BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * @description: TODO 根据orderId查询销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: orderId
     * @return:
     * @return BrtPriceSheetOrderVo
     **/
    public BrtPriceSheetOrderVo queryBrtPriceSheetOrderByOrderId(String orderId);

    /**
     * @description: TODO 新增销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderVo insertBrtPriceSheetOrder(BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * @description: TODO 修改销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPriceSheetOrderVo 销售单
     * @return:
     * @return int
     **/
    public BrtPriceSheetOrderVo updateBrtPriceSheetOrder(BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * @description: TODO 批量删除销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  orderIds 需要删除的销售单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtPriceSheetOrderByOrderIds(String[] orderIds);

    /**
     * @description: TODO 收款、退款执行
     * @author: FanGN
     * @date: 21:46 2024/5/19
     * @param:
     * @param checkVo
     * @return:
     * @return int
     **/
    public int orderCheck(BrtCheckVo checkVo);

    /**
     * @description: TODO 统计客户销售单信息
     * @author: FanGN
     * @date: 22:35 2024/5/19
     * @param:
     * @param customerId
     * @return:
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String,Object> totalPriceSheetOrder(String customerId);

    /**
     * @description: TODO 复制订单
     * @author: FanGN
     * @date: 16:56 2024/5/20
     * @param:
     * @param orderId
     * @return:
     * @return int
     **/
    int copyPriceSheetOrder(String orderId);

    /**
     * 报价单去下单
     * @param priceSheetOrderVo
     * @return
     */
    Map<String,Object> priceSheetToSales(BrtPriceSheetOrderVo priceSheetOrderVo);

    /**
     * 修改是否可报价
     * @param orderId
     * @param isPriceSheet
     */
    void updatePriceSheet(String orderId,String isPriceSheet);

    /**
     * 销售单导入报价单 查询报价单列表
     * @param orderNo
     * @return
     */
    List<Map<String,Object>> exportPriceList(@Param("orderNo")String orderNo);

    /**
     * 修改状态
     * @param brtPriceSheetOrderVo
     */
    void updateStatus( BrtPriceSheetOrderVo brtPriceSheetOrderVo);

    /**
     * 根据流程ID查询订单
     * @param orderTemplateId
     * @return
     */
    BrtPriceSheetOrderVo getOrderByOrderTemplateId(String orderTemplateId);
}
