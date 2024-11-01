package com.daym.oauth2.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/10/31
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
public class AuthRequest {
    private String username;
    private String password;

    // getters and setters
}
