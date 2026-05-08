@REM ----------------------------------------------------------------------------
@REM Maven Wrapper for Windows
@REM ----------------------------------------------------------------------------
@IF "%MAVEN_TERMINATE_CMD%"=="on" setlocal enabledelayedexpansion
@set ERROR_CODE=0
@set MAVEN_DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@if not exist "%USERPROFILE%\.m2\wrapper\maven-wrapper.jar" (
    mkdir "%USERPROFILE%\.m2\wrapper"
    echo Downloading Maven Wrapper...
    powershell -Command "Invoke-WebRequest -Uri %MAVEN_DOWNLOAD_URL% -OutFile %USERPROFILE%\.m2\wrapper\maven-wrapper.jar"
)

@set MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
@set MAVEN_OPTS=-Xmx1024m

"%MAVEN_JAVA_EXE%" %MAVEN_OPTS% -classpath "%USERPROFILE%\.m2\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*