package com.xf.tpm.core.job;


import com.xf.tpm.core.lifecycle.ILifeCycle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xufeng
 *
 * job抽象类
 *
 */
public abstract class AbstractJob implements Runnable, ILifeCycle {

    protected String lineSeparator = System.getProperty("line.separator");
    
    /**
     * 运行状态：true表示正在运行；false表示已停止
     *
     * */
    protected volatile AtomicBoolean isRun = new AtomicBoolean(true);
    
    /**
     * 线程休眠时间（单位：秒）
     * */
    protected int interval = 60;

    @Override
    public void init() {
        isRun.set(true);
    }

    @Override
    public void run() {
        while (isRun.get()) {
            execute();
        }
    }

    /**
     * job执行方法
     */
    protected abstract void execute();
    
    /**
     * 休眠<code>interval</code>指定的时间。
     */
    protected void sleep() {
        try {
            Thread.sleep(interval * 1000);
        } catch (InterruptedException e) {
            // nothing
        }
    }
    
    /**
     * @return 返回"yyyy-MM-dd HH:mm:ss"格式的当前日期时间字符串
     */
    protected String currentTime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();

        return format.format(date);
    }


    @Override
    public void destroy() {
        isRun.set(false);
    }
}
