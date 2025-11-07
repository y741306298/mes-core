package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtCustomer;
import com.brt.order.vo.BrtCustomerVo;
import com.brt.order.vo.MarketRecordVo;

import java.util.List;

/**
 * 客户信息Service接口
 *
 * @author Fgn
 * @date 2024-04-27
 */
public interface IBrtCustomerService extends IService<BrtCustomer> {

    /**
     * @description: TODO 分页查询客户信息列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     * @return TableDataInfo<BrtCustomerVo>
     **/
    public TableDataInfo<BrtCustomerVo> queryBrtCustomerList(BrtCustomerVo brtCustomerVo);

    /**
     * @description: TODO 查询全部客户信息列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     * @return java.util.List<BrtCustomerVo>
     **/
    public List<BrtCustomerVo> queryBrtCustomerAll(BrtCustomerVo brtCustomerVo);

    /**
     * @description: TODO 根据customerId查询客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: customerId
     * @return:
     * @return BrtCustomerVo
     **/
    public BrtCustomerVo queryBrtCustomerByCustomerId(String customerId);

    /**
     * @description: TODO 新增客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     * @return int
     **/
    public BrtCustomerVo insertBrtCustomer(BrtCustomerVo brtCustomerVo);

    /**
     * @description: TODO 修改客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     * @return int
     **/
    public BrtCustomerVo updateBrtCustomer(BrtCustomerVo brtCustomerVo);

    /**
     * @description: TODO 批量删除客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param:  customerIds 需要删除的客户信息主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtCustomerByCustomerIds(String[] customerIds);

    /**
     * @description: TODO 导入客户数据
     * @author: FanGN
     * @date: 00:49 2024/4/28
     * @param:
     * @param customerList
     * @param operName
     * @return:
     * @return java.lang.String
     **/
    String importData(List<BrtCustomerVo> customerList, String operName);

    /**
     * 查询客户的销售记录
     * @param customerId
     * @return
     */
    TableDataInfo<MarketRecordVo> queryMarketRecord(MarketRecordVo marketRecordVo);
}
