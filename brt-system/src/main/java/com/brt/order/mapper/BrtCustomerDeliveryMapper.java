package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtCustomerDelivery;
import com.brt.order.vo.BrtCustomerDeliveryVo;
import com.brt.order.vo.BrtDeliveryRemindVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 客户送货单Mapper接口
 *
 * @author Fgn
 * @date 2024-06-28
 */
public interface BrtCustomerDeliveryMapper extends BaseMapper<BrtCustomerDelivery> {

    /**
     * @description: TODO 分页查询客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: page
     * @param: brtCustomerDeliveryVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtCustomerDeliveryVo> queryBrtCustomerDeliveryList(Page<?> page, @Param("brtCustomerDeliveryVo") BrtCustomerDeliveryVo brtCustomerDeliveryVo);

    /**
     * @description: TODO 查询客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtCustomerDeliveryVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtCustomerDeliveryVo> queryBrtCustomerDeliveryList(@Param("brtCustomerDeliveryVo") BrtCustomerDeliveryVo brtCustomerDeliveryVo);

    /**
     * @description: TODO 根据deliveryId查询客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @return:
     * @return Vo
     **/
    BrtCustomerDeliveryVo queryBrtCustomerDeliveryByDeliveryId(@Param("DeliveryId") String deliveryId);

    /**
     * @description: TODO 送货提醒表查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    List<BrtDeliveryRemindVo>deliveryRemind(@Param("deliveryRemindVo")BrtDeliveryRemindVo deliveryRemindVo);

    /**
     * @description: TODO 客户送货查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    List<BrtCustomerDeliveryVo> deliveryByOrder(@Param("orderId")String orderId);

    /**
     * @description: TODO 查询客户地址
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    Map<String,Object> getCustomerData(@Param("orderId")String orderId);

    /**
     * @description: TODO 修改已操作数量
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    void updateMaterielNum(@Param("deliveryRemindVo")BrtDeliveryRemindVo deliveryRemindVo);

    /**
     * @description: TODO 查询已操作数量
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    String queryMaterielNum(@Param("planId")String planId);

    /**
     * 查询订单的所有未送货数量
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getDeliveryNum(@Param("orderId")String orderId);

    /**
     * 查询打印数据
     * @param orderId
     * @return
     */
    Map<String,Object> getPrint(@Param("orderId")String orderId);
    /**
     * 查询打印数据
     * @param orderId
     * @return
     */
    List<Map<String,Object>> getPrintTable(@Param("orderId")String orderId);

}
