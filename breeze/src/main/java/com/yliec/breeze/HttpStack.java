package com.yliec.breeze;

/**
 * @Author Lecion
 * @Date 6/16/15
 * @Email lecion@icloud.com
 */
public interface HttpStack {
    Response performRequest(Request<?> request);
}
