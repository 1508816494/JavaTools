package com.xf.tpm.core;

/**
 * 线程池状态
 * @author xufeng on 2017/11/2
 *
 */
public enum  ThreadPoolStatus {

    /**
     * 未初始化
     */
    UNINITIALIZED,

    /**
     * 初始化成功
     */
    INITIALITION_SUCCESSFUL,

    /**
     * 初始化失败
     */
    INITIALITION_FAILED,

    /**
     * 已销毁
     */
    DESTROYED

}
