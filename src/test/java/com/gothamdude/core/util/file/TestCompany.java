package com.gothamdude.core.util.file;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "company")
class TestCompany {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlElementWrapper(localName = "employees")
    @JacksonXmlProperty(localName = "employee")
    private List<String> employeeList;

    // Getters, Setters, Constructors
    public TestCompany() {}
    public TestCompany(String name, List<String> employeeList) {
        this.name = name;
        this.employeeList = employeeList;
    }
    public String getName() { return name; }
    public List<String> getEmployeeList() { return employeeList; }
}