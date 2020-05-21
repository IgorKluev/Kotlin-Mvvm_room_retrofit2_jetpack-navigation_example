package com.example.movies.models.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "genre",indices = arrayOf(Index( "genreName", unique = true)))
data class GenreModel(@PrimaryKey(autoGenerate = true) var gId : Long = 0,
                      @ColumnInfo(name = "genreName") var genreName : String = "",
                      @Ignore var isClicked : Boolean = false):Parcelable
{
    override fun toString(): String {
        return "Id: $gId genreName: $genreName"
    }

    override fun describeContents(): Int {
        return 0
    }
}