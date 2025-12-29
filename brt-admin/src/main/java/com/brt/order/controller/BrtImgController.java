package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import com.brt.productionflow.service.IOrderPoolService;
import com.brt.productionflow.vo.ProdApiQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/img")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrtImgController {

    private static String typesettingBaseUrl = "http://139.224.203.146.8080/";
    private static String mattingBaseUrl = "http://101.132.41.254:9929/";

    @PostMapping("/svgMatting")
    public AjaxResult svgMatting(@RequestBody String param){
        System.out.println(param);
        return AjaxResult.success();
    }

    @PostMapping("/svgMattingCutting")
    public AjaxResult svgMattingCutting(@RequestBody String param){
        System.out.println(param);
        return AjaxResult.success();
    }
    
}
