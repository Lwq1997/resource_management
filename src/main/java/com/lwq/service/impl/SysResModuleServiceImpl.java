package com.lwq.service.impl;

import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.dao.SysResMapper;
import com.lwq.dao.SysResModuleMapper;
import com.lwq.model.SysResModule;
import com.lwq.param.ResModuleParam;
import com.lwq.service.ISysResModuleService;
import com.lwq.util.BeanValidator;
import com.lwq.util.IpUtil;
import com.lwq.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/3/14 19:27
 * @Version 1.0
 * @Describe
 */
@Service
public class SysResModuleServiceImpl implements ISysResModuleService {

    @Autowired
    private SysResModuleMapper sysResModuleMapper;

    @Autowired
    private SysResMapper sysResMapper;

    public ServerResponse save(ResModuleParam param){
        ServerResponse serverResponse = BeanValidator.check(param);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        if(checkExist(param.getParentId(),param.getName(),param.getId())){
            return ServerResponse.createByErrorMessage("同一层级下存在相同名称的资源模块");
        }
        SysResModule resModule = SysResModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        resModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        resModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        resModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        resModule.setOperateTime(new Date());

        int resultCount = sysResModuleMapper.insertSelective(resModule);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("新增资源模块成功");
        }else {
            return ServerResponse.createByErrorMessage("新增资源模块失败");
        }
    }

    public ServerResponse update(ResModuleParam param){
        ServerResponse serverResponse = BeanValidator.check(param);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        if(checkExist(param.getParentId(),param.getName(),param.getId())){
            return ServerResponse.createByErrorMessage("同一层级下存在相同名称的资源模块");
        }

        SysResModule beforeResModule = sysResModuleMapper.selectByPrimaryKey(param.getId());
        if(beforeResModule==null){
            return ServerResponse.createByErrorMessage("待更新的资源模块不存在");
        }

        SysResModule afterResModule = SysResModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .id(param.getId())
                .build();

        afterResModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        afterResModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterResModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterResModule.setOperateTime(new Date());

        return updateWithChild(beforeResModule,afterResModule);
    }

    @Transactional
    public ServerResponse updateWithChild(SysResModule before,SysResModule after){
        String newLevelPerfix = after.getLevel();
        String oldLevelPerfix = before.getLevel();

        if(!StringUtils.equals(oldLevelPerfix,newLevelPerfix)){
            List<SysResModule> SysResModuleList = sysResModuleMapper.getChildresModuleListByLevel(oldLevelPerfix);
            if(CollectionUtils.isNotEmpty(SysResModuleList)){
                for(SysResModule resModule:SysResModuleList){
                    String level = resModule.getLevel();
                    if(level.indexOf(oldLevelPerfix)==0){
                        level = newLevelPerfix+level.substring(oldLevelPerfix.length());
                        resModule.setLevel(level);
                    }
                }
                sysResModuleMapper.batchUpdateLevel(SysResModuleList);
            }
        }
        int resultCount = sysResModuleMapper.updateByPrimaryKey(after);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("更新资源模块成功");
        }else {
            return ServerResponse.createByErrorMessage("更新资源模块失败");
        }
    }

    private boolean checkExist(Integer parentId,String resModuleName,Integer resModuleId){
        return sysResModuleMapper.countByNameAndParentId(parentId,resModuleName,resModuleId) > 0;
    }

    private String getLevel(Integer resModuleId){
        SysResModule resModule = sysResModuleMapper.selectByPrimaryKey(resModuleId);
        if(resModule == null){
            return null;
        }
        return resModule.getLevel();
    }

    public ServerResponse delete(int resModuleId) {
        SysResModule resModule = sysResModuleMapper.selectByPrimaryKey(resModuleId);
        if(resModule==null){
            return ServerResponse.createByErrorMessage("当前资源模块不存在");
        }
        if(sysResModuleMapper.countByParentId(resModule.getId())>0){
            return ServerResponse.createByErrorMessage("当前资源模块下有子模块，无法删除");
        }
        if(sysResMapper.countByResModuleId(resModule.getId())>0){
            return ServerResponse.createByErrorMessage("当前资源模块下有资源，无法删除");
        }
        int resultCount = sysResModuleMapper.deleteByPrimaryKey(resModuleId);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("删除资源模块成功");
        }else {
            return ServerResponse.createByErrorMessage("删除资源模块失败");
        }
    }
}
