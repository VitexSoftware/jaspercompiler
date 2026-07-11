Commandline Jasper Compiler
===========================

![logo](jaspercompiler.svg?raw=true)

[![wakatime](https://wakatime.com/badge/user/5abba9ca-813e-43ac-9b5f-b1cfdf3dc1c7/project/dd9822ba-7b9b-4130-a157-ef66b3da1675.svg)](https://wakatime.com/badge/user/5abba9ca-813e-43ac-9b5f-b1cfdf3dc1c7/project/dd9822ba-7b9b-4130-a157-ef66b3da1675)

Simple tool able to compile [AbraFlexi custom reports](https://github.com/Vitexus/winstrom-reports) into given destination

**Features:**
- JasperReports 6.21.3 compilation engine
- Automatic AbraFlexi/FlexiBee library detection and loading
- Java 11 compatibility
- Cross-platform support (Linux/Windows)
- Dynamic classpath manipulation for AbraFlexi integration

jaspercompiler is used in [AbraFlexi-Report-Tools](https://github.com/VitexSoftware/AbraFlexi-Report-Tools) by its report uploader **upreport**

Usage
-----

call it like this:

```shell
    jaspercompiler /path/to/report.jrxml
```
to build **/path/to/report.jasper**

also you can specify another destination (directory or filepath) as second parameter.

## Requirements

- Java 11 or higher
- AbraFlexi/FlexiBee installation (for library dependencies)

## Pinned JasperReports version (security note)

`pom.xml` intentionally pins `net.sf.jasperreports:jasperreports` to **6.21.3**. This is a deliberate compatibility constraint, not an oversight or stale dependency.

Per the AbraFlexi/FlexiBee support documentation on [user reports](https://podpora.flexibee.eu/cs/articles/4553831-uzivatelske-reporty), custom user reports must be compiled against exactly version 6.21.3 — newer JasperReports releases are not guaranteed compatible with AbraFlexi's report templates and integration.

This version is affected by [GHSA-9wxq-mwqw-8hhg](https://github.com/advisories/GHSA-9wxq-mwqw-8hhg), a high-severity Java deserialization vulnerability leading to remote code execution, fixed upstream in 7.0.7. **Do not bump this dependency to silence the security alert** without first confirming AbraFlexi/FlexiBee has certified compatibility with a newer JasperReports release — doing so risks breaking report compilation for all users of this tool.

## Development

Build from source:

```shell
mvn clean package
```

Run locally:

```shell
mvn exec:java -Dexec.mainClass="com.vitexsoftware.jaspercompiler.Commandline" -Dexec.args="/path/to/report.jrxml"
```

Installation
------------

```shell
sudo apt install lsb-release wget
echo "deb http://repo.vitexsoftware.com $(lsb_release -sc) main" | sudo tee /etc/apt/sources.list.d/vitexsoftware.list
sudo wget -O /etc/apt/trusted.gpg.d/vitexsoftware.gpg http://repo.vitexsoftware.com/keyring.gpg
sudo apt update
sudo apt install jaspercompiler
```
