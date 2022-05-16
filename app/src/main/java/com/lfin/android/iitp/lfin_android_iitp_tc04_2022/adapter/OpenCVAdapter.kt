package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter

import android.graphics.Bitmap

class OpenCVAdapter {
    companion object {
        init {
            System.loadLibrary("lfin_android_iitp_tc04_2022")
        }
        external fun initializeModule()
        external fun putBitmap(bitmap: Bitmap): String
        external fun putMetadata(byteArray: String)
        external fun imageProcessing()
        // 0:currentState 1:log 2:csvLinesForExport
        external fun getPtrOfString(requestCode: Int): String
    }
}