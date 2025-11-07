package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtMarketOrder;
import com.brt.order.vo.BrtCheckVo;
import com.brt.order.vo.BrtMarketOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 销售单Service接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtMarketOrderService extends IService<BrtMarketOrder> {

    /**
     * @description: TODO 分页查询销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 销售单
     * @return:
     * @return TableDataInfo<BrtMarketOrderVo>
     **/
    public TableDataInfo<BrtMarketOrderVo> queryBrtMarketOrderList(BrtMarketOrderVo brtMarketOrderVo);

    /**
     * @description: TODO 查询全部销售单列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 销售单
     * @return:
     * @return java.util.List<BrtMarketOrderVo>
     **/
    public List<BrtMarketOrderVo> queryBrtMarketOrderAll(BrtMarketOrderVo brtMarketOrderVo);

    /**
     * @description: TODO 根据orderId查询销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: orderId
     * @return:
     * @return BrtMarketOrderVo
     **/
    public BrtMarketOrderVo queryBrtMarketOrderByOrderId(String orderId);

    /**
     * @description: TODO 新增销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 销售单
     * @return:
     * @return int
     **/
    public BrtMarketOrderVo insertBrtMarketOrder(BrtMarketOrderVo brtMarketOrderVo);

    /**
     * @description: TODO 修改销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtMarketOrderVo 销售单
     * @return:
     * @return int
     **/
    public BrtMarketOrderVo updateBrtMarketOrder(BrtMarketOrderVo brtMarketOrderVo);

    /**
     * @description: TODO 批量删除销售单
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  orderIds 需要删除的销售单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtMarketOrderByOrderIds(String[] orderIds);

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
     * @param supplierId
     * @return:
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String,Object> totalMarketOrder(String supplierId);

    /**
     * @description: TODO 复制订单
     * @author: FanGN
     * @date: 16:56 2024/5/20
     * @param:
     * @param orderId
     * @return:
     * @return int
     **/
    int copyMarketOrder(String orderId);

    /**
     * 根据流程ID查询采购单
     * @return
     */
    BrtMarketOrderVo getOrderByTemplateId(String orderTemplateId);

    /**
     * 修改状态
     * @param brtMarketOrderVo
     */
    void updateStatus(BrtMarketOrderVo brtMarketOrderVo);

    /**
     * 归档
     */
    public void beNotInUse(String orderId,String childId);
}
