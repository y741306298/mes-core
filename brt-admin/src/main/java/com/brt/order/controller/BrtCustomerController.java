package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.common.enums.BusinessType;
import com.brt.order.domain.BrtCustomer;
import com.brt.order.utils.OtherFieldsUtils;
import com.brt.order.vo.MarketRecordVo;
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
import com.brt.order.vo.BrtCustomerVo;
import com.brt.order.service.IBrtCustomerService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 客户信息Controller
 *
 * @author Fgn
 * @date 2024-04-27
 */
@RestController
@RequestMapping("/order/customer")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtCustomerController extends BaseController {

    private final IBrtCustomerService brtCustomerService;

    @Autowired
    private OtherFieldsUtils otherFieldsUtils;

    /**
     * @return com.brt.common.core.page.TableDataInfo<BrtCustomerVo>
     * @description: TODO 分页查询客户信息列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customer:AllList')")
    @GetMapping("/list")
    public TableDataInfo<BrtCustomerVo> list(BrtCustomerVo brtCustomerVo) {
        return brtCustomerService.queryBrtCustomerList(brtCustomerVo);
    }

    /**
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 查询全部客户信息列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customer:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtCustomerVo brtCustomerVo) {
        return AjaxResult.success("查询成功", brtCustomerService.queryBrtCustomerAll(brtCustomerVo));
    }

    /**
     * @description: TODO 导出客户信息列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCustomerVo 客户信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customer:export')")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtCustomerVo brtCustomerVo) {
        List<BrtCustomerVo> list = brtCustomerService.queryBrtCustomerAll(brtCustomerVo);
        list = otherFieldsUtils.rinse(list, BrtCustomerVo.class);
        ExcelUtil<BrtCustomerVo> util = new ExcelUtil<BrtCustomerVo>(BrtCustomerVo.class);
        util.exportExcel(response, list, "客户信息数据");
    }


    /**
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 获取客户信息详细信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customer:query')")
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable("customerId") String customerId) {
        return success(brtCustomerService.queryBrtCustomerByCustomerId(customerId));
    }

    /**
     * @return null
     * @description: TODO 新增客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customer:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtCustomerVo brtCustomerVo) {
        return AjaxResult.success(brtCustomerService.insertBrtCustomer(brtCustomerVo));
    }

    /**
     * @return null
     * @description: TODO 修改客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerVo 客户信息
     * @return:
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:customer:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtCustomerVo brtCustomerVo) {
        return AjaxResult.success(brtCustomerService.updateBrtCustomer(brtCustomerVo));
    }

    /**
     * @return null
     * @description: TODO 删除客户信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: customerIds
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:customer:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] customerIds) {
        return toAjax(brtCustomerService.deleteBrtCustomerByCustomerIds(customerIds));
    }

    /**
     * @param response
     * @description: TODO 下载模板
     * @author: FanGN
     * @date: 00:44 2024/4/28
     * @param:
     * @return:
     **/
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<BrtCustomerVo> util = new ExcelUtil<BrtCustomerVo>(BrtCustomerVo.class);
        util.importTemplateExcel(response, "客户数据");
    }

    /**
     * @param file
     * @param updateSupport
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 导入客户信息
     * @author: FanGN
     * @date: 00:49 2024/4/28
     * @param:
     * @return:
     **/
    @Log(title = "客户信息", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('order:customer:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<BrtCustomerVo> util = new ExcelUtil<BrtCustomerVo>(BrtCustomerVo.class);
        List<BrtCustomerVo> customerList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = brtCustomerService.importData(customerList, operName);
        return success(message);
    }

    /**
     * @return com.brt.common.core.domain.AjaxResult
     * @description: TODO 查询客户的销售记录
     * @param:
     * @return:
     **/
    @GetMapping("/queryMarketRecord")
    public TableDataInfo<MarketRecordVo> queryMarketRecord(MarketRecordVo marketRecordVo) throws Exception {
        return brtCustomerService.queryMarketRecord(marketRecordVo);
    }

}
