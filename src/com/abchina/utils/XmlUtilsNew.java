package com.abchina.utils;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class XmlUtilsNew {

	/**
	 * 使用dom4j把XML转JSON
	 * @param xml
	 * @return
	 */
	public static String xmlToJson(String xml) {
        Document doc;
        try {
            doc = DocumentHelper.parseText(xml);
            JSONObject json = new JSONObject();
            dom4j2Json(doc.getRootElement(), json);
//            System.out.println("xml2Json:" + json.toJSONString());
            return json.toJSONString();
        } catch (DocumentException e) {
        	System.out.println("数据解析失败");
        }
        return null;
 
    }
	
	/**
	 * XML转JSON
	 *
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
//	public static JSONObject xml2Json(String xmlStr) throws Exception {
//		Document doc = DocumentHelper.parseText(xmlStr);
//		JSONObject json = new JSONObject();
//		//
//		// Element root = doc.getRootElement();
//		// Element e = getUserfulRoot(root);
//		
//		dom4j2Json(doc.getRootElement(), json);
//		return json;
//	}

//	@SuppressWarnings("rawtypes")
//	private static Element getUserfulRoot(Element root) {
//		List childrenList1 = root.getChildren();
//		Element element1 = (Element) childrenList1.get(0);
//		List childrenList2 = element1.getChildren();
//		Element element2 = (Element) childrenList2.get(0);
//		List childrenList3 = element2.getChildren();
//		Element element3 = (Element) childrenList3.get(0);
//		List childrenList4 = element3.getChildren();
//		return (Element) childrenList4.get(0);
//	}
	
	/**
	 * xml转json
	 *
	 * @param element
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	public static void dom4j2Json(Element element, JSONObject json) {

		// 如果是属性
//		for (Object o : element.attributes()) {
//			Attribute attr = (Attribute) o;
//			if (!isEmpty(attr.getValue())) {
//				json.put("@" + attr.getName(), attr.getValue());
//			}
//		}
		List<Element> chdEl = element.elements();
		if (chdEl.isEmpty() && !isEmpty(element.getText())) {// 如果没有子元素,只有一个值
			json.put(element.getName(), element.getText());
		}

		for (Element e : chdEl) {// 有子元素
			if (!e.elements().isEmpty()) {// 子元素也有子元素
				JSONObject chdjson = new JSONObject();
				dom4j2Json(e, chdjson);
				Object obj = json.get(e.getName());
				if (obj != null) {
					JSONArray jsonArray = null;
					if (obj instanceof JSONObject) {// 如果此元素已存在,则转为jsonArray
						JSONObject jsonObj = (JSONObject) obj;
						json.remove(e.getName());
						jsonArray = new JSONArray();
						jsonArray.add(jsonObj);
						jsonArray.add(chdjson);
					}
					if (obj instanceof JSONArray) {
						jsonArray = (JSONArray) obj;
						jsonArray.add(chdjson);
					}
					json.put(e.getName(), jsonArray);
				} else {
					if (!chdjson.isEmpty()) {
						json.put(e.getName(), chdjson);
					}
				}

			} else {// 子元素没有子元素
//				for (Object o : element.attributes()) {
//					Attribute attr = (Attribute) o;
//					if (!isEmpty(attr.getValue())) {
//						json.put("@" + attr.getName(), attr.getValue());
//					}
//				}
				if (!e.getText().isEmpty()) {
					json.put(e.getName(), e.getText());
				}
			}
		}
	}

	public static boolean isEmpty(String str) {

		if (str == null || str.trim().isEmpty() || "null".equals(str)) {
			return true;
		}
		return false;
	}
}
