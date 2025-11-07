package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtCustomerGrade;
import com.brt.order.vo.BrtCustomerGradeVo;

import java.util.List;

/**
 * 客户等级Service接口
 * 
 * @author Fgn
 * @date 2024-04-27
 */
public interface IBrtCustomerGradeService extends IService<BrtCustomerGrade> {

    /**
     * @description: TODO 分页查询客户等级列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return TableDataInfo<BrtCustomerGradeVo>
     **/
    public TableDataInfo<BrtCustomerGradeVo> queryBrtCustomerGradeList(BrtCustomerGradeVo brtCustomerGradeVo);

    /**
     * @description: TODO 查询全部客户等级列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return java.util.List<BrtCustomerGradeVo>
     **/
    public List<BrtCustomerGradeVo> queryBrtCustomerGradeAll(BrtCustomerGradeVo brtCustomerGradeVo);

    /**
     * @description: TODO 根据gradeId查询客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: gradeId
     * @return:
     * @return BrtCustomerGradeVo
     **/
    public BrtCustomerGradeVo queryBrtCustomerGradeByGradeId(String gradeId);

    /**
     * @description: TODO 新增客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return int
     **/
    public BrtCustomerGradeVo insertBrtCustomerGrade(BrtCustomerGradeVo brtCustomerGradeVo);

    /**
     * @description: TODO 修改客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo 客户等级
     * @return:
     * @return int
     **/
    public BrtCustomerGradeVo updateBrtCustomerGrade(BrtCustomerGradeVo brtCustomerGradeVo);

    /**
     * @description: TODO 批量删除客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param:  gradeIds 需要删除的客户等级主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtCustomerGradeByGradeIds(String[] gradeIds);

}
