# Android Studio Native C++ Project Setup Guide

## Project Configuration Details

### Basic Settings
- **Template**: Native C++
- **Project Name**: AndroidNativeCppProject
- **Package**: com.aayushbhure.androidnativecppproject
- **Location**: C:\Users\bhure\FLAM\android-native-cpp-project
- **Language**: Kotlin (recommended)
- **Minimum SDK**: API 24 (Android 7.0)
- **C++ Standard**: C++17

## What Will Be Created

### Project Structure:
```
android-native-cpp-project/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── cpp/           # Native C++ source files
│   │   │   │   ├── native-lib.cpp
│   │   │   │   └── CMakeLists.txt
│   │   │   ├── java/          # Java/Kotlin source
│   │   │   └── res/           # Android resources
│   │   └── test/
│   ├── build.gradle           # App-level build configuration
│   └── proguard-rules.pro
├── gradle/
├── build.gradle               # Project-level build configuration
├── gradle.properties
├── gradlew                    # Gradle wrapper (Unix)
├── gradlew.bat               # Gradle wrapper (Windows)
├── local.properties          # Local SDK paths
└── settings.gradle
```

### Key NDK Files:
- **`app/src/main/cpp/native-lib.cpp`**: Main C++ source file
- **`app/src/main/cpp/CMakeLists.txt`**: CMake build configuration
- **`app/build.gradle`**: Contains NDK configuration

### Expected NDK Configuration in build.gradle:
```gradle
android {
    compileSdk 34
    ndkVersion "25.1.8937393"
    
    defaultConfig {
        // ... other config
        
        externalNativeBuild {
            cmake {
                cppFlags "-std=c++17"
            }
        }
        ndk {
            abiFilters 'arm64-v8a', 'armeabi-v7a', 'x86', 'x86_64'
        }
    }
    
    externalNativeBuild {
        cmake {
            path "src/main/cpp/CMakeLists.txt"
            version "3.22.1"
        }
    }
}
```

## After Project Creation:

1. **Wait for Gradle Sync** to complete
2. **Verify NDK Installation** if prompted
3. **Test Build** - Run the project to ensure everything works
4. **Make Initial Commit** to Git

## Common NDK Components:
- **JNI Bridge**: Java/Kotlin ↔ C++ communication
- **CMake**: Build system for native code
- **NDK Toolchain**: Compilers and tools for Android native development
- **ABI Support**: Multiple architecture support (ARM, x86)

---
*Follow the Android Studio setup wizard with these configurations*