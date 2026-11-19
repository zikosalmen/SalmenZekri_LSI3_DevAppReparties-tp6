@echo off
chcp 65001 >nul
echo ========================================================
echo  [2/3] Demarrage du Serveur CORBA BanqueServer
echo ========================================================

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.512.9-hotspot"
set "JAVA=%JAVA_HOME%\bin\java.exe"

cd /d "%~dp0TP6_CorbaBanque_Server"
"%JAVA%" -cp "bin;src\main\resources" corbaServer.BanqueServer
pause
