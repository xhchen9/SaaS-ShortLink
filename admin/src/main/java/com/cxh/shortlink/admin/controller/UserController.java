package com.cxh.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cxh.shortlink.admin.common.convention.result.Result;
import com.cxh.shortlink.admin.common.convention.result.Results;
import com.cxh.shortlink.admin.dto.req.UserLoginReqDTO;
import com.cxh.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cxh.shortlink.admin.dto.resp.UserActualRespDTO;
import com.cxh.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.cxh.shortlink.admin.dto.resp.UserRespDTO;
import com.cxh.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 根据用户名查询无脱敏用户信息
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }

    /**
     * 判断一个用户名是否存在（用于注册用户
     * @param username
     * GET http://localhost:8002/api/short-link/admin/v1/user/has-username?usernanme=cxh
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> haUsername(@RequestParam("username") String username){
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 注册用户
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 用户修改个人信息
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Void> update(@RequestBody UserRegisterReqDTO requestParam){
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam){
        return Results.success(userService.login(requestParam));
    }

    /**
     * 检测用户是否登录
     * @param token
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam("username") String username, @RequestParam("token") String token){
        return Results.success(userService.checkLogin(username, token));
    }

    /**
     * 用户退出登录
     * @param username
     * @param token
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void> logout(@RequestParam("username") String username, @RequestParam("token") String token){
        userService.logout(username, token);
        return Results.success();
    }
}
