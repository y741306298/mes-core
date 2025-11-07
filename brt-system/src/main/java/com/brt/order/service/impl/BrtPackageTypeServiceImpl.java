package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtPackageType;
import com.brt.order.vo.BrtPackageTypeVo;
import com.brt.order.mapper.BrtPackageTypeMapper;
import com.brt.order.service.IBrtPackageTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 封装类型管理Service业务层处理
 * 
 * @author Fgn
 * @date 2024-05-09
 */
@Service
public class BrtPackageTypeServiceImpl extends ServiceImpl<BrtPackageTypeMapper, BrtPackageType> implements IBrtPackageTypeService {

    @Override
    public TableDataInfo<BrtPackageTypeVo> queryBrtPackageTypeList(BrtPackageTypeVo brtPackageTypeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtPackageTypeList(PageUtils.buildPage(), brtPackageTypeVo));
    }

    @Override
    public List<BrtPackageTypeVo> queryBrtPackageTypeAll(BrtPackageTypeVo brtPackageTypeVo) {
        return this.baseMapper.queryBrtPackageTypeList(brtPackageTypeVo);
    }

    @Override
    public BrtPackageTypeVo queryBrtPackageTypeByTypeId(String typeId) {
        return this.baseMapper.queryBrtPackageTypeByTypeId(typeId);
    }

    @Transactional
    @Override
    public BrtPackageTypeVo insertBrtPackageType(BrtPackageTypeVo brtPackageTypeVo) {
        saveBefore(brtPackageTypeVo);
        int i = this.baseMapper.insert(brtPackageTypeVo);
        return brtPackageTypeVo;
    }

    @Transactional
    @Override
    public BrtPackageTypeVo updateBrtPackageType(BrtPackageTypeVo brtPackageTypeVo) {
        saveBefore(brtPackageTypeVo);
        int i = this.baseMapper.updateById(brtPackageTypeVo);
        return brtPackageTypeVo;
    }

    @Transactional
    @Override
    public int deleteBrtPackageTypeByTypeIds(String[] typeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(typeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtPackageTypeVo
     * @return:
     **/
    public void saveBefore(BrtPackageTypeVo brtPackageTypeVo){

    }

}
