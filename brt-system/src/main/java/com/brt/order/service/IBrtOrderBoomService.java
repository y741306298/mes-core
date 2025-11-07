package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.vo.BrtOrderBoomVo;
import com.brt.order.vo.BrtSalesOrderDetailsVo;

import java.util.List;

/**
 * boom单Service接口
 *
 * @author Fgn
 * @date 2024-06-20
 */
public interface IBrtOrderBoomService extends IService<BrtOrderBoom> {

    /**
     * @description: TODO 分页查询boom单列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return TableDataInfo<BrtOrderBoomVo>
     **/
    public TableDataInfo<BrtOrderBoomVo> queryBrtOrderBoomList(BrtOrderBoomVo brtOrderBoomVo);

    /**
     * @description: TODO 查询全部boom单列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return java.util.List<BrtOrderBoomVo>
     **/
    public List<BrtOrderBoomVo> queryBrtOrderBoomAll(BrtOrderBoomVo brtOrderBoomVo);

    /**
     * @description: TODO 根据boomId查询boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: boomId
     * @return:
     * @return BrtOrderBoomVo
     **/
    public BrtOrderBoomVo queryBrtOrderBoomByBoomId(String boomId);

    /**
     * @description: TODO 新增boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return int
     **/
    public BrtOrderBoomVo insertBrtOrderBoom(BrtOrderBoomVo brtOrderBoomVo);

    /**
     * @description: TODO 修改boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return int
     **/
    public BrtOrderBoomVo updateBrtOrderBoom(BrtOrderBoomVo brtOrderBoomVo);

    /**
     * @description: TODO 批量删除boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param:  boomIds 需要删除的boom单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOrderBoomByBoomIds(String[] boomIds);

    /**
     * @description: TODO 获取boom单文件数据列表
     * @author: FanGN
     * @date: 16:03 2024/6/20
     * @param:
     * @param fileUrl
     * @return:
     * @return java.util.List<com.brt.order.vo.BrtOrderBoomVo>
     **/
    List<BrtOrderBoomVo> getFileData(String fileUrl) throws Exception;

    /**
     * @description: TODO 保存boom单列表
     * @author: FanGN
     * @date: 18:11 2024/6/20
     * @param:
     * @param salesOrderDetailsVo
     * @param materielList
     * @return:
     * @return int
     **/
    int saveBoomList(BrtSalesOrderDetailsVo salesOrderDetailsVo);

    /**
     * @description: TODO 根据订单ID删除boom单
     * @author: FanGN
     * @date: 18:29 2024/6/20
     * @param:
     * @param orderIds
     * @return:
     * @return int
     **/
    int removeByOrderIds(String[] orderIds);
}
