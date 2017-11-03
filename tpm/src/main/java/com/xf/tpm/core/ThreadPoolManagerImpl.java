package com.xf.tpm.core;

import com.xf.tpm.core.info.ThreadPoolInfo;
import com.xf.tpm.core.job.ThreadPoolStateJob;
import com.xf.tpm.core.job.ThreadStackJob;
import com.xf.tpm.core.job.ThreadStateJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xufeng on 2017/11/2
 */
public class ThreadPoolManagerImpl implements ThreadPoolManager {

    private Logger logger = LoggerFactory.getLogger(ThreadPoolManagerImpl.class);
    /**
     *  默认的线程池名称
     * */
    private static final String DEFAULT_THREAD_POOL = "default";

    private ReentrantLock reentrantLock = new ReentrantLock();

    private ThreadPoolConfigCenter threadPoolConfigCenter = new ThreadPoolConfigCenter();

    private static final ThreadPoolManager INSTANCE = new ThreadPoolManagerImpl();

    private Map<String,ThreadPool> multiThreadPool = new HashMap<>(10);

    private ThreadPoolStateJob threadPoolStateJob;

    private ThreadStateJob threadStateJob;

    private ThreadStackJob threadStackJob;

    private ThreadPoolManagerImpl() {
    }

    public static ThreadPoolManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void init() {
        this.reentrantLock.lock();
        try {
            initThreadPool();
            startThreadPoolStateJob();
            startThreadStateJob();
            startThreadStackJob();
        }finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 初始化线程池
     */
    private void initThreadPool() {
        //获取配置
        threadPoolConfigCenter.init();
        if (! threadPoolConfigCenter.containsPool(DEFAULT_THREAD_POOL)) {
            throw new IllegalStateException( String.format(
                    "the default thread pool not exists, please check the config file '%s'",
                    threadPoolConfigCenter.getConfigFile()) );
        }

        Collection<ThreadPoolInfo> threadPoolInfoList = threadPoolConfigCenter.getThreadPoolConfig();

        for (ThreadPoolInfo threadPoolInfo : threadPoolInfoList) {
            multiThreadPool.put(threadPoolInfo.getName(), new ThreadPoolImpl(threadPoolInfo));
        }
    }

    /**
     * 初始化并启动线程池状态统计Job。
     */
    private void startThreadPoolStateJob() {
        if (! threadPoolConfigCenter.getThreadPoolStateSwitch()) {
            return;
        }

        threadPoolStateJob = new ThreadPoolStateJob(
                multiThreadPool,
                threadPoolConfigCenter.getThreadPoolStateInterval() );
        threadPoolStateJob.init();
        Thread jobThread = new Thread(threadPoolStateJob);
        jobThread.setName("JT-threadpoolstate");
        jobThread.start();

        logger.info("start job 'JT-threadpoolstate' success");
    }

    /**
     * 初始化并启动线程状态统计Job。
     */
    private void startThreadStateJob() {
        if (! threadPoolConfigCenter.getThreadStateSwitch()) {
            return;
        }

        threadStateJob = new ThreadStateJob(threadPoolConfigCenter.getThreadStateInterval());
        threadStateJob.init();
        Thread jobThread = new Thread(threadStateJob);
        jobThread.setName("JT-threadstate");
        jobThread.start();

        logger.info("start job 'JT-threadstate' success");
    }

    private void startThreadStackJob() {
        if (! threadPoolConfigCenter.getThreadStackSwitch()) {
            return;
        }

        threadStackJob = new ThreadStackJob(threadPoolConfigCenter.getThreadStackInterval());
        threadStackJob.init();
        Thread jobThread = new Thread(threadStackJob);
        jobThread.setName("JT-threadstack");
        jobThread.start();

        logger.info("start job 'JT-threadstack' success");
    }

    @Override
    public ThreadPool getThreadPool() {
        return multiThreadPool.get(DEFAULT_THREAD_POOL);
    }

    @Override
    public ThreadPool getExistThreadPool() {
        ThreadPool threadPool = multiThreadPool.get(DEFAULT_THREAD_POOL);
        if (threadPool == null) {
            //todo throw can not find not exist thread pool
        }
        return threadPool;
    }

    @Override
    public ThreadPool getThreadPool(String threadPoolName) {
        return multiThreadPool.get(threadPoolName);
    }

    @Override
    public ThreadPool getExistThreadPool(String threadPoolName) {
        ThreadPool threadPool = multiThreadPool.get(threadPoolName);
        if (threadPool == null) {
            //todo throw
        }
        return threadPool;
    }

    @Override
    public void destroy() {
        this.reentrantLock.lock();
        try{
            //先停止收集工作线程
            if (null != threadPoolStateJob) {
                threadPoolStateJob.destroy();
                logger.info("stop job 'JT-threadpoolstate' success");
                threadPoolStateJob = null;
            }

            if (null != threadStateJob) {
                threadStateJob.destroy();
                logger.info("stop job 'JT-threadstate' success");
                threadStateJob = null;
            }

            if (null != threadStackJob) {
                threadStackJob.destroy();
                logger.info("stop job 'JT-threadstack' success");
                threadStackJob = null;
            }

            for (Map.Entry<String, ThreadPool> entry : multiThreadPool.entrySet()) {
                logger.info("shutdown the thread pool '{}'", entry.getKey());
                entry.getValue().destroy();
            }
            threadPoolConfigCenter.destroy();
        }finally {
            logger.info("all thread pool stop work finished");
            this.reentrantLock.unlock();
        }

    }
}
