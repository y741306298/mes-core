package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtMaterielVo;
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
import com.brt.order.vo.BrtOrderBoomVo;
import com.brt.order.service.IBrtOrderBoomService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * boom单Controller
 *
 * @author Fgn
 * @date 2024-06-20
 */
@RestController
@RequestMapping("/order/orderBoom")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOrderBoomController extends BaseController {

    private final IBrtOrderBoomService brtOrderBoomService;

    /**
     * @description: TODO 分页查询boom单列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOrderBoomVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderBoom:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOrderBoomVo> list(BrtOrderBoomVo brtOrderBoomVo) {
        return brtOrderBoomService.queryBrtOrderBoomList(brtOrderBoomVo);
    }

    /**
     * @description: TODO 查询全部boom单列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderBoom:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOrderBoomVo brtOrderBoomVo) {
        return AjaxResult.success("查询成功", brtOrderBoomService.queryBrtOrderBoomAll(brtOrderBoomVo));
    }

    /**
     * @description: TODO 导出boom单列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOrderBoomVo boom单
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderBoom:export')")
    @Log(title = "boom单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOrderBoomVo brtOrderBoomVo){
        List<BrtOrderBoomVo> list = brtOrderBoomService.queryBrtOrderBoomAll(brtOrderBoomVo);
        ExcelUtil<BrtOrderBoomVo> util = new ExcelUtil<BrtOrderBoomVo>(BrtOrderBoomVo.class);
        util.exportExcel(response, list, "boom单数据");
    }


    /**
     * @description: TODO 获取boom单详细信息
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderBoom:query')")
    @GetMapping(value = "/{boomId}")
    public AjaxResult getInfo(@PathVariable("boomId") String boomId) {
        return success(brtOrderBoomService.queryBrtOrderBoomByBoomId(boomId));
    }

    /**
     * @description: TODO 新增boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderBoom:add')")
    @Log(title = "boom单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOrderBoomVo brtOrderBoomVo) {
        return AjaxResult.success(brtOrderBoomService.insertBrtOrderBoom(brtOrderBoomVo));
    }

    /**
     * @description: TODO 修改boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderBoomVo boom单
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:orderBoom:edit')")
    @Log(title = "boom单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOrderBoomVo brtOrderBoomVo) {
        return AjaxResult.success(brtOrderBoomService.updateBrtOrderBoom(brtOrderBoomVo));
    }

    /**
     * @description: TODO 删除boom单
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: boomIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:orderBoom:remove')")
    @Log(title = "boom单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{boomIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] boomIds){
        return toAjax(brtOrderBoomService.deleteBrtOrderBoomByBoomIds(boomIds));
    }

    /**
     * @description: TODO 下载模板
     * @author: FanGN
     * @date: 00:44 2024/4/28
     * @param:
     * @param response
     * @return:
     **/
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BrtOrderBoomVo> util = new ExcelUtil<BrtOrderBoomVo>(BrtOrderBoomVo.class);
        util.importTemplateExcel(response, "Boom单");
    }

    /**
     * @description: TODO 获取文件数据列表
     * @author: FanGN
     * @date: 00:49 2024/4/28
     * @param:
     * @param fileUrl
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @PostMapping("/getFileData")
    public AjaxResult getFileData(@RequestSingleParam("fileUrl") String fileUrl) throws Exception
    {
        List<BrtOrderBoomVo> orderBoomVoList = brtOrderBoomService.getFileData(fileUrl);
        return success(orderBoomVoList);
    }

}
