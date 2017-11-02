package com.xf.tpm.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 收集所有线程池的状态信息，统计并输出汇总信息。
 *
 * @author xufeng
 */
public class ThreadPoolStateJob extends AbstractJob {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolStateJob.class);
    
    private Map<String, ExecutorService> multiThreadPool;
    
    public ThreadPoolStateJob(Map<String, ExecutorService> multiThreadPool, int interval) {

        this.multiThreadPool = multiThreadPool;
        super.interval = interval;
    }
    
    @Override
    protected void execute() {
        Set<Entry<String, ExecutorService>> poolSet = multiThreadPool.entrySet();
        for (Entry<String, ExecutorService> entry : poolSet) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) entry.getValue();
            logger.info("ThreadPool:{}, ActiveThread:{}, TotalTask:{}, CompletedTask:{}, Queue:{}",
                    entry.getKey(), pool.getActiveCount(), pool.getTaskCount(), pool.getCompletedTaskCount(), pool.getQueue().size());
        }
        
        super.sleep();
    }

}
