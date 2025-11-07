package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtAccount;
import com.brt.order.vo.BrtAccountVo;

import java.util.List;

/**
 * 账户类型Service接口
 * 
 * @author Fgn
 * @date 2024-05-15
 */
public interface IBrtAccountService extends IService<BrtAccount> {

    /**
     * @description: TODO 分页查询账户类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return TableDataInfo<BrtAccountVo>
     **/
    public TableDataInfo<BrtAccountVo> queryBrtAccountList(BrtAccountVo brtAccountVo);

    /**
     * @description: TODO 查询全部账户类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return java.util.List<BrtAccountVo>
     **/
    public List<BrtAccountVo> queryBrtAccountAll(BrtAccountVo brtAccountVo);

    /**
     * @description: TODO 根据accountId查询账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: accountId
     * @return:
     * @return BrtAccountVo
     **/
    public BrtAccountVo queryBrtAccountByAccountId(String accountId);

    /**
     * @description: TODO 新增账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return int
     **/
    public BrtAccountVo insertBrtAccount(BrtAccountVo brtAccountVo);

    /**
     * @description: TODO 修改账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo 账户类型
     * @return:
     * @return int
     **/
    public BrtAccountVo updateBrtAccount(BrtAccountVo brtAccountVo);

    /**
     * @description: TODO 批量删除账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param:  accountIds 需要删除的账户类型主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtAccountByAccountIds(String[] accountIds);

}
