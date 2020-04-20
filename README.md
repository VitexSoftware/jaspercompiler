Commandline Jasper Compiler
===========================

Simple tool able to comple FlexiBee customs reports into given destination

Usage
-----

call it like this:

```shell
    jaspercompiler /path/to/report.jrxml
```
to build **/path/to/report.rasper**

also you can specify another destination (directory or filepath) as second parameter.



Installation
------------

```shell
sudo apt install lsb-release wget
echo "deb http://repo.vitexsoftware.cz $(lsb_release -sc) main" | sudo tee /etc/apt/sources.list.d/vitexsoftware.list
sudo wget -O /etc/apt/trusted.gpg.d/vitexsoftware.gpg http://repo.vitexsoftware.cz/keyring.gpg
sudo apt update
sudo apt install jaspercompiler
```