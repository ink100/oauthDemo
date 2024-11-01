package com.daym.oauth2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_log")
public class SysLoginLog extends BaseEntity {

    private Long userId;

    private String loginMethod;

    private String ipAddress;

    private LocalDateTime loginTime;
}
