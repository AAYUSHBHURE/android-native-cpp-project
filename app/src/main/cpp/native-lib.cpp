#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <android/log.h>

#define LOG_TAG "OpenCV_NDK"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_project_1flam_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_project_1flam_MainActivity_getOpenCVVersion(
        JNIEnv* env,
        jobject /* this */) {
    std::string version = "OpenCV Version: " + cv::getVersionString();
    LOGD("OpenCV Version: %s", cv::getVersionString().c_str());
    return env->NewStringUTF(version.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_project_1flam_MainActivity_testOpenCVMat(
        JNIEnv* env,
        jobject /* this */) {
    try {
        // Create a simple 3x3 matrix
        cv::Mat mat = cv::Mat::eye(3, 3, CV_32F);
        LOGD("Created OpenCV Mat successfully: %dx%d", mat.rows, mat.cols);
        return mat.rows * mat.cols; // Should return 9
    } catch (const std::exception& e) {
        LOGD("OpenCV Mat creation failed: %s", e.what());
        return -1;
    }
}