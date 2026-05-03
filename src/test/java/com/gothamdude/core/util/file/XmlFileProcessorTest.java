package com.gothamdude.core.util.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlFileProcessorTest {

    @Test
    void testConvertToXml() throws Exception {
        Company company = new Company("TechCorp", List.of("Alice", "Bob"));
        String xml = XmlFileProcessor.toXml(company);
        Assertions.assertNotNull(xml);
    }

    @Test
    void testConvertFromObject() throws Exception {
        String companyEmployees = "<company name=\"IBM\"><employees><employee>John</employee><employee>Anne</employee></employees></company>";
        Company company = XmlFileProcessor.fromXml(companyEmployees, Company.class);
        Assertions.assertNotNull(company);
        Assertions.assertEquals("IBM", company.getName());

    }

}