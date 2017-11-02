package com.xf.tpm.core;

/**
 * 当队列满，异步任务无法提交给线程池执行的"失败处理器"。
 * 
 * @author xufeng
 */
public interface FailHandler<T> {

    /**
     * 处理无法提交线程池执行的异步任务。
     * 
     * @param task 无法提交线程池执行的异步任务
     */
    void execute(T task);

}
