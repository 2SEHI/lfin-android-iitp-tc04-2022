#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_ui_MainActivity_stringFromJNI(
        JNIEnv *env, jobject thiz) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}