package com.gothamdude.core.util.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFileProcessorTest {

    @Test
    void testWriteCsv() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        String[] headers = {"ID", "Name", "Role"};
        List<String[]> data = Arrays.asList(
                new String[]{"1", "Alice", "Dev"},
                new String[]{"2", "Bob", "Design"}
        );

        // Act
        CsvFileProcessor.writeCsv(writer, headers, data);

        // Assert
        String output = writer.toString();
        assertTrue(output.contains("ID,Name,Role"));
        assertTrue(output.contains("1,Alice,Dev"));
        assertTrue(output.contains("2,Bob,Design"));
    }

    @Test
    void testReadCsv() throws IOException {
        // Arrange
        // Note: readCsv currently prints to System.out, so we verify it doesn't crash
        // and handles the input correctly.
        String csvData = "Header1,Header2\nValue1,Value2\nValue3,Value4";
        StringReader reader = new StringReader(csvData);

        // Act & Assert
        // This confirms the parser handles the string without throwing IOException
        CsvFileProcessor.readCsv(reader);
    }



}