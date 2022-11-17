package com.anil.tmdbpopularmovies.data.remote


import com.anil.tmdbpopularmovies.util.AppConstants
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptors @Inject constructor():Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original:Request=chain.request()
        val originalHttpURL:HttpUrl=original.url

        val url=originalHttpURL.newBuilder().
        addQueryParameter(AppConstants.API_PARAM, AppConstants.API_KEY)
            .build()
        val requestBuilder:Request.Builder=original.newBuilder().url(url = url)

        val request:Request=requestBuilder.build()

        return chain.proceed(request = request)


    }
}