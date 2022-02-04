package com.happylearn.routes.interceptors;

import android.util.Log;

import com.happylearn.views.HappyLearnApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.Request;

public class SetSessionOnRequestInterceptor implements Interceptor {
    HappyLearnApplication application;

    public SetSessionOnRequestInterceptor(HappyLearnApplication application) {
        this.application = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        //if there is a cookie, set it in the request
        //else just send the request as it is
        Log.d("SESSION_COOKIE",application.getSessionCookie());

        if(application.getSessionCookie() != null) {
            Request authorizedRequest = req.newBuilder()
                    .addHeader("Cookie","JSESSIONID="+application.getSessionCookie())
                    .build();
            return chain.proceed(authorizedRequest);

        }
        else  return chain.proceed(req);
    }
}
