package com.brt.productionflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.brt.common.utils.StringUtils;
import com.brt.productionflow.domain.ProdDevice;
import com.brt.productionflow.domain.ProdDeviceHistory;
import com.brt.productionflow.mapper.ProdDeviceHistoryMapper;
import com.brt.productionflow.mapper.ProdDeviceMapper;
import com.brt.productionflow.service.IProdDeviceService;
import com.brt.productionflow.vo.ProdDeviceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 生产设备业务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProdDeviceServiceImpl implements IProdDeviceService {

    private final ProdDeviceMapper prodDeviceMapper;
    private final ProdDeviceHistoryMapper prodDeviceHistoryMapper;

    @Override
    public List<ProdDeviceVo> selectProdDeviceList(ProdDevice prodDevice) {
        LambdaQueryWrapper<ProdDevice> wrapper = buildQueryWrapper(prodDevice);
        List<ProdDevice> devices = prodDeviceMapper.selectList(wrapper);
        return attachHistory(devices);
    }

    @Override
    public ProdDeviceVo selectProdDeviceById(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }
        ProdDevice device = prodDeviceMapper.selectById(deviceId);
        if (device == null) {
            return null;
        }
        ProdDeviceVo vo = BeanUtil.copyProperties(device, ProdDeviceVo.class);
        vo.setHistory(loadHistory(Collections.singleton(deviceId))
                .getOrDefault(deviceId, Collections.emptyList()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProdDeviceVo insertProdDevice(ProdDeviceVo prodDeviceVo) {
        ProdDevice entity = BeanUtil.copyProperties(prodDeviceVo, ProdDevice.class);
        entity.setDeviceId(null);
        prodDeviceMapper.insert(entity);
        saveHistory(entity.getDeviceId(), prodDeviceVo.getHistory());
        return selectProdDeviceById(entity.getDeviceId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProdDeviceVo updateProdDevice(ProdDeviceVo prodDeviceVo) {
        if (StringUtils.isBlank(prodDeviceVo.getDeviceId())) {
            return null;
        }
        ProdDevice entity = BeanUtil.copyProperties(prodDeviceVo, ProdDevice.class);
        prodDeviceMapper.updateById(entity);
        // 先删除旧记录
        prodDeviceHistoryMapper.delete(Wrappers.<ProdDeviceHistory>lambdaQuery()
                .eq(ProdDeviceHistory::getDeviceId, prodDeviceVo.getDeviceId()));
        saveHistory(prodDeviceVo.getDeviceId(), prodDeviceVo.getHistory());
        return selectProdDeviceById(prodDeviceVo.getDeviceId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProdDeviceByIds(String[] deviceIds) {
        if (deviceIds == null || deviceIds.length == 0) {
            return 0;
        }
        List<String> ids = new ArrayList<>();
        Collections.addAll(ids, deviceIds);
        if (ids.isEmpty()) {
            return 0;
        }
        prodDeviceHistoryMapper.delete(Wrappers.<ProdDeviceHistory>lambdaQuery()
                .in(ProdDeviceHistory::getDeviceId, ids));
        return prodDeviceMapper.deleteBatchIds(ids);
    }

    private LambdaQueryWrapper<ProdDevice> buildQueryWrapper(ProdDevice prodDevice) {
        LambdaQueryWrapper<ProdDevice> wrapper = Wrappers.lambdaQuery();
        if (prodDevice == null) {
            return wrapper.orderByAsc(ProdDevice::getAssetNumber);
        }
        wrapper.like(StringUtils.isNotBlank(prodDevice.getAssetNumber()), ProdDevice::getAssetNumber, prodDevice.getAssetNumber())
                .like(StringUtils.isNotBlank(prodDevice.getDeviceName()), ProdDevice::getDeviceName, prodDevice.getDeviceName())
                .like(StringUtils.isNotBlank(prodDevice.getModel()), ProdDevice::getModel, prodDevice.getModel())
                .eq(StringUtils.isNotBlank(prodDevice.getCategory()), ProdDevice::getCategory, prodDevice.getCategory())
                .orderByAsc(ProdDevice::getAssetNumber);
        return wrapper;
    }

    private List<ProdDeviceVo> attachHistory(List<ProdDevice> devices) {
        if (CollectionUtils.isEmpty(devices)) {
            return Collections.emptyList();
        }
        List<ProdDeviceVo> result = devices.stream()
                .map(device -> BeanUtil.copyProperties(device, ProdDeviceVo.class))
                .collect(Collectors.toList());
        Set<String> deviceIds = result.stream()
                .map(ProdDevice::getDeviceId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        Map<String, List<ProdDeviceHistory>> historyMap = loadHistory(deviceIds);
        result.forEach(vo -> vo.setHistory(new ArrayList<>(historyMap.getOrDefault(vo.getDeviceId(), Collections.emptyList()))));
        return result;
    }

    private Map<String, List<ProdDeviceHistory>> loadHistory(Set<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        List<ProdDeviceHistory> histories = prodDeviceHistoryMapper.selectList(Wrappers.<ProdDeviceHistory>lambdaQuery()
                .in(ProdDeviceHistory::getDeviceId, deviceIds)
                .orderByDesc(ProdDeviceHistory::getMaintenanceDate)
                .orderByDesc(ProdDeviceHistory::getCreateTime));
        return histories.stream().collect(Collectors.groupingBy(ProdDeviceHistory::getDeviceId));
    }

    private void saveHistory(String deviceId, List<ProdDeviceHistory> histories) {
        List<ProdDeviceHistory> toSave = normalizeHistory(deviceId, histories);
        if (CollectionUtils.isEmpty(toSave)) {
            return;
        }
        toSave.forEach(prodDeviceHistoryMapper::insert);
    }

    private List<ProdDeviceHistory> normalizeHistory(String deviceId, List<ProdDeviceHistory> histories) {
        if (StringUtils.isBlank(deviceId) || CollectionUtils.isEmpty(histories)) {
            return Collections.emptyList();
        }
        return histories.stream()
                .filter(history -> history != null && (StringUtils.isNotBlank(history.getSymptom())
                        || StringUtils.isNotBlank(history.getCause())
                        || StringUtils.isNotBlank(history.getAction())
                        || StringUtils.isNotBlank(history.getDuration())
                        || StringUtils.isNotBlank(history.getPerson())
                        || history.getMaintenanceDate() != null))
                .map(history -> {
                    ProdDeviceHistory entity = BeanUtil.copyProperties(history, ProdDeviceHistory.class);
                    entity.setHistoryId(null);
                    entity.setDeviceId(deviceId);
                    return entity;
                })
                .collect(Collectors.toList());
    }
}
