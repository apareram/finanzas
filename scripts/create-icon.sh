#!/bin/bash

# Obtener la ruta absoluta del directorio del proyecto
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
RESOURCES_DIR="$PROJECT_DIR/src/main/resources"
IMAGES_DIR="$RESOURCES_DIR/images"

# Crear un icono temporal usando iconutil (más simple)
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">
<plist version=\"1.0\">
<dict>
    <key>CFBundleIconFile</key>
    <string>AppIcon</string>
</dict>
</plist>" > "$RESOURCES_DIR/Info.plist"

# Crear un icono simple usando png2icns (necesita instalar libicns)
brew install libicns
png2icns "$RESOURCES_DIR/icon.icns" "$IMAGES_DIR/icon.png"

echo "Icono creado exitosamente en: $RESOURCES_DIR/icon.icns"