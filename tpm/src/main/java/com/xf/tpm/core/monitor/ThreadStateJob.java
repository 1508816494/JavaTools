package com.xf.tpm.core.monitor;

import com.xf.tpm.core.info.ThreadStateInfo;
import com.xf.tpm.core.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author xufeng
 */
public class ThreadStateJob extends AbstractJob {

    private static Logger logger = LoggerFactory.getLogger(ThreadStateJob.class);
    
    public ThreadStateJob(int interval) {
        super.interval = interval;
    }

    @Override
    protected void execute() {
        Map<String, ThreadStateInfo> statMap = ThreadUtil.statAllGroupThreadState();
        
        for (Entry<String, ThreadStateInfo> entry : statMap.entrySet()) {
            ThreadStateInfo stateInfo = entry.getValue();
            logger.info("ThreadGroup:{}, New:{},  Runnable:{}, Blocked:{}, Waiting:{}, TimedWaiting:{}, Terminated:{}",
                    entry.getKey(), stateInfo.getNewCount(), stateInfo.getRunnableCount(), stateInfo.getBlockedCount(),
                    stateInfo.getWaitingCount(), stateInfo.getTimedWaitingCount(), stateInfo.getTerminatedCount());
        }
        
        super.sleep();
    } // end of execute

}
