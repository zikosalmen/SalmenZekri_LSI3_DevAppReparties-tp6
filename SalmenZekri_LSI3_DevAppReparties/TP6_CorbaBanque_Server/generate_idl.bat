@echo off
chcp 65001 >nul
echo ========================================================
echo   Generation des stubs/skeletons CORBA a partir de Banque.idl
echo ========================================================

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.512.9-hotspot"
set "IDLJ=%JAVA_HOME%\bin\idlj.exe"

if not exist "%IDLJ%" (
    echo [ERREUR] idlj.exe introuvable dans %JAVA_HOME%\bin
    pause
    exit /b 1
)

cd /d "%~dp0src\main\java"
echo Compilation de Banque.idl avec idlj -fall...
"%IDLJ%" -fall Banque.idl

if %ERRORLEVEL% equ 0 (
    echo [SUCCES] Classes generees avec succes dans le package corbaBanque !
) else (
    echo [ERREUR] Echec lors de la generation avec idlj.
)

pause
