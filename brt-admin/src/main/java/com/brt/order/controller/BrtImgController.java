package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import com.brt.order.dto.SvgMattingCallbackRequest;
import com.brt.order.dto.SvgMattingCuttingRequest;
import com.brt.order.dto.SvgMattingRequest;
import com.brt.order.dto.SvgMattingResponse;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/img")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BrtImgController {

    private static final String callbackBaseUrl = "http://118.31.58.44:8080/order-process-server/img/";
    private static final String mattingBaseUrl = "http://101.132.41.254:9929/";

    @Qualifier("threadPoolTaskExecutor")
    private final Executor taskExecutor;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/svgMatting")
    public SvgMattingResponse svgMatting(@RequestBody SvgMattingRequest request) {
        log.info("接收到svgMatting请求: {}", request);
        request.setImgFileName("svg/test/yazi.png");
        request.setSvgFileName("svg/test/4.svg");
        request.setOssCode("photoai");
        request.setResultDir("svg/temp");
        request.setCallbackUrl(callbackBaseUrl + "svgMattingCallback");
        request.setSplit(false);
        submitAsyncRequest("img/split", request);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/svgMattingCutting")
    public SvgMattingResponse svgMattingCutting(@RequestBody SvgMattingCuttingRequest request) {
        log.info("接收到svgMattingCutting请求: {}", request);
        request.setImgFileName("svg/test/yazi.png");
        request.setSvgFileName("svg/test/yazi1.svg");
        request.setOssCode("photoai");
        request.setResultDir("svg/temp");
        request.setCallbackUrl(callbackBaseUrl + "svgMattingCallback");
        request.setSplit(false);
        request.setStartX(500);
        request.setStartY(500);
        request.setEndX(1000);
        request.setEndY(1000);
        submitAsyncRequest("img/cut", request);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/svgMattingCallback")
    public AjaxResult svgMattingCallback(@RequestBody SvgMattingCallbackRequest request) {
        log.info("svgMatting回调入参: {}", request);
        return AjaxResult.success();
    }

    @PostMapping("/svgMattingCuttingCallback")
    public AjaxResult svgMattingCuttingCallback(@RequestBody SvgMattingCallbackRequest request) {
        log.info("svgMattingCutting回调入参: {}", request);
        return AjaxResult.success();
    }

    private void submitAsyncRequest(String path, Object body) {
        taskExecutor.execute(() -> {
            try {
                restTemplate.postForEntity(mattingBaseUrl + path, body, Void.class);
            } catch (Exception ex) {
                log.error("调用抠图服务失败，path={}, body={}", path, body, ex);
            }
        });
    }
}
