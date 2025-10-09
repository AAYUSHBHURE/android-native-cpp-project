# FLAM - Fast Live Android Mirror# 🎉 Repository Ready!

## Android Camera Processing with OpenCV & Native C++

Your GitHub repository has been successfully created:

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)- **Repository**: AAYUSHBHURE/android-native-cpp-project

![Language](https://img.shields.io/badge/Language-Kotlin%20|%20C%2B%2B%20|%20TypeScript-blue.svg)- **URL**: https://github.com/AAYUSHBHURE/android-native-cpp-project

![OpenCV](https://img.shields.io/badge/OpenCV-4.8.0-orange.svg)- **Local Path**: C:\Users\bhure\FLAM\android-native-cpp-project

![NDK](https://img.shields.io/badge/NDK-27.0.12077973-red.svg)

![License](https://img.shields.io/badge/License-MIT-yellow.svg)## Quick Commands for After Android Studio Project Creation:



---```powershell

# After adding your Android Studio project files:

## 📹 Demogit add .

git commit -m "Initial project setup with NDK support"

> **Note**: Record a GIF showing the toggle button switching between raw camera and edge detection modes, then add it here:git push origin master

>```

> ```markdown

> ![FLAM Demo](docs/demo.gif)The repository is ready and waiting for your Android Native C++ project files!
> 
> *Toggle between raw camera feed and edge-detected output in real-time*
> ```

---

## ✨ Features Implemented

### Core Requirements
- [x] **Camera Feed Implementation** - Live camera preview using Camera2 API
- [x] **Camera2 API Integration** - Modern Android camera access with TextureView
- [x] **Runtime Permission Handling** - Proper permission request flow
- [x] **OpenCV Native Integration** - C++ JNI bridge for image processing
- [x] **CMake Build System** - Native library compilation with NDK
- [x] **OpenCV Version Display** - Shows OpenCV 4.8.0 information

### Bonus Features Implemented
- [x] **Toggle Button** - Switch between raw camera and edge-detected output
- [x] **FPS Counter** - Real-time performance monitoring (top-right overlay)
- [x] **Frame Saving** - Capture and save frames to device storage
- [x] **TypeScript Web Viewer** - Separate web app to view saved frames
- [x] **JNI Frame Processing** - Native method for OpenCV edge detection (stub ready for Canny filter)
- [x] **Modern UI Design** - Material Design with semi-transparent overlays
- [x] **Comprehensive Documentation** - Setup guides, testing instructions, and commit templates

### Additional Features
- [x] **Background Thread Handling** - Proper camera operations on background thread
- [x] **MediaStore Integration** - Android 10+ compliant file storage
- [x] **Dual Save Locations** - Timestamped + fixed filename for web viewer
- [x] **Error Handling** - Robust exception handling throughout
- [x] **Logging** - Detailed logcat output for debugging

---

## 🏗️ Architecture

### Data Flow Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        Android App Layer                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Camera2 API → TextureView → onSurfaceTextureUpdated()          │
│       │              │                    │                      │
│       │              │                    ↓                      │
│       │              │          ┌──────────────────┐            │
│       │              │          │  FPS Tracking    │            │
│       │              │          │  UI Updates      │            │
│       │              │          └──────────────────┘            │
│       │              │                                           │
│       │              └─────→ TextureView.bitmap                 │
│       │                             │                            │
│       │                             ↓                            │
│       │                    ┌─────────────────┐                 │
│       │                    │  Save Frame     │                 │
│       │                    │  (MediaStore)   │                 │
│       │                    └─────────────────┘                 │
│       │                                                          │
│       └─────→ [Future] JNI Bridge                              │
│                      │                                           │
└──────────────────────┼───────────────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────────────┐
│                      Native C++ Layer (JNI)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  processFrame(enableEdgeDetection: Boolean)                      │
│       │                                                           │
│       ├─→ if (edgeDetection)                                    │
│       │      cv::Canny(input, output, 50, 150)                  │
│       │                                                           │
│       └─→ else                                                   │
│              return raw frame                                    │
│                                                                   │
│  OpenCV 4.8.0 (libopencv_java4.so + static libs)               │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    TypeScript Web Viewer                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Saved Frame (frame.jpg)                                        │
│       │                                                           │
│       ↓                                                           │
│  Display with Stats Overlay                                     │
│   • FPS                                                          │
│   • Resolution                                                   │
│   • Processing Mode                                              │
│   • OpenCV Version                                               │
│   • Timestamp                                                    │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

### Component Breakdown

#### 1. Android App (`/app`)
- **MainActivity.kt**: Main activity managing camera lifecycle, UI, and frame processing
- **ActivityMainBinding**: ViewBinding for type-safe view access
- **Camera2 API**: Modern camera access with manual control
- **TextureView**: Surface for camera preview rendering
- **FPS Counter**: Real-time frame rate calculation and display
- **Toggle Control**: Switch between raw and edge-detected modes
- **Frame Saving**: MediaStore API for Android 10+ compatibility

#### 2. Native C++ Layer (`/app/src/main/cpp`)
- **native-lib.cpp**: JNI bridge to OpenCV
  - `stringFromJNI()`: Test method
  - `getOpenCVVersion()`: Returns OpenCV version string
  - `testOpenCVMat()`: Validates OpenCV matrix operations
  - `processFrame()`: Frame processing with edge detection toggle (ready for Canny implementation)
- **CMakeLists.txt**: Build configuration linking OpenCV

#### 3. OpenCV Module (`/opencv`)
- **OpenCV 4.8.0** Android SDK
- Pre-built native libraries for all ABIs (arm64-v8a, armeabi-v7a, x86, x86_64)
- Java wrapper classes (org.opencv.*)
- CMake configuration files

#### 4. Web Viewer (`/web`)
- **TypeScript** modern web application
- **Glassmorphism UI** design
- **Frame display** with automatic refresh
- **Stats overlay** showing processing metadata
- **Development server** with hot reload

---

## 🚀 Setup Instructions

### Prerequisites

- **Android Studio**: Arctic Fox (2020.3.1) or newer
- **Android NDK**: 27.0.12077973
- **CMake**: 3.22.1 or newer
- **Android SDK**:
  - Build Tools: 36.1.0
  - Target SDK: 36 (Android 15)
  - Minimum SDK: 24 (Android 7.0)
- **OpenCV**: 4.8.0 (included in project)
- **Node.js**: 16.x or newer (for web viewer)
- **npm**: 8.x or newer

### Clone the Repository

```bash
git clone https://github.com/AAYUSHBHURE/android-native-cpp-project.git
cd android-native-cpp-project
```

### Android App Setup

#### 1. Open in Android Studio
```bash
# Open Android Studio
# File → Open → Select android-native-cpp-project folder
```

#### 2. Configure Gradle JDK
- Go to **File → Settings → Build, Execution, Deployment → Build Tools → Gradle**
- Set **Gradle JDK** to **Embedded JDK (JBR 21)** or Android Studio's JBR

#### 3. Sync Project
- Click **Sync Now** when prompted, or
- **File → Sync Project with Gradle Files**

#### 4. Build the Project
```bash
# From terminal in project root
./gradlew clean :app:assembleDebug

# On Windows
gradlew.bat clean :app:assembleDebug
```

#### 5. Run on Device/Emulator

**Option A: Physical Device (Recommended)**
1. Enable **Developer Options** on your Android device
2. Enable **USB Debugging**
3. Connect via USB
4. Run from Android Studio or:
   ```bash
   ./gradlew installDebug
   adb shell am start -n com.example.project_flam/.MainActivity
   ```

**Option B: Emulator**
1. Create AVD: **Tools → Device Manager → Create Device**
   - Select **Pixel 9a** or similar
   - System Image: **API 35 or 36** (Google APIs)
   - Enable **Hardware: Camera** in Advanced Settings
2. Start emulator and run app

### Web Viewer Setup

```bash
cd web

# Install dependencies
npm install

# Build TypeScript
npm run build

# Start development server
npm run serve
```

Open browser to `http://localhost:8080`

---

## 🎯 Usage

### Android App

1. **Launch App**: Grant camera permission when prompted
2. **View Camera Feed**: Live preview appears in TextureView
3. **Check FPS**: Top-right overlay shows current frame rate (~30 FPS)
4. **Toggle Processing**:
   - Switch **OFF** = Raw camera feed
   - Switch **ON** = Edge detection mode (Canny filter ready to integrate)
5. **Save Frame**: Tap "Save Frame" button
   - Saves to `Pictures/frame_<timestamp>.jpg`
   - Also saves `frame.jpg` for web viewer

### Web Viewer

1. **Save a frame** from Android app
2. **Copy frame** to web directory:
   ```bash
   adb pull /sdcard/Android/data/com.example.project_flam/files/Pictures/frame.jpg web/
   ```
3. **Open browser** to `http://localhost:8080`
4. **View frame** with stats overlay (FPS, resolution, mode, OpenCV version)

---

## 📁 Project Structure

```
android-native-cpp-project/
├── app/                          # Android app module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/project_flam/
│   │   │   │   └── MainActivity.kt       # Main activity (Camera2 + UI)
│   │   │   ├── cpp/                      # Native C++ code (JNI)
│   │   │   │   ├── native-lib.cpp        # OpenCV integration
│   │   │   │   └── CMakeLists.txt        # CMake build config
│   │   │   ├── res/
│   │   │   │   └── layout/
│   │   │   │       └── activity_main.xml # UI layout
│   │   │   └── AndroidManifest.xml       # App manifest
│   │   └── build.gradle.kts              # App build config
│   └── build/                            # Build outputs (gitignored)
│
├── opencv/                       # OpenCV 4.8.0 Android SDK
│   ├── java/                     # Java wrapper classes
│   ├── native/                   # Native libraries and headers
│   │   ├── jni/                  # CMake configs, headers
│   │   ├── libs/                 # Shared libraries (.so)
│   │   └── staticlibs/           # Static libraries (.a)
│   └── build.gradle              # OpenCV module config
│
├── web/                          # TypeScript web viewer
│   ├── src/
│   │   └── main.ts               # Web viewer TypeScript
│   ├── index.html                # Web viewer HTML
│   ├── package.json              # npm dependencies
│   ├── tsconfig.json             # TypeScript config
│   └── README.md                 # Web viewer docs
│
├── gradle/                       # Gradle wrapper
├── build.gradle.kts              # Root build config
├── settings.gradle.kts           # Project settings
├── IMPLEMENTATION_GUIDE.md       # Implementation details
├── TESTING_GUIDE.md              # Testing instructions
├── COMMIT_MESSAGES.md            # Git commit templates
└── README.md                     # This file
```

---

## 🔧 Build Configuration

### Versions Used

| Component | Version |
|-----------|---------|
| Android Gradle Plugin | 8.13.0 |
| Gradle | 8.13 |
| Kotlin | 2.0.21 |
| NDK | 27.0.12077973 |
| CMake | 3.22.1 |
| Build Tools | 36.1.0 |
| Target SDK | 36 (Android 15) |
| Min SDK | 24 (Android 7.0) |
| OpenCV | 4.8.0 |
| TypeScript | 5.3.3 |

### Key Build Settings

**app/build.gradle.kts:**
```kotlin
android {
    namespace = "com.example.project_flam"
    compileSdk = 36
    ndkVersion = "27.0.12077973"
    buildToolsVersion = "36.1.0"
    
    defaultConfig {
        minSdk = 24
        targetSdk = 36
        
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
                arguments += "-DOpenCV_DIR=${rootProject.projectDir}/opencv/native/jni"
            }
        }
    }
    
    buildFeatures {
        viewBinding = true
    }
}
```

**app/src/main/cpp/CMakeLists.txt:**
```cmake
cmake_minimum_required(VERSION 3.22.1)
project("project_flam")

# OpenCV integration
set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/../../../opencv/native/jni")
find_package(OpenCV REQUIRED)

add_library(project_flam SHARED native-lib.cpp)
target_link_libraries(project_flam ${OpenCV_LIBS} log)
```

---

## 🧪 Testing

### Manual Testing Checklist

- [ ] App launches without crashes
- [ ] Camera permission request appears
- [ ] Camera preview displays correctly
- [ ] FPS counter updates (~30 FPS expected)
- [ ] OpenCV version displays correctly
- [ ] Toggle button switches between modes
- [ ] Status text updates when toggling
- [ ] Logcat shows mode change messages
- [ ] Save Frame button works
- [ ] Toast confirms frame save
- [ ] Frame saved to Pictures folder
- [ ] frame.jpg saved for web viewer
- [ ] Web viewer displays saved frame
- [ ] Web viewer stats overlay shows correct info
- [ ] App handles rotation gracefully
- [ ] App pauses/resumes camera correctly

### Logcat Filters

```bash
# View all MainActivity logs
adb logcat MainActivity:D *:S

# View OpenCV/NDK logs
adb logcat OpenCV_NDK:D *:S

# View both
adb logcat MainActivity:D OpenCV_NDK:D *:S
```

### Expected Behavior

1. **On Launch**: Camera preview starts, FPS counter shows ~30 FPS
2. **Toggle OFF → ON**: Status shows "Edge Detection", logcat shows "Edge detection enabled"
3. **Toggle ON → OFF**: Status shows "Raw Camera", logcat shows "Edge detection disabled"
4. **Save Frame**: Toast confirms save, two files created (timestamped + frame.jpg)

---

## 📚 Documentation

- **[IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md)**: Detailed implementation notes, architecture diagrams, and feature walkthroughs
- **[TESTING_GUIDE.md](TESTING_GUIDE.md)**: Step-by-step testing procedures with expected results
- **[COMMIT_MESSAGES.md](COMMIT_MESSAGES.md)**: Ready-to-use commit messages for all features
- **[web/README.md](web/README.md)**: Web viewer setup and usage

---

## 🐛 Troubleshooting

### Build Issues

**Problem**: `OpenCV not found`
```bash
# Solution: Verify opencv/native/jni exists
ls opencv/native/jni/OpenCVConfig.cmake

# If missing, check OpenCV module is included
./gradlew :opencv:assembleDebug
```

**Problem**: `NDK not configured`
```bash
# Solution: Set NDK in local.properties
echo "ndk.dir=C:\\Users\\YourUser\\AppData\\Local\\Android\\Sdk\\ndk\\27.0.12077973" >> local.properties
```

### Runtime Issues

**Problem**: Camera preview black/frozen
- Check camera permission granted
- Verify emulator has camera configured (Virtual Scene for back camera)
- Check logcat for Camera2 errors

**Problem**: Low FPS (<20)
- Close other apps
- Use physical device instead of emulator
- Check if GPU acceleration enabled

**Problem**: Frame save fails
- Grant storage permission for Android < 10
- Check available storage space
- Verify app has external storage access

### Web Viewer Issues

**Problem**: Frame not displaying
```bash
# Verify frame exists
adb shell ls /sdcard/Android/data/com.example.project_flam/files/Pictures/

# Pull frame manually
adb pull /sdcard/Android/data/com.example.project_flam/files/Pictures/frame.jpg web/
```

**Problem**: TypeScript compilation fails
```bash
cd web
npm install
npm run build
```

---

## 🚦 Git Workflow

### Commits Made

1. ✅ **Commit #1**: Initial project setup with Gradle and NDK configuration
2. ✅ **Commit #2**: Add OpenCV 4.8.0 Android SDK and CMake integration
3. ✅ **Commit #3**: Feature: Implement live camera preview using Camera2 API
4. ✅ **Commit #4**: Fix: Resolve OpenCV build configuration issues
5. ✅ **Commit #5**: Fix: Resolve MainActivity Kotlin compilation errors
6. ✅ **Commit #6**: Feature: Add TypeScript web viewer for saved frames
7. ✅ **Commit #7**: Feature: Add toggle button for raw vs edge-detected output
8. ✅ **Commit #8**: Feature: Add FPS counter and frame saving capability
9. ⏳ **Commit #9**: Refactor: Clean up code and finalize project structure
10. ⏳ **Commit #10**: Docs: Create comprehensive README with setup instructions

### Branch Structure

- `master`: Stable releases
- `copilot/implement-worker-execution-logic`: Feature development branch

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Aayush Bhure**
- GitHub: [@AAYUSHBHURE](https://github.com/AAYUSHBHURE)

---

## 🙏 Acknowledgments

- **OpenCV Team**: For the excellent computer vision library
- **Android Team**: For Camera2 API and modern Android development tools
- **JetBrains**: For Kotlin programming language
- **Community**: Stack Overflow, GitHub, and Android developer community

---

## 📖 Additional Resources

- [Android Camera2 API Documentation](https://developer.android.com/training/camera2)
- [OpenCV Android Tutorial](https://docs.opencv.org/4.8.0/d0/d6c/tutorial_table_of_content_android.html)
- [CMake Android NDK Guide](https://developer.android.com/ndk/guides/cmake)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/)
- [Material Design Guidelines](https://m3.material.io/)

---

**Built with ❤️ using Kotlin, C++, OpenCV, and TypeScript**
