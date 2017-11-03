package com.xf.tpm.core;

import com.xf.tpm.core.info.ThreadPoolInfo;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author xufeng
 */
public interface ThreadPool {

    ExecutorService getExecutor();

    Future<?> submit(Runnable task);


    Future<?> submit(Runnable task,
                     FailHandler<Runnable> failHandler);

    <T> Future<T> submit(Callable<T> task);


    <T> Future<T> submit(Callable<T> task, FailHandler<Callable<T>> failHandler);


    <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,
                                  long timeout, TimeUnit timeoutUnit);


    ThreadPoolInfo getThreadPoolInfo();

}
