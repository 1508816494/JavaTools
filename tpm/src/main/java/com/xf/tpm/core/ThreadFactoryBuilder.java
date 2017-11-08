package com.xf.tpm.core;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xufeng on 2017/11/8
 */
public class ThreadFactoryBuilder {

    private String nameFormat = null;
    private Boolean daemon = null;
    private Integer priority = null;

    public ThreadFactoryBuilder() {

    }

    public ThreadFactoryBuilder setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(Boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public ThreadFactory build() {
        return build(this);
    }

    public ThreadFactory build(ThreadFactoryBuilder builder) {
        final String nameFormat = builder.nameFormat;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final AtomicLong count = nameFormat != null ? new AtomicLong(0L) : null;

        return (runnable)->{
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            if (nameFormat != null) {
                thread.setName(ThreadFactoryBuilder.format(nameFormat, count.getAndIncrement()));
            }

            if (daemon != null) {
                thread.setDaemon(daemon.booleanValue());
            }

            if (priority != null) {
                thread.setPriority(priority.intValue());
            }

            return thread;
        };
    }

    private static String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }

}
