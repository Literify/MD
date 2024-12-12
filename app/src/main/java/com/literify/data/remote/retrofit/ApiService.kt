package com.literify.data.remote.retrofit

import com.literify.data.remote.model.BookResponse
import com.literify.data.remote.model.GamificationPayload
import com.literify.data.remote.model.MessageResponse
import com.literify.data.remote.model.GamificationResponse
import com.literify.data.remote.model.LeaderboardResponse
import com.literify.data.remote.model.RankUpResponse
import com.literify.data.remote.model.StreakResponse
import com.literify.data.remote.model.UserPayload
import com.literify.data.remote.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("csv")
    suspend fun getCSV(): Response<List<BookResponse>>

    @POST("upload-and-predict")
    suspend fun uploadAndPredict(
        @Part file: MultipartBody.Part,
    ): Response<UserResponse>

    @POST("user")
    suspend fun registerUser(
        @Body requestBody: UserPayload
    ): Response<UserResponse>

    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ): Response<UserResponse>

    @GET("user/{username}/email")
    suspend fun getUserEmail(
        @Path("username") id: String,
    ): Response<ResponseBody>

    @PUT("user")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body requestBody: UserPayload
    ): Response<MessageResponse>

    @POST("points")
    suspend fun addPoints(
        @Header("Authorization") token: String,
        @Body requestBody: GamificationPayload
    ): Response<GamificationResponse>

    @POST("achievement")
    suspend fun addAchievement(
        @Header("Authorization") token: String,
        @Body requestBody: GamificationPayload
    ): Response<GamificationResponse>

    @GET("leaderboards")
    suspend fun getLeaderboards(): Response<LeaderboardResponse>

    @POST("rankup")
    suspend fun rankUp(
        @Header("Authorization") token: String,
        @Body requestBody: GamificationPayload
    ): Response<RankUpResponse>

    @GET("trackStreak")
    suspend fun trackStreak(
        @Header("Authorization") token: String,
    ): Response<StreakResponse>
}