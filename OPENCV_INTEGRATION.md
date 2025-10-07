# OpenCV Android Integration Guide

## Overview
This document describes the integration of OpenCV 4.8.0 Android SDK into the Project FLAM Android Native C++ project.

## Integration Steps Completed

### 1. OpenCV SDK Download and Setup
- **OpenCV Version**: 4.8.0
- **Download Source**: Official OpenCV GitHub releases
- **Location**: `./opencv/` directory in project root

### 2. Project Configuration

#### settings.gradle.kts
```kotlin
include(":opencv")
```

#### app/build.gradle.kts
- Added OpenCV module dependency: `implementation(project(":opencv"))`
- Configured NDK ABI filters: `arm64-v8a`, `armeabi-v7a`
- Maintained C++17 standard

#### app/src/main/cpp/CMakeLists.txt
```cmake
# Set OpenCV path
set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/../../../opencv/native/jni)

# Find OpenCV package
find_package(OpenCV REQUIRED)

# Include OpenCV headers
include_directories(${OpenCV_INCLUDE_DIRS})

# Link OpenCV libraries
target_link_libraries(${CMAKE_PROJECT_NAME}
    ${OpenCV_LIBS}
    android
    log)
```

### 3. Native Code Implementation

#### New JNI Functions Added:
1. **getOpenCVVersion()**: Returns OpenCV version string
2. **testOpenCVMat()**: Creates and tests OpenCV Mat object

#### Features:
- OpenCV header inclusion: `#include <opencv2/opencv.hpp>`
- Android logging for debugging
- Error handling for OpenCV operations

### 4. Java/Kotlin Integration

#### MainActivity.kt Updates:
- Added external function declarations for new native methods
- Updated UI to display OpenCV version and test results
- Combined all test outputs in the main TextView

## Directory Structure
```
android-native-cpp-project/
├── app/
│   ├── src/main/cpp/
│   │   ├── native-lib.cpp        # Updated with OpenCV functions
│   │   └── CMakeLists.txt        # Configured for OpenCV linking
│   └── build.gradle.kts          # Added OpenCV dependency
├── opencv/                       # OpenCV Android SDK
│   ├── build.gradle              # OpenCV module configuration
│   ├── java/                     # OpenCV Java APIs
│   └── native/                   # OpenCV native libraries
│       ├── jni/                  # CMake configuration files
│       ├── libs/                 # Compiled OpenCV libraries
│       └── staticlibs/           # Static libraries
├── downloads/                    # Download artifacts (can be removed)
└── settings.gradle.kts          # Updated to include OpenCV module
```

## Testing Functions

### Native Functions Available:
1. `stringFromJNI()` - Original test function
2. `getOpenCVVersion()` - Returns OpenCV version
3. `testOpenCVMat()` - Tests OpenCV Mat creation (returns 9 for 3x3 matrix)

### Expected Output:
```
Hello from C++
OpenCV Version: 4.8.0
Mat Test Result: 9
```

## Build Configuration

### Supported ABIs:
- `arm64-v8a` (64-bit ARM)
- `armeabi-v7a` (32-bit ARM)

### Build Requirements:
- CMake 3.22.1+
- NDK r23+
- Android API 24+
- C++17 Standard

## Troubleshooting

### Common Issues:
1. **CMake OpenCV Not Found**: Verify OpenCV path in CMakeLists.txt
2. **Library Linking Errors**: Check ABI filters match OpenCV libraries
3. **Missing Headers**: Ensure OpenCV include directories are correct

### Debug Logs:
- Use `adb logcat | grep OpenCV_NDK` to view native debug output
- Check Android Studio Build output for CMake errors

## Next Steps

1. **Build and Test**: Run the project to verify integration
2. **Advanced Features**: Implement image processing functions
3. **Camera Integration**: Add camera preview and OpenCV processing
4. **Performance Optimization**: Configure release builds

---
*OpenCV Android SDK Integration - Project FLAM*
*Date: October 7, 2025*