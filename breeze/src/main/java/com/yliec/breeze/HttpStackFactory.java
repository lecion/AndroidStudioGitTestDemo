package com.yliec.breeze;

import android.os.Build;

/**
 * @Author Lecion
 * @Date 6/16/15
 * @Email lecion@icloud.com
 */
public class HttpStackFactory {
    public static HttpStack createHttpStack() {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 9) {
            return new HttpUrlConnectionStack();
        }
        return new HttpClientStack();
    }
}
