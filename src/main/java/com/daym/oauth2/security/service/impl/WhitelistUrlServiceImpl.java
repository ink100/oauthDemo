package com.daym.oauth2.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daym.oauth2.security.entity.WhitelistUrl;
import com.daym.oauth2.security.mapper.WhitelistUrlMapper;
import com.daym.oauth2.security.service.WhitelistUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/11/1
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Service
public class WhitelistUrlServiceImpl extends ServiceImpl<WhitelistUrlMapper, WhitelistUrl> implements WhitelistUrlService {

    @Override
    // 从数据库获取所有白名单 URL
    public List<String> getWhitelistUrls() {
        List<WhitelistUrl> list = this.list();
        return list.stream().map(WhitelistUrl::getUrl).collect(Collectors.toList());
    }

}
