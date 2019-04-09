package com.lwq.service.impl;

import com.lwq.common.ServerResponse;
import com.lwq.dao.*;
import com.lwq.model.SysRole;
import com.lwq.param.RoleParam;
import com.lwq.service.ISysRoleService;
import com.lwq.util.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/3/15 14:52
 * @Version 1.0
 * @Describe
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    SysRoleResMapper sysRoleResMapper;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysResMapper sysResMapper;


    public ServerResponse save(RoleParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getName(),param.getId())){
            return ServerResponse.createByErrorMessage("角色名称已经存在");
        }

        SysRole role = SysRole.builder()
                .name(param.getName())
                .remark(param.getRemark())
                .build();

        int resultCount = sysRoleMapper.insertSelective(role);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("新增角色成功");
        }else {
            return ServerResponse.createByErrorMessage("新增角色失败");
        }
    }

    public ServerResponse update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            return ServerResponse.createByErrorMessage("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        if(before==null){
            return ServerResponse.createByErrorMessage("待更新的角色不存在");
        }

        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .remark(param.getRemark())
                .build();

        int resultCount = sysRoleMapper.updateByPrimaryKeySelective(after);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("新增部门成功");
        }else {
            return ServerResponse.createByErrorMessage("新增部门失败");
        }
    }

    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

//    public List<SysRole> getRoleListByUserId(int userId){
//        List<Integer> roleIdListByUserId = sysRoleUserMapper.getRoleIdListByUserId(userId);
//
//        if(CollectionUtils.isEmpty(roleIdListByUserId)){
//            return Lists.newArrayList();
//        }
//        return sysRoleMapper.getByIdList(roleIdListByUserId);
//    }
//
//    public List<SysRole> getRoleListByResId(int resId) {
//        List<Integer> roleIdListByResId = sysRoleResMapper.getRoleIdListByResId(resId);
//
//        if(CollectionUtils.isEmpty(roleIdListByResId)){
//            return Lists.newArrayList();
//        }
//        return sysRoleMapper.getByIdList(roleIdListByResId);
//    }
//
//    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
//        if(CollectionUtils.isEmpty(roleList)){
//            return Lists.newArrayList();
//        }
//        List<Integer> roleIdList = roleList.stream()
//                .map(role -> role.getId())
//                .collect(Collectors.toList());
//        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
//        if(CollectionUtils.isEmpty(userIdList)){
//            return Lists.newArrayList();
//        }
//        return sysUserMapper.getByIdList(userIdList);
//    }
}
