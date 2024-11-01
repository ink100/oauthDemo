package com.daym.oauth2.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/11/1
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
@TableName( "whitelist_urls")
public class WhitelistUrl {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String url;

  private String remark;
}