package com.happylearn.routes.interceptors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.happylearn.views.HappyLearnApplication;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

public class GetAndSetSessionInterceptor implements Interceptor {
    HappyLearnApplication application;
    Activity activity;
    public GetAndSetSessionInterceptor(Activity activity) {
        this.application =  (HappyLearnApplication)activity.getApplication();
        this.activity = activity;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response loginSessionResponse = chain.proceed(chain.request());
        List<String> cookielist = loginSessionResponse.headers().values("Set-Cookie");
        //get the session cookie, if there is one
        if(cookielist.size() == 1) {
            application.setSessionCookie((cookielist.get(0).split("="))[1]);
            SharedPreferences sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
            sharedPreferences.edit().putString("SESSION",(cookielist.get(0).split("="))[1]).apply();
        }


        return  loginSessionResponse;

    }
}
