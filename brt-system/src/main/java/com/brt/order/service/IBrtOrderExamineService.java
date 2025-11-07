package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderTypeEnums;
import com.brt.order.domain.BrtOrderExamine;
import com.brt.order.domain.BrtOrderNode;
import com.brt.order.vo.BrtOrderExamineVo;
import com.brt.order.vo.BrtSalesOrderVo;
import com.brt.order.vo.pub.BrtOrderVo;

import java.util.List;
import java.util.Map;

/**
 * 订单审批Service接口
 *
 * @author Fgn
 * @date 2024-05-10
 */
public interface IBrtOrderExamineService extends IService<BrtOrderExamine> {

    /**
     * @description: TODO 分页查询订单审批列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return TableDataInfo<BrtOrderExamineVo>
     **/
    public TableDataInfo<BrtOrderExamineVo> queryBrtOrderExamineList(BrtOrderExamineVo brtOrderExamineVo);

    /**
     * @description: TODO 查询全部订单审批列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return java.util.List<BrtOrderExamineVo>
     **/
    public List<BrtOrderExamineVo> queryBrtOrderExamineAll(BrtOrderExamineVo brtOrderExamineVo);

    /**
     * @description: TODO 根据examineId查询订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: examineId
     * @return:
     * @return BrtOrderExamineVo
     **/
    public BrtOrderExamineVo queryBrtOrderExamineByExamineId(String examineId);

    /**
     * @description: TODO 新增订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return int
     **/
    public BrtOrderExamineVo insertBrtOrderExamine(BrtOrderExamineVo brtOrderExamineVo);

    /**
     * @description: TODO 修改订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo 订单审批
     * @return:
     * @return int
     **/
    public BrtOrderExamineVo updateBrtOrderExamine(BrtOrderExamineVo brtOrderExamineVo);

    /**
     * @description: TODO 批量删除订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param:  examineIds 需要删除的订单审批主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderExamineByExamineIds(String[] examineIds);

    /**
     * @description: TODO 生成订单审批信息
     * @author: FanGN
     * @date: 17:42 2024/5/10
     * @param:
     * @param brtSalesOrderVo
     * @return:
     **/
    void createOrderExamine(BrtOrderVo orderVo, OrderTypeEnums orderTypeEnums, String childId);

    /**
     * @description: TODO 审核
     * @author: FanGN
     * @date: 18:12 2024/5/10
     * @param:
     * @param auditStatus
     * @param auditRemark
     * @return:
     * @return int
     **/
    int audit(String examineIds,String auditStatus,String childId,String auditRemark);

    /**
     * @description: TODO 获取全部订单列表
     * @author: FanGN
     * @date: 00:52 2024/5/20
     * @param:
     * @return:
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    List<Map<String,String>> orderAllList();

    /**
     * @description: TODO 节点审批
     * @author: FanGN
     * @date: 18:55 2024/5/20
     * @param:
     * @param orderNode
     * @param auditStatus
     * @return:
     * @return int
     **/
    int nodeAudit(BrtOrderNode orderNode,String childId , String auditStatus);
}
