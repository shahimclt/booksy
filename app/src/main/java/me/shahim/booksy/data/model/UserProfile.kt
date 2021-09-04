package me.shahim.booksy.data.model

import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class UserProfile(
    @DocumentId val id: String = "",
    val email: String = "",
    val name: String = "",
    val photo: String = "",
    val ownedBooks: List<String> = listOf(),
) {
    fun doesOwnBook(id: String): Boolean {
        return ownedBooks.contains(id)
    }
}
