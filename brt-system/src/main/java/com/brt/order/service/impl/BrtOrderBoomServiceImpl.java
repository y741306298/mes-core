package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.config.RuoYiConfig;
import com.brt.common.constant.Constants;
import com.brt.common.core.domain.R;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.domain.BrtOrderBoom;
import com.brt.order.service.IBrtMaterielService;
import com.brt.order.vo.BrtCustomerVo;
import com.brt.order.vo.BrtOrderBoomVo;
import com.brt.order.mapper.BrtOrderBoomMapper;
import com.brt.order.service.IBrtOrderBoomService;
import com.brt.order.vo.BrtSalesOrderDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * boom单Service业务层处理
 *
 * @author Fgn
 * @date 2024-06-20
 */
@Service
public class BrtOrderBoomServiceImpl extends ServiceImpl<BrtOrderBoomMapper, BrtOrderBoom> implements IBrtOrderBoomService {

    @Autowired
    private IBrtMaterielService materielService;

    @Override
    public TableDataInfo<BrtOrderBoomVo> queryBrtOrderBoomList(BrtOrderBoomVo brtOrderBoomVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOrderBoomList(PageUtils.buildPage(), brtOrderBoomVo));
    }

    @Override
    public List<BrtOrderBoomVo> queryBrtOrderBoomAll(BrtOrderBoomVo brtOrderBoomVo) {
        return this.baseMapper.queryBrtOrderBoomList(brtOrderBoomVo);
    }

    @Override
    public BrtOrderBoomVo queryBrtOrderBoomByBoomId(String boomId) {
        return this.baseMapper.queryBrtOrderBoomByBoomId(boomId);
    }

    @Transactional
    @Override
    public BrtOrderBoomVo insertBrtOrderBoom(BrtOrderBoomVo brtOrderBoomVo) {
        saveBefore(brtOrderBoomVo);
        int i = this.baseMapper.insert(brtOrderBoomVo);
        return brtOrderBoomVo;
    }

    @Transactional
    @Override
    public BrtOrderBoomVo updateBrtOrderBoom(BrtOrderBoomVo brtOrderBoomVo) {
        saveBefore(brtOrderBoomVo);
        int i = this.baseMapper.updateById(brtOrderBoomVo);
        return brtOrderBoomVo;
    }

    @Transactional
    @Override
    public int deleteBrtOrderBoomByBoomIds(String[] boomIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(boomIds));
    }

    @Override
    public List<BrtOrderBoomVo> getFileData(String fileUrl) throws Exception {
        // 判断识别的文件是否为空
        if(StringUtils.isEmpty(fileUrl)){
            return new ArrayList<>();
        }

        fileUrl = fileUrl.replaceAll(Constants.RESOURCE_PREFIX,RuoYiConfig.getProfile());
        InputStream inputStream = new FileInputStream(fileUrl);

        ExcelUtil<BrtOrderBoomVo> util = new ExcelUtil<BrtOrderBoomVo>(BrtOrderBoomVo.class);
        List<BrtOrderBoomVo> orderBoomVoList = util.importExcel(inputStream);
        return orderBoomVoList;
    }

    @Override
    public int saveBoomList(BrtSalesOrderDetailsVo salesOrderDetailsVo) {
        // 保存boom单列表
        if(ObjectUtil.isNotEmpty(salesOrderDetailsVo)&&ObjectUtil.isNotEmpty(salesOrderDetailsVo.getBoomVoList())){
            salesOrderDetailsVo.getBoomVoList().forEach(boom -> {
                boom.setOrderId(salesOrderDetailsVo.getOrderId());
                boom.setOrderDetailsId(salesOrderDetailsVo.getDetailsId());
                boom.setTotalNum(salesOrderDetailsVo.getDetailsNum() * boom.getBoomNum());
                this.save(boom);
            });
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByOrderIds(String[] orderIds) {
        // 查询订单下的boom单
        List<BrtOrderBoom> boomList = this.list(new LambdaQueryWrapper<BrtOrderBoom>().in(BrtOrderBoom::getOrderId, orderIds));

        if (ObjectUtil.isNotEmpty(boomList)) {
            // 获取boom单关联的产品信息
            Set<String> materielIds = boomList.stream().map(BrtOrderBoom::getMaterielId).collect(Collectors.toSet());

            // 2，查询商品信息
            List<BrtMateriel> materielList = materielService.listByIds(materielIds);

            // 3,恢复库存
            materielList.forEach(materiel -> {
                // 统计该商品的库存数量
                long totalNum = boomList.stream().filter(boom -> boom.getMaterielId().equals(materiel.getMaterielId())).mapToLong(BrtOrderBoom::getTotalNum).sum();

                materiel.setMaterielNum(materiel.getMaterielNum() + totalNum);
                materiel.setLockNum(materiel.getLockNum() - totalNum);
                materielService.updateById(materiel);
            });

            // 获取需要删除的boom单ID 并删除
            List<String> boomIdList = boomList.stream().map(BrtOrderBoom::getBoomId).collect(Collectors.toList());
            return this.removeByIds(boomIdList) ? boomIdList.size() : 0;
        }
        return 0;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param brtOrderBoomVo
     * @return:
     **/
    public void saveBefore(BrtOrderBoomVo brtOrderBoomVo){

    }

}
