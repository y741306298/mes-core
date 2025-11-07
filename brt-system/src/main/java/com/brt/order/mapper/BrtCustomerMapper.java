package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtCustomer;
import com.brt.order.vo.BrtCustomerVo;
import com.brt.order.vo.MarketRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户信息Mapper接口
 *
 * @author Fgn
 * @date 2024-04-27
 */
public interface BrtCustomerMapper extends BaseMapper<BrtCustomer> {

    /**
     * @description: TODO 分页查询客户信息列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: page
     * @param: brtCustomerVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtCustomerVo> queryBrtCustomerList(Page<?> page, @Param("brtCustomerVo") BrtCustomerVo brtCustomerVo);

    /**
     * @description: TODO 查询客户信息列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtCustomerVo> queryBrtCustomerList(@Param("brtCustomerVo") BrtCustomerVo brtCustomerVo);

    /**
     * @description: TODO 根据customerId查询客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return Vo
     **/
    BrtCustomerVo queryBrtCustomerByCustomerId(@Param("CustomerId") String customerId);

    /**
     * 查询客户的销售记录
     * @param customerId
     * @return
     */
    Page<MarketRecordVo> queryMarketRecord(Page<?> page, @Param("marketRecordVo") MarketRecordVo marketRecordVo);
}
