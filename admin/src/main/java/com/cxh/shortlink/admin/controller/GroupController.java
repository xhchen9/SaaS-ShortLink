package com.cxh.shortlink.admin.controller;

import com.cxh.shortlink.admin.common.convention.result.Result;
import com.cxh.shortlink.admin.common.convention.result.Results;
import com.cxh.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.cxh.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.cxh.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.cxh.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.cxh.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增短链接分组
     * @param shortLinkGroupSaveReqDTO
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO shortLinkGroupSaveReqDTO){
        groupService.save(shortLinkGroupSaveReqDTO.getName());
        return Results.success();
    }

    /**
     * 根据ThreadLocal中用户名查询短链接分组集合
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup(){
        return Results.success(groupService.listGroup());
    }

    /**
     * 修改短链接分组信息，只更改名称
     * @param shortLinkGroupUpdateReqDTO
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/group")
    public Result<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO){
        groupService.updateGroup(shortLinkGroupUpdateReqDTO);
        return Results.success();
    }

    /**
     * 删除本地用户的短链接分组
     * @param gid
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid){
        groupService.deleteGroup(gid);
        return Results.success();
    }

    /**
     * 短链接分组排序（前端已排序好，只需要修改数据库
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> requestParam){
        groupService.sortGroup(requestParam);
        return Results.success();
    }
}
