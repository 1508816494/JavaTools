package com.xf.tpm.core;

import com.xf.tmp.xml.DomUtil;
import com.xf.tmp.xml.NodeParser;
import com.xf.tpm.core.info.ThreadPoolInfo;
import com.xf.tpm.core.lifecycle.ILifeCycle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有线程池的配置中心
 * 
 * @author xufneg
 */
public class ThreadPoolConfigCenter implements ILifeCycle {

    public final static String DEFAULT_CONFIG_FILE = "/threadpool4j.xml";

    private String configFile = DEFAULT_CONFIG_FILE;
    
    /**
     * key为线程池名称，value为{@link ThreadPoolInfo}实例。
     */
    protected Map<String, ThreadPoolInfo> multiThreadPoolInfo = new HashMap<>();
    
    /** 线程池状态收集开关 */
    private boolean threadPoolStateSwitch = false;

    private int threadPoolStateInterval = 60;   // 单位：秒
    
    /**
     * 线程状态收集开关
     * */
    private boolean threadStateSwitch = false;

    private int threadStateInterval = 60;   // 单位：秒
    
    /** 线程堆栈收集开关 */
    private boolean threadStackSwitch = false;

    private int threadStackInterval = 60;   // 单位：秒
    
    @Override
    public void init() {
        initConfig();
    }
    
    private void initConfig() {
        Document document = DomUtil.createDocument(configFile);
        
        Element root = document.getDocumentElement();
        NodeParser rootParser = new NodeParser(root);
        List<Node> nodeList = rootParser.getChildNodes();
        for (Node node : nodeList) {
            NodeParser nodeParser = new NodeParser(node);
            if ( "pool".equals(node.getNodeName()) ) {
                ThreadPoolInfo info = new ThreadPoolInfo();
                info.setName(nodeParser.getAttributeValue("name"));
                info.setCoreSize(Integer.parseInt(nodeParser.getChildNodeValue("corePoolSize")));
                info.setMaxSize(Integer.parseInt(nodeParser.getChildNodeValue("maxPoolSize")));
                info.setThreadKeepAliveTime(Long.parseLong(nodeParser.getChildNodeValue("keepAliveTime")));
                info.setQueueSize(Integer.parseInt(nodeParser.getChildNodeValue("workQueueSize")));
                
                multiThreadPoolInfo.put(info.getName(), info);
            } else if ( "threadpoolstate".equals(node.getNodeName()) ) {
                threadPoolStateSwitch = computeSwitchValue(nodeParser);
                threadPoolStateInterval = computeIntervalValue(nodeParser);
            } else if ( "threadstate".equals(node.getNodeName()) ) {
                threadStateSwitch = computeSwitchValue(nodeParser);
                threadStateInterval = computeIntervalValue(nodeParser);
            } else if ( "threadstack".equals(node.getNodeName()) ) {
                threadStackSwitch = computeSwitchValue(nodeParser);
                threadStackInterval = computeIntervalValue(nodeParser);
            }
        } // end of for
    }
    
    private boolean computeSwitchValue(NodeParser nodeParser) {
        return "on".equalsIgnoreCase(
                nodeParser.getAttributeValue("switch"));
    }
    
    private int computeIntervalValue(NodeParser nodeParser) {
        return Integer.parseInt(nodeParser.getAttributeValue("interval"));
    }
    
    /**
     * 指定名称的线程池的配置是否存在。
     * 
     * @return 如果指定名称的线程池的配置存在返回true，如果不存在返回false；如果传入的线程池名称为null也返回false。
     */
    public boolean containsPool(String poolName) {
        if (null == poolName || null == multiThreadPoolInfo || multiThreadPoolInfo.isEmpty()) {
            return false;
        }
        
        return multiThreadPoolInfo.containsKey(poolName);
    }
    
    /**
     * 获取指定线程池的配置信息。
     * 
     * @param threadpoolName 线程池名称
     * @return 线程池配置信息（{@link ThreadPoolInfo}）
     */
    public ThreadPoolInfo getThreadPoolConfig(String threadpoolName) {
        return multiThreadPoolInfo.get(threadpoolName);
    }
    
    /**
     * 获取所有线程池的配置信息。
     * 
     * @return 线程池配置信息（{@link ThreadPoolInfo}）集合
     */
    public Collection<ThreadPoolInfo> getThreadPoolConfig() {
        return multiThreadPoolInfo.values();
    }
    
    /**
     * @return 输出各个线程池状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadPoolStateSwitch() {
        return threadPoolStateSwitch;
    }
    
    /**
     * @return 线程池状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadPoolStateInterval() {
        return threadPoolStateInterval;
    }
    
    /**
     * @return 输出各个线程组中线程状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadStateSwitch() {
        return threadStateSwitch;
    }
    
    /**
     * @return 线程状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadStateInterval() {
        return threadStateInterval;
    }
    
    /**
     * @return 输出所有线程堆栈的开关，true表示开，false表示关
     */
    public boolean getThreadStackSwitch() {
        return threadStackSwitch;
    }
    
    /**
     * @return 线程堆栈信息输出的间隔时间（单位：秒）
     */
    public int getThreadStackInterval() {
        return threadStackInterval;
    }

    public String getConfigFile() {
        return configFile;
    }

    @Override
    public void destroy() {
        threadPoolStateSwitch = false;
        threadStateSwitch = false;
        multiThreadPoolInfo.clear();
    }

}
