package com.lwq.dto;

import com.lwq.model.SysRes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @Author: Lwq
 * @Date: 2019/3/15 16:11
 * @Version 1.0
 * @Describe
 */
@Getter
@Setter
@ToString
public class ResDto extends SysRes {

    //是否要默认选中
    private boolean checked = false;

    public static ResDto adapt(SysRes res){
        ResDto dto = new ResDto();
        BeanUtils.copyProperties(res,dto);
        return dto;
    }
}
