package com.test.cleanarchitecture.data.remote.dto

import com.google.gson.annotations.SerializedName


data class ApiContact(
    val name: Name?,
    val email: String?,
    val picture: Picture?
)

data class Name(
    @SerializedName("first")
    val firstName: String?,
    @SerializedName("last")
    val lastName: String?,
    val picture: Picture?
)

data class Picture(
    val large: String?,
    val medium: String?,
    val thumbnail: String?
)