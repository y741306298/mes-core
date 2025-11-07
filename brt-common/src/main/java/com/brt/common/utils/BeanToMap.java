package com.brt.common.utils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @ClassName BeanToMap
 * @Description TODO
 * @Author FGN
 * @Date 2022/11/25 16:03
 * @Version 1.0
 **/
public class BeanToMap {
    /**
     * 将map转化为xml类型的字符串
     * @param map
     * @return
     */
    public static String callMapToXML(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("");
        mapToXMLTest2(map, sb);
        sb.append("");
        try {
            return sb.toString();
        } catch (Exception e) {
        }
        return null;
    }

    private static void mapToXMLTest2(Map map, StringBuffer sb) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext();) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value)
                value = "";
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXMLTest2(hm, sb);
                }
                sb.append("" + key + ">");
            } else {
                if (value instanceof HashMap) {
                    sb.append("");
                    mapToXMLTest2((HashMap) value, sb);
                    sb.append("" + key + ">");
                } else {
                    sb.append("" + value + "" + key + ">");
                }
            }
        }
    }

    /**
     * 把xml类型的字符串转化为map
     * @return
     */
    public static Map getMapFromXML(String xmlStr) {
        SAXBuilder builder = new SAXBuilder();
        Map map = new HashMap();
        try {
            Reader in = new StringReader(xmlStr);
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List<Element> list = root.getChildren();
            for (Element e : list)
                map.put(e.getName(), e.getText());
            return map;
        } catch (JDOMException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (IOException e) {

        } catch (Exception e) {

        }
        return map;

    }
}
