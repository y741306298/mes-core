package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.order.domain.BrtOrderDynamic;
import com.brt.order.vo.BrtOrderDynamicVo;
import com.brt.order.mapper.BrtOrderDynamicMapper;
import com.brt.order.service.IBrtOrderDynamicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 订单动态Service业务层处理
 * 
 * @author Fgn
 * @date 2024-05-12
 */
@Service
public class BrtOrderDynamicServiceImpl extends ServiceImpl<BrtOrderDynamicMapper, BrtOrderDynamic> implements IBrtOrderDynamicService {

    @Override
    public TableDataInfo<BrtOrderDynamicVo> queryBrtOrderDynamicList(BrtOrderDynamicVo brtOrderDynamicVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderDynamicList(PageUtils.buildPage(), brtOrderDynamicVo));
    }

    @Override
    public List<BrtOrderDynamicVo> queryBrtOrderDynamicAll(BrtOrderDynamicVo brtOrderDynamicVo) {
        return this.baseMapper.queryBrtOrderDynamicList(brtOrderDynamicVo);
    }

    @Override
    public BrtOrderDynamicVo queryBrtOrderDynamicByDynamicId(String dynamicId) {
        return this.baseMapper.queryBrtOrderDynamicByDynamicId(dynamicId);
    }

    @Transactional
    @Override
    public BrtOrderDynamicVo insertBrtOrderDynamic(BrtOrderDynamicVo brtOrderDynamicVo) {
        saveBefore(brtOrderDynamicVo);
        brtOrderDynamicVo.setUserId(SecurityUtils.getUserId().toString());
        int i = this.baseMapper.insert(brtOrderDynamicVo);
        return brtOrderDynamicVo;
    }

    @Transactional
    @Override
    public BrtOrderDynamicVo updateBrtOrderDynamic(BrtOrderDynamicVo brtOrderDynamicVo) {
        saveBefore(brtOrderDynamicVo);
        int i = this.baseMapper.updateById(brtOrderDynamicVo);
        return brtOrderDynamicVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderDynamicByDynamicIds(String[] dynamicIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(dynamicIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-12
     * @param:
     * @param brtOrderDynamicVo
     * @return:
     **/
    public void saveBefore(BrtOrderDynamicVo brtOrderDynamicVo){

    }

}
