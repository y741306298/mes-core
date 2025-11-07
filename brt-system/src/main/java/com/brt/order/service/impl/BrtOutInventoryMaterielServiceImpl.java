package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.config.RuoYiConfig;
import com.brt.common.constant.Constants;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.order.domain.BrtOutInventoryMateriel;
import com.brt.order.vo.BrtOutInventoryMaterielVo;
import com.brt.order.mapper.BrtOutInventoryMaterielMapper;
import com.brt.order.service.IBrtOutInventoryMaterielService;
import com.brt.order.vo.BrtOrderBoomVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 入库单详情 入库单关联物料Service业务层处理
 *
 * @author Fgn
 * @date 2024-07-11
 */
@Service
public class BrtOutInventoryMaterielServiceImpl extends ServiceImpl<BrtOutInventoryMaterielMapper, BrtOutInventoryMateriel> implements IBrtOutInventoryMaterielService {

    @Override
    public TableDataInfo<BrtOutInventoryMaterielVo> queryBrtOutInventoryMaterielList(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtOutInventoryMaterielList(PageUtils.buildPage(), brtOutInventoryMaterielVo));
    }

    @Override
    public List<BrtOutInventoryMaterielVo> queryBrtOutInventoryMaterielAll(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        return this.baseMapper.queryBrtOutInventoryMaterielList(brtOutInventoryMaterielVo);
    }

    @Override
    public BrtOutInventoryMaterielVo queryBrtOutInventoryMaterielByOutInventoryMaterielId(String outInventoryMaterielId) {
        return this.baseMapper.queryBrtOutInventoryMaterielByOutInventoryMaterielId(outInventoryMaterielId);
    }

    @Transactional
    @Override
    public BrtOutInventoryMaterielVo insertBrtOutInventoryMateriel(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        saveBefore(brtOutInventoryMaterielVo);
        int i = this.baseMapper.insert(brtOutInventoryMaterielVo);
        return brtOutInventoryMaterielVo;
    }

    @Transactional
    @Override
    public BrtOutInventoryMaterielVo updateBrtOutInventoryMateriel(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo) {
        saveBefore(brtOutInventoryMaterielVo);
        int i = this.baseMapper.updateById(brtOutInventoryMaterielVo);
        return brtOutInventoryMaterielVo;
    }

    @Transactional
    @Override
    public int deleteBrtOutInventoryMaterielByOutInventoryMaterielIds(String[] outInventoryMaterielIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(outInventoryMaterielIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param brtOutInventoryMaterielVo
     * @return:
     **/
    public void saveBefore(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo){

    }

    /**
     * @description: TODO 根据outInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    public List<BrtOutInventoryMaterielVo> queryByOutInventoryId(String outInventoryId){
        return this.baseMapper.queryByOutInventoryId(outInventoryId);
    }

    @Override
    public List<BrtOutInventoryMaterielVo> getFileData(String fileUrl) throws Exception {
        // 判断识别的文件是否为空
        if(StringUtils.isEmpty(fileUrl)){
            return new ArrayList<>();
        }

        fileUrl = fileUrl.replaceAll(Constants.RESOURCE_PREFIX, RuoYiConfig.getProfile());
        InputStream inputStream = new FileInputStream(fileUrl);

        ExcelUtil<BrtOutInventoryMaterielVo> util = new ExcelUtil<BrtOutInventoryMaterielVo>(BrtOutInventoryMaterielVo.class);
        List<BrtOutInventoryMaterielVo> outInventoryMaterielVos = util.importExcel(inputStream);
        return outInventoryMaterielVos;
    }

    public void deleteByInventoryId(String[] inInventoryIds){
        this.baseMapper.deleteByOutInventoryId(inInventoryIds);
    }

}
