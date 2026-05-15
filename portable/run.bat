@echo off
REM Try to find JDK 17
set JAVA=

REM Check common locations
for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set JAVA=%%i\bin\java.exe
for /d %%i in ("%USERPROFILE%\AppData\Local\Programs\Eclipse Adoptium\jdk-17*") do set JAVA=%%i\bin\java.exe

if "%JAVA%"=="" (
    echo Please install JDK 17 from https://adoptium.net/download/
    pause
    exit
)

"%JAVA%" --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -jar monopoly-game-1.0.0.jar
pause