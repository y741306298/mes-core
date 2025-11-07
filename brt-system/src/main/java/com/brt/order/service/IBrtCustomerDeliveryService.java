package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtCustomerDelivery;
import com.brt.order.domain.BrtOrderNode;
import com.brt.order.vo.BrtCustomerDeliveryVo;
import com.brt.order.vo.BrtDeliveryRemindVo;
import com.brt.order.vo.BrtOrderNodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 客户送货单Service接口
 *
 * @author Fgn
 * @date 2024-06-28
 */
public interface IBrtCustomerDeliveryService extends IService<BrtCustomerDelivery> {

    /**
     * @description: TODO 分页查询客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return TableDataInfo<BrtCustomerDeliveryVo>
     **/
    public TableDataInfo<BrtCustomerDeliveryVo> queryBrtCustomerDeliveryList(BrtCustomerDeliveryVo brtCustomerDeliveryVo);

    /**
     * @description: TODO 查询全部客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return java.util.List<BrtCustomerDeliveryVo>
     **/
    public List<BrtCustomerDeliveryVo> queryBrtCustomerDeliveryAll(BrtCustomerDeliveryVo brtCustomerDeliveryVo);

    /**
     * @description: TODO 根据deliveryId查询客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: deliveryId
     * @return:
     * @return BrtCustomerDeliveryVo
     **/
    public BrtCustomerDeliveryVo queryBrtCustomerDeliveryByDeliveryId(String deliveryId);

    /**
     * @description: TODO 新增客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return int
     **/
    public BrtCustomerDeliveryVo insertBrtCustomerDelivery(BrtCustomerDeliveryVo brtCustomerDeliveryVo);

    /**
     * @description: TODO 修改客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo 客户送货单
     * @return:
     * @return int
     **/
    public BrtCustomerDeliveryVo updateBrtCustomerDelivery(BrtCustomerDeliveryVo brtCustomerDeliveryVo);

    /**
     * @description: TODO 批量删除客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param:  deliveryIds 需要删除的客户送货单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtCustomerDeliveryByDeliveryIds(String[] deliveryIds);

    /**
     * @description: TODO 生成客户送货单
     * @author: FanGN
     * @date: 15:32 2024/6/28
     * @param:
     * @param orderNode
     * @return:
     * @return int
     **/
    public int createCustomerDelivery(BrtOrderNode orderNode);

    /**
     * @description: TODO 送货提醒表查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    Map<String,Object> deliveryRemind(BrtDeliveryRemindVo deliveryRemindVo);

    /**
     * @description: TODO 客户送货查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    Map<String,Object> deliveryByOrder(BrtDeliveryRemindVo deliveryRemindVo);

    /**
     * @description: TODO 客户送货提交
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    AjaxResult deliverySubmit(List<BrtDeliveryRemindVo> deliveryRemindVoList );

    /**
     * 查询打印数据
     * @param orderId
     * @return
     */
    Map<String,Object> getPrint(@Param("orderId")String orderId);
}
