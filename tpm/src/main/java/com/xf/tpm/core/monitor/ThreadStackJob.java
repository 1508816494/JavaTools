package com.xf.tpm.core.monitor;

import com.xf.tpm.core.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 收集所有线程的堆栈信息并输出到文件。
 * 
 * @author xufeng
 */
public class ThreadStackJob extends AbstractJob {

    private static Logger logger = LoggerFactory.getLogger(ThreadStackJob.class);
    
    private String lineSeparator = SystemUtil.getEndLine();

    /**
     * 线程堆栈缓冲区初始大小
     * */
    private final static int BUFFER_SIZE = 4096;
    
    public ThreadStackJob(int interval) {
        super.interval = interval;
    }
    
    @Override
    protected void execute() {
        Map<Thread, StackTraceElement[]> stackMap = Thread.getAllStackTraces();
        for (Entry<Thread, StackTraceElement[]> entry : stackMap.entrySet()) {
            // 线程基本信息
            Thread thread = entry.getKey();
            StringBuilder buffer = new StringBuilder(BUFFER_SIZE)
                .append("name:").append(thread.getName())
                .append(", id:").append(thread.getId())
                .append(", status:").append(thread.getState().toString())
                .append(", priority:").append(thread.getPriority())
                .append(lineSeparator);
            
            // 线程堆栈
            StackTraceElement[] stackList = entry.getValue();
            for (StackTraceElement ste : stackList) {
                buffer.append(ste.toString())
                    .append(lineSeparator);
            }
            
            logger.info(buffer.toString());
        }
        
        super.sleep();
    }

}
