package com.lwq.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.lwq.dao.SysDeptMapper;
import com.lwq.dao.SysResMapper;
import com.lwq.dao.SysResModuleMapper;
import com.lwq.dto.DeptLevelDto;
import com.lwq.dto.ResDto;
import com.lwq.dto.ResModuleLevelDto;
import com.lwq.model.SysDept;
import com.lwq.model.SysRes;
import com.lwq.model.SysResModule;
import com.lwq.service.ISysResService;
import com.lwq.service.ISysRoleService;
import com.lwq.service.ISysTreeService;
import com.lwq.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Lwq
 * @Date: 2019/3/11 22:28
 * @Version 1.0
 * @Describe
 */
@Service
public class SysTreeServiceImpl implements ISysTreeService {

    @Autowired
    SysDeptMapper sysDeptMapper;

    @Autowired
    SysResModuleMapper sysResModuleMapper;

    @Autowired
    SysResMapper sysResMapper;

    @Autowired
    ISysRoleService iSysRoleService;

    @Autowired
    ISysResService iSysResService;


    /**
     * 部门树
     * @return
     */
    public List<DeptLevelDto> deptTree(){
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> deptDtoList = Lists.newArrayList();

        for(SysDept dept:deptList){
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            deptDtoList.add(dto);
        }
        return deptListToTree(deptDtoList);
    }

    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptDtoList){
        if(CollectionUtils.isEmpty(deptDtoList)){
            return Lists.newArrayList();
        }
//        level -> [dept1,dept2....]
//        Map<String,List<Object>>
        Multimap<String,DeptLevelDto> levelDtoMultiMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();

        for(DeptLevelDto dto:deptDtoList){
            levelDtoMultiMap.put(dto.getLevel(),dto);
            if(LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }

        //按照seq排序,处理第一层
        Collections.sort(rootList,deptSeqComparator);

        //递归生成树（从第二层开始）
        transformDeptTree(rootList, LevelUtil.ROOT,levelDtoMultiMap);
        return rootList;
    }

    //level:0 0,all
    public void transformDeptTree(List<DeptLevelDto> deptLevelList,String level,Multimap<String,DeptLevelDto> levelDtoMultiMap){
        for(int  i = 0; i < deptLevelList.size(); i++){
            //遍历该层的每个元素
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level,deptLevelDto.getId());
            //处理下一层
            List<DeptLevelDto> temoDeptList = (List<DeptLevelDto>) levelDtoMultiMap.get(nextLevel);
            if(CollectionUtils.isNotEmpty(temoDeptList)){
                //排序
                Collections.sort(temoDeptList,deptSeqComparator);

                //设置下一层
                deptLevelDto.setDeptList(temoDeptList);

                //处理下一层
                transformDeptTree(temoDeptList,nextLevel,levelDtoMultiMap);
            }
        }
    }

    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    };


    /**
     * 资源模块树
     * @return
     */
    public List<ResModuleLevelDto> resModuleTree(){
        List<SysResModule> resModuleList = sysResModuleMapper.getAllResModule();
        List<ResModuleLevelDto> dtoList = Lists.newArrayList();

        for(SysResModule resModule:resModuleList){
            ResModuleLevelDto dto = ResModuleLevelDto.adapt(resModule);
            dtoList.add(dto);
        }
        return resModuleListToTree(dtoList);
    }

    private List<ResModuleLevelDto> resModuleListToTree( List<ResModuleLevelDto> resModuleLevelList) {
        if(CollectionUtils.isEmpty(resModuleLevelList)){
            return Lists.newArrayList();
        }
//        level -> [resModule1,resModule2....]
//        Map<String,List<Object>>
        Multimap<String,ResModuleLevelDto> levelResModuleMap = ArrayListMultimap.create();
        List<ResModuleLevelDto> rootList = Lists.newArrayList();

        for(ResModuleLevelDto dto:resModuleLevelList){
            levelResModuleMap.put(dto.getLevel(),dto);
            if(LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }

        //按照seq排序,处理第一层
        Collections.sort(rootList, resModuleSeqComparator);

        //递归生成树（从第二层开始）
        transformResModuleTree(rootList,LevelUtil.ROOT,levelResModuleMap);
        return rootList;
    }

    private void transformResModuleTree(List<ResModuleLevelDto> resModuleLevelList, String level, Multimap<String, ResModuleLevelDto> levelResModuleMap) {
        for(int  i = 0; i < resModuleLevelList.size(); i++){
            //遍历该层的每个元素
            ResModuleLevelDto resModuleLevelDto = resModuleLevelList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level,resModuleLevelDto.getId());
            //处理下一层
            List<ResModuleLevelDto> tempResModuleList = (List<ResModuleLevelDto>) levelResModuleMap.get(nextLevel);
            if(CollectionUtils.isNotEmpty(tempResModuleList)){
                //排序
                Collections.sort(tempResModuleList,resModuleSeqComparator);

                //设置下一层
                resModuleLevelDto.setResModuleList(tempResModuleList);

                //处理下一层
                transformResModuleTree(tempResModuleList,nextLevel,levelResModuleMap);
            }
        }
    }

    public Comparator<ResModuleLevelDto> resModuleSeqComparator = new Comparator<ResModuleLevelDto>() {
        @Override
        public int compare(ResModuleLevelDto o1, ResModuleLevelDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    };

    //角色的资源树
    @Override
    public List<ResModuleLevelDto> roleTree(int roleId) {
        //当前角色已经被分配的资源点
        List<SysRes> roleResList = iSysResService.getRoleResList(roleId);
        //系统所有的资源点
        List<SysRes> resList = sysResMapper.getAll();

        //对所有的资源点进行封装
        List<ResDto> resDtoList = Lists.newArrayList();

        Set<Integer> roleResIdList = roleResList.stream()
                .map(sysRes -> sysRes.getId())
                .collect(Collectors.toSet());

        for(SysRes res:resList){
            ResDto dto = ResDto.adapt(res);
            if(roleResIdList.contains(res.getId())){
                dto.setChecked(true);
            }
            resDtoList.add(dto);
        }
        return resListToTree(resDtoList);
    }

    private List<ResModuleLevelDto> resListToTree(List<ResDto> resDtoList) {
        if(CollectionUtils.isEmpty(resDtoList)){
            return Lists.newArrayList();
        }
        //系统所有的资源模块树
        List<ResModuleLevelDto> resModuleLevelList = resModuleTree();

        Multimap<Integer,ResDto> moduleIdResMap = ArrayListMultimap.create();
        for(ResDto res:resDtoList){
            if(res.getStatus()==0){
                moduleIdResMap.put(res.getResModuleId(),res);
            }
        }
        bindRessWithOrder(resModuleLevelList,moduleIdResMap);
        return resModuleLevelList;
    }

    private void bindRessWithOrder(List<ResModuleLevelDto> resModuleLevelList, Multimap<Integer, ResDto> moduleIdResMap) {
        if(CollectionUtils.isEmpty(resModuleLevelList)){
            return;
        }
        for(ResModuleLevelDto dto:resModuleLevelList){
            List<ResDto> resDtoList = (List<ResDto>) moduleIdResMap.get(dto.getId());
            if(CollectionUtils.isNotEmpty(resDtoList)){
                Collections.sort(resDtoList,resSeqComparator);
                dto.setResList(resDtoList);
            }
            bindRessWithOrder(dto.getResModuleList(),moduleIdResMap);
        }
    }

    public Comparator<ResDto> resSeqComparator = new Comparator<ResDto>() {
        @Override
        public int compare(ResDto o1, ResDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    };
}
