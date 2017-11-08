package com.xf.tpm.core.info;

import java.io.Serializable;

/**
 * 线程池信息。
 *
 * @author xufeng
 */
public class ThreadPoolInfo implements Serializable, Cloneable {

    private static final long serialVersionUID = 8994270363831737712L;

    /**
     * 线程池名称
     */
    private String name;

    /**
     * 核心线程数
     */
    private int coreSize = 5;

    /**
     * 最大线程数量
     */
    private int maxSize = 30;

    /**
     * 线程空闲的生存时间。单位：秒
     */
    private long threadKeepAliveTime = 5;

    /**
     * 线程池队列容量
     */
    private int queueSize = 10000;

    /**
     * 线程池启动时间
     */
    private long createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getThreadKeepAliveTime() {
        return threadKeepAliveTime;
    }

    public void setThreadKeepAliveTime(long threadKeepAliveTime) {
        this.threadKeepAliveTime = threadKeepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public ThreadPoolInfo clone() {
        ThreadPoolInfo obj = new ThreadPoolInfo();
        obj.name = this.name;
        obj.coreSize = this.coreSize;
        obj.maxSize = this.maxSize;
        obj.threadKeepAliveTime = this.threadKeepAliveTime;
        obj.queueSize = this.queueSize;
        obj.createTime = this.createTime;
        return obj;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadPoolInfo)) {
            return false;
        }
        ThreadPoolInfo other = (ThreadPoolInfo) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ThreadPoolInfo{" +
                "name='" + name + '\'' +
                ", coreSize=" + coreSize +
                ", maxSize=" + maxSize +
                ", threadKeepAliveTime=" + threadKeepAliveTime +
                ", queueSize=" + queueSize +
                ", createTime=" + createTime +
                '}';
    }
}
