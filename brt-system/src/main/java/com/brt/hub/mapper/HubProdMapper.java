package com.brt.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.hub.domain.HubProd;
import com.brt.hub.vo.HubProdVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品信息Mapper接口
 */
public interface HubProdMapper extends BaseMapper<HubProd> {

    /**
     * 分页查询产品列表
     */
    Page<HubProdVo> queryHubProdList(Page<?> page, @Param("hubProdVo") HubProdVo hubProdVo);

    /**
     * 查询产品列表
     */
    List<HubProdVo> queryHubProdList(@Param("hubProdVo") HubProdVo hubProdVo);

    /**
     * 根据编码查询产品
     */
    HubProdVo queryHubProdByProdCode(@Param("ProdCode") String prodCode);
}
