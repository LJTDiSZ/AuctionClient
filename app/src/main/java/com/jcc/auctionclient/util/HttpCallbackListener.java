package com.jcc.auctionclient.util;

/**
 * Created by juyuan on 1/8/2016.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
