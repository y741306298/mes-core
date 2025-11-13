package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtTaskTemplate;
import com.brt.order.mapper.BrtTaskTemplateMapper;
import com.brt.order.service.IBrtTaskTemplateService;
import com.brt.order.vo.BrtTaskTemplateVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务模板Service业务层处理
 */
@Service
public class BrtTaskTemplateServiceImpl extends ServiceImpl<BrtTaskTemplateMapper, BrtTaskTemplate>
    implements IBrtTaskTemplateService {

    @Override
    public TableDataInfo<BrtTaskTemplateVo> queryTaskTemplateList(BrtTaskTemplateVo query) {
        LambdaQueryWrapper<BrtTaskTemplate> wrapper = buildQueryWrapper(query);
        Page<BrtTaskTemplate> page = this.page(PageUtils.buildPage(), wrapper);
        Page<BrtTaskTemplateVo> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream()
            .map(item -> BeanUtil.copyProperties(item, BrtTaskTemplateVo.class))
            .collect(Collectors.toList()));
        return PageUtils.buildDataInfo(voPage);
    }

    @Override
    public List<BrtTaskTemplateVo> queryTaskTemplateAll(BrtTaskTemplateVo query) {
        LambdaQueryWrapper<BrtTaskTemplate> wrapper = buildQueryWrapper(query);
        List<BrtTaskTemplate> list = this.list(wrapper);
        return list.stream()
            .map(item -> BeanUtil.copyProperties(item, BrtTaskTemplateVo.class))
            .collect(Collectors.toList());
    }

    @Override
    public BrtTaskTemplateVo queryTaskTemplateById(String templateId) {
        if (StrUtil.isBlank(templateId)) {
            return null;
        }
        BrtTaskTemplate entity = this.getById(templateId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, BrtTaskTemplateVo.class);
    }

    @Transactional
    @Override
    public BrtTaskTemplateVo insertTaskTemplate(BrtTaskTemplateVo vo) {
        fillDefaults(vo);
        this.save(vo);
        return queryTaskTemplateById(vo.getTemplateId());
    }

    @Transactional
    @Override
    public BrtTaskTemplateVo updateTaskTemplate(BrtTaskTemplateVo vo) {
        fillDefaults(vo);
        this.updateById(vo);
        return queryTaskTemplateById(vo.getTemplateId());
    }

    @Override
    public int deleteTaskTemplateByIds(String[] templateIds) {
        if (templateIds == null || templateIds.length == 0) {
            return 0;
        }
        return this.baseMapper.deleteBatchIds(Arrays.asList(templateIds));
    }

    private LambdaQueryWrapper<BrtTaskTemplate> buildQueryWrapper(BrtTaskTemplate query) {
        LambdaQueryWrapper<BrtTaskTemplate> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (StrUtil.isNotBlank(query.getTemplateName())) {
                wrapper.like(BrtTaskTemplate::getTemplateName, query.getTemplateName().trim());
            }
            if (StrUtil.isNotBlank(query.getTemplateType())) {
                wrapper.eq(BrtTaskTemplate::getTemplateType, query.getTemplateType());
            }
            if (StrUtil.isNotBlank(query.getTriggerMode())) {
                wrapper.eq(BrtTaskTemplate::getTriggerMode, query.getTriggerMode());
            }
            if (StrUtil.isNotBlank(query.getStatus())) {
                wrapper.eq(BrtTaskTemplate::getStatus, query.getStatus());
            }
        }
        wrapper.orderByDesc(BrtTaskTemplate::getUpdateTime)
            .orderByDesc(BrtTaskTemplate::getCreateTime);
        return wrapper;
    }

    private void fillDefaults(BrtTaskTemplate template) {
        if (template == null) {
            return;
        }
        template.setTemplateType(StrUtil.blankToDefault(template.getTemplateType(), "API"));
        template.setTriggerMode(StrUtil.blankToDefault(template.getTriggerMode(), "AUTO"));
        template.setConfig(StrUtil.blankToDefault(template.getConfig(), "{}"));
        template.setResultStatuses(StrUtil.blankToDefault(template.getResultStatuses(), "[]"));
        if (StrUtil.isNotBlank(template.getQuerySql())) {
            template.setQuerySql(template.getQuerySql().trim());
        }
        if (StrUtil.isNotBlank(template.getStorageSql())) {
            template.setStorageSql(template.getStorageSql().trim());
        }
        template.setStatus(StrUtil.blankToDefault(template.getStatus(), "0"));
    }
}
