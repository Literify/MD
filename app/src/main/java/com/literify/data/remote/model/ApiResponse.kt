package com.literify.data.remote.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("data")
    val data: MessageData? = null
)

data class BookResponse (
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("authors")
    val authors: String? = null,

    @field:SerializedName("genre")
    val genre: String? = null,

    @field:SerializedName("publisher")
    val publisher: String? = null,

    @field:SerializedName("price")
    val price: Double? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("previewLink")
    val previewLink: String? = null,

    @field:SerializedName("infoLink")
    val infoLink: String? = null,

    @field:SerializedName("averageRating")
    val averageRating: Double? = null,

    @field:SerializedName("ratingCount")
    val ratingCount: Int? = null,

    @field:SerializedName("weightedAverage")
    val weightedAverage: Double? = null,

    @field:SerializedName("bagOfWords")
    val bagOfWords: String? = null,
)

data class UserResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("firstName")
    val firstName: String? = null,

    @field:SerializedName("lastName")
    val lastName: String? = null,

    @field:SerializedName("level")
    val level: String? = null,

    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null

)

data class GamificationResponse(

    @field:SerializedName("eventTimes")
    val eventTimes: List<Long?>? = null,

    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("achievements")
    val achievements: Map<String, Any?>? = null,

    @field:SerializedName("coolOff")
    val coolOff: Any? = null,

    @field:SerializedName("errorCount")
    val errorCount: Int? = null
)

data class LeaderboardResponse(
    @field:SerializedName("leaderboard")
    val leaderboard: List<LeaderboardList?>? = null,
)

data class LeaderboardList(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("score")
    val score: Int? = null
)

data class RankUpResponse(
    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("rank")
    val rank: Int? = null,

    @field:SerializedName("rankUpImageUrl")
    val rankUpImageUrl: String? = null
)

data class StreakResponse(
    @field:SerializedName("streak")
    val streak: Int? = null,

    @field:SerializedName("lastCompletionDate")
    val lastCompletionDate: String? = null
)

data class PredictionResponse(
    @field:SerializedName("fileUrl")
    val fileUrl: String? = null,

    @field:SerializedName("prediction")
    val prediction: String? = null
)

data class PredictionList(
    @field:SerializedName("extracted_text")
    val extractedText: String?,

    @field:SerializedName("predicted_genre")
    val predictedGenre: String?
)

sealed class MessageData {
    data class GamificationResponseData(val gamificationResponse: GamificationResponse): MessageData()
    data class LeaderboardResponseData(val leaderboardResponse: LeaderboardResponse): MessageData()
    data class RankUpResponseData(val rankUpResponse: RankUpResponse): MessageData()
    data class StreakResponseData(val streakResponse: StreakResponse): MessageData()
    data class PredictionResponseData(val predictionResponse: PredictionResponse): MessageData()
}



