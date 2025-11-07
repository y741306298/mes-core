package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtSupplierReceiving;
import com.brt.order.vo.BrtReceivingRemindVo;
import com.brt.order.vo.BrtSupplierReceivingVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 供应商收货单Mapper接口
 *
 * @author Fgn
 * @date 2024-06-28
 */
public interface BrtSupplierReceivingMapper extends BaseMapper<BrtSupplierReceiving> {

    /**
     * @description: TODO 分页查询供应商收货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: page
     * @param: brtSupplierReceivingVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtSupplierReceivingVo> queryBrtSupplierReceivingList(Page<?> page, @Param("brtSupplierReceivingVo") BrtSupplierReceivingVo brtSupplierReceivingVo);

    /**
     * @description: TODO 查询供应商收货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtSupplierReceivingVo> queryBrtSupplierReceivingList(@Param("brtSupplierReceivingVo") BrtSupplierReceivingVo brtSupplierReceivingVo);

    /**
     * @description: TODO 根据receivingId查询供应商收货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @return:
     * @return Vo
     **/
    BrtSupplierReceivingVo queryBrtSupplierReceivingByReceivingId(@Param("ReceivingId") String receivingId);

    /**
     * @description: TODO 收货提醒表查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    List<BrtReceivingRemindVo>receivingRemind(@Param("receivingRemindVo")BrtReceivingRemindVo receivingRemindVo);

    /**
     * @description: TODO 供应商收货查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    List<BrtSupplierReceivingVo> receivingByOrder(@Param("orderId")String orderId);

    /**
     * @description: TODO 查询供应商地址
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    Map<String,Object> getSupplierData(@Param("orderId")String orderId);

    /**
     * @description: TODO 修改已操作数量
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    void updateMaterielNum(@Param("receivingRemindVo")BrtReceivingRemindVo receivingRemindVo);

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
    List<String> getReceivingNum(@Param("orderId")String orderId);

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
