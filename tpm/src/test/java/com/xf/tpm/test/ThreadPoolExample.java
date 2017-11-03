package com.xf.tpm.test;

import com.xf.tpm.core.ThreadPool;
import com.xf.tpm.core.ThreadPoolManager;
import com.xf.tpm.core.ThreadPoolManagerImpl;

/**
 * @author xufeng on 2017/11/2
 */
public class ThreadPoolExample {

    public static void main(String[] args) {
        ThreadPoolManager tpm = ThreadPoolManagerImpl.getInstance();
        tpm.init();
        ThreadPool threadPool = tpm.getThreadPool();
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        });
    }
}
