package com.yliec.breeze;

import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * @Author Lecion
 * @Date 6/16/15
 * @Email lecion@icloud.com
 * 网络请求执行者
 */
public class NetworkExecutor extends Thread{
    private final BlockingQueue<Request<?>> requestQueue;
    private final HttpStack httpStack;
    private boolean isAlive = true;

    public NetworkExecutor(BlockingQueue<Request<?>> requestQueue, HttpStack httpStack) {
        this.requestQueue = requestQueue;
        this.httpStack = httpStack;
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                Request<?> request = requestQueue.take();
                if (request.isCancel()) {
                    Log.d("DEBUG", "取消执行了");
                    continue;
                }
                Response response = null;
                //TODO 增加缓存
                response = httpStack.performRequest(request);
                if (response != null && response.getStatusCode() == 200) {
                    if (request.isShouldCache()) {
                        //TODO加入缓存
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void quit() {
        if (isAlive) {
            isAlive = false;
        }
        interrupt();
    }
}
