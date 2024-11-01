package com.daym.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daym.oauth2.entity.SysRole;

public interface SysRoleService extends IService<SysRole> {
    SysRole createRole(SysRole role);

    SysRole findRoleById(Long id);
}
