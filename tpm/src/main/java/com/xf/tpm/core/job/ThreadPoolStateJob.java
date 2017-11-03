package com.xf.tpm.core.job;

import com.xf.tpm.core.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 收集所有线程池的状态信息，统计并输出汇总信息。
 *
 * @author xufeng
 */
public class ThreadPoolStateJob extends AbstractJob {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolStateJob.class);
    
    private Map<String, ThreadPool> multiThreadPool;
    
    public ThreadPoolStateJob(Map<String, ThreadPool> multiThreadPool, int interval) {

        this.multiThreadPool = multiThreadPool;
        super.interval = interval;
    }
    
    @Override
    protected void execute() {
        Set<Entry<String, ThreadPool>> poolSet = multiThreadPool.entrySet();
        for (Entry<String, ThreadPool> entry : poolSet) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) entry.getValue().getExecutor();
            logger.info("ThreadPool:{}, ActiveThread:{}, TotalTask:{}, CompletedTask:{}, Queue:{}",
                    entry.getKey(), pool.getActiveCount(), pool.getTaskCount(), pool.getCompletedTaskCount(), pool.getQueue().size());
        }
        
        super.sleep();
    }

}
