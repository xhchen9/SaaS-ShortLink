package com.cxh.shortlink.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cxh.shortlink.admin.common.convention.result.Result;
import com.cxh.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.cxh.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.cxh.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.cxh.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.cxh.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {
    ShortLinkRemoteService shortLinkService = new ShortLinkRemoteService() {
    };
    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return shortLinkService.createShortLink(requestParam);
    }

    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam){

        return shortLinkService.pageShortLink(requestParam);
    }
}
