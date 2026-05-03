package com.gothamdude.core.util.file;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlFileProcessor {

    private static final XmlMapper xmlMapper = new XmlMapper();

    public static String toXml(Object obj) throws Exception {
        return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static <T> T fromXml(String xml, Class<T> clazz) throws Exception {
        return xmlMapper.readValue(xml, clazz);
    }

}
