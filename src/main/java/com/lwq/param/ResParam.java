package com.lwq.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 22:41
 * @Version 1.0
 * @Describe
 */
@Getter
@Setter
@ToString
public class ResParam {

    private Integer id;

    @NotBlank(message = "资源名称不能为空")
    @Length(min = 2,max = 50,message = "资源名称长度在2-50个字之间")
    private String name;

    @NotNull(message = "必须指定资源模块")
    private Integer resModuleId;

    @NotNull(message = "必须指定资源价格")
    private Integer price = 1;

    @NotNull(message = "必须指定资源单位")
    private String unit = "个";

    @NotNull(message = "必须指定资源生产厂家")
    private String manu;

    @NotNull(message = "必须指定资源的状态")
    @Min(value = 0,message = "资源的状态不合法")
    @Max(value = 2,message = "资源的状态不合法")
    private Integer status;

    @NotNull(message = "必须指定资源的展示顺序")
    private Integer seq;

    private String code;

    @Length(min = 0,max = 200,message = "资源点备注信息在0-200之间")
    private String remark;

}
