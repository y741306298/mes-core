package com.brt.order.controller;

import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtOutInventoryVo;
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
import com.brt.order.vo.BrtOutInventoryMaterielVo;
import com.brt.order.service.IBrtOutInventoryMaterielService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;

/**
 * 入库单详情 入库单关联物料Controller
 *
 * @author Fgn
 * @date 2024-07-11
 */
@RestController
@RequestMapping("/order/outInventoryMateriel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtOutInventoryMaterielController extends BaseController {

    private final IBrtOutInventoryMaterielService brtOutInventoryMaterielService;

    /**
     * @description: TODO 分页查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtOutInventoryMaterielVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventoryMateriel:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtOutInventoryMaterielVo> list(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        return brtOutInventoryMaterielService.queryBrtOutInventoryMaterielList(brtOutInventoryMaterielVo);
    }

    /**
     * @description: TODO 查询全部入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventoryMateriel:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        return AjaxResult.success("查询成功", brtOutInventoryMaterielService.queryBrtOutInventoryMaterielAll(brtOutInventoryMaterielVo));
    }

    /**
     * @description: TODO 导出入库单详情 入库单关联物料列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventoryMateriel:export')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtOutInventoryMaterielVo brtOutInventoryMaterielVo){
        List<BrtOutInventoryMaterielVo> list = brtOutInventoryMaterielService.queryBrtOutInventoryMaterielAll(brtOutInventoryMaterielVo);
        ExcelUtil<BrtOutInventoryMaterielVo> util = new ExcelUtil<BrtOutInventoryMaterielVo>(BrtOutInventoryMaterielVo.class);
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
        ExcelUtil<BrtOutInventoryMaterielVo> util = new ExcelUtil<BrtOutInventoryMaterielVo>(BrtOutInventoryMaterielVo.class);
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
//    @PreAuthorize("@ss.hasPermi('order:outInventoryMateriel:query')")
    @GetMapping(value = "/{outInventoryMaterielId}")
    public AjaxResult getInfo(@PathVariable("outInventoryMaterielId") String outInventoryMaterielId) {
        return success(brtOutInventoryMaterielService.queryBrtOutInventoryMaterielByOutInventoryMaterielId(outInventoryMaterielId));
    }

    /**
     * @description: TODO 新增入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventoryMateriel:add')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        return AjaxResult.success(brtOutInventoryMaterielService.insertBrtOutInventoryMateriel(brtOutInventoryMaterielVo));
    }

    /**
     * @description: TODO 修改入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:outInventoryMateriel:edit')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        return AjaxResult.success(brtOutInventoryMaterielService.updateBrtOutInventoryMateriel(brtOutInventoryMaterielVo));
    }

    /**
     * @description: TODO 删除入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: outInventoryMaterielIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:outInventoryMateriel:remove')")
    @Log(title = "入库单详情 入库单关联物料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{outInventoryMaterielIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] outInventoryMaterielIds){
        return toAjax(brtOutInventoryMaterielService.deleteBrtOutInventoryMaterielByOutInventoryMaterielIds(outInventoryMaterielIds));
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
        List<BrtOutInventoryMaterielVo> outInventoryMaterielVos = brtOutInventoryMaterielService.getFileData(fileUrl);
        return success(outInventoryMaterielVos);
    }

    /**
     * @description: TODO 根据outInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    @PostMapping("/queryByOutInventoryId")
    public AjaxResult queryByOutInventoryId(@RequestSingleParam("outInventoryId") String outInventoryId){
        return AjaxResult.success(brtOutInventoryMaterielService.queryByOutInventoryId(outInventoryId));
    }

}
