package com.xf.tpm.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xufeng on 2017/11/2
 */
public class DefaultThreadFactory implements ThreadFactory {
    private AtomicLong count;
    private static final String DEFAULT_THREAD_NAME_PRIFIX = "JT-thread";
    private ThreadGroup _group;
    private String _threadNamePrefix;

    public DefaultThreadFactory() {
        this("JT-thread");
    }

    public DefaultThreadFactory(String threadNamePrefix) {
        this.count = new AtomicLong(1L);
        this._threadNamePrefix = "JT-thread";
        this._threadNamePrefix = threadNamePrefix;
        ThreadGroup root = this.getRootThreadGroup();
        this._group = new ThreadGroup(root, this._threadNamePrefix);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(this._group, r);
        thread.setName(this._threadNamePrefix + "-" + this.count.getAndIncrement());
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        if (5 != thread.getPriority()) {
            thread.setPriority(5);
        }

        return thread;
    }

    private ThreadGroup getRootThreadGroup() {
        ThreadGroup threadGroup;
        for(threadGroup = Thread.currentThread().getThreadGroup(); null != threadGroup.getParent(); threadGroup = threadGroup.getParent()) {
            ;
        }

        return threadGroup;
    }
}
