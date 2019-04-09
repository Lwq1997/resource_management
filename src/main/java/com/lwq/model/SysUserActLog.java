package com.lwq.model;

import lombok.*;

import java.util.Date;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SysUserActLog {
    private Integer id;

    private Integer userId;

    private String userAct;

    private Date operateTime;
}