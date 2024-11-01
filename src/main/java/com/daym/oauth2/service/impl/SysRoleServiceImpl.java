package com.daym.oauth2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daym.oauth2.entity.SysRole;
import com.daym.oauth2.mapper.SysRoleMapper;
import com.daym.oauth2.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public SysRole createRole(SysRole role) {
        return null;
    }

    @Override
    public SysRole findRoleById(Long id) {
        return null;
    }
    // 业务逻辑实现
}
