package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtCustomerGrade;
import com.brt.order.vo.BrtCustomerGradeVo;
import com.brt.order.mapper.BrtCustomerGradeMapper;
import com.brt.order.service.IBrtCustomerGradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 客户等级Service业务层处理
 * 
 * @author Fgn
 * @date 2024-04-27
 */
@Service
public class BrtCustomerGradeServiceImpl extends ServiceImpl<BrtCustomerGradeMapper, BrtCustomerGrade> implements IBrtCustomerGradeService {

    @Override
    public TableDataInfo<BrtCustomerGradeVo> queryBrtCustomerGradeList(BrtCustomerGradeVo brtCustomerGradeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtCustomerGradeList(PageUtils.buildPage(), brtCustomerGradeVo));
    }

    @Override
    public List<BrtCustomerGradeVo> queryBrtCustomerGradeAll(BrtCustomerGradeVo brtCustomerGradeVo) {
        return this.baseMapper.queryBrtCustomerGradeList(brtCustomerGradeVo);
    }

    @Override
    public BrtCustomerGradeVo queryBrtCustomerGradeByGradeId(String gradeId) {
        return this.baseMapper.queryBrtCustomerGradeByGradeId(gradeId);
    }

    @Transactional
    @Override
    public BrtCustomerGradeVo insertBrtCustomerGrade(BrtCustomerGradeVo brtCustomerGradeVo) {
        saveBefore(brtCustomerGradeVo);
        int i = this.baseMapper.insert(brtCustomerGradeVo);
        return brtCustomerGradeVo;
    }

    @Transactional
    @Override
    public BrtCustomerGradeVo updateBrtCustomerGrade(BrtCustomerGradeVo brtCustomerGradeVo) {
        saveBefore(brtCustomerGradeVo);
        int i = this.baseMapper.updateById(brtCustomerGradeVo);
        return brtCustomerGradeVo;
    }

    @Transactional
    @Override
    public int deleteBrtCustomerGradeByGradeIds(String[] gradeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(gradeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param brtCustomerGradeVo
     * @return:
     **/
    public void saveBefore(BrtCustomerGradeVo brtCustomerGradeVo){

    }

}
