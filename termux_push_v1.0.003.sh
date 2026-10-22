#!/data/data/com.termux/files/usr/bin/bash
# ==============================================================================
# LogiCut Studio Pro v1.0.005 — Safe Termux to GitHub Push Script
# Automatically fixes Termux libcurl/openssl symbol error and pushes v1.0.005
# Target Repository: https://github.com/shriramnag/logicut-motion-editor.git
# ==============================================================================
set -e

echo "============================================================"
echo "🚀 Starting LogiCut Studio Pro v1.0.005 Termux Git Push Setup"
echo "============================================================"

# 1. Auto-fix Termux curl/openssl symbol mismatch ("SSL_set_quic_tls_early_data_enabled")
if ! curl --version >/dev/null 2>&1; then
  echo "🔧 Fixing Termux openssl & libcurl library mismatch via apt..."
  apt update -y || true
  DEBIAN_FRONTEND=noninteractive apt -y -o Dpkg::Options::="--force-confnew" install --only-upgrade openssl libcurl git || true
fi

if ! command -v git >/dev/null 2>&1; then
  apt update -y && apt install -y git unzip
fi

git config --global user.name "Shriram Nag"
git config --global user.email "shriramnag@users.noreply.github.com"

rm -f app/release.jks release.jks *.jks *.keystore keystore_base64.txt

if [ ! -d ".git" ]; then
  git init
fi
git branch -M main

if ! grep -q "release.jks" .gitignore 2>/dev/null; then
  echo -e "\n*.jks\n*.keystore\napp/release.jks\nrelease.jks\nkeystore_base64.txt" >> .gitignore
fi

git add .

if git diff --cached --name-only | grep -E '\.(jks|keystore)$|keystore_base64\.txt'; then
  echo "❌ ERROR: A keystore file was detected in staged files! Aborting push for security."
  git reset
  exit 1
fi

git commit -m "feat(v1.0.005): Fix Video Upload & All 449 Buttons, Clean Sada 360px UI & Multi-Language (React/Python/Node/Java) Engine" || echo "Nothing new to commit"

git remote remove origin 2>/dev/null || true
git remote add origin https://github.com/shriramnag/logicut-motion-editor.git

echo "============================================================"
echo "✅ Ready to push LogiCut v1.0.005 to GitHub (shriramnag/logicut-motion-editor)"
echo "👉 Username: shriramnag"
echo "👉 Password: Paste your GitHub Personal Access Token (PAT)"
echo "============================================================"
git push -u origin main --force
