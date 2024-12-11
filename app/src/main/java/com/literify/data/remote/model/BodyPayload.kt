package com.literify.data.remote.model

data class UserPayload(
    val id: String,
    val email: String,
    val username: String?,
    val firstName: String,
    val lastName: String,
    val level: String?
)

data class GamificationPayload(
    val points: Int?,
    val achievement: String
)