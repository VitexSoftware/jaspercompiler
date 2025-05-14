Commandline Jasper Compiler
===========================

![logo](jaspercompiler.svg?raw=true)

[![wakatime](https://wakatime.com/badge/user/5abba9ca-813e-43ac-9b5f-b1cfdf3dc1c7/project/dd9822ba-7b9b-4130-a157-ef66b3da1675.svg)](https://wakatime.com/badge/user/5abba9ca-813e-43ac-9b5f-b1cfdf3dc1c7/project/dd9822ba-7b9b-4130-a157-ef66b3da1675)

Simple tool able to comple [AbraFlexi customs reports](https://github.com/Vitexus/winstrom-reports) into given destination

jaspercompiler is used in [AbraFlexi-Report-Tools](https://github.com/VitexSoftware/AbraFlexi-Report-Tools) by its report uploader **upreport** 

Usage
-----

call it like this:

```shell
    jaspercompiler /path/to/report.jrxml
```
to build **/path/to/report.jasper**

also you can specify another destination (directory or filepath) as second parameter.

Installation
------------

```shell
sudo apt install lsb-release wget
echo "deb http://repo.vitexsoftware.com $(lsb_release -sc) main" | sudo tee /etc/apt/sources.list.d/vitexsoftware.list
sudo wget -O /etc/apt/trusted.gpg.d/vitexsoftware.gpg http://repo.vitexsoftware.com/keyring.gpg
sudo apt update
sudo apt install jaspercompiler
```
