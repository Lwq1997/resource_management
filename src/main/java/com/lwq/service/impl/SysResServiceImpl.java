package com.lwq.service.impl;

import com.google.common.collect.Lists;
import com.lwq.beans.PageQuery;
import com.lwq.beans.PageResult;
import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.dao.SysResMapper;
import com.lwq.dao.SysRoleResMapper;
import com.lwq.dao.SysRoleUserMapper;
import com.lwq.model.SysRes;
import com.lwq.param.ResParam;
import com.lwq.service.ISysResService;
import com.lwq.util.BeanValidator;
import com.lwq.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/3/14 20:52
 * @Version 1.0
 * @Describe
 */
@Service
public class SysResServiceImpl implements ISysResService {

    @Autowired
    SysResMapper sysResMapper;

    @Autowired
    SysRoleResMapper sysRoleResMapper;

    @Autowired
    SysRoleUserMapper sysRoleUserMapper;


    public ServerResponse save(ResParam param){
        ServerResponse serverResponse = BeanValidator.check(param);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        if(checkExist(param.getResModuleId(), param.getName(),param.getId())){
            return ServerResponse.createByErrorMessage("当前资源模块下面存在相同名称的资源");
        }

        SysRes res = SysRes.builder()
                .name(param.getName())
                .resModuleId(param.getResModuleId())
                .unit(param.getUnit())
                .price(param.getPrice())
                .manu(param.getManu())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        res.setCode(generateCode());
        res.setOperator(RequestHolder.getCurrentUser().getUsername());
        res.setOperateTime(new Date());
        res.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        int resultCount = sysResMapper.insertSelective(res);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("新增资源成功");
        }else {
            return ServerResponse.createByErrorMessage("新增资源失败");
        }
    }

    public ServerResponse update(ResParam param) {

        if(StringUtils.isBlank(param.getCode())){
            return ServerResponse.createByErrorMessage("参数错误，更新请传递资源的唯一标识符");
        }

        BeanValidator.check(param);
        if (checkExist(param.getResModuleId(), param.getId() ,param.getCode())) {
            return ServerResponse.createByErrorMessage("当前资源模块下面存在相同的资源,请检查您的唯一标识");
        }

        SysRes before = sysResMapper.selectByPrimaryKey(param.getId());
        if(before==null){
            return ServerResponse.createByErrorMessage("待更新的资源不存在");
        }

        //TODO
        SysRes after = SysRes.builder()
                .id(param.getId())
                .name(param.getName())
                .resModuleId(param.getResModuleId())
                .unit(param.getUnit())
                .price(param.getPrice())
                .manu(param.getManu())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        int resultCount = sysResMapper.updateByPrimaryKeySelective(after);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("更新资源成功");
        }else {
            return ServerResponse.createByErrorMessage("更新资源失败");
        }
    }

    @Override
    public ServerResponse getPageByResModuleId(Integer resModuleId, PageQuery page) {
        ServerResponse serverResponse = BeanValidator.check(page);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        int count = sysResMapper.countByResModuleId(resModuleId);
        if (count > 0) {
            List<SysRes> resList = sysResMapper.getPageByResModuleId(resModuleId, page);
            PageResult<SysRes> pageResult = PageResult.<SysRes>builder().data(resList).total(count).build();
            return ServerResponse.createBySuccess(pageResult);
        }
        PageResult<SysRes> pageResult = PageResult.<SysRes>builder().build();
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse detail(Integer resId) {
        SysRes sysRes = sysResMapper.selectByPrimaryKey(resId);
        if(sysRes==null){
            return ServerResponse.createByErrorMessage("该资源不存在");
        }
        return ServerResponse.createBySuccess(sysRes);
    }

    /**
     * 查看当前角色能够操作的资源
     * @param roleId
     * @return
     */
    public List<SysRes> getRoleResList(int roleId){
        List<Integer> resIdList = sysRoleResMapper.getResIdListByRoleIdList(Lists.newArrayList(roleId));
        if(CollectionUtils.isEmpty(resIdList)){
            return Lists.newArrayList();
        }
        return sysResMapper.getByIdList(resIdList);
    }

    /**
     * 查看当前用户能够操作的资源
     * @param userId
     * @return
     */
    @Override
    public List<SysRes> getUserResList(int userId) {
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if(CollectionUtils.isEmpty(userRoleIdList)){
            return Lists.newArrayList();
        }

        List<Integer> roleAclIdList = sysRoleResMapper.getResIdListByRoleIdList(userRoleIdList);
        if(CollectionUtils.isEmpty(roleAclIdList)){
            return Lists.newArrayList();
        }

        return sysResMapper.getByIdList(roleAclIdList);
    }

    public boolean checkExist(int resModuleId, String name, Integer id) {
        return sysResMapper.countByNameAndResModuleId(resModuleId, name, id) > 0;
    }

    private boolean checkExist(Integer resModuleId, Integer id, String code) {
        return sysResMapper.countByCodeAndResModuleId(resModuleId, id,code) > 0;
    }

    public String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }
}
