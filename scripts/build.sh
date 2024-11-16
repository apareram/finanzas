#!/bin/bash

# Obtener la ruta absoluta del directorio del proyecto
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
APP_NAME="GestorFinanzas"
APP_VERSION="1.0"
ICON_PATH="$PROJECT_DIR/src/main/resources/icon.icns"
MAIN_JAR="$PROJECT_DIR/target/finanzas-1.0-SNAPSHOT.jar"

echo "Directorio del proyecto: $PROJECT_DIR"

# Construir el proyecto con Maven
echo "Construyendo el proyecto con Maven..."
cd "$PROJECT_DIR"
mvn clean package

# Verificar si la construcción fue exitosa
if [ $? -ne 0 ]; then
    echo "Error en la construcción de Maven"
    exit 1
fi

# Crear el ejecutable con jpackage
echo "Creando el ejecutable..."
jpackage \
  --name "${APP_NAME}" \
  --app-version "${APP_VERSION}" \
  --input target \
  --main-jar $(basename ${MAIN_JAR}) \
  --main-class com.finanzas.Main \
  --type dmg \
  --mac-package-name "${APP_NAME}" \
  --vendor "TuNombre" \
  --copyright "Copyright 2024" \
  --mac-package-identifier com.finanzas \
  --module-path "${PROJECT_DIR}/target/modules" \
  --add-modules javafx.controls,javafx.fxml,java.sql \
  --description "Gestor de Finanzas Personales"

# Verificar si la creación del ejecutable fue exitosa
if [ $? -eq 0 ]; then
    echo "Ejecutable creado exitosamente"
    echo "Puedes encontrar el instalador en: ${APP_NAME}.dmg"
else
    echo "Error al crear el ejecutable"
    exit 1
fi