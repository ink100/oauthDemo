package com.daym.oauth2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oauth_client_details")
public class SysOauthClientDetails extends BaseEntity {

  private String clientId;

  private String clientSecret;

  private String scope;

  private String authorizedGrantTypes;

  private String webServerRedirectUri;

  private String authorities;

  private Integer accessTokenValidity;

  private Integer refreshTokenValidity;
}
