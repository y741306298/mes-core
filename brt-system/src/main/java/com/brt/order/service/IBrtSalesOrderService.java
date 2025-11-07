package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderAuditStatus;
import com.brt.order.domain.BrtSalesOrder;
import com.brt.order.vo.BrtCheckVo;
import com.brt.order.vo.BrtSalesOrderVo;

import java.util.List;
import java.util.Map;

/**
 * 销售单Service接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtSalesOrderService extends IService<BrtSalesOrder> {

    /**
     * @description: TODO 分页查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return TableDataInfo<BrtSalesOrderVo>
     **/
    public TableDataInfo<BrtSalesOrderVo> queryBrtSalesOrderList(BrtSalesOrderVo brtSalesOrderVo);

    /**
     * @description: TODO 查询全部销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return java.util.List<BrtSalesOrderVo>
     **/
    public List<BrtSalesOrderVo> queryBrtSalesOrderAll(BrtSalesOrderVo brtSalesOrderVo);

    /**
     * @description: TODO 根据orderId查询销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: orderId
     * @return:
     * @return BrtSalesOrderVo
     **/
    public BrtSalesOrderVo queryBrtSalesOrderByOrderId(String orderId);

    /**
     * @description: TODO 新增销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return int
     **/
    public BrtSalesOrderVo insertBrtSalesOrder(BrtSalesOrderVo brtSalesOrderVo);

    /**
     * @description: TODO 修改销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderVo 销售单
     * @return:
     * @return int
     **/
    public BrtSalesOrderVo updateBrtSalesOrder(BrtSalesOrderVo brtSalesOrderVo);

    /**
     * @description: TODO 批量删除销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  orderIds 需要删除的销售单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtSalesOrderByOrderIds(String[] orderIds);

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
    Map<String,Object> totalSalesOrder(String customerId);

    /**
     * @description: TODO 复制订单
     * @author: FanGN
     * @date: 16:56 2024/5/20
     * @param:
     * @param orderId
     * @return:
     * @return int
     **/
    int copySalesOrder(String orderId);

    /**
     * 修改状态
     * @param brtSalesOrderVo
     */
    void updateStatus(BrtSalesOrderVo brtSalesOrderVo);

    /**
     * 归档
     */
    public void beNotInUse(String orderId,String childId);

    /**
     * 根据流程ID查询订单
     * @param orderTemplateId
     * @return
     */
    public BrtSalesOrderVo getOrderByOrderTemplateId(String orderTemplateId);
}
