package com.lwq.controller;

import com.google.common.collect.Maps;
import com.lwq.beans.PageQuery;
import com.lwq.common.ServerResponse;
import com.lwq.param.UserParam;
import com.lwq.service.ISysTreeService;
import com.lwq.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author: Lwq
 * @Date: 2019/3/14 13:32
 * @Version 1.0
 * @Describe
 */
@Controller
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController {

    @Autowired
    ISysUserService  iSysUserService;

    @Autowired
    ISysTreeService iSysTreeService;

    @RequestMapping("/save.json")
    @ResponseBody
    public ServerResponse saveUser(UserParam param){
        return iSysUserService.save(param);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public ServerResponse updateUser(UserParam param){
        return iSysUserService.update(param);
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public ServerResponse page(@RequestParam("deptId") int deptId, PageQuery pageQuery){
        return iSysUserService.getPageByDeptId(deptId,pageQuery);
    }

    /**
     * 查看该用户能够借用的资源和已经借用的资源
     * @param userId
     * @return
     */
    @RequestMapping("/detail.json")
    @ResponseBody
    public ServerResponse detail(@RequestParam("userId") int userId){
        Map<String,Object> map = Maps.newHashMap();
        map.put("nohave",iSysUserService.userNoHaveRes(userId));
        map.put("have",iSysUserService.userHaveRes(userId));
        return ServerResponse.createBySuccess(map);
    }

    @RequestMapping("/borrow.json")
    @ResponseBody
    public ServerResponse borrow(@RequestParam("userId") int userId,@RequestParam("resId") int resId){
        return iSysUserService.borrow(userId,resId);
    }

    @RequestMapping("/ret.json")
    @ResponseBody
    public ServerResponse ret(@RequestParam("userId") int userId,@RequestParam("resId") int resId){
        return iSysUserService.ret(userId,resId);
    }

    @RequestMapping("/find.json")
    @ResponseBody
    public ServerResponse find(@RequestParam("userId") int userId){
        return iSysUserService.find(userId);
    }

}
