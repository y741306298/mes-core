package com.brt.productionflow.service;

import com.brt.productionflow.domain.ProdDevice;
import com.brt.productionflow.vo.ProdDeviceVo;

import java.util.List;

/**
 * 生产设备业务接口
 */
public interface IProdDeviceService {

    /**
     * 查询设备列表
     *
     * @param prodDevice 查询条件
     * @return 设备集合
     */
    List<ProdDeviceVo> selectProdDeviceList(ProdDevice prodDevice);

    /**
     * 查询设备详情
     *
     * @param deviceId 设备ID
     * @return 设备信息
     */
    ProdDeviceVo selectProdDeviceById(String deviceId);

    /**
     * 新增设备
     *
     * @param prodDeviceVo 设备信息
     * @return 保存后的设备
     */
    ProdDeviceVo insertProdDevice(ProdDeviceVo prodDeviceVo);

    /**
     * 修改设备
     *
     * @param prodDeviceVo 设备信息
     * @return 修改后的设备
     */
    ProdDeviceVo updateProdDevice(ProdDeviceVo prodDeviceVo);

    /**
     * 删除设备
     *
     * @param deviceIds 设备ID集合
     * @return 删除数量
     */
    int deleteProdDeviceByIds(String[] deviceIds);
}
