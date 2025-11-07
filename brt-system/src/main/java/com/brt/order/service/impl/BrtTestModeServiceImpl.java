package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtTestMode;
import com.brt.order.vo.BrtTestModeVo;
import com.brt.order.mapper.BrtTestModeMapper;
import com.brt.order.service.IBrtTestModeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 测试方式管理Service业务层处理
 * 
 * @author Fgn
 * @date 2024-05-09
 */
@Service
public class BrtTestModeServiceImpl extends ServiceImpl<BrtTestModeMapper, BrtTestMode> implements IBrtTestModeService {

    @Override
    public TableDataInfo<BrtTestModeVo> queryBrtTestModeList(BrtTestModeVo brtTestModeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtTestModeList(PageUtils.buildPage(), brtTestModeVo));
    }

    @Override
    public List<BrtTestModeVo> queryBrtTestModeAll(BrtTestModeVo brtTestModeVo) {
        return this.baseMapper.queryBrtTestModeList(brtTestModeVo);
    }

    @Override
    public BrtTestModeVo queryBrtTestModeByModeId(String modeId) {
        return this.baseMapper.queryBrtTestModeByModeId(modeId);
    }

    @Transactional
    @Override
    public BrtTestModeVo insertBrtTestMode(BrtTestModeVo brtTestModeVo) {
        saveBefore(brtTestModeVo);
        int i = this.baseMapper.insert(brtTestModeVo);
        return brtTestModeVo;
    }

    @Transactional
    @Override
    public BrtTestModeVo updateBrtTestMode(BrtTestModeVo brtTestModeVo) {
        saveBefore(brtTestModeVo);
        int i = this.baseMapper.updateById(brtTestModeVo);
        return brtTestModeVo;
    }

    @Transactional
    @Override
    public int deleteBrtTestModeByModeIds(String[] modeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(modeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param brtTestModeVo
     * @return:
     **/
    public void saveBefore(BrtTestModeVo brtTestModeVo){

    }

}
