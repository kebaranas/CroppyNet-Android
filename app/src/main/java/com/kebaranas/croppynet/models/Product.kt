package com.kebaranas.croppynet.models

import com.google.firebase.firestore.PropertyName


data class Product(
    @PropertyName("title")
    val title: String = "",

    @PropertyName("price")
    val price: Double = 0.0,

    @PropertyName("photoUrl")
    val photoUrl: String = ""
)