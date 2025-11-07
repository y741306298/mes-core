package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtTestType;
import com.brt.order.vo.BrtTestTypeVo;
import com.brt.order.mapper.BrtTestTypeMapper;
import com.brt.order.service.IBrtTestTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 测试类型管理Service业务层处理
 * 
 * @author Fgn
 * @date 2024-05-09
 */
@Service
public class BrtTestTypeServiceImpl extends ServiceImpl<BrtTestTypeMapper, BrtTestType> implements IBrtTestTypeService {

    @Override
    public TableDataInfo<BrtTestTypeVo> queryBrtTestTypeList(BrtTestTypeVo brtTestTypeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtTestTypeList(PageUtils.buildPage(), brtTestTypeVo));
    }

    @Override
    public List<BrtTestTypeVo> queryBrtTestTypeAll(BrtTestTypeVo brtTestTypeVo) {
        return this.baseMapper.queryBrtTestTypeList(brtTestTypeVo);
    }

    @Override
    public BrtTestTypeVo queryBrtTestTypeByTypeId(String typeId) {
        return this.baseMapper.queryBrtTestTypeByTypeId(typeId);
    }

    @Transactional
    @Override
    public BrtTestTypeVo insertBrtTestType(BrtTestTypeVo brtTestTypeVo) {
        saveBefore(brtTestTypeVo);
        int i = this.baseMapper.insert(brtTestTypeVo);
        return brtTestTypeVo;
    }

    @Transactional
    @Override
    public BrtTestTypeVo updateBrtTestType(BrtTestTypeVo brtTestTypeVo) {
        saveBefore(brtTestTypeVo);
        int i = this.baseMapper.updateById(brtTestTypeVo);
        return brtTestTypeVo;
    }

    @Transactional
    @Override
    public int deleteBrtTestTypeByTypeIds(String[] typeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(typeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtTestTypeVo
     * @return:
     **/
    public void saveBefore(BrtTestTypeVo brtTestTypeVo){

    }

}
