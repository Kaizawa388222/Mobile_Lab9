package vn.edu.vlu.lab09.data

/**
 * Data class representing a photo item in the gallery.
 * Conforms strictly to Lab 09 specification:
 * PhotoItem(val id: Int, val title: String, val author: String, val imageUrl: String)
 */
data class PhotoItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String
)
