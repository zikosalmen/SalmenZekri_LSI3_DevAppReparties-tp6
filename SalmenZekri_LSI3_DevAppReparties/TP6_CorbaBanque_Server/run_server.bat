@echo off
chcp 65001 >nul
echo ========================================================
echo   Demarrage du Serveur CORBA BanqueServer
echo ========================================================

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.512.9-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

cd /d "%~dp0"
echo Lancement de BanqueServer via Maven...
call mvn exec:java -Dexec.mainClass="corbaServer.BanqueServer"
pause
