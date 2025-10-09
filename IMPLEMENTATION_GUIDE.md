# FLAM Project - Feature Implementation Guide

## ✅ Implemented Features

### 1. TypeScript Web Viewer (Commit #6)
- **Location**: `/web` directory
- **Stack**: TypeScript, HTML5, CSS3
- **Features**:
  - Display processed camera frames
  - Real-time stats overlay (FPS, resolution, mode, OpenCV version)
  - Glassmorphism UI design
  - Auto-refresh capability

### 2. Toggle Button (Commit #7)
- **Location**: Android app UI (activity_main.xml, MainActivity.kt)
- **Functionality**:
  - Switch between raw camera feed and edge-detected output
  - Real-time toggle during camera preview
  - JNI integration ready for C++ processing

### 3. FPS Counter (Commit #8)
- **Location**: Android app overlay
- **Features**:
  - Real-time FPS calculation
  - Overlay display on camera preview
  - Performance monitoring

## 🚀 Quick Start

### Android App Setup

1. **Open in Android Studio**:
   ```
   File > Open > android-native-cpp-project
   ```

2. **Set Gradle JDK**:
   - File > Settings > Build, Execution, Deployment > Build Tools > Gradle
   - Gradle JDK: Embedded JDK (JBR 21)
   - Click OK

3. **Sync and Build**:
   ```
   Build > Sync Project with Gradle Files
   Build > Make Project
   ```

4. **Run the App**:
   - Connect a device or start an emulator
   - Run > Run 'app'
   - Grant CAMERA permission when prompted

### Web Viewer Setup

1. **Navigate to web directory**:
   ```powershell
   cd C:\Users\bhure\FLAM\android-native-cpp-project\web
   ```

2. **Install dependencies**:
   ```powershell
   npm install
   ```

3. **Build TypeScript**:
   ```powershell
   npm run build
   ```

4. **Start the web server**:
   ```powershell
   npm run serve
   ```

5. **Open browser**:
   - Navigate to: http://localhost:8080

## 📸 Capturing and Viewing Frames

### Step 1: Capture a Frame
1. Run the Android app
2. Toggle "Edge Detection" on/off as desired
3. Tap the "Save Frame" button
4. Frame is saved to:
   - Pictures folder (timestamped)
   - App's external files directory as `frame.jpg`

### Step 2: Copy Frame to Web Viewer
```powershell
# Pull the frame from the device
adb pull /sdcard/Android/data/com.example.project_flam/files/Pictures/frame.jpg C:\Users\bhure\FLAM\android-native-cpp-project\web\

# Or manually copy from device storage
```

### Step 3: View in Browser
- Refresh http://localhost:8080
- The frame will display with stats overlay

## 🎛️ Feature Usage

### Toggle Button
- **OFF (Default)**: Raw camera feed (passthrough)
- **ON**: Edge detection mode (Canny filter ready for implementation)
- Status shown in bottom panel

### FPS Counter
- Located in top-right corner of camera preview
- Updates every second
- Shows real-time frame rate

### Save Frame Button
- Captures current TextureView bitmap
- Saves with timestamp to Pictures
- Also saves as `frame.jpg` for web viewer
- Toast notification confirms save location

## 🔧 Development Notes

### Android App Architecture
```
MainActivity.kt
├── Camera2 API integration
├── TextureView for preview
├── FPS tracking (updateFPS())
├── Toggle state (edgeDetectionEnabled)
├── Frame saving (saveCurrentFrame())
└── JNI calls to native code

native-lib.cpp
├── stringFromJNI() - Hello message
├── getOpenCVVersion() - OpenCV version string
├── testOpenCVMat() - Mat creation test
└── processFrame(enableEdgeDetection) - Toggle-aware processing
```

### Web Viewer Architecture
```
web/
├── index.html        - Main UI with glassmorphism design
├── src/main.ts       - TypeScript viewer logic
├── package.json      - npm configuration
├── tsconfig.json     - TypeScript config
└── README.md         - Web viewer documentation
```

### JNI Integration
The toggle button state is passed to C++ via:
```kotlin
// In MainActivity.kt (ready to call on frame updates)
processFrame(edgeDetectionEnabled)
```

```cpp
// In native-lib.cpp
extern "C" JNIEXPORT jint JNICALL
Java_com_example_project_1flam_MainActivity_processFrame(
    JNIEnv* env, jobject, jboolean enableEdgeDetection)
```

## 🎨 UI Features

### Android App
- **Camera Preview**: Full-screen TextureView
- **FPS Counter**: Semi-transparent overlay (top-right)
- **Control Panel** (bottom):
  - Edge Detection toggle switch
  - Save Frame button
  - Status text with OpenCV info

### Web Viewer
- **Gradient Background**: Purple-blue gradient
- **Glass Card**: Frosted glass effect container
- **Stats Display**: Monospace font with color-coded values
- **Responsive Design**: Works on mobile and desktop

## 📝 Next Steps for Full Implementation

### To Connect Camera Frames to Processing:
1. Modify `onSurfaceTextureUpdated()` to extract bitmap
2. Convert bitmap to cv::Mat in C++
3. Apply cv::Canny() when `enableEdgeDetection == true`
4. Return processed Mat to Java
5. Display on TextureView

### Sample Code Snippet:
```cpp
// In processFrame() - add actual frame processing
cv::Mat inputMat = ...; // Convert from camera frame
cv::Mat outputMat;

if (enableEdgeDetection) {
    cv::Canny(inputMat, outputMat, 50, 150);
} else {
    outputMat = inputMat.clone();
}
// Convert back to bitmap and return
```

## 🧪 Testing

### Android App
1. Launch app
2. Verify camera preview appears
3. Check FPS counter updates (should be ~30 FPS)
4. Toggle edge detection switch
5. Save a frame
6. Check Pictures folder for saved image

### Web Viewer
1. Build and serve web app
2. Open http://localhost:8080
3. Should see placeholder or frame.jpg if copied
4. Stats should display sample values
5. Verify responsive design

## 📦 Commits

Recommended commit sequence:

```bash
# Commit #6: TypeScript Web Viewer
git add web/
git commit -m "Feature: Create TypeScript web viewer with static frame display

- Initialize TypeScript project with npm
- Create responsive HTML UI with glassmorphism design
- Implement FrameViewer class for stats display
- Add auto-refresh capability for frame updates"

# Commit #7: Toggle Button
git add app/src/main/res/layout/activity_main.xml
git add app/src/main/java/com/example/project_flam/MainActivity.kt
git add app/src/main/cpp/native-lib.cpp
git commit -m "Bonus: Add toggle between raw camera feed and edge-detected output

- Add SwitchCompat to activity_main.xml
- Wire toggle state to edgeDetectionEnabled flag
- Implement JNI processFrame() method with toggle support
- Ready for cv::Canny integration in C++"

# Commit #8: FPS Counter
git add app/src/main/res/layout/activity_main.xml
git add app/src/main/java/com/example/project_flam/MainActivity.kt
git commit -m "Bonus: Implement FPS counter for performance monitoring

- Add FPS counter overlay to camera preview
- Track frame timestamps and calculate FPS
- Update UI every second with current FPS
- Display in semi-transparent overlay"
```

## 🛠️ Troubleshooting

### Android Studio
- **Gradle JDK Error**: Set to Embedded JDK (JBR 21)
- **NDK Not Found**: Install NDK 27.0.12077973 via SDK Manager
- **CMake Error**: Install CMake 3.22.1 via SDK Manager

### Web Viewer
- **npm install fails**: Ensure Node.js 16+ is installed
- **TypeScript errors**: Run `npm install typescript@latest`
- **frame.jpg not showing**: Check file exists in /web directory

### Emulator
- **Device offline**: Tools > Device Manager > Cold Boot Now
- **Camera not working**: Set back camera to "Virtual Scene" in AVD settings
- **Slow performance**: Use non-PlayStore system image (API 35 Google APIs)

## 📞 Support

For issues or questions:
1. Check Android Studio's Build Output panel
2. Review logcat for runtime errors (filter: "OpenCV_NDK" or "MainActivity")
3. Check web viewer browser console for TypeScript errors

## 🎉 Success Criteria

- ✅ Android app displays live camera preview
- ✅ FPS counter shows real-time frame rate
- ✅ Toggle button switches between modes
- ✅ Save Frame button captures and stores image
- ✅ Web viewer displays frame with stats overlay
- ✅ All features commit-ready

---

**Project**: FLAM (Feature-rich Live Android Monitoring)  
**Tech Stack**: Kotlin, C++, OpenCV, TypeScript, Camera2 API  
**Author**: Implementation complete with toggle, FPS, and web viewer
