package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtInInventoryVo;
import com.brt.order.vo.BrtOrderBoomVo;
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
import com.brt.order.vo.BrtInInventoryMaterielVo;
import com.brt.order.service.IBrtInInventoryMaterielService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 入库单详情 入库单关联物料Controller
 *
 * @author Fgn
 * @date 2024-07-11
 */
@RestController
@RequestMapping("/order/inInventoryMateriel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtInInventoryMaterielController extends BaseController {

    private final IBrtInInventoryMaterielService brtInInventoryMaterielService;

    /**
     * @description: TODO 分页查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtInInventoryMaterielVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventoryMateriel:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtInInventoryMaterielVo> list(BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        return brtInInventoryMaterielService.queryBrtInInventoryMaterielList(brtInInventoryMaterielVo);
    }

    /**
     * @description: TODO 查询全部入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventoryMateriel:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        return AjaxResult.success("查询成功", brtInInventoryMaterielService.queryBrtInInventoryMaterielAll(brtInInventoryMaterielVo));
    }

    /**
     * @description: TODO 导出入库单详情 入库单关联物料列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventoryMateriel:export')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtInInventoryMaterielVo brtInInventoryMaterielVo){
        List<BrtInInventoryMaterielVo> list = brtInInventoryMaterielService.queryBrtInInventoryMaterielAll(brtInInventoryMaterielVo);
        ExcelUtil<BrtInInventoryMaterielVo> util = new ExcelUtil<BrtInInventoryMaterielVo>(BrtInInventoryMaterielVo.class);
        util.exportExcel(response, list, "入库单详情 入库单关联物料数据");
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
        ExcelUtil<BrtInInventoryMaterielVo> util = new ExcelUtil<BrtInInventoryMaterielVo>(BrtInInventoryMaterielVo.class);
        util.importTemplateExcel(response, "导入");
    }


    /**
     * @description: TODO 获取入库单详情 入库单关联物料详细信息
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventoryMateriel:query')")
    @GetMapping(value = "/{inInventoryMaterielId}")
    public AjaxResult getInfo(@PathVariable("inInventoryMaterielId") String inInventoryMaterielId) {
        return success(brtInInventoryMaterielService.queryBrtInInventoryMaterielByInInventoryMaterielId(inInventoryMaterielId));
    }

    /**
     * @description: TODO 新增入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventoryMateriel:add')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        return AjaxResult.success(brtInInventoryMaterielService.insertBrtInInventoryMateriel(brtInInventoryMaterielVo));
    }

    /**
     * @description: TODO 修改入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:inInventoryMateriel:edit')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        return AjaxResult.success(brtInInventoryMaterielService.updateBrtInInventoryMateriel(brtInInventoryMaterielVo));
    }

    /**
     * @description: TODO 删除入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: inInventoryMaterielIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:inInventoryMateriel:remove')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.DELETE)
	@DeleteMapping("/{inInventoryMaterielIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] inInventoryMaterielIds){
        return toAjax(brtInInventoryMaterielService.deleteBrtInInventoryMaterielByInInventoryMaterielIds(inInventoryMaterielIds));
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
        List<BrtInInventoryMaterielVo> inInventoryMaterielVos = brtInInventoryMaterielService.getFileData(fileUrl);
        return success(inInventoryMaterielVos);
    }

    /**
     * @description: TODO 根据outInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    @PostMapping("/queryByInInventoryId")
    public AjaxResult queryByInInventoryId(@RequestSingleParam("inInventoryId") String inInventoryId){
        return AjaxResult.success(brtInInventoryMaterielService.queryByInInventoryId(inInventoryId));
    }

}
