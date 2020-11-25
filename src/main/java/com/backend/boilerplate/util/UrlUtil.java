package com.backend.boilerplate.util;

import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sarvesh
 * @version 0.0.3
 * @since 0.0.3
 */
public final class UrlUtil {
    private UrlUtil() {
        throw new AssertionError();
    }

    public static String encodeQueryString(String queryString) {
        String[] params = queryString.split("&");
        return
            Stream.of(params)
                .map(s -> s.split("="))
                .map(arr -> arr[0] + "=" + UriUtils.encodeQueryParam(arr[1], StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}
