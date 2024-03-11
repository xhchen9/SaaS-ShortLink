package com.cxh.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginRespDTO {
    /**
     * 用户登录时返回的token
     */
    private String token;
}
