#!/bin/bash

PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
IMAGES_DIR="$PROJECT_DIR/src/main/resources/images"

mkdir -p "$IMAGES_DIR"

# Crear un PNG simple usando ImageMagick
convert -size 1024x1024 xc:white \
        -fill '#2196F3' \
        -draw 'circle 512,512 512,912' \
        -pointsize 400 \
        -fill white \
        -gravity center \
        -draw "text 0,0 '$'" \
        "$IMAGES_DIR/icon.png"

echo "Icono PNG creado en: $IMAGES_DIR/icon.png"