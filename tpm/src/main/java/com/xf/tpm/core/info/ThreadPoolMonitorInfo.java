package com.xf.tpm.core.info;

/**
 * 线程池基本状态
 * @author xufeng on 2017/11/3
 */
public class ThreadPoolMonitorInfo {

    private String poolName;
    private long createTime;  //线程池启动时间戳
    private long activeCount;
    private long totalTask;
    private long completedTaskCount;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

    public long getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(long totalTask) {
        this.totalTask = totalTask;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ThreadPoolMonitorInfo{" +
                "poolName='" + poolName + '\'' +
                ", createTime=" + createTime +
                ", activeCount=" + activeCount +
                ", totalTask=" + totalTask +
                ", completedTaskCount=" + completedTaskCount +
                '}';
    }
}
