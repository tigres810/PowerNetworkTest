@echo on
SET var=%cd%
SET version="1.0"
IF exist %var%\build\libs\modid-1.0.jar (
  ren "%var%\build\libs\modid-1.0.jar" "TestMod-1.16.5-%version%.jar"
  cd %var%\build\libs\
  start .
 ) ELSE (
 echo "No se encontro el archivo especificado."
)