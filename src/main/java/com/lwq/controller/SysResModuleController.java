package com.lwq.controller;

import com.lwq.common.ServerResponse;
import com.lwq.dto.ResModuleLevelDto;
import com.lwq.param.ResModuleParam;
import com.lwq.service.ISysResModuleService;
import com.lwq.service.ISysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/3/14 19:24
 * @Version 1.0
 * @Describe
 */
@Controller
@RequestMapping("/sys/resModule")
@Slf4j
public class SysResModuleController {

    @Resource
    private ISysResModuleService iSysResModuleService;

    @Resource
    private ISysTreeService iSysTreeService;

    @RequestMapping("/save.json")
    @ResponseBody
    public ServerResponse saveAclModule(ResModuleParam param){
        return iSysResModuleService.save(param);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public ServerResponse updateAclModule(ResModuleParam param){
        return iSysResModuleService.update(param);
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public ServerResponse tree(){
        List<ResModuleLevelDto> dtoList = iSysTreeService.resModuleTree();
        return ServerResponse.createBySuccess(dtoList);
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public ServerResponse deleteAclModule(@RequestParam("id") int aclModuleId){
        return iSysResModuleService.delete(aclModuleId);
    }
}
