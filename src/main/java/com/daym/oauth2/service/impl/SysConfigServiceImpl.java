package com.daym.oauth2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daym.oauth2.entity.SysConfig;
import com.daym.oauth2.mapper.SysConfigMapper;
import com.daym.oauth2.service.SysConfigService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
    // 业务逻辑实现
}
