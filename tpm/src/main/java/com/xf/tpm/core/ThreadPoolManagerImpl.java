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

/**
 * @author xufeng on 2017/11/2
 */
public class ThreadPoolManagerImpl implements ThreadPoolManager {

    private Logger logger = LoggerFactory.getLogger(ThreadPoolManagerImpl.class);
    /**
     *
     *  默认的线程池名称
     *
     * */
    private static final String DEFAULT_THREAD_POOL = "default";

    private ThreadPoolConfigCenter threadPoolConfigCenter = new ThreadPoolConfigCenter();

    private static final ThreadPoolManager instance = new ThreadPoolManagerImpl();

    private Map<String,ThreadPool> multiThreadPool = new HashMap<>(10);

    private ThreadPoolStateJob threadPoolStateJob;

    private ThreadStateJob threadStateJob;

    private ThreadStackJob threadStackJob;
    private ThreadPoolManagerImpl() {
    }

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    @Override
    public void init() {
        initThreadPool();
        startThreadPoolStateJob();
        startThreadStateJob();
        startThreadStackJob();
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
            logger.info("initialization thread pool '{}' success", threadPoolInfo.getName());
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
        jobThread.setName("threadpool4j-threadpoolstate");
        jobThread.start();

        logger.info("start job 'threadpool4j-threadpoolstate' success");
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
        jobThread.setName("threadpool4j-threadstate");
        jobThread.start();

        logger.info("start job 'threadpool4j-threadstate' success");
    }

    private void startThreadStackJob() {
        if (! threadPoolConfigCenter.getThreadStackSwitch()) {
            return;
        }

        threadStackJob = new ThreadStackJob(threadPoolConfigCenter.getThreadStackInterval());
        threadStackJob.init();
        Thread jobThread = new Thread(threadStackJob);
        jobThread.setName("threadpool4j-threadstack");
        jobThread.start();

        logger.info("start job 'threadpool4j-threadstack' success");
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
        if (null != threadPoolStateJob) {
            threadPoolStateJob.destroy();
            logger.info("stop job 'threadpool4j-threadpoolstate' success");
            threadPoolStateJob = null;
        }

        if (null != threadStateJob) {
            threadStateJob.destroy();
            logger.info("stop job 'threadpool4j-threadstate' success");
            threadStateJob = null;
        }

        if (null != threadStackJob) {
            threadStackJob.destroy();
            logger.info("stop job 'threadpool4j-threadstack' success");
            threadStackJob = null;
        }
    }
}
