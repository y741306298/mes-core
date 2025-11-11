package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;

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
import java.util.Locale;
import java.util.Objects;
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
        Page<BrtTaskTemplateVo> voPage = page.convert(item -> BeanUtil.copyProperties(item, BrtTaskTemplateVo.class));
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
        if (StrUtil.isBlank(template.getCreateTableSql())) {
            template.setCreateTableSql(buildCreateTableSql(template));
        }
        template.setStatus(StrUtil.blankToDefault(template.getStatus(), "0"));
    }

    private String buildCreateTableSql(BrtTaskTemplate template) {
        String rawName = StrUtil.blankToDefault(template.getTemplateName(), "task_template");
        String normalized = rawName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_");
        normalized = normalized.replaceAll("^_+", "");
        normalized = normalized.replaceAll("_+$", "");
        if (StrUtil.isBlank(normalized)) {
            normalized = "task_template";
        }
        if (normalized.length() > 32) {
            normalized = normalized.substring(0, 32);
        }
        String tableName = "task_tpl_" + normalized;
        String comment = StrUtil.isBlank(template.getTemplateName())
            ? "任务模板执行结果表"
            : "任务模板-" + template.getTemplateName() + "执行结果表";
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (\n")
            .append("  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',\n")
            .append("  `template_id` varchar(64) NOT NULL COMMENT '任务模板ID',\n")
            .append("  `status_code` varchar(64) NOT NULL COMMENT '")
            .append(buildStatusColumnComment(template)).append("',\n")
            .append("  `status_label` varchar(128) DEFAULT NULL COMMENT '结果状态名称',\n")
            .append("  `result_payload` json DEFAULT NULL COMMENT '执行结果内容',\n")
            .append("  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n")
            .append("  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n")
            .append("  PRIMARY KEY (`id`),\n")
            .append("  KEY `idx_template_status` (`template_id`,`status_code`)\n")
            .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='")
            .append(comment)
            .append("';");
        return builder.toString();
    }

    private String buildStatusColumnComment(BrtTaskTemplate template) {
        if (template == null || StrUtil.isBlank(template.getResultStatuses())) {
            return sanitizeSqlComment("结果状态编码");
        }
        try {
            JSONArray array = JSONUtil.parseArray(template.getResultStatuses());
            List<String> options = array.stream()
                .filter(Objects::nonNull)
                .map(this::resolveStatusOption)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
            if (!options.isEmpty()) {
                return sanitizeSqlComment("结果状态编码(可选: " + String.join("、", options) + ")");
            }
        } catch (Exception ignored) {
            // ignore parse exception and fallback to default
        }
        return sanitizeSqlComment("结果状态编码");
    }

    private String resolveStatusOption(Object item) {
        if (item == null) {
            return null;
        }
        if (item instanceof CharSequence) {
            String raw = StrUtil.trim(item.toString());
            return StrUtil.blankToDefault(raw, null);
        }
        JSONObject obj;
        try {
            obj = JSONUtil.parseObj(item, true);
        } catch (Exception e) {
            return null;
        }
        String value = StrUtil.blankToDefault(obj.getStr("statusValue"),
            StrUtil.blankToDefault(obj.getStr("value"), obj.getStr("code")));
        String label = StrUtil.blankToDefault(obj.getStr("statusLabel"),
            StrUtil.blankToDefault(obj.getStr("label"), obj.getStr("name")));
        if (StrUtil.isAllBlank(value, label)) {
            return null;
        }
        if (StrUtil.isNotBlank(value) && StrUtil.isNotBlank(label)) {
            return value + "(" + label + ")";
        }
        return StrUtil.blankToDefault(value, label);
    }

    private String sanitizeSqlComment(String comment) {
        return StrUtil.replace(StrUtil.blankToDefault(comment, ""), "'", "''");
    }
}
