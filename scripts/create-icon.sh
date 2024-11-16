#!/bin/bash

# Obtener la ruta absoluta del directorio del proyecto
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
ICON_SOURCE="$PROJECT_DIR/src/main/resources/images/icon.png"
ICON_SET_DIR="$PROJECT_DIR/icon.iconset"
RESOURCES_DIR="$PROJECT_DIR/src/main/resources"

echo "Creando iconset en: $ICON_SET_DIR"

# Crear directorio para iconset si no existe
mkdir -p "$ICON_SET_DIR"

# Crear diferentes tamaños de icono
sips -z 16 16     "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_16x16.png"
sips -z 32 32     "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_16x16@2x.png"
sips -z 32 32     "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_32x32.png"
sips -z 64 64     "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_32x32@2x.png"
sips -z 128 128   "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_128x128.png"
sips -z 256 256   "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_128x128@2x.png"
sips -z 256 256   "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_256x256.png"
sips -z 512 512   "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_256x256@2x.png"
sips -z 512 512   "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_512x512.png"
sips -z 1024 1024 "$ICON_SOURCE" --out "$ICON_SET_DIR/icon_512x512@2x.png"

# Crear el archivo .icns
iconutil -c icns "$ICON_SET_DIR"

# Mover el icono al directorio de recursos
mv icon.icns "$RESOURCES_DIR/"

# Limpiar archivos temporales
rm -rf "$ICON_SET_DIR"

echo "Icono creado exitosamente en: $RESOURCES_DIR/icon.icns"