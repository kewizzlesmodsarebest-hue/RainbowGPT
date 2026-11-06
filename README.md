# RainbowGPT Android
Independent rainbow-themed Android chat app. Not an official OpenAI app or ChatGPT plugin.

## Build an APK with GitHub (no Android Studio required)
1. Create a new private GitHub repository.
2. Upload all project files, including the hidden `.github/workflows/build.yml` file (extract ZIP first).
3. Open repository **Actions** > **Build Android APK** > **Run workflow** (or push changes).
4. After it completes, open the workflow run and download **RainbowGPT-APK** from Artifacts.
5. Unzip that artifact, transfer `app-debug.apk` to your Android phone, and install it.

Alternative: open the project in Android Studio and select Build > Build APK(s).

## Use
Open RainbowGPT > Settings > enter your OpenAI API key. API use is billed separately from ChatGPT subscriptions. Key is stored locally but unencrypted; use a restricted project key and avoid using the app on shared or compromised devices. Chat history is held in memory only, cleared when the app is closed.

## Notes
The debug APK is for personal testing, not Play Store distribution. Build has not been verified in this environment because Android SDK/Gradle and network access are unavailable.
