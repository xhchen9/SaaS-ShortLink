package com.cxh.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxh.shortlink.project.common.convention.exception.ServiceException;
import com.cxh.shortlink.project.dao.entity.ShortLinkDO;
import com.cxh.shortlink.project.dao.mapper.ShortLinkMapper;
import com.cxh.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cxh.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.cxh.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.cxh.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.cxh.shortlink.project.service.ShortLinkService;
import com.cxh.shortlink.project.toolkit.HashUtil;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCachePenetrationBloomFilter;
    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = requestParam.getDomain() + "/" + shortLinkSuffix;

        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);
        shortLinkDO.setFullShortUrl(fullShortUrl);
        shortLinkDO.setShortUri(shortLinkSuffix);
        shortLinkDO.setEnableStatus(0);

        try {
            baseMapper.insert(shortLinkDO);
        }catch (DuplicateKeyException ex){
            LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl);
            ShortLinkDO shortLinkDO1 = baseMapper.selectOne(queryWrapper);
            if (shortLinkDO1 != null){
                log.warn("短链接重复入库");
                throw new ServiceException("短链接重复");
            }
        }
        shortUriCachePenetrationBloomFilter.add(shortLinkSuffix);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .build();
    }

    /**
     * 分页
     * @param requestParam
     * @return
     */
    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);

        return resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }

    private String generateSuffix(ShortLinkCreateReqDTO requestParam){
        String originUrl = requestParam.getOriginUrl();
        String shortUri;
        int count = 0;
        while (true){
            if (count > 10){
                throw new ServiceException("短链接重复，请稍后重试!");
            }
            // 加上当前时间，降低冲突
            originUrl += System.currentTimeMillis();
            shortUri = HashUtil.hashToBase62(originUrl);
            // 布隆过滤器判断短链接是否存在
            if (!shortUriCachePenetrationBloomFilter.contains(shortUri)){
                break;
            }
            count++;
        }
        return HashUtil.hashToBase62(shortUri);
    }
}
