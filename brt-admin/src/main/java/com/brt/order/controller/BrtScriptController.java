package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import com.brt.productionflow.vo.ProdApiQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/script/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtScriptController {

    @PostMapping("/first")
    public AjaxResult first(){
        return AjaxResult.success();
    }

    @PostMapping("/second")
    public AjaxResult second(){
        return AjaxResult.success();
    }

    @PostMapping("/flowFirst")
    public AjaxResult flowFirst(@RequestBody ProdApiQuery param){
        System.out.println(param);
        return AjaxResult.success();
    }

    @PostMapping("/flowSecond")
    public AjaxResult flowSecond(@RequestBody ProdApiQuery param){
        System.out.println(param);
        return AjaxResult.success();
    }

    @PostMapping("/flowThird")
    public AjaxResult flowThird(@RequestBody ProdApiQuery param){
        System.out.println(param);
        return AjaxResult.success();
    }

}
