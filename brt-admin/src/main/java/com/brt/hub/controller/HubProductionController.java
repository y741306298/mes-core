package com.brt.hub.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.brt.common.annotation.Anonymous;
import com.brt.common.core.domain.AjaxResult;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.MD5Util;
import com.brt.common.utils.Sha256;
import com.brt.hub.domain.HubMat;
import com.brt.hub.domain.HubProc;
import com.brt.hub.domain.HubProd;
import com.brt.hub.service.IHubMatService;
import com.brt.hub.service.IHubProcService;
import com.brt.hub.service.IHubProdService;
import com.brt.hub.vo.HubMatVo;
import com.brt.hub.vo.HubProcVo;
import com.brt.hub.vo.HubProdVo;
import com.brt.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Anonymous
@RestController
@RequestMapping("/hub")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class HubProductionController {

    //hub平台为api用户预先配置的 <api用户名, api密码>
//    private final static String API_USERNAME = "FWPT0001";
//    private final static String API_PASSWORD = "8lk90n08e";
    private final static String API_USERNAME = "SPMES0002";
    private final static String API_PASSWORD = "v5Q8CZ9q6";
//    private final static String API_USERNAME = "CUS00000001";
//    private final static String API_PASSWORD = "s1ad3eW2dst2";
//    private final static String API_USERNAME = "CUS10600";
//    private final static String API_PASSWORD = "H0zVMDA";

    //authorization prefix
    private final static String AUTHORIZATION_PREFIX = "Bearer ";

    //摘要算法
    private final static String DIGEST_ALGORITHM = "HmacSHA256";

    //hub
//    private final static String HUB_HOST = "47.100.116.208";
//    private final static String HUB_HOST = "47.100.42.68";
//    private final static Integer HUB_PORT = 8090;
    private final static String HUB_HOST = "139.196.181.226";//测试
    private final static Integer HUB_PORT = 8070;

    private final IHubMatService hubMatService;
    private final IHubProdService hubProdService;
    private final IHubProcService hubProcService;

    @GetMapping("/test")
    public AjaxResult syncHubData() {
        try {
            doSyncHubData();
            return AjaxResult.success("同步完成");
        } catch (Exception ex) {
            log.error("同步Hub数据失败", ex);
            return AjaxResult.error("同步失败");
        }
    }

    @Scheduled(cron = "0 30 5 * * ?")
    public void autoSyncHubData() {
        try {
            doSyncHubData();
        } catch (Exception ex) {
            log.error("定时同步Hub数据失败", ex);
        }
    }

    public void doSyncHubData() {
        AuthResponse authResponse = getAuth();

        List<Mat> matList = JSON.parseArray(fetchManuMatList(authResponse, null), Mat.class);
        List<Prod> prodList = JSON.parseArray(fetchManuProdList(authResponse, null), Prod.class);
        List<Proc> procList = JSON.parseArray(fetchManuProcList(authResponse, null), Proc.class);

        refreshMatList(matList);
        refreshProdList(prodList);
        refreshProcList(procList);
    }

    @GetMapping("/manuMatList")
    public TableDataInfo<HubMatVo> manuMatList(HubMatVo hubMatVo) {
        return hubMatService.queryHubMatList(hubMatVo);
    }

    @GetMapping("/manuProdList")
    public TableDataInfo<HubProdVo> manuProdList(HubProdVo hubProdVo) {
        return hubProdService.queryHubProdList(hubProdVo);
    }

    @GetMapping("/manuProcList")
    public TableDataInfo<HubProcVo> manuProcList(HubProcVo hubProcVo) {
        return hubProcService.queryHubProcList(hubProcVo);
    }

    private void refreshMatList(List<Mat> matList) {
        if (CollectionUtils.isEmpty(matList)) {
            log.warn("未获取到材料数据，跳过同步");
            return;
        }
        hubMatService.remove(Wrappers.<HubMat>lambdaQuery().isNotNull(HubMat::getMatCode));
        List<HubMat> hubMats = matList.stream()
                .map(mat -> new HubMat()
                        .setMatCode(mat.getMat_code())
                        .setMatName(mat.getMat_name())
                        .setMatCategory(mat.getMat_category())
                        .setMatColor(mat.getMat_color())
                        .setMatBrand(mat.getMat_brand())
                        .setMatSupplier(mat.getMat_supplier())
                        .setMatWidth(mat.getMat_width())
                        .setMatLength(mat.getMat_length())
                        .setMatThickness(mat.getMat_thickness())
                        .setPackageName(mat.getPackage_name())
                        .setMeasureUnitInt(mat.getMeasure_unitInt())
                        .setMeasureUnit(mat.getMeasure_unit())
                        .setUnitWeight(mat.getUnit_weight())
                        .setUnitPrice(mat.getUnit_price())
                        .setValid(mat.getIs_valid())
                        .setComments(mat.getComments()))
                .collect(Collectors.toList());
        hubMatService.saveBatch(hubMats);
    }

    private void refreshProdList(List<Prod> prodList) {
        if (CollectionUtils.isEmpty(prodList)) {
            log.warn("未获取到产品数据，跳过同步");
            return;
        }
        hubProdService.remove(Wrappers.<HubProd>lambdaQuery().isNotNull(HubProd::getProdCode));
        List<HubProd> hubProds = prodList.stream()
                .map(prod -> new HubProd()
                        .setProdCode(prod.getProd_code())
                        .setProdType(prod.getProd_type())
                        .setProdName(prod.getProd_name())
                        .setMinLength(prod.getMin_length())
                        .setMaxLength(prod.getMax_length())
                        .setMinWidth(prod.getMin_width())
                        .setMaxWidth(prod.getMax_width())
                        .setMaterialCode(prod.getMaterial_code())
                        .setMaterialName(prod.getMaterial_name())
                        .setMaterialColor(prod.getMaterial_color())
                        .setMaterialBrand(prod.getMaterial_brand())
                        .setMaterialSupplier(prod.getMaterial_supplier())
                        .setMeasureUnitInt(prod.getMeasure_unitInt())
                        .setMeasureUnit(prod.getMeasure_unit())
                        .setUnitWeight(prod.getUnit_weight())
                        .setAdditionalUnitfee(prod.getAdditional_unitfee())
                        .setComments(prod.getComments())
                        .setMerchandise(prod.getIs_merchandise()))
                .collect(Collectors.toList());
        hubProdService.saveBatch(hubProds);
    }

    private void refreshProcList(List<Proc> procList) {
        if (CollectionUtils.isEmpty(procList)) {
            log.warn("未获取到工艺数据，跳过同步");
            return;
        }
        hubProcService.remove(Wrappers.<HubProc>lambdaQuery().isNotNull(HubProc::getProcCode));
        List<HubProc> hubProcs = procList.stream()
                .map(proc -> new HubProc()
                        .setProcCode(proc.getProc_code())
                        .setProcName(proc.getProc_name())
                        .setProcAttachmentTypeList(proc.getProc_attachmentTypeList())
                        .setMeasureUnit(proc.getMeasure_unit())
                        .setMeasureUnitStr(proc.getMeasure_unitStr())
                        .setProcPrice(proc.getProc_price())
                        .setValid(proc.getIs_valid())
                        .setComments(proc.getComments()))
                .collect(Collectors.toList());
        hubProcService.saveBatch(hubProcs);
    }

    private String fetchManuMatList(AuthResponse authResponse, String manufacturerCode) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(null, headers);

        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/business/manumatlist?manufacturer_code=" + getManufacturerCode(manufacturerCode);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(bizTestUrl, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    private String fetchManuProdList(AuthResponse authResponse, String manufacturerCode) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(null, headers);

        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/business/manuprodlist?manufacturer_code=" + getManufacturerCode(manufacturerCode);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(bizTestUrl, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    private String fetchManuProcList(AuthResponse authResponse, String manufacturerCode) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(null, headers);

        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/business/processlist?manufacturer_code=" + getManufacturerCode(manufacturerCode);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(bizTestUrl, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    private String callBiz5(AuthResponse authResponse) {
        ProcessList processList1 = new ProcessList();
        processList1.setProc_code("SPTE231020140227874");
        processList1.setCap_occupy(0.81f);
        processList1.setProc_seq(1);

        ProcessList processList2 = new ProcessList();
        processList2.setProc_code("SPTE231020141438349");
        processList2.setCap_occupy(0f);
        processList2.setProc_seq(2);

        List<ProcessList> processListList = new ArrayList<>();
        processListList.add(processList1);
        processListList.add(processList2);

        InputOrderItem inputOrderItem = new InputOrderItem();
        inputOrderItem.setOrderItem_sn("CL010001133");
        inputOrderItem.setProd_code("MPR231020145318433");
        inputOrderItem.setProcessList(processListList);

        List<InputOrderItem> inputOrderItemList = new ArrayList<>();
        inputOrderItemList.add(inputOrderItem);

        GetAvailableDeliveryDatesInput getAvailableDeliveryDatesInput = new GetAvailableDeliveryDatesInput();
        getAvailableDeliveryDatesInput.setManufacturer_code("SP0792300");
        getAvailableDeliveryDatesInput.setAddr_id(360424100);
        getAvailableDeliveryDatesInput.setOrderItemList(inputOrderItemList);
        getAvailableDeliveryDatesInput.setDelivery_prefer(2);

        String jsonString = JSON.toJSONString(getAvailableDeliveryDatesInput);
        jsonString = "{\"addr_id\":330101,\"delivery_prefer\":2,\"manufacturer_code\":\"SP0792300\",\"orderItemList\":[{\"orderItem_sn\":\"CL010001133\",\"processList\":[{\"cap_occupy\":0.81,\"proc_code\":\"SPTE231020140227874\",\"proc_seq\":1},{\"cap_occupy\":0.0,\"proc_code\":\"SPTE231020141438349\",\"proc_seq\":2}],\"prod_code\":\"MPR231020145318433\"}]}";
        String encodedString = Base64Utils.encodeToString(jsonString.getBytes());
        String sign = MD5Util.encrypt(encodedString + authResponse.getRandomKey());

        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        baseTransferEntity.setObject(encodedString);
        baseTransferEntity.setSign(sign);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(baseTransferEntity, headers);

        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/business/getAvailebleDeliveryDates";

        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.postForObject(bizTestUrl, entity, String.class);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(bizTestUrl, entity, String.class);
        System.out.println(responseEntity.getStatusCodeValue());
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }

    private String callBiz4(AuthResponse authResponse) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(null, headers);

        //String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/business/manufacturers?client_code=CL10200&addr_id=330783004&manufacturer_type=喷印输出中心";
        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/business/manufacturers?client_code=CL10600&addr_id=a&manufacturer_type=喷印输出中心";


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(bizTestUrl, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    private String callBiz1(AuthResponse authResponse, BizDTO bizDTO) {
        String jsonString = JSON.toJSONString(bizDTO);
        String encodedString = Base64Utils.encodeToString(jsonString.getBytes());
        String sign = MD5Util.encrypt(encodedString + authResponse.getRandomKey());

        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        baseTransferEntity.setObject(encodedString);
        baseTransferEntity.setSign(sign);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(baseTransferEntity, headers);

        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/test";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(bizTestUrl, entity, String.class);
    }

    private String callBiz2(AuthResponse authResponse, Integer pageNo) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(null, headers);
        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/district/paging?pageNo="+pageNo+"&pageSize=100";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(bizTestUrl, HttpMethod.GET, entity, String.class);
        return responseEntity.getBody();
    }

    private String callBiz3(AuthResponse authResponse) {
        TransferOrderInput transferOrderInput = new TransferOrderInput();
        transferOrderInput.setOrderSn("18014398513894181");

        List<TransferOrderInput.TransferData> transferDataList = new ArrayList<>();
        TransferOrderInput.TransferData transferData1 = new TransferOrderInput.TransferData();
        transferData1.setManufacturerCode("SP025100");

        List<TransferOrderInput.ItemData> itemDataList = new ArrayList<>();
        TransferOrderInput.ItemData itemData1 = new TransferOrderInput.ItemData();
        itemData1.setOrderItemSn("CL018014398525009630");
        itemData1.setQuantity(1000);
        itemDataList.add(itemData1);

        TransferOrderInput.ItemData itemData2 = new TransferOrderInput.ItemData();
        itemData2.setOrderItemSn("CL018014398525009631");
        itemData2.setQuantity(1000);
        itemDataList.add(itemData2);

        TransferOrderInput.ItemData itemData3 = new TransferOrderInput.ItemData();
        itemData3.setOrderItemSn("CL018014398525009632");
        itemData3.setQuantity(1000);
        itemDataList.add(itemData3);

        TransferOrderInput.ItemData itemData4 = new TransferOrderInput.ItemData();
        itemData4.setOrderItemSn("CL018014398525009633");
        itemData4.setQuantity(1000);
        itemDataList.add(itemData4);

        TransferOrderInput.ItemData itemData5 = new TransferOrderInput.ItemData();
        itemData5.setOrderItemSn("CL018014398525009634");
        itemData5.setQuantity(1000);
        itemDataList.add(itemData5);

        TransferOrderInput.ItemData itemData6 = new TransferOrderInput.ItemData();
        itemData6.setOrderItemSn("CL018014398525009635");
        itemData6.setQuantity(1000);
        itemDataList.add(itemData6);

        TransferOrderInput.ItemData itemData7 = new TransferOrderInput.ItemData();
        itemData7.setOrderItemSn("CL018014398525009636");
        itemData7.setQuantity(1000);
        itemDataList.add(itemData7);

        transferData1.setItemDataList(itemDataList);

        transferDataList.add(transferData1);
        transferOrderInput.setTransferDataList(transferDataList);

        String jsonString = JSON.toJSONString(transferOrderInput);
        String encodedString = Base64Utils.encodeToString(jsonString.getBytes());
        String sign = MD5Util.encrypt(encodedString + authResponse.getRandomKey());

        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        baseTransferEntity.setObject(encodedString);
        baseTransferEntity.setSign(sign);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", AUTHORIZATION_PREFIX + authResponse.getToken());
        HttpEntity entity = new HttpEntity(baseTransferEntity, headers);

        String bizTestUrl = "http://" + HUB_HOST + ":" + HUB_PORT + "/business/transferOrder";

        String responseData = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            responseData = restTemplate.postForObject(bizTestUrl, entity, String.class);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return responseData;
    }

    private AuthResponse getAuth() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        String digest = Sha256.hmacDigest(API_USERNAME, API_PASSWORD, DIGEST_ALGORITHM);
        System.out.println(digest);

        String url = "http://" + HUB_HOST + ":" + HUB_PORT + "/auth?userName=" + API_USERNAME + "&password=" + digest;
        System.out.println(url);

        RestTemplate restTemplate = new RestTemplate();
        AuthResponse authResponse = restTemplate.getForObject(url, AuthResponse.class);
        System.out.println(authResponse);
        // todo: 接入方需要缓存authResponse，后续业务api调用需要token和randomKey，且过期时间为7天

        return authResponse;

//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setRandomKey("ag8q5k");
//        authResponse.setToken("eyJhbGciOiJIUzUxMiJ9.eyJyYW5kb21LZXkiOiJhZzhxNWsiLCJzdWIiOiJTUE1FUzAwMDEiLCJleHAiOjE2OTc2MjM0NDEsImlhdCI6MTY5NzAxODY0MX0.69VmYWXV0BjOu_g8LGS1_e7q843fr30v0KGRz1G-YrKtwXpwnqhPjv8KKA6bD--KJ39dAEWVpDN-gTWR9tqJ1g");
//        return authResponse;
    }

    private String getManufacturerCode(String manufacturerCode) {
        return Optional.ofNullable(StringUtils.hasText(manufacturerCode) ? manufacturerCode : null)
                .orElse("SP2000100");
    }
}
