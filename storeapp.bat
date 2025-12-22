@echo off
java --enable-native-access=ALL-UNNAMED -jar "%~dp0target\storeapp-1.0-SNAPSHOT.jar" %*
