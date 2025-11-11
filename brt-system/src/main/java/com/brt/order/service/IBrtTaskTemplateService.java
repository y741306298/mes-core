package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtTaskTemplate;
import com.brt.order.vo.BrtTaskTemplateVo;

import java.util.List;

/**
 * 任务模板Service接口
 */
public interface IBrtTaskTemplateService extends IService<BrtTaskTemplate> {

    /**
     * 分页查询任务模板列表
     */
    TableDataInfo<BrtTaskTemplateVo> queryTaskTemplateList(BrtTaskTemplateVo query);

    /**
     * 查询全部任务模板列表
     */
    List<BrtTaskTemplateVo> queryTaskTemplateAll(BrtTaskTemplateVo query);

    /**
     * 根据ID查询任务模板
     */
    BrtTaskTemplateVo queryTaskTemplateById(String templateId);

    /**
     * 新增任务模板
     */
    BrtTaskTemplateVo insertTaskTemplate(BrtTaskTemplateVo vo);

    /**
     * 修改任务模板
     */
    BrtTaskTemplateVo updateTaskTemplate(BrtTaskTemplateVo vo);

    /**
     * 批量删除任务模板
     */
    int deleteTaskTemplateByIds(String[] templateIds);
}
