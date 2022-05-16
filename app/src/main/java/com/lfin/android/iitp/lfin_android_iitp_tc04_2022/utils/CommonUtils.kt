package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils

import java.util.*


    /**
     * convert String to base64
     */
    fun encodeBase64(oriString: String): String = Base64.getEncoder().encodeToString(oriString.toByteArray())

    /**
     * convert base64 to byteArray
     */
    fun decodeBase64(encodedStr: String): String = String(Base64.getDecoder().decode(encodedStr))
