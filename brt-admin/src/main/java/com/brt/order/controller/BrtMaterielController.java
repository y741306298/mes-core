package com.brt.order.controller;

import cn.hutool.core.util.ObjectUtil;
import com.brt.common.annotation.RepeatSubmit;
import com.brt.common.annotation.RequestSingleParam;
import com.brt.common.enums.BusinessType;
import com.brt.order.vo.BrtCustomerVo;
import com.brt.order.vo.BrtMaterielRecordVo;
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
import com.brt.order.vo.BrtMaterielVo;
import com.brt.order.service.IBrtMaterielService;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 物料信息Controller
 *
 * @author Fgn
 * @date 2024-05-07
 */
@RestController
@RequestMapping("/order/materiel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtMaterielController extends BaseController {

    private final IBrtMaterielService brtMaterielService;

    /**
     * @description: TODO 分页查询物料信息列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<BrtMaterielVo>
     **/
//    @PreAuthorize("@ss.hasPermi('order:materiel:list')")
    @GetMapping("/list")
    public TableDataInfo<BrtMaterielVo> list(BrtMaterielVo brtMaterielVo) {
        return brtMaterielService.queryBrtMaterielList(brtMaterielVo);
    }

    /**
     * @description: TODO 查询全部物料信息列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:materiel:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BrtMaterielVo brtMaterielVo) {
        return AjaxResult.success("查询成功", brtMaterielService.queryBrtMaterielAll(brtMaterielVo));
    }

    /**
     * @description: TODO 导出物料信息列表
     * @author: FanGN
     * @date: 10:48 2023/6/12
     * @param:
     * @param: response
     * @param: brtMaterielVo 物料信息
     * @return:
     **/
//    @PreAuthorize("@ss.hasPermi('order:materiel:export')")
    @Log(title = "物料信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RepeatSubmit
    public void export(HttpServletResponse response, BrtMaterielVo brtMaterielVo){
        List<BrtMaterielVo> list = brtMaterielService.queryBrtMaterielAll(brtMaterielVo);

        ExcelUtil<BrtMaterielVo> util = new ExcelUtil<BrtMaterielVo>(BrtMaterielVo.class);
        util.exportExcel(response, list, "物料信息数据");
    }


    /**
     * @description: TODO 获取物料信息详细信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:materiel:query')")
    @GetMapping(value = "/{materielId}")
    public AjaxResult getInfo(@PathVariable("materielId") String materielId) {
        return success(brtMaterielService.queryBrtMaterielByMaterielId(materielId));
    }

    /**
     * @description: TODO 新增物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:materiel:add')")
    @Log(title = "物料信息", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BrtMaterielVo brtMaterielVo) {
        return AjaxResult.success(brtMaterielService.insertBrtMateriel(brtMaterielVo));
    }

    /**
     * @description: TODO 修改物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return null
     **/
    @RepeatSubmit
//    @PreAuthorize("@ss.hasPermi('order:materiel:edit')")
    @Log(title = "物料信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BrtMaterielVo brtMaterielVo) {
        return AjaxResult.success(brtMaterielService.updateBrtMateriel(brtMaterielVo));
    }

    /**
     * @description: TODO 删除物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: materielIds
     * @return:
     * @return null
     **/
//    @PreAuthorize("@ss.hasPermi('order:materiel:remove')")
    @Log(title = "物料信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{materielIds}")
    @RepeatSubmit
    public AjaxResult remove(@PathVariable String[] materielIds){
        return toAjax(brtMaterielService.deleteBrtMaterielByMaterielIds(materielIds));
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
        ExcelUtil<BrtMaterielVo> util = new ExcelUtil<BrtMaterielVo>(BrtMaterielVo.class);
        util.importTemplateExcel(response, "物料数据");
    }

    /**
     * @description: TODO 导入物料信息
     * @author: FanGN
     * @date: 00:49 2024/4/28
     * @param:
     * @param file
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
    @Log(title = "物料信息", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('order:materiel:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<BrtMaterielVo> util = new ExcelUtil<BrtMaterielVo>(BrtMaterielVo.class);
        List<BrtMaterielVo> materielVoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = brtMaterielService.importData(materielVoList, operName);
        return success(message);
    }

    /**
     * @description: TODO 设置库存预警数量
     * @author: FanGN
     * @date: 22:32 2024/5/18
     * @param:
     * @param materielIds
     * @return:
     * @return com.brt.common.core.domain.AjaxResult
     **/
//    @PreAuthorize("@ss.hasPermi('order:materiel:warning')")
    @Log(title = "物料信息", businessType = BusinessType.UPDATE)
    @PostMapping("warning")
    @RepeatSubmit
    public AjaxResult warning(@RequestSingleParam("materielIds") String materielIds,
                              @RequestSingleParam("warningNum") String warningNum) {
        return AjaxResult.success(brtMaterielService.warning(materielIds,warningNum));
    }

    /**
     * 查询出入库记录
     * @param materielRecordVo
     * @return
     */
    @PostMapping("selectRecord")
    public AjaxResult selectRecord(@RequestBody BrtMaterielRecordVo materielRecordVo){
        return AjaxResult.success(brtMaterielService.selectRecord(materielRecordVo));
    }
}
