package com.lwq.service.impl;

import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.dao.SysDeptMapper;
import com.lwq.dao.SysUserMapper;
import com.lwq.model.SysDept;
import com.lwq.param.DeptParam;
import com.lwq.service.ISysDeptService;
import com.lwq.util.BeanValidator;
import com.lwq.util.IpUtil;
import com.lwq.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/3/11 22:05
 * @Version 1.0
 * @Describe
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    public ServerResponse save(DeptParam param){
        ServerResponse serverResponse = BeanValidator.check(param);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }
        if(checkExist(param.getParentId(),param.getName(),param.getId())){
            return ServerResponse.createByErrorMessage("同一层级下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();

        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());

        int resultCount = sysDeptMapper.insertSelective(dept);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("新增部门成功");
        }else {
            return ServerResponse.createByErrorMessage("新增部门失败");
        }
    }

    public ServerResponse update(DeptParam param){
        ServerResponse serverResponse = BeanValidator.check(param);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }
        if(checkExist(param.getParentId(),param.getName(),param.getId())){
            return ServerResponse.createByErrorMessage("同一层级下存在相同名称的部门");
        }

        SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(param.getId());
        if(beforeDept == null){
            return ServerResponse.createByErrorMessage("待更新的部门不存在");
        }

        SysDept afterDept = SysDept.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();

        afterDept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterDept.setOperateTime(new Date());

        return updateWithChild(beforeDept,afterDept);
    }

    @Transactional
    public ServerResponse updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        int resultCount = sysDeptMapper.updateByPrimaryKey(after);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("更新部门成功");
        }else {
            return ServerResponse.createByErrorMessage("更新部门失败");
        }
    }

    private boolean checkExist(Integer parentId,String deptName,Integer deptId){
        return sysDeptMapper.countByNameAndParentId(parentId,deptName,deptId) > 0;
    }

    private String getLevel(Integer deptId){
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if(dept == null){
            return null;
        }
        return dept.getLevel();
    }


    public ServerResponse delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if(dept==null){
            return ServerResponse.createByErrorMessage("待删除的部门不存在");
        }
        if(sysDeptMapper.countByParentId(deptId)>0){
            return ServerResponse.createByErrorMessage("当前部门下面有子部门,无法删除");
        }
        if(sysUserMapper.countByDeptId(deptId)>0){
            return ServerResponse.createByErrorMessage("当前部门下面由用户,无法删除");
        }
        int resultCount = sysDeptMapper.deleteByPrimaryKey(deptId);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("删除部门成功");
        }else {
            return ServerResponse.createByErrorMessage("删除部门失败");
        }
    }
}
