#!/bin/bash

# Script de compilation du projet ChessPingPong

echo "=== Compilation du projet ChessPingPong ==="

# Créer le dossier de sortie pour les classes compilées
mkdir -p bin

# Compiler tous les fichiers Java
echo "Compilation des fichiers sources..."
javac -d bin -sourcepath src $(find src -name "*.java")

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie!"
    echo "Les fichiers .class sont dans le dossier 'bin/'"
    echo ""
    echo "Pour exécuter le programme:"
    echo "  java -cp bin lancement.Main"
else
    echo "✗ Erreur de compilation"
    exit 1
fi
