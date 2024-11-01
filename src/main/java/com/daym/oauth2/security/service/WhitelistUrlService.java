package com.daym.oauth2.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daym.oauth2.security.entity.WhitelistUrl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/11/1
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 * Description:
 */

public interface WhitelistUrlService extends IService<WhitelistUrl> {
    public List<String> getWhitelistUrls();

}