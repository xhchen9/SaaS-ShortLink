package com.cxh.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxh.shortlink.admin.dao.entity.UserDO;
import com.cxh.shortlink.admin.dto.req.UserLoginReqDTO;
import com.cxh.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cxh.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.cxh.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);

    Boolean hasUsername(String username);

    void register(UserRegisterReqDTO requestParam);

    void update(UserRegisterReqDTO userRegisterReqDTO);

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    Boolean checkLogin(String username, String token);

    void logout(String username, String token);
}
