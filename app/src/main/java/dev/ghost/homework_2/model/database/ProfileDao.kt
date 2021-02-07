package dev.ghost.homework_2.model.database

import androidx.room.*
import dev.ghost.homework_2.model.entities.Profile
import io.reactivex.Flowable

@Dao
interface ProfileDao {
    @Query("Select * from profile")
    @Transaction
    fun getAllProfiles(): Flowable<List<Profile>>

    @Query("Select * from profile where id = :userId")
    @Transaction
    fun getUserProfile(userId: Int): Flowable<List<Profile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProfile(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProfile(profiles: List<Profile>)

    @Delete
    fun delete(profile: Profile)
}