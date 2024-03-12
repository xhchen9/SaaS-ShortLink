package com.cxh.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cxh.shortlink.project.common.convention.result.Result;
import com.cxh.shortlink.project.common.convention.result.Results;
import com.cxh.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cxh.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.cxh.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.cxh.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.cxh.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/project/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    @GetMapping("/api/short-link/project/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam){
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }
}
