package com.cxh.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxh.shortlink.project.dao.entity.ShortLinkDO;
import com.cxh.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cxh.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.cxh.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.cxh.shortlink.project.dto.resp.ShortLinkPageRespDTO;

public interface ShortLinkService extends IService<ShortLinkDO> {
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);
}
