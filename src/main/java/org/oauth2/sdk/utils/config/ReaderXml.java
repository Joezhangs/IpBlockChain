package org.oauth2.sdk.utils.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.oauth2.sdk.utils.StrUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 解析XML配置
 */
public class ReaderXml {

	public static List<Xml> xmls = null;
	static{
		xmls = new ArrayList<Xml>();
		Document document = getDocument("oauth.xml");
		NodeList booklist = document.getElementsByTagName("oauth");
		for (int j = 0; j < booklist.getLength(); j++) {
			Node book = booklist.item(j);
			NodeList childlist = book.getChildNodes();
			Xml module = new Xml();
			module.setName(getText(childlist, "name"));
			module.setClientId(getText(childlist, "clientId"));
			module.setClientSecret(getText(childlist, "clientSecret"));
			module.setRedirectUri(getText(childlist, "redirectUri"));
			module.setIntro(getText(childlist, "intro"));
			xmls.add(module);
		}
	}

	/**
	 * 查询配置
	 */
	public static Xml getXml(String key) {
		if (StrUtil.isBlank(key)) {
			return null;
		}
		List<Xml> lists = getXmls();
		for (Xml xml : lists) {
			String name = xml.getName();
			if (name.equals(key)) {
				return xml;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	public static String getText(NodeList childlist, String name) {
		String val = "";
		for (int t = 0; t < childlist.getLength(); t++) {
			// 区分出text类型的node以及element类型的node
			if (childlist.item(t).getNodeType() == Node.ELEMENT_NODE) {
				Node node = childlist.item(t);
				String nodeName = node.getNodeName();
				if (nodeName.equals(name)) {
					val = node.getTextContent();
					if (StrUtil.isBlank(val)) {
						val = "";
					} else {
						val = val.trim();
					}
					return val;
				}
			}
		}
		return val;
	}

	/**
	 * 获取Document
	 */
	public static Document getDocument(String name) {
		Document document = null;
		try {
			InputStream xml = ReaderXml.class.getClassLoader().getResourceAsStream(name);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder db = factory.newDocumentBuilder();
			//忽略dtd校验
			db.setEntityResolver((publicId, systemId) -> {
				if (systemId.contains("properties.dtd")) {
					InputSource is = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
					return is;
				} else
					return null;
			});
			document = (Document) db.parse(xml);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析XML文件失败！", e);
		}
		return document;
	}

	/**
	 * 解析xml文件,获取所有配置
	 */
	public static List<Xml> getXmls() {
		/*if (xmls == null) {
			xmls = new ArrayList<Xml>();
			Document document = getDocument("oauth.xml");
			NodeList booklist = document.getElementsByTagName("oauth");
			for (int j = 0; j < booklist.getLength(); j++) {
				Node book = booklist.item(j);
				NodeList childlist = book.getChildNodes();
				Xml module = new Xml();
				module.setName(getText(childlist, "name"));
				module.setClientId(getText(childlist, "clientId"));
				module.setClientSecret(getText(childlist, "clientSecret"));
				module.setRedirectUri(getText(childlist, "redirectUri"));
				module.setIntro(getText(childlist, "intro"));
				xmls.add(module);
			}
		}*/
		return xmls;
	}

	/**
	 * 获取Appkey
	 */
	public static String getClientId(String name) {
		return getXml(name).getClientId();
	}

	/**
	 * 获取Secret
	 */
	public static String getClientSecret(String name) {
		return getXml(name).getClientSecret();
	}
	
	/**
	 * 获取getRedirectUri
	 */
	public static String getRedirectUri(String name) {
		return getXml(name).getRedirectUri();
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println(getXmls());
		System.out.println(getClientId("wechat"));
	}
}