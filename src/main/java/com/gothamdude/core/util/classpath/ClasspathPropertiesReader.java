package com.gothamdude.core.util.classpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClasspathPropertiesReader {
    /**
     * Loads a properties file from the classpath.
     * @param fileName The name or path of the file relative to the classpath root.
     * @return A Properties object containing the file data.
     */
    public static Properties loadProperties(String fileName) {
        Properties props = new Properties();

        // Use the current thread's context class loader for maximum compatibility
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Sorry, unable to find " + fileName + " on the classpath.");
            }
            // Load the properties from the stream
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return props;
    }

}
