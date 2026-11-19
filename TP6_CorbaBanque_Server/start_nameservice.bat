@echo off
chcp 65001 >nul
echo ========================================================
echo   Demarrage du service de nommage CORBA (tnameserv)
echo ========================================================

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.512.9-hotspot"
set "TNAMESERV=%JAVA_HOME%\bin\tnameserv.exe"

if not exist "%TNAMESERV%" (
    echo [ERREUR] tnameserv.exe introuvable dans %JAVA_HOME%\bin
    pause
    exit /b 1
)

echo Demarrage de tnameserv sur le port 1050...
echo (Laissez cette fenetre ouverte)
echo.
"%TNAMESERV%" -ORBInitialPort 1050
pause
