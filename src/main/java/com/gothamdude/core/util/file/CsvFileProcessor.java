package com.gothamdude.core.util.file;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.List;

public class CsvFileProcessor {

    public static void writeCsv(Writer writer, String[] headers, List<String[]> data) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader(headers).build())) {
            for (String[] record : data) {
                printer.printRecord((Object[]) record);
            }
        }
    }

    // Example: Reading CSV
    public static void readCsv(Reader reader) throws IOException {
        try (CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : parser) {
                System.out.println(record.get(0)); // Access by index or header name
            }
        }
    }
}
