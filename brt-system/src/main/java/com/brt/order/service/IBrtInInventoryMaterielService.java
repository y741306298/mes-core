package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.config.RuoYiConfig;
import com.brt.common.constant.Constants;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.StringUtils;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.order.domain.BrtInInventoryMateriel;
import com.brt.order.vo.BrtInInventoryMaterielVo;
import com.brt.order.vo.BrtOrderBoomVo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 入库单详情 入库单关联物料Service接口
 *
 * @author Fgn
 * @date 2024-07-11
 */
public interface IBrtInInventoryMaterielService extends IService<BrtInInventoryMateriel> {

    /**
     * @description: TODO 分页查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return TableDataInfo<BrtInInventoryMaterielVo>
     **/
    public TableDataInfo<BrtInInventoryMaterielVo> queryBrtInInventoryMaterielList(BrtInInventoryMaterielVo brtInInventoryMaterielVo);

    /**
     * @description: TODO 查询全部入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return java.util.List<BrtInInventoryMaterielVo>
     **/
    public List<BrtInInventoryMaterielVo> queryBrtInInventoryMaterielAll(BrtInInventoryMaterielVo brtInInventoryMaterielVo);

    /**
     * @description: TODO 根据inInventoryMaterielId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: inInventoryMaterielId
     * @return:
     * @return BrtInInventoryMaterielVo
     **/
    public BrtInInventoryMaterielVo queryBrtInInventoryMaterielByInInventoryMaterielId(String inInventoryMaterielId);

    /**
     * @description: TODO 新增入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return int
     **/
    public BrtInInventoryMaterielVo insertBrtInInventoryMateriel(BrtInInventoryMaterielVo brtInInventoryMaterielVo);

    /**
     * @description: TODO 修改入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return int
     **/
    public BrtInInventoryMaterielVo updateBrtInInventoryMateriel(BrtInInventoryMaterielVo brtInInventoryMaterielVo);

    /**
     * @description: TODO 批量删除入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  inInventoryMaterielIds 需要删除的入库单详情 入库单关联物料主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtInInventoryMaterielByInInventoryMaterielIds(String[] inInventoryMaterielIds);

    /**
     * 解析文件数据
     * @param fileUrl
     * @return
     * @throws Exception
     */
    public List<BrtInInventoryMaterielVo> getFileData(String fileUrl) throws Exception;

    /**
     * @description: TODO 根据inInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    public List<BrtInInventoryMaterielVo> queryByInInventoryId(String inInventoryId);

    void deleteByInventoryId(String[] inInventoryIds);

}
