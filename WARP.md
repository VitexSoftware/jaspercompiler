# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

Jaspercompiler is a command-line tool for compiling JasperReports XML templates (.jrxml) into binary Jasper reports (.jasper) with AbraFlexi/FlexiBee support. It's a Java-based Maven project that uses dynamic classloading to include AbraFlexi libraries at runtime.

## Architecture

### Core Components

- **Commandline.java**: Main entry point that handles argument parsing, library discovery, and compilation orchestration
- **Agent.java**: Java instrumentation agent for dynamic classloading of JAR files into the system classloader
- **Dynamic Library Loading**: Automatically detects and loads AbraFlexi/FlexiBee libraries from system paths:
  - Linux: `/usr/share/flexibee/lib/`
  - Windows: `C:\Program Files (x86)\WinStrom\lib`

### Key Dependencies

- JasperReports 6.21.3 (core compilation engine)
- Apache Commons IO 2.16.1 (file operations)
- SLF4J 2.0.16 with Simple implementation (logging)
- Java 11 (minimum required version)

## Development Commands

### Build
```bash
mvn clean package
```

### Run Locally (Development)
```bash
# Compile a single report
mvn exec:java -Dexec.mainClass="com.vitexsoftware.jaspercompiler.Commandline" -Dexec.args="/path/to/report.jrxml"

# With custom destination
mvn exec:java -Dexec.mainClass="com.vitexsoftware.jaspercompiler.Commandline" -Dexec.args="/path/to/report.jrxml /output/directory/"
```

### Package for Distribution
```bash
# Create JAR with dependencies
mvn clean package

# The executable JAR is created at: target/jaspercompiler-*-jar-with-dependencies.jar
```

### Debian Package Build
```bash
# Build Debian package (requires debhelper and maven-debian-helper)
dpkg-buildpackage -b -uc

# Or using debian/rules directly
debian/rules clean
debian/rules build
```

### Test the Built Package
```bash
# After building, test with a sample .jrxml file
java -jar target/jaspercompiler-*-jar-with-dependencies.jar sample.jrxml
```

## Project Structure

```
src/main/java/com/vitexsoftware/jaspercompiler/
├── Commandline.java     # Main application logic
└── Agent.java          # JVM instrumentation for classpath manipulation

src/main/resources/usr/bin/
└── jaspercompiler      # Shell script wrapper for system installation

debian/                 # Debian packaging configuration
├── rules              # Build rules
├── control            # Package metadata
├── install            # Installation mappings
└── changelog          # Version history
```

## Key Development Notes

### Library Discovery Logic
The application automatically searches for AbraFlexi libraries using a wildcard pattern matching files starting with "winstrom-" or "binding-" in the detected lib directory. This ensures compatibility with different AbraFlexi versions.

### Instrumentation Agent
The Agent class uses Java's instrumentation API to add JAR files to the system classloader at runtime. This is necessary because JasperReports may need to access AbraFlexi classes during report compilation.

### Error Handling
The application has built-in fallback logic for library path detection and provides clear error messages when AbraFlexi libraries cannot be found.

### Filename Validation
The application validates that the input JRXML filename matches the `name` attribute of the `jasperReport` XML element. If they differ:
- A visible warning is displayed
- The destination filename is automatically adjusted to use the correct report name from the XML
- This ensures generated .jasper files have consistent naming with their internal report definitions

## Usage Patterns

### Basic Compilation
```bash
jaspercompiler /path/to/report.jrxml
# Creates: /path/to/report.jasper
```

### Custom Output Location
```bash
jaspercompiler /path/to/report.jrxml /output/directory/
# Creates: /output/directory/report.jasper

jaspercompiler /path/to/report.jrxml /custom/path/custom-name.jasper
# Creates: /custom/path/custom-name.jasper
```

## Integration Context

This tool is primarily used by:
- [AbraFlexi-Report-Tools](https://github.com/VitexSoftware/AbraFlexi-Report-Tools) via the `upreport` utility
- Custom AbraFlexi report development workflows
- Automated report compilation in CI/CD pipelines

## Distribution

The project builds both:
1. Standalone JAR with all dependencies (`jar-with-dependencies`)
2. Debian package for easy system installation via apt

The Debian package installs:
- Executable script at `/usr/bin/jaspercompiler`
- JAR file at `/usr/share/java/`
- Proper symbolic links for system libraries