package me.shahim.booksy.data.model

import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class Book(
    @DocumentId val id: String = "",
    val isbn: String = "",
    val title: String = "",
    val author: String = "",
    val coverImage: String = "",
    val desc: String = "",
    val genres: List<String> = listOf(),
    val year: String = "",
    val pages: Int = 0,
    val language: String = "",
    val publisher: String = "",
    val publishSentence: String = "",
    val bookLink: String = "",
    val authorImage: String = "",
    val authorAbout: String = ""
) {
    val genreString: String
        get() {
            return genres.joinToString(", ")
        }
}
