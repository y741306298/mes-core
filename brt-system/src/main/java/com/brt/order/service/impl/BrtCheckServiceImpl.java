package com.brt.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.annotation.BrtDataFiltration;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderTypeEnums;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.common.utils.spring.SpringUtils;
import com.brt.order.domain.BrtCheck;
import com.brt.order.vo.BrtCheckAchievementVo;
import com.brt.order.vo.BrtCheckVo;
import com.brt.order.mapper.BrtCheckMapper;
import com.brt.order.service.IBrtCheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 账单Service业务层处理
 *
 * @author Fgn
 * @date 2024-05-15
 */
@Service
public class BrtCheckServiceImpl extends ServiceImpl<BrtCheckMapper, BrtCheck> implements IBrtCheckService {

    @Override
    @BrtDataFiltration(perms = {"order:check:AllList"},field = "A.user_id")
    public TableDataInfo<BrtCheckVo> queryBrtCheckList(BrtCheckVo brtCheckVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtCheckList(PageUtils.buildPage(), brtCheckVo));
    }

    @Override
    public List<BrtCheckVo> queryBrtCheckAll(BrtCheckVo brtCheckVo) {
        return this.baseMapper.queryBrtCheckList(brtCheckVo);
    }

    @Override
    public BrtCheckVo queryBrtCheckByCheckId(String checkId) {
        return this.baseMapper.queryBrtCheckByCheckId(checkId);
    }

    @Transactional
    @Override
    public BrtCheckVo insertBrtCheck(BrtCheckVo brtCheckVo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        saveBefore(brtCheckVo);
        brtCheckVo.setCheckNo(System.currentTimeMillis()+"");
        int i = this.baseMapper.insert(brtCheckVo);

        if (StringUtils.isNotBlank(brtCheckVo.getOrderId())){
            // 订单逻辑处理
            OrderTypeEnums orderTypeEnums = OrderTypeEnums.getEnumsByCode(brtCheckVo.getOrderType());
            // 通过反射获取对应服务类的实例
            Object bean = SpringUtils.getBean(orderTypeEnums.getBeanName());
            Method method = bean.getClass().getMethod(orderTypeEnums.getCheckMethod(), new Class[]{BrtCheckVo.class});
            // 调用方法，并传入参数
            Object result = method.invoke(bean, brtCheckVo);
        }

        return brtCheckVo;
    }

    @Transactional
    @Override
    public BrtCheckVo updateBrtCheck(BrtCheckVo brtCheckVo) {
        saveBefore(brtCheckVo);
        int i = this.baseMapper.updateById(brtCheckVo);
        return brtCheckVo;
    }

    @Transactional
    @Override
    public int deleteBrtCheckByCheckIds(String[] checkIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(checkIds));
    }

    @Override
    public List<Map<String, Object>> achievement(String year, String orderType) {

        //判断年份是否是本年，如果是本年则只显示到本月
        LocalDateTime localDateTime = LocalDateTime.now();
        //当前年份
        String thisYear = String.valueOf(localDateTime.getYear());
        //如果查询的年份是当前年则需要根据月份做查询
        String thisMonth = StringUtils.isNotEmpty(year)&&thisYear.equals(year)?String.valueOf(localDateTime.getMonthValue()):"";

        List<Map<String, Object>> achievement = this.baseMapper.achievement(year, orderType,thisMonth);
        achievement.forEach(item -> {
            item.put("hasChildren",true);
        });
        return achievement;
    }

    public List<BrtCheckAchievementVo> exportAchievement(String year, String orderType) {

        //判断年份是否是本年，如果是本年则只显示到本月
        LocalDateTime localDateTime = LocalDateTime.now();
        //当前年份
        String thisYear = String.valueOf(localDateTime.getYear());
        //如果查询的年份是当前年则需要根据月份做查询
        String thisMonth = StringUtils.isNotEmpty(year)&&thisYear.equals(year)?String.valueOf(localDateTime.getMonthValue()):"";

        List<BrtCheckAchievementVo> achievement = this.baseMapper.exportAchievement(year, orderType,thisMonth);

        return achievement;
    }

    @Override
    public List<Map<String, Object>> accountAchievement(String year, String orderType, String month) {
        return this.baseMapper.accountAchievement(year,orderType,month);
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param brtCheckVo
     * @return:
     **/
    public void saveBefore(BrtCheckVo brtCheckVo){

    }

}
