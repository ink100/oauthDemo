package com.daym.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daym.oauth2.entity.SysUser;
import com.daym.oauth2.mapper.SysUserMapper;
import com.daym.oauth2.service.SysUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService , UserDetailsService {
    @Override
    public SysUser register(SysUser user) {
        return null;
    }

    @Override
    public SysUser login(String username, String password) {
        return null;
    }

    @Override
    public SysUser findById(Long id) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser =  this.getOne(queryWrapper);
        return User.builder()
                .username(sysUser.getUsername())  // 设置用户名
                .password(sysUser.getPassword())  // 设置密码
                .roles("user")
//                .authorities(user.getAuthorities())  // 设置用户权限
                .accountExpired(false)  // 账户未过期
                .accountLocked(false)   // 账户未锁定
                .credentialsExpired(false) // 凭证未过期
                .disabled(sysUser.getStatus())  // 用户状态 (0: 禁用, 1: 启用)
                .build();
    }
    // 业务逻辑实现
}
