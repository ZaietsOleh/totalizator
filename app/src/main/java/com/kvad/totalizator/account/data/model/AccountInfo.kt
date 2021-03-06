package com.kvad.totalizator.account.data.model

import com.google.gson.annotations.SerializedName

data class AccountInfo(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("avatarLink") val avatarLink: String,
)
