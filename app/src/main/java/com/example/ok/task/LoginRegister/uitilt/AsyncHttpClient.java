package com.example.ok.task.LoginRegister.uitilt;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;


/**
 *     10.0.3.2  http://aqar.esy.es/  http://android.alatheertech.com/
 */
public class AsyncHttpClient {
    private static final String BASE_URL = "http://android.alatheertech.com/";

    public static com.loopj.android.http.AsyncHttpClient syncHttpClient  = new SyncHttpClient();
    public static com.loopj.android.http.AsyncHttpClient asyncHttpClient = new com.loopj.android.http.AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        syncHttpClient.setResponseTimeout(90000);
        asyncHttpClient.setTimeout(90000);
        getClient().post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static com.loopj.android.http.AsyncHttpClient getClient()
    {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }
}
