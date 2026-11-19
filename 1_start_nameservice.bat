@echo off
chcp 65001 >nul
echo ========================================================
echo  [1/3] Demarrage de l'Annuaire CORBA CosNaming (Port 900)
echo ========================================================

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.512.9-hotspot"
set "TNAMESERV=%JAVA_HOME%\bin\tnameserv.exe"

if not exist "%TNAMESERV%" (
    echo [ERREUR] tnameserv.exe introuvable dans %JAVA_HOME%\bin
    pause
    exit /b 1
)

echo Lancement de tnameserv sur le port 900...
echo (Laissez cette fenetre ouverte)
echo.
"%TNAMESERV%" -ORBInitialPort 900
pause
