package com.cxh.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxh.shortlink.admin.common.constant.RedisCacheConstant;
import com.cxh.shortlink.admin.common.convention.exception.ClientException;
import com.cxh.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.cxh.shortlink.admin.dao.entity.UserDO;
import com.cxh.shortlink.admin.dao.mapper.UserMapper;
import com.cxh.shortlink.admin.dto.req.UserLoginReqDTO;
import com.cxh.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cxh.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.cxh.shortlink.admin.dto.resp.UserRespDTO;
import com.cxh.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 通过用户名查询用户
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;  // 构造器加载方式

    private final RedissonClient redissonClient;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        // 避免使用字符串表示字段，防止运行时错误（ user_name ----> User::getName
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(userDO, res);
        return res;
    }

    /**
     * 判断用户名是否可用(使用布隆过滤器
     * @param username
     * @return
     */
    @Override
    public Boolean hasUsername(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    /**
     * 注册用户
     * @param requestParam
     */
    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if (!hasUsername(requestParam.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }
        // Redisson分布式锁防止海量用户请求注册同一用户名
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY+requestParam.getUsername());
        try {
            if (lock.tryLock()){
                int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                if (inserted < 1) {
                    throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());  // 添加至布隆过滤器
                return;
            }
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 用户修改个人信息
     * @param userRegisterReqDTO
     */
    @Override
    public void update(UserRegisterReqDTO userRegisterReqDTO) {
        // TODO 暂未完成
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, userRegisterReqDTO.getUsername());
        baseMapper.update(BeanUtil.toBean(userRegisterReqDTO, UserDO.class), updateWrapper);
    }

    /**
     * 用户登录
     * @param requestParam
     * @return
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + requestParam.getUsername());
        if (BooleanUtil.isTrue(hasLogin)){
            throw new ClientException("用户已存在");
        }

        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put("login_"+requestParam.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire("login_" + requestParam.getUsername(), 30, TimeUnit.DAYS);

        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get("login_" + username, token) != null;
    }

    @Override
    public void logout(String username, String token) {
        if (checkLogin(username, token)){
            stringRedisTemplate.delete("login_" + username);
            return;
        }
        throw new ClientException("用户Token不存在或用户未登录!");
    }
}
