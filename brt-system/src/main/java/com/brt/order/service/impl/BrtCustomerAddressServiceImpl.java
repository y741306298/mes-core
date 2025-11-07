package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtCustomerAddress;
import com.brt.order.vo.BrtCustomerAddressVo;
import com.brt.order.mapper.BrtCustomerAddressMapper;
import com.brt.order.service.IBrtCustomerAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 用户地址Service业务层处理
 * 
 * @author Fgn
 * @date 2024-04-27
 */
@Service
public class BrtCustomerAddressServiceImpl extends ServiceImpl<BrtCustomerAddressMapper, BrtCustomerAddress> implements IBrtCustomerAddressService {

    @Override
    public TableDataInfo<BrtCustomerAddressVo> queryBrtCustomerAddressList(BrtCustomerAddressVo brtCustomerAddressVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtCustomerAddressList(PageUtils.buildPage(), brtCustomerAddressVo));
    }

    @Override
    public List<BrtCustomerAddressVo> queryBrtCustomerAddressAll(BrtCustomerAddressVo brtCustomerAddressVo) {
        return this.baseMapper.queryBrtCustomerAddressList(brtCustomerAddressVo);
    }

    @Override
    public BrtCustomerAddressVo queryBrtCustomerAddressByAddressId(String addressId) {
        return this.baseMapper.queryBrtCustomerAddressByAddressId(addressId);
    }

    @Transactional
    @Override
    public BrtCustomerAddressVo insertBrtCustomerAddress(BrtCustomerAddressVo brtCustomerAddressVo) {
        saveBefore(brtCustomerAddressVo);
        int i = this.baseMapper.insert(brtCustomerAddressVo);
        return brtCustomerAddressVo;
    }

    @Transactional
    @Override
    public BrtCustomerAddressVo updateBrtCustomerAddress(BrtCustomerAddressVo brtCustomerAddressVo) {
        saveBefore(brtCustomerAddressVo);
        int i = this.baseMapper.updateById(brtCustomerAddressVo);
        return brtCustomerAddressVo;
    }

    @Transactional
    @Override
    public int deleteBrtCustomerAddressByAddressIds(String[] addressIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(addressIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param brtCustomerAddressVo
     * @return:
     **/
    public void saveBefore(BrtCustomerAddressVo brtCustomerAddressVo){

    }

}
