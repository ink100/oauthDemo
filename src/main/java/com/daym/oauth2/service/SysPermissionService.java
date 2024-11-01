package com.daym.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daym.oauth2.entity.SysPermission;

import java.security.Permission;

public interface SysPermissionService extends IService<SysPermission> {

    SysPermission createPermission(SysPermission permission);
    SysPermission findPermissionById(Long id);
}
