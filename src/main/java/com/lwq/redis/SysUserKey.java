package com.lwq.redis;

/**
 * @Author: Lwq
 * @Date: 2019/3/19 12:31
 * @Version 1.0
 * @Describe
 */
public class SysUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24*2;
    public SysUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SysUserKey token = new SysUserKey(TOKEN_EXPIRE,"tk");
    public static SysUserKey getByName = new SysUserKey(0,"name");


}
