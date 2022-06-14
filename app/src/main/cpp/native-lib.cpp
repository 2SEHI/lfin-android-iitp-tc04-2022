#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <android/log.h>
#include "opencv2/core.hpp"
#include "common.hpp"
#define LOG(...) __android_log_print(ANDROID_LOG_INFO   , "libnav", __VA_ARGS__)

static char *buffer_status;
static char *buffer_log;
// c++20 버전업 대응
//static char *buffer_logLine;
static char *buffer_fullResult;

extern "C"
JNIEXPORT int JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_initializeModule(
        JNIEnv *env, jobject thiz) {
    LOG("Initialize 시작", "--------------------------");
    int result = lpin::opencv::Initialize(0);

    LOG("Initialize 끝", "--------------------------");
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_putBitmap(
        JNIEnv *env,
        jobject thiz,
        jobject bitmap) {

    LOG("putBitmap 시작", "--------------------------");
    AndroidBitmapInfo info;
    int ret = AndroidBitmap_getInfo(env, bitmap, &info);
    if(ret != ANDROID_BITMAP_RESULT_SUCCESS)  {
        throw "get Bitmap Info failure";
    }
    void *ptr;
    ret = AndroidBitmap_lockPixels(env, bitmap, &ptr);
    if(ret != ANDROID_BITMAP_RESULT_SUCCESS) {
        throw "lockPixels failure";
    }

    // PutImage
    // c++20 버전업 대응
//    lpin::opencv::PutImage(&ptr);
    lpin::opencv::PutImage(&ptr, info.width, info.height);
    LOG("Image size --> width: %d, height: %d", info.width, info.height);
    // unlock
    ret = AndroidBitmap_unlockPixels(env, bitmap);
    if(ret != ANDROID_BITMAP_RESULT_SUCCESS) {
        throw "unlockPixels failure";
    }
    return env->NewStringUTF("result");
}


extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_imageProcessing(
        JNIEnv *env,
        jobject thiz) {

    // 이미지 흑백처리
    lpin::opencv::Process();
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_getPtrOfString(
        JNIEnv *env,
        jobject thiz,
        jint request_code) {
    if(request_code == 0){
        LOG("buffer_status --> %s", buffer_status);
        return env->NewStringUTF(buffer_status);
    }else if(request_code == 1){
        LOG("buffer_log --> %s", buffer_log);
        return env->NewStringUTF(buffer_log);
    }else if(request_code == 2){
        LOG("buffer_fullResult --> %s", buffer_fullResult);
        return env->NewStringUTF(buffer_fullResult);
    }else{
        LOG("getPtrOfString --> error!");
        return env->NewStringUTF("error!");
    }
}

jdouble GetNumberFromMetadata(jbyteArray decodedArray, jint idx)
{
    double *decodePtr = (double *)decodedArray;

    return decodePtr[idx];
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_putMetadata(
        JNIEnv *env,
        jobject thiz,
        jbyteArray meta_data) {
    LOG("result[0] %x", GetNumberFromMetadata(meta_data, 0));
    LOG("result[1] %x", GetNumberFromMetadata(meta_data, 1));
    LOG("result[2] %x", GetNumberFromMetadata(meta_data, 2));
    LOG("result[3] %x", GetNumberFromMetadata(meta_data, 3));

    // c++20 버전업 대응
    // lpin::opencv::PutByteBlock(meta_data);
    lpin::opencv::PutByteBlock(meta_data, 32);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_putMetadata2(
        JNIEnv *env, jobject thiz, jstring byte_array) {
    const char *meta_data = env->GetStringUTFChars(byte_array, 0);

    env->ReleaseStringUTFChars(byte_array, meta_data);
    // c++20 버전업 대응
    // lpin::opencv::PutByteBlock((char *)meta_data);
    lpin::opencv::PutByteBlock((char *)meta_data, 32);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_restart(
        JNIEnv *env,
        jobject thiz) {
    lpin::opencv::Restart();

}