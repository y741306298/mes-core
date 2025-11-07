package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtField;
import com.brt.order.vo.BrtFieldVo;
import com.brt.order.mapper.BrtFieldMapper;
import com.brt.order.service.IBrtFieldService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 自定义字段Service业务层处理
 *
 * @author Fgn
 * @date 2024-06-15
 */
@Service
public class BrtFieldServiceImpl extends ServiceImpl<BrtFieldMapper, BrtField> implements IBrtFieldService {

    @Override
    public TableDataInfo<BrtFieldVo> queryBrtFieldList(BrtFieldVo brtFieldVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtFieldList(PageUtils.buildPage(), brtFieldVo));
    }

    @Override
    public List<BrtFieldVo> queryBrtFieldAll(BrtFieldVo brtFieldVo) {
        return this.baseMapper.queryBrtFieldList(brtFieldVo);
    }

    @Override
    public BrtFieldVo queryBrtFieldByFieldId(String fieldId) {
        return this.baseMapper.queryBrtFieldByFieldId(fieldId);
    }

    @Transactional
    @Override
    public BrtFieldVo insertBrtField(BrtFieldVo brtFieldVo) {
        saveBefore(brtFieldVo);
        int i = this.baseMapper.insert(brtFieldVo);
        return brtFieldVo;
    }

    @Transactional
    @Override
    public BrtFieldVo updateBrtField(BrtFieldVo brtFieldVo) {
        saveBefore(brtFieldVo);
        int i = this.baseMapper.updateById(brtFieldVo);
        return brtFieldVo;
    }

    @Transactional
    @Override
    public int deleteBrtFieldByFieldIds(String[] fieldIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(fieldIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param brtFieldVo
     * @return:
     **/
    public void saveBefore(BrtFieldVo brtFieldVo){

    }

}
