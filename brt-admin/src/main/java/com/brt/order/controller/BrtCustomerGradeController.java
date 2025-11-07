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
import com.brt.order.vo.BrtCustomerGradeVo;
import com.brt.order.service.IBrtCustomerGradeService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 客户等级Controller
 *
 * @author Fgn
 * @date 2024-04-27
 */
@RestController
@RequestMapping("/order/customerGrade")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtCustomerGradeController extends BaseController {

    private final IBrtCustomerGradeService brtCustomerGradeService;

    /**
     * @description: TODO 分页查询客户等级列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtCustomerGradeVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerGrade:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtCustomerGradeVo> list(BrtCustomerGradeVo brtCustomerGradeVo) {
        return brtCustomerGradeService.queryBrtCustomerGradeList(brtCustomerGradeVo);
    }

    /**
     * @description: TODO 查询全部客户等级列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerGrade:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtCustomerGradeVo brtCustomerGradeVo) {
        return AjaxResult.success("查询成功", brtCustomerGradeService.queryBrtCustomerGradeAll(brtCustomerGradeVo));
    }

    /**
     * @description: TODO 导出客户等级列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtCustomerGradeVo 客户等级
     * @return:
//     **/
//    @PreAuthorize("@ss.hasPermi('order:customerGrade:export')")
    @Log(title = "客户等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtCustomerGradeVo brtCustomerGradeVo){
        List<BrtCustomerGradeVo> list = brtCustomerGradeService.queryBrtCustomerGradeAll(brtCustomerGradeVo);
        ExcelUtil<BrtCustomerGradeVo> util = new ExcelUtil<BrtCustomerGradeVo>(BrtCustomerGradeVo.class);
        util.exportExcel(response, list, "客户等级数据");
    }


    /**
     * @description: TODO 获取客户等级详细信息
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerGrade:query')")
    @GetMapping(value = "/{gradeId}")
    public AjaxResult getInfo(@PathVariable("gradeId") String gradeId) {
        return success(brtCustomerGradeService.queryBrtCustomerGradeByGradeId(gradeId));
    }

    /**
     * @description: TODO 新增客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerGrade:add')")
    @Log(title = "客户等级", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtCustomerGradeVo brtCustomerGradeVo) {
        return AjaxResult.success(brtCustomerGradeService.insertBrtCustomerGrade(brtCustomerGradeVo));
    }

    /**
     * @description: TODO 修改客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:customerGrade:edit')")
    @Log(title = "客户等级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtCustomerGradeVo brtCustomerGradeVo) {
        return AjaxResult.success(brtCustomerGradeService.updateBrtCustomerGrade(brtCustomerGradeVo));
    }

    /**
     * @description: TODO 删除客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: gradeIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:customerGrade:remove')")
    @Log(title = "客户等级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gradeIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] gradeIds){
        return toAjax(brtCustomerGradeService.deleteBrtCustomerGradeByGradeIds(gradeIds));
    }

}
