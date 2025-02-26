package com.rick.ricktour.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rick.ricktour.api.TourItem

@Dao
interface TourItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tourItems: MutableList<TourItem>)

    @Query("SELECT * FROM TourItem")
    fun getAll(): LiveData<MutableList<TourItem>>

    @Delete
    suspend fun delete(tourItem: TourItem)

    @Query("SELECT * FROM TourItem WHERE name LIKE :searchQuery")
    fun searchTourItems(searchQuery: String): LiveData<List<TourItem>>

}