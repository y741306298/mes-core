package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtSupplierReceiving;
import com.brt.order.domain.BrtOrderNode;
import com.brt.order.vo.BrtReceivingRemindVo;
import com.brt.order.vo.BrtSupplierReceivingVo;
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
public interface IBrtSupplierReceivingService extends IService<BrtSupplierReceiving> {

    /**
     * @description: TODO 分页查询客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return TableDataInfo<BrtSupplierReceivingVo>
     **/
    public TableDataInfo<BrtSupplierReceivingVo> queryBrtSupplierReceivingList(BrtSupplierReceivingVo brtSupplierReceivingVo);

    /**
     * @description: TODO 查询全部客户送货单列表
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return java.util.List<BrtSupplierReceivingVo>
     **/
    public List<BrtSupplierReceivingVo> queryBrtSupplierReceivingAll(BrtSupplierReceivingVo brtSupplierReceivingVo);

    /**
     * @description: TODO 根据receivingId查询客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: receivingId
     * @return:
     * @return BrtSupplierReceivingVo
     **/
    public BrtSupplierReceivingVo queryBrtSupplierReceivingByReceivingId(String receivingId);

    /**
     * @description: TODO 新增客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return int
     **/
    public BrtSupplierReceivingVo insertBrtSupplierReceiving(BrtSupplierReceivingVo brtSupplierReceivingVo);

    /**
     * @description: TODO 修改客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param: brtSupplierReceivingVo 客户送货单
     * @return:
     * @return int
     **/
    public BrtSupplierReceivingVo updateBrtSupplierReceiving(BrtSupplierReceivingVo brtSupplierReceivingVo);

    /**
     * @description: TODO 批量删除客户送货单
     * @author: Fgn
     * @date: 2024-06-28
     * @param:
     * @param:  receivingIds 需要删除的客户送货单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtSupplierReceivingByReceivingIds(String[] receivingIds);

    /**
     * @description: TODO 生成客户送货单
     * @author: FanGN
     * @date: 15:32 2024/6/28
     * @param:
     * @param orderNode
     * @return:
     * @return int
     **/
    public int createSupplierReceiving(BrtOrderNode orderNode);

    /**
     * @description: TODO 送货提醒表查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    Map<String,Object> receivingRemind(BrtReceivingRemindVo receivingRemindVo);

    /**
     * @description: TODO 客户送货查询
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    Map<String,Object> receivingByOrder(BrtReceivingRemindVo receivingRemindVo);

    /**
     * @description: TODO 客户送货提交
     * @author: lf
     * @date: 2024-07-09
     * @param:
     * @return:
     * @return Vo
     **/
    AjaxResult receivingSubmit(List<BrtReceivingRemindVo> receivingRemindVoList );

    Map<String,Object> getPrint(String orderId);
}
