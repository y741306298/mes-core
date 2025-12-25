package com.brt.hub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.hub.domain.HubProd;
import com.brt.hub.vo.HubProdVo;

import java.util.List;

/**
 * 产品信息Service接口
 */
public interface IHubProdService extends IService<HubProd> {

    /**
     * 分页查询产品列表
     */
    TableDataInfo<HubProdVo> queryHubProdList(HubProdVo hubProdVo);

    /**
     * 查询产品列表
     */
    List<HubProdVo> queryHubProdAll(HubProdVo hubProdVo);

    /**
     * 根据编码查询产品
     */
    HubProdVo queryHubProdByProdCode(String prodCode);

    /**
     * 新增产品
     */
    HubProdVo insertHubProd(HubProdVo hubProdVo);

    /**
     * 修改产品
     */
    HubProdVo updateHubProd(HubProdVo hubProdVo);

    /**
     * 批量删除产品
     */
    int deleteHubProdByProdCodes(String[] prodCodes);
}
