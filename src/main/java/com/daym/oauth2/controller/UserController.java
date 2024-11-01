package com.daym.oauth2.controller;

import com.daym.oauth2.entity.SysUser;
import com.daym.oauth2.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/10/30
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@RestController
@RequestMapping("/users")
public class UserController {

  @Resource
  private SysUserService userService;

  @PostMapping("/register")
  public SysUser register(@RequestBody SysUser user) {
    return userService.register(user);
  }

  @PostMapping("/login")
  public SysUser login(@RequestParam String username, @RequestParam String password) {
    return userService.login(username, password);
  }
}