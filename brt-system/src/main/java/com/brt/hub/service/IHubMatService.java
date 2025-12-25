package com.brt.hub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.hub.domain.HubMat;
import com.brt.hub.vo.HubMatVo;

import java.util.List;

/**
 * 材料信息Service接口
 */
public interface IHubMatService extends IService<HubMat> {

    /**
     * 分页查询材料列表
     */
    TableDataInfo<HubMatVo> queryHubMatList(HubMatVo hubMatVo);

    /**
     * 查询材料列表
     */
    List<HubMatVo> queryHubMatAll(HubMatVo hubMatVo);

    /**
     * 根据编码查询材料
     */
    HubMatVo queryHubMatByMatCode(String matCode);

    /**
     * 新增材料
     */
    HubMatVo insertHubMat(HubMatVo hubMatVo);

    /**
     * 修改材料
     */
    HubMatVo updateHubMat(HubMatVo hubMatVo);

    /**
     * 批量删除材料
     */
    int deleteHubMatByMatCodes(String[] matCodes);
}
