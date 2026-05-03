# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Compile
mvn clean compile

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=DateUtilTest
mvn test -Dtest=TimeUtilTest

# Package JAR
mvn clean package
```

## Architecture

`core-util` is a Java 21 utility library (`com.gothamdude.core.util`) with static helper classes organized by domain:

- **`date/`** — `DateUtil`: LocalDate operations (format, parse, add days, convert to/from legacy `java.util.Date`). Uses `yyyy-MM-dd` as the standard format.
- **`time/`** — `TimeUtil`: EST/EDT ↔ UTC conversions using `ZonedDateTime`; handles DST automatically. Uses `yyyy-MM-dd HH:mm:ss` format.
- **`file/`** — `CsvFileProcessor`: process csv files 
- **`file/`** — `JsonFileProcessor`: process json files
- **`file/`** — `XmlFileProcessor`: process xml files
- **`classpath/`**, **`file/`**, **`string/`** — placeholder packages with no implementation yet.

All utility classes use only static methods (no instantiation). Logging via SLF4J + Logback.

## Testing Conventions

Tests live under `src/test/java/com/gothamdude/core/util/<domain>/` mirroring the main package structure. JUnit 5 with AssertJ and Mockito available. Test methods are organized by the method under test and cover: happy paths, edge cases (null/empty input, boundary dates), invalid formats (`assertThrows`), and round-trip conversions.
