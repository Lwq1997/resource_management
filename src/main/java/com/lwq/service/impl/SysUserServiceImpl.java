package com.lwq.service.impl;

import com.google.common.collect.Lists;
import com.lwq.beans.MailInfo;
import com.lwq.beans.PageQuery;
import com.lwq.beans.PageResult;
import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.dao.SysResMapper;
import com.lwq.dao.SysUserActLogMapper;
import com.lwq.dao.SysUserMapper;
import com.lwq.dao.SysUserRecordMapper;
import com.lwq.model.SysRes;
import com.lwq.model.SysUser;
import com.lwq.model.SysUserActLog;
import com.lwq.model.SysUserRecord;
import com.lwq.param.UserParam;
import com.lwq.service.ISysResService;
import com.lwq.service.ISysUserService;
import com.lwq.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/3/14 13:40
 * @Version 1.0
 * @Describe
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    ISysResService iSysResService;

    @Autowired
    SysUserRecordMapper sysUserRecordMapper;

    @Autowired
    SysResMapper sysResMapper;

    @Autowired
    SysUserActLogMapper sysUserActLogMapper;

    public ServerResponse save(UserParam param){
        ServerResponse serverResponse = BeanValidator.check(param);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        if(checkTelephoneExist(param.getTelephone(),param.getId())){
            return ServerResponse.createBySuccessMessage("电话已被占用");
        }
        if(checkEmailExist(param.getMail(),param.getId())){
            return ServerResponse.createBySuccessMessage("邮箱已被占用");
        }

        String password = PasswordUtil.randomPassword();
        String encryptedPassword = MD5Util.MD5EncodeUtf8(password);
        SysUser user = SysUser.builder()
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(encryptedPassword)
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();

        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        MailInfo mailInfo = new MailInfo();
        List<String> toList = new ArrayList<String>();
        toList.add(user.getMail());

        mailInfo.setToAddress(toList);//收件人

        mailInfo.setSubject("欢迎使用国资管理系统，请查收密码");
        mailInfo.setContent("内容："+password);

        MailUtil.sendEmail(mailInfo);

        int resultCount = sysUserMapper.insertSelective(user);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("新增用户成功");
        }else {
            return ServerResponse.createByErrorMessage("新增用户失败");
        }
    }

    public ServerResponse update(UserParam param){
        ServerResponse serverResponse = BeanValidator.check(param);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        if(checkTelephoneExist(param.getTelephone(),param.getId())){
            return ServerResponse.createBySuccessMessage("电话已被占用");
        }
        if(checkEmailExist(param.getMail(),param.getId())){
            return ServerResponse.createBySuccessMessage("邮箱已被占用");
        }

        SysUser beforeUser = sysUserMapper.selectByPrimaryKey(param.getId());
        if(beforeUser==null){
            return ServerResponse.createBySuccessMessage("待更新的用户不存在");
        }

        SysUser afterUser = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(beforeUser.getPassword())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();

        afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());//TODO
        afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterUser.setOperateTime(new Date());

        int resultCount = sysUserMapper.updateByPrimaryKeySelective(afterUser);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("更新用户成功");
        }else {
            return ServerResponse.createByErrorMessage("更新用户失败");
        }
    }

    public ServerResponse getPageByDeptId(int deptId, PageQuery page) {

        ServerResponse serverResponse = BeanValidator.check(page);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            PageResult<SysUser> pageResult = PageResult.<SysUser>builder().total(count).data(list).build();
            return ServerResponse.createBySuccess(pageResult);
        }
        PageResult<SysUser> pageResult = PageResult.<SysUser>builder().build();
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }

    //查看该用户没有借用的所有资源
    public List<SysRes> userNoHaveRes(int userId){
        List<SysRes> userAllResList = iSysResService.getUserResList(userId);
        List<SysRes> userNoHaveResList = Lists.newArrayList();
        for(SysRes sysRes:userAllResList){
            //等于0说明没有被借出
            if(sysRes.getStatus()==0){
                userNoHaveResList.add(sysRes);
            }
        }
        return userNoHaveResList;
    }

    //查看该用户借用的所有资源
    public List<SysRes> userHaveRes(int userId){
        List<Integer> resIdList = sysUserRecordMapper.selectResIdByUserId(userId);
        if(CollectionUtils.isEmpty(resIdList)){
            return Lists.newArrayList();
        }
        return sysResMapper.getByIdList(resIdList);
    }

    @Override
    public ServerResponse borrow(int userId, int resId) {
        Date date = new Date();
        String formatStr = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss").format(date);
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        List<SysRes> sysResList = this.userNoHaveRes(userId);
        for(SysRes sysRes:sysResList){
            if(resId == sysRes.getId() && sysRes.getStatus() == 0){
                SysUserRecord sysUserRecord = SysUserRecord.builder()
                        .userId(userId)
                        .resId(resId)
                        .operateTime(date)
                        .build();
                int resultCount = sysUserRecordMapper.insert(sysUserRecord);
                if(resultCount>0){
                    sysRes.setStatus(1);
                    resultCount = sysResMapper.updateByPrimaryKeySelective(sysRes);
                    if(resultCount>0){
                        sysResMapper.updateByPrimaryKeySelective(sysRes);
                        SysUserActLog actLog = SysUserActLog.builder()
                                .userId(userId)
                                .operateTime(date)
                                .userAct(formatStr+"用户"+user.getUsername()+"借用了"+sysRes.getName()+"资源")
                                .build();
                        sysUserActLogMapper.insert(actLog);
                        return ServerResponse.createBySuccessMessage("资源借用成功");
                    }else {
                        return ServerResponse.createByErrorMessage("资源借用失败");
                    }
                }else {
                    return ServerResponse.createByErrorMessage("资源借用失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("您没有权限借用该资源或该资源已经借出");
    }

    @Override
    public ServerResponse ret(int userId, int resId) {
        Date date = new Date();
        String formatStr = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss").format(date);
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        List<SysRes> sysResList = this.userHaveRes(userId);
        for(SysRes sysRes:sysResList){
            if(resId == sysRes.getId() && sysRes.getStatus() == 1){
                sysUserRecordMapper.delete(userId,resId);
                sysRes.setStatus(0);
                int resultCount = sysResMapper.updateByPrimaryKeySelective(sysRes);
                if(resultCount>0){
                    SysUserActLog actLog = SysUserActLog.builder()
                            .userId(userId)
                            .operateTime(date)
                            .userAct(formatStr+"用户"+user.getUsername()+"归还了"+sysRes.getName()+"资源")
                            .build();
                    sysUserActLogMapper.insert(actLog);
                    return ServerResponse.createBySuccessMessage("资源归还成功");
                }else {
                    return ServerResponse.createByErrorMessage("资源归还失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("您没有借用该资源或该资源已经归还");
    }

    @Override
    public ServerResponse find(int userId) {
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        List<String> re = sysUserActLogMapper.find(userId);
        return ServerResponse.createBySuccess(re);
    }

    public boolean checkEmailExist(String mail,Integer userId){
        return sysUserMapper.countByMail(mail,userId)>0;
    }

    public boolean checkTelephoneExist(String telephone,Integer userId){
        return sysUserMapper.countByTelephone(telephone,userId)>0;
    }
}
