package com.brt.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.hub.domain.HubMat;
import com.brt.hub.vo.HubMatVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 材料信息Mapper接口
 */
public interface HubMatMapper extends BaseMapper<HubMat> {

    /**
     * 分页查询材料列表
     */
    Page<HubMatVo> queryHubMatList(Page<?> page, @Param("hubMatVo") HubMatVo hubMatVo);

    /**
     * 查询材料列表
     */
    List<HubMatVo> queryHubMatList(@Param("hubMatVo") HubMatVo hubMatVo);

    /**
     * 根据编码查询材料
     */
    HubMatVo queryHubMatByMatCode(@Param("MatCode") String matCode);
}
