package com.brt.hub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.hub.domain.HubMat;
import com.brt.hub.mapper.HubMatMapper;
import com.brt.hub.service.IHubMatService;
import com.brt.hub.vo.HubMatVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 材料信息Service业务层处理
 */
@Service
public class HubMatServiceImpl extends ServiceImpl<HubMatMapper, HubMat> implements IHubMatService {

    @Override
    public TableDataInfo<HubMatVo> queryHubMatList(HubMatVo hubMatVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryHubMatList(PageUtils.buildPage(), hubMatVo));
    }

    @Override
    public List<HubMatVo> queryHubMatAll(HubMatVo hubMatVo) {
        return this.baseMapper.queryHubMatList(hubMatVo);
    }

    @Override
    public HubMatVo queryHubMatByMatCode(String matCode) {
        return this.baseMapper.queryHubMatByMatCode(matCode);
    }

    @Transactional
    @Override
    public HubMatVo insertHubMat(HubMatVo hubMatVo) {
        saveBefore(hubMatVo);
        this.baseMapper.insert(hubMatVo);
        return hubMatVo;
    }

    @Transactional
    @Override
    public HubMatVo updateHubMat(HubMatVo hubMatVo) {
        saveBefore(hubMatVo);
        this.baseMapper.updateById(hubMatVo);
        return hubMatVo;
    }

    @Transactional
    @Override
    public int deleteHubMatByMatCodes(String[] matCodes) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(matCodes));
    }

    private void saveBefore(HubMatVo hubMatVo) {
        // hook for preprocessing before save
    }
}
