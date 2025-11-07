package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.brt.common.annotation.Log;
import com.brt.common.core.controller.BaseController;
import com.brt.common.core.domain.AjaxResult;
import com.brt.order.vo.BrtCustomerAddressVo;
import com.brt.order.service.IBrtCustomerAddressService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 用户地址Controller
 *
 * @author Fgn
 * @date 2024-04-27
 */
@RestController
@RequestMapping("/order/customerAddress")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtCustomerAddressController extends BaseController {

    private final IBrtCustomerAddressService brtCustomerAddressService;

    /**
     * @description: TODO 分页查询用户地址列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtCustomerAddressVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerAddress:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtCustomerAddressVo> list(BrtCustomerAddressVo brtCustomerAddressVo) {
        return brtCustomerAddressService.queryBrtCustomerAddressList(brtCustomerAddressVo);
    }

    /**
     * @description: TODO 查询全部用户地址列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerAddress:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtCustomerAddressVo brtCustomerAddressVo) {
        return AjaxResult.success("查询成功", brtCustomerAddressService.queryBrtCustomerAddressAll(brtCustomerAddressVo));
    }

    /**
     * @description: TODO 导出用户地址列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerAddress:export')")
    @Log(title = "用户地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtCustomerAddressVo brtCustomerAddressVo){
        List<BrtCustomerAddressVo> list = brtCustomerAddressService.queryBrtCustomerAddressAll(brtCustomerAddressVo);
        ExcelUtil<BrtCustomerAddressVo> util = new ExcelUtil<BrtCustomerAddressVo>(BrtCustomerAddressVo.class);
        util.exportExcel(response, list, "用户地址数据");
    }


    /**
     * @description: TODO 获取用户地址详细信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerAddress:query')")
    @GetMapping(value = "/{addressId}")
    public AjaxResult getInfo(@PathVariable("addressId") String addressId) {
        return success(brtCustomerAddressService.queryBrtCustomerAddressByAddressId(addressId));
    }

    /**
     * @description: TODO 新增用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerAddress:add')")
    @Log(title = "用户地址", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtCustomerAddressVo brtCustomerAddressVo) {
        return AjaxResult.success(brtCustomerAddressService.insertBrtCustomerAddress(brtCustomerAddressVo));
    }

    /**
     * @description: TODO 修改用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerAddressVo 用户地址
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:customerAddress:edit')")
    @Log(title = "用户地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtCustomerAddressVo brtCustomerAddressVo) {
        return AjaxResult.success(brtCustomerAddressService.updateBrtCustomerAddress(brtCustomerAddressVo));
    }

    /**
     * @description: TODO 删除用户地址
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: addressIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerAddress:remove')")
    @Log(title = "用户地址", businessType = BusinessType.DELETE)
	@DeleteMapping("/{addressIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] addressIds){
        return toAjax(brtCustomerAddressService.deleteBrtCustomerAddressByAddressIds(addressIds));
    }

}
