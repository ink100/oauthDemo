package com.daym.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daym.oauth2.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SysUserService extends IService<SysUser> {
    SysUser register(SysUser user);

    SysUser login(String username, String password);

    SysUser findById(Long id);
}
