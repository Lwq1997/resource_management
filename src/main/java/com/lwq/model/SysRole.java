package com.lwq.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SysRole {
    private Integer id;

    private String name;

    private String remark;
}