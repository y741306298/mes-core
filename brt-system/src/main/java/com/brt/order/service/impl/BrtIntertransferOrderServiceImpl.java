package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.annotation.BrtDataFiltration;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.order.domain.BrtIntertransferOrder;
import com.brt.order.vo.BrtIntertransferOrderVo;
import com.brt.order.mapper.BrtIntertransferOrderMapper;
import com.brt.order.service.IBrtIntertransferOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 互转单Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-16
 */
@Service
public class BrtIntertransferOrderServiceImpl extends ServiceImpl<BrtIntertransferOrderMapper, BrtIntertransferOrder> implements IBrtIntertransferOrderService {

    @Override
    @BrtDataFiltration(perms = {"order:intertransferOrder:AllList"},field = "A.user_id")
    public TableDataInfo<BrtIntertransferOrderVo> queryBrtIntertransferOrderList(BrtIntertransferOrderVo brtIntertransferOrderVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtIntertransferOrderList(PageUtils.buildPage(), brtIntertransferOrderVo));
    }

    @Override
    public List<BrtIntertransferOrderVo> queryBrtIntertransferOrderAll(BrtIntertransferOrderVo brtIntertransferOrderVo) {
        return this.baseMapper.queryBrtIntertransferOrderList(brtIntertransferOrderVo);
    }

    @Override
    public BrtIntertransferOrderVo queryBrtIntertransferOrderByIntertransferId(String intertransferId) {
        return this.baseMapper.queryBrtIntertransferOrderByIntertransferId(intertransferId);
    }

    @Transactional
    @Override
    public BrtIntertransferOrderVo insertBrtIntertransferOrder(BrtIntertransferOrderVo brtIntertransferOrderVo) {
        saveBefore(brtIntertransferOrderVo);
        brtIntertransferOrderVo.setIntertransferNo("HZ"+System.currentTimeMillis());
        int i = this.baseMapper.insert(brtIntertransferOrderVo);
        return brtIntertransferOrderVo;
    }

    @Transactional
    @Override
    public BrtIntertransferOrderVo updateBrtIntertransferOrder(BrtIntertransferOrderVo brtIntertransferOrderVo) {
        saveBefore(brtIntertransferOrderVo);
        int i = this.baseMapper.updateById(brtIntertransferOrderVo);
        return brtIntertransferOrderVo;
    }

    @Transactional
    @Override
    public int deleteBrtIntertransferOrderByIntertransferIds(String[] intertransferIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(intertransferIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param brtIntertransferOrderVo
     * @return:
     **/
    public void saveBefore(BrtIntertransferOrderVo brtIntertransferOrderVo){

    }

}
