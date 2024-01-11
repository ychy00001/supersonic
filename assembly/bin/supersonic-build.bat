@echo off
setlocal
chcp 65001
set "sbinDir=%~dp0"
set "baseDir=%~dp0.."
set "buildDir=%baseDir%\build"
set "runtimeDir=%baseDir%\..\runtime"
set "pip_path=pip3"
set "service=%~1"


rem 1. build backend java modules
del /q "%buildDir%\*.tar.gz" 2>NUL
call mvn -f "%baseDir%\..\pom.xml" clean package -DskipTests

IF ERRORLEVEL 1 (
    ECHO Failed to build backend Java modules.
    EXIT /B 1
)

rem 2. move package to build
echo f|xcopy "%baseDir%\..\launchers\standalone\target\*.tar.gz" "%buildDir%\supersonic-standalone.tar.gz"

rem 3. build frontend webapp
cd "%baseDir%\..\webapp"
call start-fe-prod.bat
copy /y "%baseDir%\..\webapp\supersonic-webapp.tar.gz" "%buildDir%\"

IF ERRORLEVEL 1 (
    ECHO Failed to build frontend webapp.
    EXIT /B 1
)

rem 4. copy webapp to java classpath
cd "%buildDir%"
tar -zxvf supersonic-webapp.tar.gz
move supersonic-webapp webapp
move webapp ..\..\launchers\standalone\target\classes

rem 5. build backend python modules
if "%service%"=="pyllm" (
    echo "start installing python modules with pip: ${pip_path}"
    set requirementPath="%baseDir%/../chat/python/requirements.txt"
    %pip_path% install -r %requirementPath%
    echo "install python modules success"
)

call :BUILD_RUNTIME

:BUILD_RUNTIME
  rem 6. reset runtime
  IF EXIST "%runtimeDir%" (
      echo begin to delete dir : %runtimeDir%
      rd /s /q "%runtimeDir%"
  ) ELSE (
      echo %runtimeDir% does not exist, create directly
  )
  mkdir "%runtimeDir%"
  tar -zxvf "%buildDir%\supersonic-standalone.tar.gz" -C "%runtimeDir%"
  for /d %%f in ("%runtimeDir%\launchers-standalone-*") do (
      move "%%f" "%runtimeDir%\supersonic-standalone"
  )

  rem 7. copy webapp to runtime
  tar -zxvf "%buildDir%\supersonic-webapp.tar.gz" -C "%buildDir%"
  if not exist "%runtimeDir%\supersonic-standalone\webapp" mkdir "%runtimeDir%\supersonic-standalone\webapp"
  xcopy /s /e /h /y "%buildDir%\supersonic-webapp\*" "%runtimeDir%\supersonic-standalone\webapp"
  if not exist "%runtimeDir%\supersonic-standalone\conf\webapp" mkdir "%runtimeDir%\supersonic-standalone\conf\webapp"
  xcopy /s /e /h /y "%runtimeDir%\supersonic-standalone\webapp\*" "%runtimeDir%\supersonic-standalone\conf\webapp"
  rd /s /q "%buildDir%\supersonic-webapp"

endlocal