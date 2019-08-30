package com.kebaranas.croppynet.models

import com.google.firebase.firestore.PropertyName

data class AccountBasicInfo(
    @PropertyName("userName")
    val userName: String,

    @PropertyName("accountType")
    val accountType: String,

    @PropertyName("firstName")
    val firstName: String,

    @PropertyName("lastName")
    val lastName: String,

    @PropertyName("birthYear")
    val birthYear: Int,

    @PropertyName("birthMonth")
    val birthMonth: Int,

    @PropertyName("birthDay")
    val birthDay: Int,

    @PropertyName("sex")
    val sex: String
)