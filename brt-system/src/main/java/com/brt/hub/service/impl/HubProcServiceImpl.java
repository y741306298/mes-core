package com.brt.hub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.hub.domain.HubProc;
import com.brt.hub.mapper.HubProcMapper;
import com.brt.hub.service.IHubProcService;
import com.brt.hub.vo.HubProcVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 工艺信息Service业务层处理
 */
@Service
public class HubProcServiceImpl extends ServiceImpl<HubProcMapper, HubProc> implements IHubProcService {

    @Override
    public TableDataInfo<HubProcVo> queryHubProcList(HubProcVo hubProcVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryHubProcList(PageUtils.buildPage(), hubProcVo));
    }

    @Override
    public List<HubProcVo> queryHubProcAll(HubProcVo hubProcVo) {
        return this.baseMapper.queryHubProcList(hubProcVo);
    }

    @Override
    public HubProcVo queryHubProcByProcCode(String procCode) {
        return this.baseMapper.queryHubProcByProcCode(procCode);
    }

    @Transactional
    @Override
    public HubProcVo insertHubProc(HubProcVo hubProcVo) {
        saveBefore(hubProcVo);
        this.baseMapper.insert(hubProcVo);
        return hubProcVo;
    }

    @Transactional
    @Override
    public HubProcVo updateHubProc(HubProcVo hubProcVo) {
        saveBefore(hubProcVo);
        this.baseMapper.updateById(hubProcVo);
        return hubProcVo;
    }

    @Transactional
    @Override
    public int deleteHubProcByProcCodes(String[] procCodes) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(procCodes));
    }

    private void saveBefore(HubProcVo hubProcVo) {
        // hook for preprocessing before save
    }
}
