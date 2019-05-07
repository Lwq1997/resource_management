package com.lwq.model;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SysUser {
    private Integer id;

    private String username;

    private String telephone;

    private String mail;

    private String password;

    private Integer deptId;

    private Integer status;

    private String remark;

    private String operator;

    private Date operateTime;

    private String operateIp;

    private String token;
}