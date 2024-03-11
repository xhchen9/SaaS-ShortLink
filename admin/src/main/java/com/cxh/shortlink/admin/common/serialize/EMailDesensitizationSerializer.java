package com.cxh.shortlink.admin.common.serialize;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EMailDesensitizationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String email, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String email1 = DesensitizedUtil.email(email);
        jsonGenerator.writeString(email1);
    }
}
