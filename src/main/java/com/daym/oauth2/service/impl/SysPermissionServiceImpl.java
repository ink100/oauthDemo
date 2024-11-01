package com.daym.oauth2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daym.oauth2.entity.SysPermission;
import com.daym.oauth2.mapper.SysPermissionMapper;
import com.daym.oauth2.service.SysPermissionService;
import org.springframework.stereotype.Service;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    @Override
    public SysPermission createPermission(SysPermission permission) {
        return null;
    }

    @Override
    public SysPermission findPermissionById(Long id) {
        return null;
    }
    // 业务逻辑实现
}
