package com.brt.productionflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.brt.common.utils.StringUtils;
import com.brt.order.service.IBrtFlowTemplateService;
import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.productionflow.domain.OrderPool;
import com.brt.productionflow.domain.ProductionFlow;
import com.brt.productionflow.domain.ProductionFlowMaterial;
import com.brt.productionflow.domain.ProductionFlowOrderRel;
import com.brt.productionflow.mapper.OrderPoolMapper;
import com.brt.productionflow.mapper.ProductionFlowMapper;
import com.brt.productionflow.mapper.ProductionFlowMaterialMapper;
import com.brt.productionflow.mapper.ProductionFlowOrderRelMapper;
import com.brt.productionflow.service.IOrderPoolService;
import com.brt.productionflow.vo.OrderPoolQuery;
import com.brt.productionflow.vo.OrderPoolVo;
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
import java.util.List;
import java.util.Map;
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
    private final IBrtFlowTemplateService flowTemplateService;

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
        if (orderIds == null || orderIds.length == 0) {
            return 0;
        }
        List<String> ids = Arrays.stream(orderIds)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return 0;
        }
        return orderPoolMapper.deleteBatchIds(ids);
    }

    @Override
    public List<ProductionFlowVo> selectProductionFlowList() {
        List<ProductionFlow> flows = productionFlowMapper.selectList(Wrappers.<ProductionFlow>lambdaQuery()
            .orderByDesc(ProductionFlow::getCreatedAt));
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
        saveFlowRelations(entity.getFlowId(), productionFlowVo);
        updateOrdersStatus(toSafeSet(productionFlowVo.getOrderIds()), "已入池", now);
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

        Map<String, List<String>> previousRelations = loadFlowOrderIds(Collections.singleton(entity.getFlowId()));
        Set<String> previousOrderIds = new HashSet<>(previousRelations.getOrDefault(entity.getFlowId(), Collections.emptyList()));

        clearFlowRelations(entity.getFlowId());
        saveFlowRelations(entity.getFlowId(), productionFlowVo);

        Set<String> newOrderIds = toSafeSet(productionFlowVo.getOrderIds());
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
        Map<String, List<String>> flowOrders = loadFlowOrderIds(new HashSet<>(ids));
        productionFlowMaterialMapper.delete(Wrappers.<ProductionFlowMaterial>lambdaQuery()
            .in(ProductionFlowMaterial::getFlowId, ids));
        productionFlowOrderRelMapper.delete(Wrappers.<ProductionFlowOrderRel>lambdaQuery()
            .in(ProductionFlowOrderRel::getFlowId, ids));
        int rows = productionFlowMapper.deleteBatchIds(ids);
        Set<String> orderIds = flowOrders.values().stream()
            .flatMap(List::stream)
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
        return result;
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
        Map<String, List<String>> orderMap = loadFlowOrderIds(flowIds);
        flowMap.forEach((flowId, vo) -> {
            vo.setMaterialsSummary(new ArrayList<>(materialMap.getOrDefault(flowId, Collections.emptyList())));
            vo.setOrderIds(new ArrayList<>(orderMap.getOrDefault(flowId, Collections.emptyList())));
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

    private Map<String, List<String>> loadFlowOrderIds(Set<String> flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Collections.emptyMap();
        }
        List<ProductionFlowOrderRel> relations = productionFlowOrderRelMapper.selectList(Wrappers.<ProductionFlowOrderRel>lambdaQuery()
            .in(ProductionFlowOrderRel::getFlowId, flowIds));
        Map<String, List<String>> result = new HashMap<>();
        relations.forEach(rel -> result.computeIfAbsent(rel.getFlowId(), key -> new ArrayList<>()).add(rel.getOrderId()));
        return result;
    }

    private void saveFlowRelations(String flowId, ProductionFlowVo flowVo) {
        saveMaterials(flowId, flowVo.getMaterialsSummary());
        saveOrderRelations(flowId, flowVo.getOrderIds());
    }

    private void clearFlowRelations(String flowId) {
        productionFlowMaterialMapper.delete(Wrappers.<ProductionFlowMaterial>lambdaQuery()
            .eq(ProductionFlowMaterial::getFlowId, flowId));
        productionFlowOrderRelMapper.delete(Wrappers.<ProductionFlowOrderRel>lambdaQuery()
            .eq(ProductionFlowOrderRel::getFlowId, flowId));
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

    private void saveOrderRelations(String flowId, List<String> orderIds) {
        if (StringUtils.isBlank(flowId) || CollectionUtils.isEmpty(orderIds)) {
            return;
        }
        orderIds.stream()
            .filter(StringUtils::isNotBlank)
            .map(orderId -> new ProductionFlowOrderRel()
                .setId(null)
                .setFlowId(flowId)
                .setOrderId(orderId))
            .forEach(productionFlowOrderRelMapper::insert);
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
