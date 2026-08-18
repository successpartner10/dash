#!/usr/bin/env bash
# Copy the web app (repo root) into Capacitor's www/, then sync to platforms.
set -e
cd "$(dirname "$0")"
cp ../index.html ../manifest.json ../sw.js ../icon-192.png ../icon-512.png ../icon-180.png www/
npx cap sync android
echo "Synced. Open Android Studio: npx cap open android"
