package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtAccount;
import com.brt.order.vo.BrtAccountVo;
import com.brt.order.mapper.BrtAccountMapper;
import com.brt.order.service.IBrtAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 账户类型Service业务层处理
 * 
 * @author Fgn
 * @date 2024-05-15
 */
@Service
public class BrtAccountServiceImpl extends ServiceImpl<BrtAccountMapper, BrtAccount> implements IBrtAccountService {

    @Override
    public TableDataInfo<BrtAccountVo> queryBrtAccountList(BrtAccountVo brtAccountVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtAccountList(PageUtils.buildPage(), brtAccountVo));
    }

    @Override
    public List<BrtAccountVo> queryBrtAccountAll(BrtAccountVo brtAccountVo) {
        return this.baseMapper.queryBrtAccountList(brtAccountVo);
    }

    @Override
    public BrtAccountVo queryBrtAccountByAccountId(String accountId) {
        return this.baseMapper.queryBrtAccountByAccountId(accountId);
    }

    @Transactional
    @Override
    public BrtAccountVo insertBrtAccount(BrtAccountVo brtAccountVo) {
        saveBefore(brtAccountVo);
        int i = this.baseMapper.insert(brtAccountVo);
        return brtAccountVo;
    }

    @Transactional
    @Override
    public BrtAccountVo updateBrtAccount(BrtAccountVo brtAccountVo) {
        saveBefore(brtAccountVo);
        int i = this.baseMapper.updateById(brtAccountVo);
        return brtAccountVo;
    }

    @Transactional
    @Override
    public int deleteBrtAccountByAccountIds(String[] accountIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(accountIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param brtAccountVo
     * @return:
     **/
    public void saveBefore(BrtAccountVo brtAccountVo){

    }

}
