# LogiCut Studio Pro v1.0.002 — Signed Release Android Build

This repository builds **LogiCut Studio Pro** as a properly **release-signed Android APK (`LogiCut_v1.0.002_StudioPro_Release.apk`)** with v1 + v2 signing enabled (`minifyEnabled false` to keep the WebView JavaScript interface intact).

> **Note on Old Prebuilt APK**: The old debug-signed `prebuilt-apk/LogiCut_v3.0_StudioPro_Installable.apk` has been removed so a stale debug APK is never mistaken for the new signed release APK. Always install `LogiCut_v1.0.002_StudioPro_Release.apk` from GitHub Actions.

## Part 1: One-Time Keystore Setup (Do This First)

1. Open your GitHub repository and go to **Settings → Secrets and variables → Actions → New repository secret**.
2. Create these **3 secrets** first:
   - `KEYSTORE_PASSWORD` — choose a strong password (at least 6 characters).
   - `KEY_ALIAS` — choose a key name (for example, `logicut_release_key`).
   - `KEY_PASSWORD` — choose the key password (can be the same as `KEYSTORE_PASSWORD`).
3. Go to the **Actions** tab, select **Create Android Release Keystore (Run Once)** on the left, and click **Run workflow**.
4. When the workflow finishes (in about 20 seconds), open the run and download the **`android-release-keystore-bundle`** artifact (kept for 1 day only).
5. Unzip `android-release-keystore-bundle.zip` on your computer/phone:
   - Keep `release.jks` backed up in a safe private place.
   - Open `keystore_base64.txt`, copy the entire text inside it, and add a **4th repository secret** in **Settings → Secrets and variables → Actions**:
     - `KEYSTORE_BASE64` — paste the copied base64 string here.

## Part 2: Normal Build Steps (Every Time You Want a Signed APK)

1. **Automatic Build**: Every push to `main` or `master` automatically runs **Build Signed Release Android APK**, verifies the signature with `apksigner verify --verbose`, and uploads **`LogiCut_v1.0.002_StudioPro_Release.apk`**.
2. **Manual Build (or Optional `.aab` Play Store Bundle)**:
   - Open the **Actions** tab → select **Build Signed Release Android APK** → click **Run workflow**.
   - *(Optional)* Check **"Also build signed Android App Bundle (.aab)"** (`build_aab: true`) if you also want `LogiCut_v1.0.002_StudioPro_Release.aab`.
3. When the job finishes, scroll down to **Artifacts** on the workflow summary page and download **`LogiCut_v1.0.002_StudioPro_Release.apk`**.

## Local Command-Line Release Build (Optional)

If you build on your own computer, export the 4 environment variables first (never hardcode them in `app/build.gradle`):

```bash
export KEYSTORE_PATH="/full/path/to/release.jks"
export KEYSTORE_PASSWORD="your_keystore_password"
export KEY_ALIAS="logicut_release_key"
export KEY_PASSWORD="your_key_password"

gradle assembleRelease
```
