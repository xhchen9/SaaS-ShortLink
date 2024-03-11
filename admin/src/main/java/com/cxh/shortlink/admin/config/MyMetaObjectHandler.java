package com.cxh.shortlink.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * MyBatis-Plus,给更改数据库时，对指定字段自动填充值
 */
@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date::new, Date.class); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "updateTime", Date::new, Date.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "delFlag", () -> 0, Integer.class); // 起始版本 3.3.3(推荐)
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
    }
}