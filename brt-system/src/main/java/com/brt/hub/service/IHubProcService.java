package com.brt.hub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.hub.domain.HubProc;
import com.brt.hub.vo.HubProcVo;

import java.util.List;

/**
 * 工艺信息Service接口
 */
public interface IHubProcService extends IService<HubProc> {

    /**
     * 分页查询工艺列表
     */
    TableDataInfo<HubProcVo> queryHubProcList(HubProcVo hubProcVo);

    /**
     * 查询工艺列表
     */
    List<HubProcVo> queryHubProcAll(HubProcVo hubProcVo);

    /**
     * 根据编码查询工艺
     */
    HubProcVo queryHubProcByProcCode(String procCode);

    /**
     * 新增工艺
     */
    HubProcVo insertHubProc(HubProcVo hubProcVo);

    /**
     * 修改工艺
     */
    HubProcVo updateHubProc(HubProcVo hubProcVo);

    /**
     * 批量删除工艺
     */
    int deleteHubProcByProcCodes(String[] procCodes);
}
