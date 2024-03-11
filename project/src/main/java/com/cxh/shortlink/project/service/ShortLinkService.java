package com.cxh.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxh.shortlink.project.dao.entity.ShortLinkDO;
import com.cxh.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cxh.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

public interface ShortLinkService extends IService<ShortLinkDO> {
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);
}
