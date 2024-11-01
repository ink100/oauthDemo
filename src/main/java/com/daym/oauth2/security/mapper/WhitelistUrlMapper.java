package com.daym.oauth2.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daym.oauth2.security.entity.WhitelistUrl;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/11/1
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Mapper
public interface WhitelistUrlMapper extends BaseMapper<WhitelistUrl> {
}
