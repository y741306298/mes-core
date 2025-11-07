package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtFlowTemplate;
import com.brt.order.vo.BrtFlowTemplateVo;

import java.util.List;

/**
 * 流程模板Service接口
 * 
 * @author Fgn
 * @date 2024-04-30
 */
public interface IBrtFlowTemplateService extends IService<BrtFlowTemplate> {

    /**
     * @description: TODO 分页查询流程模板列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return TableDataInfo<BrtFlowTemplateVo>
     **/
    public TableDataInfo<BrtFlowTemplateVo> queryBrtFlowTemplateList(BrtFlowTemplateVo brtFlowTemplateVo);

    /**
     * @description: TODO 查询全部流程模板列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return java.util.List<BrtFlowTemplateVo>
     **/
    public List<BrtFlowTemplateVo> queryBrtFlowTemplateAll(BrtFlowTemplateVo brtFlowTemplateVo);

    /**
     * @description: TODO 根据templateId查询流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: templateId
     * @return:
     * @return BrtFlowTemplateVo
     **/
    public BrtFlowTemplateVo queryBrtFlowTemplateByTemplateId(String templateId);

    /**
     * @description: TODO 新增流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return int
     **/
    public BrtFlowTemplateVo insertBrtFlowTemplate(BrtFlowTemplateVo brtFlowTemplateVo);

    /**
     * @description: TODO 修改流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowTemplateVo 流程模板
     * @return:
     * @return int
     **/
    public BrtFlowTemplateVo updateBrtFlowTemplate(BrtFlowTemplateVo brtFlowTemplateVo);

    /**
     * @description: TODO 批量删除流程模板
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param:  templateIds 需要删除的流程模板主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtFlowTemplateByTemplateIds(String[] templateIds);

    /**
     * @description: TODO 复制模板
     * @author: FanGN
     * @date: 12:01 2024/4/30
     * @param:
     * @param templateId
     * @return:
     * @return int
     **/
    int copyTemplate(String templateId);
}
