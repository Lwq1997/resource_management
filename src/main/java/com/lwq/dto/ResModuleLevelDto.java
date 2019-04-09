package com.lwq.dto;

import com.google.common.collect.Lists;
import com.lwq.model.SysResModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/4/10 20:04
 * @Version 1.0
 * @Describe
 */
@Getter
@Setter
@ToString
public class ResModuleLevelDto extends SysResModule {

    private List<ResModuleLevelDto> resModuleList = Lists.newArrayList();

    private List<ResDto> resList = Lists.newArrayList();
    /**
     * 把传入的的SysResModule对象复制到ResModuleLevelDto
     * @param resModule
     * @return
     */
    public static ResModuleLevelDto adapt(SysResModule resModule){
        ResModuleLevelDto dto = new ResModuleLevelDto();
        BeanUtils.copyProperties(resModule,dto);
        return dto;
    }
}
