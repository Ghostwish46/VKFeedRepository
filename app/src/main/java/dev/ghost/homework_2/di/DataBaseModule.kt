package dev.ghost.homework_2.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.ghost.homework_2.model.database.AppDatabase
import javax.inject.Singleton

@Module
class DataBaseModule {
    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "NewsDatabase"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providePostDao(appDatabase: AppDatabase) = appDatabase.postDao

    @Provides
    @Singleton
    fun provideGroupDao(appDatabase: AppDatabase) = appDatabase.groupDao

    @Provides
    @Singleton
    fun providePhotoDao(appDatabase: AppDatabase) = appDatabase.photoDao

    @Provides
    @Singleton
    fun provideProfileDao(appDatabase: AppDatabase) = appDatabase.profileDao


    @Provides
    @Singleton
    fun provideCommentDao(appDatabase: AppDatabase) = appDatabase.commentDao

}
