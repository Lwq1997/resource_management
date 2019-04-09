package com.lwq.controller;

import com.lwq.common.ServerResponse;
import com.lwq.dto.DeptLevelDto;
import com.lwq.param.DeptParam;
import com.lwq.service.ISysDeptService;
import com.lwq.service.ISysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/3/11 22:03
 * @Version 1.0
 * @Describe
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysTreeService iSysTreeService;

    /**
     * 新增部门
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public ServerResponse saveDept(DeptParam param){
        return iSysDeptService.save(param);
    }

    /**
     * 查看所有的部门树
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public ServerResponse tree(){
        List<DeptLevelDto> dtoList = iSysTreeService.deptTree();
        return ServerResponse.createBySuccess(dtoList);
    }

    /**
     * 更新部门
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public ServerResponse updateDept(DeptParam param){
        return iSysDeptService.update(param);
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public ServerResponse deleteDept(@RequestParam("id") int id){
        return iSysDeptService.delete(id);
    }
}
