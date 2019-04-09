package com.lwq.controller;

import com.lwq.beans.PageQuery;
import com.lwq.common.ServerResponse;
import com.lwq.param.ResParam;
import com.lwq.service.ISysResService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author: Lwq
 * @Date: 2019/4/04 19:24
 * @Version 1.0
 * @Describe
 */
@Controller
@RequestMapping("/sys/res")
public class SysResController {

    @Resource
    private ISysResService iSysResService;

    @RequestMapping("/save.json")
    @ResponseBody
    public ServerResponse saveResModule(ResParam param) {
        return iSysResService.save(param);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public ServerResponse updateResModule(ResParam param) {
        return iSysResService.update(param);
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public ServerResponse list(@RequestParam("resModuleId") Integer resModuleId, PageQuery pageQuery) {
        return iSysResService.getPageByResModuleId(resModuleId,pageQuery);
    }

    @RequestMapping("/detail.json")
    @ResponseBody
    public ServerResponse detail(@RequestParam("resId") Integer resId) {
        return iSysResService.detail(resId);
    }
}
