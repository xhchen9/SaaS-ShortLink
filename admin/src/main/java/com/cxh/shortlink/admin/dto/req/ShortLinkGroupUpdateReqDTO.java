package com.cxh.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 短链接分组修改
 */
@Data
public class ShortLinkGroupUpdateReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;
}
