package com.lwq.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysUser;
import com.lwq.param.RoleParam;
import com.lwq.service.*;
import com.lwq.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Lwq
 * @Date: 2019/3/15 14:51
 * @Version 1.0
 * @Describe
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private ISysRoleService iSysRoleService;

    @Resource
    private ISysTreeService iSysTreeService;

    @Resource
    private ISysRoleResService iSysRoleResService;

    @Resource
    private ISysRoleUserService iSysRoleUserService;

    @Resource
    private ISysUserService iSysUserService;

    /**
     * 新增角色
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public ServerResponse saveRole(RoleParam param) {
        return iSysRoleService.save(param);
    }

    /**
     * 更新角色
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public ServerResponse updateRole(RoleParam param) {
        return iSysRoleService.update(param);
    }

    /**
     * 查看所有的角色
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public ServerResponse list() {
        return ServerResponse.createBySuccess(iSysRoleService.getAll());
    }

    /**
     * 根据角色id查看该角色能够操作的资源
     * @param roleId
     * @return
     */
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public ServerResponse roleTree(@RequestParam("roleId") int roleId) {
        return ServerResponse.createBySuccess(iSysTreeService.roleTree(roleId));
    }

    /**
     *  修改该角色的持有资源
     * @param roleId
     * @param resIds
     * @return
     */
    @RequestMapping("/changeRess.json")
    @ResponseBody
    public ServerResponse changeRess(@RequestParam("roleId") int roleId, @RequestParam(value = "resIds", required = false, defaultValue = "") String resIds) {
        List<Integer> resIdList = StringUtils.splitToListInt(resIds);
        iSysRoleResService.changeRoleRess(roleId, resIdList);
        return ServerResponse.createBySuccessMessage("修改该角色的持有资源成功");
    }

    /**
     *  修改该角色的持有用户
     * @param roleId
     * @param userIds
     * @return
     */
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public ServerResponse changeUsers(@RequestParam("roleId") int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtils.splitToListInt(userIds);
        iSysRoleUserService.changeRoleUsers(roleId, userIdList);
        return ServerResponse.createBySuccessMessage("修改该角色的持有用户成功");
    }

    /**
     *  查看该角色被哪些用户持有和不持有
     * @param roleId
     * @return
     */
    @RequestMapping("/users.json")
    @ResponseBody
    public ServerResponse users(@RequestParam("roleId") int roleId) {
        List<SysUser> selectedUserList = iSysRoleUserService.getListByRoleId(roleId);
        List<SysUser> allUserList = iSysUserService.getAll();
        List<SysUser> unselectedUserList = Lists.newArrayList();

        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for(SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }
        // selectedUserList = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() != 1).collect(Collectors.toList());
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        return ServerResponse.createBySuccess(map);
    }
}
