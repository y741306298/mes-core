package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.config.RuoYiConfig;
import com.brt.common.constant.Constants;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.StringUtils;
import com.brt.common.utils.poi.ExcelUtil;
import com.brt.order.domain.BrtOutInventoryMateriel;
import com.brt.order.vo.BrtOutInventoryMaterielVo;
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
public interface IBrtOutInventoryMaterielService extends IService<BrtOutInventoryMateriel> {

    /**
     * @description: TODO 分页查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return TableDataInfo<BrtOutInventoryMaterielVo>
     **/
    public TableDataInfo<BrtOutInventoryMaterielVo> queryBrtOutInventoryMaterielList(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo);

    /**
     * @description: TODO 查询全部入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return java.util.List<BrtOutInventoryMaterielVo>
     **/
    public List<BrtOutInventoryMaterielVo> queryBrtOutInventoryMaterielAll(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo);

    /**
     * @description: TODO 根据outInventoryMaterielId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: outInventoryMaterielId
     * @return:
     * @return BrtOutInventoryMaterielVo
     **/
    public BrtOutInventoryMaterielVo queryBrtOutInventoryMaterielByOutInventoryMaterielId(String outInventoryMaterielId);

    /**
     * @description: TODO 新增入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return int
     **/
    public BrtOutInventoryMaterielVo insertBrtOutInventoryMateriel(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo);

    /**
     * @description: TODO 修改入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo 入库单详情 入库单关联物料
     * @return:
     * @return int
     **/
    public BrtOutInventoryMaterielVo updateBrtOutInventoryMateriel(BrtOutInventoryMaterielVo brtOutInventoryMaterielVo);

    /**
     * @description: TODO 批量删除入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param:  outInventoryMaterielIds 需要删除的入库单详情 入库单关联物料主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtOutInventoryMaterielByOutInventoryMaterielIds(String[] outInventoryMaterielIds);

    /**
     * 解析文件数据
     * @param fileUrl
     * @return
     * @throws Exception
     */
    public List<BrtOutInventoryMaterielVo> getFileData(String fileUrl) throws Exception;

    /**
     * @description: TODO 根据outInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    public List<BrtOutInventoryMaterielVo> queryByOutInventoryId(String outInventoryId);


    public void deleteByInventoryId(String[] inInventoryIds);

}
