package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtCustomerType;
import com.brt.order.vo.BrtCustomerTypeVo;
import com.brt.order.mapper.BrtCustomerTypeMapper;
import com.brt.order.service.IBrtCustomerTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 客户类型管理Service业务层处理
 * 
 * @author Fgn
 * @date 2024-04-27
 */
@Service
public class BrtCustomerTypeServiceImpl extends ServiceImpl<BrtCustomerTypeMapper, BrtCustomerType> implements IBrtCustomerTypeService {

    @Override
    public TableDataInfo<BrtCustomerTypeVo> queryBrtCustomerTypeList(BrtCustomerTypeVo brtCustomerTypeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtCustomerTypeList(PageUtils.buildPage(), brtCustomerTypeVo));
    }

    @Override
    public List<BrtCustomerTypeVo> queryBrtCustomerTypeAll(BrtCustomerTypeVo brtCustomerTypeVo) {
        return this.baseMapper.queryBrtCustomerTypeList(brtCustomerTypeVo);
    }

    @Override
    public BrtCustomerTypeVo queryBrtCustomerTypeByTypeId(String typeId) {
        return this.baseMapper.queryBrtCustomerTypeByTypeId(typeId);
    }

    @Transactional
    @Override
    public BrtCustomerTypeVo insertBrtCustomerType(BrtCustomerTypeVo brtCustomerTypeVo) {
        saveBefore(brtCustomerTypeVo);
        int i = this.baseMapper.insert(brtCustomerTypeVo);
        return brtCustomerTypeVo;
    }

    @Transactional
    @Override
    public BrtCustomerTypeVo updateBrtCustomerType(BrtCustomerTypeVo brtCustomerTypeVo) {
        saveBefore(brtCustomerTypeVo);
        int i = this.baseMapper.updateById(brtCustomerTypeVo);
        return brtCustomerTypeVo;
    }

    @Transactional
    @Override
    public int deleteBrtCustomerTypeByTypeIds(String[] typeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(typeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param brtCustomerTypeVo
     * @return:
     **/
    public void saveBefore(BrtCustomerTypeVo brtCustomerTypeVo){

    }

}
