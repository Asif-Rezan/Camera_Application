📸 PhotoCapture App
A simple Android app built with Kotlin and XML layouts that captures photos using CameraX, applies aspect ratio-based overlays (3:2, 4:3, 16:9), and saves images locally with UUID-based filenames.

⚙️ Setup Instructions
1. Clone or Import
   git clone https://github.com/Asif-Rezan/Camera_Application.git


Open the project in Android Studio.

2. Sync Gradle

Click Sync Project with Gradle Files or go to File > Sync Project with Gradle Files.

3. Build

Clean: Build > Clean Project.
Rebuild: Build > Rebuild Project.

4. Run

Connect an Android device (API 24+) or use an emulator with a camera.
Click Run > Run 'app'.
Grant camera permission when prompted.


📷 Usage

Select a photo type (ID Photo, Member Photo, Combo) using the buttons at the top.
Align the subject within the overlay (turns green when aligned).
Press "Capture" to save the photo to the app’s private storage.


🛠️ Debugging

Check Logcat (filter: CameraActivity) for permission and camera logs.
Ensure camera permission is enabled in Settings > Apps > PhotoCapture > Permissions.
For build issues, try File > Invalidate Caches / Restart.
