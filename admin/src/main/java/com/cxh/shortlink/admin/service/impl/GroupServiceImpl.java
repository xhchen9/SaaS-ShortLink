package com.cxh.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxh.shortlink.admin.common.biz.user.UserContext;
import com.cxh.shortlink.admin.common.database.BaseDO;
import com.cxh.shortlink.admin.dao.entity.GroupDO;
import com.cxh.shortlink.admin.dao.mapper.GroupMapper;
import com.cxh.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.cxh.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.cxh.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.cxh.shortlink.admin.service.GroupService;
import com.cxh.shortlink.admin.toolkit.RandomGenerator;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    /**
     * 保存短链接分组
     * @param username
     */
    @Override
    public void save(String name){
        // 生成一个没有使用过的GID
        String gid;
        while (true){
            gid = RandomGenerator.generateRandom();
            if (hasGid(gid)){
                break;
            }
        }
        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .username(UserContext.getUsername())
                .name(name)
                .build();

        baseMapper.insert(groupDO);
    }

    /**
     * 查询分组
     * @return
     */
    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(BaseDO::getDelFlag, 0)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);

        List<GroupDO> groupDOS = baseMapper.selectList(queryWrapper);

        return BeanUtil.copyToList(groupDOS, ShortLinkGroupRespDTO.class);
    }

    /**
     * 短链接分组修改
     * @param shortLinkGroupUpdateReqDTO
     */
    @Override
    public void updateGroup(ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, shortLinkGroupUpdateReqDTO.getGid())
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(BaseDO::getDelFlag, 0);
        String username = UserContext.getUsername();
        GroupDO groupDO = new GroupDO();
        groupDO.setName(shortLinkGroupUpdateReqDTO.getName());

        baseMapper.update(groupDO, updateWrapper);
    }

    /**
     * 删除本地用户的短链接分组
     * @param gid
     */
    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(BaseDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);
    }

    /**
     * 前端传入排序后字段，后端按排序值修改数据库排序字段值
     * @param requestParam
     */
    @Override
    public void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam) {
        requestParam.forEach(each ->{
            GroupDO groupDO = GroupDO.builder()
                    .sortOrder(each.getSortOrder())
                    .build();

            LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getGid, each.getGid())
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getDelFlag, 0);

            baseMapper.update(groupDO, updateWrapper);
        });
    }

    private Boolean hasGid(String gid) {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, gid);
        GroupDO hasGroupFlag = baseMapper.selectOne(wrapper);
        return hasGroupFlag == null;
    }
}
