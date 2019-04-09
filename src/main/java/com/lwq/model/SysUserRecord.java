package com.lwq.model;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SysUserRecord {
    private Integer id;

    private Integer userId;

    private Integer resId;

    private Integer type;

    private Date operateTime;

}