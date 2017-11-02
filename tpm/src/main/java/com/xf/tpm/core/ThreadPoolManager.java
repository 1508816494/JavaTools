package com.xf.tpm.core;


import com.xf.tpm.core.lifecycle.ILifeCycle;

/**
 * @author xufeng on 2017/11/2
 */
public interface ThreadPoolManager extends ILifeCycle{

    /**
     * return the default thread pool
     * @return
     */
    ThreadPool getThreadPool();

    /**
     *
     * @return
     */
    ThreadPool getExistThreadPool();

    /**
     * 返回指定的threadPool
     * @param threadPoolName
     * @return
     */
    ThreadPool getThreadPool(String threadPoolName);

    /**
     * @param threadPoolName
     * @return
     */
    ThreadPool getExistThreadPool(String threadPoolName);

}
