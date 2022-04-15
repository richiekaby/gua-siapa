package net.larntech.guasiapa.network

import net.larntech.guasiapa.model.login.LoginResponse
import net.larntech.guasiapa.share_pref.SharePref
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {


    private fun getRetrofit(): Retrofit {
        var client : OkHttpClient?;
        val logger = HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
        if(SharePref.getLoginDetails() != null) {

            var loginResponse: LoginResponse = SharePref.getLoginDetails()!!
            var token = loginResponse.loginResult!!.token

             client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(Interceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(newRequest)
                })
                .build();
        }else{
            client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();
        }


        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://story-api.dicoding.dev/")
            .client(client)
            .build();

    }


    fun getApiService(): ApiService{
        return getRetrofit().create(ApiService::class.java)
    }


}