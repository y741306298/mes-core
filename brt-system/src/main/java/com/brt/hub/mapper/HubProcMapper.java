package com.brt.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.hub.domain.HubProc;
import com.brt.hub.vo.HubProcVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工艺信息Mapper接口
 */
public interface HubProcMapper extends BaseMapper<HubProc> {

    /**
     * 分页查询工艺列表
     */
    Page<HubProcVo> queryHubProcList(Page<?> page, @Param("hubProcVo") HubProcVo hubProcVo);

    /**
     * 查询工艺列表
     */
    List<HubProcVo> queryHubProcList(@Param("hubProcVo") HubProcVo hubProcVo);

    /**
     * 根据编码查询工艺
     */
    HubProcVo queryHubProcByProcCode(@Param("ProcCode") String procCode);
}
