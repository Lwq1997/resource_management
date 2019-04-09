package com.lwq.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lwq.common.RequestHolder;
import com.lwq.dao.SysRoleResMapper;
import com.lwq.model.SysRoleRes;
import com.lwq.service.ISysRoleResService;
import com.lwq.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: Lwq
 * @Date: 2019/3/15 17:36
 * @Version 1.0
 * @Describe
 */
@Service
public class SysRoleResServiceImpl implements ISysRoleResService {

    @Resource
    private SysRoleResMapper sysRoleResMapper;

    @Override
    public void changeRoleRess(int roleId, List<Integer> resIdList) {
        List<Integer> originResIdList = sysRoleResMapper.getResIdListByRoleIdList(Lists.newArrayList(roleId));
        if(originResIdList.size()==resIdList.size()){
            Set<Integer> originResIdSet = Sets.newHashSet(originResIdList);
            Set<Integer> resIdSet = Sets.newHashSet(resIdList);

            originResIdSet.removeAll(resIdSet);
            if(CollectionUtils.isEmpty(originResIdSet)){
                return;
            }
        }
        updateRoleRess(roleId,resIdList);
    }

    @Transactional
    public void updateRoleRess(Integer roleId, List<Integer> resIdList) {
        sysRoleResMapper.deleteByRoleId(roleId);

        if(CollectionUtils.isEmpty(resIdList)){
            return;
        }
        List<SysRoleRes> roleResList = Lists.newArrayList();
        for(Integer resId:resIdList){
            SysRoleRes roleRes = SysRoleRes.builder()
                    .roleId(roleId)
                    .resId(resId)
                    .build();
            roleResList.add(roleRes);
        }
        sysRoleResMapper.batchInsert(roleResList);
    }
}
