package com.happylearn.routes;

import android.util.Log;

import com.happylearn.views.HappyLearnApplication;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

public class GetAndSetSessionInterceptor implements Interceptor {
    HappyLearnApplication application;

    public GetAndSetSessionInterceptor(HappyLearnApplication application) {
        this.application = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response loginSessionResponse = chain.proceed(chain.request());
        List<String> cookielist = loginSessionResponse.headers().values("Set-Cookie");
        //get the session cookie, if there is one
        if(cookielist.size() == 1) {
            application.setSessionCookie((cookielist.get(0).split("="))[1]);
            Log.d("SESSION_HAPPY",cookielist.get(0));
        }


        return  loginSessionResponse;

    }
}
