package com.brt.productionflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.brt.common.enums.AuditStatusEnums;
import com.brt.common.enums.NodeStatusEnums;
import com.brt.common.enums.OrderTemplateStatusEnums;
import com.brt.common.enums.OrderTemplateTypeEnums;
import com.brt.common.utils.StringUtils;
import cn.hutool.core.util.StrUtil;
import com.brt.order.domain.BrtOrderNode;
import com.brt.order.domain.BrtOrderTemplate;
import com.brt.order.service.IBrtFlowNodeService;
import com.brt.order.service.IBrtFlowTemplateService;
import com.brt.order.service.IBrtOrderNodeService;
import com.brt.order.service.IBrtOrderTemplateService;
import com.brt.order.vo.BrtFlowNodeVo;
import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.order.vo.BrtOrderNodeVo;
import com.brt.order.vo.BrtOrderTemplateVo;
import com.brt.productionflow.domain.ComposeTypePool;
import com.brt.productionflow.domain.ComposeFlow;
import com.brt.productionflow.domain.ComposeFlowMaterial;
import com.brt.productionflow.domain.ComposeFlowRel;
import com.brt.productionflow.domain.ComposeFlowStep;
import com.brt.productionflow.mapper.ComposeTypePoolMapper;
import com.brt.productionflow.mapper.ComposeFlowMapper;
import com.brt.productionflow.mapper.ComposeFlowMaterialMapper;
import com.brt.productionflow.mapper.ComposeFlowRelMapper;
import com.brt.productionflow.mapper.ComposeFlowStepMapper;
import com.brt.productionflow.service.IComposeTypePoolService;
import com.brt.productionflow.vo.ComposeTypePoolQuery;
import com.brt.productionflow.vo.ComposeTypePoolVo;
import com.brt.productionflow.vo.ComposeFlowQuery;
import com.brt.productionflow.vo.ComposeFlowVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 排版池业务实现
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ComposeTypePoolServiceImpl implements IComposeTypePoolService {

    private final ComposeTypePoolMapper composeTypePoolMapper;
    private final ComposeFlowMapper composeFlowMapper;
    private final ComposeFlowMaterialMapper composeFlowMaterialMapper;
    private final ComposeFlowRelMapper composeFlowRelMapper;
    private final ComposeFlowStepMapper composeFlowStepMapper;
    private final IBrtFlowTemplateService flowTemplateService;
    private final IBrtOrderTemplateService orderTemplateService;
    private final IBrtOrderNodeService orderNodeService;
    private final IBrtFlowNodeService flowNodeService;

    @Override
    public List<ComposeTypePoolVo> selectComposeTypePoolList(ComposeTypePoolQuery query) {
        LambdaQueryWrapper<ComposeTypePool> wrapper = Wrappers.lambdaQuery();
        if (query != null) {
            if (StringUtils.isNotBlank(query.getKeyword())) {
                String keyword = query.getKeyword();
                wrapper.and(w -> w.like(ComposeTypePool::getComposeId, keyword)
                    .or().like(ComposeTypePool::getOrderIds, keyword)
                    .or().like(ComposeTypePool::getMaterial, keyword));
            }
            wrapper.eq(StringUtils.isNotBlank(query.getStatus()), ComposeTypePool::getOrderStatus, query.getStatus());
            wrapper.eq(StringUtils.isNotBlank(query.getPriority()), ComposeTypePool::getPriority, query.getPriority());
        }
        wrapper.orderByDesc(ComposeTypePool::getCreatedAt);
        List<ComposeTypePool> records = composeTypePoolMapper.selectList(wrapper);
        return attachTemplateData(records);
    }

    @Override
    public ComposeTypePoolVo selectComposeTypePoolById(String composeId) {
        if (StringUtils.isBlank(composeId)) {
            return null;
        }
        ComposeTypePool record = composeTypePoolMapper.selectById(composeId);
        if (record == null) {
            return null;
        }
        return attachTemplateData(Collections.singletonList(record)).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComposeTypePoolVo insertComposeTypePool(ComposeTypePoolVo composeTypePoolVo) {
        LocalDateTime now = LocalDateTime.now();
        ComposeTypePool entity = BeanUtil.copyProperties(composeTypePoolVo, ComposeTypePool.class);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        composeTypePoolMapper.insert(entity);
        createOrderProcessData(entity, false);
        return selectComposeTypePoolById(entity.getComposeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComposeTypePoolVo updateComposeTypePool(ComposeTypePoolVo composeTypePoolVo) {
        if (composeTypePoolVo == null || StringUtils.isBlank(composeTypePoolVo.getComposeId())) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        ComposeTypePool entity = BeanUtil.copyProperties(composeTypePoolVo, ComposeTypePool.class);
        entity.setUpdatedAt(now);
        composeTypePoolMapper.updateById(entity);
        return selectComposeTypePoolById(composeTypePoolVo.getComposeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteComposeTypePoolByIds(String[] composeIds) {
        List<String> ids = normalizeComposeIds(composeIds);
        if (ids.isEmpty()) {
            return 0;
        }
        clearComposeProcessesByIds(ids.toArray(new String[0]));
        return composeTypePoolMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int clearComposeProcessesByIds(String[] composeIds) {
        List<String> ids = normalizeComposeIds(composeIds);
        if (ids.isEmpty()) {
            return 0;
        }
        int nodeDeleted = orderNodeService.count(Wrappers.<BrtOrderNode>lambdaQuery()
            .in(BrtOrderNode::getOrderId, ids));
        int templateDeleted = orderTemplateService.count(Wrappers.<BrtOrderTemplate>lambdaQuery()
            .in(BrtOrderTemplate::getOrderId, ids));

        if (nodeDeleted > 0) {
            orderNodeService.remove(Wrappers.<BrtOrderNode>lambdaQuery()
                .in(BrtOrderNode::getOrderId, ids));
        }
        if (templateDeleted > 0) {
            orderTemplateService.remove(Wrappers.<BrtOrderTemplate>lambdaQuery()
                .in(BrtOrderTemplate::getOrderId, ids));
        }
        return nodeDeleted + templateDeleted;
    }

    @Override
    public List<ComposeFlowVo> selectComposeFlowList(ComposeFlowQuery query) {
        LambdaQueryWrapper<ComposeFlow> wrapper = Wrappers.lambdaQuery();
        if (query != null) {
            if (StringUtils.isNotBlank(query.getKeyword())) {
                String keyword = query.getKeyword();
                wrapper.and(w -> w.like(ComposeFlow::getFlowId, keyword)
                    .or().like(ComposeFlow::getAssignedOperator, keyword));
            }
            wrapper.eq(StringUtils.isNotBlank(query.getStatus()), ComposeFlow::getFlowStatus, query.getStatus());
            wrapper.eq(StringUtils.isNotBlank(query.getPriority()), ComposeFlow::getPriority, query.getPriority());
            wrapper.eq(StringUtils.isNotBlank(query.getTemplateId()), ComposeFlow::getTemplateId, query.getTemplateId());
        }
        wrapper.orderByDesc(ComposeFlow::getCreatedAt);
        List<ComposeFlow> flows = composeFlowMapper.selectList(wrapper);
        return attachFlowDetails(flows);
    }

    @Override
    public ComposeFlowVo selectComposeFlowById(String flowId) {
        if (StringUtils.isBlank(flowId)) {
            return null;
        }
        ComposeFlow flow = composeFlowMapper.selectById(flowId);
        if (flow == null) {
            return null;
        }
        Map<String, ComposeFlowVo> flowMap = new HashMap<>();
        ComposeFlowVo vo = BeanUtil.copyProperties(flow, ComposeFlowVo.class);
        flowMap.put(flowId, vo);
        fillFlowRelations(Collections.singleton(flowId), flowMap);
        applyTemplateData(flowMap.values());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComposeFlowVo insertComposeFlow(ComposeFlowVo productionFlowVo) {
        LocalDateTime now = LocalDateTime.now();
        ComposeFlow entity = BeanUtil.copyProperties(productionFlowVo, ComposeFlow.class);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        composeFlowMapper.insert(entity);
        Set<String> composeIds = resolveComposeIds(productionFlowVo);
        productionFlowVo.setComposeIds(new ArrayList<>(composeIds));
        saveFlowRelations(entity.getFlowId(), productionFlowVo);
        syncFlowTemplateToOrders(entity, composeIds, now);
        refreshFlowOrderStatuses(Collections.singleton(entity.getFlowId()));
        updateOrdersStatus(composeIds, "已入池", now);
        return selectComposeFlowById(entity.getFlowId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComposeFlowVo updateComposeFlow(ComposeFlowVo productionFlowVo) {
        if (productionFlowVo == null || StringUtils.isBlank(productionFlowVo.getFlowId())) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        ComposeFlow entity = BeanUtil.copyProperties(productionFlowVo, ComposeFlow.class);
        entity.setUpdatedAt(now);
        composeFlowMapper.updateById(entity);

        Map<String, List<ComposeFlowRel>> previousRelations = loadFlowComposeRelations(Collections.singleton(entity.getFlowId()));
        Set<String> previousComposeIds = extractComposeIds(previousRelations.get(entity.getFlowId()));

        clearFlowRelations(entity.getFlowId());
        saveFlowRelations(entity.getFlowId(), productionFlowVo);

        Set<String> newComposeIds = resolveComposeIds(productionFlowVo);
        productionFlowVo.setComposeIds(new ArrayList<>(newComposeIds));
        syncFlowTemplateToOrders(entity, newComposeIds, now);

        refreshFlowOrderStatuses(Collections.singleton(entity.getFlowId()));
        updateOrdersStatus(newComposeIds, "已入池", now);
        previousComposeIds.removeAll(newComposeIds);
        updateOrdersStatus(previousComposeIds, "待处理", now);
        return selectComposeFlowById(entity.getFlowId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteComposeFlowByIds(String[] flowIds) {
        if (flowIds == null || flowIds.length == 0) {
            return 0;
        }
        List<String> ids = Arrays.stream(flowIds)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return 0;
        }
        Map<String, List<ComposeFlowRel>> flowOrders = loadFlowComposeRelations(new HashSet<>(ids));
        composeFlowMaterialMapper.delete(Wrappers.<ComposeFlowMaterial>lambdaQuery()
            .in(ComposeFlowMaterial::getFlowId, ids));
        composeFlowRelMapper.delete(Wrappers.<ComposeFlowRel>lambdaQuery()
            .in(ComposeFlowRel::getFlowId, ids));
        composeFlowStepMapper.delete(Wrappers.<ComposeFlowStep>lambdaQuery()
            .in(ComposeFlowStep::getFlowId, ids));
        int rows = composeFlowMapper.deleteBatchIds(ids);
        Set<String> composeIds = flowOrders.values().stream()
            .flatMap(list -> extractComposeIds(list).stream())
            .collect(Collectors.toSet());
        if (!composeIds.isEmpty()) {
            updateOrdersStatus(composeIds, "待处理", LocalDateTime.now());
        }
        return rows;
    }

    private List<ComposeTypePoolVo> attachTemplateData(List<ComposeTypePool> records) {
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        List<ComposeTypePoolVo> result = records.stream()
            .map(record -> BeanUtil.copyProperties(record, ComposeTypePoolVo.class))
            .collect(Collectors.toList());
        applyTemplateData(result);
        applyOrderProcessData(result);
        return result;
    }

    private void applyOrderProcessData(List<ComposeTypePoolVo> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        Set<String> orderIds = records.stream()
            .map(ComposeTypePool::getComposeId)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(orderIds)) {
            return;
        }

        Map<String, List<BrtOrderTemplateVo>> templateMap = orderTemplateService.list(Wrappers.<BrtOrderTemplate>lambdaQuery()
                .in(BrtOrderTemplate::getOrderId, orderIds))
            .stream()
            .map(item -> BeanUtil.copyProperties(item, BrtOrderTemplateVo.class))
            .collect(Collectors.groupingBy(BrtOrderTemplateVo::getOrderId));

        Map<String, List<BrtOrderNodeVo>> nodeMap = orderNodeService.list(Wrappers.<BrtOrderNode>lambdaQuery()
                .in(BrtOrderNode::getOrderId, orderIds)
                .orderByAsc(BrtOrderNode::getSort)
                .orderByAsc(BrtOrderNode::getOrderNodeId))
            .stream()
            .map(item -> BeanUtil.copyProperties(item, BrtOrderNodeVo.class))
            .collect(Collectors.groupingBy(BrtOrderNodeVo::getOrderId));

        records.forEach(record -> {
            String composeId = record.getComposeId();
            BrtOrderTemplateVo template = templateMap.getOrDefault(composeId, Collections.emptyList())
                .stream()
                .filter(item -> StringUtils.isBlank(record.getTemplateId()) || StringUtils.equals(record.getTemplateId(), item.getTemplateId()))
                .findFirst()
                .orElseGet(() -> templateMap.getOrDefault(composeId, Collections.emptyList()).stream().findFirst().orElse(null));
            record.setOrderTemplate(template);
            record.setOrderNodes(new ArrayList<>(nodeMap.getOrDefault(composeId, Collections.emptyList())));
        });
    }

    private List<ComposeFlowVo> attachFlowDetails(List<ComposeFlow> flows) {
        if (CollectionUtils.isEmpty(flows)) {
            return Collections.emptyList();
        }
        Map<String, ComposeFlowVo> flowMap = flows.stream()
            .map(flow -> BeanUtil.copyProperties(flow, ComposeFlowVo.class))
            .collect(Collectors.toMap(ComposeFlow::getFlowId, vo -> vo));
        fillFlowRelations(flowMap.keySet(), flowMap);
        applyTemplateData(flowMap.values());
        return flowMap.values().stream()
            .sorted((a, b) -> {
                LocalDateTime aTime = a.getCreatedAt();
                LocalDateTime bTime = b.getCreatedAt();
                if (aTime == null && bTime == null) {
                    return 0;
                }
                if (aTime == null) {
                    return 1;
                }
                if (bTime == null) {
                    return -1;
                }
                return bTime.compareTo(aTime);
            })
            .collect(Collectors.toList());
    }

    private void fillFlowRelations(Set<String> flowIds, Map<String, ComposeFlowVo> flowMap) {
        if (CollectionUtils.isEmpty(flowIds) || CollectionUtils.isEmpty(flowMap)) {
            return;
        }
        Map<String, List<ComposeFlowMaterial>> materialMap = loadFlowMaterials(flowIds);
        Map<String, List<ComposeFlowRel>> composeMap = loadFlowComposeRelations(flowIds);
        Map<String, List<ComposeFlowStep>> stepMap = loadFlowSteps(flowIds);
        flowMap.forEach((flowId, vo) -> {
            List<ComposeFlowRel> relations = new ArrayList<>(composeMap.getOrDefault(flowId, Collections.emptyList()));
            vo.setMaterialsSummary(new ArrayList<>(materialMap.getOrDefault(flowId, Collections.emptyList())));
            vo.setComposeAllocations(relations);
            vo.setComposeIds(relations.stream()
                .map(ComposeFlowRel::getComposeId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()));
            vo.setProcess(new ArrayList<>(stepMap.getOrDefault(flowId, Collections.emptyList())));
        });
    }

    private Map<String, List<ComposeFlowMaterial>> loadFlowMaterials(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Collections.emptyMap();
        }
        List<ComposeFlowMaterial> materials = composeFlowMaterialMapper.selectList(Wrappers.<ComposeFlowMaterial>lambdaQuery()
            .in(ComposeFlowMaterial::getFlowId, flowIds)
            .orderByAsc(ComposeFlowMaterial::getSortOrder)
            .orderByAsc(ComposeFlowMaterial::getMaterialId));
        return materials.stream().collect(Collectors.groupingBy(ComposeFlowMaterial::getFlowId));
    }

    private Map<String, List<ComposeFlowRel>> loadFlowComposeRelations(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Collections.emptyMap();
        }
        List<ComposeFlowRel> relations = composeFlowRelMapper.selectList(Wrappers.<ComposeFlowRel>lambdaQuery()
            .in(ComposeFlowRel::getFlowId, flowIds));
        applyComposeStatusFromProcesses(relations);
        return relations.stream().collect(Collectors.groupingBy(ComposeFlowRel::getFlowId));
    }

    private void refreshFlowOrderStatuses(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return;
        }
        loadFlowComposeRelations(flowIds);
    }

    private void applyComposeStatusFromProcesses(List<ComposeFlowRel> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return;
        }
        Set<String> composeIds = relations.stream()
            .map(ComposeFlowRel::getComposeId)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
        Map<String, String> statusMap = resolveOrderProcessStatuses(composeIds);
        relations.forEach(rel -> {
            String targetStatus = statusMap.getOrDefault(rel.getComposeId(), "pending");
            if (!StringUtils.equals(targetStatus, rel.getStatus())) {
                rel.setStatus(targetStatus);
                composeFlowRelMapper.update(null, Wrappers.<ComposeFlowRel>lambdaUpdate()
                    .eq(ComposeFlowRel::getId, rel.getId())
                    .set(ComposeFlowRel::getStatus, targetStatus));
            }
        });
    }

    private Map<String, String> resolveOrderProcessStatuses(Set<String> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return Collections.emptyMap();
        }
        List<BrtOrderNode> nodes = orderNodeService.list(Wrappers.<BrtOrderNode>lambdaQuery()
            .in(BrtOrderNode::getOrderId, orderIds));
        Map<String, List<BrtOrderNode>> nodeMap = nodes.stream()
            .collect(Collectors.groupingBy(BrtOrderNode::getOrderId));
        Map<String, String> statusMap = new HashMap<>();
        orderIds.forEach(orderId -> statusMap.put(orderId, calculateProcessStatus(nodeMap.get(orderId))));
        return statusMap;
    }

    private String calculateProcessStatus(List<BrtOrderNode> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return "pending";
        }
        boolean hasProcessing = false;
        boolean hasPending = false;
        for (BrtOrderNode node : nodes) {
            String status = node.getNodeStatus();
            if (StringUtils.equals(status, NodeStatusEnums.已超时.getCode())) {
                return "timeout";
            }
            if (StringUtils.equals(status, NodeStatusEnums.进行中.getCode())) {
                hasProcessing = true;
            } else if (!StringUtils.equals(status, NodeStatusEnums.已完成.getCode())) {
                hasPending = true;
            }
        }
        if (hasProcessing) {
            return "processing";
        }
        if (!hasPending) {
            return "completed";
        }
        return "pending";
    }

    private void saveFlowRelations(String flowId, ComposeFlowVo flowVo) {
        saveMaterials(flowId, flowVo.getMaterialsSummary());
        saveComposeRelations(flowId, flowVo);
        saveSteps(flowId, flowVo.getProcess());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyFlowTemplates(String flowId, Collection<String> composeIds) {
        if (StringUtils.isBlank(flowId) || composeIds == null) {
            return false;
        }
        ComposeFlow flow = composeFlowMapper.selectById(flowId);
        if (flow == null) {
            return false;
        }
        Set<String> normalizedComposeIds = new HashSet<>();
        composeIds.forEach(id -> {
            if (StringUtils.isNotBlank(id)) {
                normalizedComposeIds.add(id.trim());
            }
        });
        if (normalizedComposeIds.isEmpty()) {
            return false;
        }
        syncFlowTemplateToOrders(flow, normalizedComposeIds, LocalDateTime.now(), true);
        return true;
    }

    private void syncFlowTemplateToOrders(ComposeFlow flow, Set<String> composeIds, LocalDateTime now) {
        syncFlowTemplateToOrders(flow, composeIds, now, false);
    }

    private void syncFlowTemplateToOrders(ComposeFlow flow, Set<String> composeIds, LocalDateTime now,
        boolean forceCreateProcesses) {
        if (flow == null || StringUtils.isBlank(flow.getTemplateId()) || CollectionUtils.isEmpty(composeIds)) {
            return;
        }
        List<ComposeTypePool> orders = composeTypePoolMapper.selectList(Wrappers.<ComposeTypePool>lambdaQuery()
            .in(ComposeTypePool::getComposeId, composeIds));
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        orders.forEach(order -> {
            order.setTemplateId(flow.getTemplateId());
            order.setUpdatedAt(now);
            composeTypePoolMapper.updateById(order);
            createOrderProcessData(order, forceCreateProcesses);
        });
    }

    private void createOrderProcessData(ComposeTypePool orderPool, boolean forceCreateProcesses) {
        if (orderPool == null || StringUtils.isBlank(orderPool.getComposeId()) || StringUtils.isBlank(orderPool.getTemplateId())) {
            return;
        }
        List<BrtOrderTemplate> existingTemplates = orderTemplateService.list(Wrappers.<BrtOrderTemplate>lambdaQuery()
            .eq(BrtOrderTemplate::getOrderId, orderPool.getComposeId())
            .eq(BrtOrderTemplate::getTemplateId, orderPool.getTemplateId()));

        if (!forceCreateProcesses && !CollectionUtils.isEmpty(existingTemplates)) {
            return;
        }

        BrtFlowTemplateVo flowTemplate = flowTemplateService.queryBrtFlowTemplateByTemplateId(orderPool.getTemplateId());
        if (flowTemplate == null) {
            return;
        }

        BrtOrderTemplateVo orderTemplate = null;
        if (CollectionUtils.isEmpty(existingTemplates)) {
            orderTemplate = new BrtOrderTemplateVo();
            orderTemplate.setOrderId(orderPool.getComposeId());
            orderTemplate.setTemplateId(orderPool.getTemplateId());
            orderTemplate.setOrderTemplateStatus(OrderTemplateStatusEnums.正常.getCode());
            orderTemplate.setAuditStatus(AuditStatusEnums.通过.getCode());
            orderTemplate.setTemplateType(OrderTemplateTypeEnums.订单模板.getCode());
            orderTemplate.setOrderNum(Long.valueOf(orderPool.getQuantity()));
            orderTemplate.setStatus("1");
            orderTemplateService.insertBrtOrderTemplate(orderTemplate);
        }

        BrtOrderTemplate targetTemplate = CollectionUtils.isEmpty(existingTemplates)
            ? BeanUtil.copyProperties(orderTemplate, BrtOrderTemplate.class)
            : existingTemplates.get(0);

        List<BrtFlowNodeVo> flowNodes = flowTemplate.getFlowNodeList();
        if (CollectionUtils.isEmpty(flowNodes)) {
            BrtFlowNodeVo query = new BrtFlowNodeVo();
            query.setTemplateId(orderPool.getTemplateId());
            flowNodes = flowNodeService.queryBrtFlowNodeAll(query);
        }
        if (CollectionUtils.isEmpty(flowNodes)) {
            return;
        }

        List<BrtOrderNode> existingNodes = orderNodeService.list(Wrappers.<BrtOrderNode>lambdaQuery()
            .eq(BrtOrderNode::getOrderId, orderPool.getComposeId())
            .eq(BrtOrderNode::getTemplateId, orderPool.getTemplateId()));
        Set<String> existingNodeIds = existingNodes.stream()
            .map(BrtOrderNode::getNodeId)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());

        AtomicInteger sort = new AtomicInteger(existingNodes.size());
        flowNodes.forEach(flowNode -> {
            if (existingNodeIds.contains(flowNode.getNodeId())) {
                return;
            }
            BrtOrderNode orderNode = new BrtOrderNode();
            orderNode.setOrderTemplateId(targetTemplate.getOrderTemplateId());
            orderNode.setChildId(targetTemplate.getChildId());
            orderNode.setOrderId(orderPool.getComposeId());
            orderNode.setTemplateId(orderPool.getTemplateId());
            orderNode.setNodeId(flowNode.getNodeId());
            orderNode.setNodeRemark(flowNode.getNodeName());
            orderNode.setOperSetting(flowNode.getOtherSetting());
            orderNode.setTriggerMode(Boolean.TRUE.equals(flowNode.getAutoCompletion()) ? "AUTO" : "MANUAL");
            orderNode.setInterfaceType(StrUtil.blankToDefault(flowNode.getInterfaceType(), "SYNC"));
            orderNode.setCallbackUrl(StrUtil.blankToDefault(flowNode.getCallbackUrl(), ""));
            orderNode.setSort(flowNode.getSort() == null
                ? Long.valueOf(sort.getAndIncrement())
                : flowNode.getSort().longValue());
            orderNode.setNodeStatus(NodeStatusEnums.未开始.getCode());
            orderNodeService.save(orderNode);
        });
    }

    private void clearFlowRelations(String flowId) {
        composeFlowMaterialMapper.delete(Wrappers.<ComposeFlowMaterial>lambdaQuery()
            .eq(ComposeFlowMaterial::getFlowId, flowId));
        composeFlowRelMapper.delete(Wrappers.<ComposeFlowRel>lambdaQuery()
            .eq(ComposeFlowRel::getFlowId, flowId));
        composeFlowStepMapper.delete(Wrappers.<ComposeFlowStep>lambdaQuery()
            .eq(ComposeFlowStep::getFlowId, flowId));
    }

    private List<String> normalizeComposeIds(String[] composeIds) {
        if (composeIds == null || composeIds.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(composeIds)
            .filter(StringUtils::isNotBlank)
            .flatMap(id -> Arrays.stream(id.split(",")))
            .map(String::trim)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
    }

    private Set<String> resolveComposeIds(ComposeFlowVo flowVo) {
        Set<String> result = new HashSet<>();
        if (flowVo == null) {
            return result;
        }
        if (!CollectionUtils.isEmpty(flowVo.getComposeAllocations())) {
            flowVo.getComposeAllocations().stream()
                .filter(Objects::nonNull)
                .map(ComposeFlowRel::getComposeId)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .forEach(result::add);
        }
        if (!CollectionUtils.isEmpty(flowVo.getComposeIds())) {
            flowVo.getComposeIds().stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .forEach(result::add);
        }
        return result;
    }

    private Set<String> extractComposeIds(Collection<ComposeFlowRel> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return new HashSet<>();
        }
        return relations.stream()
            .filter(Objects::nonNull)
            .map(ComposeFlowRel::getComposeId)
            .filter(StringUtils::isNotBlank)
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    private Map<String, Integer> aggregateComposeQuantities(ComposeFlowVo flowVo) {
        Map<String, Integer> result = new LinkedHashMap<>();
        if (flowVo == null) {
            return result;
        }
        if (!CollectionUtils.isEmpty(flowVo.getComposeAllocations())) {
            flowVo.getComposeAllocations().stream()
                .filter(Objects::nonNull)
                .filter(rel -> StringUtils.isNotBlank(rel.getComposeId()))
                .forEach(rel -> result.merge(rel.getComposeId().trim(),
                    rel.getQuantity(),
                    (oldVal, newVal) -> (oldVal == null ? 0 : oldVal) + (newVal == null ? 0 : newVal)));
        }
        if (result.isEmpty() && !CollectionUtils.isEmpty(flowVo.getComposeIds())) {
            flowVo.getComposeIds().stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .forEach(composeId -> result.put(composeId, null));
        }
        return result;
    }

    private void saveMaterials(String flowId, List<ComposeFlowMaterial> materials) {
        if (StringUtils.isBlank(flowId) || CollectionUtils.isEmpty(materials)) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        materials.stream()
            .filter(material -> material != null && (StringUtils.isNotBlank(material.getMaterial())
                || material.getQuantity() != null))
            .map(material -> new ComposeFlowMaterial()
                .setMaterialId(null)
                .setFlowId(flowId)
                .setMaterial(material.getMaterial())
                .setQuantity(material.getQuantity())
                .setSortOrder(material.getSortOrder() != null ? material.getSortOrder() : index.getAndIncrement()))
            .forEach(composeFlowMaterialMapper::insert);
    }

    private void saveComposeRelations(String flowId, ComposeFlowVo flowVo) {
        if (StringUtils.isBlank(flowId) || flowVo == null) {
            return;
        }
        Map<String, Integer> quantityMap = aggregateComposeQuantities(flowVo);
        Map<String, String> statusMap = resolveOrderProcessStatuses(quantityMap.keySet());
        if (quantityMap.isEmpty()) {
            return;
        }
        quantityMap.forEach((composeId, quantity) -> composeFlowRelMapper.insert(new ComposeFlowRel()
            .setId(null)
            .setFlowId(flowId)
            .setComposeId(composeId)
            .setQuantity(quantity == null ? 0 : quantity)
            .setStatus(statusMap.getOrDefault(composeId, "pending"))));
    }

    private void saveSteps(String flowId, List<ComposeFlowStep> steps) {
        if (StringUtils.isBlank(flowId) || CollectionUtils.isEmpty(steps)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        AtomicInteger index = new AtomicInteger(0);
        steps.stream()
            .filter(step -> step != null && (StringUtils.isNotBlank(step.getNodeId())
                || StringUtils.isNotBlank(step.getStepName())
                || StringUtils.isNotBlank(step.getRemark())))
            .map(step -> {
                int position = index.getAndIncrement();
                String stepName = step.getStepName();
                if (StringUtils.isBlank(stepName)) {
                    stepName = StringUtils.isNotBlank(step.getRemark()) ? step.getRemark() : "步骤" + (position + 1);
                }
                return new ComposeFlowStep()
                    .setStepId(null)
                    .setFlowId(flowId)
                    .setNodeId(step.getNodeId())
                    .setStepName(stepName)
                    .setStepStatus(StringUtils.isNotBlank(step.getStepStatus()) ? step.getStepStatus() : "pending")
                    .setRemark(step.getRemark())
                    .setSortOrder(step.getSortOrder() != null ? step.getSortOrder() : position)
                    .setUpdatedAt(now);
            })
            .forEach(composeFlowStepMapper::insert);
    }

    private Map<String, List<ComposeFlowStep>> loadFlowSteps(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Collections.emptyMap();
        }
        List<ComposeFlowStep> steps = composeFlowStepMapper.selectList(Wrappers.<ComposeFlowStep>lambdaQuery()
            .in(ComposeFlowStep::getFlowId, flowIds)
            .orderByAsc(ComposeFlowStep::getSortOrder)
            .orderByAsc(ComposeFlowStep::getStepId));
        return steps.stream().collect(Collectors.groupingBy(ComposeFlowStep::getFlowId));
    }

    private void updateOrdersStatus(Collection<String> orderIds, String status, LocalDateTime now) {
        if (CollectionUtils.isEmpty(orderIds) || StringUtils.isBlank(status)) {
            return;
        }
        LambdaUpdateWrapper<ComposeTypePool> updateWrapper = Wrappers.<ComposeTypePool>lambdaUpdate()
            .in(ComposeTypePool::getComposeId, orderIds)
            .set(ComposeTypePool::getOrderStatus, status)
            .set(ComposeTypePool::getUpdatedAt, now);
        composeTypePoolMapper.update(null, updateWrapper);
    }

    private Set<String> toSafeSet(List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return new HashSet<>();
        }
        return values.stream()
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
    }

    private void applyTemplateData(Collection<?> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        Set<String> templateIds = records.stream()
            .map(record -> {
                if (record instanceof ComposeTypePool) {
                    return ((ComposeTypePool) record).getTemplateId();
                }
                if (record instanceof ComposeFlow) {
                    return ((ComposeFlow) record).getTemplateId();
                }
                return null;
            })
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(templateIds)) {
            return;
        }
        Map<String, BrtFlowTemplateVo> templateMap = loadTemplates(templateIds);
        records.forEach(record -> {
            if (record instanceof ComposeTypePoolVo) {
                ComposeTypePoolVo order = (ComposeTypePoolVo) record;
                order.setFlowTemplate(templateMap.get(order.getTemplateId()));
            } else if (record instanceof ComposeFlowVo) {
                ComposeFlowVo flow = (ComposeFlowVo) record;
                flow.setFlowTemplate(templateMap.get(flow.getTemplateId()));
            }
        });
    }

    private Map<String, BrtFlowTemplateVo> loadTemplates(Set<String> templateIds) {
        if (CollectionUtils.isEmpty(templateIds)) {
            return Collections.emptyMap();
        }
        Map<String, BrtFlowTemplateVo> result = new HashMap<>();
        templateIds.forEach(templateId -> {
            BrtFlowTemplateVo templateVo = flowTemplateService.queryBrtFlowTemplateByTemplateId(templateId);
            if (templateVo != null) {
                result.put(templateId, templateVo);
            }
        });
        return result;
    }
}
