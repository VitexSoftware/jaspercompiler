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
