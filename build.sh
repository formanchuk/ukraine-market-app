#!/bin/bash

echo "🚀 Початок збірки UAMO Android..."

# Крок 1: Копіюємо веб-файли
echo "📦 Копіювання веб-файлів..."
cp -r ../uamo-web/* www/

# Крок 2: Синхронізація з Capacitor
echo "🔄 Синхронізація з Capacitor..."
npx cap sync android

# Крок 3: Відкриваємо в Android Studio
echo "📱 Відкриття в Android Studio..."
npx cap open android

echo "✅ Готово!"
