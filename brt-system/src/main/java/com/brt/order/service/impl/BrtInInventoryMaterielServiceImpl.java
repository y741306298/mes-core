package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.config.RuoYiConfig;
import com.brt.common.constant.Constants;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.order.domain.BrtInInventoryMateriel;
import com.brt.order.vo.BrtInInventoryMaterielVo;
import com.brt.order.mapper.BrtInInventoryMaterielMapper;
import com.brt.order.service.IBrtInInventoryMaterielService;
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
public class BrtInInventoryMaterielServiceImpl extends ServiceImpl<BrtInInventoryMaterielMapper, BrtInInventoryMateriel> implements IBrtInInventoryMaterielService {

    @Override
    public TableDataInfo<BrtInInventoryMaterielVo> queryBrtInInventoryMaterielList(BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtInInventoryMaterielList(PageUtils.buildPage(), brtInInventoryMaterielVo));
    }

    @Override
    public List<BrtInInventoryMaterielVo> queryBrtInInventoryMaterielAll(BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        return this.baseMapper.queryBrtInInventoryMaterielList(brtInInventoryMaterielVo);
    }

    @Override
    public BrtInInventoryMaterielVo queryBrtInInventoryMaterielByInInventoryMaterielId(String inInventoryMaterielId) {
        return this.baseMapper.queryBrtInInventoryMaterielByInInventoryMaterielId(inInventoryMaterielId);
    }

    @Transactional
    @Override
    public BrtInInventoryMaterielVo insertBrtInInventoryMateriel(BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        saveBefore(brtInInventoryMaterielVo);
        int i = this.baseMapper.insert(brtInInventoryMaterielVo);
        return brtInInventoryMaterielVo;
    }

    @Transactional
    @Override
    public BrtInInventoryMaterielVo updateBrtInInventoryMateriel(BrtInInventoryMaterielVo brtInInventoryMaterielVo) {
        saveBefore(brtInInventoryMaterielVo);
        int i = this.baseMapper.updateById(brtInInventoryMaterielVo);
        return brtInInventoryMaterielVo;
    }

    @Transactional
    @Override
    public int deleteBrtInInventoryMaterielByInInventoryMaterielIds(String[] inInventoryMaterielIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(inInventoryMaterielIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param brtInInventoryMaterielVo
     * @return:
     **/
    public void saveBefore(BrtInInventoryMaterielVo brtInInventoryMaterielVo){

    }

    /**
     * @description: TODO 根据inInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    public List<BrtInInventoryMaterielVo> queryByInInventoryId(String inInventoryId){
        return this.baseMapper.queryByInInventoryId(inInventoryId);
    }

    @Override
    public List<BrtInInventoryMaterielVo> getFileData(String fileUrl) throws Exception {
        // 判断识别的文件是否为空
        if(StringUtils.isEmpty(fileUrl)){
            return new ArrayList<>();
        }

        fileUrl = fileUrl.replaceAll(Constants.RESOURCE_PREFIX, RuoYiConfig.getProfile());
        InputStream inputStream = new FileInputStream(fileUrl);

        ExcelUtil<BrtInInventoryMaterielVo> util = new ExcelUtil<BrtInInventoryMaterielVo>(BrtInInventoryMaterielVo.class);
        List<BrtInInventoryMaterielVo> inInventoryMaterielVos = util.importExcel(inputStream);
        return inInventoryMaterielVos;
    }

    public void deleteByInventoryId(String[] inInventoryIds){
        this.baseMapper.deleteByInInventoryId(inInventoryIds);
    }

}
