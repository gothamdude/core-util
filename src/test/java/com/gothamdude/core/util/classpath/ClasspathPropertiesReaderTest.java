package com.gothamdude.core.util.classpath;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;


class ClasspathPropertiesReaderTest {

    @Test
    void testClassPathReaderReturnsProperties() {
        Properties config = ClasspathPropertiesReader.loadProperties("config.properties");
        Assertions.assertNotNull(config);
        Assertions.assertEquals("jdbc:postgresql://localhost:5432/sample_db", config.getProperty("database.url"));
    }

}