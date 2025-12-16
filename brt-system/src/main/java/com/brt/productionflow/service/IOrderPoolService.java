package com.brt.productionflow.service;

import com.brt.productionflow.vo.OrderPoolQuery;
import com.brt.productionflow.vo.OrderPoolVo;
import com.brt.productionflow.vo.ProductionFlowQuery;
import com.brt.productionflow.vo.ProductionFlowVo;

import java.util.Collection;
import java.util.List;

/**
 * 订单池业务接口
 */
public interface IOrderPoolService {

    /**
     * 查询订单列表
     */
    List<OrderPoolVo> selectOrderPoolList(OrderPoolQuery query);

    /**
     * 查询订单详情
     */
    OrderPoolVo selectOrderPoolById(String orderId);

    /**
     * 新增订单
     */
    OrderPoolVo insertOrderPool(OrderPoolVo orderPoolVo);

    /**
     * 更新订单
     */
    OrderPoolVo updateOrderPool(OrderPoolVo orderPoolVo);

    /**
     * 删除订单
     */
    int deleteOrderPoolByIds(String[] orderIds);

    /**
     * 清理订单关联的流程节点与模板
     */
    int clearOrderProcessesByIds(String[] orderIds);

    /**
     * 查询生产流列表
     */
    List<ProductionFlowVo> selectProductionFlowList(ProductionFlowQuery query);

    /**
     * 查询生产流详情
     */
    ProductionFlowVo selectProductionFlowById(String flowId);

    /**
     * 新增生产流
     */
    ProductionFlowVo insertProductionFlow(ProductionFlowVo productionFlowVo);

    /**
     * 更新生产流
     */
    ProductionFlowVo updateProductionFlow(ProductionFlowVo productionFlowVo);

    /**
     * 删除生产流
     */
    int deleteProductionFlowByIds(String[] flowIds);

    /**
     * 将指定生产流的模板与节点任务同步到给定订单，保留已有记录。
     */
    boolean applyFlowTemplates(String flowId, Collection<String> orderIds);
}
