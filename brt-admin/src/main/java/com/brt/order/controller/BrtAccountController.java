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
import com.brt.order.vo.BrtAccountVo;
import com.brt.order.service.IBrtAccountService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 账户类型Controller
 *
 * @author Fgn
 * @date 2024-05-15
 */
@RestController
@RequestMapping("/order/account")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtAccountController extends BaseController {

    private final IBrtAccountService brtAccountService;

    /**
     * @description: TODO 分页查询账户类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtAccountVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:account:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtAccountVo> list(BrtAccountVo brtAccountVo) {
        return brtAccountService.queryBrtAccountList(brtAccountVo);
    }

    /**
     * @description: TODO 查询全部账户类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:account:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtAccountVo brtAccountVo) {
        return AjaxResult.success("查询成功", brtAccountService.queryBrtAccountAll(brtAccountVo));
    }

    /**
     * @description: TODO 导出账户类型列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtAccountVo 账户类型
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:account:export')")
    @Log(title = "账户类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtAccountVo brtAccountVo){
        List<BrtAccountVo> list = brtAccountService.queryBrtAccountAll(brtAccountVo);
        ExcelUtil<BrtAccountVo> util = new ExcelUtil<BrtAccountVo>(BrtAccountVo.class);
        util.exportExcel(response, list, "账户类型数据");
    }


    /**
     * @description: TODO 获取账户类型详细信息
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:account:query')")
    @GetMapping(value = "/{accountId}")
    public AjaxResult getInfo(@PathVariable("accountId") String accountId) {
        return success(brtAccountService.queryBrtAccountByAccountId(accountId));
    }

    /**
     * @description: TODO 新增账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:account:add')")
    @Log(title = "账户类型", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtAccountVo brtAccountVo) {
        return AjaxResult.success(brtAccountService.insertBrtAccount(brtAccountVo));
    }

    /**
     * @description: TODO 修改账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:account:edit')")
    @Log(title = "账户类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtAccountVo brtAccountVo) {
        return AjaxResult.success(brtAccountService.updateBrtAccount(brtAccountVo));
    }

    /**
     * @description: TODO 删除账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: accountIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:account:remove')")
    @Log(title = "账户类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{accountIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] accountIds){
        return toAjax(brtAccountService.deleteBrtAccountByAccountIds(accountIds));
    }

}
