package com.xf.tpm.core;

import com.xf.tpm.core.info.ThreadPoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程池。
 * 
 * @author xufeng
 */
public class ThreadPoolImpl implements ThreadPool {

    private final ThreadPoolInfo threadPoolInfo;

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolImpl.class);

    private ThreadPoolStatus status = ThreadPoolStatus.UNINITIALIZED;
    
    private ExecutorService executorService;

    public ThreadPoolImpl(ThreadPoolInfo threadPoolInfo) {
        this.threadPoolInfo = threadPoolInfo;
        init();
    }

    //todo 外部不应该可以获取 线程池的执行者
    @Override
    public ExecutorService getExecutor() {
        return executorService;

    }

    @Override
    public void init() {
        if (ThreadPoolStatus.UNINITIALIZED != status) {
            logger.warn("initialization thread pool failed, because the status was wrong, current status was {} " +
                    "(0:UNINITIALIZED, 1:INITIALITION_SUCCESSFUL, 2:INITIALITION_FAILED, 3:DESTROYED)", status);
            return;
        }
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(threadPoolInfo.getQueueSize());
        this.executorService = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getMaxSize(),
                threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, workQueue,
                new DefaultThreadFactory(threadPoolInfo.getName()));
        status = ThreadPoolStatus.INITIALITION_SUCCESSFUL;
        logger.info("initialization thread pool '{}' finished current state {}", threadPoolInfo.getName(),status);
    }

    @Override
    public Future<?> submit(Runnable task, FailHandler<Runnable> failHandler) {
        return executorService.submit(task,failHandler);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (null == task) {
            logger.warn("task is null");
            // todo throw exception
            return null;
        }
        logger.info("thread pool {} get a task",threadPoolInfo.getName());
        return executorService.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, FailHandler<Callable<T>> failHandler) {
        try {
            return submit(task);
        } catch (RejectedExecutionException e) {
            if (null == failHandler) {
                failHandler.execute(task);
            }
        }
        return null;
    }

    @Override
    public ThreadPoolInfo getThreadPoolInfo() {
        return threadPoolInfo;
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (null == task) {
            logger.warn("task is null");
            //todo throw exception
            return null;
        }
        return executorService.submit(task);
    }


    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, 
            long timeout, TimeUnit timeoutUnit) {
        if (null == tasks) {
            //todo throw exception
            return null;
        }
        if (timeout < 0) {
            //todo throw exception
            return null;
        }

        try {
            return executorService.invokeAll(tasks, timeout, timeoutUnit);
        } catch (InterruptedException e) {
            logger.info("invoke task list occurs error",e);
        }
        return null;
    }

    @Override
    public void destroy() {
        if (ThreadPoolStatus.DESTROYED == status) {
            return;
        }
        this.executorService.shutdown();
        status = ThreadPoolStatus.DESTROYED;
    }



}
