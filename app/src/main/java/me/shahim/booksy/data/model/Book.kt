package me.shahim.booksy.data.model

import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class Book(
    @DocumentId val isbn: String,
    val title: String,
    val author: String,
    val coverImage: String,
    val desc: String,
    val genres: String,
    val year: String,
    val pages: Int,
    val language: String,
    val publisher: String,
    val publishSentence: String,
    val bookLink: String,
    val authorImage: String,
    val authorAbout: String
)
