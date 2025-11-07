package com.brt.productionflow.controller;

import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.enums.BusinessType;
import com.brt.productionflow.domain.ProdDevice;
import com.brt.productionflow.service.IProdDeviceService;
import com.brt.productionflow.vo.ProdDeviceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产设备管理
 */
@RestController
@RequestMapping("/productionflow/device")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProdDeviceController extends BaseController {

    private final IProdDeviceService prodDeviceService;

    /**
     * 查询设备列表
     */
    @GetMapping("/list")
    public AjaxResult list(ProdDevice prodDevice) {
        return AjaxResult.success(prodDeviceService.selectProdDeviceList(prodDevice));
    }

    /**
     * 查询设备详情
     */
    @GetMapping("/{deviceId}")
    public AjaxResult getInfo(@PathVariable String deviceId) {
        return AjaxResult.success(prodDeviceService.selectProdDeviceById(deviceId));
    }

    /**
     * 新增设备
     */
    @Log(title = "生产设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProdDeviceVo prodDeviceVo) {
        return AjaxResult.success(prodDeviceService.insertProdDevice(prodDeviceVo));
    }

    /**
     * 修改设备
     */
    @Log(title = "生产设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProdDeviceVo prodDeviceVo) {
        return AjaxResult.success(prodDeviceService.updateProdDevice(prodDeviceVo));
    }

    /**
     * 删除设备
     */
    @Log(title = "生产设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable String[] deviceIds) {
        return toAjax(prodDeviceService.deleteProdDeviceByIds(deviceIds));
    }
}
