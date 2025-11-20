package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

}
