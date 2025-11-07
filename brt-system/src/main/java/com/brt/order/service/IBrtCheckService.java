package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtCheck;
import com.brt.order.vo.BrtCheckAchievementVo;
import com.brt.order.vo.BrtCheckVo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 账单Service接口
 *
 * @author Fgn
 * @date 2024-05-15
 */
public interface IBrtCheckService extends IService<BrtCheck> {

    /**
     * @description: TODO 分页查询账单列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return TableDataInfo<BrtCheckVo>
     **/
    public TableDataInfo<BrtCheckVo> queryBrtCheckList(BrtCheckVo brtCheckVo);

    /**
     * @description: TODO 查询全部账单列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return java.util.List<BrtCheckVo>
     **/
    public List<BrtCheckVo> queryBrtCheckAll(BrtCheckVo brtCheckVo);

    /**
     * @description: TODO 根据checkId查询账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: checkId
     * @return:
     * @return BrtCheckVo
     **/
    public BrtCheckVo queryBrtCheckByCheckId(String checkId);

    /**
     * @description: TODO 新增账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return int
     **/
    public BrtCheckVo insertBrtCheck(BrtCheckVo brtCheckVo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    /**
     * @description: TODO 修改账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo 账单
     * @return:
     * @return int
     **/
    public BrtCheckVo updateBrtCheck(BrtCheckVo brtCheckVo);

    /**
     * @description: TODO 批量删除账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param:  checkIds 需要删除的账单主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtCheckByCheckIds(String[] checkIds);

    /**
     * @description: TODO 业绩统计
     * @author: FanGN
     * @date: 14:40 2024/5/20
     * @param:
     * @param year
     * @param orderType
     * @return:
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    List<Map<String, Object>> achievement(String year, String orderType);

    /**
     * 导出
     * @param year
     * @param orderType
     * @return
     */
    List<BrtCheckAchievementVo> exportAchievement(String year, String orderType);
    /**
     * @description: TODO 查询账户业绩
     * @author: FanGN
     * @date: 15:32 2024/5/20
     * @param:
     * @param year
     * @param orderType
     * @return:
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> accountAchievement(String year, String orderType,String month);
}
