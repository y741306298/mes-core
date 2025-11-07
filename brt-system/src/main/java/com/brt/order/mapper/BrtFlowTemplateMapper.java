package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtFlowTemplate;
import com.brt.order.vo.BrtFlowTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程模板Mapper接口
 * 
 * @author Fgn
 * @date 2024-04-30
 */
public interface BrtFlowTemplateMapper extends BaseMapper<BrtFlowTemplate> {

    /**
     * @description: TODO 分页查询流程模板列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: page
     * @param: brtFlowTemplateVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtFlowTemplateVo> queryBrtFlowTemplateList(Page<?> page, @Param("brtFlowTemplateVo") BrtFlowTemplateVo brtFlowTemplateVo);

    /**
     * @description: TODO 查询流程模板列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtFlowTemplateVo> queryBrtFlowTemplateList(@Param("brtFlowTemplateVo") BrtFlowTemplateVo brtFlowTemplateVo);

    /**
     * @description: TODO 根据templateId查询流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @return:
     * @return Vo
     **/
    BrtFlowTemplateVo queryBrtFlowTemplateByTemplateId(@Param("TemplateId") String templateId);

    /**
     * @description: TODO 根据templateId查询流程模板
     * @author: FanGN
     * @date: 01:09 2024/5/11
     * @param:
     * @param templateId
     * @return:
     * @return com.brt.order.vo.BrtFlowTemplateVo
     **/
    BrtFlowTemplateVo queryNotOtherFlowTemplateByTemplateId(@Param("TemplateId") String templateId);

}
