package com.brt.order.controller;

import com.alibaba.fastjson2.JSON;
import com.brt.common.annotation.Anonymous;
import com.brt.common.core.domain.AjaxResult;
import com.brt.order.domain.BrtCommonCallRecord;
import com.brt.order.dto.AppendTemplateCallbackRequest;
import com.brt.order.dto.AppendTemplateRequest;
import com.brt.order.dto.CutPltCallbackRequest;
import com.brt.order.dto.CutPltRequest;
import com.brt.order.dto.PolygonNestCallbackRequest;
import com.brt.order.dto.PolygonNestRequest;
import com.brt.order.dto.SvgMattingCallbackRequest;
import com.brt.order.dto.SvgMattingCuttingRequest;
import com.brt.order.dto.SvgMattingRequest;
import com.brt.order.dto.SvgMattingResponse;
import com.brt.order.service.IBrtCommonCallRecordService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
import java.util.concurrent.Executor;

@Anonymous
@RestController
@RequestMapping("/img")
@Slf4j
public class BrtImgController {

    private static final String callbackBaseUrl = "http://118.31.58.44:9030/order-process-server/img/";
    private static final String mattingBaseUrl = "http://101.132.41.254:9929/";
    private static final String nestBaseUrl = "http://139.224.203.146:8080/nest/";

    private final Executor taskExecutor;

    private final IBrtCommonCallRecordService callRecordService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public BrtImgController(
        @Qualifier("threadPoolTaskExecutor") Executor taskExecutor,
        IBrtCommonCallRecordService callRecordService) {
        this.taskExecutor = taskExecutor;
        this.callRecordService = callRecordService;
    }

    @PostMapping("/svgMatting")
    public SvgMattingResponse svgMatting(@RequestBody SvgMattingRequest request) {
        log.info("接收到svgMatting请求: {}", request);
        String recordId = IdWorker.getIdStr();
        request.setRecordId(recordId);
        applyDefaults(request, recordId, false);
        saveCallRecord("svgMatting", mattingBaseUrl + "img/split", request.getCallbackUrl(), request, recordId);
        submitAsyncRequest(mattingBaseUrl + "img/split", request, recordId);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/svgMattingCutting")
    public SvgMattingResponse svgMattingCutting(@RequestBody SvgMattingCuttingRequest request) {
        log.info("接收到svgMattingCutting请求: {}", request);
        String recordId = IdWorker.getIdStr();
        request.setRecordId(recordId);
        applyDefaults(request, recordId, true);
        saveCallRecord("svgMattingCutting", mattingBaseUrl + "img/cut", request.getCallbackUrl(), request, recordId);
        submitAsyncRequest(mattingBaseUrl + "img/cut", request, recordId);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/svgMattingCallback")
    public AjaxResult svgMattingCallback(
        @RequestBody SvgMattingCallbackRequest request, @RequestParam(value = "recordId", required = false) String recordId) {
        log.info("svgMatting回调入参: {}", request);
        String targetRecordId = resolveRecordId(recordId, request.getRecordId(), "svgMatting", request);
        updateCallRecordStatus("svgMatting", request, targetRecordId, request.getSuccess(), request.getErrorMessage());
        return AjaxResult.success();
    }

    @PostMapping("/svgMattingCuttingCallback")
    public AjaxResult svgMattingCuttingCallback(
        @RequestBody SvgMattingCallbackRequest request, @RequestParam(value = "recordId", required = false) String recordId) {
        log.info("svgMattingCutting回调入参: {}", request);
        String targetRecordId = resolveRecordId(recordId, request.getRecordId(), "svgMattingCutting", request);
        updateCallRecordStatus(
            "svgMattingCutting", request, targetRecordId, request.getSuccess(), request.getErrorMessage());
        return AjaxResult.success();
    }

    @PostMapping("/polygonNest")
    public SvgMattingResponse polygonNest(@RequestBody PolygonNestRequest request) {
        log.info("接收到polygonNest请求: {}", request);
        String recordId = IdWorker.getIdStr();
        if (!StringUtils.hasText(request.getRequestId())) {
            request.setRequestId(recordId);
        }
        String callbackUrl = buildCallbackUrl("polygonNestCallback", recordId, request.getCallbackUrl());
        request.setCallbackUrl(callbackUrl);
        saveCallRecord("polygonNest", nestBaseUrl + "polygonNest", callbackUrl, request, recordId);
        submitAsyncRequest(nestBaseUrl + "polygonNest", request, recordId);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/polygonNestCallback")
    public AjaxResult polygonNestCallback(
        @RequestBody PolygonNestCallbackRequest request, @RequestParam(value = "recordId", required = false) String recordId) {
        log.info("polygonNest回调入参: {}", request);
        String requestId =
            request != null && request.getData() != null && !request.getData().isEmpty() ? request.getData().get(0).getRequestId() : null;
        String targetRecordId = resolveRecordId(recordId, requestId, "polygonNest", request);
        updateCallRecordStatus("polygonNest", request, targetRecordId, request.getSuccess(), request.getResponseMsg());
        return AjaxResult.success();
    }

    @PostMapping("/cutPlt")
    public SvgMattingResponse cutPlt(@RequestBody CutPltRequest request) {
        log.info("接收到cutPlt请求: {}", request);
        String recordId = IdWorker.getIdStr();
        if (!StringUtils.hasText(request.getRequestId())) {
            request.setRequestId(recordId);
        }
        String callbackUrl = buildCallbackUrl("cutPltCallback", recordId, request.getCallbackUrl());
        request.setCallbackUrl(callbackUrl);
        saveCallRecord("cutPlt", nestBaseUrl + "cutPlt", callbackUrl, request, recordId);
        submitAsyncRequest(nestBaseUrl + "cutPlt", request, recordId);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/cutPltCallback")
    public AjaxResult cutPltCallback(
        @RequestBody CutPltCallbackRequest request, @RequestParam(value = "recordId", required = false) String recordId) {
        log.info("cutPlt回调入参: {}", request);
        String targetRecordId = resolveRecordId(recordId, request != null ? request.getRequestId() : null, "cutPlt", request);
        updateCallRecordStatus("cutPlt", request, targetRecordId, request != null ? request.getSuccess() : null, request != null ? request.getResponseMsg() : null);
        return AjaxResult.success();
    }

    @PostMapping("/appendTemplate")
    public SvgMattingResponse appendTemplate(@RequestBody AppendTemplateRequest request) {
        log.info("接收到appendTemplate请求: {}", request);
        String recordId = IdWorker.getIdStr();
        if (!StringUtils.hasText(request.getRequestId())) {
            request.setRequestId(recordId);
        }
        String callbackUrl = buildCallbackUrl("appendTemplateCallback", recordId, request.getCallbackUrl());
        request.setCallbackUrl(callbackUrl);
        saveCallRecord("appendTemplate", nestBaseUrl + "appendTemplate", callbackUrl, request, recordId);
        submitAsyncRequest(nestBaseUrl + "appendTemplate", request, recordId);
        return SvgMattingResponse.ok();
    }

    @PostMapping("/appendTemplateCallback")
    public AjaxResult appendTemplateCallback(
        @RequestBody AppendTemplateCallbackRequest request, @RequestParam(value = "recordId", required = false) String recordId) {
        log.info("appendTemplate回调入参: {}", request);
        String requestId = request != null && request.getData() != null ? request.getData().getRequestId() : null;
        String targetRecordId = resolveRecordId(recordId, requestId, "appendTemplate", request);
        updateCallRecordStatus("appendTemplate", request, targetRecordId, request != null ? request.getSuccess() : null, request != null ? request.getResponseMsg() : null);
        return AjaxResult.success();
    }

    private void saveCallRecord(String interfaceName, String requestPath, String callbackUrl, Object body, String recordId) {
        BrtCommonCallRecord record = new BrtCommonCallRecord()
            .setRecordId(recordId)
            .setInterfaceName(interfaceName)
            .setRequestPath(requestPath)
            .setCallbackUrl(callbackUrl)
            .setRequestPayload(JSON.toJSONString(body))
            .setStatus("PENDING");
        callRecordService.save(record);
    }

    private void submitAsyncRequest(String url, Object body, String recordId) {
        taskExecutor.execute(() -> {
            try {
                restTemplate.postForEntity(url, body, String.class);
                callRecordService.lambdaUpdate()
                    .eq(BrtCommonCallRecord::getRecordId, recordId)
                    .set(BrtCommonCallRecord::getStatus, "SENT")
                    .update();
            } catch (Exception ex) {
                log.error("调用远程服务失败，url={}, body={}", url, body, ex);
                callRecordService.lambdaUpdate()
                    .eq(BrtCommonCallRecord::getRecordId, recordId)
                    .set(BrtCommonCallRecord::getStatus, "FAILED")
                    .set(BrtCommonCallRecord::getErrorMessage, ex.getMessage())
                    .update();
            }
        });
    }

    private void updateCallRecordStatus(String interfaceName, Object request, String targetRecordId, Boolean success, String errorMessage) {
        if (!StringUtils.hasText(targetRecordId)) {
            log.warn("回调未包含recordId，interface={}, request={}", interfaceName, request);
            return;
        }

        callRecordService.lambdaUpdate()
            .eq(BrtCommonCallRecord::getRecordId, targetRecordId)
            .set(BrtCommonCallRecord::getStatus, Boolean.TRUE.equals(success) ? "SUCCESS" : "FAILED")
            .set(BrtCommonCallRecord::getCallbackPayload, JSON.toJSONString(request))
            .set(BrtCommonCallRecord::getErrorMessage, errorMessage)
            .update();
    }

    private String buildCallbackUrl(String defaultCallback, String recordId, String existingCallback) {
        String callback = StringUtils.hasText(existingCallback) ? existingCallback : callbackBaseUrl + defaultCallback;
        return callback.contains("?") ? callback + "&recordId=" + recordId : callback + "?recordId=" + recordId;
    }

    private String resolveRecordId(String providedRecordId, String fallbackRecordId, String interfaceName, Object request) {
        if (StringUtils.hasText(providedRecordId)) {
            return providedRecordId;
        }
        if (StringUtils.hasText(fallbackRecordId)) {
            return fallbackRecordId;
        }
        log.warn("回调未包含recordId，interface={}, request={}", interfaceName, request);
        return null;
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

        String callback =
            buildCallbackUrl(isCutting ? "svgMattingCuttingCallback" : "svgMattingCallback", recordId, request.getCallbackUrl());
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
