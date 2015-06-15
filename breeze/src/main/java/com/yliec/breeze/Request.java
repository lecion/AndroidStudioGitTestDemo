package com.yliec.breeze;

import java.util.HashMap;

/**
 * Created by Lecion on 6/14/15.
 */
public abstract class Request<T> implements Comparable<Request<T>>{

    /**
     * 请求方法
     */
    public static class Method {
        public static final String GET = "get";
        public static final String POST = "post";
        public static final String PUT = "put";
        public static final String DELETE = "delete";
    }

    /**
     * 优先级
     */
    public static class Priority {
        public static final int LOW = 0;
        public static final int NORMAL = 1;
        public static final int HIGH = 1;
        public static final int IMMEDIATE = 1;

    }

    /**
     * 请求url
     */
    private String url = "";

    /**
     * 请求方法
     */
    private String method = Method.GET;

    /**
     * 请求头
     */
    private HashMap<String, String> headers;


    /**
     * 请求参数
     */
    private HashMap<String, String> params;

    /**
     * 请求编码
     */
    private static final String DEFUALT_PARAMS_ENCODING = "UTF-8";

    /**
     * 是否缓存，默认不缓存
     */
    private boolean shouldCache = false;

    /**
     * 是否取消该请求
     */
    private boolean isCancel = false;

    /**
     * 请求序号
     */
    private int serialNum;

    private int priority = Priority.NORMAL;

    private RequestListener requestListener;

    /**
     * 解析响应结果，子类可根据需求复写该方法
     * @param response
     * @return
     */
    public abstract T parseResponse(Response response);

    /**
     * 分发响应结果，该方法在UI线程运行
     * @param response
     */
    public void deliveryResponse(Response response) {
        T result = parseResponse(response);
        if (requestListener != null) {

        }
    }

    /**
     * 请求回调接口
     * @param <T>
     */
    public interface RequestListener<T> {
        void onComplete(int statusCode, T response, String msg);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String defaultMethod) {
        this.method = defaultMethod;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void cancel() {
        this.isCancel = true;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    @Override
    public int compareTo(Request<T> another) {
        //优先级相等，按序列号排序，否则按优先级
        return this.priority == another.priority ? this.serialNum - another.serialNum : this.priority - another.priority;
    }

}
