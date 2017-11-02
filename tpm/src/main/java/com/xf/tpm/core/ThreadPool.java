package com.xf.tpm.core;

import com.xf.tpm.core.info.ThreadPoolInfo;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author xufeng
 */
public interface ThreadPool {

    Future<?> submit(Runnable task);

    Future<?> submit(Runnable task, String threadPoolName);

    Future<?> submit(Runnable task, String threadPoolName,
                     FailHandler<Runnable> failHandler);

    <T> Future<T> submit(Callable<T> task);

    <T> Future<T> submit(Callable<T> task, String threadPoolName);


    <T> Future<T> submit(Callable<T> task, String threadPoolName,
                         FailHandler<Callable<T>> failHandler);


    <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,
                                  long timeout, TimeUnit timeoutUnit);


    <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,
                                  long timeout, TimeUnit timeoutUnit, String threadPoolName);

    boolean isExists(String threadPoolName);

    ThreadPoolInfo getThreadPoolInfo(String threadPoolName);

}
