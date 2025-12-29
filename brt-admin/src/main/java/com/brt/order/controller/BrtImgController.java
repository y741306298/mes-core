package com.brt.order.controller;

import com.brt.common.core.domain.AjaxResult;
import com.alibaba.fastjson2.JSON;
import com.brt.order.domain.BrtCommonCallRecord;
import com.brt.order.dto.SvgMattingCallbackRequest;
import com.brt.order.dto.SvgMattingCuttingRequest;
import com.brt.order.dto.SvgMattingRequest;
import com.brt.order.dto.SvgMattingResponse;
import com.brt.order.service.IBrtCommonCallRecordService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private final IBrtCommonCallRecordService callRecordService;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/svgMatting")
    public SvgMattingResponse svgMatting(@RequestBody SvgMattingRequest request) {
        log.info("接收到svgMatting请求: {}", request);
        String recordId = IdWorker.getIdStr();
        request.setRecordId(recordId);
        applyDefaults(request, recordId, false);
        saveCallRecord("svgMatting", "img/split", request, recordId);
        submitAsyncRequest("img/split", request, recordId);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/svgMattingCutting")
    public SvgMattingResponse svgMattingCutting(@RequestBody SvgMattingCuttingRequest request) {
        log.info("接收到svgMattingCutting请求: {}", request);
        String recordId = IdWorker.getIdStr();
        request.setRecordId(recordId);
        applyDefaults(request, recordId, true);
        saveCallRecord("svgMattingCutting", "img/cut", request, recordId);
        submitAsyncRequest("img/cut", request, recordId);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/svgMattingCallback")
    public AjaxResult svgMattingCallback(
        @RequestBody SvgMattingCallbackRequest request, @RequestParam(value = "recordId", required = false) String recordId) {
        log.info("svgMatting回调入参: {}", request);
        updateCallRecordStatus("svgMatting", request, recordId);
        return AjaxResult.success();
    }

    @PostMapping("/svgMattingCuttingCallback")
    public AjaxResult svgMattingCuttingCallback(
        @RequestBody SvgMattingCallbackRequest request, @RequestParam(value = "recordId", required = false) String recordId) {
        log.info("svgMattingCutting回调入参: {}", request);
        updateCallRecordStatus("svgMattingCutting", request, recordId);
        return AjaxResult.success();
    }

    private void saveCallRecord(String interfaceName, String path, SvgMattingRequest body, String recordId) {
        BrtCommonCallRecord record = new BrtCommonCallRecord()
            .setRecordId(recordId)
            .setInterfaceName(interfaceName)
            .setRequestPath(mattingBaseUrl + path)
            .setCallbackUrl(body.getCallbackUrl())
            .setRequestPayload(JSON.toJSONString(body))
            .setStatus("PENDING");
        callRecordService.save(record);
    }

    private void submitAsyncRequest(String path, Object body, String recordId) {
        taskExecutor.execute(() -> {
            try {
                restTemplate.postForEntity(mattingBaseUrl + path, body, Void.class);
                callRecordService.lambdaUpdate()
                    .eq(BrtCommonCallRecord::getRecordId, recordId)
                    .set(BrtCommonCallRecord::getStatus, "SENT")
                    .update();
            } catch (Exception ex) {
                log.error("调用抠图服务失败，path={}, body={}", path, body, ex);
                callRecordService.lambdaUpdate()
                    .eq(BrtCommonCallRecord::getRecordId, recordId)
                    .set(BrtCommonCallRecord::getStatus, "FAILED")
                    .set(BrtCommonCallRecord::getErrorMessage, ex.getMessage())
                    .update();
            }
        });
    }

    private void updateCallRecordStatus(String interfaceName, SvgMattingCallbackRequest request, String recordId) {
        String targetRecordId = StringUtils.hasText(recordId) ? recordId : request.getRecordId();
        if (!StringUtils.hasText(targetRecordId)) {
            log.warn("回调未包含recordId，interface={}, request={}", interfaceName, request);
            return;
        }
        callRecordService.lambdaUpdate()
            .eq(BrtCommonCallRecord::getRecordId, targetRecordId)
            .set(BrtCommonCallRecord::getStatus, Boolean.TRUE.equals(request.getSuccess()) ? "SUCCESS" : "FAILED")
            .set(BrtCommonCallRecord::getCallbackPayload, JSON.toJSONString(request))
            .set(BrtCommonCallRecord::getErrorMessage, request.getErrorMessage())
            .update();
    }

    private void applyDefaults(SvgMattingRequest request, String recordId, boolean isCutting) {
        if (!StringUtils.hasText(request.getImgFileName())) {
            request.setImgFileName("svg/test/yazi.png");
        }
        if (!StringUtils.hasText(request.getSvgFileName())) {
            request.setSvgFileName(isCutting ? "svg/test/yazi1.svg" : "svg/test/4.svg");
        }
        if (!StringUtils.hasText(request.getOssCode())) {
            request.setOssCode("photoai");
        }
        if (!StringUtils.hasText(request.getResultDir())) {
            request.setResultDir("svg/temp");
        }
        request.setSplit(request.getSplit() == null ? Boolean.FALSE : request.getSplit());

        String callback = request.getCallbackUrl();
        if (!StringUtils.hasText(callback)) {
            callback = callbackBaseUrl + (isCutting ? "svgMattingCuttingCallback" : "svgMattingCallback");
        }
        callback = callback.contains("?") ? callback + "&recordId=" + recordId : callback + "?recordId=" + recordId;
        request.setCallbackUrl(callback);

        if (isCutting && request instanceof SvgMattingCuttingRequest) {
            SvgMattingCuttingRequest cuttingRequest = (SvgMattingCuttingRequest) request;
            if (cuttingRequest.getStartX() == null) {
                cuttingRequest.setStartX(500);
            }
            if (cuttingRequest.getStartY() == null) {
                cuttingRequest.setStartY(500);
            }
            if (cuttingRequest.getEndX() == null) {
                cuttingRequest.setEndX(1000);
            }
            if (cuttingRequest.getEndY() == null) {
                cuttingRequest.setEndY(1000);
            }
        }
    }
}
