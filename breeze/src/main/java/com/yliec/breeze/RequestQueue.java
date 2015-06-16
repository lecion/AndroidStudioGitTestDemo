package com.yliec.breeze;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Lecion
 * @Date 6/16/15
 * @Email lecion@icloud.com
 * 请求队列
 */
public class RequestQueue {
    private BlockingQueue<Request<?>> requestQueue = new PriorityBlockingQueue<Request<?>>();

    private AtomicInteger serialNumGenetrator = new AtomicInteger(0);

    public static int DEFAULT_CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;

    private int dispatcherNum = DEFAULT_CORE_NUM;

    private NetworkExecutor[] dispatchers = null;

    private HttpStack httpStack;

    public RequestQueue(HttpStack httpStack, int dispatcherNum) {
        this.httpStack = httpStack != null ? httpStack : HttpStackFactory.createHttpStack();
        this.dispatcherNum = dispatcherNum;
    }

    /**
     * 启动所有的NetworkExecutor
     */
    private void startNetworkExecutors() {
        dispatchers = new NetworkExecutor[dispatcherNum];
        for (int i = 0; i < dispatcherNum; i++) {
            dispatchers[i] = new NetworkExecutor(requestQueue, httpStack);
            dispatchers[i].start();
        }
    }

    public void start() {
        stop();
        startNetworkExecutors();
    }

    private void stop() {
        if (dispatchers != null && dispatchers.length > 0) {
            for (int i = 0; i < dispatchers.length; i++) {
                dispatchers[i].quit();
            }
        }
    }

    /**
     * 添加一个请求，重复请求不能添加
     * @param request
     */
    public void add(Request<?> request) {
        if (!requestQueue.contains(request)) {
            request.setSerialNum(generateSerialNum());
            requestQueue.add(request);
        } else {
            throw new RuntimeException("The request has been added in to the queue");
        }
    }

    public void clear() {
        if (requestQueue != null && requestQueue.size() > 0) {
            requestQueue.clear();
        }
    }

    public BlockingQueue<Request<?>> getRequests() {
        return requestQueue;
    }

    public int generateSerialNum() {
        return serialNumGenetrator.incrementAndGet();
    }
}
