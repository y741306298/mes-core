package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtCustomerAddress;
import com.brt.order.vo.BrtCustomerAddressVo;

import java.util.List;

/**
 * 用户地址Service接口
 * 
 * @author Fgn
 * @date 2024-04-27
 */
public interface IBrtCustomerAddressService extends IService<BrtCustomerAddress> {

    /**
     * @description: TODO 分页查询用户地址列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return TableDataInfo<BrtCustomerAddressVo>
     **/
    public TableDataInfo<BrtCustomerAddressVo> queryBrtCustomerAddressList(BrtCustomerAddressVo brtCustomerAddressVo);

    /**
     * @description: TODO 查询全部用户地址列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return java.util.List<BrtCustomerAddressVo>
     **/
    public List<BrtCustomerAddressVo> queryBrtCustomerAddressAll(BrtCustomerAddressVo brtCustomerAddressVo);

    /**
     * @description: TODO 根据addressId查询用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: addressId
     * @return:
     * @return BrtCustomerAddressVo
     **/
    public BrtCustomerAddressVo queryBrtCustomerAddressByAddressId(String addressId);

    /**
     * @description: TODO 新增用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return int
     **/
    public BrtCustomerAddressVo insertBrtCustomerAddress(BrtCustomerAddressVo brtCustomerAddressVo);

    /**
     * @description: TODO 修改用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return int
     **/
    public BrtCustomerAddressVo updateBrtCustomerAddress(BrtCustomerAddressVo brtCustomerAddressVo);

    /**
     * @description: TODO 批量删除用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param:  addressIds 需要删除的用户地址主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtCustomerAddressByAddressIds(String[] addressIds);

}
