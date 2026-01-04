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
import com.brt.productionflow.domain.OrderPool;
import com.brt.productionflow.domain.ProductionFlow;
import com.brt.productionflow.domain.ProductionFlowMaterial;
import com.brt.productionflow.domain.ProductionFlowOrderRel;
import com.brt.productionflow.domain.ProductionFlowStep;
import com.brt.productionflow.mapper.OrderPoolMapper;
import com.brt.productionflow.mapper.ProductionFlowMapper;
import com.brt.productionflow.mapper.ProductionFlowMaterialMapper;
import com.brt.productionflow.mapper.ProductionFlowOrderRelMapper;
import com.brt.productionflow.mapper.ProductionFlowStepMapper;
import com.brt.productionflow.service.IOrderPoolService;
import com.brt.productionflow.vo.OrderPoolQuery;
import com.brt.productionflow.vo.OrderPoolVo;
import com.brt.productionflow.vo.ProductionFlowQuery;
import com.brt.productionflow.vo.ProductionFlowVo;
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
 * 订单池业务实现
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderPoolServiceImpl implements IOrderPoolService {

    private final OrderPoolMapper orderPoolMapper;
    private final ProductionFlowMapper productionFlowMapper;
    private final ProductionFlowMaterialMapper productionFlowMaterialMapper;
    private final ProductionFlowOrderRelMapper productionFlowOrderRelMapper;
    private final ProductionFlowStepMapper productionFlowStepMapper;
    private final IBrtFlowTemplateService flowTemplateService;
    private final IBrtOrderTemplateService orderTemplateService;
    private final IBrtOrderNodeService orderNodeService;
    private final IBrtFlowNodeService flowNodeService;

    @Override
    public List<OrderPoolVo> selectOrderPoolList(OrderPoolQuery query) {
        LambdaQueryWrapper<OrderPool> wrapper = Wrappers.lambdaQuery();
        if (query != null) {
            if (StringUtils.isNotBlank(query.getKeyword())) {
                String keyword = query.getKeyword();
                wrapper.and(w -> w.like(OrderPool::getOrderId, keyword)
                    .or().like(OrderPool::getCustomerInfo, keyword)
                    .or().like(OrderPool::getMainMaterial, keyword));
            }
            wrapper.eq(StringUtils.isNotBlank(query.getStatus()), OrderPool::getOrderStatus, query.getStatus());
            wrapper.eq(StringUtils.isNotBlank(query.getPriority()), OrderPool::getPriority, query.getPriority());
        }
        wrapper.orderByDesc(OrderPool::getCreatedAt);
        List<OrderPool> orders = orderPoolMapper.selectList(wrapper);
        return attachTemplateData(orders);
    }

    @Override
    public OrderPoolVo selectOrderPoolById(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return null;
        }
        OrderPool order = orderPoolMapper.selectById(orderId);
        if (order == null) {
            return null;
        }
        return attachTemplateData(Collections.singletonList(order)).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderPoolVo insertOrderPool(OrderPoolVo orderPoolVo) {
        LocalDateTime now = LocalDateTime.now();
        OrderPool entity = BeanUtil.copyProperties(orderPoolVo, OrderPool.class);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        orderPoolMapper.insert(entity);
        createOrderProcessData(entity, false);
        return selectOrderPoolById(entity.getOrderId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderPoolVo updateOrderPool(OrderPoolVo orderPoolVo) {
        if (orderPoolVo == null || StringUtils.isBlank(orderPoolVo.getOrderId())) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        OrderPool entity = BeanUtil.copyProperties(orderPoolVo, OrderPool.class);
        entity.setUpdatedAt(now);
        orderPoolMapper.updateById(entity);
        return selectOrderPoolById(orderPoolVo.getOrderId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOrderPoolByIds(String[] orderIds) {
        List<String> ids = normalizeOrderIds(orderIds);
        if (ids.isEmpty()) {
            return 0;
        }
        clearOrderProcessesByIds(ids.toArray(new String[0]));
        return orderPoolMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int clearOrderProcessesByIds(String[] orderIds) {
        List<String> ids = normalizeOrderIds(orderIds);
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
    public List<ProductionFlowVo> selectProductionFlowList(ProductionFlowQuery query) {
        LambdaQueryWrapper<ProductionFlow> wrapper = Wrappers.lambdaQuery();
        if (query != null) {
            if (StringUtils.isNotBlank(query.getKeyword())) {
                String keyword = query.getKeyword();
                wrapper.and(w -> w.like(ProductionFlow::getFlowId, keyword)
                    .or().like(ProductionFlow::getAssignedOperator, keyword));
            }
            wrapper.eq(StringUtils.isNotBlank(query.getStatus()), ProductionFlow::getFlowStatus, query.getStatus());
            wrapper.eq(StringUtils.isNotBlank(query.getPriority()), ProductionFlow::getPriority, query.getPriority());
            wrapper.eq(StringUtils.isNotBlank(query.getTemplateId()), ProductionFlow::getTemplateId, query.getTemplateId());
        }
        wrapper.orderByDesc(ProductionFlow::getCreatedAt);
        List<ProductionFlow> flows = productionFlowMapper.selectList(wrapper);
        return attachFlowDetails(flows);
    }

    @Override
    public ProductionFlowVo selectProductionFlowById(String flowId) {
        if (StringUtils.isBlank(flowId)) {
            return null;
        }
        ProductionFlow flow = productionFlowMapper.selectById(flowId);
        if (flow == null) {
            return null;
        }
        Map<String, ProductionFlowVo> flowMap = new HashMap<>();
        ProductionFlowVo vo = BeanUtil.copyProperties(flow, ProductionFlowVo.class);
        flowMap.put(flowId, vo);
        fillFlowRelations(Collections.singleton(flowId), flowMap);
        applyTemplateData(flowMap.values());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductionFlowVo insertProductionFlow(ProductionFlowVo productionFlowVo) {
        LocalDateTime now = LocalDateTime.now();
        ProductionFlow entity = BeanUtil.copyProperties(productionFlowVo, ProductionFlow.class);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        productionFlowMapper.insert(entity);
        Set<String> orderIds = resolveOrderIds(productionFlowVo);
        productionFlowVo.setOrderIds(new ArrayList<>(orderIds));
        saveFlowRelations(entity.getFlowId(), productionFlowVo);
        syncFlowTemplateToOrders(entity, orderIds, now);
        refreshFlowOrderStatuses(Collections.singleton(entity.getFlowId()));
        updateOrdersStatus(orderIds, "已入池", now);
        return selectProductionFlowById(entity.getFlowId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductionFlowVo updateProductionFlow(ProductionFlowVo productionFlowVo) {
        if (productionFlowVo == null || StringUtils.isBlank(productionFlowVo.getFlowId())) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        ProductionFlow entity = BeanUtil.copyProperties(productionFlowVo, ProductionFlow.class);
        entity.setUpdatedAt(now);
        productionFlowMapper.updateById(entity);

        Map<String, List<ProductionFlowOrderRel>> previousRelations = loadFlowOrderRelations(Collections.singleton(entity.getFlowId()));
        Set<String> previousOrderIds = extractOrderIds(previousRelations.get(entity.getFlowId()));

        clearFlowRelations(entity.getFlowId());
        saveFlowRelations(entity.getFlowId(), productionFlowVo);

        Set<String> newOrderIds = resolveOrderIds(productionFlowVo);
        productionFlowVo.setOrderIds(new ArrayList<>(newOrderIds));
        syncFlowTemplateToOrders(entity, newOrderIds, now);

        refreshFlowOrderStatuses(Collections.singleton(entity.getFlowId()));
        updateOrdersStatus(newOrderIds, "已入池", now);
        previousOrderIds.removeAll(newOrderIds);
        updateOrdersStatus(previousOrderIds, "待处理", now);
        return selectProductionFlowById(entity.getFlowId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProductionFlowByIds(String[] flowIds) {
        if (flowIds == null || flowIds.length == 0) {
            return 0;
        }
        List<String> ids = Arrays.stream(flowIds)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return 0;
        }
        Map<String, List<ProductionFlowOrderRel>> flowOrders = loadFlowOrderRelations(new HashSet<>(ids));
        productionFlowMaterialMapper.delete(Wrappers.<ProductionFlowMaterial>lambdaQuery()
            .in(ProductionFlowMaterial::getFlowId, ids));
        productionFlowOrderRelMapper.delete(Wrappers.<ProductionFlowOrderRel>lambdaQuery()
            .in(ProductionFlowOrderRel::getFlowId, ids));
        productionFlowStepMapper.delete(Wrappers.<ProductionFlowStep>lambdaQuery()
            .in(ProductionFlowStep::getFlowId, ids));
        int rows = productionFlowMapper.deleteBatchIds(ids);
        Set<String> orderIds = flowOrders.values().stream()
            .flatMap(list -> extractOrderIds(list).stream())
            .collect(Collectors.toSet());
        if (!orderIds.isEmpty()) {
            updateOrdersStatus(orderIds, "待处理", LocalDateTime.now());
        }
        return rows;
    }

    private List<OrderPoolVo> attachTemplateData(List<OrderPool> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return Collections.emptyList();
        }
        List<OrderPoolVo> result = orders.stream()
            .map(order -> BeanUtil.copyProperties(order, OrderPoolVo.class))
            .collect(Collectors.toList());
        applyTemplateData(result);
        applyOrderProcessData(result);
        return result;
    }

    private void applyOrderProcessData(List<OrderPoolVo> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        Set<String> orderIds = orders.stream()
            .map(OrderPool::getOrderId)
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

        orders.forEach(order -> {
            BrtOrderTemplateVo template = templateMap.getOrDefault(order.getOrderId(), Collections.emptyList())
                .stream()
                .filter(item -> StringUtils.isBlank(order.getTemplateId()) || StringUtils.equals(order.getTemplateId(), item.getTemplateId()))
                .findFirst()
                .orElseGet(() -> templateMap.getOrDefault(order.getOrderId(), Collections.emptyList()).stream().findFirst().orElse(null));
            order.setOrderTemplate(template);
            order.setOrderNodes(new ArrayList<>(nodeMap.getOrDefault(order.getOrderId(), Collections.emptyList())));
        });
    }

    private List<ProductionFlowVo> attachFlowDetails(List<ProductionFlow> flows) {
        if (CollectionUtils.isEmpty(flows)) {
            return Collections.emptyList();
        }
        Map<String, ProductionFlowVo> flowMap = flows.stream()
            .map(flow -> BeanUtil.copyProperties(flow, ProductionFlowVo.class))
            .collect(Collectors.toMap(ProductionFlow::getFlowId, vo -> vo));
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

    private void fillFlowRelations(Set<String> flowIds, Map<String, ProductionFlowVo> flowMap) {
        if (CollectionUtils.isEmpty(flowIds) || CollectionUtils.isEmpty(flowMap)) {
            return;
        }
        Map<String, List<ProductionFlowMaterial>> materialMap = loadFlowMaterials(flowIds);
        Map<String, List<ProductionFlowOrderRel>> orderMap = loadFlowOrderRelations(flowIds);
        Map<String, List<ProductionFlowStep>> stepMap = loadFlowSteps(flowIds);
        flowMap.forEach((flowId, vo) -> {
            List<ProductionFlowOrderRel> relations = new ArrayList<>(orderMap.getOrDefault(flowId, Collections.emptyList()));
            vo.setMaterialsSummary(new ArrayList<>(materialMap.getOrDefault(flowId, Collections.emptyList())));
            vo.setOrderAllocations(relations);
            vo.setOrderIds(relations.stream()
                .map(ProductionFlowOrderRel::getOrderId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()));
            vo.setProcess(new ArrayList<>(stepMap.getOrDefault(flowId, Collections.emptyList())));
        });
    }

    private Map<String, List<ProductionFlowMaterial>> loadFlowMaterials(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Collections.emptyMap();
        }
        List<ProductionFlowMaterial> materials = productionFlowMaterialMapper.selectList(Wrappers.<ProductionFlowMaterial>lambdaQuery()
            .in(ProductionFlowMaterial::getFlowId, flowIds)
            .orderByAsc(ProductionFlowMaterial::getSortOrder)
            .orderByAsc(ProductionFlowMaterial::getMaterialId));
        return materials.stream().collect(Collectors.groupingBy(ProductionFlowMaterial::getFlowId));
    }

    private Map<String, List<ProductionFlowOrderRel>> loadFlowOrderRelations(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Collections.emptyMap();
        }
        List<ProductionFlowOrderRel> relations = productionFlowOrderRelMapper.selectList(Wrappers.<ProductionFlowOrderRel>lambdaQuery()
            .in(ProductionFlowOrderRel::getFlowId, flowIds));
        applyOrderStatusFromProcesses(relations);
        return relations.stream().collect(Collectors.groupingBy(ProductionFlowOrderRel::getFlowId));
    }

    private void refreshFlowOrderStatuses(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return;
        }
        loadFlowOrderRelations(flowIds);
    }

    private void applyOrderStatusFromProcesses(List<ProductionFlowOrderRel> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return;
        }
        Set<String> orderIds = relations.stream()
            .map(ProductionFlowOrderRel::getOrderId)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
        Map<String, String> statusMap = resolveOrderProcessStatuses(orderIds);
        relations.forEach(rel -> {
            String targetStatus = statusMap.getOrDefault(rel.getOrderId(), "pending");
            if (!StringUtils.equals(targetStatus, rel.getStatus())) {
                rel.setStatus(targetStatus);
                productionFlowOrderRelMapper.update(null, Wrappers.<ProductionFlowOrderRel>lambdaUpdate()
                    .eq(ProductionFlowOrderRel::getId, rel.getId())
                    .set(ProductionFlowOrderRel::getStatus, targetStatus));
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

    private void saveFlowRelations(String flowId, ProductionFlowVo flowVo) {
        saveMaterials(flowId, flowVo.getMaterialsSummary());
        saveOrderRelations(flowId, flowVo);
        saveSteps(flowId, flowVo.getProcess());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyFlowTemplates(String flowId, Collection<String> orderIds) {
        if (StringUtils.isBlank(flowId) || orderIds == null) {
            return false;
        }
        ProductionFlow flow = productionFlowMapper.selectById(flowId);
        if (flow == null) {
            return false;
        }
        Set<String> normalizedOrderIds = new HashSet<>();
        orderIds.forEach(id -> {
            if (StringUtils.isNotBlank(id)) {
                normalizedOrderIds.add(id.trim());
            }
        });
        if (normalizedOrderIds.isEmpty()) {
            return false;
        }
        syncFlowTemplateToOrders(flow, normalizedOrderIds, LocalDateTime.now(), true);
        return true;
    }

    private void syncFlowTemplateToOrders(ProductionFlow flow, Set<String> orderIds, LocalDateTime now) {
        syncFlowTemplateToOrders(flow, orderIds, now, false);
    }

    private void syncFlowTemplateToOrders(ProductionFlow flow, Set<String> orderIds, LocalDateTime now,
        boolean forceCreateProcesses) {
        if (flow == null || StringUtils.isBlank(flow.getTemplateId()) || CollectionUtils.isEmpty(orderIds)) {
            return;
        }
        List<OrderPool> orders = orderPoolMapper.selectList(Wrappers.<OrderPool>lambdaQuery()
            .in(OrderPool::getOrderId, orderIds));
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        orders.forEach(order -> {
            order.setTemplateId(flow.getTemplateId());
            order.setUpdatedAt(now);
            orderPoolMapper.updateById(order);
            createOrderProcessData(order, forceCreateProcesses);
        });
    }

    private void createOrderProcessData(OrderPool orderPool, boolean forceCreateProcesses) {
        if (orderPool == null || StringUtils.isBlank(orderPool.getOrderId()) || StringUtils.isBlank(orderPool.getTemplateId())) {
            return;
        }
        List<BrtOrderTemplate> existingTemplates = orderTemplateService.list(Wrappers.<BrtOrderTemplate>lambdaQuery()
            .eq(BrtOrderTemplate::getOrderId, orderPool.getOrderId())
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
            orderTemplate.setOrderId(orderPool.getOrderId());
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
            .eq(BrtOrderNode::getOrderId, orderPool.getOrderId())
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
            orderNode.setOrderId(orderPool.getOrderId());
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
        productionFlowMaterialMapper.delete(Wrappers.<ProductionFlowMaterial>lambdaQuery()
            .eq(ProductionFlowMaterial::getFlowId, flowId));
        productionFlowOrderRelMapper.delete(Wrappers.<ProductionFlowOrderRel>lambdaQuery()
            .eq(ProductionFlowOrderRel::getFlowId, flowId));
        productionFlowStepMapper.delete(Wrappers.<ProductionFlowStep>lambdaQuery()
            .eq(ProductionFlowStep::getFlowId, flowId));
    }

    private List<String> normalizeOrderIds(String[] orderIds) {
        if (orderIds == null || orderIds.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(orderIds)
            .filter(StringUtils::isNotBlank)
            .flatMap(id -> Arrays.stream(id.split(",")))
            .map(String::trim)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
    }

    private Set<String> resolveOrderIds(ProductionFlowVo flowVo) {
        Set<String> result = new HashSet<>();
        if (flowVo == null) {
            return result;
        }
        if (!CollectionUtils.isEmpty(flowVo.getOrderAllocations())) {
            flowVo.getOrderAllocations().stream()
                .filter(Objects::nonNull)
                .map(ProductionFlowOrderRel::getOrderId)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .forEach(result::add);
        }
        if (!CollectionUtils.isEmpty(flowVo.getOrderIds())) {
            flowVo.getOrderIds().stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .forEach(result::add);
        }
        return result;
    }

    private Set<String> extractOrderIds(Collection<ProductionFlowOrderRel> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return new HashSet<>();
        }
        return relations.stream()
            .filter(Objects::nonNull)
            .map(ProductionFlowOrderRel::getOrderId)
            .filter(StringUtils::isNotBlank)
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    private Map<String, Integer> aggregateOrderQuantities(ProductionFlowVo flowVo) {
        Map<String, Integer> result = new LinkedHashMap<>();
        if (flowVo == null) {
            return result;
        }
        if (!CollectionUtils.isEmpty(flowVo.getOrderAllocations())) {
            flowVo.getOrderAllocations().stream()
                .filter(Objects::nonNull)
                .filter(rel -> StringUtils.isNotBlank(rel.getOrderId()))
                .forEach(rel -> result.merge(rel.getOrderId().trim(),
                    rel.getQuantity(),
                    (oldVal, newVal) -> (oldVal == null ? 0 : oldVal) + (newVal == null ? 0 : newVal)));
        }
        if (result.isEmpty() && !CollectionUtils.isEmpty(flowVo.getOrderIds())) {
            flowVo.getOrderIds().stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .forEach(orderId -> result.put(orderId, null));
        }
        return result;
    }

    private void saveMaterials(String flowId, List<ProductionFlowMaterial> materials) {
        if (StringUtils.isBlank(flowId) || CollectionUtils.isEmpty(materials)) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        materials.stream()
            .filter(material -> material != null && (StringUtils.isNotBlank(material.getMaterial())
                || material.getQuantity() != null))
            .map(material -> new ProductionFlowMaterial()
                .setMaterialId(null)
                .setFlowId(flowId)
                .setMaterial(material.getMaterial())
                .setQuantity(material.getQuantity())
                .setSortOrder(material.getSortOrder() != null ? material.getSortOrder() : index.getAndIncrement()))
            .forEach(productionFlowMaterialMapper::insert);
    }

    private void saveOrderRelations(String flowId, ProductionFlowVo flowVo) {
        if (StringUtils.isBlank(flowId) || flowVo == null) {
            return;
        }
        Map<String, Integer> quantityMap = aggregateOrderQuantities(flowVo);
        Map<String, String> statusMap = resolveOrderProcessStatuses(quantityMap.keySet());
        if (quantityMap.isEmpty()) {
            return;
        }
        quantityMap.forEach((orderId, quantity) -> productionFlowOrderRelMapper.insert(new ProductionFlowOrderRel()
            .setId(null)
            .setFlowId(flowId)
            .setOrderId(orderId)
            .setQuantity(quantity == null ? 0 : quantity)
            .setStatus(statusMap.getOrDefault(orderId, "pending"))));
    }

    private void saveSteps(String flowId, List<ProductionFlowStep> steps) {
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
                return new ProductionFlowStep()
                    .setStepId(null)
                    .setFlowId(flowId)
                    .setNodeId(step.getNodeId())
                    .setStepName(stepName)
                    .setStepStatus(StringUtils.isNotBlank(step.getStepStatus()) ? step.getStepStatus() : "pending")
                    .setRemark(step.getRemark())
                    .setSortOrder(step.getSortOrder() != null ? step.getSortOrder() : position)
                    .setUpdatedAt(now);
            })
            .forEach(productionFlowStepMapper::insert);
    }

    private Map<String, List<ProductionFlowStep>> loadFlowSteps(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Collections.emptyMap();
        }
        List<ProductionFlowStep> steps = productionFlowStepMapper.selectList(Wrappers.<ProductionFlowStep>lambdaQuery()
            .in(ProductionFlowStep::getFlowId, flowIds)
            .orderByAsc(ProductionFlowStep::getSortOrder)
            .orderByAsc(ProductionFlowStep::getStepId));
        return steps.stream().collect(Collectors.groupingBy(ProductionFlowStep::getFlowId));
    }

    private void updateOrdersStatus(Collection<String> orderIds, String status, LocalDateTime now) {
        if (CollectionUtils.isEmpty(orderIds) || StringUtils.isBlank(status)) {
            return;
        }
        LambdaUpdateWrapper<OrderPool> updateWrapper = Wrappers.<OrderPool>lambdaUpdate()
            .in(OrderPool::getOrderId, orderIds)
            .set(OrderPool::getOrderStatus, status)
            .set(OrderPool::getUpdatedAt, now);
        orderPoolMapper.update(null, updateWrapper);
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
                if (record instanceof OrderPool) {
                    return ((OrderPool) record).getTemplateId();
                }
                if (record instanceof ProductionFlow) {
                    return ((ProductionFlow) record).getTemplateId();
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
            if (record instanceof OrderPoolVo) {
                OrderPoolVo order = (OrderPoolVo) record;
                order.setFlowTemplate(templateMap.get(order.getTemplateId()));
            } else if (record instanceof ProductionFlowVo) {
                ProductionFlowVo flow = (ProductionFlowVo) record;
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
