package com.brt.productionflow.service;

import com.brt.productionflow.vo.ComposeFlowQuery;
import com.brt.productionflow.vo.ComposeFlowVo;
import com.brt.productionflow.vo.ComposeTypePoolQuery;
import com.brt.productionflow.vo.ComposeTypePoolVo;

import java.util.List;

public interface IComposeTypePoolService {

    List<ComposeTypePoolVo> selectComposeTypePoolList(ComposeTypePoolQuery query);

    ComposeTypePoolVo selectComposeTypePoolById(String composeId);

    ComposeTypePoolVo insertComposeTypePool(ComposeTypePoolVo composeTypePoolVo);

    ComposeTypePoolVo updateComposeTypePool(ComposeTypePoolVo composeTypePoolVo);

    int deleteComposeTypePoolByIds(String[] composeIds);

    int clearComposeProcessesByIds(String[] composeIds);

    List<ComposeFlowVo> selectComposeFlowList(ComposeFlowQuery query);

    ComposeFlowVo selectComposeFlowById(String flowId);

    ComposeFlowVo insertComposeFlow(ComposeFlowVo composeFlowVo);

    ComposeFlowVo updateComposeFlow(ComposeFlowVo composeFlowVo);

    int deleteComposeFlowByIds(String[] flowIds);

    boolean applyFlowTemplates(String flowId, java.util.Collection<String> composeIds);
}
