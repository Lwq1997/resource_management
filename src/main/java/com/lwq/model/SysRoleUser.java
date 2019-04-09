package com.lwq.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SysRoleUser {
    private Integer id;

    private Integer roleId;

    private Integer userId;

}