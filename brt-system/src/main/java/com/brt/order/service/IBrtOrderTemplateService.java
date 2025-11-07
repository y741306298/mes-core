package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderTypeEnums;
import com.brt.order.domain.BrtOrderTemplate;
import com.brt.order.vo.BrtOrderTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单模板Service接口
 *
 * @author Fgn
 * @date 2024-05-10
 */
public interface IBrtOrderTemplateService extends IService<BrtOrderTemplate> {

    /**
     * @description: TODO 分页查询订单模板列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return TableDataInfo<BrtOrderTemplateVo>
     **/
    public TableDataInfo<BrtOrderTemplateVo> queryBrtOrderTemplateList(BrtOrderTemplateVo brtOrderTemplateVo);

    /**
     * @description: TODO 查询全部订单模板列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return java.util.List<BrtOrderTemplateVo>
     **/
    public List<BrtOrderTemplateVo> queryBrtOrderTemplateAll(BrtOrderTemplateVo brtOrderTemplateVo);

    /**
     * @description: TODO 根据orderTemplateId查询订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: orderTemplateId
     * @return:
     * @return BrtOrderTemplateVo
     **/
    public BrtOrderTemplateVo queryBrtOrderTemplateByOrderTemplateId(String orderTemplateId);

    /**
     * @description: TODO 派工查询
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: orderTemplateId
     * @return:
     * @return BrtOrderTemplateVo
     **/
    public BrtOrderTemplateVo sendWorkSelect(String orderTemplateId);

    /**
     * @description: TODO 新增订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return int
     **/
    public BrtOrderTemplateVo insertBrtOrderTemplate(BrtOrderTemplateVo brtOrderTemplateVo);

    /**
     * @description: TODO 修改订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderTemplateVo 订单模板
     * @return:
     * @return int
     **/
    public BrtOrderTemplateVo updateBrtOrderTemplate(BrtOrderTemplateVo brtOrderTemplateVo);

    /**
     * @description: TODO 批量删除订单模板
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param:  orderTemplateIds 需要删除的订单模板主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderTemplateByOrderTemplateIds(String[] orderTemplateIds);

    /**
     * @description: TODO 派工
     * @author: FanGN
     * @date: 10:27 2024/5/11
     * @param:
     * @param brtOrderTemplateVo
     * @return:
     * @return int
     **/
    int sendWork(BrtOrderTemplateVo brtOrderTemplateVo);

    /**
     * @description: TODO 复制模板
     * @author: FanGN
     * @date: 10:32 2024/5/11
     * @param:
     * @param orderTemplateId
     * @return:
     * @return java.lang.String
     **/
    int copy(String orderTemplateId);

    /**
     * 获取订单类型
     * @param orderNodeId
     * @return
     */
    String getOrderType(String orderNodeId);

    /**
     * 查询订单ID
     * @param orderType
     * @param orderTemplateId
     * @return
     */
    String getOrderId(String orderType,String orderTemplateId);

    /**
     * 订单归档
     * @param orderType
     * @param orderId
     * @return
     */
    void beNotInUseSubmit(String orderType, String orderId,String childId);

    /**
     * 获取订单数据
     * @param orderType
     * @param orderId
     * @return
     */
    AjaxResult getOrderForm(String orderType,String orderId);

    /**
     * 查询订单流程和节点
     * @param orderId
     * @param templateId
     * @param childId
     * @param isFilterVoid
     * @return
     */
    Map<String,Object> getOrderTempleatAndOrderNode(String orderId, String templateId, String childId, String isFilterVoid);
}
