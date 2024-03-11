package com.cxh.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxh.shortlink.admin.dao.entity.GroupDO;
import com.cxh.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.cxh.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.cxh.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {
    public void save(String name);

    List<ShortLinkGroupRespDTO> listGroup();

    void updateGroup(ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO);

    void deleteGroup(String gid);

    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);
}
