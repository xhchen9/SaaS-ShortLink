package com.cxh.shortlink.admin.dto.resp;

import com.cxh.shortlink.admin.common.serialize.EMailDesensitizationSerializer;
import com.cxh.shortlink.admin.common.serialize.PhoneDesensitizationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * 与前端交互，回应
 */
@Data
public class UserRespDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;


    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    @JsonSerialize(using = EMailDesensitizationSerializer.class)
    private String mail;

}
