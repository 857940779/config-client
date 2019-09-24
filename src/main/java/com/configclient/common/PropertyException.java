package com.configclient.common;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * @Author: luohanwen
 * @Date: 2019/9/18 16:53
 */
public class PropertyException extends BeansException {
    public PropertyException(String msg) {
        super(msg);
    }

    public PropertyException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
