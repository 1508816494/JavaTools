package com.xf.tpm.core;

import com.xf.tpm.core.info.ThreadPoolInfo;
import com.xf.tpm.core.job.ThreadPoolStateJob;
import com.xf.tpm.core.job.ThreadStackJob;
import com.xf.tpm.core.job.ThreadStateJob;
import com.xf.tpm.core.lifecycle.ILifeCycle;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * 多线程池。
 * 
 * @author xufeng
 */
public class ThreadPoolImpl implements ILifeCycle, ThreadPool {

    /**
     *
     *  默认的线程池名称
     *
     * */
    private static final String DEFAULT_THREAD_POOL = "default";

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolImpl.class);
    
    private ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();

    private int status = ThreadPoolStatus.UNINITIALIZED;
    
    private Map<String, ExecutorService> multiThreadPool = new HashMap<>();

    private ThreadPoolStateJob threadPoolStateJob;

    private ThreadStateJob threadStateJob;

    private ThreadStackJob threadStackJob;
    
    public ThreadPoolImpl() {
        // nothing
    }
    
    @Override
    public void init() {
        if (ThreadPoolStatus.UNINITIALIZED != status) {
            logger.warn("initialization thread pool failed, because the status was wrong, current status was {} " +
                    "(0:UNINITIALIZED, 1:INITIALITION_SUCCESSFUL, 2:INITIALITION_FAILED, 3:DESTROYED)", status);
            return;
        }
        
        try {
            initThreadPool();
            startThreadPoolStateJob();
            startThreadStateJob();
            startThreadStackJob();
            status = ThreadPoolStatus.INITIALITION_SUCCESSFUL;
        } catch (RuntimeException e) {
            status = ThreadPoolStatus.INITIALITION_FAILED;
            throw e;
        }
    }
    
    /**
     * 初始化所有线程池。
     */
    private void initThreadPool() {
        threadPoolConfig.init();
        if (! threadPoolConfig.containsPool(DEFAULT_THREAD_POOL)) {
            throw new IllegalStateException( String.format(
                    "the default thread pool not exists, please check the config file '%s'",
                    threadPoolConfig.getConfigFile()) );
        }

        Collection<ThreadPoolInfo> threadPoolInfoList = threadPoolConfig.getThreadPoolConfig();

        for (ThreadPoolInfo threadPoolInfo : threadPoolInfoList) {

            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(threadPoolInfo.getQueueSize());

            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getMaxSize(), 
                    threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, workQueue, 
                    new DefaultThreadFactory(threadPoolInfo.getName()));
            multiThreadPool.put(threadPoolInfo.getName(), threadPool);
            logger.info("initialization thread pool '{}' success", threadPoolInfo.getName());
        }
    }
    
    /**
     * 初始化并启动线程池状态统计Job。
     */
    private void startThreadPoolStateJob() {
        if (! threadPoolConfig.getThreadPoolStateSwitch()) {
            return;
        }
        
        threadPoolStateJob = new ThreadPoolStateJob(
                multiThreadPool,
                threadPoolConfig.getThreadPoolStateInterval() );
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
        if (! threadPoolConfig.getThreadStateSwitch()) {
            return;
        }
        
        threadStateJob = new ThreadStateJob(threadPoolConfig.getThreadStateInterval());
        threadStateJob.init();
        Thread jobThread = new Thread(threadStateJob);
        jobThread.setName("threadpool4j-threadstate");
        jobThread.start();
        
        logger.info("start job 'threadpool4j-threadstate' success");
    }
    
    private void startThreadStackJob() {
        if (! threadPoolConfig.getThreadStackSwitch()) {
            return;
        }
        
        threadStackJob = new ThreadStackJob(threadPoolConfig.getThreadStackInterval());
        threadStackJob.init();
        Thread jobThread = new Thread(threadStackJob);
        jobThread.setName("threadpool4j-threadstack");
        jobThread.start();
        
        logger.info("start job 'threadpool4j-threadstack' success");
    }

    @Override
    public Future<?> submit(Runnable task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }

    @Override
    public Future<?> submit(Runnable task, String threadPoolName) {
        if (null == task) {
            throw new IllegalArgumentException("task is null");
        }
        
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        logger.debug("submit a task to thread pool {}", threadPoolName);
        
        return threadPool.submit(task);
    }
    
    @Override
    public Future<?> submit(Runnable task, String threadPoolName,
            FailHandler<Runnable> failHandler) {
        try {
            return submit(task, threadPoolName);
        } catch (RejectedExecutionException e) {
            if (null != failHandler) {
                failHandler.execute(task);
            }
        }
        
        return null;
    }
    
    private ExecutorService getThreadPool(String threadPoolName) {
        if (StringUtils.isBlank(threadPoolName)) {
            throw new IllegalArgumentException("thread pool name is empty");
        }
        
        return multiThreadPool.get(threadPoolName);

    }
    
    private ExecutorService getExistsThreadPool(String threadPoolName) {
        ExecutorService threadPool = getThreadPool(threadPoolName);
        if (null == threadPool) {
            throw new IllegalArgumentException( String.format("thread pool %s not exists", threadPoolName) );
        }
        
        return threadPool;
    }
    
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }
    
    @Override
    public <T> Future<T> submit(Callable<T> task, String threadPoolName) {
        if (null == task) {
            throw new IllegalArgumentException("task is null");
        }
        
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        logger.debug("submit a task to thread pool {}", threadPoolName);
        
        return threadPool.submit(task);
    }
    
    @Override
    public <T> Future<T> submit(Callable<T> task, String threadPoolName,
            FailHandler<Callable<T>> failHandler) {
        try {
            return submit(task, threadPoolName);
        } catch (RejectedExecutionException e) {
            if (null != failHandler) {
                failHandler.execute(task);
            }
        }
        
        return null;
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, 
            long timeout, TimeUnit timeoutUnit) {
        return invokeAll(tasks, timeout, timeoutUnit, DEFAULT_THREAD_POOL);
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,
            long timeout, TimeUnit timeoutUnit, String threadPoolName) {
        if (null == tasks || tasks.isEmpty()) {
            throw new IllegalArgumentException("task list is null or empty");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout less than or equals zero");
        }
        
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        logger.debug("invoke task list in thread pool {}", threadPoolName);
        
        try {
            return threadPool.invokeAll(tasks, timeout, timeoutUnit);
        } catch (InterruptedException e) {
            logger.error("invoke task list occurs error", e);
        }
        
        return null;
    }
    
    @Override
    public boolean isExists(String threadPoolName) {
        ExecutorService threadPool = getThreadPool(threadPoolName);
        
        return null != threadPool;
    }
    
    @Override
    public ThreadPoolInfo getThreadPoolInfo(String threadPoolName) {
        ThreadPoolInfo info = threadPoolConfig.getThreadPoolConfig(threadPoolName);
        
        return info.clone();
    }
    
    @Override
    public void destroy() {
        if (ThreadPoolStatus.DESTROYED == status) {
            return;
        }
        
        for (Entry<String, ExecutorService> entry : multiThreadPool.entrySet()) {
            logger.info("shutdown the thread pool '{}'", entry.getKey());
            entry.getValue().shutdown();
        }
        
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
        
        threadPoolConfig.destroy();
        status = ThreadPoolStatus.DESTROYED;
    }



}
