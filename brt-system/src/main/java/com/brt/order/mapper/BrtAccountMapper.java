package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtAccount;
import com.brt.order.vo.BrtAccountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账户类型Mapper接口
 * 
 * @author Fgn
 * @date 2024-05-15
 */
public interface BrtAccountMapper extends BaseMapper<BrtAccount> {

    /**
     * @description: TODO 分页查询账户类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: page
     * @param: brtAccountVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtAccountVo> queryBrtAccountList(Page<?> page, @Param("brtAccountVo") BrtAccountVo brtAccountVo);

    /**
     * @description: TODO 查询账户类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtAccountVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtAccountVo> queryBrtAccountList(@Param("brtAccountVo") BrtAccountVo brtAccountVo);

    /**
     * @description: TODO 根据accountId查询账户类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @return:
     * @return Vo
     **/
    BrtAccountVo queryBrtAccountByAccountId(@Param("AccountId") String accountId);

}
