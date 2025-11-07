package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderNode;
import com.brt.order.vo.BrtOrderNodeVo;
import com.brt.order.vo.BrtSalesOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单流程节点Mapper接口
 *
 * @author Fgn
 * @date 2024-05-10
 */
public interface BrtOrderNodeMapper extends BaseMapper<BrtOrderNode> {

    /**
     * @description: TODO 分页查询订单流程节点列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: page
     * @param: brtOrderNodeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderNodeVo> queryBrtOrderNodeList(Page<?> page, @Param("brtOrderNodeVo") BrtOrderNodeVo brtOrderNodeVo);

    /**
     * @description: TODO 查询我的任务
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: page
     * @param: brtOrderNodeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderNodeVo> myTask(Page<?> page, @Param("brtOrderNodeVo") BrtOrderNodeVo brtOrderNodeVo);


    /**
     * @description: TODO 查询订单流程节点列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderNodeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderNodeVo> queryBrtOrderNodeList(@Param("brtOrderNodeVo") BrtOrderNodeVo brtOrderNodeVo);

    /**
     * @description: TODO 根据orderNodeId查询订单流程节点
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderNodeVo queryBrtOrderNodeByOrderNodeId(@Param("OrderNodeId") String orderNodeId);

    /**
     * @description: TODO 根据订单模板ID查询订单列表
     * @author: FanGN
     * @date: 01:13 2024/5/11
     * @param:
     * @param orderTemplateId
     * @return:
     * @return java.util.List<com.brt.order.vo.BrtOrderNodeVo>
     **/
    List<BrtOrderNodeVo> queryBrtOrderNodeListByOrderTemplateId(@Param("orderTemplateId") String orderTemplateId);

    /**
     * @description: TODO 查询订单当前的节点
     * @author: FanGN
     * @date: 17:41 2024/6/19
     * @param:
     * @param orderId
     * @return:
     * @return com.brt.order.vo.BrtOrderNodeVo
     **/
    BrtOrderNodeVo selectNowOrderNodeByOrderId(@Param("orderId") String orderId);

    /**
     * 查询流程是否所有节点都已完成
     * @param orderId
     * @return
     */
    String isComplete(@Param("orderId") String orderId,@Param("childId") String childId,@Param("orderTemplateId") String orderTemplateId);

    /**
     * 审批节点拒绝
     * @param orderId
     * @param auditStatus
     */
    String onAudit(@Param("orderId") String orderId);
}
