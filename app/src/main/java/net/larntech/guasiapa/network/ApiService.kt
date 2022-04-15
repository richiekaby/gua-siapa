package net.larntech.guasiapa.network

import net.larntech.guasiapa.model.login.LoginModelRequest
import net.larntech.guasiapa.model.login.LoginResponse
import net.larntech.guasiapa.model.post_story.PostStoryModel
import net.larntech.guasiapa.model.post_story.PostStoryResponse
import net.larntech.guasiapa.model.register.RegisterModelRequest
import net.larntech.guasiapa.model.register.RegisterModelResponse
import net.larntech.guasiapa.model.stories.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

 interface ApiService {

    @POST("/v1/register")
    fun registerUser(@Body registerModelRequest: RegisterModelRequest): Call<RegisterModelResponse>

    @POST("/v1/login")
    fun loginUser(@Body loginRequest: LoginModelRequest): Call<LoginResponse>

    @GET("/v1/stories")
    suspend fun allStories(
        @Query("page") page: Int,
        @Query("size") size: Int = 10,
    ): StoriesResponse

    @Multipart
    @POST("/v1/stories")
    fun postStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part):
            Call<PostStoryResponse>



}