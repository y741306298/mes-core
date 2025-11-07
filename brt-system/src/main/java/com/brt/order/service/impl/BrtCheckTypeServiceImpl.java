package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtCheckType;
import com.brt.order.vo.BrtCheckTypeVo;
import com.brt.order.mapper.BrtCheckTypeMapper;
import com.brt.order.service.IBrtCheckTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 账单类型Service业务层处理
 * 
 * @author Fgn
 * @date 2024-05-15
 */
@Service
public class BrtCheckTypeServiceImpl extends ServiceImpl<BrtCheckTypeMapper, BrtCheckType> implements IBrtCheckTypeService {

    @Override
    public TableDataInfo<BrtCheckTypeVo> queryBrtCheckTypeList(BrtCheckTypeVo brtCheckTypeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtCheckTypeList(PageUtils.buildPage(), brtCheckTypeVo));
    }

    @Override
    public List<BrtCheckTypeVo> queryBrtCheckTypeAll(BrtCheckTypeVo brtCheckTypeVo) {
        return this.baseMapper.queryBrtCheckTypeList(brtCheckTypeVo);
    }

    @Override
    public BrtCheckTypeVo queryBrtCheckTypeByTypeId(String typeId) {
        return this.baseMapper.queryBrtCheckTypeByTypeId(typeId);
    }

    @Transactional
    @Override
    public BrtCheckTypeVo insertBrtCheckType(BrtCheckTypeVo brtCheckTypeVo) {
        saveBefore(brtCheckTypeVo);
        int i = this.baseMapper.insert(brtCheckTypeVo);
        return brtCheckTypeVo;
    }

    @Transactional
    @Override
    public BrtCheckTypeVo updateBrtCheckType(BrtCheckTypeVo brtCheckTypeVo) {
        saveBefore(brtCheckTypeVo);
        int i = this.baseMapper.updateById(brtCheckTypeVo);
        return brtCheckTypeVo;
    }

    @Transactional
    @Override
    public int deleteBrtCheckTypeByTypeIds(String[] typeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(typeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param brtCheckTypeVo
     * @return:
     **/
    public void saveBefore(BrtCheckTypeVo brtCheckTypeVo){

    }

}
