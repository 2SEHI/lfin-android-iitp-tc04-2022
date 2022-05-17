#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <android/log.h>
#include "opencv2/core.hpp"
#include "common.hpp"
#define LOG(...) __android_log_print(ANDROID_LOG_INFO   , "libnav", __VA_ARGS__)

static char *buffer_status;
static char *buffer_log;
static char *buffer_fullResult;

extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_initializeModule(
        JNIEnv *env, jobject thiz) {
    lpin::opencv::Initialize(0);

    buffer_status = lpin::opencv::GetPtrOfString(0);
    buffer_log = lpin::opencv::GetPtrOfString(1);
    buffer_fullResult = lpin::opencv::GetPtrOfString(2);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_putBitmap(
        JNIEnv *env,
        jobject thiz,
        jobject bitmap) {

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
    double *ptr = (double *)decodedArray;

    return ptr[idx];
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_putMetadata(
        JNIEnv *env,
        jobject thiz,
        jstring byte_array) {
    const char *meta_data = env->GetStringUTFChars(byte_array, 0);

    size_t length = strlen(meta_data);

    env->ReleaseStringUTFChars(byte_array, meta_data);

//    double *ptr = (double *)meta_data;
//
//    // Metadata
    lpin::opencv::PutByteBlock((char *)meta_data, 32);
}

