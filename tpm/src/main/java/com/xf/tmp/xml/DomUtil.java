package com.xf.tmp.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author xufeng on 2017/11/2
 */
public class DomUtil {
    private static Logger logger = LoggerFactory.getLogger(DomUtil.class);

    public DomUtil() {
    }

    public static Document createDocument(String classPathXmlFile) {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        Document document = null;

        try {
            DocumentBuilder dom = domFactory.newDocumentBuilder();
            document = dom.parse(DomUtil.class.getResourceAsStream(classPathXmlFile));
        } catch (Exception var4) {
            logger.error(String.format("create Document of xml file[%s] occurs error", classPathXmlFile), var4);
        }

        return document;
    }
}
