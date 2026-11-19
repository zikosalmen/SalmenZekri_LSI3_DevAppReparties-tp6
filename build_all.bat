@echo off
chcp 65001 >nul
echo ========================================================
echo   COMPILATION COMPLETE DU TP6 CORBA (SERVEUR & CLIENT)
echo ========================================================

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.512.9-hotspot"
set "JAVAC=%JAVA_HOME%\bin\javac.exe"
set "IDLJ=%JAVA_HOME%\bin\idlj.exe"

if not exist "%JAVAC%" (
    echo [ERREUR] JDK 1.8 introuvable dans %JAVA_HOME%
    pause
    exit /b 1
)

echo [1/4] Compilation IDL Serveur...
cd /d "%~dp0TP6_CorbaBanque_Server\src\main\java"
"%IDLJ%" -fall -v Banque.idl

echo [2/4] Compilation IDL Client...
cd /d "%~dp0TP6_CorbaBanque_Client\src\main\java"
"%IDLJ%" -fall -v Banque.idl

echo [3/4] Compilation des classes Java Serveur...
cd /d "%~dp0TP6_CorbaBanque_Server"
if not exist "bin" mkdir bin
"%JAVAC%" -encoding UTF-8 -d bin src\main\java\corbaBanque\*.java src\main\java\service\*.java src\main\java\corbaServer\*.java
copy /Y src\main\resources\jndi.properties bin\jndi.properties >nul

echo [4/4] Compilation des classes Java Client...
cd /d "%~dp0TP6_CorbaBanque_Client"
if not exist "bin" mkdir bin
"%JAVAC%" -encoding UTF-8 -d bin src\main\java\corbaBanque\*.java src\main\java\corbaClient\*.java
copy /Y src\main\resources\jndi.properties bin\jndi.properties >nul

cd /d "%~dp0"
echo.
echo ========================================================
echo [SUCCES] Compilation terminee avec succes pour JDK 1.8 !
echo ========================================================
pause
