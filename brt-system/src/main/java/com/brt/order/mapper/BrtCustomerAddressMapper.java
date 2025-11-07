package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtCustomerAddress;
import com.brt.order.vo.BrtCustomerAddressVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户地址Mapper接口
 * 
 * @author Fgn
 * @date 2024-04-27
 */
public interface BrtCustomerAddressMapper extends BaseMapper<BrtCustomerAddress> {

    /**
     * @description: TODO 分页查询用户地址列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: page
     * @param: brtCustomerAddressVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtCustomerAddressVo> queryBrtCustomerAddressList(Page<?> page, @Param("brtCustomerAddressVo") BrtCustomerAddressVo brtCustomerAddressVo);

    /**
     * @description: TODO 查询用户地址列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtCustomerAddressVo> queryBrtCustomerAddressList(@Param("brtCustomerAddressVo") BrtCustomerAddressVo brtCustomerAddressVo);

    /**
     * @description: TODO 根据addressId查询用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return Vo
     **/
    BrtCustomerAddressVo queryBrtCustomerAddressByAddressId(@Param("AddressId") String addressId);

}
