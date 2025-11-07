package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.domain.BrtSalesOrderDetails;
import com.brt.order.vo.BrtSalesOrderDetailsVo;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * 销售单详情Service接口
 *
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtSalesOrderDetailsService extends IService<BrtSalesOrderDetails> {

    /**
     * @description: TODO 分页查询销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return TableDataInfo<BrtSalesOrderDetailsVo>
     **/
    public TableDataInfo<BrtSalesOrderDetailsVo> queryBrtSalesOrderDetailsList(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 查询全部销售单详情列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return java.util.List<BrtSalesOrderDetailsVo>
     **/
    public List<BrtSalesOrderDetailsVo> queryBrtSalesOrderDetailsAll(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 根据detailsId查询销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: detailsId
     * @return:
     * @return BrtSalesOrderDetailsVo
     **/
    public BrtSalesOrderDetailsVo queryBrtSalesOrderDetailsByDetailsId(String detailsId);

    /**
     * @description: TODO 新增销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return int
     **/
    public BrtSalesOrderDetailsVo insertBrtSalesOrderDetails(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 修改销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return int
     **/
    public BrtSalesOrderDetailsVo updateBrtSalesOrderDetails(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 修改销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtSalesOrderDetailsVo 销售单详情
     * @return:
     * @return int
     **/
    public List<BrtOrderBoom> updateAndLock(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo) throws Exception;

    /**
     * @description: TODO 批量删除销售单详情
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  detailsIds 需要删除的销售单详情主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtSalesOrderDetailsByDetailsIds(String[] detailsIds);

    /**
     * @description: TODO 查询报表
     * @author: FanGN
     * @date: 15:20 2024/5/18
     * @param:
     * @param brtSalesOrderDetailsVo
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<com.brt.order.vo.BrtSalesOrderDetailsVo>
     **/
    TableDataInfo<BrtSalesOrderDetailsVo> statementList(BrtSalesOrderDetailsVo brtSalesOrderDetailsVo);

    /**
     * @description: TODO 根据OrderIds删除订单详情
     * @author: FanGN
     * @date: 18:42 2024/6/20
     * @param:
     * @param orderIds
     * @return:
     * @return int
     **/
    int removeByOrderIds(String[] orderIds);

    /**
     * 识别Excel文件数据
     * @param dataList
     * @return
     */
    List<BrtSalesOrderDetailsVo> importData(List<BrtSalesOrderDetailsVo> dataList);

    /**
     * 根据订单ID查询销售单详情
     * @param
     * @return
     */
    List<BrtSalesOrderDetailsVo> getBrtSalesOrderDetailsVoListByOrderId(String orderId);
}
