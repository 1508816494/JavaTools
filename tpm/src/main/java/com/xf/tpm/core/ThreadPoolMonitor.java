package com.xf.tpm.core;

import com.xf.tpm.core.info.ThreadPoolMonitorInfo;

/**
 * @author xufeng on 2017/11/6
 */
public interface ThreadPoolMonitor {

    /**
     * 获取监控线程池的基本信息
     * @param threadPoolName
     * @return
     */
    ThreadPoolMonitorInfo getThreadPoolMonitorInfo(String threadPoolName);
}
