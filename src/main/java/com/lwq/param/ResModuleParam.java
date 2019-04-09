package com.lwq.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author: Lwq
 * @Date: 2019/3/30 19:26
 * @Version 1.0
 * @Describe
 */
@Getter
@Setter
@ToString
public class ResModuleParam {

    private Integer id;

    @NotBlank(message = "资源模块名称不能为空")
    @Length(min = 2,max = 20,message = "资源模块名称的长度需要在2到20字之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "资源模块展示顺序不能为空")
    private Integer seq;

    @Length(max = 200,message = "资源模块的备注信息需要在200字以内")
    private String remark;
}
