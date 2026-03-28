@echo off
echo Stopping Payara Server...
taskkill /F /IM java.exe /FI "WINDOWTITLE eq *payara*" 2>nul
timeout /t 3 /nobreak >nul

echo Cleaning target directory...
rmdir /s /q target 2>nul

echo Building project...
call mvn clean package

echo.
echo Done! Now start Payara Server from NetBeans and run the project.
pause
