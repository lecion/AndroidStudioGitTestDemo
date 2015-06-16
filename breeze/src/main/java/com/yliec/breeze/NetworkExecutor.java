package com.yliec.breeze;

import java.util.concurrent.BlockingQueue;

/**
 * @Author Lecion
 * @Date 6/16/15
 * @Email lecion@icloud.com
 * 网络请求执行者
 */
public class NetworkExecutor extends Thread{
    private boolean isAlive = true;

    public NetworkExecutor(BlockingQueue<Request<?>> requestQueue, HttpStack httpStack) {

    }

    public void quit() {
        if (isAlive) {
            isAlive = false;
        }
        interrupt();
    }
}
