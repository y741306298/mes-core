package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.annotation.BrtDataFiltration;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.OrderNoEnums;
import com.brt.common.exception.ServiceException;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.SecurityUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtCustomer;
import com.brt.order.domain.BrtCustomerGrade;
import com.brt.order.domain.BrtCustomerType;
import com.brt.order.service.IBrtCustomerGradeService;
import com.brt.order.service.IBrtCustomerTypeService;
import com.brt.order.utils.BrtOrderNoUtil;
import com.brt.order.vo.BrtCustomerVo;
import com.brt.order.mapper.BrtCustomerMapper;
import com.brt.order.service.IBrtCustomerService;
import com.brt.order.vo.MarketRecordVo;
import com.brt.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 客户信息Service业务层处理
 *
 * @author Fgn
 * @date 2024-04-27
 */
@Service
public class BrtCustomerServiceImpl extends ServiceImpl<BrtCustomerMapper, BrtCustomer> implements IBrtCustomerService {

    @Autowired
    private IBrtCustomerGradeService customerGradeService;

    @Autowired
    private IBrtCustomerTypeService customerTypeService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private BrtOrderNoUtil orderNoUtil;

    @Override
    @BrtDataFiltration(perms = {"order:customer:AllList"},field = "A.user_id")
    public TableDataInfo<BrtCustomerVo> queryBrtCustomerList(BrtCustomerVo brtCustomerVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtCustomerList(PageUtils.buildPage(), brtCustomerVo));
    }

    @Override
    public List<BrtCustomerVo> queryBrtCustomerAll(BrtCustomerVo brtCustomerVo) {
        return this.baseMapper.queryBrtCustomerList(brtCustomerVo);
    }

    @Override
    public BrtCustomerVo queryBrtCustomerByCustomerId(String customerId) {
        return this.baseMapper.queryBrtCustomerByCustomerId(customerId);
    }

    @Transactional
    @Override
    public BrtCustomerVo insertBrtCustomer(BrtCustomerVo brtCustomerVo) {
        saveBefore(brtCustomerVo);
        int i = this.baseMapper.insert(brtCustomerVo);
        return brtCustomerVo;
    }

    @Transactional
    @Override
    public BrtCustomerVo updateBrtCustomer(BrtCustomerVo brtCustomerVo) {
        saveBefore(brtCustomerVo);
        int i = this.baseMapper.updateById(brtCustomerVo);
        return brtCustomerVo;
    }

    @Transactional
    @Override
    public int deleteBrtCustomerByCustomerIds(String[] customerIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(customerIds));
    }

    @Override
    public String importData(List<BrtCustomerVo> customerList, String operName) {
        // 查询客户等级列表
        List<BrtCustomerGrade> gradeList = customerGradeService.list();
        // 查询客户分类列表
        List<BrtCustomerType> typeList = customerTypeService.list();
        // 查询业务员列表
        List<SysUser> userList = userService.selectUserList(new SysUser() {{
            setRoleId(2L);
        }});

        customerList.stream().forEach(customer -> {

            // 查询业务员
            Optional<SysUser> userOptional = userList.stream().filter(user -> user.getUserName().equals(customer.getUserName())).findFirst();
            if (userOptional.isPresent()){
                customer.setUserId(userOptional.get().getUserId().toString());
            }

            this.baseMapper.insert(customer);
        });
        return StringUtils.format("成功导入{}条数据",customerList.size());
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param brtCustomerVo
     * @return:
     **/
    public void saveBefore(BrtCustomerVo brtCustomerVo){
        BrtCustomer customer = this.baseMapper.selectOne(new LambdaQueryWrapper<BrtCustomer>()
                .ne(StringUtils.isNotEmpty(brtCustomerVo.getCustomerId()), BrtCustomer::getCustomerId, brtCustomerVo.getCustomerId())
                .eq(BrtCustomer::getCustomerNo, brtCustomerVo.getCustomerNo())
                .last(" limit 1")
        );

        if (ObjectUtil.isNotEmpty(customer)){
            throw new ServiceException("用户编号已存在");
        }
    }

    /**
     * 查询客户的销售记录
     * @param marketRecordVo
     * @return
     */
    public TableDataInfo<MarketRecordVo> queryMarketRecord(MarketRecordVo marketRecordVo){
        return PageUtils.buildDataInfo(this.baseMapper.queryMarketRecord(PageUtils.buildPage(),marketRecordVo));
    }

}
