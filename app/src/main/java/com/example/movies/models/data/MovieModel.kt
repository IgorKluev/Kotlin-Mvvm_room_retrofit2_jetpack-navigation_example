package com.example.movies.models.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movies",indices = arrayOf(Index( "title", unique = true)))
data class MovieModel(@PrimaryKey(autoGenerate = true) var id : Long = 0,
                      @ColumnInfo(name = "title") @SerializedName("title") var title : String = "",
                      @ColumnInfo(name = "image") @SerializedName("image") var image : String? = null,
                      @ColumnInfo(name = "rating") @SerializedName("rating") var rating : Double? = 0.0,
                      @ColumnInfo(name = "releaseYear") @SerializedName("releaseYear") var releaseYear : Int? = 0,
                      @Ignore @SerializedName("genre") var genreString : List<String> = ArrayList()) : Parcelable
{

    override fun toString(): String {
        return  "Id:$id Title: $title \nimgUrl: $image\nRating: $rating\nRelease Year: $releaseYear\nGenre: $genreString"
    }

    override fun describeContents(): Int {
        return 0
    }

}