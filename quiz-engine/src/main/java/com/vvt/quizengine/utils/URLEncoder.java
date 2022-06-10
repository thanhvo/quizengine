package com.vvt.quizengine.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Component
public class URLEncoder {

    public String encode(String path) throws UnsupportedEncodingException {
        return Base64Utils.encodeToString(path.getBytes(StandardCharsets.UTF_8));
    }

    public String decode(String encodedPath) {
        return new String(Base64Utils.decode(encodedPath.getBytes(StandardCharsets.UTF_8)));
    }

}
