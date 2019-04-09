package com.lwq.model;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SysRes {
    private Integer id;

    private String name;

    private Integer resModuleId;

    private Integer seq;

    private Integer count;

    private String code;

    private Integer price;

    private String unit;

    private String manu;

    private Integer status;

    private String remark;

    private String operator;

    private Date operateTime;

    private String operateIp;
}