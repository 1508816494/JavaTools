package com.xf.tpm.test;

import com.xf.tpm.core.ThreadPoolManager;
import com.xf.tpm.core.ThreadPoolManagerImpl;
import com.xf.tpm.core.info.ThreadPoolMonitorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

/**
 * @author xufeng on 2017/11/2
 */
public class ThreadPoolExample {
    private static Logger logger = LoggerFactory.getLogger(ThreadPoolExample.class);
    public static void main(String[] args) {
        ThreadPoolManager tpm = ThreadPoolManagerImpl.getInstance();
        tpm.init();
        Future<ThreadPoolMonitorInfo> f = tpm.getThreadPool().submit(() ->{

            return new ThreadPoolMonitorInfo();

        });

        tpm.getThreadPool("other").submit(()-> {
            while (true){
                try {
                    System.out.println("other work");
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        tpm.getThreadPool("other").submit(()-> {
            try {
                System.out.println(Thread.currentThread().getName() + "other work1");
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        tpm.getThreadPool("other").submit(()-> {
            try {
                System.out.println(Thread.currentThread().getName() + "other work2");
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(tpm.getThreadPoolMonitorInfo("other"));

    }
}
