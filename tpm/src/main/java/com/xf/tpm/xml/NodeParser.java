package com.xf.tmp.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * @author xufeng on 2017/11/2
 */
public class NodeParser {
    private Node node;
    private Map<String, String> attrMap;
    private List<Node> childNodes;

    public NodeParser(Node node) {
        this.node = node;
    }

    public String getName() {
        return this.node.getNodeName();
    }

    public String getValue() {
        return this.node.getTextContent();
    }

    public String getAttributeValue(String attrName) {
        this.initAttrMap();
        return (String)this.attrMap.get(attrName);
    }

    public int getAttributeCount() {
        this.initAttrMap();
        return this.attrMap.size();
    }

    private void initAttrMap() {
        if (null == this.attrMap) {
            this.attrMap = new HashMap<>(10);
            NamedNodeMap nodeMap = this.node.getAttributes();
            if (null != nodeMap) {
                for(int i = 0; i < nodeMap.getLength(); ++i) {
                    Node attr = nodeMap.item(i);
                    this.attrMap.put(attr.getNodeName(), attr.getNodeValue());
                }

            }
        }
    }

    public List<Node> getChildNodes() {
        this.initChildNodeList();
        return this.childNodes;
    }

    public int getChildNodeCount() {
        this.initChildNodeList();
        return this.childNodes.size();
    }

    public Node getChildNode(String nodeName) {
        if (null == nodeName) {
            return null;
        } else {
            this.initChildNodeList();
            Iterator i$ = this.childNodes.iterator();

            Node node;
            do {
                if (!i$.hasNext()) {
                    return null;
                }

                node = (Node)i$.next();
            } while(!nodeName.equals(node.getNodeName()));

            return node;
        }
    }

    public String getChildNodeValue(String nodeName) {
        Node node = this.getChildNode(nodeName);
        return null == node ? null : node.getTextContent();
    }

    private void initChildNodeList() {
        if (null == this.childNodes) {
            this.childNodes = new ArrayList();
            NodeList nodeList = this.node.getChildNodes();

            for(int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                if (1 == node.getNodeType()) {
                    this.childNodes.add(node);
                }
            }

        }
    }
}
