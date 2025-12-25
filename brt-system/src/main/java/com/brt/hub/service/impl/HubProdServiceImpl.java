package com.brt.hub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.hub.domain.HubProd;
import com.brt.hub.mapper.HubProdMapper;
import com.brt.hub.service.IHubProdService;
import com.brt.hub.vo.HubProdVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 产品信息Service业务层处理
 */
@Service
public class HubProdServiceImpl extends ServiceImpl<HubProdMapper, HubProd> implements IHubProdService {

    @Override
    public TableDataInfo<HubProdVo> queryHubProdList(HubProdVo hubProdVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryHubProdList(PageUtils.buildPage(), hubProdVo));
    }

    @Override
    public List<HubProdVo> queryHubProdAll(HubProdVo hubProdVo) {
        return this.baseMapper.queryHubProdList(hubProdVo);
    }

    @Override
    public HubProdVo queryHubProdByProdCode(String prodCode) {
        return this.baseMapper.queryHubProdByProdCode(prodCode);
    }

    @Transactional
    @Override
    public HubProdVo insertHubProd(HubProdVo hubProdVo) {
        saveBefore(hubProdVo);
        this.baseMapper.insert(hubProdVo);
        return hubProdVo;
    }

    @Transactional
    @Override
    public HubProdVo updateHubProd(HubProdVo hubProdVo) {
        saveBefore(hubProdVo);
        this.baseMapper.updateById(hubProdVo);
        return hubProdVo;
    }

    @Transactional
    @Override
    public int deleteHubProdByProdCodes(String[] prodCodes) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(prodCodes));
    }

    private void saveBefore(HubProdVo hubProdVo) {
        // hook for preprocessing before save
    }
}
