#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <android/log.h>
#include "opencv2/core.hpp"
#include "common.hpp"
#include "Constants.hpp"

#define LOG(...) __android_log_print(ANDROID_LOG_INFO   , "libnav", __VA_ARGS__)

static char *buffer_status;
// c++20 버전업 대응
static char *buffer_logLine;
static char *buffer_fullResult;

using Constants = lpin::opencv::Constants<3>;

extern "C"
JNIEXPORT int JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_initializeModule(
        JNIEnv *env, jobject thiz) {
    LOG("Initialize 시작", "--------------------------");
    int result = lpin::opencv::Initialize(0);
    buffer_status = lpin::opencv::GetPtrOfString(::Constants::Gimme_Status);
    buffer_logLine = lpin::opencv::GetPtrOfString(::Constants::Gimme_LogLine);
    buffer_fullResult = lpin::opencv::GetPtrOfString(::Constants::Gimme_FullResult);
//    buffer_logLine = lpin::opencv::GetPtrOfString(::Constants::Gimme_Todo);
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
    lpin::opencv::PutImage(ptr);
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
    LOG("imageProcessing", "들어옴");
    // 이미지 흑백처리
    lpin::opencv::Process();
    LOG("imageProcessing", "처리 끝남");
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
        LOG("buffer_log --> %s", buffer_logLine);
        return env->NewStringUTF(buffer_logLine);
    }else if(request_code == 2){
        LOG("buffer_fullResult --> %s", buffer_fullResult);
        return env->NewStringUTF(buffer_fullResult);
    }else{
        LOG("getPtrOfString --> error!");
        return env->NewStringUTF("error!");
    }
}

jdouble GetNumberFromMetadata(JNIEnv *env,jbyteArray decodedArray, jint idx)
{
    double *decodePtr = (double *)env->GetByteArrayElements(decodedArray, NULL);

    return decodePtr[idx];
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_putMetadata(
        JNIEnv *env,
        jobject thiz,
        jbyteArray meta_data) {
//    LOG("result[0] %f", GetNumberFromMetadata(env,meta_data, 0));
//    LOG("result[1] %f", GetNumberFromMetadata(env,meta_data, 1));
//    LOG("result[2] %f", GetNumberFromMetadata(env,meta_data, 2));
//    LOG("result[3] %f", GetNumberFromMetadata(env,meta_data, 3));

    // c++20 버전업 대응
     lpin::opencv::PutByteBlock(env->GetByteArrayElements(meta_data, NULL));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lfin_android_iitp_lfin_1android_1iitp_1tc04_12022_adapter_OpenCVAdapter_00024Companion_restart(
        JNIEnv *env,
        jobject thiz) {
    lpin::opencv::Restart();

}