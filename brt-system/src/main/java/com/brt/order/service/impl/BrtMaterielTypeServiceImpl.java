package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtMaterielType;
import com.brt.order.vo.BrtMaterielTypeVo;
import com.brt.order.mapper.BrtMaterielTypeMapper;
import com.brt.order.service.IBrtMaterielTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 物料类型Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-07
 */
@Service
public class BrtMaterielTypeServiceImpl extends ServiceImpl<BrtMaterielTypeMapper, BrtMaterielType> implements IBrtMaterielTypeService {

    @Override
    public TableDataInfo<BrtMaterielTypeVo> queryBrtMaterielTypeList(BrtMaterielTypeVo brtMaterielTypeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtMaterielTypeList(PageUtils.buildPage(), brtMaterielTypeVo));
    }

    @Override
    public List<BrtMaterielTypeVo> queryBrtMaterielTypeAll(BrtMaterielTypeVo brtMaterielTypeVo) {
        return this.baseMapper.queryBrtMaterielTypeList(brtMaterielTypeVo);
    }

    @Override
    public BrtMaterielTypeVo queryBrtMaterielTypeByTypeId(String typeId) {
        return this.baseMapper.queryBrtMaterielTypeByTypeId(typeId);
    }

    @Transactional
    @Override
    public BrtMaterielTypeVo insertBrtMaterielType(BrtMaterielTypeVo brtMaterielTypeVo) {
        saveBefore(brtMaterielTypeVo);
        int i = this.baseMapper.insert(brtMaterielTypeVo);
        return brtMaterielTypeVo;
    }

    @Transactional
    @Override
    public BrtMaterielTypeVo updateBrtMaterielType(BrtMaterielTypeVo brtMaterielTypeVo) {
        saveBefore(brtMaterielTypeVo);
        int i = this.baseMapper.updateById(brtMaterielTypeVo);
        return brtMaterielTypeVo;
    }

    @Transactional
    @Override
    public int deleteBrtMaterielTypeByTypeIds(String[] typeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(typeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param brtMaterielTypeVo
     * @return:
     **/
    public void saveBefore(BrtMaterielTypeVo brtMaterielTypeVo){

    }

    /**
     * 根据名称查询
     * @param typeName
     * @return
     */
    @Override
    public BrtMaterielTypeVo queryByName(String typeName) {
        return this.baseMapper.queryByName(typeName);
    }

}
